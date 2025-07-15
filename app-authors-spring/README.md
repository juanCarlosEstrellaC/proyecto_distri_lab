# App Authors Spring

Este módulo es una migración del módulo `app-authors` de Quarkus a Spring Boot. Proporciona la misma funcionalidad pero utilizando el framework Spring Boot en lugar de Quarkus.

## Características

- **Framework**: Spring Boot 3.4.1
- **Base de datos**: PostgreSQL con JPA/Hibernate
- **Migración**: Flyway para control de versiones de DB
- **Service Discovery**: Consul
- **Métricas**: Micrometer con Prometheus
- **Health Checks**: Spring Boot Actuator
- **Java Version**: 21

## Funcionalidades

### Endpoints REST

- `GET /authors` - Obtener todos los autores
- `GET /authors/{id}` - Obtener autor por ID
- `GET /authors/find/{isbn}` - Obtener autores por ISBN del libro
- `POST /authors` - Crear nuevo autor
- `PUT /authors/{id}` - Actualizar autor existente
- `DELETE /authors/{id}` - Eliminar autor

### Health Checks

- `/actuator/health` - Estado general de la aplicación
- `/actuator/health/liveness` - Health check de liveness
- `/actuator/health/readiness` - Health check de readiness

### Métricas

- `/actuator/metrics` - Métricas de la aplicación
- `/actuator/prometheus` - Métricas en formato Prometheus

## Configuración

### Base de datos
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/postgres
    username: postgres
    password: labcom,2015
```

### Consul
```yaml
spring:
  cloud:
    consul:
      host: 127.0.0.1
      port: 8500
```

## Construcción y ejecución

### Ejecutar localmente
```bash
./gradlew bootRun
```

### Construir JAR
```bash
./gradlew build
```

### Construir imagen Docker
```bash
docker build -t app-authors-spring .
```

### Ejecutar con Docker
```bash
docker run -p 8080:8080 app-authors-spring
```

## Diferencias con el módulo Quarkus

1. **Inyección de dependencias**: Usa `@Autowired` y `@RequiredArgsConstructor` en lugar de `@Inject`
2. **REST Controllers**: Usa `@RestController` y `@RequestMapping` en lugar de JAX-RS
3. **Repositorios**: Extiende `JpaRepository` en lugar de `PanacheRepository`
4. **Configuración**: Usa `application.yml` en lugar de `application.properties`
5. **Health Checks**: Usa Spring Boot Actuator en lugar de MicroProfile Health
6. **Métricas**: Usa Micrometer en lugar de MicroProfile Metrics

## Entidades

- **Author**: Entidad principal que representa un autor
- **BookAuthor**: Tabla intermedia para la relación many-to-many entre libros y autores
- **BookAuthorId**: Clave compuesta para la tabla BookAuthor
