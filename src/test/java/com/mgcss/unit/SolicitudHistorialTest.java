package com.mgcss.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.mgcss.domain.EstadoTecnico;
import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Tecnico;

@Tag("unit")
class SolicitudHistorialTest {

    @Test
    void debe_registrar_estado_inicial_abierta_al_crear() {
        Solicitud s = new Solicitud("Descripción de prueba larga válida");

        List<String> historial = s.getHistorial();

        assertEquals(1, historial.size());
        assertTrue(historial.get(0).contains("ABIERTA"));
    }

    @Test
    void debe_registrar_todas_las_transiciones_en_orden_exacto() {
        Solicitud s = new Solicitud("Reparación de servidor de correo principal");
        Tecnico t = new Tecnico(EstadoTecnico.ACTIVO);

        s.asignarTecnico(t);  // no genera entrada en historial
        s.iniciarTrabajo();   // ABIERTA → EN_PROCESO
        s.cerrar();           // EN_PROCESO → CERRADA
        s.reabrir();          // CERRADA → EN_PROCESO

        List<String> historial = s.getHistorial();

        assertEquals(4, historial.size());
        assertTrue(historial.get(0).contains("ABIERTA"));
        assertTrue(historial.get(1).contains("EN_PROCESO"));
        assertTrue(historial.get(2).contains("CERRADA"));
        assertTrue(historial.get(3).contains("EN_PROCESO"));
    }

    @Test
    void reabrir_desde_estado_no_cerrada_no_debe_modificar_historial() {
        Solicitud s = new Solicitud("Revisión de firewall perimetral de red");
        int tamañoAntes = s.getHistorial().size();

        assertThrows(IllegalStateException.class, s::reabrir);

        assertEquals(tamañoAntes, s.getHistorial().size());
    }

    @Test
    void getHistorial_debe_devolver_copia_defensiva_no_la_referencia() {
        Solicitud s = new Solicitud("Mantenimiento de servidor de base de datos");

        List<String> copia = s.getHistorial();
        copia.add("entrada intrusa externa");

        assertEquals(1, s.getHistorial().size());
    }

    @Test
    void asignar_tecnico_no_debe_generar_entrada_en_historial() {
        Solicitud s = new Solicitud("Configuración de VLAN en switch central");
        Tecnico t = new Tecnico(EstadoTecnico.ACTIVO);

        s.asignarTecnico(t);

        assertEquals(1, s.getHistorial().size());
    }
}
