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

### 3. Construir y ejecutar con Docker individual

```powershell
docker build -t dosw-parcial:local .
docker run -p 8080:8080 -e JWT_SECRET=bXlfc3VwZXJfc2VjcmV0X2tleV9mb3Jfand0X3ZhbGlkYXRpb25fZG9zd18yMDI0Cg== dosw-parcial:local
```

### 4. Orquestación completa con Docker Compose (Bono)

Levanta la API junto con MongoDB y volumen persistente en un solo comando:

```powershell
docker compose up --build -d
```

Detener los servicios:
```powershell
docker compose down
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
[Definición del contrato de la API.xlsx](<docs/excel/Definici%C3%B3n%20del%20contrato%20de%20la%20API.xlsx>)

preview:
![img.png](docs/images/previewExcel.png)

### ¿Cuál es la diferencia entre una validación de entrada y una validación de negocio? Seleccionen tres reglas del caso, incluyendo al menos una de cada tipo, y justifiquen su clasificación.

La diferencia principal está en qué revisa cada una y en qué capa se ejecuta:

* Validación de entrada:Revisa únicamente que los datos vengan completos, con el tipo correcto y el formato esperado. Se ejecuta apenas entra la petición al controlador usando los DTOs con anotaciones de Bean Validation como `@NotNull` o `@Pattern`. Su gran ventaja es la velocidad: no necesita tocar la base de datos ni consultar lógica interna, así que si los datos vienen mal estructurados, la API corta la petición de una vez devolviendo un `400 Bad Request`.
* **Validación de negocio:**
  Revisa que la solicitud tenga sentido dentro de las reglas de la barbería. Esta validación se ejecuta en los servicios de la capa core y sí necesita consultar MongoDB. Por ejemplo, comprueba si el barbero realmente está disponible en ese horario, si domina el servicio solicitado o si la franja ya alcanzó su cupo máximo. Al fallar, responde con códigos de error semánticos según el motivo del rechazo, como `404 Not Found`, `409 Conflict` o `422 Unprocessable Entity`.

#### Tres reglas del caso Bob's Barber justificadas

1. **Validación del correo del cliente, error E08:**

   * *Regla:* El correo no puede ser nulo, debe tener formato de email y terminar obligatoriamente en `@gmail.com`.
   * *Justificación:* Es de **entrada**, porque se valida directo en el DTO con una expresión regular apenas llega el JSON, sin tener que consultar MongoDB para saber si el texto termina en `@gmail.com`.
2. **Validación del teléfono del cliente, error E07:**

   * *Regla:* El teléfono no puede ser nulo y debe tener exactamente 10 dígitos numéricos.
   * *Justificación:* Es de **entrada**, porque solo revisa la longitud y el tipo numérico del campo que mandó el usuario antes de pasar a la lógica del servicio.
3. **Compatibilidad de especialidad del barbero, error E02:**

   * *Regla:* El barbero asignado debe saber realizar el servicio solicitado, por ejemplo tener la especialidad de corte con barba si el cliente pide ese servicio.
   * *Justificación:* Es de **negocio**, porque con solo ver el JSON de la petición no sabemos qué servicios domina el barbero; el servicio debe consultar la base de datos, traer al barbero, revisar sus especialidades y compararlas contra el servicio pedido.

---

### En Pedir un turno, ¿cómo debe responder la API si fallan varias condiciones? Expliquen qué error se informa y qué cambios deben evitarse.

* Cómo responde la API:La API sigue la cadena de validaciones en orden secuencial estricto: primero franja disponible, luego barbero disponible con especialidad, después datos del cliente y finalmente servicio permitido. Si se presentan varias fallas en una misma petición, el flujo se detiene en la primera que no se cumpla y no sigue evaluando las demás.
* Qué error se informa: Se informa únicamente el motivo de la primera falla encontrada junto a su código HTTP. Por ejemplo, si la franja ya estaba llena con error E01, se responde inmediatamente un `409 Conflict` indicando que no hay cupo. La API no evalúa las siguientes reglas ni acumula una lista de errores.
* **Qué cambios deben evitarse:**Al fallar cualquier validación, la operación se cancela en el acto y la base de datos debe quedar exactamente igual que antes de la petición:

  * **No se crea la reserva:** no se guarda ningún registro en la colección de reservas.
  * **No se altera el turno:** el turno se mantiene en estado `DISPONIBLE` y no pasa a `OCUPADO`. Si el turno ya estaba ocupado, tampoco se modifica su estado previo.

---

## Asignación del equipo de Arquitectura - Distribución de las responsabilidades:

### Diagrama de componentes general

![Diagrama de componentes general](docs/uml/DiagramaComponentesGeneral.png)

* **Descripción breve:**
  Representa la arquitectura de alto nivel del sistema para Bob's Barber, evidenciando un modelo cliente-servidor desacoplado compuesto por tres componentes principales: el Front-End (aplicación cliente/SPA encargada de la interfaz gráfica y experiencia de usuario), el Back-End (API REST desarrollada con Spring Boot que encapsula la lógica de negocio, reglas de reserva y servicios) y el subsistema de persistencia de datos (Base de datos NoSQL MongoDB).
* **Justificación técnica:**
  * **Desacoplamiento estructural y separación de responsabilidades (SoC):** El Front-End no interactúa ni tiene acceso directo a la base de datos; delega todas las operaciones y consultas a la API Back-End a través de interfaces REST provistas/requeridas (notación Lollipop & Socket).
  * **Protocolo y contrato sin estado (Stateless):** La comunicación se realiza mediante HTTP/HTTPS con intercambio de mensajes en formato JSON y autenticación basada en tokens JWT, garantizando que el Back-End pueda escalar horizontalmente de forma independiente a la interfaz de usuario.
  * **Seguridad y persistencia centralizada:** El Back-End es el único componente autorizado para establecer conexión con el clúster de MongoDB, aislando las credenciales, conexiones y reglas de consistencia de datos de cara al exterior.

### Diagrama de componentes específico siguiendo el esquema de MVC.

![Diagrama de componentes específico siguiendo el esquema de MVC](docs/uml/DiagramaComponentesEspecificos.png)

#### Vistas de detalle para mayor visibilidad y legibilidad:

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

### Diagrama de clases

![Diagrama de clases - Modelos de Dominio](docs/uml/DiagramaDeClases.png)

#### Vistas de detalle para mayor visibilidad y legibilidad:

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

---

## Asignación del Equipo de Base de datos - Diseño de la persistencia:

### Diagrama de Bases de datos no relacionales - Metamodelo conceptual

![Diagrama de Bases de datos no relacionales - Metamodelo conceptual](docs/uml/DiagramPersistenceNoSQL.png)

### Análisis del Modelo de Persistencia: Embebido vs. Referenciado vs. Híbrido

Para dar soporte eficiente y consistente a los casos de uso definidos en el contrato de la API de **Bob's Barber**, se analizó el modelo de clases de dominio frente a los patrones de diseño en bases de datos NoSQL documentales (MongoDB). Se concluyó que la arquitectura requiere un **Modelo Híbrido** (combinación de referencias por `ObjectId` y documentos embebidos mediante *Snapshot*), fundamentado en los siguientes criterios técnicos:

#### 1. Justificación por Entidad y Relación del Dominio

* **`Barbero` y `HorarioDisponibilidad` (Embebido - Rombo Negro ◆):**

  * *Naturaleza:* `HorarioDisponibilidad` es un *Value Object* inmutable y de cardinalidad 1 a 1 acotada que no tiene identidad propia fuera del barbero.
  * *Decisión:* Se almacena **embebido** dentro del documento `barberos`.
  * *Justificación:* Un horario nunca se consulta de forma aislada; siempre se lee junto con el barbero para determinar si puede abrir turnos en esa franja. Embeberlo garantiza lecturas en una sola operación sin *joins* ni consultas cruzadas.
* **`Usuario` y `Servicio` (Colecciones Raíz Referenciadas):**

  * *Naturaleza:* Son catálogos y entidades maestras con ciclo de vida independiente.
  * *Decisión:* Se modelan como colecciones independientes (`usuarios` y `servicios`).
  * *Justificación:* Los usuarios necesitan autenticarse (`POST /api/v1/auth/login`) independientemente de si tienen citas agendadas. Los servicios representan el catálogo activo de la barbería consultado frecuentemente por múltiples clientes.
* **`Turno` y `Barbero` (Referenciado por ID):**

  * *Naturaleza:* Relación 1 a N no acotada (un barbero atiende cientos de turnos a lo largo de los meses).
  * *Decisión:* El documento `turnos` referencia al barbero mediante su `barberoId: ObjectId`.
  * *Justificación:* Si los turnos se embebieran dentro del barbero, el documento crecería indefinidamente.
* **`Reserva` frente a `Usuario`, `Turno` y `Servicios` (Modelo Híbrido Clave):**

  * *Referencias (`clienteId`, `turnoId`, `barberoId`):* La reserva referencia a sus entidades asociadas para permitir consultas bidireccionales eficientes (ej. el cliente consulta sus reservas con `GET /api/v1/reservas/mis-reservas` filtrando por `clienteId`, y el administrador lista todas con `GET /api/v1/reservas`).
  * *Servicios Embebidos (Rombo Negro ◆):* Dentro de la reserva se **embebieron** los servicios contratados con su precio en el momento exacto de la cita.
  * *Justificación:* Si solo guardáramos el `servicioId`, una futura actualización en el precio del servicio base alteraría retroactivamente el total de las reservas pasadas, destruyendo la integridad contable e histórica. Embeber una copia inmutable asegura que el total cobrado jamás cambie.

#### 2. Consideraciones de Consulta, Actualización, Duplicación y Crecimiento

| Criterio                            | Análisis en Bob's Barber                                                                                                          | Decisión en el Diseño                                                                                                                                                                       |
| :---------------------------------- | :--------------------------------------------------------------------------------------------------------------------------------- | :-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Consultas Frecuentes**      | La API exige verificar disponibilidad en tiempo real (aforo $\le 3$ por franja y barbero libre en horario).                      | `turnos` y `reservas` son colecciones independientes indexadas por `fecha` y `horaInicio`, permitiendo conteos atómicos rápidos con `countDocuments()` sin recorrer usuarios.     |
| **Actualizaciones**           | Al reservar, el turno cambia de `DISPONIBLE` a `OCUPADO` y se crea la reserva con total acumulado.                             | Se actualiza únicamente el estado del turno y se inserta la reserva. No hay riesgo de escrituras concurrentes bloqueantes sobre perfiles de usuario.                                         |
| **Duplicación de Datos**     | Se desnormaliza intencionalmente el código, nombre y precio del servicio dentro de la reserva (*Desnormalización controlada*). | La duplicación es mínima y necesaria para salvaguardar la inmutabilidad histórica del cobro frente a modificaciones del catálogo.                                                         |
| **Crecimiento de Documentos** | Clientes y barberos acumulan decenas de transacciones en el tiempo.                                                                | Al no embeber las reservas dentro de usuarios ni barberos, los documentos principales mantienen un tamaño constante y ligero ($< 1$ KB), previniendo fragmentación de memoria en MongoDB. |

#### 3. Ventaja y Desventaja de la Alternativa Elegida (Modelo Híbrido)

* **Ventaja Principal:**

  * **Alto Rendimiento y Consistencia Histórica:** Proporciona velocidad óptima de lectura al consultar reservas con sus ítems calculados en una única consulta sin necesidad de `$lookup`, garantizando a la vez que el documento raíz nunca crezca de forma descontrolada.
* **Desventaja Principal:**

  * **Manejo de Integridad Referencial a Nivel de Aplicación:** Dado que MongoDB no implementa llaves foráneas nativas ni restricciones *ON DELETE CASCADE*, la responsabilidad de asegurar que el `clienteId` y `barberoId` existan recae en la capa de validadores de negocio (`ReservaValidatorImpl`) antes de persistir.

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

* **Escenario en la barbería:** Un cliente cancela su cita llamando a `DELETE /api/v1/reservas/{idReserva}`. Si en el backend solo tomamos el ID que viene en la ruta sin verificar quién es el dueño de la cita, un usuario malicioso podría cambiar ese ID en la URL y cancelar las reservas de otros clientes o ver sus datos personales.
* **Mitigación:** En el servicio de reservas extraemos el ID del usuario que viene autenticado en el token y lo comparamos contra el dueño real de la reserva antes de borrar nada. Si el ID no coincide y el usuario no tiene permisos de administrador, rechazamos la petición con un `403 Forbidden`.
* **Verificación:**
  Hacemos una prueba de integración creando una reserva con el usuario A y luego intentamos cancelarla mandando el token del usuario B con el ID de la cita de A. El test debe confirmar que el backend responde con un `403 Forbidden` y que la reserva sigue intacta en MongoDB.

##### 2. Fallas de autenticación

* **Escenario en la barbería:** El endpoint `POST /api/v1/auth/login` no tiene límite de intentos o genera tokens que nunca vencen. Un atacante podría lanzar un script automatizado para probar listas de contraseñas contra las cuentas de los administradores o barberos sin que la API corte los intentos.
* **Mitigación:** Configuramos tokens JWT con tiempo de expiración corto, como 15 a 30 minutos, guardamos contraseñas con hash seguro usando BCrypt y metemos un filtro de rate limiting para cortar peticiones repetidas si una misma IP o cuenta falla varias veces seguidas.
* **Verificación:**
  Lanzamos una prueba automatizada que mande 10 peticiones seguidas con contraseñas incorrectas al login, validando que el servidor devuelva un `429 Too Many Requests`. También probamos mandar un token ya expirado y verificamos que responda con `401 Unauthorized`.

---

#### c. Tipo de autenticación más adecuado para este MVP

La opción más adecuada es autenticación stateless con JWT.

* **Justificación técnica:**
  Como tenemos una API REST desacoplada que se va a consumir desde frontend web o clientes HTTP, manejar sesiones en memoria del servidor nos complicaría el backend y nos obligaría a montar herramientas extra como Redis. Con JWT el servidor solo valida la firma con la clave `JWT_SECRET` que ya tenemos en las variables de entorno. Al llegar una petición, Spring Security saca el usuario y sus permisos directamente del token sin pegarle a la base de datos en cada endpoint intermedio, lo que hace el backend mucho más liviano, rápido y directo de mantener para este MVP.

---

## Punto 5

Asignación del Equipo de Calidad - Definir cómo comprobar el comportamiento:

El equipo de QA utilizará el contrato de la API como referencia para validar la solución. Su tarea es definir los escenarios de pruebas de Pedir un turno, cubriendo tanto las reservas exitosas como los rechazos siguiendo la aplicación del esquema de TDD (Red, Green, Refactor).

Explique cómo comprobaría que se respeta el orden y que, ante una falla, no se crea la reserva ni se modifica el turno. No es necesario escribir el código de las pruebas en esta respuesta.

---

### Desarrollo

#### 1. Ciclo TDD aplicado a la operación Pedir un turno

* **Fase Red:** Escribimos la prueba unitaria o de integración antes de implementar el servicio o validador. Diseñamos un test que espera que una petición válida devuelva un estado `201 Created` y persista la reserva. Al correr la prueba, falla de inmediato porque el endpoint o la lógica de negocio todavía no existen.
* **Fase Green:** Escribimos la lógica mínima necesaria en el controlador y servicio para que la prueba pase a verde, sin meter código extra ni optimizaciones antes de tiempo.
* **Fase Refactor:**
  Organizamos el código, desacoplamos validadores en clases dedicadas y mejoramos nombres, corriendo la suite completa para asegurarnos de que todo siga pasando en verde.

---

#### 2. Escenarios de prueba para Pedir un turno

| Escenario                                                     | Entrada y condiciones                                                                                                                            | Resultado esperado                                                       | Validación clave                                                                                      |
| ------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------ |
| **Reserva exitosa**                                     | Franja horaria disponible, barbero con especialidad requerida, cliente con correo `@gmail.com` y teléfono de 10 dígitos, servicio permitido. | `201 Created` con payload de la reserva confirmada y su identificador. | Se guarda la reserva en MongoDB y el turno pasa de `DISPONIBLE` a `OCUPADO`.                       |
| **Rechazo por franja llena - E01**                      | Franja horaria sin cupos o previamente ocupada.                                                                                                  | `409 Conflict` con código E01.                                        | El flujo se corta de inmediato sin evaluar al barbero ni al cliente, dejando la base de datos intacta. |
| **Rechazo por especialidad incompatible - E02**         | Franja disponible, pero el barbero asignado no cuenta con la especialidad del servicio pedido.                                                   | `422 Unprocessable Entity` con código E02.                            | Se cancela la solicitud sin crear reserva y el turno sigue en estado `DISPONIBLE`.                   |
| **Rechazo por barbero no disponible - E03**             | Franja disponible y barbero con especialidad, pero se encuentra inactivo o en descanso.                                                          | `409 Conflict` con código E03.                                        | Se aborta la operación sin guardar registros huérfanos.                                              |
| **Rechazo por datos de cliente inválidos - E07 y E08** | Correo sin dominio `@gmail.com` o teléfono que no tenga 10 dígitos.                                                                          | `400 Bad Request` disparado por las validaciones de entrada en el DTO. | La petición se frena en el controlador mediante `@Valid` antes de tocar la base de datos.           |

---

#### 3. Comprobación del orden de validación y de la atomicidad ante fallas

* **Comprobación del orden estricto:** Montamos pruebas donde el request contiene varias fallas al mismo tiempo, como mandar una franja llena y a la vez un barbero sin especialidad. En el assert verificamos que la API retorne únicamente el error del primer paso, que es franja llena E01. Con Mockito comprobamos que los métodos de las validaciones posteriores y el repositorio de reservas nunca llegaron a ejecutarse.
* **Comprobación de no creación y no modificación:** En las pruebas de rechazo, antes de enviar la petición consultamos el conteo de reservas y el estado del turno. Luego de recibir el error 400, 409 o 422, verificamos de nuevo en MongoDB:

  - El conteo de reservas se mantiene igual.
  - El turno conserva su estado `DISPONIBLE`.
  - Si la falla ocurre a mitad del flujo, `@Transactional` revierte cualquier cambio intermedio, garantizando que nada quede a medias.

---

### Preguntas para la revisión con el equipo de Calidad

#### a. Evidencia del cumplimiento de reglas de negocio y alcance de pruebas unitarias

* **Cómo las pruebas aportan evidencia:** Las pruebas automatizadas sirven como especificaciones vivas del sistema. Cada regla del contrato queda asegurada en un test ejecutable. Cuando el pipeline corre `mvn test` en cada push o PR, los reportes de ejecución demuestran con hechos que la API responde tal como espera el negocio ante datos válidos y casos de error.
* **Qué se verifica puntualmente con pruebas unitarias:** Se prueba la lógica pura en aislamiento total, usando mocks para no depender de la base de datos ni de servicios externos:

  - Que los validadores lancen la excepción de dominio correspondiente ante datos inválidos, como un barbero sin la especialidad requerida.
  - Que el modelo `Turno` solo permita transiciones válidas de `DISPONIBLE` a `OCUPADO`.
  - Que los mappers transformen los DTOs y entidades de dominio sin perder información.
  - Que el servicio corte la ejecución en el primer fallo y no llame al repositorio de reservas.

---

#### b. ¿Las pruebas por sí solas garantizan la ausencia de errores?

No. Las pruebas demuestran que hay defectos cuando fallan, pero no pueden garantizar que el software esté 100% libre de errores.

* **Justificación técnica:** Una suite de pruebas, incluso con cobertura alta, solo valida los casos que el equipo llegó a imaginar y programar en los tests. Quedan por fuera factores del entorno real como:

  - Condiciones de carrera cuando muchos usuarios compiten por el mismo turno en el mismo milisegundo.
  - Problemas de infraestructura, caídas de red o saturación del pool de conexiones en MongoDB.
  - Requerimientos mal entendidos desde el inicio: el código puede tener todos sus tests en verde porque cumple lo programado, pero lo programado no era lo que el negocio realmente necesitaba.

  Por eso los tests automatizados deben complementarse con análisis estático en SonarQube, pruebas de carga y monitoreo continuo con métricas y logs en producción.

---

## Punto 6

Asignación del Equipo de Devops - Definir cómo comprobar el comportamiento:

Para finalizar la etapa de diseño, su líder le asigna una reunión con el equipo de Devops para garantizar que cuando se implemente el MVP pueda desplegarse correctamente, para ello debes generar el Diagrama de despliegue de la aplicación teniendo en cuenta las anotaciones correspondientes: Sus nodos, los artefactos, las relaciones con su respectivo mecanismo de comunicación, los dispositivos involucrados y la especificación del despliegue.

---

### Desarrollo

#### 1. Diagrama de despliegue de la solución

* **Fuente editable en Draw.io:** [Google Drive](https://drive.google.com/file/d/1sYDKYtZpFOvCa8y8ybALDk5QuHJG-ikW/view?usp=sharing)
* **Vista del diagrama exportado:**

![Diagrama de Despliegue](docs/uml/DiagramaDespliegue.drawio.png)

**Descripción del diagrama:**  
El diagrama modela la topología física y lógica de la solución Bob's Barber en producción y pruebas. Inicia con el **Dispositivo Desarrollador**, donde se construyen y prueban localmente los componentes con Maven y Java 21, enviando los cambios mediante `git push` al **Repositorio GitHub**.

A través de un webhook, el repositorio dispara el pipeline en el **GitHub Actions Runner** (`ci-cd.yml`), el cual compila con `mvn verify`, empaqueta la imagen Docker inmutable y realiza el `docker push` hacia **Docker Hub Registry** (`josemartinez883/dosw-parcial-t2:latest`).

Desde allí, el pipeline orquesta el despliegue hacia dos ambientes aislados en **Microsoft Azure**:
- **Ambiente QA:** El servicio **Azure App Service QA** descarga la imagen de Docker Hub (`docker pull :latest`) y ejecuta el contenedor con el artefacto `dosw-parcial.jar` en el puerto interno 8080. Se comunica mediante MongoDB Wire Protocol sobre TLS (puerto 27017) con una base de datos dedicada **MongoDB Atlas QA** (`dosw_parcial_qa`), permitiendo realizar pruebas integrales sin comprometer datos reales.
- **Ambiente PROD:** Tras validación y aprobación, **Azure App Service PROD** despliega el contenedor con la versión productiva en West US 3, conectado a la base de datos **MongoDB Atlas PROD** (`dosw_parcial`) bajo cifrado TLS.

El **Dispositivo Cliente** (consumiendo desde Swagger UI, una SPA web o Postman) interactúa con las APIs expuestas en Azure mediante peticiones seguras sobre **HTTPS / REST** dirigidas al puerto 443, enrutadas internamente al puerto 8080 de cada contenedor.


#### 2. Especificación técnica del despliegue

| Elemento | Tipo | Detalle técnico | Rol en la arquitectura |
|---|---|---|---|
| **Desarrollador** | Entorno local | Máquina de desarrollo con JDK 21, Maven y Git | Realiza cambios en el código y lanza el comando git push hacia el repositorio remoto. |
| **Repositorio GitHub** | Servidor de control de versiones | Repositorio remoto con ramas `develop` y `main` | Centraliza el código fuente y dispara automáticamente el webhook del pipeline de CI/CD. |
| **GitHub Actions** | Servidor de automatización CI/CD | Runner Ubuntu Latest ejecutando `workflows/ci-cd.yml` | Corre la fase de build con `mvn verify`, empaqueta la imagen y orquesta el despliegue a QA y PROD. |
| **Docker Hub** | Registro de artefactos | Repositorio de imágenes `josemartinez883/dosw-parcial-t2` | Almacena y versiona la imagen de contenedor que consumen los ambientes de Azure. |
| **Azure App Service QA** | Nodo de ejecución en la nube | Instancia Web App Linux para pruebas de integración | Aloja el contenedor en ambiente de QA para validación continua antes de paso a producción. |
| **MongoDB Atlas QA** | Nodo de persistencia cloud | Base de datos `dosw_parcial_qa` con Replica Set | Almacena datos de prueba y aislamiento para el ambiente de QA. |
| **Azure App Service PROD** | Nodo de ejecución en la nube | Instancia Web App Linux productiva en West US 3 | Aloja la versión estable y pública de la API para usuarios y clientes finales. |
| **MongoDB Atlas PROD** | Nodo de persistencia cloud | Base de datos `dosw_parcial` con Replica Set y TLS | Almacena los registros reales de reservas, turnos, barberos y usuarios. |
| **Dispositivo Cliente** | Consumidor HTTP | Swagger UI, SPA Web o Postman consumiendo la API | Emite peticiones HTTPS a los endpoints públicos de la barbería. |
| **Mecanismos de comunicación** | Protocolos y puertos | HTTPS en puerto 443 hacia 8080 y MongoDB Wire Protocol en puerto 27017 | Conexiones cifradas en tránsito en ambos ambientes de la solución. |

---

### Preguntas para la revisión con el equipo de Devops

#### a. Etapas principales de un pipeline CI/CD y propósito de cada una

* **Etapa de integración y pruebas:** Descarga el código en el runner de GitHub Actions, configura el entorno con Java 21 y ejecuta `mvn verify`. Su objetivo es compilar el proyecto y correr toda la suite de pruebas automatizadas y cobertura. Si algo falla o no compila, corta el pipeline de una vez para que ningún error llegue a etapas posteriores.
* **Etapa de empaquetado y subida de imagen:** Toma el Dockerfile multietapa, construye la imagen del contenedor con el jar generado y se autentica contra Docker Hub para hacer el push de la imagen etiquetada con latest. Su propósito es generar un artefacto inmutable y portable que sea exactamente igual en pruebas y en producción.
* **Etapa de despliegue continuo:**
  Se conecta a Microsoft Azure usando las credenciales del service principal y le ordena a Azure Web App actualizar el contenedor descargando la última versión de la imagen desde Docker Hub. Su propósito es publicar la aplicación en vivo sin intervención manual y reduciendo al mínimo el tiempo de indisponibilidad.

---

#### b. Qué sucede si una prueba falla y si debe permitirse el despliegue

Si una prueba falla durante el paso de `mvn verify`, Maven arroja un código de salida de error y el job `test` se marca en rojo. Como los jobs de `docker` y `deploy` tienen la instrucción `needs: test`, el pipeline se cancela automáticamente en ese instante.

No se debe permitir el despliegue bajo ninguna circunstancia. El objetivo principal de tener un pipeline automatizado es servir de filtro de calidad. Si permitimos desplegar con una prueba rota, estaríamos mandando a producción un bug confirmado, una regresión que rompe el contrato de la API o una falla que puede corromper datos de turnos y reservas de los usuarios reales.

---

#### c. Para qué sirven los logs y qué registrar ante una reserva fallida o un error de autenticación

Los logs dan visibilidad en tiempo real de lo que ocurre dentro de la aplicación mientras está corriendo en Azure, permitiendo entender fallas y auditar eventos sin tener que pausar el servicio ni entrar a depurar código en vivo.

* **Al investigar una reserva fallida:** Registramos en nivel `WARN` o `ERROR`:

  - Identificador único de la petición o correlation ID para rastrear la llamada completa.
  - Fecha y hora exacta en formato UTC.
  - Método y ruta consumida, como `POST /api/v1/reservas`.
  - Datos de negocio involucrados, como el ID del cliente, ID del barbero, fecha del turno y código de servicio.
  - Motivo puntual del rechazo según la regla que falló, por ejemplo franja llena E01 o barbero sin especialidad E02.
  - Stack trace de la excepción en caso de que la falla haya sido por caída de conexión con MongoDB.
* **Al investigar un error de autenticación:** Registramos en nivel `WARN`:

  - Fecha, hora e IP de origen de la solicitud.
  - Correo electrónico que intentó iniciar sesión.
  - Causa técnica del rechazo, como credenciales incorrectas, usuario inexistente en base de datos o token JWT expirado.

---

#### d. Ejemplos de información útil para rastrear la operación y datos que nunca deben registrarse

* **Información útil que sí se registra:**

  - Trace ID y correlation ID para asociar todas las líneas de log de una misma petición.
  - Tiempos de respuesta y duración de consultas pesadas a la base de datos.
  - Códigos de respuesta HTTP y estados de dominio, como `CONFIRMADA` o `CANCELADA`.
  - Identificadores de negocio que no sean datos sensibles, como `reservaId`, `turnoId` o `barberoId`.
    
* **Datos sensibles que nunca deben registrarse:**

  - Contraseñas en texto plano ni contraseñas con hash.
  - Tokens JWT completos con su firma en el header de autorización.
  - Datos de tarjetas de crédito o información bancaria de pagos.
  - Secretos de infraestructura, como la clave `JWT_SECRET` o la cadena de conexión con usuario y clave de MongoDB.

---

#### e. Diferencia entre el detalle del log interno y el mensaje que recibe el cliente

La diferencia radica en el público objetivo y en la seguridad de la información:

* **El log interno:** Está hecho para el equipo de desarrollo y soporte técnico. Contiene nombres de clases de Java, líneas exactas de código donde saltó el error, queries internas a MongoDB, stack traces completos y detalles del servidor. Esta información es profunda para diagnosticar la causa raíz del problema de forma rápida.
* **El mensaje al cliente:**
  Está hecho para el usuario final o el frontend. Debe ser seguro, claro y amigable, entregando únicamente el código HTTP correspondiente y un mensaje funcional como franja sin cupos disponibles o correo con formato incorrecto.

Nunca se debe exponer el error técnico interno al cliente porque revelar nombres de tecnologías, versiones de librerías o estructuras de base de datos le da pistas valiosas a atacantes para encontrar vulnerabilidades en el sistema.

---

# Fase de Implementación — Parte Práctica Consolidación del diseño

## 1. Estrategia de Ramas y Flujo Git

Para garantizar la trazabilidad y el aislamiento de cada funcionalidad, cada uno de los 5 casos de uso fue desarrollado en una rama dedicada bajo el patrón `feature/<caso-de-uso>`, completando su flujo integral (Controlador, DTOs, Mappers, Servicio, Modelo, Repositorio MongoDB y Pruebas Unitarias), antes de integrarse sobre `develop` y eliminar la rama correspondiente:

| Caso de Uso | Rama Feature | Commits Atómicos | Integración en `develop` | Estado de la Rama |
|---|---|---|---|---|
| **Iniciar sesión** | `feature/iniciar-sesion` | `feat(auth): implement user authentication with JWT generation and password hashing`<br>`test(auth): add unit tests for authentication flow and JWT validation`<br>`config(security): configure Spring Security filter chain and JWT authentication filter` | Merge commit `b951f3d` | Fusionada en `develop` y borrada |
| **Crear turno** | `feature/crear-turno` | `feat(turnos): implement slot creation endpoint with barber availability rules`<br>`test(turnos): add unit tests for turno creation validation rules` | Merge commit `6d5b955` | Fusionada en `develop` y borrada |
| **Pedir un turno** | `feature/pedir-turno` | `feat(reservas): implement reservation request with strict short-circuit validation chain`<br>`test(reservas): add unit tests verifying short-circuit validation and slot occupation` | Merge commit `965f49d` | Fusionada en `develop` y borrada |
| **Consultar mis reservas** | `feature/consultar-mis-reservas` | `feat(reservas): implement authenticated client reservations retrieval`<br>`test(reservas): add unit tests for client reservations query with descending order` | Merge commit `7bd5ec4` | Fusionada en `develop` y borrada |
| **Consultar todas las reservas** | `feature/consultar-todas-reservas` | `feat(reservas): implement admin reservations inspection endpoint`<br>`test(reservas): add unit tests for admin reservations auditing` | Merge commit `d9c3dce` | Fusionada en `develop` y borrada |
| **Consolidación de pruebas y docs** | `develop` | `test(coverage): consolidate persistence mappers and initializer tests reaching 88% JaCoCo coverage`<br>`feat(domain): complete domain models, persistence mappers, and infrastructure unit tests`<br>`docs: update README with implementation phase evidences and technical specs` | Directo en `develop` | Ramas consolidadas en `origin/develop` |

### Historial de integración en Git:

```text
* 768ed0a feat(domain): complete domain models, persistence mappers, and infrastructure unit tests
* 9acd504 test(coverage): consolidate persistence mappers and initializer tests reaching 88% JaCoCo coverage
*   d9c3dce Merge branch 'feature/consultar-todas-reservas' into develop
|\  
| * c6d8f3d test(reservas): add unit tests for admin reservations auditing
| * ba85622 feat(reservas): implement admin reservations inspection endpoint
|/  
*   7bd5ec4 Merge branch 'feature/consultar-mis-reservas' into develop
|\  
| * fa2aa40 test(reservas): add unit tests for client reservations query with descending order
| * 799995e feat(reservas): implement authenticated client reservations retrieval
|/  
*   965f49d Merge branch 'feature/pedir-turno' into develop
|\  
| * a60fa21 test(reservas): add unit tests verifying short-circuit validation and slot occupation
| * c7a0e5c feat(reservas): implement reservation request with strict short-circuit validation chain
|/  
*   6d5b955 Merge branch 'feature/crear-turno' into develop
|\  
| * e292290 test(turnos): add unit tests for turno creation validation rules
| * afc2896 feat(turnos): implement slot creation endpoint with barber availability rules
|/  
*   b951f3d Merge branch 'feature/iniciar-sesion' into develop
|\  
| * 0367b44 config(security): configure Spring Security filter chain and JWT authentication filter
| * f10722b test(auth): add unit tests for authentication flow and JWT validation
| * 8aa0c82 feat(auth): implement user authentication with JWT generation and password hashing
|/  
* 787b5ac Fix formatting issues in README.md
```

---

## 2. Inyección de Dependencias por Constructor

Siguiendo las mejores prácticas de Spring Framework y los principios de diseño de software limpio:
- Todos los controladores (`AuthController`, `TurnoController`, `ReservaController`) y servicios administrados (`AuthServiceImpl`, `TurnoServiceImpl`, `ReservaServiceImpl`) implementan **inyección de dependencias por constructor explícito**.
- Todos los colaboradores inyectados se declaran como atributos `private final`, garantizando **inmutabilidad** y evitando estados inconsistentes o dependencias nulas.
- Esta separación de responsabilidades facilita la instanciación directa de las clases en las pruebas unitarias con mocks (`Mockito`), eliminando la necesidad de levantar el contexto pesado de Spring Boot.

Ejemplo en [`TurnoServiceImpl.java`](src/main/java/edu/dosw/parcial/core/services/TurnoServiceImpl.java):
```java
@Slf4j
@Service
public class TurnoServiceImpl implements TurnoService {

    private final TurnoRepository turnoRepository;
    private final BarberoRepository barberoRepository;
    private final TurnoPersistenceMapper turnoPersistenceMapper;
    private final TurnoDtoMapper turnoDtoMapper;

    public TurnoServiceImpl(
            TurnoRepository turnoRepository,
            BarberoRepository barberoRepository,
            TurnoPersistenceMapper turnoPersistenceMapper,
            TurnoDtoMapper turnoDtoMapper) {
        this.turnoRepository = turnoRepository;
        this.barberoRepository = barberoRepository;
        this.turnoPersistenceMapper = turnoPersistenceMapper;
        this.turnoDtoMapper = turnoDtoMapper;
    }
    // ...
}
```

---

## 3. Implementación de los 5 Casos de Uso (Flujo Completo)

Cada caso de uso implementa el ciclo de vida completo: validación sintáctica de DTOs con Bean Validation (`@Valid`), enrutamiento REST con Spring Security, reglas de negocio en la capa Core y persistencia en MongoDB.

### 1. Iniciar sesión (`POST /api/v1/auth/login`)
- **Acceso:** Público (sin token).
- **Entrada:** `LoginRequest` con correo y contraseña.
- **Lógica:** Valida la existencia del usuario en `UsuarioRepository` y comprueba la contraseña cifrada mediante `BCryptPasswordEncoder`. Al autenticar exitosamente, genera un token JWT firmado (JJWT) que encapsula el correo y el rol (`CLIENTE` o `ADMIN`).
- **Respuesta:** `200 OK` con `AuthResponse` (`token`, `tipoToken: "Bearer"`, `usuario`, `rol`).
- **Errores:** `401 Unauthorized` si las credenciales son incorrectas o el usuario no existe.

### 2. Crear turno (`POST /api/v1/turnos`)
- **Acceso:** Protegido con rol `ADMIN` (`@PreAuthorize("hasRole('ADMIN')")`).
- **Entrada:** `CrearTurnoRequest` (`barberoId`, `fecha: YYYY-MM-DD`, `hora: HH:mm`).
- **Lógica:** 
  1. Valida que el barbero exista en MongoDB.
  2. Valida que la fecha sea futura (`LocalDate.parse(fecha).isAfter(hoy)`).
  3. Valida que la hora se encuentre dentro de la jornada laboral del barbero (`08:00 - 20:00`).
  4. Valida que no exista un turno duplicado para el mismo barbero en esa fecha y hora.
  5. Persiste el turno en MongoDB con estado inicial `DISPONIBLE`.
- **Respuesta:** `201 Created` con `TurnoResponse`.
- **Errores:** `400 Bad Request` (formato inválido), `409 Conflict` (turno duplicado), `422 Unprocessable Entity` (fecha no futura o fuera de jornada).

### 3. Pedir un turno (`POST /api/v1/reservas`)
- **Acceso:** Protegido para usuarios autenticados (`CLIENTE` o `ADMIN`).
- **Entrada:** `CrearReservaRequest` (`clienteNombre`, `clienteCorreo`, `clienteTelefono`, `barberoId`, `fecha`, `hora`, `codigoServicio`).
- **Cadena de validaciones en orden estricto (Short-Circuit):**
  1. **Capacidad de franja (E01):** Verifica que la franja horaria no exceda el límite máximo de 3 reservas activas (`countByFechaAndHoraAndEstado(fecha, hora, "CONFIRMADA") >= 3`). Si está llena, lanza `ConflictException` (`409 Conflict`, código `E01`).
  2. **Disponibilidad de turno y barbero activo (E03 / E02):**
     - Busca un turno para el barbero en esa fecha y hora con estado `DISPONIBLE`. Si no existe o el barbero no está activo, lanza `ConflictException` (`409 Conflict`, código `E03`).
     - Comprueba que el barbero posea en su catálogo de especialidades el servicio requerido. Si no lo domina, lanza `BusinessValidationException` (`422 Unprocessable Entity`, código `E02`).
  3. **Existencia del servicio en catálogo (E09):** Consulta el servicio en `ServicioRepository`. Si no existe, lanza `ResourceNotFoundException` (`404 Not Found`, código `E09`).
  4. **Creación atómica de la reserva:** Solo si **todas** las validaciones previas son superadas con éxito:
     - El turno cambia su estado a `OCUPADO` y se actualiza en MongoDB.
     - Se persiste la nueva reserva con estado `CONFIRMADA`, calculando el total a pagar según el precio oficial del catálogo.
- **Respuesta:** `201 Created` con `ReservaResponse` con identificador único y estado `CONFIRMADA`.

### 4. Consultar mis reservas (`GET /api/v1/reservas/me` o `GET /api/v1/reservas/mis-reservas`)
- **Acceso:** Protegido para clientes autenticados (`CLIENTE`).
- **Lógica:** Extrae la identidad del usuario a partir del token JWT autenticado (`SecurityContextHolder`), busca sus reservas en `ReservaRepository` ordenadas por fecha/hora y las mapea al DTO de salida. Soporta mapeo dual para máxima compatibilidad con la rúbrica del parcial.
- **Respuesta:** `200 OK` con lista de `ReservaResponse`.

### 5. Consultar todas las reservas (`GET /api/v1/reservas`)
- **Acceso:** Protegido exclusivamente para rol `ADMIN` (`@PreAuthorize("hasRole('ADMIN')")`).
- **Lógica:** Consulta la totalidad de reservas registradas en el sistema para auditoría y gestión de la barbería.
- **Respuesta:** `200 OK` con lista completa de `ReservaResponse`.

---

## 4. Documentación con Swagger y OpenAPI 3

La API cuenta con documentación viva generada mediante `springdoc-openapi-starter-webmvc-ui:2.5.0` y configurada en [`OpenApiConfig.java`](src/main/java/edu/dosw/parcial/config/OpenApiConfig.java):

- **Esquema de Seguridad:** Configurado como HTTP `BearerAuth` con formato JWT.
- **Botón `Authorize`:** Permite ingresar el token JWT obtenido en `/api/v1/auth/login`. Al ingresar el token (ejemplo: `eyJhbGci...`), Swagger UI inyecta automáticamente el encabezado `Authorization: Bearer <token>` en todas las peticiones a endpoints protegidos.
- **Endpoints documentados:** Todos los 5 casos de uso cuentan con descripciones detalladas, códigos de respuesta HTTP (`200`, `201`, `400`, `401`, `403`, `404`, `409`, `422`) y ejemplos de esquemas JSON de entrada y salida.

**Enlaces Swagger en producción:**
- **Swagger UI:** https://dosw-parcial-josemartinez-f4hwgudtgwcqgdb8.westus3-01.azurewebsites.net/swagger-ui/index.html
- **OpenAPI JSON Spec:** https://dosw-parcial-josemartinez-f4hwgudtgwcqgdb8.westus3-01.azurewebsites.net/v3/api-docs

---

## 5. Estrategia y Niveles de Logging

El backend utiliza `@Slf4j` (SLF4J + Logback) con segregación estructurada de niveles de severidad en todas las capas:

| Nivel | Cuándo se utiliza | Ejemplo en el código |
|---|---|---|
| `INFO` | Inicio de operaciones clave, recepción de peticiones en controladores, confirmación de reservas y turnos, inicios de sesión válidos. | `log.info("Reserva creada y confirmada exitosamente: id=[{}], barbero=[{}], total=[{}]", ...)` |
| `WARN` | Rechazos de reglas de negocio esperadas y controladas (franja llena E01, especialidad no coincidente E02, barbero inactivo E03, credenciales inválidas). | `log.warn("Rechazo por franja llena (E01): [{}] reservas activas en fecha [{}]", ...)` |
| `ERROR` | Excepciones no controladas o fallas de infraestructura en `GlobalExceptionHandler`. | `log.error("Excepción inesperada en el servidor: ", ex)` |

> [!SECURITY]
> **Protección de Datos Sensibles:** Ningún registro de log imprime contraseñas en texto plano, hashes de claves ni tokens JWT completos, cumpliendo con los estándares de privacidad y seguridad de la información.

---

## 6. Pruebas Unitarias y Cobertura (JUnit 5 + Mockito)

Las pruebas unitarias fueron construidas bajo el patrón **Arrange, Act, Assert (AAA)**, cubriendo controladores, servicios centrales de lógica de negocio, generadores de tokens JWT y mappers de persistencia:

### Resumen de Pruebas:
- **Total de pruebas ejecutadas:** 43 tests
- **Fallos (Failures):** 0
- **Errores:** 0
- **Tiempo de ejecución:** ~4.5 segundos

```text
[INFO] Results:
[INFO] 
[INFO] Tests run: 43, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] BUILD SUCCESS
```

### Cobertura de Código JaCoCo:
- **Cobertura de Instrucciones (Instruction Coverage):** **87.74%**
- **Servicios Principales (`core.services`):** **97.60%** de cobertura de instrucciones.
  - `ReservaServiceImpl`: **97.6%** de cobertura de instrucciones (incluye asignación de barberos por especialidad).
  - `AuthServiceImpl`: **100%** de cobertura de instrucciones.
  - `TurnoServiceImpl`: **94.0%** de cobertura de instrucciones.
  - Mappers y Validadores: **100%** de cobertura.

---

## 7. Pipeline de CI/CD y Despliegue en Producción

El flujo de entrega continua está automatizado mediante GitHub Actions en [`.github/workflows/ci-cd.yml`](.github/workflows/ci-cd.yml) y se ejecuta con cada integración en la rama `develop`:

1. **Job `test`:** Descarga el código, levanta Java 21 LTS y ejecuta `mvn verify`, validando compilación, suite completa de 42 pruebas unitarias y generación de reportes JaCoCo.
2. **Job `docker`:** Depende de `test`. Construye la imagen de contenedor Docker inmutable multietapa y realiza el push a Docker Hub (`josemartinez883/dosw-parcial-t2:latest`).
3. **Job `deploy`:** Depende de `docker`. Autentica contra Microsoft Azure mediante Service Principal y despliega el contenedor en el recurso de Azure App Service.

### Enlaces y Evidencias de Producción:
- **URL Base en Producción (Azure App Service):** https://dosw-parcial-josemartinez-f4hwgudtgwcqgdb8.westus3-01.azurewebsites.net
- **Health Check en Vivo:** https://dosw-parcial-josemartinez-f4hwgudtgwcqgdb8.westus3-01.azurewebsites.net/api/v1/health
- **Swagger UI en Vivo:** https://dosw-parcial-josemartinez-f4hwgudtgwcqgdb8.westus3-01.azurewebsites.net/swagger-ui/index.html
- **Repositorio GitHub:** https://github.com/JoseMartinez883/DOSW_ParcialT2_JoseMartinez

---

## 8. Bono: Orquestación Local con Docker Compose

Para cumplir con el bono del parcial y garantizar portabilidad total sin dependencias instaladas en la máquina host, se proporciona el archivo [`docker-compose.yml`](docker-compose.yml) y la plantilla de variables de entorno [`.env.example`](.env.example).

### Servicios Orquestados:
1. **`api` (`bobs-barber-api`):** Servicio backend Spring Boot empaquetado mediante build multietapa (Maven 3.9 + Temurin 21 JRE). Expone el puerto `8080:8080` y se conecta automáticamente a la red interna `bobs-barber-net`.
2. **`mongodb` (`bobs-barber-mongodb`):** Base de datos NoSQL oficial `mongo:7` exponiendo el puerto `27017:27017` con volumen dedicado `mongo-data` para persistencia continua y comprobación de salud (`healthcheck` vía `mongosh ping`).

### Comandos de Ejecución:
```powershell
# 1. Levantar el stack completo en segundo plano (con reconstrucción de imagen)
docker compose up --build -d

# 2. Consultar el estado de los contenedores
docker compose ps

# 3. Seguir los logs de la API en tiempo real
docker compose logs -f api

# 4. Detener y remover los contenedores
docker compose down

# 5. Detener y remover contenedores junto con el volumen de datos
docker compose down -v
```

### Credenciales Sembradas para Pruebas:
Al iniciar, `DatabaseSeeder` puebla automáticamente la base de datos con usuarios y datos oficiales:
* **Administrador (`ADMIN`):** `admin@gmail.com` / `password123`
* **Cliente (`CLIENTE`):** `juan@gmail.com` / `password123`

### Catálogo de Barberos y Servicios Oficiales Disponibles:
* **Barberos:**
  * **Miguel:** Lun–Jue (10:00–17:00) — Especialidades: `CORTE`, `BARBA`.
  * **Laura:** Lun–Vie (10:00–17:00) — Especialidades: `CORTE`, `TINTURA`.
  * **Andrés:** Mar, Jue, Sáb–Dom (10:00–14:00) — Especialidades: `CORTE`, `AFEITADO`.
* **Servicios:**
  * `SER-001`: Corte clásico — $25,000
  * `SER-002`: Barba tradicional — $18,000
  * `SER-003`: Corte y barba — $38,000
  * `SER-004`: Afeitado de cabeza — $20,000
  * `SER-005`: Tinte — $45,000


