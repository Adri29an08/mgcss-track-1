package com.mgcss.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.mgcss.domain.EstadoTecnico;
import com.mgcss.domain.Solicitud;
import com.mgcss.domain.SolicitudRepository;
import com.mgcss.domain.Tecnico;
import com.mgcss.domain.TecnicoRepository;

@DataJpaTest
@Tag("integration")
class SolicitudRepositoryTest {

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Test
    void debeGuardarYRecuperarSolicitud() {
        Solicitud solicitud = new Solicitud("Error en el servidor de correo");

        Solicitud guardada = solicitudRepository.save(solicitud);

        Solicitud recuperada = solicitudRepository.findById(guardada.getId()).orElse(null);

        assertNotNull(recuperada);
        assertEquals("Error en el servidor de correo", recuperada.getDescripcion());
        assertNotNull(recuperada.getId());
    }

    @Test
    void debe_persistir_el_historial_de_cambios_de_estado() {
        // Arrange: persistir técnico primero por la FK
        Tecnico tecnico = tecnicoRepository.save(new Tecnico(EstadoTecnico.ACTIVO));

        Solicitud s = new Solicitud("Revisión de infraestructura de red local");
        s.asignarTecnico(tecnico);
        s.iniciarTrabajo();
        s.cerrar();

        Solicitud guardada = solicitudRepository.save(s);
        solicitudRepository.flush(); // forzar escritura en H2

        // Act: recargar desde BD (limpia el contexto de persistencia)
        Solicitud recuperada = solicitudRepository.findById(guardada.getId()).orElseThrow();

        // Assert: el historial debe sobrevivir al ciclo guardar/recuperar
        List<String> historial = recuperada.getHistorial();

        assertEquals(3, historial.size());
        assertTrue(historial.get(0).contains("ABIERTA"));
        assertTrue(historial.get(1).contains("EN_PROCESO"));
        assertTrue(historial.get(2).contains("CERRADA"));
    }
}