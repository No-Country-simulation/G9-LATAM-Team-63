# Energia Backend API

Backend REST desarrollado con Spring Boot para analizar consumo energético, clasificar eficiencia, estimar costo mensual y generar recomendaciones de ahorro. El proyecto incluye autenticación JWT, documentación OpenAPI/Swagger, persistencia con Spring Data JPA y base de datos H2 en memoria.

## Tabla de contenido

- [Descripción general](#descripción-general)
- [Stack tecnológico](#stack-tecnológico)
- [Arquitectura del proyecto](#arquitectura-del-proyecto)
- [Estructura de carpetas](#estructura-de-carpetas)
- [Requisitos previos](#requisitos-previos)
- [Configuración](#configuración)
- [Ejecución local](#ejecución-local)
- [Seguridad y autenticación](#seguridad-y-autenticación)
- [Documentación Swagger/OpenAPI](#documentación-swaggeropenapi)
- [Base de datos H2](#base-de-datos-h2)
- [Endpoints principales](#endpoints-principales)
- [Reglas de negocio](#reglas-de-negocio)
- [Validaciones](#validaciones)
- [Manejo de errores](#manejo-de-errores)
- [Pruebas](#pruebas)
- [Notas de seguridad](#notas-de-seguridad)

## Descripción general

La aplicación permite registrar análisis de consumo energético a partir de datos como consumo mensual en kWh, uso en horario pico, cantidad de equipos, tipo de inmueble y horas de alto consumo. Con esa información, el backend:

- Clasifica el consumo como `Eficiente`, `Moderado` o `Ineficiente`.
- Calcula un costo mensual estimado usando una tarifa fija por kWh.
- Genera recomendaciones según la categoría y los patrones de consumo.
- Persiste el resultado en una base de datos H2 en memoria.
- Permite consultar análisis previamente creados por ID.
- Protege los endpoints de análisis mediante JWT.

## Stack tecnológico

| Tecnología | Uso |
| --- | --- |
| Java 21 | Lenguaje base del proyecto |
| Spring Boot 3.3.5 | Framework principal |
| Spring Web | Exposición de API REST |
| Spring Security | Seguridad HTTP y autenticación stateless |
| JJWT 0.12.6 | Generación y validación de tokens JWT |
| Spring Data JPA | Persistencia y repositorios |
| H2 Database | Base de datos en memoria para desarrollo/pruebas |
| Jakarta Validation | Validación de DTOs de entrada |
| Lombok 1.18.38 | Reducción de código repetitivo |
| SpringDoc OpenAPI 2.6.0 | Documentación interactiva de la API |
| Maven / Maven Wrapper | Gestión de dependencias, build y ejecución del proyecto |

## Arquitectura del proyecto

El proyecto sigue una arquitectura por capas:

- `controller`: expone endpoints REST.
- `service`: contiene la lógica de negocio del análisis energético.
- `repository`: abstrae el acceso a datos con Spring Data JPA.
- `entity`: define el modelo persistido en base de datos.
- `dto`: define contratos de entrada y salida de la API.
- `security`: gestiona JWT y autenticación por request.
- `config`: centraliza seguridad, OpenAPI, JSON, zona horaria y constantes.
- `exception`: maneja errores de forma centralizada.
- `enums`: define valores de dominio como categorías y tipos de inmueble.

## Estructura de carpetas

```text
.
├── pom.xml
├── mvnw
├── mvnw.cmd
├── HELP.md
└── src
    ├── main
    │   ├── java/com/hackathon/energia_backend
    │   │   ├── EnergiaBackendApplication.java
    │   │   ├── config
    │   │   ├── controller
    │   │   ├── dto
    │   │   ├── entity
    │   │   ├── enums
    │   │   ├── exception
    │   │   ├── repository
    │   │   ├── security
    │   │   └── service
    │   └── resources
    │       ├── application.properties
    │       └── static/swagger-custom.html
    └── test
        └── java/com/hackathon/energia_backend
```

## Requisitos previos

- Java 21.
- Maven instalado globalmente, o restaurar la configuración completa del Maven Wrapper.

> Nota: el repositorio contiene `mvnw` y `mvnw.cmd`, pero actualmente no contiene `.mvn/wrapper/maven-wrapper.properties`. Por eso, el wrapper no puede ejecutarse hasta que se restaure esa carpeta/configuración.

## Configuración

La configuración principal está en `src/main/resources/application.properties`.

Valores relevantes:

```properties
spring.application.name=energia-backend
server.port=8080

app.timezone=America/Bogota
spring.jackson.time-zone=America/Bogota
spring.jackson.date-format=yyyy-MM-dd HH:mm:ss

spring.datasource.url=jdbc:h2:mem:hackathon_db
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html

app.jwt.expiration-ms=86400000
```

La aplicación usa la zona horaria `America/Bogota` por defecto para la JVM y para la serialización JSON.

## Ejecución local

Desde la raíz del proyecto:

Con Maven instalado:

```bash
mvn spring-boot:run
```

Si se restaura el Maven Wrapper:

- Windows: `.\mvnw.cmd spring-boot:run`
- Linux/macOS: `./mvnw spring-boot:run`

La API queda disponible en:

```text
http://localhost:8080
```

## Seguridad y autenticación

La autenticación está implementada con JWT. El endpoint de login es público y genera un token firmado. Los endpoints de análisis requieren enviar el token en el header `Authorization`.

Credenciales actuales para entorno de hackathon:

```text
Usuario: admin
Contraseña: hackathon2026
```

### Login

```http
POST /api/auth/login
Content-Type: application/json
```

Body:

```json
{
  "username": "admin",
  "password": "hackathon2026"
}
```

Respuesta exitosa:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### Uso del token

Para consumir endpoints protegidos:

```http
Authorization: Bearer <token>
```

Ejemplo:

```bash
curl -X GET http://localhost:8080/api/analisis/1 \
  -H "Authorization: Bearer <token>"
```

## Documentación Swagger/OpenAPI

El proyecto incluye documentación interactiva con SpringDoc.

URLs disponibles:

- Swagger UI estándar: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- Swagger custom: `http://localhost:8080/swagger-custom.html`

El archivo `swagger-custom.html` incluye una interfaz personalizada con manejo visual del estado de autenticación y campo para aplicar el token Bearer.

## Base de datos H2

La aplicación usa H2 en memoria:

```text
JDBC URL: jdbc:h2:mem:hackathon_db
Usuario: sa
Contraseña: sin contraseña
```

Consola H2:

```text
http://localhost:8080/h2-console
```

La tabla principal es:

```text
resultados_analisis
```

Campos principales persistidos:

- `id`
- `consumoKwh`
- `usoHorarioPico`
- `cantidadEquipos`
- `tipoInmueble`
- `categoria`
- `probabilidad`
- `costoEstimadoMensual`
- `fechaCreacion`

## Endpoints principales

### Autenticación

| Método | Ruta | Seguridad | Descripción |
| --- | --- | --- | --- |
| `POST` | `/api/auth/login` | Pública | Valida credenciales y genera un JWT |

### Análisis energético

| Método | Ruta | Seguridad | Descripción |
| --- | --- | --- | --- |
| `POST` | `/api/analisis` | JWT requerido | Crea un análisis energético |
| `GET` | `/api/analisis/{id}` | JWT requerido | Consulta un análisis guardado por ID |

## Crear análisis energético

```http
POST /api/analisis
Content-Type: application/json
Authorization: Bearer <token>
```

Body:

```json
{
  "consumoKwh": 420.5,
  "usoHorarioPico": true,
  "cantidadEquipos": 8,
  "tipoInmueble": "Oficina",
  "horasAltoConsumo": 8
}
```

Respuesta:

```json
{
  "categoria": "Moderado",
  "probabilidad": 0.7,
  "recomendaciones": [
    "Identificar oportunidades de ahorro en horarios pico",
    "Revisar equipos individuales con mayor consumo para optimizar el ratio",
    "Implementar apagado automático de equipos al cierre de jornada"
  ],
  "costo_estimado_mensual": 315.375,
  "idAnalisis": 1
}
```

## Consultar análisis por ID

```http
GET /api/analisis/{id}
Authorization: Bearer <token>
```

Ejemplo:

```bash
curl -X GET http://localhost:8080/api/analisis/1 \
  -H "Authorization: Bearer <token>"
```

## Reglas de negocio

La clasificación se calcula en `AnalisisEnergiaService` usando:

```text
ratio = consumoKwh / cantidadEquipos
```

| Condición | Categoría | Probabilidad base |
| --- | --- | --- |
| `ratio > 55` y `horasAltoConsumo > 7` | `Ineficiente` | `0.75` |
| `ratio < 28` y `horasAltoConsumo < 4` | `Eficiente` | `0.85` |
| Cualquier otro caso | `Moderado` | `0.70` |

El costo estimado mensual se calcula con una tarifa fija:

```text
costo_estimado_mensual = consumoKwh * 0.75
```

La tarifa está definida en:

```java
AppConfig.TARIFA_KWH = 0.75
```

## Recomendaciones generadas

Las recomendaciones dependen de la categoría y de algunos datos de entrada.

### Categoría Ineficiente

- Reducir horas de alto consumo.
- Evaluar equipos con alto consumo por unidad.
- Si usa horario pico, desplazar consumo a horarios no pico.

### Categoría Eficiente

- Mantener prácticas actuales de consumo.
- Monitorear periódicamente el consumo.

### Categoría Moderado

- Identificar oportunidades de ahorro en horarios pico.
- Si el ratio `kWh/equipo` es mayor a `40`, revisar equipos con mayor consumo.

### Reglas adicionales

- Si el inmueble es `Oficina`, se recomienda implementar apagado automático al cierre de jornada.
- Si hay más de `10` equipos, se recomienda considerar reemplazo por modelos con certificación energética.

## Validaciones

El DTO `AnalisisRequest` aplica las siguientes validaciones:

| Campo | Tipo | Reglas |
| --- | --- | --- |
| `consumoKwh` | `Double` | Obligatorio, mínimo `0.1` |
| `usoHorarioPico` | `Boolean` | Obligatorio |
| `cantidadEquipos` | `Integer` | Obligatorio, entre `1` y `50` |
| `tipoInmueble` | `String` | Obligatorio, valores: `Casa`, `Apartamento`, `Local`, `Oficina` |
| `horasAltoConsumo` | `Integer` | Obligatorio, entre `0` y `24` |

Ejemplo de error de validación:

```json
{
  "error": "Datos de entrada inválidos",
  "detalles": {
    "cantidadEquipos": "Debe haber al menos 1 equipo",
    "tipoInmueble": "El tipo debe ser Casa, Apartamento, Local u Oficina"
  }
}
```

## Manejo de errores

El proyecto incluye un manejador global con `@RestControllerAdvice`.

Casos cubiertos:

- `400 Bad Request`: errores de validación en DTOs.
- `404 Not Found`: errores cuyo mensaje contiene `no encontrado`.
- `500 Internal Server Error`: errores runtime no clasificados.
- `401 Unauthorized`: credenciales inválidas en login o acceso sin token válido a rutas protegidas.

## Pruebas

El proyecto incluye una prueba base de contexto Spring:

```text
EnergiaBackendApplicationTests.contextLoads()
```

Ejecutar pruebas con Maven instalado:

```bash
mvn test
```

Si se restaura el Maven Wrapper:

- Windows: `.\mvnw.cmd test`
- Linux/macOS: `./mvnw test`

## Estado actual de build local

Durante la revisión del proyecto se detectó lo siguiente:

- `.\mvnw.cmd test` no ejecuta porque falta `.mvn/wrapper/maven-wrapper.properties`.
- `mvn test` requiere Maven instalado globalmente; en el entorno revisado no estaba disponible en el `PATH`.

El código fuente y la configuración Maven están definidos en `pom.xml`, pero para ejecutar pruebas localmente se debe instalar Maven o restaurar correctamente el Maven Wrapper.

## Notas de seguridad

Este proyecto está configurado para un entorno de hackathon/desarrollo. Antes de llevarlo a producción, se recomienda:

- Mover `app.jwt.secret` a variables de entorno o gestor de secretos.
- Reemplazar las credenciales hardcodeadas de `AuthController` por usuarios persistidos con contraseñas hasheadas.
- Usar una base de datos persistente en lugar de H2 en memoria.
- Ajustar CORS a dominios reales del frontend.
- Evitar exponer la consola H2 en producción.
- Revisar el tiempo de expiración del JWT según el nivel de riesgo del sistema.

## Autoría y contexto

Proyecto backend para hackathon de consumo energético bajo el paquete base:

```text
com.hackathon.energia_backend
```

El nombre de paquete usa guion bajo porque `com.hackathon.energia-backend` no es válido en Java.
