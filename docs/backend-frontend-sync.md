# Sincronización Backend <-> Frontend (Android)

Este documento resume los endpoints públicos del backend, los DTOs esperados (request/response), reglas de validación y recomendaciones para integrar desde Android (Retrofit/Gson/Moshi). Entregar esto al equipo de frontend para mantener ambos lados sincronizados.

---

Base URL (ejemplo):

- `http://{HOST}:{PORT}/api`

---

## Autenticación

- Endpoint: `POST /api/auth/login`
  - Request: `LoginDTO` { username, password }
  - Response: JSON con campos `success`, `message`, `usuario`, `token`.
  - Uso en Android: incluir header `Authorization: Bearer {token}` en requests autenticados.

- Endpoint: `POST /api/auth/register`
  - Request: `RegisterDTO`
  - Response: objeto con `success`, `message` y `usuario` (UsuarioDTO)

### Formatos y convenciones
- Long / Integer / BigDecimal -> number
- String -> string
- Boolean -> boolean
- LocalDate / LocalDateTime -> ISO-8601 string (p.ej. `2025-10-19T15:30:00`)
- Todos los endpoints devuelven JSON
- Validaciones declaradas vía `jakarta.validation`. Mostrar errores cuando backend responda 400 con mensaje

---

## Endpoints principales

### 1) Autenticación
- POST `/api/auth/login`
  - Request: `LoginDTO`
    ```json
    {
      "username": "string",
      "password": "string"
    }
    ```
  - Response 200 (ejemplo):
    ```json
    {
      "success": true,
      "message": "Login exitoso",
      "usuario": { /* UsuarioDTO */ },
      "token": "temporal-token-1"
    }
    ```

- POST `/api/auth/register`
  - Request: `RegisterDTO`
    ```json
    {
      "username":"juan",
      "email":"juan@ejemplo.com",
      "password":"123456",
      "nombre":"Juan",
      "apellido":"Perez",
      "telefono":"123456789",
      "tipoUsuario":"ADMIN"
    }
    ```
  - Response 201: objeto con `success`, `message` y `usuario` (UsuarioDTO)

### 2) Clientes
- GET `/api/clientes` -> lista de `ClienteDTO`
- GET `/api/clientes/activos` -> lista de `ClienteDTO` activos
- GET `/api/clientes/{id}` -> `ClienteDTO` o 404
- GET `/api/clientes/documento/{documento}` -> `ClienteDTO` o 404
- GET `/api/clientes/buscar?q={term}` -> lista de `ClienteDTO`
- POST `/api/clientes` -> Crear `ClienteDTO` (Request body: ClienteDTO sin id)
- PUT `/api/clientes/{id}` -> Actualizar ClienteDTO
- DELETE `/api/clientes/{id}` -> Eliminación lógica (desactiva)
- PATCH `/api/clientes/{id}/activar` -> Activa cliente
- GET `/api/clientes/estadisticas` -> `{ totalClientesActivos: number }`

ClienteDTO (campos relevantes)
- id: number
- nombre: string (required)
- apellido: string (required)
- documento: string
- telefono: string
- email: string
- direccion: string
- activo: boolean
- totalMascotas: number

### 3) Mascotas
- GET `/api/mascotas` -> lista de `MascotaDTO`
- GET `/api/mascotas/activas` -> lista activas
- GET `/api/mascotas/{id}` -> `MascotaDTO`
- GET `/api/mascotas/cliente/{clienteId}` -> lista por cliente
- GET `/api/mascotas/buscar?q={term}` -> lista
- GET `/api/mascotas/especies` -> lista de strings
- GET `/api/mascotas/razas/{especie}` -> lista de strings
- POST `/api/mascotas` -> Crear `MascotaDTO`
- PUT `/api/mascotas/{id}` -> Actualizar
- DELETE `/api/mascotas/{id}` -> Eliminar lógico
- PATCH `/api/mascotas/{id}/activar` -> Activar mascota
- GET `/api/mascotas/estadisticas` -> `{ totalMascotasActivas: number }`

MascotaDTO (campos relevantes)
- id: number
- nombre: string (required)
- especie: string (required)
- raza: string
- sexo: string
- fechaNacimiento: string (ISO-8601 date, p.ej. "2020-05-01")
- peso: number (decimal)
- color: string
- observaciones: string
- activo: boolean
- clienteId: number (required)
- clienteNombre: string
- edad: number

### 4) Citas (si está disponible)
- POST `/api/citas` (si existe en backend)

CitaDTO (campos relevantes)
- id: number
- fechaHora: string (ISO-8601 datetime)
- estado: string
- motivo: string
- observaciones: string
- costo: number
- clienteId: number (required)
- mascotaId: number (required)
- veterinarioId: number
- clienteNombre, mascotaNombre, veterinarioNombre

---

## Errores y códigos HTTP comunes
- 200 OK (operación exitosa)
- 201 Created (recurso creado)
- 400 Bad Request (validación falló) — el cuerpo puede contener `message` con detalle
- 401 Unauthorized (credenciales inválidas o token ausente)
- 404 Not Found (recurso no encontrado)
- 500 Internal Server Error (problema servidor)

---

## Recomendaciones para Android (Retrofit)
- Usar Retrofit + OkHttp + Moshi o Gson.
- Registrar un interceptor que añada `Authorization: Bearer {token}` cuando esté disponible.
- Serializadores: añadir soporte para java.time (LocalDate/LocalDateTime). Con Moshi usar adapters (ThreeTenBP o java.time via kotlinx.serialization) o con Gson usar `GsonBuilder.registerTypeAdapter` para LocalDateTime.
- Manejar respuestas con envoltorio (wrapper) que contienen `success` y `message`.

Ejemplo rápido de Interceptor (Kotlin pseudo):

```
// Interceptor añade Authorization si token existe
val authInterceptor = Interceptor { chain ->
  val req = chain.request().newBuilder()
  token?.let { req.addHeader("Authorization", "Bearer $it") }
  chain.proceed(req.build())
}
```

---

## Notas sobre versiones y el upgrade a Spring 3.5 / Spring Security 3.5
- Estado actual: Spring Boot 3.4.x en el backend.
- Recomendación: si se va a actualizar a Spring Boot 3.5.x, verificar compatibilidad de Java (17/21) y ejecutar pruebas.

---

## Roles y compatibilidad
- El backend expone roles como strings en mayúsculas: `CLIENTE`, `VETERINARIO`, `ADMIN`.
- Al enviar peticiones de registro o actualización, usar los nombres enum en mayúsculas.

---

Fecha: 2025-10-20
Autor: Equipo Backend
 
---

## Swagger / OpenAPI (URLs)

- Swagger UI (interfaz web):
  - Common path: `/swagger-ui/index.html`
  - Full example (host running on localhost, port 8081):
    - `http://localhost:8081/swagger-ui/index.html`
  - If the app is deployed under a context path or different port, replace host/port accordingly.

- OpenAPI JSON (machine-readable):
  - Common path: `/v3/api-docs`
  - Example: `http://localhost:8081/v3/api-docs`

> Tip: From an Android emulator use `http://10.0.2.2:{PORT}` in Retrofit to reach the host machine's localhost (e.g. `http://10.0.2.2:8081/api`).

## Troubleshooting common integration issues

- Emulator cannot reach backend (connection refused / failed to connect to localhost)
  - Cause: `localhost` inside the emulator points to the emulator itself, not the host machine.
  - Fix: Use `10.0.2.2` as the host when the backend runs on your development machine. Example base URL for Retrofit: `http://10.0.2.2:8081/api/`.

- Registration / Save returns HTTP 400 with message about enum values:
  - Symptom (server response):
    {
      "success": false,
      "message": "No enum constant com.veterinaria.model.Usuario.TipoUsuario.ADMIN"
    }
  - Cause: The backend expects specific enum names for `tipoUsuario` (case-sensitive). The client sent `"ADMIN"` but the backend's enum may be defined differently (for example `ADMINISTRADOR`) or expects a different casing/namespace.
  - Recommended fixes:
    1. Confirm the exact enum values on the backend (ask the backend team or inspect the OpenAPI docs returned by `/v3/api-docs`). The docs will show the allowed values for `tipoUsuario`.
    2. Update the client to send the exact enum string expected by the backend. Example mapping in Kotlin before sending the DTO:
       - map("ADMIN") -> "ADMIN" (or "ADMINISTRADOR" depending on backend)
    3. Alternatively, if you can change the backend, make the enum tolerant (e.g., implement a case-insensitive converter or accept alternate names) and return friendly validation errors.

- Serializing LocalDate / LocalDateTime incorrectly
  - Cause: Missing adapters for java.time types in Gson/Moshi causes parse/format mismatches and 400s.
  - Fix: Register adapters (Gson `GsonBuilder.registerTypeAdapter`) or use Moshi with the java.time adapters.

## Quick Retrofit example (base + auth interceptor)

```kotlin
// Base URL for emulator pointing to host's port 8081
const val BASE_URL_DEV = "http://10.0.2.2:8081/api/"

val authInterceptor = Interceptor { chain ->
  val reqBuilder = chain.request().newBuilder()
  val token = tokenProvider.get() // implement tokenProvider to read from DataStore
  token?.let { reqBuilder.addHeader("Authorization", "Bearer $it") }
  chain.proceed(reqBuilder.build())
}

val client = OkHttpClient.Builder()
  .addInterceptor(authInterceptor)
  .build()

val retrofit = Retrofit.Builder()
  .baseUrl(BASE_URL_DEV)
  .client(client)
  .addConverterFactory(GsonConverterFactory.create())
  .build()
```

---

## Short checklist for a successful integration test

- Start backend and verify Swagger UI is reachable from your desktop browser at `/swagger-ui/index.html`.
- From the emulator, open the browser and try `http://10.0.2.2:{PORT}/swagger-ui/index.html` to confirm reachability.
- If registration returns 400 about `tipoUsuario`, inspect `/v3/api-docs` or ask backend for exact enum values and update mapping on the client.
- Enable verbose OkHttp logs in debug builds to see request/response bodies and headers when troubleshooting.

## Notas agregadas desde el repositorio (confirmadas)

- El cliente Android en este repositorio todavía usa el campo `tipoUsuario` (String) en sus modelos y pantallas. Además de los valores "CLIENTE", "VETERINARIO" y "ADMIN" que aparecen en la documentación, la app Android contiene también la opción "RECEPCIONISTA" en su selección de tipo de usuario (ver `app/src/main/java/com/example/veterinaria/data/model/CommonModels.kt` y `app/src/main/java/com/example/veterinaria/screen/auth/RegisterScreen.kt`).
- En la app Android el base URL para desarrollo está en `app/src/main/java/com/example/veterinaria/util/Constants.kt` como:
  - `BASE_URL_DEV = "http://10.0.2.2:8081/api/"` (para el emulador)
  - `BASE_URL_LOCAL = "http://localhost:8081/api/"` (para pruebas desde host)
- Aunque en el backend se añadió recientemente el campo `roleId` en los DTOs (y se planeó una migración a una entidad `Roles`), en el código Android actual no hay soporte automático para `roleId` en los modelos ni en los formularios de registro. Por ahora el frontend no necesita enviar `roleId`; seguid enviando `tipoUsuario` como string.
- Confirmado: el backend añadió una migración (Flyway) que causó problemas en algunos entornos (dependencia de versión de Postgres). Esa migración fue retirada temporalmente para evitar fallos al iniciar. La opción de migrar a `roleId` sigue disponible y puede realizarse vía SQL manual o reintroduciendo Flyway con una versión compatible.

Si queréis que adapte el cliente Android para usar `roleId` (añadir el campo en los DTOs y formularios), puedo preparar los cambios y un plan de migración (SQL / Flyway) bajo coordinación.

