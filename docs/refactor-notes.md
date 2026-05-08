# Informe de Refactorización - Sesión 8

## 1. Análisis Pre-Refactorización
Este análisis identifica la deuda técnica y los problemas de calidad detectados mediante SonarCloud antes de proceder con las modificaciones del código.

### Problema 1: Alta complejidad lógica en la entidad de dominio
* **Problema identificado**: La clase `Solicitud` concentra una excesiva densidad de reglas de negocio y validaciones de estado en sus métodos principales, lo que dificulta la legibilidad del flujo de control.
* **Métrica asociada**: Complejidad Ciclomática con un valor de **19**.
* **Riesgo potencial**: Existe un alto riesgo de introducir errores de regresión al realizar cambios. []Dado que en la Sesión 9 se requiere implementar la reapertura de solicitudes, mantener esta complejidad dificultará la extensión segura del comportamiento y aumentará la fragilidad del sistema.

### Problema 2: Code Smells en la suite de pruebas unitarias
* **Problema identificado**: Se han detectado malas prácticas en `SolicitudTest` relacionadas con el uso de expresiones lambda redundantes en las aserciones de excepciones y la ausencia de validación de los mensajes de error.
* **Métrica asociada**: Maintainability Rating (Code Smells).
* **Riesgo potencial**: Los tests no son lo suficientemente precisos para garantizar que una excepción se lanza por el motivo de negocio correcto. Esto reduce la efectividad de la red de seguridad necesaria para las fases de mantenimiento y evolución continua del software.



## 2. Documentación de Resultados (Post-Sesión 8)

Tras la aplicación de técnicas de refactorización profesional, se detallan a continuación los resultados obtenidos para alcanzar los objetivos técnicos de la sesión.

---

### 1. Métricas que han mejorado

*   **Code Smells:** Reducción de **2 a 0**. Se han eliminado todas las incidencias de estilo y malas prácticas detectadas inicialmente en la clase de pruebas.
*   **Complejidad por Método:** Se ha eliminado el "método monstruo" (complejidad ciclomática de **19**). Aunque la complejidad total de la clase aumentó ligeramente por la fragmentación, la complejidad individual de cada método es ahora **óptima (valores de 1 o 2)**, simplificando drásticamente el mantenimiento.
*   **Deuda Técnica:** Mejora significativa del *Technical Debt Ratio* y el *Maintainability Rating* general del proyecto.

---

### 2. Técnicas de refactorización aplicadas

*   **Extract Method (Extraer Método):** Aplicado en `Solicitud.java` para segregar las validaciones de dominio en métodos privados atómicos:
    *   `validarPresencia`
    *   `validarLongitudMinima`
    *   `asegurarQueSePuedeModificar`
*   **Replace Lambda with Method Reference:** Sustitución de lambdas por referencias a métodos (ej. `s::cerrar`, `s::iniciarTrabajo`) en las aserciones `assertThrows`.
*   **Rename for Clarity:** Mejora del nombrado de métodos internos para que el código sea **auto-explicativo**.

---

### 3. Beneficios para el mantenimiento futuro

1.  **Extensibilidad:** La estructura modular facilita la implementación de los requisitos de la **Sesión 9** (reapertura de solicitudes e historial de estados) sin riesgo de romper la lógica de negocio existente.
2.  **Seguridad de Regresión:** Al reducir la complejidad individual, es más sencillo garantizar una cobertura de tests efectiva y mantener el *Quality Gate* en verde.
3.  **Legibilidad:** El código sigue estándares profesionales, reduciendo el esfuerzo cognitivo necesario para comprender las reglas de integridad del dominio.