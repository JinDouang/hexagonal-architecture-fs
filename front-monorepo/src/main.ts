import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { AppComponent } from './app/app.component';
import { routes } from './app/app.routes';
import { PRODUCT_API_PORT } from './app/features/entity-catalogue/products/application/ports/product-api.port';
import { ProductHttpAdapter } from './app/features/entity-catalogue/products/adapters/api/product-http.adapter';
import { bearerTokenInterceptor } from './app/shared/auth/adapters/oidc/bearer-token.interceptor';

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptors([bearerTokenInterceptor])),
    { provide: PRODUCT_API_PORT, useClass: ProductHttpAdapter }
  ]
}).catch((error: unknown) => console.error(error));