package com.mgcss.integration;

import com.mgcss.domain.*;
import com.mgcss.infrastructure.JpaSolicitudRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest // Levanta solo JPA y H2 para que sea rápido
class SolicitudPersistenceTest {

    @Autowired
    private JpaSolicitudRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void debe_persistir_el_historial_de_estados_correctamente() {
        //Crear y guardar solicitud inicial
        Solicitud s = new Solicitud("Reparación de servidor crítico");
        s = repository.save(s);

        //Provocar cambios de estado
        // Necesitamos un técnico para iniciar
        Tecnico t = new Tecnico(EstadoTecnico.ACTIVO);
        entityManager.persist(t); // Guardamos el técnico primero
        
        s.asignarTecnico(t);
        s.iniciarTrabajo(); // Cambia a EN_PROCESO
        s.cerrar();         // Cambia a CERRADA
        s.reabrir();        // Cambia a EN_PROCESO
        
        repository.save(s);
        
        //Forzar a que se escriba en DB
        entityManager.flush();
        entityManager.clear();

        // Recuperar de la base de datos
        Solicitud recuperada = repository.findById(s.getId()).orElseThrow();

        // Debería tener 4 entradas: ABIERTA, EN_PROCESO, CERRADA, EN_PROCESO
        assertEquals(4, recuperada.getHistorial().size());
        assertTrue(recuperada.getHistorial().get(0).contains("ABIERTA"));
        assertTrue(recuperada.getHistorial().get(3).contains("EN_PROCESO"));
    }
}
