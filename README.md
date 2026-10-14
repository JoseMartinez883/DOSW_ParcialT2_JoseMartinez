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

La diferencia principal está en qué revisa cada una y en qué capa se ejecuta:

* **Validación de entrada:**  
  Revisa únicamente que los datos vengan completos, con el tipo correcto y el formato esperado. Se ejecuta apenas entra la petición al controlador usando los DTOs con anotaciones de Bean Validation como `@NotNull` o `@Pattern`. Su gran ventaja es la velocidad: no necesita tocar la base de datos ni consultar lógica interna, así que si los datos vienen mal estructurados, la API corta la petición de una vez devolviendo un `400 Bad Request`.

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

* **Cómo responde la API:**  
  La API sigue la cadena de validaciones en orden secuencial estricto: primero franja disponible, luego barbero disponible con especialidad, después datos del cliente y finalmente servicio permitido. Si se presentan varias fallas en una misma petición, el flujo se detiene en la primera que no se cumpla y no sigue evaluando las demás.

* **Qué error se informa:**  
  Se informa únicamente el motivo de la primera falla encontrada junto a su código HTTP. Por ejemplo, si la franja ya estaba llena con error E01, se responde inmediatamente un `409 Conflict` indicando que no hay cupo. La API no evalúa las siguientes reglas ni acumula una lista de errores.

* **Qué cambios deben evitarse:**  
  Al fallar cualquier validación, la operación se cancela en el acto y la base de datos debe quedar exactamente igual que antes de la petición:
  * **No se crea la reserva:** no se guarda ningún registro en la colección de reservas.
  * **No se altera el turno:** el turno se mantiene en estado `DISPONIBLE` y no pasa a `OCUPADO`. Si el turno ya estaba ocupado, tampoco se modifica su estado previo.

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

* **Fase Red:**  
  Escribimos la prueba unitaria o de integración antes de implementar el servicio o validador. Diseñamos un test que espera que una petición válida devuelva un estado `201 Created` y persista la reserva. Al correr la prueba, falla de inmediato porque el endpoint o la lógica de negocio todavía no existen.

* **Fase Green:**  
  Escribimos la lógica mínima necesaria en el controlador y servicio para que la prueba pase a verde, sin meter código extra ni optimizaciones antes de tiempo.

* **Fase Refactor:**  
  Organizamos el código, desacoplamos validadores en clases dedicadas y mejoramos nombres, corriendo la suite completa para asegurarnos de que todo siga pasando en verde.

---

#### 2. Escenarios de prueba para Pedir un turno

| Escenario | Entrada y condiciones | Resultado esperado | Validación clave |
|---|---|---|---|
| **Reserva exitosa** | Franja horaria disponible, barbero con especialidad requerida, cliente con correo `@gmail.com` y teléfono de 10 dígitos, servicio permitido. | `201 Created` con payload de la reserva confirmada y su identificador. | Se guarda la reserva en MongoDB y el turno pasa de `DISPONIBLE` a `OCUPADO`. |
| **Rechazo por franja llena - E01** | Franja horaria sin cupos o previamente ocupada. | `409 Conflict` con código E01. | El flujo se corta de inmediato sin evaluar al barbero ni al cliente, dejando la base de datos intacta. |
| **Rechazo por especialidad incompatible - E02** | Franja disponible, pero el barbero asignado no cuenta con la especialidad del servicio pedido. | `422 Unprocessable Entity` con código E02. | Se cancela la solicitud sin crear reserva y el turno sigue en estado `DISPONIBLE`. |
| **Rechazo por barbero no disponible - E03** | Franja disponible y barbero con especialidad, pero se encuentra inactivo o en descanso. | `409 Conflict` con código E03. | Se aborta la operación sin guardar registros huérfanos. |
| **Rechazo por datos de cliente inválidos - E07 y E08** | Correo sin dominio `@gmail.com` o teléfono que no tenga 10 dígitos. | `400 Bad Request` disparado por las validaciones de entrada en el DTO. | La petición se frena en el controlador mediante `@Valid` antes de tocar la base de datos. |

---

#### 3. Comprobación del orden de validación y de la atomicidad ante fallas

* **Comprobación del orden estricto:**  
  Montamos pruebas donde el request contiene varias fallas al mismo tiempo, como mandar una franja llena y a la vez un barbero sin especialidad. En el assert verificamos que la API retorne únicamente el error del primer paso, que es franja llena E01. Con Mockito comprobamos que los métodos de las validaciones posteriores y el repositorio de reservas nunca llegaron a ejecutarse.

* **Comprobación de no creación y no modificación:**  
  En las pruebas de rechazo, antes de enviar la petición consultamos el conteo de reservas y el estado del turno. Luego de recibir el error 400, 409 o 422, verificamos de nuevo en MongoDB:
  - El conteo de reservas se mantiene igual.
  - El turno conserva su estado `DISPONIBLE`.
  - Si la falla ocurre a mitad del flujo, `@Transactional` revierte cualquier cambio intermedio, garantizando que nada quede a medias.

---

### Preguntas para la revisión con el equipo de Calidad

#### a. Evidencia del cumplimiento de reglas de negocio y alcance de pruebas unitarias

* **Cómo las pruebas aportan evidencia:**  
  Las pruebas automatizadas sirven como especificaciones vivas del sistema. Cada regla del contrato queda asegurada en un test ejecutable. Cuando el pipeline corre `mvn test` en cada push o PR, los reportes de ejecución demuestran con hechos que la API responde tal como espera el negocio ante datos válidos y casos de error.

* **Qué se verifica puntualmente con pruebas unitarias:**  
  Se prueba la lógica pura en aislamiento total, usando mocks para no depender de la base de datos ni de servicios externos:
  - Que los validadores lancen la excepción de dominio correspondiente ante datos inválidos, como un barbero sin la especialidad requerida.
  - Que el modelo `Turno` solo permita transiciones válidas de `DISPONIBLE` a `OCUPADO`.
  - Que los mappers transformen los DTOs y entidades de dominio sin perder información.
  - Que el servicio corte la ejecución en el primer fallo y no llame al repositorio de reservas.

---

#### b. ¿Las pruebas por sí solas garantizan la ausencia de errores?

No. Las pruebas demuestran que hay defectos cuando fallan, pero no pueden garantizar que el software esté 100% libre de errores.

* **Justificación técnica:**  
  Una suite de pruebas, incluso con cobertura alta, solo valida los casos que el equipo llegó a imaginar y programar en los tests. Quedan por fuera factores del entorno real como:
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

```mermaid
flowchart TD
    subgraph ClientDevice ["Dispositivo Cliente - Navegador o App Web"]
        FrontendApp["Artefacto: SPA Web / Consumidor HTTP"]
    end

    subgraph GitHubEnv ["Plataforma GitHub - CI/CD"]
        Workflow["Pipeline: ci-cd.yml"]
        Runner["Runner: Ubuntu Latest"]
    end

    subgraph DockerRegistry ["Docker Hub - Registro de Contenedores"]
        DockerImage["Imagen: josemartinez883/dosw-parcial-t2:latest"]
    end

    subgraph CloudAzure ["Microsoft Azure - App Service"]
        subgraph AppServiceNode ["Nodo de Ejecución: Azure Web App - Linux"]
            DockerContainer["Contenedor Docker: Spring Boot 3 - JDK 21"]
            AppArtifact["Artefacto: dosw-parcial.jar"]
        end
    end

    subgraph CloudMongo ["MongoDB Atlas - Clúster en la Nube"]
        subgraph MongoNode ["Nodo de Base de Datos: Replica Set"]
            Database["Colecciones: reservas, turnos, barberos, usuarios"]
        end
    end

    FrontendApp -->|"HTTPS / REST en puerto 443 a 8080"| DockerContainer
    Runner -->|"Git push a main o develop"| Workflow
    Workflow -->|"Docker login, build y push"| DockerImage
    Workflow -->|"Deploy webhook y credenciales Azure"| AppServiceNode
    DockerContainer -->|"Pull de imagen latest"| DockerImage
    DockerContainer -->|"MongoDB Wire Protocol con TLS puerto 27017"| Database
```

#### 2. Especificación técnica del despliegue

| Elemento | Tipo | Detalle técnico | Rol en la arquitectura |
|---|---|---|---|
| **Dispositivo Cliente** | Dispositivo físico o lógico | Navegador web, cliente HTTP o frontend de la barbería | Emite las peticiones HTTP y consume los endpoints definidos en el contrato de la API. |
| **Pipeline GitHub Actions** | Servidor de automatización | Runner en Ubuntu Latest con Java 21 Temurin y Docker CLI | Compila el código, ejecuta las pruebas unitarias y genera la imagen inmutable. |
| **Docker Hub** | Registro de artefactos | Repositorio de imágenes Docker con la etiqueta latest | Almacena y distribuye la imagen de contenedor lista para ejecución. |
| **Azure App Service** | Nodo de ejecución en la nube | Instancia Web App Linux en Microsoft Azure en puerto 8080 | Aloja y corre el contenedor Docker con la aplicación Spring Boot en producción. |
| **MongoDB Atlas** | Nodo de base de datos en la nube | Clúster gestionado con réplica y cifrado TLS | Persiste la información de turnos, reservas, usuarios y servicios. |
| **Mecanismos de comunicación** | Protocolos de red | HTTPS en puerto 443 mapeado a 8080 y protocolo MongoDB en puerto 27017 | Asegura tráfico cifrado tanto desde el cliente como hacia el clúster de datos. |

---

### Preguntas para la revisión con el equipo de Devops

#### a. Etapas principales de un pipeline CI/CD y propósito de cada una

* **Etapa de integración y pruebas:**  
  Descarga el código en el runner de GitHub Actions, configura el entorno con Java 21 y ejecuta `mvn verify`. Su objetivo es compilar el proyecto y correr toda la suite de pruebas automatizadas y cobertura. Si algo falla o no compila, corta el pipeline de una vez para que ningún error llegue a etapas posteriores.

* **Etapa de empaquetado y subida de imagen:**  
  Toma el Dockerfile multietapa, construye la imagen del contenedor con el jar generado y se autentica contra Docker Hub para hacer el push de la imagen etiquetada con latest. Su propósito es generar un artefacto inmutable y portable que sea exactamente igual en pruebas y en producción.

* **Etapa de despliegue continuo:**  
  Se conecta a Microsoft Azure usando las credenciales del service principal y le ordena a Azure Web App actualizar el contenedor descargando la última versión de la imagen desde Docker Hub. Su propósito es publicar la aplicación en vivo sin intervención manual y reduciendo al mínimo el tiempo de indisponibilidad.

---

#### b. Qué sucede si una prueba falla y si debe permitirse el despliegue

Si una prueba falla durante el paso de `mvn verify`, Maven arroja un código de salida de error y el job `test` se marca en rojo. Como los jobs de `docker` y `deploy` tienen la instrucción `needs: test`, el pipeline se cancela automáticamente en ese instante.

No se debe permitir el despliegue bajo ninguna circunstancia. El objetivo principal de tener un pipeline automatizado es servir de filtro de calidad. Si permitimos desplegar con una prueba rota, estaríamos mandando a producción un bug confirmado, una regresión que rompe el contrato de la API o una falla que puede corromper datos de turnos y reservas de los usuarios reales.

---

#### c. Para qué sirven los logs y qué registrar ante una reserva fallida o un error de autenticación

Los logs dan visibilidad en tiempo real de lo que ocurre dentro de la aplicación mientras está corriendo en Azure, permitiendo entender fallas y auditar eventos sin tener que pausar el servicio ni entrar a depurar código en vivo.

* **Al investigar una reserva fallida:**  
  Registramos en nivel `WARN` o `ERROR`:
  - Identificador único de la petición o correlation ID para rastrear la llamada completa.
  - Fecha y hora exacta en formato UTC.
  - Método y ruta consumida, como `POST /api/v1/reservas`.
  - Datos de negocio involucrados, como el ID del cliente, ID del barbero, fecha del turno y código de servicio.
  - Motivo puntual del rechazo según la regla que falló, por ejemplo franja llena E01 o barbero sin especialidad E02.
  - Stack trace de la excepción en caso de que la falla haya sido por caída de conexión con MongoDB.

* **Al investigar un error de autenticación:**  
  Registramos en nivel `WARN`:
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

* **El log interno:**  
  Está hecho para el equipo de desarrollo y soporte técnico. Contiene nombres de clases de Java, líneas exactas de código donde saltó el error, queries internas a MongoDB, stack traces completos y detalles del servidor. Esta información es profunda para diagnosticar la causa raíz del problema de forma rápida.

* **El mensaje al cliente:**  
  Está hecho para el usuario final o el frontend. Debe ser seguro, claro y amigable, entregando únicamente el código HTTP correspondiente y un mensaje funcional como franja sin cupos disponibles o correo con formato incorrecto. 
  
Nunca se debe exponer el error técnico interno al cliente porque revelar nombres de tecnologías, versiones de librerías o estructuras de base de datos le da pistas valiosas a atacantes para encontrar vulnerabilidades en el sistema.


