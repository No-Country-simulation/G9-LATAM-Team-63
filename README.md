# 🐳 Energía Backend - Configuración Docker

Guía completa para levantar todos los contenedores del proyecto (Backend, Frontend, Data Science y MySQL) usando Docker.

## Tabla de contenido

- [Descripción general](#descripcion-general)
- [Stack tecnológico](#stack-tecnologico)
- [Arquitectura del proyecto](#arquitectura-del-proyecto)
- [Estructura de carpetas](#estructura-de-carpetas)
- [Requisitos previos](#requisitos-previos)
- [Configuración](#configuracion)
- [Ejecución con Docker](#ejecucion-con-docker)
- [Verificación de servicios](#verificacion-de-servicios)
- [Comandos útiles](#comandos-utiles)
- [Variables de entorno](#variables-de-entorno)
- [Base de datos MySQL](#base-de-datos-mysql)
- [Solución de problemas](#solucion-de-problemas)
- [Notas de seguridad](#notas-de-seguridad)
- [Requisitos previos para Docker](#requisitos-previos-para-docker)
- [Creación de contenedores individuales](#creacion-de-contenedores-individuales)

---

## Descripción general

Este proyecto utiliza **Docker Compose** para orquestar todos los servicios necesarios:

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
