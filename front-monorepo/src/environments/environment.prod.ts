export const environment = {
  production: true,
  apiBaseUrl: 'http://localhost:8080/api',
  oidc: {
    authority: 'http://localhost:8081/realms/training-demo',
    clientId: 'training-spa',
    redirectUri: 'http://localhost:4200/',
    scope: 'openid profile email'
  }
};