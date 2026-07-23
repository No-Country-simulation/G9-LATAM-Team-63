# Guía de Contribución: EnergiAI - Team 63

Este documento define las convenciones de trabajo para mantener un flujo ordenado, claro y escalable dentro del equipo multidisciplinario (Backend, Data Science, Frontend).

---

## 1. Ramas (Branches)

**Formato:** 
```bash
tipo/descripcion-corta
```

**Tipos permitidos:**

| Tipo       | Cuándo usarlo |
|------------|---------------|
| `feat`     | Nueva funcionalidad (endpoint, componente, etc.) |
| `fix`      | Corrección de bugs |
| `refactor` | Reestructuración sin cambiar comportamiento |
| `data`     | Cambios en datasets, modelos entrenados, o lógica de clasificación |
| `docs`     | Documentación únicamente (README, comentarios, etc.) |
| `test`     | Agregar o modificar pruebas |
| `chore`    | Mantenimiento, configuraciones, tooling, actualización de dependencias |
| `perf`     | Mejoras de rendimiento (consultas, optimización de código) |

**Ejemplos válidos:**
- `feat/api-analysis-endpoint`
- `fix/null-handling-in-dto`
- `data/update-classification-thresholds`
- `docs/update-swagger-docs`
- `chore/update-spring-boot-version`
- `perf/optimize-model-inference`

---

## 2. Commits

**Formato:**
```bash
tipo(alcance): descripción en imperativo
```

**Alcances típicos (según capa):**
- Para backend (Java/Spring): `api`, `service`, `repository`, `dto`, `config`, `security`, `oci`
- Para data science (Python): `notebook`, `dataset`, `model`, `eda`, `features`
- Generales: `docs`, `tests`, `ci`, `deps`

**Ejemplos válidos:**
- `feat(api): add POST /analisis-energetico endpoint`
- `fix(service): handle missing fields in request`
- `data(dataset): generate synthetic consumption data with new thresholds`
- `docs(readme): update OCI setup instructions`
- `chore(deps): update scikit-learn to 1.3.0`
- `perf(model): reduce inference time with smaller tree depth`

**Reglas:**
- Descripción en imperativo (ej: "add", no "added")
- En minúscula
- Máximo 72 caracteres en la primera línea
- Si necesitas más detalle, usa un cuerpo opcional después de una línea en blanco.
- Mantén el idioma consistente: **siempre en inglés** para commits y mensajes.

---

## 3. Reglas de Ramas y Flujo de Trabajo

Este equipo utiliza un flujo simplificado sin Pull Requests formales debido a la experiencia del equipo. Se sigue este proceso:

- **Rama `main`**: contiene el código estable y en producción. Solo se fusiona desde `development` cuando se complete un hito o una versión.
- **Rama `development`**: es la rama de integración principal. Todos los cambios se fusionan aquí primero.


**Flujo de trabajo diario:**
1. Asegúrate de estar en `development` y actualizado: `git checkout development && git pull origin development`
2. Resuelve conflictos si los hay, y haz commit del merge.
3. Sube los cambios: `git push origin development`
4. Periódicamente (al final de cada sprint o hito), el Tech Lead o responsable fusiona `development` a `main`:
   - `git checkout main && git pull origin main`
   - `git merge --no-ff development`
   - `git push origin main`

**Reglas importantes:**
- **No hacer push directo a `main`**. Solo se actualiza mediante merge desde `development`.
- **No hacer push directo a `development`** sin pasar por una rama de características (excepto para cambios menores de documentación o configuración, con previo acuerdo).
- Mantén las ramas **pequeñas y enfocadas** en un solo objetivo.
- Resuelve los conflictos localmente antes de subir tu rama.
- Asegúrate de que tu código compile y pase las pruebas básicas antes de fusionar.

---

## 4. Archivos que NUNCA deben incluirse en un commit

- Credenciales: `.env`, `.env.local`, `*.key`, `*.pem`, `*.json` con secretos
- Datos pesados: `data/processed/*.csv`, `*.parquet` (si superan 1 MB) – mejor subirlos a OCI Object Storage
- Entornos virtuales: `.venv/`, `venv/`, `env/`, `__pycache__/`, `*.pyc`
- Archivos de build: `backend/target/`, `node_modules/`, `dist/`, `build/`
- Configuraciones locales del editor: `.vscode/`, `.idea/`, `*.swp`
- Archivos de logs: `*.log`

---

## 5. Recomendaciones Generales

- Escribe código limpio, claro y con nombres expresivos.
- Añade **type hints** en Python y **tipos** en Java.
- Documenta funciones o métodos complejos con comentarios claros.
- Mantén consistencia en estilo y convenciones (PEP 8 para Python, Oracle/Spring para Java).
- Si una rama crece demasiado, divídela en partes lógicas.
- Actualiza siempre la documentación (README, Swagger, etc.) junto con el código.

---

## 6. Dudas o Soporte

Antes de hacer un merge o si tienes dudas sobre el flujo:
- Pregunta primero en el canal de Discord del equipo (Team 63).