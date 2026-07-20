import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { environment } from '../../../../../environments/environment';
import { KeycloakAuthService } from './keycloak-auth.service';

export const bearerTokenInterceptor: HttpInterceptorFn = (request, next) => {
  const token = inject(KeycloakAuthService).getAccessToken();
  const targetsApi = request.url.startsWith(environment.apiBaseUrl);

  if (!token || !targetsApi) {
    return next(request);
  }

  return next(request.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`
    }
  }));
};