# DOSW_ParcialT2_JoseMartinez

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

- **Nombre completo:** Jose Alejandro Martinez Arias
- **Grupo:** 1

## 2. Descripción del proyecto

Servicio backend base desarrollado con Spring Boot 3 y persistencia en MongoDB para el Parcial Práctico del Segundo Tercio de DOSW. Implementa una arquitectura desacoplada por capas (MVC), endpoints de salud y documentación interactiva con OpenAPI/Swagger, preparado para despliegue continuo en Azure App Service mediante Docker y GitHub Actions.

## 3. Despliegue

- **URL del servicio en Azure:** https://dosw-parcial-josemartinez-f4hwgudtgwcqgdb8.westus3-01.azurewebsites.net
- **URL del health:** https://dosw-parcial-josemartinez-f4hwgudtgwcqgdb8.westus3-01.azurewebsites.net/api/v1/health
- **URL de Swagger:** https://dosw-parcial-josemartinez-f4hwgudtgwcqgdb8.westus3-01.azurewebsites.net/swagger-ui/index.html

### Evidencia de endpoint de salud (`/api/v1/health`)

![Evidencia Health](docs/images/HealthEvidence.png)

### Evidencia de interfaz Swagger UI

![Evidencia Swagger](docs/images/SwaggerEvidence.png)

## 4. Estructura del proyecto

```text
DOSW_ParcialT2_JoseMartinez/
├── docs/
│   ├── uml/                      # Diagramas fuente (Draw.io / Lucidchart / Miro)
│   └── images/                   # Exportes PNG/SVG y evidencias del README
│
├── src/
│   ├── main/
│   │   ├── java/edu/dosw/parcial/
│   │   │   ├── controller/
│   │   │   │   ├── dtos/
│   │   │   │   │   ├── request/  # DTOs de entrada con validaciones
│   │   │   │   │   └── response/ # DTOs de salida
│   │   │   │   ├── mappers/      # Mapper IN: DTO ↔ Dominio
│   │   │   │   └── handlers/     # Manejador global de excepciones
│   │   │   │
│   │   │   ├── core/
│   │   │   │   ├── services/     # Servicios y lógica de negocio
│   │   │   │   ├── models/       # Objetos y entidades de dominio
│   │   │   │   ├── validators/   # Validadores de negocio
│   │   │   │   └── utils/        # Utilidades transversales
│   │   │   │
│   │   │   ├── persistence/
│   │   │   │   ├── entities/     # Clases @Document para MongoDB
│   │   │   │   ├── repositories/ # Interfaces MongoRepository
│   │   │   │   └── mappers/      # Mapper OUT: Dominio ↔ Document
│   │   │   │
│   │   │   ├── config/           # Configuraciones (SecurityConfig, Swagger)
│   │   │   └── main/             # Clase principal con @SpringBootApplication
│   │   │
│   │   └── resources/
│   │       └── application.yaml  # Configuración externa de la aplicación
│   │
│   └── test/
│       ├── java/edu/dosw/parcial/
│       │   ├── controller/       # Pruebas unitarias de controladores
│       │   ├── core/             # Pruebas unitarias de lógica y servicios
│       │   └── persistence/      # Pruebas de persistencia y repositorios
│       └── resources/            # Recursos y configuraciones de test
│
├── Dockerfile                    # Construcción de imagen multietapa
├── .dockerignore                 # Archivos excluidos del build de Docker
├── .github/workflows/ci-cd.yml   # Pipeline automatizado de CI/CD
├── pom.xml                       # Dependencias, plugins y compilador Maven
├── .gitignore                    # Reglas de exclusión de Git
└── README.md                     # Documentación principal del proyecto
```

### Responsabilidad por paquetes:

- `docs/uml/`: Almacena los diagramas fuente de diseño y modelado (Draw.io, Miro, Lucidchart).
- `docs/images/`: Almacena capturas de pantalla, evidencias de cuentas y pruebas.
- `controller`: Exposición de endpoints REST y recepción de peticiones HTTP.
  - `controller/dtos`: Objetos de transferencia de datos (`request` con `@Valid` y `response`).
  - `controller/mappers`: Mapper IN para transformar DTOs hacia objetos de dominio y viceversa.
  - `controller/handlers`: Captura centralizada de errores (`GlobalExceptionHandler`).
- `core`: Capa central pura donde reside la lógica de negocio, independiente de la base de datos.
  - `core/services`: Interfaces e implementaciones de las reglas de negocio.
  - `core/models`: Modelos y estructuras de dominio.
  - `core/validators`: Validadores de lógica y reglas de dominio.
  - `core/utils`: Clases auxiliares y utilidades comunes.
- `persistence`: Capa de interacción y almacenamiento en base de datos MongoDB.
  - `persistence/entities`: Documentos de MongoDB mapeados con `@Document`.
  - `persistence/repositories`: Interfaces que extienden de `MongoRepository`.
  - `persistence/mappers`: Mapper OUT para transformar objetos de dominio a documentos MongoDB.
- `config`: Configuración de seguridad (Spring Security, JWT) y OpenAPI (Swagger).
- `main`: Contiene `ParcialApplication.java`, punto de entrada de arranque de Spring Boot.
- `resources`: Propiedades de la aplicación (`application.yaml`) con lectura de variables de entorno.
- `test`: Banco de pruebas unitarias y de integración para garantizar calidad y cobertura.

## 5. Tecnologías y dependencias

| Tecnología / Dependencia            | Versión    | Para qué se usa                                                              |
| ------------------------------------ | ----------- | ----------------------------------------------------------------------------- |
| Spring Boot                          | 3.2.5       | Framework base para el desarrollo del backend REST                            |
| Java (JDK)                           | 21          | Lenguaje de programación base (LTS)                                          |
| Spring Data MongoDB                  | Starter     | Conexión, repositorios y operaciones sobre MongoDB                           |
| Spring Boot Validation               | Starter     | Validación de payloads y DTOs con anotaciones (`@Valid`, etc.)             |
| Spring Security                      | Starter     | Filtros de autorización, reglas de acceso y seguridad base                   |
| JJWT (api, impl, jackson)            | 0.12.3      | Generación, firmado y validación de tokens JWT                              |
| Springdoc OpenAPI                    | 2.5.0       | Documentación interactiva de la API con Swagger UI                           |
| MapStruct                            | 1.5.5.Final | Mapeo rápido y desacoplado entre DTOs, entidades y dominio                   |
| Project Lombok                       | 1.18.38     | Reducción de código repetitivo (getters, setters, constructores,`@Slf4j`) |
| Spring Boot Test (JUnit 5 + Mockito) | Starter     | Pruebas unitarias y de integración                                           |
| JaCoCo Maven Plugin                  | 0.8.12      | Medición y reporte de cobertura de código                                   |
| Sonar Scanner Plugin                 | 3.10.0.2594 | Análisis estático de código para calidad y seguridad                       |
| Docker                               | 20+         | Contenedorización multietapa para ejecución reproducible                    |

## 6. Configuración y variables de entorno

| Variable          | Descripción                                                         | Ejemplo (sin credenciales reales)                                        |
| ----------------- | -------------------------------------------------------------------- | ------------------------------------------------------------------------ |
| `MONGODB_URI`   | Cadena de conexión a MongoDB (local o en la nube)                   | `mongodb+srv://user:pass@cluster.mongodb.net/dosw_parcial`             |
| `JWT_SECRET`    | Clave secreta en Base64 (mínimo 44 caracteres) para firma de tokens | `bXlfc3VwZXJfc2VjcmV0X2tleV9mb3Jfand0X3ZhbGlkYXRpb25fZG9zd18yMDI0Cg==` |
| `PORT`          | Puerto de escucha HTTP del servidor Spring Boot                      | `8080`                                                                 |
| `WEBSITES_PORT` | Puerto de enrutamiento interno utilizado por Azure App Service       | `8080`                                                                 |

## 7. Cómo ejecutar

### 1. Iniciar MongoDB localmente con Docker

```powershell
docker run -d -p 27017:27017 --name mongo-parcial mongo:7
```

### 2. Ejecutar la aplicación en local con Maven

```powershell
mvn spring-boot:run
```

El servicio quedará disponible en: `http://localhost:8080`

### 3. Construir y ejecutar con Docker

```powershell
docker build -t dosw-parcial:local .
docker run -p 8080:8080 -e JWT_SECRET=bXlfc3VwZXJfc2VjcmV0X2tleV9mb3Jfand0X3ZhbGlkYXRpb25fZG9zd18yMDI0Cg== dosw-parcial:local
```

## 8. Pruebas

Comando de ejecución:

```powershell
mvn test
```

### Resultado de pruebas unitarias en verde

![Resultado mvn test](docs/images/MVNTestEvidence.png)

### Reporte de cobertura de código JaCoCo

![Reporte JaCoCo](docs/images/JacocoEvidence.png)

## 9. Docker

Comandos build y run:

```powershell
docker build -t dosw-parcial:local .
docker run -p 8080:8080 -e JWT_SECRET=bXlfc3VwZXJfc2VjcmV0X2tleV9mb3Jfand0X3ZhbGlkYXRpb25fZG9zd18yMDI0Cg== dosw-parcial:local
```

### Evidencia de contenedor corriendo en Docker Desktop

![Contenedor corriendo en Docker](docs/images/DockerRunningEvidence.png)

## 10. CI/CD

El pipeline realiza build, testing, creación de imagen Docker, publicación en Docker Hub y despliegue a Azure Web App.

### Evidencia de pipeline en verde en GitHub Actions
![Pipeline CI/CD en verde](docs/images/WorksFlowsEvidence.png)

Secrets usados: `DOCKERHUB_USERNAME`, `DOCKERHUB_TOKEN`, `AZURE_CREDENTIALS`, `AZURE_WEBAPP_NAME`, `JWT_SECRET`

## 11. Herramientas de modelado y diseño (evidencias)

### Cuenta en herramienta de modelado (Draw.io)

![Evidencia Draw.io](docs/images/DrawEvidence.png)

### Cuenta en herramienta de diseño de interfaces (Figma)

![Evidencia Figma](docs/images/FigmaEvidence.png)


### Asignación del equipo de Arquitectura - Distribución de las responsabilidades: 

#### Diagrama de componentes general

![ComponentsGeneral.png](docs%2Fimages%2FComponentsGeneral.png)


#### Diagrama de componentes específico siguiendo el esquema de MVC. 


#### Diagrama de clases


### Preguntas para la revisión con Arquitectura: 

#### ¿Qué problemas aparecen cuando no se separan correctamente las capas? Presenten un ejemplo del caso y expliquen cómo distribuyen sus responsabilidades.
Pueden llegar a surgir multiples errores, desde codigo repetido (metodos, funcionalidad, responsabilidades), mal flujo de solicitudes, conversion de datos, problemas con los modelos, el flujo de os test tambien se podria ver afectado ya que al implementar un test digamos para un DTO, se debe haber un flujo con las clases de los mappers y al no tener una buena organizacion, el test se vería afectado

#### ¿Qué diferencia existe entre un validador, una utilidad y un servicio? Ubiquen un ejemplo de cada uno en su diseño e indiquen su responsabilidad.

### Validador
Es una clase que verifica el proposito de la aplicacion (funcionalidad) y esos se ven representados a partir de (Request DTOs, entidades, etc.). En donde se implementan las anotaciones (@NotNull, @Email, @Size, etc.), se realiza a partir de la inserccion de datos, en donde se cumpla los inputs, con ciertos requerimientos com el correo, o la contraseña con ciertos caracteres

### Utilidad
Son clases, adicionales que complementan las clases de modelo, siendo estas genericas pero utiles como una interfaz, No manejan persistencia, ni lógica de negocio compleja. implementadfas como delegacion de responsabilidad Son estáticas, no necesitan inyección de dependencias. 
En nuestro caso, seria un ejemplo seria para poder validar que el correo contenga el dominio de "@gmail"

### Servicio
Son clases que contiene reglas complementando la logica de negocio, representado con @service
En nuestro caso, un ejemplo seria serviceTurno, con el fin de validar que e


#### ¿Qué diferencia existe entre el Mapper IN y el Mapper OUT? Indiquen dónde se ubican y qué representaciones transforman cada uno.
convierte lo que entra al Service, ya sea un RequestDTO del Controller o un Modelo que llega del Repository.

convierte lo que sale del Service hacia un ResponseDTO para el Controller, o hacia un Modelo para persistir en BD.

El Mapper In interactua entre el controller y el service
El Mapper out interactua entre el service y el repository

#### Retomen las tres reglas clasificadas en el paso anterior: ¿en qué componente debe ejecutarse cada una y por qué? 

