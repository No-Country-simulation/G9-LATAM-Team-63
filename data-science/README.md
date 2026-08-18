# Data Science – EnergiAI

Módulo de ciencia de datos del proyecto EnergiAI. Incluye la generación de datasets sintéticos, análisis exploratorio de datos, entrenamiento de modelos de clasificación de eficiencia energética (XGBoost) y una API REST (FastAPI) para servir las predicciones al backend.

## Tabla de contenido

- [Data Science – EnergiAI](#data-science--energiai)
  - [Tabla de contenido](#tabla-de-contenido)
  - [Descripción general](#descripción-general)
  - [Stack tecnológico](#stack-tecnológico)
  - [Estructura de carpetas](#estructura-de-carpetas)
  - [Requisitos previos](#requisitos-previos)
  - [Configuración del entorno](#configuración-del-entorno)
  - [Generación de datasets](#generación-de-datasets)
  - [Análisis exploratorio de datos (EDA)](#análisis-exploratorio-de-datos-eda)
  - [Entrenamiento de modelos](#entrenamiento-de-modelos)
  - [API de predicción (FastAPI)](#api-de-predicción-fastapi)
  - [Ejecución con Docker](#ejecución-con-docker)
  - [Notas de seguridad](#notas-de-seguridad)
  - [Créditos](#créditos)

---

## Descripción general

El módulo de data science de EnergiAI implementa la capa de inteligencia del proyecto. A través de un pipeline completo (generación de datos, EDA, entrenamiento con XGBoost y API REST), transforma los datos de consumo de un inmueble en una clasificación de eficiencia (`Eficiente`, `Moderado`, `Ineficiente`), junto con recomendaciones personalizadas y un costo estimado mensual.

El flujo de trabajo se divide en cuatro fases principales:

1. **Generación de datos sintéticos:** Se crean datasets con características realistas para entrenar y validar los modelos.
2. **Análisis exploratorio de datos (EDA):** Se estudian las variables, sus relaciones y se define la lógica de clasificación.
3. **Entrenamiento de modelos:** Se prueban varios algoritmos (Regresión Logística, Árbol de Decisión, Random Forest, XGBoost) y se selecciona el mejor (XGBoost) basado en métricas de rendimiento.
4. **API de predicción:** Se expone un endpoint REST (FastAPI) que recibe los datos del usuario, aplica el preprocesamiento, ejecuta el modelo y devuelve la clasificación, probabilidad, recomendaciones y costo estimado.

---

## Stack tecnológico

| Tecnología | Uso |
|------------|-----|
| **Python 3.14.6** | Lenguaje base del módulo |
| **Pandas** | Manipulación y análisis de datos |
| **NumPy** | Operaciones numéricas |
| **Matplotlib / Seaborn** | Visualización de datos |
| **Scikit-learn** | Preprocesamiento, modelos base, métricas |
| **XGBoost** | Modelo de clasificación final (mejor rendimiento) |
| **FastAPI** | Framework para la API de predicción |
| **Uvicorn** | Servidor ASGI para ejecutar FastAPI |
| **Pydantic** | Validación de datos de entrada/salida |
| **Joblib** | Serialización de modelos y preprocesadores |
| **Jupyter Notebook** | Entorno interactivo para EDA y entrenamiento |

---

## Estructura de carpetas

```text
data-science/
│
├── api/                           # API de predicción (FastAPI)
│   ├── core/
│   │   └── config.py              # Configuración (rutas, parámetros)
│   ├── models/
│   │   └── schemas.py             # Esquemas Pydantic (request/response)
│   ├── routers/
│   │   └── predict.py             # Endpoint de predicción
│   ├── services/
│   │   └── prediction_service.py  # Lógica de predicción y carga de modelos
│   ├── utils/                     # Utilidades
│   ├── main.py                    # Punto de entrada de la API
│   └── __init__.py
│
├── data/
│   └── processed/                 # Datasets generados
│       ├── dataset_inicial.csv
│       └── dataset_extendido.csv
│
├── models/
│   └── saved/                     # Modelos y preprocesadores serializados
│       ├── xgboost_model.pkl
│       ├── scaler.pkl
│       ├── preprocessor.pkl
│       ├── label_encoder.pkl
│       ├── feature_names.pkl
│       └── model_metadata.json    # Métricas y metadatos de los modelos
│
├── notebooks/                     # Notebooks de análisis y entrenamiento
│   ├── 01_Generacion_Dataset.ipynb
│   ├── 02_EDA_Analisis_Exploratorio.ipynb
│   ├── 03_EDA_Dataset_Extendido.ipynb
│   └── 04_Entrenamiento_Modelos.ipynb
│
├── scripts/
│   └── generador_datos.py         # Script con funciones para generar datasets
│
├── Dockerfile
├── README.md                      # Este archivo
├── requirements.txt               # Dependencias del entorno
├── .dockerignore
└── .gitignore
```

---

## Requisitos previos

- **Python 3.12 o superior**.
- **pip** (gestor de paquetes de Python).
- **venv** para crear un entorno virtual aislado.

---

## Configuración del entorno

1. **Clonar el repositorio**:
```bash
git clone https://github.com/No-Country-simulation/G9-LATAM-Team-63.git
cd G9-LATAM-Team-63/data-science
```

2. **Crear y activar un entorno virtual**:
```bash
python -m venv .venv
source .venv/bin/activate      # Linux/macOS
.venv\Scripts\activate         # Windows
```

3. **Instalar las dependencias**:
```bash
pip install -r requirements.txt
```

---

## Generación de datasets
Los datasets se generan de forma sintética usando el script `scripts/generador_datos.py`. Las funciones principales son:

- `generar_dataset_inicial(n, seed, output_path)`: Genera el dataset con las 5 variables.

- `generar_dataset_extendido(n, seed, output_path)`: Genera el dataset extendido con 9 variables.

Para ejecutar la generación desde un notebook:

```python
from scripts.generador_datos import generar_dataset_extendido

df = generar_dataset_extendido(n=3000)
```
Los datasets se guardan en `data/processed/`.

---

## Análisis exploratorio de datos (EDA)
Se realizaron dos EDAs completos:

- `02_EDA_Analisis_Exploratorio.ipynb`: Sobre el dataset inicial (5 variables). Se analizaron distribuciones, correlaciones y se confirmó que la variable `ratio` (consumo/equipos) es el principal diferenciador.

- `03_EDA_Dataset_Extendido.ipynb`: Sobre el dataset extendido. Se validó que las nuevas variables (calefaccion, aire_acondicionado) aportan información, aunque con menor poder discriminante individual. Se confirmó el desbalanceo de la variable objetivo (categoria).

---

## Entrenamiento de modelos
En el notebook `04_Entrenamiento_Modelos.ipynb` se realizó:

1. **Preprocesamiento**: Codificación de variables categóricas (OneHotEncoder) y escalado de numéricas (StandardScaler).

2. **División de datos**: 80% entrenamiento, 20% prueba, estratificado por categoria.

3. **Entrenamiento de modelos**:
   - Regresión Logística
   - Árbol de Decisión
   - Random Forest
   - XGBoost

4. **Evaluación y selección**: XGBoost obtuvo el mejor rendimiento (F1-score weighted = 0.9780).

5. **Serialización**: El modelo y los preprocesadores se guardaron en `models/saved/` con `joblib`.

---

## API de predicción (FastAPI)

La API se encuentra en la carpeta `api/` y expone un endpoint REST para realizar predicciones.

**Ejecución local**
```bash
cd data-science
uvicorn api.main:app --host 0.0.0.0 --port 8000 --reload
```

**Endpoint principal**
- URL: `POST /api/v1/predict/`

- **Autenticación**: No requiere (se asume que el backend Java maneja la autenticación y llama a este servicio internamente). Si se desea, se puede añadir una API Key o JWT.

**Ejemplo de petición (JSON)**
```json
{
  "consumo_kwh": 600,
  "cantidad_equipos": 12,
  "horas_alto_consumo": 6,
  "tipo_inmueble": "Casa",
  "uso_horario_pico": true,
  "numero_habitantes": 4,
  "antiguedad_inmueble": 20,
  "calefaccion": false,
  "aire_acondicionado": true
}
```

**Ejemplo de respuesta (JSON)**
```json
{
  "categoria": "Moderado",
  "probabilidad": 0.9998527765274048,
  "costo_estimado_mensual": 450,
  "recomendaciones": [
    "Identificar oportunidades de ahorro en horarios pico.",
    "Revisar equipos con mayor consumo por unidad.",
    "Considerar reemplazo por equipos con certificación energética."
  ],
  "distancias": {
    "Eficiente": 0.00011930661275982857,
    "Ineficiente": 0.0000278394109045621,
    "Moderado": 0.9998527765274048
  }
}
```

**Documentación interactiva**
- Swagger UI: `http://localhost:8000/docs`

- ReDoc: `http://localhost:8000/redoc`

---

## Ejecución con Docker

**1. Construir la imagen**
Desde la carpeta `data-science/`:

```bash
docker build -f Dockerfile -t energi-api .
```

**2. Ejecutar el contenedor**
```bash
docker run -d --name energi-fastapi -p 8000:8000 energi-api
```

La API estará disponible en `http://localhost:8000`.

**3. Verificar logs**
```bash
docker logs energi-fastapi
```

**4. Detener y eliminar el contenedor**
```bash
docker stop energi-fastapi
docker rm energi-fastapi
```

---

## Notas de seguridad
- La API no incluye autenticación porque se asume que está en una red interna y el backend Java valida al usuario.

- Si se expone directamente al exterior, se recomienda añadir autenticación (JWT o API Key) y usar HTTPS.

- Los modelos y preprocesadores están serializados; asegurarse de que solo el proceso de la API tenga acceso de lectura.

---

## Créditos
- **Equipo de Data Science**: Sandro Sanca.

- **Hackathon No Country** ONE G9 - LATAM – Alura + Oracle.