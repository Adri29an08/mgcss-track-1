# mgcss-track-1 (mgcss-track)

Proyecto **Spring Boot** usando **Java 17** y **Maven**.

---

## Requisitos

- **Java 17 (JDK 17)**
- **Docker** (recomendado / necesario si el proyecto usa Postgres vía Testcontainers)
- (Opcional) **Maven instalado**
  - Este repositorio incluye **Maven Wrapper** (`./mvnw` y `mvnw.cmd`), así que puedes ejecutar sin instalar Maven global.

---

## Clonar el repositorio

```bash
git clone https://github.com/Adri29an08/mgcss-track-1.git
cd mgcss-track-1
git checkout feature/initial-structure
```

---

## Ejecutar en desarrollo

### Linux / macOS

```bash
./mvnw spring-boot:run
```

### Windows (CMD / PowerShell)

```bat
mvnw.cmd spring-boot:run
```

Por defecto, Spring Boot levanta en:  
- `http://localhost:8080`

> Nota: si existiera `application.properties` o `application.yml`, el puerto podría cambiar. En esta rama no hay README con esa info.

---

## Compilar

```bash
./mvnw clean package
```

Esto genera el `.jar` dentro de la carpeta `target/`.

---

## Ejecutar el JAR compilado

```bash
java -jar target/mgcss-track-0.0.1-SNAPSHOT.jar
```

---

## Ejecutar tests

```bash
./mvnw test
```

---

## Base de datos

El proyecto incluye dependencias para:

- **H2** (runtime)
- **PostgreSQL** (runtime)

Además, en `HELP.md` se indica soporte de **Testcontainers** (en desarrollo) con imagen:

- `postgres:latest`

Si el proyecto está usando Testcontainers para levantar Postgres automáticamente:
- asegúrate de tener **Docker Desktop / Docker Engine** corriendo antes de ejecutar la app o los tests.

---

## Documentación API (Swagger / OpenAPI)

El proyecto incluye `springdoc-openapi-starter-webmvc-ui`, por lo que normalmente están disponibles:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

---

## Comandos útiles (resumen)

```bash
# Ejecutar
./mvnw spring-boot:run

# Compilar
./mvnw clean package

# Tests
./mvnw test

# Ejecutar jar
java -jar target/mgcss-track-0.0.1-SNAPSHOT.jar
```
