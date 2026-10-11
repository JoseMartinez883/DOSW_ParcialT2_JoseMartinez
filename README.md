# DOSW_ParcialT2_PrimerNombrePrimerApellido

## Índice
1. [Datos del estudiante](#1-datos-del-estudiante)
2. [Descripción del proyecto](#2-descripción-del-proyecto)
3. [Despliegue](#3-despliegue)
4. [Estructura del proyecto](#4-estructura-del-proyecto)
5. [Tecnologías y dependencias](#5-tecnologías-y-dependencias)
6. [Configuración y variables de entorno](#6-configuración-y-variables-de-entorno)
7. [Cómo ejecutar](#7-cómo-ejecutar)
8. [Pruebas](#8-pruebas)
9. [Docker](#9-docker)
10. [CI/CD](#10-cicd)
11. [Herramientas de modelado y diseño (evidencias)](#11-herramientas-de-modelado-y-diseño-evidencias)

## 1. Datos del estudiante
- **Nombre completo:** [Tu Nombre Completo Aquí]
- **Grupo:** [Tu Grupo Aquí]
- **Profesor agregado al repositorio:** ✔

## 2. Descripción del proyecto
[Qué es el servicio y para qué se usa (3–5 líneas)]

## 3. Despliegue
- **URL del servicio en Azure:** [URL Aquí]
- **URL del health:** [URL Aquí]
- **URL de Swagger:** [URL Aquí]

## 4. Estructura del proyecto
- `controller`: Controladores REST, DTOs de request/response y mappers de entrada.
- `core`: Lógica de negocio, servicios, modelos de dominio y validadores.
- `persistence`: Entidades MongoDB, repositorios y mappers de salida.
- `config`: Configuraciones globales como Seguridad y Swagger.
- `main`: Clase principal de la aplicación Spring Boot.

## 5. Tecnologías y dependencias
| Tecnología | Versión | Para qué se usa |
|---|---|---|
| Spring Boot | 3.2.5 | Framework base |
| Java | 21 | Lenguaje de programación |
| MongoDB | - | Base de datos NoSQL |
| JWT | 0.12.3 | Autenticación y Autorización |
| MapStruct | 1.5.5.Final | Mapeo de objetos |
| Lombok | - | Reducción de boilerplate |

## 6. Configuración y variables de entorno
| Variable | Descripción | Ejemplo |
|---|---|---|
| MONGODB_URI | URI de conexión a MongoDB | mongodb://localhost:27017/db |
| JWT_SECRET | Secreto para firma de tokens (Base64) | [secreto_base64_aqui] |
| PORT | Puerto del servidor | 8080 |

## 7. Cómo ejecutar
- **Local con Maven:** `mvn spring-boot:run`
- **Local con Docker:** `docker run -p 8080:8080 -e JWT_SECRET=... dosw-parcial-t2`
- **Mongo local con Docker:** `docker run -d -p 27017:27017 --name mongo-parcial mongo:7`

## 8. Pruebas
Comando: `mvn test`
[Captura del resultado en verde aquí]
[Captura del reporte Jacoco aquí]

## 9. Docker
Comandos build y run:
`docker build -t dosw-parcial:local .`
`docker run -p 8080:8080 -e JWT_SECRET=<su_secret_base64> dosw-parcial:local`
[Captura del contenedor corriendo aquí]

## 10. CI/CD
El pipeline realiza build, testing, creación de imagen Docker, publicación en Docker Hub y despliegue a Azure Web App.
[Captura del workflow en verde aquí]
Secrets usados: `DOCKERHUB_USERNAME`, `DOCKERHUB_TOKEN`, `AZURE_CREDENTIALS`, `AZURE_WEBAPP_NAME`, `JWT_SECRET`

## 11. Herramientas de modelado y diseño (evidencias)
[Captura de la cuenta en Lucidchart / Draw.io / Miro aquí]
[Captura de la cuenta en Figma aquí]