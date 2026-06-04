package com.mgcss.unit;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
        assertThrows(IllegalStateException.class, () -> s.asignarTecnico(inactivo));
    }

    @Test
    void no_debe_iniciar_trabajo_sin_tecnico() {
        Solicitud s = new Solicitud("Configuración de VLAN interna");
        assertThrows(IllegalStateException.class, s::iniciarTrabajo);
    }

    @Test
    void no_debe_permitir_descripcion_demasiado_corta() {
        assertThrows(IllegalArgumentException.class, () -> new Solicitud("Corta"));
    }

    @Test
    @Tag("unit")
    void debe_permitir_reapertura_y_registrar_historial_completo() {
        Solicitud s = new Solicitud("Reparación de terminal punto de venta");
        
        s.asignarTecnico(new Tecnico(EstadoTecnico.ACTIVO));
        s.iniciarTrabajo(); 
        s.cerrar(); 
        
        s.reabrir();

        assertEquals(EstadoSolicitud.EN_PROCESO, s.getEstado(), "La solicitud debería estar otra vez EN_PROCESO");
        
        List<String> historial = s.getHistorial();
        assertTrue(historial.size() >= 4, "El historial debería tener registrados todos los pasos");
        assertTrue(historial.get(historial.size() - 1).contains("EN_PROCESO"), "El último cambio debe ser la reapertura");
    }

    private void cambiarFechaPrivada(Solicitud s, String nombreCampo, LocalDateTime nuevaFecha) throws Exception {
        Field campo = Solicitud.class.getDeclaredField(nombreCampo);
        campo.setAccessible(true);
        campo.set(s, nuevaFecha);
    }

    @Test
    void debe_cumplir_sla_cuando_es_nueva() {
        Solicitud s = new Solicitud("Descripción válida de prueba");
        assertFalse(s.isSlaIncumplido(), "Una solicitud recién creada no debería romper el SLA");
    }

    @Test
    void debe_incumplir_sla_cuando_pasan_4_dias_abierta() throws Exception {
        Solicitud s = new Solicitud("Descripción válida de prueba");
        
        cambiarFechaPrivada(s, "fechaCreacion", LocalDateTime.of(2024, 1, 1, 10, 0));
        
        assertTrue(s.isSlaIncumplido(), "Debería romper el SLA si lleva 4 días abierta");
    }

    @Test
    void debe_cumplir_sla_cuando_se_cierra_en_2_dias() throws Exception {
        Solicitud s = new Solicitud("Descripción válida de prueba");
        
        cambiarFechaPrivada(s, "fechaCreacion", LocalDateTime.of(2024, 1, 1, 10, 0));
        
        s.asignarTecnico(new Tecnico(EstadoTecnico.ACTIVO));
        s.iniciarTrabajo();
        s.cerrar();
        
        cambiarFechaPrivada(s, "fechaCierre", LocalDateTime.of(2024, 1, 3, 10, 0));
        
        assertFalse(s.isSlaIncumplido(), "No rompe el SLA porque se resolvió en 1 día");
    }

    @Test
    void debe_incumplir_sla_cuando_se_cierra_tarde() throws Exception {
        Solicitud s = new Solicitud("Descripción válida de prueba");
        
        cambiarFechaPrivada(s, "fechaCreacion", LocalDateTime.of(2024, 1, 1, 10, 0));
        
        s.asignarTecnico(new Tecnico(EstadoTecnico.ACTIVO));
        s.iniciarTrabajo();
        s.cerrar();
        
        cambiarFechaPrivada(s, "fechaCierre", LocalDateTime.of(2024, 1, 10, 10, 0));
        
        assertTrue(s.isSlaIncumplido(), "Rompe el SLA porque tardaron 9 días en cerrarla");
    }
}