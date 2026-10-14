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

![Diagrama de componentes general](docs/uml/DiagramaComponentesGeneral.png)

* **Descripción breve:**
  Representa la arquitectura de alto nivel del sistema para Bob's Barber, evidenciando un modelo cliente-servidor desacoplado compuesto por tres componentes principales: el Front-End (aplicación cliente/SPA encargada de la interfaz gráfica y experiencia de usuario), el Back-End (API REST desarrollada con Spring Boot que encapsula la lógica de negocio, reglas de reserva y servicios) y el subsistema de persistencia de datos (Base de datos NoSQL MongoDB).
* **Justificación técnica:**
  * **Desacoplamiento estructural y separación de responsabilidades (SoC):** El Front-End no interactúa ni tiene acceso directo a la base de datos; delega todas las operaciones y consultas a la API Back-End a través de interfaces REST provistas/requeridas (notación Lollipop & Socket).
  * **Protocolo y contrato sin estado (Stateless):** La comunicación se realiza mediante HTTP/HTTPS con intercambio de mensajes en formato JSON y autenticación basada en tokens JWT, garantizando que el Back-End pueda escalar horizontalmente de forma independiente a la interfaz de usuario.
  * **Seguridad y persistencia centralizada:** El Back-End es el único componente autorizado para establecer conexión con el clúster de MongoDB, aislando las credenciales, conexiones y reglas de consistencia de datos de cara al exterior.

#### Diagrama de componentes específico siguiendo el esquema de MVC.

![Diagrama de componentes específico siguiendo el esquema de MVC](docs/uml/DiagramaComponentesEspecificos.png)

##### Vistas de detalle para mayor visibilidad y legibilidad:

* **Detalle Capa Controller, Capa Mapper IN y Capa Service:**
  ![Detalle Controladores y Servicios](docs/uml/DiagramComponentsPart01.png)

* **Detalle Capa Mapper OUT, Capa Repository, Capa Validator y Configuración:**
  ![Detalle Mappers, Repositorios, Validadores y Configuración](docs/uml/DiagramComponentsPart02.png)

* **Descripción breve:**
  Descompone internamente la arquitectura del Back-End en capas lógicas estrictas basadas en el patrón Modelo-Vista-Controlador (MVC) extendido para APIs REST. Se organizan los componentes en: Capa Controller (adaptadores REST y manejador global de excepciones), Capa Mapper IN (transformación de DTOs de entrada al dominio), Capa Service (orquestación de casos de uso del negocio), Capa Validator (evaluación de reglas de negocio con corte inmediato), Capa Mapper OUT (transformación de dominio a entidades de persistencia y DTOs de salida), Capa Repository (acceso y consultas sobre MongoDB con Spring Data) y el módulo transversal Config & Util (seguridad JWT/RBAC, documentación OpenAPI/Swagger, utilidades de fecha y data seeding).
* **Justificación técnica:**
  * **Controladores ligeros y desacoplados:** Los controladores (`AuthController`, `TurnoController`, `ReservaController`, etc.) actúan únicamente como puntos de entrada HTTP, delegando el procesamiento sin contener reglas de negocio ni acceso a datos, garantizando alta cohesión y bajo acoplamiento.
  * **Aislamiento perimetral mediante Mappers (In/Out):** Se evita el antipatrón de exponer entidades de persistencia directamente al cliente. `Mapper IN` convierte las peticiones externas (`Request DTO`) en objetos de negocio limpios; `Mapper OUT` transforma entidades de dominio a entidades de base de datos (`*Entity`) o respuestas hacia el cliente (`Response DTO`), permitiendo cambiar la estructura de la base de datos o de los endpoints sin afectar la lógica interna.
  * **Capa Service como orquestador del caso de uso:** Concentra la lógica del negocio (ej. coordinación del flujo de reserva, sumatoria de precios de servicios y transiciones de estado) e inyecta sus dependencias (validadores, repositorios y utilidades) por constructor mediante inversión de control (IoC).
  * **Capa Validator con estrategia Fail-Fast:** Aísla las reglas de negocio complejas (aforo de franjas $\le 3$, compatibilidad de especialidades y datos del cliente) del flujo del servicio. Al estructurarse de forma modular y secuencial, si una validación falla, el flujo se detiene inmediatamente y arroja la excepción correspondiente, evitando escrituras parciales o inconsistentes en la base de datos.
  * **Manejo uniforme de errores transversales:** Se incluye `GlobalExceptionHandler` (`@RestControllerAdvice`) para interceptar excepciones de negocio y de validación en un único punto, garantizando respuestas estandarizadas con códigos HTTP semánticos (400, 401, 403, 404, 409, 422).

#### Diagrama de clases

![Diagrama de clases - Modelos de Dominio](docs/uml/DiagramaDeClases.png)

##### Vistas de detalle para mayor visibilidad y legibilidad:

* **Detalle Entidades de Dominio, Objetos de Valor y Enums:**
  ![Detalle Entidades de Dominio](docs/uml/DiagramClassPart01.png)

* **Detalle Patrones de Diseño (Chain of Responsibility, Strategy y Builder):**
  ![Detalle Patrones de Diseño](docs/uml/DiagramClassPart02.png)

* **Descripción breve:**
  Modela la estructura del dominio del problema para Bob's Barber aplicando Programación Orientada a Objetos (POO) y Principios SOLID. Identifica las entidades principales del negocio (`Usuario`, `Reserva`, `Turno`, `Barbero`, `Servicio`), sus objetos de valor y enumeraciones (`RolUsuario`, `EstadoReserva`, `EstadoTurno`, `HorarioDisponibilidad`, `Especialidad`), e incorpora tres patrones de diseño de software GoF: *Chain of Responsibility* para la cadena secuencial de validaciones, *Strategy* para la asignación dinámica de barberos según especialidad y disponibilidad, y *Builder* para la construcción robusta e inmutable de reservas complejas.
* **Justificación técnica:**
  * **Separación entre Entidades con ciclo de vida y Objetos de Valor:** Entidades como `Turno` y `Reserva` gestionan su propia identidad y transiciones de estado mediante métodos encapsulados (`ocupar()`, `liberar()`, `confirmar()`, `cancelar()`, `calcularTotal()`). `HorarioDisponibilidad` actúa como un Value Object inmutable que encapsula la lógica temporal (`incluye(dia, hora)`), desacoplando los días y rangos horarios del barbero.
  * **Patrón Chain of Responsibility (Cadena de Responsabilidad):** Utilizado para implementar la cadena de 5 validaciones requerida por el caso de estudio (Disponibilidad de franja, Catálogo de servicios, Especialidad del barbero, Datos del cliente). A través de la clase base abstracta `AbstractValidadorReservaHandler` y sus handlers concretos, cada validador ejecuta su verificación y, en caso exitoso, delega al siguiente handler (`checkNext()`). Si una condición no se cumple, interrumpe el flujo (*fail-fast*) y lanza el error correspondiente sin acoplar los validadores entre sí ni sobrecargar el servicio de reservas.
  * **Patrón Strategy (Estrategia):** Permite resolver el requerimiento de negocio donde un cliente solicita un turno sin preferencia de barbero y el sistema debe asignar automáticamente uno disponible con la especialidad requerida. La interfaz `AsignacionBarberoStrategy` abstrae la regla de selección, y la clase `AsignacionPorEspecialidadStrategy` implementa la búsqueda y filtrado de barberos libres, facilitando la incorporación de nuevas estrategias en el futuro (ej. balanceo de carga o calificación) sin alterar el dominio.
  * **Patrón Builder (Constructor):** Dado que la creación de una `Reserva` involucra múltiples asociaciones obligatorias (identificador de turno, cliente, barbero asignado y lista de servicios), `ReservaBuilder` garantiza la creación segura y consistente del objeto, validando invariantes antes de su construcción y evitando constructores telescópicos.

### Preguntas para la revisión con Arquitectura:

#### ¿Qué problemas aparecen cuando no se separan correctamente las capas? Presenten un ejemplo del caso y expliquen cómo distribuyen sus responsabilidades.

Pueden llegar a surgir múltiples problemas de diseño y mantenimiento: código repetido (métodos y responsabilidades duplicadas en varios puntos), un mal flujo en el manejo de las solicitudes, confusiones en la conversión de datos y problemas con los modelos al acoplar la base de datos con la capa web. Además, el flujo de las pruebas unitarias se vería gravemente afectado, ya que al querer probar un DTO o una regla de negocio aislada, si no existe una separación limpia mediante mappers y servicios, el test obligaría a depender de la base de datos o de peticiones completas.

Ejemplo del caso Bob's Barber (Pedir un turno): Si el `TurnoController` recibiera la solicitud y en lugar de delegar se pusiera a consultar directamente a MongoDB para contar si ya hay 3 reservas en la franja, calculara la suma de los servicios y guardara la reserva en la base de datos:

* El controlador tendría demasiadas responsabilidades y violaría el principio de responsabilidad única.
* Si el día de mañana se cambia MongoDB o la regla de aforo cambia, tocaría modificar directamente el controlador web en lugar de un servicio.

**Distribución correcta de responsabilidades:**

1. **Controller (`TurnoController`):** Solo recibe la petición HTTP, valida el formato del JSON entrante (Bean Validation) y llama al servicio.
2. **Service (`TurnoServiceImpl`):** Orquesta el flujo del caso de uso, invoca a los validadores de negocio, calcula el precio total y coordina la persistencia.
3. **Validator (`ReservaValidatorImpl`):** Verifica las reglas del negocio de la barbería (aforo $\le 3$, especialidad del barbero `E02`).
4. **Repository (`TurnoRepository`, `ReservaRepository`):** Se encarga exclusivamente de las consultas y operaciones con MongoDB.

#### ¿Qué diferencia existe entre un validador, una utilidad y un servicio? Ubiquen un ejemplo de cada uno en su diseño e indiquen su responsabilidad.

* **Servicio (`TurnoServiceImpl`):**

  * *Responsabilidad:* Se encarga de todo el flujo de negocio, coordina las validaciones, calcula operaciones (como la suma total de los precios de los servicios) y le pide a los repositorios que guarden la información. Es un componente administrado por Spring (`@Service`) e inyecta dependencias por constructor.
* **Validador (`ReservaValidatorImpl`):**

  * *Responsabilidad:* Es una clase especializada cuya única función es evaluar si se cumplen las reglas del negocio antes de ejecutar una acción, aplicando el principio de falla rapida, es decir, el primero que falle, como una cadena de responsabilidad, al fallar uno, se para la cadena.
  * *Ejemplo en el diseño:* `ReservaValidatorImpl` (o la cadena de validadores), que revisa que el barbero libre en la franja realmente tenga la especialidad requerida para el corte (`E02`) y que la franja no supere los 3 turnos ocupados (`E01`).
* **Utilidad (`DateUtil` o `JwtUtil`):**

  * *Responsabilidad:* Son métodos auxiliares, genéricos y reutilizables que realizan cálculos o transformaciones puras sin guardar estado ni depender de la base de datos. Suelen tener métodos estáticos o sin dependencias de dominio complejas.
  * *Ejemplo en el diseño:* `DateUtil`, que toma una hora de inicio y calcula la franja exacta de una hora, o verifica si una fecha corresponde a un día laboral del barbero (ej. lunes a jueves para Miguel).

#### ¿Qué diferencia existe entre el Mapper IN y el Mapper OUT? Indiquen dónde se ubican y qué representaciones transforman cada uno.

La diferencia radica en **en qué frontera de la arquitectura se ubican y qué modelos traducen**:

* **Mapper IN (`TurnoDTOMapper` / `AuthDTOMapper`):**

  * *Ubicación:* En la **Capa Controller** (`edu.dosw.parcial.controller.mappers`).
  * *Transformación:* Traduce entre los objetos de transferencia de la web y el dominio:
    * **Entrada:** `Request DTO` (JSON que envía el cliente) ➔ `Modelo de Dominio`.
    * **Salida:** `Modelo de Dominio` ➔ `Response DTO` (JSON que recibe el cliente).
  * *Propósito:* Aísla a la capa de Servicio para que no tenga idea de cómo viajan los datos en HTTP.
* **Mapper OUT (`TurnoEntityMapper` / `ReservaEntityMapper`):**

  * *Ubicación:* En la **Capa de Persistencia** (`edu.dosw.parcial.persistence.mappers`).
  * *Transformación:* Traduce entre el dominio y los documentos de MongoDB:
    * `Modelo de Dominio` ➔ `Documento MongoDB (@Document)` (para guardar en BD).
    * `Documento MongoDB` ➔ `Modelo de Dominio` (al consultar de la BD para entregárselo al servicio).
  * *Propósito:* Aísla a la capa de Servicio para que no dependa de las anotaciones ni estructuras de la base de datos NoSQL.

#### Retomen las tres reglas clasificadas en el paso anterior: ¿en qué componente debe ejecutarse cada una y por qué?

Retomando las tres reglas seleccionadas en la revisión de Producto:

1. **Formato y dominio del correo (`E08 - Correo inválido`):**

   * *Componente:* Se ejecuta en el **Request DTO / Controller** (`CreateReservaRequest` o `LoginRequest`).
   * *Por qué:* Es una validación de entrada puramente sintáctica. Se resuelve inmediatamente con la anotación `@Pattern` de Bean Validation apenas llega el JSON, evitando gastar recursos del servidor o llegar a la capa de servicios si el correo no termina en `@gmail.com`.
2. **Longitud numérica del teléfono (`E07 - Teléfono inválido`):**

   * *Componente:* Se ejecuta en el **Request DTO / Controller** (`CreateReservaRequest`).
   * *Por qué:* Al igual que el correo, es una validación de formato estructural. El controlador la rechaza de inmediato con un `400 Bad Request` usando `@Pattern(regexp = "^[0-9]{10}$")` antes de que la petición toque el dominio.
3. **Compatibilidad de especialidad del barbero (`E02 - Especialidad incompatible`):**

   * *Componente:* Se ejecuta en la **Capa Core / Validator** (`ReservaValidatorImpl` o `BarberoEspecialidadValidator`).
   * *Por qué:* Es una regla de negocio semántica. No se puede saber si el barbero es apto solo mirando el JSON de entrada, el componente necesita consultar al barbero en la base de datos (MongoDB), extraer su lista de especialidades y verificar si incluye la especialidad requerida por el servicio (por ejemplo, que Laura tenga `CAPILAR` o que Miguel tenga `CORTE CON BARBA`).
