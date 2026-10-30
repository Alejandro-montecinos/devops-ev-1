# Proyecto CI/CD & DevSecOps - Microservicio Pedidos

Este proyecto corresponde a la implementación de un flujo integral de **CI/CD** y prácticas de **DevSecOps** para la integración continua, análisis de calidad/seguridad y despliegue automatizado de un microservicio desarrollado con **Spring Boot**.

---

## 🚀 Tecnologías

* **Lenguaje:** Java 21 (Eclipse Temurin)
* **Framework:** Spring Boot 3.x (Spring Web, Spring Data JPA)
* **Gestor de Construcción:** Maven con Maven Wrapper (`mvnw`)
* **Control de Versiones:** Git / GitHub
* **Orquestador CI/CD:** GitHub Actions
* **Seguridad SCA (Software Composition Analysis):** Snyk (escaneo de dependencias y vulnerabilidades)
* **Testing:** JUnit 5, Mockito
* **Cobertura de Código:** JaCoCo (generación de reportes de cobertura)
* **Calidad y Seguridad de Código SAST:** SonarCloud (análisis estático y Quality Gate)
* **Base de Datos:**
  * **Producción:** MySQL (alojado en AWS)
  * **Testing/CI:** Base de datos en memoria H2 (`application-test.properties`)
* **Infraestructura Cloud:** AWS EC2 (Ubuntu Server, servicio systemd `ms-pedidos`)
* **Documentación API:** OpenAPI 3 / Swagger UI

---

## 🌿 Estrategia de Ramas (Git Flow)

Para el ciclo de desarrollo se utiliza una estructura jerárquica de ramas:

```text
main
 └── develops
      └── feature/<nombre-funcionalidad>
```

* **`main`:** Contiene la versión estable y lista para producción. Todo push a esta rama dispara automáticamente el pipeline completo de CI/CD.
* **`develops`:** Rama de integración continua durante las etapas de desarrollo.
* **`feature/<nombre-funcionalidad>`:** Ramas individuales creadas a partir de `develops` para el desarrollo de nuevos endpoints o componentes (ej: `feature/endpointAgregar`).

**Flujo de integración:**
1. Los cambios se desarrollan y prueban en la rama `feature`.
2. Se realiza Pull Request / merge hacia `develops`.
3. Tras la validación general, se integra hacia `main` disparando el despliegue a producción.

---

## 🔒 Gestión de Secretos (GitHub Secrets)

Para garantizar la seguridad de las credenciales y cumplir con las mejores prácticas de DevSecOps, ninguna contraseña o llave sensible está expuesta en el código fuente. Se configuran como **Repository Secrets** en GitHub Actions:

| Secreto | Descripción |
|---|---|
| `DB_URL` | Cadena de conexión JDBC hacia MySQL en AWS |
| `DB_USERNAME` | Usuario de base de datos |
| `DB_PASSWORD` | Contraseña de base de datos |
| `EC2_HOST` | Dirección IP pública o DNS de la instancia AWS EC2 |
| `EC2_USER` | Usuario de conexión SSH (ej: `ubuntu`) |
| `EC2_SSH_KEY` | Clave privada SSH (`.pem`) para despliegue automatizado |
| `SNYK_TOKEN` | Token de autenticación de Snyk CLI para análisis SCA |
| `SONAR_TOKEN` | Token de autenticación de SonarCloud para análisis SAST |

---

## ⚙️ Pipeline CI/CD (GitHub Actions)

El flujo de trabajo automatizado está definido en `.github/workflows/deploy-ms-pedidos.yml` y se divide en **4 etapas secuenciales**:

```text
               Push a rama 'main'
                       │
                       ▼
       ┌───────────────────────────────┐
       │     1. Snyk Security Scan     │  ──> Escaneo SCA de dependencias
       └───────────────────────────────┘      Reporte: snyk-report.json
                       │ (pasa)
                       ▼
       ┌───────────────────────────────┐
       │   2. Test & JaCoCo Coverage   │  ──> Pruebas JUnit 5 con BD H2
       └───────────────────────────────┘      Reporte: target/site/jacoco
                       │ (pasa)
                       ▼
       ┌───────────────────────────────┐
       │    3. SonarCloud Analysis     │  ──> Análisis SAST de código
       └───────────────────────────────┘      Quality Gate y métricas
                       │ (pasa)
                       ▼
       ┌───────────────────────────────┐
       │   4. Build & Deploy to EC2    │  ──> Compilación de JAR
       └───────────────────────────────┘      Transferencia SCP
                                              Reinicio servicio systemd
```

### Detalle de los Jobs:
1. **`snyk-scan` (Snyk Security Scan):**  
   Instala Snyk CLI y analiza las dependencias del proyecto (`pom.xml`) buscando vulnerabilidades conocidas con severidad alta. Exporta y almacena el artefacto `snyk-report.json`.
2. **`test` (Run Tests with JaCoCo):**  
   Ejecuta la suite de pruebas unitarias usando el perfil `test` con base de datos en memoria H2. Mide la cobertura de código mediante el plugin JaCoCo y sube el reporte como artefacto.
3. **`sonar-analysis` (SonarCloud Analysis):**  
   Inspecciona la calidad del código, deuda técnica, code smells, bugs y puntos débiles de seguridad (SAST), enviando los resultados al panel de SonarCloud.
4. **`build-and-deploy` (Build & Deploy to EC2):**  
   Compila el paquete ejecutable (`pedidosBackend-0.0.1-SNAPSHOT.jar`) con variables de entorno inyectadas, transfiere el archivo vía SCP a la carpeta `/home/ubuntu/micros` en EC2 y reinicia el servicio systemd `ms-pedidos`.

---

## 🌐 Endpoints y Acceso al Servicio

El microservicio se encuentra ejecutándose en segundo plano mediante `systemctl` en una instancia **AWS EC2** en el puerto **2020**.

### 📖 Documentación Swagger / OpenAPI
* **Swagger UI:** [http://184.192.139.90:2020/productos.html](http://184.192.139.90:2020/productos.html)
* **OpenAPI Docs (JSON):** [http://184.192.139.90:2020/api-docs](http://184.192.139.90:2020/api-docs)

### 📦 Endpoints Principales (API Productos)
* **Base URL:** `http://184.192.139.90:2020/api/v1/productos`

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/v1/productos` | Obtiene la lista de todos los productos |
| `GET` | `/api/v1/productos/{id}` | Obtiene un producto por su identificador |
| `POST` | `/api/v1/productos` | Registra un nuevo producto |
| `PUT` | `/api/v1/productos/{id}` | Actualiza la información de un producto |
| `DELETE` | `/api/v1/productos/{id}` | Elimina un producto del catálogo |

---

## 🎯 Objetivo Logrado

Implementación exitosa de un ciclo de vida de desarrollo de software moderno con enfoque **DevSecOps**, integrando:
* Control de versiones y flujo de trabajo colaborativo por ramas.
* Pruebas automatizadas y control de cobertura con JaCoCo.
* Escaneo continuo de vulnerabilidades en dependencias (SCA con Snyk).
* Inspección de calidad y seguridad estática de código (SAST con SonarCloud).
* Ocultación y protección de credenciales mediante GitHub Secrets.
* Despliegue continuo (CD) desatendido hacia infraestructura Cloud en AWS EC2.
