package com.mgcss.unit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.mgcss.domain.EstadoSolicitud;
import com.mgcss.domain.EstadoTecnico;
import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Tecnico;

@Tag("unit")
class SolicitudTest {

    @Test
    void no_debe_permitir_cerrar_si_no_esta_en_proceso() {
        Solicitud s = new Solicitud("Reparación de switch principal");
        // Está ABIERTA, debe fallar al intentar cerrar
        assertThrows(IllegalStateException.class, s::cerrar);
    }

    @Test
    void debe_permitir_cerrar_si_esta_en_proceso() {
        Solicitud s = new Solicitud("Reparación de switch principal");
        s.asignarTecnico(new Tecnico(EstadoTecnico.ACTIVO));
        s.iniciarTrabajo();
        s.cerrar();
        assertEquals(EstadoSolicitud.CERRADA, s.getEstado());
    }

    @Test
    void no_debe_asignar_tecnico_inactivo() {
        Solicitud s = new Solicitud("Revisión de firewall perimetral");
        Tecnico inactivo = new Tecnico(EstadoTecnico.INACTIVO);
        // Debe fallar según la regla de negocio
        assertThrows(IllegalStateException.class, () -> s.asignarTecnico(inactivo));
    }

    @Test
    void no_debe_iniciar_trabajo_sin_tecnico() {
        Solicitud s = new Solicitud("Configuración de VLAN interna");
        // Falla porque técnico es null 
        assertThrows(IllegalStateException.class, s::iniciarTrabajo);
    }

    @Test
    void no_debe_permitir_descripcion_demasiado_corta() {
        // Regla de integridad de datos 
        assertThrows(IllegalArgumentException.class, () -> new Solicitud("Corta"));
    }

    @Test
    @Tag("unit")
    void debe_permitir_reapertura_y_mantener_historial() {
        Solicitud s = new Solicitud("Reparación de servidor de correo");
        Tecnico t = new Tecnico(EstadoTecnico.ACTIVO);
        
        s.asignarTecnico(t);
        s.iniciarTrabajo();
        s.cerrar();
        
        // Acción de la Sesión 9
        s.reabrir();
        
        assertEquals(EstadoSolicitud.EN_PROCESO, s.getEstado());
        assertTrue(s.getHistorial().size() >= 4); // Abierta, En Proceso, Cerrada, Reabierta
    }

    @Test
    @Tag("unit")
    void debe_permitir_reapertura_y_registrar_historial_completo() {
        // 1. Crear (Estado: ABIERTA)
        Solicitud s = new Solicitud("Reparación de terminal punto de venta");
        
        // 2. Transiciones
        s.asignarTecnico(new Tecnico(EstadoTecnico.ACTIVO));
        s.iniciarTrabajo(); // Estado: EN_PROCESO
        s.cerrar();         // Estado: CERRADA
        
        // 3. El cambio de la Sesión 9: Reabrir
        s.reabrir();
        
        // Verificaciones
        assertEquals(EstadoSolicitud.EN_PROCESO, s.getEstado(), "La solicitud debería estar otra vez EN_PROCESO");
        
        // Verificar Historial (debe tener al menos 5 entradas: Creada, Asignada, Iniciada, Cerrada, Reabierta)
        List<String> historial = s.getHistorial();
        assertTrue(historial.size() >= 4, "El historial debería tener registrados todos los pasos");
        assertTrue(historial.get(historial.size() - 1).contains("EN_PROCESO"), "El último cambio debe ser la reapertura");
    }
}