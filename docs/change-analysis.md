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