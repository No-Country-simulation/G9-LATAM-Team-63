# ⚡ EnergiAI — Plataforma de Análisis de Consumo Energético

> Proyecto desarrollado para el **Hackathon No Country — ONE G9 LATAM (Alura + Oracle)**, Team 63.

EnergiAI es una plataforma full-stack que permite a un usuario registrar los datos de consumo eléctrico de su inmueble y recibir, en segundos:

- Una **clasificación de eficiencia energética** (`Eficiente`, `Moderado` o `Ineficiente`).
- El **costo mensual estimado** en base al consumo.
- **Recomendaciones de ahorro personalizadas** según su perfil de consumo.

El sistema combina tres capas independientes que se comunican entre sí: un **backend en Java (Spring Boot)** que expone la API principal y gestiona usuarios/seguridad, un **servicio de Data Science en Python (FastAPI)** que sirve un modelo de Machine Learning (XGBoost) entrenado para predecir la eficiencia energética, y un **frontend en React** que consume la API y ofrece la interfaz de usuario. Todo se persiste en **MySQL** y todo el stack se levanta con un solo comando gracias a **Docker Compose**.

---

## 📑 Tabla de contenido

1. [Arquitectura general](#-arquitectura-general)
2. [Stack tecnológico](#-stack-tecnológico)
3. [Estructura de carpetas del proyecto](#-estructura-de-carpetas-del-proyecto)
4. [Requisitos previos](#-requisitos-previos)
5. [Clonar el proyecto](#-clonar-el-proyecto)
6. [Configuración de variables de entorno](#-configuración-de-variables-de-entorno)
7. [Levantar todo el proyecto con Docker (recomendado)](#-levantar-todo-el-proyecto-con-docker-recomendado)
8. [Verificar que todo esté corriendo](#-verificar-que-todo-esté-corriendo)
9. [Ver logs de cada servicio](#-ver-logs-de-cada-servicio)
10. [Detener y limpiar los contenedores](#-detener-y-limpiar-los-contenedores)
11. [Configuración de la base de datos MySQL](#-configuración-de-la-base-de-datos-mysql)
12. [Ejecución en modo desarrollo (sin Docker)](#-ejecución-en-modo-desarrollo-sin-docker)
13. [Flujo de comunicación entre servicios](#-flujo-de-comunicación-entre-servicios)
14. [Autenticación y usuario administrador](#-autenticación-y-usuario-administrador)
15. [Documentación interactiva de las APIs](#-documentación-interactiva-de-las-apis)
16. [Solución de problemas comunes](#-solución-de-problemas-comunes)
17. [Buenas prácticas y convenciones](#-buenas-prácticas-y-convenciones)

---

## 🏗 Arquitectura general

El proyecto sigue una arquitectura de **microservicios simple**, orquestada con Docker Compose. Cada carpeta en la raíz del repositorio es un servicio independiente, con su propio `Dockerfile`, su propio lenguaje y sus propias dependencias:

```
                ┌────────────────┐
   Usuario ───▶ │   Frontend     │  React + TypeScript + Vite (servido con Nginx)
                │   Puerto 3000  │
                └───────┬────────┘
                        │  HTTP (REST + JWT)
                        ▼
                ┌────────────────┐
                │   Backend      │  Java 21 + Spring Boot 3.3.5
                │   Puerto 8080  │  (API principal, seguridad, persistencia)
                └───┬────────┬───┘
                    │        │
         JDBC (SQL) │        │ HTTP (REST)
                    ▼        ▼
            ┌──────────┐  ┌──────────────────┐
            │  MySQL   │  │  Data Science     │  Python 3.12 + FastAPI
            │ Puerto   │  │  Puerto 8000      │  (modelo XGBoost entrenado)
            │ 3306/3307│  └──────────────────┘
            └──────────┘
```

**Cómo colaboran los tres servicios:**

- El **frontend** nunca habla directamente con la base de datos ni con el servicio de Data Science: todas las peticiones pasan por el **backend**, que actúa como puerta de entrada única (single entry point).
- El **backend** valida credenciales (JWT), aplica las reglas de negocio, persiste los resultados en **MySQL** y delega el cálculo de la predicción al servicio de **Data Science** vía HTTP.
- Si el servicio de Data Science no responde (caído, timeout, error), el backend **no falla**: aplica automáticamente un **motor de reglas local en Java** como mecanismo de respaldo (*fallback*), garantizando que el usuario siempre reciba una respuesta.
- El servicio de **Data Science** es *stateless*: no tiene base de datos propia, solo carga en memoria un modelo `XGBoost` ya entrenado (junto con su *scaler*, *encoder* y *preprocessor*) y expone un único endpoint de predicción.

---

## 🧰 Stack tecnológico

| Capa | Lenguaje / Framework | Función principal | Puerto (Docker) |
|---|---|---|---|
| **Frontend** | React 19 + TypeScript + Vite, servido con **Nginx** | Interfaz de usuario (SPA) | `3000` |
| **Backend** | Java 21 + Spring Boot 3.3.5 | API REST, autenticación JWT, persistencia | `8080` |
| **Data Science** | Python 3.12 + FastAPI + Uvicorn | Servir el modelo de Machine Learning (predicción) | `8000` |
| **Base de datos** | MySQL 8.0 | Persistencia relacional | `3306` (interno) / `3307` (host) |

**Librerías y herramientas clave por módulo:**

| Backend (Java) | Data Science (Python) | Frontend (TypeScript) |
|---|---|---|
| Spring Web / Spring Data JPA | FastAPI + Uvicorn | React Router 7 |
| Spring Security + JJWT (JWT) | scikit-learn + XGBoost | React Hook Form + Zod |
| Flyway (migraciones) | Pandas / NumPy | TanStack Query 5 |
| Lombok | Joblib (serialización de modelos) | Zustand (estado global) |
| SpringDoc OpenAPI (Swagger) | Pydantic (validación) | Vite (bundler) |
| Maven | Jupyter Notebook (EDA / entrenamiento) | ESLint |

---

## 📂 Estructura de carpetas del proyecto

```
energia-backend/
├── docker-compose.yml          # Orquesta los 4 servicios (db, backend, frontend, data-science)
├── .env.example                 # Plantilla de variables de entorno
├── CONTRIBUTING.md              # Convenciones de ramas, commits y flujo de trabajo
│
├── backend/                     # 🟦 API principal — Java 21 + Spring Boot
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/main/java/com/hackathon/energia_backend/
│       ├── controller/          # Endpoints REST (auth, análisis, usuarios)
│       ├── service/             # Lógica de negocio (motor de reglas + integración ML)
│       ├── repository/          # Acceso a datos (Spring Data JPA)
│       ├── entity/               # Entidades JPA (Usuario, ResultadoAnalisis)
│       ├── security/             # Filtro y utilidades JWT
│       ├── config/               # Seguridad, OpenAPI, CORS, datos iniciales
│       └── dto/                  # Contratos de entrada/salida de la API
│
├── data-science/                # 🟩 Servicio de predicción — Python + FastAPI
│   ├── Dockerfile
│   ├── requirements.txt
│   ├── api/
│   │   ├── main.py               # Punto de entrada FastAPI
│   │   ├── routers/predict.py    # Endpoint POST /api/v1/predict/
│   │   ├── services/             # Carga del modelo y lógica de predicción
│   │   └── core/config.py        # Rutas a los artefactos del modelo
│   ├── models/saved/             # Modelo XGBoost + scaler + encoder (.pkl)
│   ├── notebooks/                # EDA y entrenamiento (Jupyter)
│   └── scripts/                  # Generación de datasets sintéticos
│
└── frontend/                    # 🟨 Interfaz de usuario — React + TypeScript
    ├── Dockerfile
    ├── nginx.conf                # Configuración de Nginx para servir la SPA
    ├── package.json
    └── src/
        ├── features/              # Páginas por funcionalidad (auth, análisis, historial…)
        ├── data/                  # Cliente HTTP, hooks y servicios de API
        ├── store/                 # Estado global (Zustand)
        └── shared/components/     # Componentes reutilizables
```

> 📘 Cada módulo tiene además su propio `README.md` con documentación técnica detallada: `backend/README.md`, `data-science/README.md` y `frontend/README.md`.

---

## ✅ Requisitos previos

Para levantar el proyecto **con Docker** (forma recomendada) solo necesitas:

- **Docker Desktop** instalado y en ejecución (incluye Docker Engine y Docker Compose).
    - Descarga oficial: <https://www.docker.com/products/docker-desktop/>
    - Guía de instalación: <https://docs.docker.com/get-started/get-docker/>
    - En Windows, acepta habilitar **WSL 2** cuando el instalador lo solicite.
- **Git** para clonar el repositorio.

Verifica que Docker esté correctamente instalado:

```bash
docker --version
docker compose version
docker info
```

> ⚠️ Si `docker info` falla, Docker Desktop no está corriendo. Ábrelo y espera a que el ícono indique "Engine running" antes de continuar.

Si en cambio quieres ejecutar cada servicio **de forma local sin Docker** (útil para desarrollo), revisa además la sección [Ejecución en modo desarrollo](#-ejecución-en-modo-desarrollo-sin-docker).

---

## 📥 Clonar el proyecto

```bash
git clone <URL_DEL_REPOSITORIO>
cd energia-backend
```

---

## ⚙️ Configuración de variables de entorno

El proyecto trae un archivo de ejemplo `.env.example` en la raíz. Cópialo como `.env`:

```bash
# Linux / macOS
cp .env.example .env

# Windows (PowerShell)
copy .env.example .env
```

Contenido de `.env` (valores por defecto, ya listos para desarrollo local):

```env
# Nombre de la base de datos que se creará automáticamente
DB_ENERGI_AI=energia_db

# Usuario de MySQL (root por defecto)
DB_USER_MYSQL=root

# Contraseña de MySQL
DB_PASSWORD=rootpassword

# Puerto en el que MySQL quedará expuesto en tu máquina (host)
DB_PORT=3307
```

> 🔒 **Importante:** `.env` está incluido en `.gitignore` y **nunca debe subirse al repositorio**. Para un entorno real de producción, cambia `DB_PASSWORD` por una contraseña segura.

Estas variables son leídas automáticamente por `docker-compose.yml` y por Spring Boot (perfil `docker`) para configurar la conexión a la base de datos y la URL interna del servicio de Data Science — no necesitas tocar ningún archivo de código para levantar el proyecto.

---

## 🚀 Levantar todo el proyecto con Docker (recomendado)

Desde la **raíz del repositorio** (donde está `docker-compose.yml`):

### 1. Construir las imágenes y levantar todos los servicios

```bash
docker compose up -d --build
```

- `up` crea y arranca los contenedores.
- `-d` (*detached*) los ejecuta en segundo plano.
- `--build` fuerza la reconstrucción de las imágenes (necesario la primera vez o cuando cambias código).

Este comando levanta, en orden y con las dependencias correctas:

1. **`db`** → contenedor MySQL 8.0, con *healthcheck* (el backend espera a que esté realmente listo antes de arrancar).
2. **`backend`** → API Spring Boot, en el perfil `docker`.
3. **`data-science`** → API FastAPI con el modelo de Machine Learning.
4. **`frontend`** → build de producción de React servido con Nginx.

### 2. Primer arranque

La primera vez puede tardar varios minutos, ya que Docker debe:
- Descargar las imágenes base (MySQL, Maven, Node, Python, Nginx).
- Compilar el backend con Maven.
- Instalar las dependencias de Python.
- Compilar el frontend con Vite.

Las siguientes veces será mucho más rápido gracias al *cache* de capas de Docker.

### 3. Levantar sin reconstruir (arranques posteriores)

Si no cambiaste código y solo quieres volver a levantar los contenedores ya construidos:

```bash
docker compose up -d
```

---

## 🔍 Verificar que todo esté corriendo

### Ver el estado de los contenedores

```bash
docker compose ps
```

Deberías ver los 4 servicios (`energia-db`, `energia-backend`, `energia-ds`, `energia-frontend`) con estado `Up` (y `healthy` en el caso de `db` y `backend`, que tienen *healthcheck* configurado).

### Probar cada servicio desde el navegador o con `curl`

| Servicio | URL de verificación | Resultado esperado |
|---|---|---|
| Frontend | http://localhost:3000 | Landing page de EnergiAI |
| Backend (health) | http://localhost:8080/actuator/health | `{"status":"UP"}` |
| Backend (Swagger) | http://localhost:8080/swagger-ui.html | Documentación interactiva de la API |
| Data Science (docs) | http://localhost:8000/docs | Documentación interactiva (Swagger de FastAPI) |
| MySQL | conectar con cualquier cliente a `localhost:3307` | Conexión exitosa |

```bash
curl http://localhost:8080/actuator/health
curl http://localhost:8000/
```

---

## 📜 Ver logs de cada servicio

```bash
# Logs de todos los servicios en tiempo real
docker compose logs -f

# Logs de un servicio específico
docker compose logs -f backend
docker compose logs -f data-science
docker compose logs -f frontend
docker compose logs -f db

# Solo las últimas 100 líneas
docker compose logs --tail 100 backend
```

Presiona `Ctrl + C` para salir del modo de seguimiento (`-f`) sin detener los contenedores.

Para ver el estado detallado del *healthcheck* del backend o la base de datos:

```bash
docker inspect --format='{{.State.Health.Status}}' energia-backend
docker inspect --format='{{.State.Health.Status}}' energia-db
```

---

## 🛑 Detener y limpiar los contenedores

```bash
# Detener todos los servicios (mantiene los contenedores y volúmenes)
docker compose stop

# Volver a levantar los servicios detenidos, sin reconstruir
docker compose start

# Detener y eliminar los contenedores (mantiene los volúmenes, es decir, los datos de MySQL)
docker compose down

# Detener, eliminar contenedores Y eliminar el volumen de datos de MySQL (borra la base de datos por completo)
docker compose down -v
```

> ⚠️ Usa `docker compose down -v` solo cuando quieras empezar la base de datos **desde cero**. Elimina permanentemente todos los datos guardados (usuarios, análisis, etc.).

---

## 🗄 Configuración de la base de datos MySQL

El servicio `db` en `docker-compose.yml` usa la imagen oficial **`mysql:8.0`** y se configura enteramente a través de variables de entorno — no requiere ningún script SQL manual.

### Cómo se configura automáticamente

```yaml
db:
  image: mysql:8.0
  environment:
    MYSQL_ROOT_PASSWORD: ${DB_PASSWORD:-rootpassword}
    MYSQL_DATABASE: ${DB_ENERGI_AI:-energia_db}
  ports:
    - "${DB_PORT:-3307}:3306"
```

- Al arrancar por primera vez, MySQL crea automáticamente la base de datos `energia_db` (o el nombre que definas en `DB_ENERGI_AI`).
- El usuario `root` queda configurado con la contraseña definida en `DB_PASSWORD`.
- El **backend** (Spring Boot + Hibernate) crea automáticamente todas las tablas necesarias al arrancar, gracias a `spring.jpa.hibernate.ddl-auto=update` — **no necesitas ejecutar ningún script `.sql` manualmente**.

### Tablas que se crean automáticamente

| Tabla | Contenido |
|---|---|
| `usuarios` | Usuarios registrados (username, password encriptado, rol) |
| `usuario_roles` | Roles asociados a cada usuario (`ADMIN`, `USER`) |
| `analisis_energetico` | Historial de análisis energéticos realizados, con sus resultados |

### Conexión desde un cliente externo (DBeaver, MySQL Workbench, TablePlus, etc.)

| Parámetro | Valor |
|---|---|
| Host | `localhost` |
| Puerto | `3307` (el mapeado en `.env` → `DB_PORT`) |
| Usuario | `root` |
| Contraseña | `rootpassword` (o el valor que definiste en `DB_PASSWORD`) |
| Base de datos | `energia_db` |

> ℹ️ Se usa el puerto **3307** en el host (en vez del `3306` estándar) para evitar conflictos si ya tienes una instancia de MySQL corriendo localmente en tu máquina. Internamente, dentro de la red de Docker, el backend siempre se conecta al puerto `3306` del contenedor `db`.

### Perfiles de conexión de Spring Boot

El backend cambia automáticamente su configuración de base de datos según el perfil activo (`SPRING_PROFILES_ACTIVE`):

| Perfil | Uso | Host de conexión |
|---|---|---|
| `docker` | Contenedores (usado por `docker-compose.yml`) | `db` (nombre del servicio en la red de Docker) |
| `local` | Desarrollo local (IDE, sin Docker) | `localhost` |

No necesitas editar estos archivos manualmente: el perfil correcto se activa solo con la variable `SPRING_PROFILES_ACTIVE`, que ya viene configurada en `docker-compose.yml`.

---

## 💻 Ejecución en modo desarrollo (sin Docker)

Si prefieres correr cada servicio directamente en tu máquina (por ejemplo, para debuguear con tu IDE), sigue este orden:

### 1. Levantar solo MySQL con Docker (recomendado, aunque el resto corra local)

```bash
docker compose up -d db
```

### 2. Backend (Java 21 + Maven)

```bash
cd backend
export SPRING_PROFILES_ACTIVE=local        # Windows PowerShell: $env:SPRING_PROFILES_ACTIVE="local"
export DB_PASSWORD=rootpassword             # Windows PowerShell: $env:DB_PASSWORD="rootpassword"
mvn spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

### 3. Data Science (Python 3.11+)

```bash
cd data-science
python -m venv .venv
source .venv/bin/activate        # Windows: .venv\Scripts\activate
pip install -r requirements.txt
uvicorn api.main:app --host 0.0.0.0 --port 8000 --reload
```

La API queda disponible en `http://localhost:8000`.

### 4. Frontend (Node 20+)

```bash
cd frontend
npm install
npm run dev
```

La aplicación queda disponible en `http://localhost:5173` (puerto por defecto de Vite).

> 📌 En modo local, el CORS del backend ya está preparado para aceptar peticiones desde `localhost:5173`, `localhost:3000`, `localhost:4200` y `localhost:8080`.

---

## 🔗 Flujo de comunicación entre servicios

1. El usuario completa el formulario de consumo en el **frontend** (`POST /api/analisis`) autenticado con un token JWT.
2. El **backend** recibe la petición, valida los datos y llama internamente al servicio de **Data Science** (`POST http://data-science:8000/api/v1/predict/` dentro de Docker).
3. El modelo **XGBoost** ya entrenado predice la categoría de eficiencia y las probabilidades asociadas.
4. Si el servicio de Data Science no responde, el backend recurre automáticamente a un **motor de reglas local** (basado en el ratio consumo/equipos y horas de alto consumo) para no interrumpir la experiencia del usuario.
5. El resultado final se guarda en **MySQL** (tabla `analisis_energetico`) y se devuelve al frontend con la categoría, probabilidad, costo estimado y recomendaciones.

---

## 🔐 Autenticación y usuario administrador

El backend crea automáticamente, al primer arranque, un usuario administrador si aún no existe ninguno:

| Usuario | Contraseña | Roles |
|---|---|---|
| `admin` | `hackathon2026` | `ADMIN`, `USER` |

> ⚠️ Estas credenciales son solo para el entorno de hackathon/desarrollo. **Cámbialas antes de cualquier despliegue real**, y nunca las dejes hardcodeadas en un entorno de producción.

**Login:**

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"hackathon2026"}'
```

**Uso del token en endpoints protegidos:**

```bash
curl http://localhost:8080/api/analisis/historial \
  -H "Authorization: Bearer <token_recibido>"
```

---

## 📖 Documentación interactiva de las APIs

| Servicio | Swagger / Docs | OpenAPI JSON |
|---|---|---|
| Backend (Spring Boot) | http://localhost:8080/swagger-ui.html | http://localhost:8080/v3/api-docs |
| Data Science (FastAPI) | http://localhost:8000/docs (también `/redoc`) | http://localhost:8000/openapi.json |

---

## 🩺 Solución de problemas comunes

| Problema | Causa probable | Solución |
|---|---|---|
| `docker: command not found` | Docker Desktop no está instalado | Instalar Docker Desktop y reiniciar la terminal |
| `Cannot connect to the Docker daemon` | Docker Desktop no está corriendo | Abrir Docker Desktop y esperar a "Engine running" |
| El backend queda reiniciándose (*unhealthy*) | MySQL aún no terminó de inicializar | Esperar unos segundos más; revisar `docker compose logs db` |
| `403 Forbidden` al entrar a `http://localhost:8080/` | Comportamiento esperado: la ruta raíz no es pública | Usa `/swagger-ui.html` o autentícate primero en `/api/auth/login` |
| Puerto ya en uso (`3307`, `8080`, `8000` o `3000`) | Otro proceso local está usando ese puerto | Cambia el puerto en `.env` (MySQL) o libera el puerto ocupado |
| El frontend no logra conectarse al backend | Variable `VITE_API_URL` mal configurada en modo local | Verificar `.env` del frontend o que el backend esté corriendo en `8080` |
| Cambié código pero no se refleja en Docker | La imagen sigue siendo la anterior | Reconstruir con `docker compose up -d --build` |

---

## 🤝 Buenas prácticas y convenciones

Este repositorio sigue un flujo de ramas y commits documentado en `CONTRIBUTING.md`:

- Ramas con formato `tipo/descripcion-corta` (`feat/`, `fix/`, `data/`, `docs/`, etc.).
- Commits en inglés, en imperativo, con formato `tipo(alcance): descripción`.
- Flujo simplificado: `development` como rama de integración, `main` como rama estable.
- Nunca subir `.env`, credenciales, `node_modules/`, `target/` ni modelos pesados sin evaluar antes su tamaño.

---

## 👥 Créditos

Proyecto desarrollado por el **Team 63** para el programa **No Country — ONE G9 LATAM (Alura + Oracle)**, con equipos de Backend, Frontend y Data Science trabajando de forma integrada sobre esta misma base de código.
