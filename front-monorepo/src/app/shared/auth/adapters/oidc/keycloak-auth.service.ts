import { Injectable, computed, signal } from '@angular/core';
import { environment } from '../../../../../environments/environment';

type TokenResponse = {
  access_token: string;
  id_token?: string;
  expires_in: number;
  token_type: string;
};

@Injectable({ providedIn: 'root' })
export class KeycloakAuthService {
  private readonly accessTokenKey = 'training-catalogue.access-token';
  private readonly idTokenKey = 'training-catalogue.id-token';
  private readonly expiresAtKey = 'training-catalogue.expires-at';
  private readonly verifierKey = 'training-catalogue.pkce-verifier';
  private readonly stateKey = 'training-catalogue.oidc-state';

  readonly accessToken = signal<string | null>(sessionStorage.getItem(this.accessTokenKey));
  readonly authenticated = computed(() => this.accessToken() !== null);

  getAccessToken(): string | null {
    const token = sessionStorage.getItem(this.accessTokenKey);
    const expiresAt = Number(sessionStorage.getItem(this.expiresAtKey) ?? 0);
    if (!token) {
      this.accessToken.set(null);
      return null;
    }
    if (expiresAt > 0 && Date.now() >= expiresAt) {
      this.clearTokens();
      return null;
    }
    this.accessToken.set(token);
    return token;
  }

  async login(): Promise<void> {
    const verifier = this.randomString(96);
    const challenge = await this.sha256(verifier);
    const state = this.randomString(32);

    sessionStorage.setItem(this.verifierKey, verifier);
    sessionStorage.setItem(this.stateKey, state);

    const authorizationUrl = new URL(`${environment.oidc.authority}/protocol/openid-connect/auth`);
    authorizationUrl.searchParams.set('client_id', environment.oidc.clientId);
    authorizationUrl.searchParams.set('redirect_uri', environment.oidc.redirectUri);
    authorizationUrl.searchParams.set('response_type', 'code');
    authorizationUrl.searchParams.set('scope', environment.oidc.scope);
    authorizationUrl.searchParams.set('code_challenge', challenge);
    authorizationUrl.searchParams.set('code_challenge_method', 'S256');
    authorizationUrl.searchParams.set('state', state);

    window.location.assign(authorizationUrl.toString());
  }

  async handleRedirectCallback(): Promise<void> {
    const currentUrl = new URL(window.location.href);
    const code = currentUrl.searchParams.get('code');
    const error = currentUrl.searchParams.get('error');

    if (error) {
      throw new Error(`OIDC login failed: ${error}`);
    }
    if (!code) {
      return;
    }

    const expectedState = sessionStorage.getItem(this.stateKey);
    if (currentUrl.searchParams.get('state') !== expectedState) {
      throw new Error('OIDC state validation failed');
    }

    const verifier = sessionStorage.getItem(this.verifierKey);
    if (!verifier) {
      throw new Error('Missing PKCE verifier');
    }

    const body = new URLSearchParams();
    body.set('grant_type', 'authorization_code');
    body.set('client_id', environment.oidc.clientId);
    body.set('code', code);
    body.set('redirect_uri', environment.oidc.redirectUri);
    body.set('code_verifier', verifier);

    const response = await fetch(`${environment.oidc.authority}/protocol/openid-connect/token`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body
    });

    if (!response.ok) {
      throw new Error(await response.text());
    }

    this.storeTokens(await response.json() as TokenResponse);
    sessionStorage.removeItem(this.verifierKey);
    sessionStorage.removeItem(this.stateKey);
    window.history.replaceState({}, document.title, window.location.pathname);
  }

  logout(): void {
    const idToken = sessionStorage.getItem(this.idTokenKey);
    this.clearTokens();

    const logoutUrl = new URL(`${environment.oidc.authority}/protocol/openid-connect/logout`);
    logoutUrl.searchParams.set('post_logout_redirect_uri', environment.oidc.redirectUri);
    if (idToken) {
      logoutUrl.searchParams.set('id_token_hint', idToken);
    }
    window.location.assign(logoutUrl.toString());
  }

  private storeTokens(tokenResponse: TokenResponse): void {
    sessionStorage.setItem(this.accessTokenKey, tokenResponse.access_token);
    sessionStorage.setItem(this.expiresAtKey, String(Date.now() + tokenResponse.expires_in * 1000));
    if (tokenResponse.id_token) {
      sessionStorage.setItem(this.idTokenKey, tokenResponse.id_token);
    }
    this.accessToken.set(tokenResponse.access_token);
  }

  private clearTokens(): void {
    sessionStorage.removeItem(this.accessTokenKey);
    sessionStorage.removeItem(this.idTokenKey);
    sessionStorage.removeItem(this.expiresAtKey);
    this.accessToken.set(null);
  }

  private randomString(byteLength: number): string {
    const bytes = new Uint8Array(byteLength);
    crypto.getRandomValues(bytes);
    return this.base64UrlEncode(bytes);
  }

  private async sha256(value: string): Promise<string> {
    const data = new TextEncoder().encode(value);
    const digest = await crypto.subtle.digest('SHA-256', data);
    return this.base64UrlEncode(new Uint8Array(digest));
  }

  private base64UrlEncode(bytes: Uint8Array): string {
    let binary = '';
    bytes.forEach((byte) => binary += String.fromCharCode(byte));
    return btoa(binary).replace(/\+/g, '-').replace(/\//g, '_').replace(/=+$/, '');
  }
}