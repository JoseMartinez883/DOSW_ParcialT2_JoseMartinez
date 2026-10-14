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

---

# Fase de diseño — Parte Teórica De los requerimientos a una solución implementable

## Asignación del equipo de Producto - Definición del contrato de la API:

Excel Tabla:
[Definición del contrato de la API.xlsx](docs%2Fexcel%2FDefinici%F3n%20del%20contrato%20de%20la%20API.xlsx)

### ¿Cuál es la diferencia entre una validación de entrada y una validación de negocio? Seleccionen tres reglas del caso, incluyendo al menos una de cada tipo, y justifiquen su clasificación.

Son formas de verificacion divididas en FrontEnd (UI) y Backend, siendo estos implementaciones a partir de la inserccion o solicitudes tomandas desde el controlador y convertidas por los mappers, luego en el ambito de la logica de negocio comprobar que la solicitud es valida o exista, por ejemplo:
cuando el usuario quiere registrarse con un correo con un correo institucional, se envia la solicitud (inserccion de datos) a partir de los DTOS, para que luego la logica del negocio del servicio verifique que tipo de solicitud es, y si es valida cumplienod con los caracteres especiales de la contraseña que use( verificacion en UI/input/DTO) al agregar los datos se verifica el correo que tenga "@", que la contraseña cumpla con ciertos caracteres tec.., luego de ello se verifica que los datos se cumplan segun su funcionalidad y proposito de la aplicacion atravez de los servicios (que sea un correo educativo)

### Reglas del caso

1. Cuando creamos un turno la regla de negocio es que el barbero debe existir y estar disponible, es una regla a nivel de negocio, ya que debemos verificar la dispnobilidad del barbero antes de crear el turno
2. Por otro lado, una regla de validacion, seria que el usuario cuando creara el turno, las fechas deben ser mayores o iguales a las actuales, ya que no tendria sentido en ningun caso, crear un turno en una fecha y hora pasada
3. Por otro lado, cuando iniciamos sesion, una regla de validacion seria que la contraseña y el correo no deben ser nulos, ya que no se podria iniciar sesion

---
### En Pedir un turno, ¿cómo debe responder la API si fallan varias condiciones? Expliquen qué error se informa y qué cambios deben evitarse. 

En Pedir un turno, si fallan varias condiciones, la API evalúa la Cadena de Validaciones en el orden estricto en el que fue definida y se detiene inmediatamente en la primera falla encontrada.   
Para el error informado: Se retorna únicamente el motivo específico de ese primer rechazo, acompañado del código de estado HTTP  definido en la matriz de errores por ejemplo, un 409 Conflict si la franja está llena. No se continúan evaluando las demás condiciones ni se acumulan los errores.
Los cambios a evitar serian que el proceso se debe abortar inmediatamente garantizando que no se cree ningún registro de reserva en la base de datos y no se modifique el estado del turno. Manteniendo la consistencia de la información.   


---
## Punto 4
Asignación del Equipo de Seguridad - Proteger el acceso y la información:

Una vez finalizada la asignación, su líder técnico le pide que se reúna con el área de seguridad, el equipo le solicita revisar cómo se controlará el acceso a los endpoints y cómo se protegerá la información durante las operaciones. Para ello le invitan a responder las siguientes preguntas:

- ¿Cuál es la diferencia entre autenticación, autorización e integridad? Expliquen dónde y cómo aplicarán cada concepto en la API, con ejemplos del caso.

Seleccionen dos riesgos del OWASP Top 10 que se pueden encontrar en su API. Para cada uno, describan cómo se vería ese escenario aplicable a la barbería, su mitigación y cómo verificarían que el control funciona.

- ¿Cuál de los tipos de autenticación es el más adecuado para este MVP? Justifique su respuesta  

---
### Desarrollo

#### a. Diferencia entre autenticación, autorización e integridad

* **Autenticación:**
  Valida la identidad de quien hace la petición. En la API la manejamos en el endpoint `POST /api/v1/auth/login`. El usuario manda su correo y contraseña, revisamos el hash con BCrypt contra la colección en MongoDB y devolvemos un JWT si los datos cuadran. En los otros endpoints, el filtro de Spring Security lee el header `Authorization: Bearer <token>`, valida la firma y deja pasar la petición con el usuario ya identificado.

* **Autorización:**
  Revisa qué cosas tiene permiso de hacer cada quien una vez ya inició sesión. Lo dividimos según el tipo de usuario:
  - **Cliente:** solo puede consultar sus datos, listar los turnos que están disponibles y pedir o cancelar sus propias reservas en `POST /api/v1/reservas`.
  - **Barbero:** puede consultar su agenda de citas asignadas y marcar la disponibilidad de sus turnos.
  - **Administrador:** tiene acceso a gestionar el sistema completo, como dar de alta barberos en `POST /api/v1/barberos` o modificar la lista de servicios.
  Si un cliente intenta consumir un endpoint de administración, el filtro de seguridad lo corta de inmediato y retorna un `403 Forbidden`.

* **Integridad:**
  Garantiza que la información viaje sin alteraciones y no quede rota o inconsistente en la base de datos. Para la comunicación aseguramos HTTPS y la firma criptográfica del token; si alguien altera los datos dentro del JWT, la firma no coincide y la API rechaza el request con un `401 Unauthorized`. Para los datos del negocio, aplicamos consistencia transaccional en MongoDB: si el proceso de pedir turno falla en mitad del flujo, por ejemplo porque el barbero no tenía el servicio pedido, la operación se cancela completa para no dejar reservas guardadas a medias ni cambiar el turno a ocupado.

---

#### b. Dos riesgos de OWASP Top 10 aplicables a la API

##### 1. Control de acceso roto o BOLA

* **Escenario en la barbería:**  
  Un cliente cancela su cita llamando a `DELETE /api/v1/reservas/{idReserva}`. Si en el backend solo tomamos el ID que viene en la ruta sin verificar quién es el dueño de la cita, un usuario malicioso podría cambiar ese ID en la URL y cancelar las reservas de otros clientes o ver sus datos personales.

* **Mitigación:**  
  En el servicio de reservas extraemos el ID del usuario que viene autenticado en el token y lo comparamos contra el dueño real de la reserva antes de borrar nada. Si el ID no coincide y el usuario no tiene permisos de administrador, rechazamos la petición con un `403 Forbidden`.

* **Verificación:**  
  Hacemos una prueba de integración creando una reserva con el usuario A y luego intentamos cancelarla mandando el token del usuario B con el ID de la cita de A. El test debe confirmar que el backend responde con un `403 Forbidden` y que la reserva sigue intacta en MongoDB.

##### 2. Fallas de autenticación

* **Escenario en la barbería:**  
  El endpoint `POST /api/v1/auth/login` no tiene límite de intentos o genera tokens que nunca vencen. Un atacante podría lanzar un script automatizado para probar listas de contraseñas contra las cuentas de los administradores o barberos sin que la API corte los intentos.

* **Mitigación:**  
  Configuramos tokens JWT con tiempo de expiración corto, como 15 a 30 minutos, guardamos contraseñas con hash seguro usando BCrypt y metemos un filtro de rate limiting para cortar peticiones repetidas si una misma IP o cuenta falla varias veces seguidas.

* **Verificación:**  
  Lanzamos una prueba automatizada que mande 10 peticiones seguidas con contraseñas incorrectas al login, validando que el servidor devuelva un `429 Too Many Requests`. También probamos mandar un token ya expirado y verificamos que responda con `401 Unauthorized`.

---

#### c. Tipo de autenticación más adecuado para este MVP

La opción más adecuada es autenticación stateless con JWT.

* **Justificación técnica:**  
  Como tenemos una API REST desacoplada que se va a consumir desde frontend web o clientes HTTP, manejar sesiones en memoria del servidor nos complicaría el backend y nos obligaría a montar herramientas extra como Redis. Con JWT el servidor solo valida la firma con la clave `JWT_SECRET` que ya tenemos en las variables de entorno. Al llegar una petición, Spring Security saca el usuario y sus permisos directamente del token sin pegarle a la base de datos en cada endpoint intermedio, lo que hace el backend mucho más liviano, rápido y directo de mantener para este MVP.