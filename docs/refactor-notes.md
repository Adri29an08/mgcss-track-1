# Informe de Refactorización - Sesión 8

## 1. Análisis Pre-Refactorización
Este análisis identifica la deuda técnica y los problemas de calidad detectados mediante SonarCloud antes de proceder con las modificaciones del código.

### Problema 1: Alta complejidad lógica en la entidad de dominio
* [cite_start]**Problema identificado**: La clase `Solicitud` concentra una excesiva densidad de reglas de negocio y validaciones de estado en sus métodos principales, lo que dificulta la legibilidad del flujo de control.
* [cite_start]**Métrica asociada**: Complejidad Ciclomática con un valor de **19**.
* **Riesgo potencial**: Existe un alto riesgo de introducir errores de regresión al realizar cambios. [cite_start]Dado que en la Sesión 9 se requiere implementar la reapertura de solicitudes, mantener esta complejidad dificultará la extensión segura del comportamiento y aumentará la fragilidad del sistema.

### Problema 2: Code Smells en la suite de pruebas unitarias
* **Problema identificado**: Se han detectado malas prácticas en `SolicitudTest` relacionadas con el uso de expresiones lambda redundantes en las aserciones de excepciones y la ausencia de validación de los mensajes de error.
* [cite_start]**Métrica asociada**: Maintainability Rating (Code Smells).
* [cite_start]**Riesgo potencial**: Los tests no son lo suficientemente precisos para garantizar que una excepción se lanza por el motivo de negocio correcto. Esto reduce la efectividad de la red de seguridad necesaria para las fases de mantenimiento y evolución continua del software.