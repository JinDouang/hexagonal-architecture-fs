# API monorepo

Spring Boot API following a feature-oriented hexagonal architecture.

```text
product/
  domain/                 # Spring-free business model
  application/            # use cases
  application/port/in     # inbound ports and commands
  application/port/out    # outbound ports
  adapter/in/web          # REST controller and API contracts
  adapter/out/persistence # JPA PostgreSQL adapter
  adapter/out/moodle      # conditional Moodle HTTP adapter
```

Local quick run without Keycloak:

```bash
APP_SECURITY_ENABLED=false mvn spring-boot:run
```

With Docker compose from the repository root, security is enabled and JWTs are validated against Keycloak.