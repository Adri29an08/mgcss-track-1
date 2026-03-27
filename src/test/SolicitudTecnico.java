package com.mgcss.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class TecnicoTest {

    @Test
    void debe_crearse_con_el_estado_correcto() {
        Tecnico t = new Tecnico(EstadoTecnico.ACTIVO);
        assertEquals(EstadoTecnico.ACTIVO, t.getEstado()); // [cite: 761]
    }

    @Test
    void debe_permitir_cambiar_de_estado_manualmente() {
        Tecnico t = new Tecnico(EstadoTecnico.ACTIVO);
        
        t.desactivar();
        assertEquals(EstadoTecnico.INACTIVO, t.getEstado());
        
        t.activar();
        assertEquals(EstadoTecnico.ACTIVO, t.getEstado());
    }

    @Test
    void debe_guardar_los_datos_basicos() {
        Tecnico t = new Tecnico(1L, "Adrian", EstadoTecnico.ACTIVO);
        assertAll(
            () -> assertEquals(1L, t.getId()),
            () -> assertEquals("Adrian", t.getNombre())
        );
    }
}