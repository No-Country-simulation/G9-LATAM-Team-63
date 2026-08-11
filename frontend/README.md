# EnergiAI Frontend

SPA (Single Page Application) desarrollada con React para analizar consumo energético, clasificar la eficiencia del inmueble, estimar el costo mensual y mostrar recomendaciones personalizadas. El frontend se conecta al backend Spring Boot de EnergiAI, incluye autenticación JWT (login y registro), rutas protegidas, historial local de análisis y tema claro/oscuro.

## Tabla de contenido

- [Descripción general](#descripción-general)
- [Stack tecnológico](#stack-tecnológico)
- [Arquitectura del proyecto](#arquitectura-del-proyecto)
- [Estructura de carpetas](#estructura-de-carpetas)
- [Requisitos previos](#requisitos-previos)
- [Configuración](#configuración)
- [Ejecución local](#ejecución-local)
- [Scripts disponibles](#scripts-disponibles)
- [Rutas de la aplicación](#rutas-de-la-aplicación)
- [Autenticación](#autenticación)
- [Formulario de análisis](#formulario-de-análisis)
- [Respuesta del análisis](#respuesta-del-análisis)
- [Historial de análisis](#historial-de-análisis)
- [Manejo de errores](#manejo-de-errores)
- [Notas de seguridad](#notas-de-seguridad)

## Descripción general

La aplicación permite a un usuario registrado completar un formulario con los datos de consumo de su inmueble y obtener un análisis energético real del backend. El frontend:

- Muestra una landing page con las características del producto.
- Permite registrar e iniciar sesión con JWT (el registro autentica automáticamente).
- Protege el análisis y el historial: solo usuarios con sesión activa pueden usarlos.
- Envía el formulario completo de 9 campos al backend sin fallback local.
- Muestra los resultados: categoría, probabilidad, costo estimado mensual y recomendaciones.
- Guarda un historial local (localStorage) de los últimos 50 análisis.
- Ofrece tema claro/oscuro persistente y navegación móvil.

## Stack tecnológico

| Tecnología | Uso |
| --- | --- |
| React 19 | Librería base de la interfaz |
| TypeScript | Tipado estático del proyecto |
| Vite | Bundler y servidor de desarrollo |
| React Router 7 | Enrutado de la SPA y rutas protegidas |
| react-hook-form | Manejo de formularios |
| Zod 4 | Schemas de validación de formularios |
| @hookform/resolvers | Integración de Zod con react-hook-form |
| TanStack Query 5 | Mutaciones y estado del servidor |
| Zustand 5 | Estado global (auth, UI, análisis) |
| ESLint | Lint del código |

## Arquitectura del proyecto

El proyecto sigue una arquitectura por features:

- `features/<feature>/pages`: páginas de cada feature (landing, auth, analysis, results, history, help).
- `features/<feature>/components`: componentes propios del feature.
- `features/<feature>/data`: API, hooks y tipos locales del feature.
- `data`: capa de datos global (cliente HTTP, API, hooks, servicios y tipos).
- `store`: stores de Zustand (auth, UI, análisis).
- `navigation`: router de la aplicación y rutas protegidas.
- `shared/components`: componentes reutilizables (Button, Input, Icons, Loader, etc.).
- `styles`: design system, tema, animaciones y CSS por componente.

## Estructura de carpetas

```text
frontend
├── index.html
├── package.json
├── tsconfig.json
├── vite.config.ts
├── public/
│   ├── favicon.svg
│   └── icons.svg
└── src
    ├── main.tsx
    ├── App.tsx
    ├── config/constants.ts
    ├── data
    │   ├── api
    │   │   ├── client.ts
    │   │   └── analysis.ts
    │   ├── hooks
    │   │   ├── useAnalysis.ts
    │   │   └── useHistory.ts
    │   ├── services/analysisService.ts
    │   └── types/analysis.ts
    ├── features
    │   ├── analysis
    │   │   ├── pages/AnalysisPage.tsx
    │   │   ├── components/ConsumptionForm.tsx
    │   │   └── schema.ts
    │   ├── auth
    │   │   ├── pages/LoginPage.tsx
    │   │   ├── pages/RegisterPage.tsx
    │   │   └── data
    │   │       ├── api/auth.ts
    │   │       ├── hooks/useAuth.ts
    │   │       └── types/auth.ts
    │   ├── landing
    │   │   ├── pages/LandingPage.tsx
    │   │   ├── components/
    │   │   └── data/
    │   ├── results
    │   │   ├── pages/ResultsPage.tsx
    │   │   └── components/
    │   ├── history/pages/HistoryPage.tsx
    │   └── help/pages/HelpPage.tsx
    ├── navigation/AppRouter.tsx
    ├── shared/components/
    ├── store
    │   ├── analysisStore.ts
    │   ├── authStore.ts
    │   └── uiStore.ts
    └── styles
        ├── design-system.css
        ├── theme.css
        ├── animations.css
        └── components/
```

## Requisitos previos

- Node.js (versión compatible con Vite, recomendado Node 20+).
- npm (incluido con Node.js).
- El backend corriendo en `http://localhost:8080` (ver README de la carpeta `backend`).

## Configuración

La URL base de la API se define en `src/config/constants.ts`:

```ts
export const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'
```

Para apuntar a otra API, crear un archivo `.env` en la raíz de `frontend/`:

```env
VITE_API_URL=http://localhost:8080/api
```

Nota CORS: el backend permite orígenes de desarrollo locales, incluyendo `http://localhost:5173` (puerto por defecto de Vite).

## Ejecución local

Desde la carpeta `frontend/`:

```bash
npm install
npm run dev
```

La aplicación queda disponible en:

```text
http://localhost:5173
```

## Scripts disponibles

| Comando | Descripción |
| --- | --- |
| `npm run dev` | Servidor de desarrollo con HMR |
| `npm run build` | Compila TypeScript y genera el build de producción (`dist/`) |
| `npm run lint` | Ejecuta ESLint sobre el código |
| `npm run preview` | Sirve el build de producción localmente |

## Rutas de la aplicación

| Ruta | Acceso | Descripción |
| --- | --- | --- |
| `/` | Pública | Landing page |
| `/login` | Pública | Inicio de sesión |
| `/registro` | Pública | Registro de usuario |
| `/analizar` | Requiere sesión | Formulario de análisis de consumo |
| `/historial` | Requiere sesión | Historial de análisis |
| `/resultados` | Pública* | Resultados del último análisis |
| `/resultados/:id` | Pública* | Resultado de un análisis guardado |
| `/ayuda` | Pública | Página de ayuda |

*`/resultados` redirige a `/analizar` si no existe un resultado en el store.

## Autenticación

La autenticación usa JWT. El token se guarda en `localStorage` (claves `energiai_token` y `energiai_username`) y se restaura al recargar la página.

### Flujo de registro

1. El usuario envía `username` y `password` a `POST /api/auth/register`.
2. El backend responde `201` con el token JWT incluido.
3. El frontend autentica automáticamente con ese token y redirige a `/analizar` (no requiere login adicional).

### Flujo de login

1. El usuario envía `username` y `password` a `POST /api/auth/login`.
2. El backend responde `200` con `{ "token": "..." }`.
3. El frontend guarda la sesión y redirige a la ruta protegida a la que intentaba entrar.

### Cierre de sesión

El botón "Cerrar sesión" del navbar limpia el token y el username de `localStorage` y redirige a `/`.

### Rutas protegidas

- `ProtectedRoute` (en `src/navigation/AppRouter.tsx`) redirige a `/login` con `state.from` si no hay sesión, para volver a la ruta original tras autenticarse.
- Cuando el backend responde `401`, el cliente HTTP limpia la sesión y emite el evento `energiai:unauthorized`, que redirige a `/login`.

### Inyección del token

`src/data/api/client.ts` lee el token de `localStorage` y agrega automáticamente el header `Authorization: Bearer <token>` a todas las peticiones.

## Formulario de análisis

El formulario replica exactamente el contrato de `AnalisisRequest` del backend. Todos los campos son obligatorios:

| Campo | Tipo | Reglas |
| --- | --- | --- |
| `consumoKwh` | número | Mayor a 0 (consumo mensual en kWh) |
| `usoHorarioPico` | booleano | Checkbox (consumo en horario pico 18:00–22:00) |
| `cantidadEquipos` | entero | Entre 1 y 50 |
| `tipoInmueble` | select | `Casa`, `Apartamento`, `Local` u `Oficina` |
| `horasAltoConsumo` | entero | Entre 0 y 24 |
| `numeroHabitantes` | entero | Entre 1 y 6 |
| `antiguedadInmueble` | entero | Entre 0 y 50 años |
| `calefaccion` | booleano | Checkbox (calefacción eléctrica) |
| `aireAcondicionado` | booleano | Checkbox (aire acondicionado) |

Los valores por defecto facilitan probar el flujo rápidamente:

```ts
{ consumoKwh: 420, usoHorarioPico: true, cantidadEquipos: 10, tipoInmueble: 'Casa',
  horasAltoConsumo: 8, numeroHabitantes: 3, antiguedadInmueble: 10,
  calefaccion: false, aireAcondicionado: true }
```

El envío es 100% real: si el backend falla, el error se muestra al usuario (no existe fallback local).

## Respuesta del análisis

`POST /api/analisis` responde `201` con la siguiente estructura:

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

| Campo | Descripción |
| --- | --- |
| `categoria` | `Eficiente`, `Moderado` o `Ineficiente` |
| `probabilidad` | Probabilidad de la clasificación (0 a 1) |
| `recomendaciones` | Lista de recomendaciones personalizadas |
| `costo_estimado_mensual` | Costo mensual estimado (nota: llega en snake_case desde el backend) |
| `idAnalisis` | ID del análisis persistido |

La página de resultados muestra estos datos en tres tarjetas: clasificación, costo estimado y recomendaciones.

## Historial de análisis

El historial se mantiene local en `localStorage` (clave `energiai_history`, máx. 50 entradas) porque el backend aún no expone un endpoint `/historial`. Cada análisis exitoso se guarda automáticamente con su entrada, resultado y fecha.

## Manejo de errores

El cliente HTTP (`src/data/api/client.ts`) define `ApiError` con `status` y `details`:

- **400 Bad Request**: `{ "error": "...", "detalles": { "<campo>": "<mensaje>" } }` — el formulario mapea los mensajes de `detalles` al error de cada campo.
- **401 Unauthorized**: limpia la sesión y redirige a `/login`.
- **Otros errores**: se muestra el mensaje de `error` del backend.

## Notas de seguridad

Este proyecto está configurado para un entorno de hackathon/desarrollo. Antes de producción, se recomienda:

- Servir la app con HTTPS y validar el origen en CORS del backend.
- Usar cookies `httpOnly` + `Secure` en lugar de `localStorage` para el token JWT (mitiga XSS).
- No exponer secretos ni credenciales en el frontend.
- Añadir tests (unitarios y e2e) al flujo de autenticación y análisis.
