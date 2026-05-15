package com.mgcss.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

// IMPORTANTE: Asegúrate de que esta ruta coincida con donde tienes el controlador
import com.mgcss.api.SolicitudController; 
import com.mgcss.domain.Solicitud;
import com.mgcss.service.SolicitudService;

@WebMvcTest(SolicitudController.class) // Fase 3.1: Carga solo la capa web
class SolicitudControllerTest {

    @Autowired
    private MockMvc mockMvc; 

    @MockBean // Fase 3.1: Mockeamos el servicio 
    private SolicitudService solicitudService;

    @Test
    void debe_crear_solicitud_y_devolver_200_ok() throws Exception {
        // 1. Arrange: Simulamos comportamiento del servicio
        Solicitud s = new Solicitud("Descripción de prueba de más de diez");
        
        // Corregido: Usamos el nombre real del método del servicio
        when(solicitudService.crearSolicitud(any())).thenReturn(s);

        // 2. Act & Assert: Verificar HTTP 200 y JSON
        mockMvc.perform(post("/api/solicitudes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"descripcion\": \"Descripción de prueba de más de diez\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ABIERTA"));
    }

    @Test
    void debe_devolver_400_si_la_descripcion_es_corta() throws Exception {
        // Fase 3.2: Manejo de errores [cite: 101]
        mockMvc.perform(post("/api/solicitudes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"descripcion\": \"corta\"}"))
                .andExpect(status().isBadRequest());
    }
}