# Angular + Spring Boot Hexagonal Architecture POC

POC compose de deux monorepos independants dans ce workspace :

- `front-monorepo` : Angular SPA, OIDC Authorization Code + PKCE, bearer interceptor, client REST produits.
- `api-monorepo` : Spring Boot API, couches hexagonales `adapters -> application/ports -> domain`.

## Modules

Le module implemente est `Entity catalogue / Products`. Les dossiers `training-sessions`, `users` et `settings` sont reserves pour montrer la regle de decoupage par feature : chaque feature possede son domaine, son application et ses adapters.

## Endpoints API

| Method | URL | Description |
| --- | --- | --- |
| GET | `/api/products` | List all products |
| GET | `/api/products/{id}` | Find a product by ID |
| POST | `/api/products` | Create a new product |
| PUT | `/api/products/{id}` | Update an existing product |
| DELETE | `/api/products/{id}` | Remove a product |
| PATCH | `/api/products/{id}/activate` | Activate a product |
| PATCH | `/api/products/{id}/deactivate` | Deactivate a product |

## Run avec Docker

```bash
docker compose up --build
```

Services :

- Front Angular : http://localhost:4200
- API Spring Boot : http://localhost:8080
- Keycloak : http://localhost:8081
- PostgreSQL : localhost:5432

Identifiants Keycloak de demo :

- Realm : `training-demo`
- Client public SPA : `training-spa`
- User : `demo`
- Password : `demo`

## Security

Le front effectue un login OIDC Authorization Code + PKCE contre Keycloak, stocke l'access token en session storage et l'envoie en bearer token vers l'API.

L'API valide les JWT via Spring Security Resource Server avec :

- issuer : `OIDC_ISSUER_URI`
- JWKS : `OIDC_JWK_SET_URI`

Pour un test API sans Keycloak, lancer l'API avec `APP_SECURITY_ENABLED=false`.

## PostgreSQL et Moodle

PostgreSQL n'est accessible que par l'adapter de persistence JPA de l'API. Le domaine et les use cases ne dependent pas de Spring Data.

La communication Moodle est exposee via le port sortant `MoodleProductSyncPort`. L'adapter HTTP Moodle n'est actif que si `APP_MOODLE_ENABLED=true`; sinon un adapter no-op est cable pour garder le use case inchange.