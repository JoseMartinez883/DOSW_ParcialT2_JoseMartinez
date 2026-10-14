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
[Definición del contrato de la API.xlsx](docs/excel/Definici%C3%B3n%20del%20contrato%20de%20la%20API.xlsx)

preview:
![img.png](docs/images/previewExcel.png)

### ¿Cuál es la diferencia entre una validación de entrada y una validación de negocio? Seleccionen tres reglas del caso, incluyendo al menos una de cada tipo, y justifiquen su clasificación.

La diferencia principal está en **qué revisa cada una y en qué parte de la aplicación se ejecuta**:

* **Validación de entrada:** Revisa únicamente el formato, tipo y que esten los datos que el cliente. Se hace apenas llega la petición en el controlador (usando DTOs y anotaciones como `@NotNull`, `@Pattern`, etc.). Su ventaja es que es rápida y no necesita conectarse a la base de datos ni consultar nada del sistema, si los datos vienen mal escritos o incompletos, se rechazan de una vez con un error `400 Bad Request`.

* **Validación de negocio:** Revisa que la solicitud tenga sentido y cumpla con las reglas de negocios de la barbería (esto en nuetro caso del parcial). Esta validación se hace en los servicios (en la lógica del negocio) y sí necesita consultar la base de datos (MongoDB). Por ejemplo, verifica si un barbero realmente trabaja a esa hora, si tiene la especialidad requerida o si ya se llenó el cupo de la hora. Si falla, devuelve errores como `404 Not Found`, `409 Conflict` o `422 Unprocessable Entity`.

#### Tres reglas del caso Bob's Barber justificadas:

1. **Validación del correo del cliente (`E08 - Correo inválido`):**
   * *Regla:* El correo no puede ser nulo, debe tener formato de email y su dominio tiene que ser obligatoriamente `@gmail.com`.
   * *Justificación:* Es de **entrada**, porque se valida directamente en el DTO con una expresión regular apenas llega el JSON. No necesitamos consultar MongoDB para saber si el texto termina en `@gmail.com` o no.

2. **Validación del teléfono del cliente (`E07 - Teléfono inválido`):**
   * *Regla:* El teléfono no puede ser nulo y debe tener exactamente 10 dígitos numéricos.
   * *Justificación:* Es de **entrada**, porque solo revisa la longitud y el tipo de dato que el usuario escribió en la petición, antes de pasar a cualquier otra lógica.

3. **Compatibilidad de especialidad del barbero (`E02 - Especialidad incompatible`):**
   * *Regla:* El barbero asignado debe saber hacer el servicio que el cliente pidió (por ejemplo, si pide `SER-002` Corte + barba, el barbero debe tener la especialidad `CORTE CON BARBA`).
   * *Justificación:* Es de **negocio**, porque con solo ver el JSON de la petición no sabemos qué sabe hacer el barbero. El servicio tiene que ir a la base de datos, buscar al barbero, revisar su lista de especialidades y compararla con el servicio solicitado.

---

### En Pedir un turno, ¿cómo debe responder la API si fallan varias condiciones? Expliquen qué error se informa y qué cambios deben evitarse.

* **Cómo responde la API:**  
  La API sigue la cadena de validaciones en orden estricto (1. Franja disponible, 2. Barbero disponible con especialidad, 3. Datos del cliente, 4. Servicio permitido). Si se encuentran varias fallas en la misma petición, la API se detiene inmediatamente en la primera que falle.

* **Qué error se informa:**  
  Se informa **únicamente el motivo de la primera falla que ocurrió**, con su código HTTP correspondiente. Por ejemplo, si la franja ya estaba llena (`E01`), se responde inmediatamente un `409 Conflict` indicando que la franja está llena. La API no sigue revisando las demás condiciones ni devuelve una lista acumulada de errores.

* **Qué cambios deben evitarse:**  
  Al fallar cualquier validación, el proceso se cancela de inmediato y no debe haber ningún cambio en la base de datos:
  * **No se debe crear la reserva:** No se puede guardar ningún registro nuevo en la colección de reservas.
  * **No se debe modificar el turno:** El turno debe quedarse en estado `DISPONIBLE` y no pasar a `OCUPADO`. Esto ya que el turno que el cliente solicito, no se reservo, es decir el turno sigue 
  'DISPONIBLE'. Aunque, tambien se puede presentar el caso que el turno no este DISPONIBLE, en ese caso la reserva no se crearia. 
  * La base de datos debe quedar exactamente igual que antes de que llegara la petición.  

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

---

## Punto 5
Asignación del Equipo de Calidad - Definir cómo comprobar el comportamiento:

El equipo de QA utilizará el contrato de la API como referencia para validar la solución. Su tarea es definir los escenarios de pruebas de Pedir un turno, cubriendo tanto las reservas exitosas como los rechazos siguiendo la aplicación del esquema de TDD (Red, Green, Refactor).

Explique cómo comprobaría que se respeta el orden y que, ante una falla, no se crea la reserva ni se modifica el turno. No es necesario escribir el código de las pruebas en esta respuesta.

---

### Desarrollo

#### 1. Ciclo TDD aplicado a la operación Pedir un turno

* **Red (Fase roja):**  
  Escribimos la prueba unitaria o de integración antes de implementar el servicio o validador. Por ejemplo, definimos un test que espera que una petición con datos válidos retorne un estado `201 Created` y persista la reserva. Al ejecutar la suite, la prueba falla porque la lógica o el endpoint aún no existen o no cumplen la aserción.

* **Green (Fase verde):**  
  Implementamos el código mínimo en el controlador y servicio para satisfacer la prueba y hacer que pase a verde, sin agregar optimizaciones ni lógica de más.

* **Refactor (Fase de refactorización):**  
  Limpiamos el código, extraemos validadores a clases independientes, mejoramos nombres y desacoplamos dependencias, comprobando en cada cambio que toda la suite de pruebas siga pasando en verde.

---

#### 2. Escenarios de prueba para Pedir un turno

| Escenario | Entrada / Condiciones | Resultado esperado | Validación clave |
|---|---|---|---|
| **Reserva exitosa** (Camino feliz) | Franja horaria disponible, barbero asignado con especialidad requerida, cliente con correo `@gmail.com` y teléfono de 10 dígitos, servicio permitido. | `201 Created` con payload de la reserva (`idReserva`, `fechaHora`, estado `CONFIRMADA`). | Se inserta 1 documento en la colección de reservas y el turno pasa de `DISPONIBLE` a `OCUPADO`. |
| **Rechazo por franja llena** (E01) | Franja horaria sin cupos o ya ocupada. | `409 Conflict` con código de error `E01`. | El flujo se corta de inmediato; no se verifica el barbero ni el cliente; ningún dato se modifica. |
| **Rechazo por especialidad incompatible** (E02) | Franja disponible, pero el barbero seleccionado no tiene la especialidad que exige el servicio solicitado. | `422 Unprocessable Entity` con código `E02`. | El servicio rechaza la solicitud; no se crea reserva y el turno sigue en estado `DISPONIBLE`. |
| **Rechazo por barbero no disponible** (E03) | Franja disponible y barbero con especialidad, pero el barbero no está activo o se encuentra en descanso. | `409 Conflict` con código `E03`. | Se cancela la operación sin guardar registros huérfanos. |
| **Rechazo por datos de cliente inválidos** (E07 / E08) | Correo sin dominio `@gmail.com` o teléfono diferente a 10 dígitos numéricos. | `400 Bad Request` disparado por las validaciones de entrada (`@Valid`). | La petición se rechaza en la capa del controlador mediante los DTOs, sin consultar MongoDB. |

---

#### 3. Comprobación del orden de validación y de la atomicidad ante fallas

* **Comprobación del orden estricto:**  
  Configuramos pruebas donde el request contiene varias fallas al mismo tiempo (por ejemplo, franja llena y simultáneamente un barbero sin especialidad). En el assert verificamos que la respuesta devuelva únicamente el error del primer paso (`E01 - Franja llena`). Usando Mockito sobre los colaboradores, validamos con `verifyNoInteractions()` o `verify(..., never())` que los métodos correspondientes a las validaciones posteriores nunca llegaron a ejecutarse.

* **Comprobación de no creación y no modificación:**  
  En las pruebas de fallo, antes de disparar la petición capturamos el conteo de reservas en base de datos y el estado actual del turno. Al recibir la respuesta de error (400, 409 o 422), consultamos nuevamente MongoDB y comprobamos que:
  - El conteo de documentos en la colección de reservas no cambió.
  - El documento del turno sigue teniendo exactamente el estado `DISPONIBLE`.
  - Si la falla ocurre a mitad del servicio, la anotación `@Transactional` revierte cualquier cambio pendiente, garantizando consistencia total.

---

### Preguntas para la revisión con el equipo de Calidad

#### a. Evidencia del cumplimiento de reglas de negocio y alcance de pruebas unitarias

* **Cómo las pruebas aportan evidencia:**  
  Las pruebas automatizadas funcionan como especificaciones vivas del sistema. Cada regla del contrato queda plasmada en un caso de prueba ejecutable. Cuando el pipeline de CI/CD corre `mvn test`, genera reportes objetivos (Surefire y JaCoCo) que demuestran que el sistema reacciona exactamente como exige el negocio ante datos correctos y erróneos, sirviendo como evidencia técnica auditable.

* **Qué se verifica puntualmente con pruebas unitarias:**  
  Se valida la lógica pura de negocio en aislamiento, simulando con mocks la base de datos y servicios externos:
  - Que los métodos validadores arrojen la excepción de dominio correspondiente ante condiciones inválidas (como fechas pasadas o barberos sin especialidad).
  - Que la transición de estados en el modelo `Turno` solo permita transiciones válidas (`DISPONIBLE` a `OCUPADO`).
  - Que los mappers transformen correctamente los campos entre DTOs, entidades y modelos de dominio sin perder datos ni truncar valores.
  - Que la secuencia de validaciones en la capa de servicio se detenga en el primer fallo sin invocar el guardado en el repositorio.

---

#### b. ¿Las pruebas por sí solas garantizan la ausencia de errores?

No. Las pruebas demuestran la presencia de defectos conocidos, pero no pueden garantizar la ausencia total de errores en un software.

* **Justificación técnica:**  
  Una suite de pruebas, incluso alcanzando altos porcentajes de cobertura de código, solo puede evaluar los escenarios y casos de borde que los desarrolladores y QA previeron al escribirlas. No cubre situaciones imprevistas del entorno real como:
  - Condiciones de carrera y concurrencia extrema cuando cientos de usuarios intentan reservar el mismo turno en el mismo milisegundo.
  - Comportamientos atípicos de infraestructura, como caídas de red, latencias altas o saturación del pool de conexiones a MongoDB.
  - Malentendidos en los requerimientos originales: el código puede pasar todas sus pruebas en verde porque cumple la lógica programada, pero esa lógica podría estar resolviendo una necesidad equivocada del cliente.
  
  Por este motivo, las pruebas automatizadas deben complementarse con análisis estático de código (SonarQube), pruebas de carga y monitoreo continuo en producción mediante logs estructurados y métricas de salud.
