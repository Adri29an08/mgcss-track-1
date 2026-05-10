# Análisis de Impacto - Solicitud de Cambio (CR-001)

### 1. Métodos del dominio afectados
* **cerrar()**: Deja de ser la transición final absoluta
* **reabrir()**: Nuevo método para transicionar de CERRADA a EN_PROCESO
* **Métodos de cambio de estado**: Todos deben registrar ahora el movimiento en el historial

### 2. Cambios en las reglas de negocio
* Se rompe la inmutabilidad total del estado **CERRADA**.Ahora permite una transición controlada hacia **EN_PROCESO**

### 3. Tests afectados
* Se deben modificar los tests que verificaban que una solicitud CERRADA no admitía más cambios, ya que ahora el método `reabrir()` es una excepción válida

### 4. Extensión del modelo y persistencia
* Se añade una colección interna para el historial de estados
* Se utilizará un mapeo JPA de tipo `@ElementCollection` para persistir los cambios sin necesidad de una entidad compleja separada

### 5. Decisión de diseño del Histórico

Se evaluaron tres opciones antes de elegir la implementación:

**Opción A – Lista interna de Strings con `@ElementCollection` (ELEGIDA)**
- Pros: Máxima simplicidad, sin entidad adicional, mapeado directo con `@ElementCollection`, tabla `solicitud_historial` generada automáticamente
- Contras: Formato string dificulta consultas estructuradas en el futuro

**Opción B – Entidad separada `EstadoChange`**
- Pros: Permite consultas complejas sobre el historial, tipado fuerte, fácilmente extensible
- Contras: Añade complejidad innecesaria para el alcance actual (nueva entidad, nuevo repositorio, relación `@OneToMany`)

**Opción C – Value Object interno `@Embeddable`**
- Pros: Semántica clara en el dominio, evita strings libres
- Contras: Requiere mapeo `@Embeddable` y `@ElementCollection` con `@AttributeOverrides`, complejidad media sin beneficio adicional frente a la Opción A en este contexto

**Decisión:** Se elige la **Opción A** por ser suficiente para los requisitos actuales (trazabilidad de cambios de estado) y mantener la entidad `Solicitud` sin dependencias adicionales. Cada entrada registra el estado alcanzado y el timestamp de la transición. Si en el futuro se necesitan consultas avanzadas sobre el historial, se migrará a la Opción B sin cambiar la interfaz pública de `Solicitud`.


### 4. Impacto en persistencia (Fase 4)

Se ha confirmado que el historial de estados debe ser persistente para garantizar la trazabilidad completa de la solicitud, incluso tras el reinicio del sistema. Los pasos realizados han sido: 

 **Añadir mapeo JPA:** Se ha implementado el mapeo mediante la anotación @ElementCollection en la entidad Solicitud. Esto genera automáticamente una tabla denominada solicitud_historial vinculada por la clave ajena solicitud_id.  
 
 **Crear test de integración específico:** Se ha desarrollado una prueba de integración (usando @DataJpaTest) para validar el ciclo de vida completo de la persistencia del historial. 
 
 **Verificar almacenamiento correcto:** El test de integración confirma que, tras realizar múltiples transiciones de estado (incluida la nueva lógica de reapertura), los registros se almacenan y recuperan correctamente en el orden cronológico esperado.