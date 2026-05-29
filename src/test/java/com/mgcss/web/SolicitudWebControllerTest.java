package com.mgcss.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.mgcss.domain.Solicitud;
import com.mgcss.service.SolicitudService;

@WebMvcTest(SolicitudWebController.class)
class SolicitudWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SolicitudService solicitudService;

    @Test
    void debe_listar_solicitudes_y_devolver_200() throws Exception {
        when(solicitudService.listarTodas()).thenReturn(List.of());

        mockMvc.perform(get("/solicitudes"))
                .andExpect(status().isOk())
                .andExpect(view().name("solicitudes"));
    }

    @Test
    void debe_crear_solicitud_y_redirigir() throws Exception {
        when(solicitudService.crearSolicitud(any())).thenReturn(new Solicitud("Descripción de prueba larga"));

        mockMvc.perform(post("/solicitudes/nueva")
                .param("descripcion", "Descripción de prueba larga"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/solicitudes"));
    }

    @Test
    void debe_cerrar_solicitud_y_redirigir() throws Exception {
        when(solicitudService.cerrarSolicitud(1L)).thenReturn(new Solicitud("Solicitud para cerrar prueba"));

        mockMvc.perform(post("/solicitudes/1/cerrar"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/solicitudes"));
    }

    @Test
    void debe_reabrir_solicitud_y_redirigir() throws Exception {
        when(solicitudService.reabrirSolicitud(1L)).thenReturn(new Solicitud("Solicitud para reabrir prueba"));

        mockMvc.perform(post("/solicitudes/1/reabrir"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/solicitudes"));
    }
}
