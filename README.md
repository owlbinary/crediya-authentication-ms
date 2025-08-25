# Crediya Authentication Microservice

Microservicio de autenticación desarrollado con arquitectura hexagonal y programación reactiva para el sistema Crediya.

## 🏗️ Arquitectura

Este proyecto sigue los principios de **Arquitectura Hexagonal (Clean Architecture)** usando el [Plugin de Bancolombia](https://bancolombia.github.io/scaffold-clean-architecture) con las siguientes capas:

- **Domain**: Entidades de negocio y reglas del dominio
- **Use Cases**: Casos de uso de la aplicación
- **Infrastructure**: Adaptadores para entrada y salida
- **Applications**: Configuración y punto de entrada de la aplicación

![Clean Architecture](https://miro.medium.com/max/1400/1*ZdlHz8B0-qu9Y-QO3AXR_w.png)


## 🚀 Tecnologías

- **Java 21**
- **Spring Boot 3.x**
- **Spring WebFlux** (Programación Reactiva)
- **Spring Security** (Autenticación y autorización)
- **Spring Data R2DBC** (Base de datos reactiva)
- **PostgreSQL**
- **JWT** (JSON Web Tokens)
- **Gradle** (Gestión de dependencias)
- **JUnit 5** (Testing)
- **Mockito** (Mocking)
- **Jacoco** (Cobertura de código)
- **PiTest** (Mutation testing)
- **OpenAPI 3/Swagger** (Documentación de API)

## 🔗 Endpoints API

### Gestión de Usuarios

#### POST /api/v1/usuarios
Registra un nuevo usuario (requiere rol ADMIN o ASESOR).

**Request:**
```json
{
  "nombre": "User",
  "apellido": "Test",
  "email": "user@ejemplo.com",
  "password": "password123",
  "documento_identidad": "1234567890",
  "telefono": "3001234567",
  "direccion": "Calle 123 #45-67",
  "id_rol": 3,
  "salario_base": 2500000.00
}
```

**Response (201):**
```json
{
  "idUsuario": 1,
  "nombre": "User",
  "apellido": "Test",
  "email": "user@ejemplo.com",
  "documentoIdentidad": "1234567890",
  "telefono": "3001234567",
  "idRol": 3,
  "salarioBase": 2500000.00,
  "fechaCreacion": "2025-08-24T10:30:00"
}
```

## 🛠️ Instalación y Configuración

### Prerrequisitos
- Java 21
- PostgreSQL 13+
- Gradle 8+

### Ejecución

```bash
# Clonar repositorio
git clone <repository-url>
cd crediya-authentication-ms

# Ejecutar aplicación
./gradlew bootRun

# La aplicación estará disponible en http://localhost:8080
```

## 🧪 Testing

### Ejecutar Tests
```bash
# Todos los tests
./gradlew test

# Tests con reporte de cobertura
./gradlew test jacocoTestReport

# Mutation testing
./gradlew pitest

# Reporte combinado
./gradlew jacocoMergedReport
```

### Cobertura de Código
- **Objetivo**: 90% de cobertura de líneas
- **Herramientas**: Jacoco + PiTest
- **Reportes**: `build/reports/jacocoHtml/index.html`

## 📚 Documentación API

La documentación completa de la API está disponible en:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## 🔒 Seguridad

- **Headers de Seguridad**: CSP, HSTS, X-Content-Type-Options
- **Validación**: Bean Validation en DTOs

## 🏗️ Estructura del Proyecto

```
crediya-authentication-ms/
├── applications/
│   └── app-service/          # Configuración principal
├── domain/
│   ├── model/                # Entidades de dominio
│   └── usecase/              # Casos de uso
├── infrastructure/
│   ├── driven-adapters/
│   │   └── r2dbc-postgresql/ # Adaptador de BD
│   └── entry-points/
│       └── reactive-web/     # Controladores REST
└── deployment/               # Configuración de despliegue
```

## 📈 Monitoreo

- **Health Check**: `/actuator/health`
- **Métricas**: `/actuator/metrics`
- **Info**: `/actuator/info`

**Artículo de referencia**: [Clean Architecture — Aislando los detalles](https://medium.com/bancolombia-tech/clean-architecture-aislando-los-detalles-4f9530f35d7a)
