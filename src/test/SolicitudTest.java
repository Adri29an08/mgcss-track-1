package com.mgcss.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class SolicitudTest {

    @Test
    void no_debe_permitir_cerrar_si_no_esta_en_proceso() {
        Solicitud s = new Solicitud("Reparación de switch principal");
        // Está ABIERTA, debe fallar al intentar cerrar
        assertThrows(IllegalStateException.class, () -> s.cerrar());
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
        assertThrows(IllegalStateException.class, () -> s.iniciarTrabajo());
    }

    @Test
    void no_debe_permitir_descripcion_demasiado_corta() {
        // Regla de integridad de datos 
        assertThrows(IllegalArgumentException.class, () -> new Solicitud("Corta"));
    }
}