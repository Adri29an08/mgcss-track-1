# Evidencias de Casos de Uso - Sesión 10

Este documento detalla las pruebas realizadas mediante Swagger UI para verificar el correcto funcionamiento de la API REST y las reglas de negocio del dominio.

## Caso 1: Creación de Solicitudes
* **Acción**: `POST /api/solicitudes`
* **Descripción**: Se han registrado dos incidencias de prueba.
* **Resultado**: El sistema ha generado los **IDs 1 y 2** con estado inicial `ABIERTA`. Se confirma que el historial registra correctamente el evento de creación.

## Caso 2: Validación de Reglas de Negocio (Transición Prohibida)
* **Acción**: `PUT /api/solicitudes/1/cerrar`
* **Descripción**: Se intenta cerrar la solicitud con ID 1 estando en estado `ABIERTA`.
* **Resultado esperado**: El sistema devuelve un **error (500 Internal Server Error)** con el mensaje `IllegalStateException`. 
* **Conclusión**: Esto demuestra que la API respeta las reglas del dominio: una solicitud no se puede cerrar si no ha sido iniciada previamente (`EN_PROCESO`).

## Caso 3: Listado Global de Solicitudes
* **Acción**: `GET /api/solicitudes`
* **Resultado**: Se obtiene un JSON con la lista completa de solicitudes (IDs 1 y 2). 
* **Observación**: Se verifica que los DTOs filtran correctamente la información y muestran el historial de estados de cada una.

## Caso 4: Reapertura de Solicitud (Gestión del Cambio)
* **Acción**: `PATCH /api/solicitudes/{id}/reabrir`
* **Descripción**: Probar la funcionalidad de la Sesión 9 expuesta en la API.
* **Resultado**: Al ejecutarlo sobre una solicitud CERRADA, el estado vuelve a `EN_PROCESO`, permitiendo su gestión de nuevo.