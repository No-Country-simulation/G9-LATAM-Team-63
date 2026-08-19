- [Descripción general](#descripción-general)
- [Stack tecnológico](#stack-tecnologico)
- [Arquitectura del proyecto](#arquitectura-del-proyecto)

## Descripción general
- ⚙️ **Backend** - API REST con Spring Boot
- 🖥️ **Frontend** - Interfaz de usuario
- 🤖 **Data Science** - Servicios de análisis y modelos ML
- 🗄️ **MySQL** - Base de datos relacional

## Stack tecnológico
| Servicio | Tecnología | Puerto |
|----------|-----------|--------|
| Backend | Java 17 + Spring Boot 3.x | 8080 |
| Frontend | React / Vue / Angular | 3000 |
| Data Science | Python 3.11 + FastAPI | 5000 |
| Base de datos | MySQL 8.0 | 3306 |

## Arquitectura del proyecto
─────────────┐ ┌──────────────┐ ┌─────────────┐
│ Frontend │────▶│ Backend │────▶│ MySQL │
│ :3000 │ │ :8080 │ │ :3306 │
└─────────────┘ └──────┬───────┘ └─────────────┘
│
┌──────▼───────┐
│ Data Science │
│ :5000 │
└──────────────┘