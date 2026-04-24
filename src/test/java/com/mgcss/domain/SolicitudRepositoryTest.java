package com.mgcss.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest // Levanta solo JPA y la base de datos H2
class SolicitudRepositoryTest {

    @Autowired
    private SolicitudRepository solicitudRepository;


    @Test
    void debeGuardarYRecuperarSolicitud() {
        // 1. Arrange: Crear una entidad real (NADA de mocks aquí) 
        Solicitud solicitud = new Solicitud("Error en el servidor de correo");

        // 2. Act: Guardar en la base de datos H2 
        Solicitud guardada = solicitudRepository.save(solicitud);

        // 3. Assert: Recuperar y verificar integridad 
        Solicitud recuperada = solicitudRepository.findById(guardada.getId()).orElse(null);
        
        assertNotNull(recuperada);
        assertEquals("Error en el servidor de correo", recuperada.getDescripcion());
        assertNotNull(recuperada.getId()); // La BD debe haber generado el ID
    }
}