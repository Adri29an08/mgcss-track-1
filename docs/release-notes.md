# Release Notes - Justificación de Versión

**Próxima versión a publicar:** `v1.0.1`

**Justificación del Versionado Semántico:**
Tras analizar el historial de commits partiendo de la versión base `v1.0.0` (la cual ya incluye la API REST, Swagger y la containerización base), se determina que la próxima release será la **1.0.1** (incremento de la versión PATCH).

Basándonos en las reglas de versionado semántico:
- **No es un MAJOR (2.0.0):** No hay cambios disruptivos en la arquitectura ni en el contrato de la API.
- **No es un MINOR (1.1.0):** No se han añadido nuevas funcionalidades al código fuente de la aplicación en esta iteración.
- **Es un PATCH (1.0.1):** Los cambios a integrar consisten exclusivamente en la automatización del flujo de despliegue continuo (Continuous Delivery) mediante GitHub Actions. Al ser únicamente mejoras en la infraestructura del repositorio y el pipeline (archivos `.yml` y `.md`) que no alteran el comportamiento del software, corresponde un incremento de parche.