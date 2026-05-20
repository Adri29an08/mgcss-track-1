package com.mgcss.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.mgcss.api.SolicitudController;
import com.mgcss.domain.EstadoSolicitud;
import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Tecnico;
import com.mgcss.service.SolicitudService;

@WebMvcTest(SolicitudController.class)
class SolicitudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SolicitudService solicitudService;

    @Test
    void debe_crear_solicitud_y_devolver_200_ok() throws Exception {
        Solicitud s = new Solicitud("Descripción de prueba de más de diez");
        when(solicitudService.crearSolicitud(any())).thenReturn(s);

        mockMvc.perform(post("/api/solicitudes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"descripcion\": \"Descripción de prueba de más de diez\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ABIERTA"));
    }

    @Test
    void debe_devolver_400_si_la_descripcion_es_corta() throws Exception {
        when(solicitudService.crearSolicitud("corta"))
                .thenThrow(new IllegalStateException("La descripción debe tener al menos 10 caracteres"));

        mockMvc.perform(post("/api/solicitudes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"descripcion\": \"corta\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void debe_consultar_solicitud_por_id_y_devolver_200() throws Exception {
        Solicitud s = new Solicitud("Incidencia de red en el servidor principal");
        when(solicitudService.buscarPorId(1L)).thenReturn(s);

        mockMvc.perform(get("/api/solicitudes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ABIERTA"));
    }

    @Test
    void debe_devolver_404_si_solicitud_no_existe() throws Exception {
        when(solicitudService.buscarPorId(99L))
                .thenThrow(new IllegalArgumentException("Solicitud no encontrada"));

        mockMvc.perform(get("/api/solicitudes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void debe_listar_todas_las_solicitudes() throws Exception {
        Solicitud s1 = new Solicitud("Primera incidencia de prueba");
        Solicitud s2 = new Solicitud("Segunda incidencia de prueba");
        when(solicitudService.listarTodas()).thenReturn(List.of(s1, s2));

        mockMvc.perform(get("/api/solicitudes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void debe_reabrir_solicitud_y_devolver_200() throws Exception {
        Solicitud s = new Solicitud("Incidencia reabierta para revisión");
        when(solicitudService.reabrirSolicitud(1L)).thenReturn(s);

        mockMvc.perform(patch("/api/solicitudes/1/reabrir"))
                .andExpect(status().isOk());
    }

    @Test
    void debe_cerrar_solicitud_y_devolver_200() throws Exception {
        Solicitud s = new Solicitud("Incidencia resuelta correctamente");
        when(solicitudService.cerrarSolicitud(1L)).thenReturn(s);

        mockMvc.perform(put("/api/solicitudes/1/cerrar"))
                .andExpect(status().isOk());
    }

    @Test
    void debe_devolver_400_al_cerrar_solicitud_en_estado_incorrecto() throws Exception {
        when(solicitudService.cerrarSolicitud(1L))
                .thenThrow(new IllegalStateException("Solo solicitudes en proceso pueden cerrarse"));

        mockMvc.perform(put("/api/solicitudes/1/cerrar"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void debe_asignar_tecnico_y_devolver_200() throws Exception {
        Solicitud s = new Solicitud("Incidencia con técnico asignado");
        when(solicitudService.buscarPorId(1L)).thenReturn(s);

        mockMvc.perform(patch("/api/solicitudes/1/asignar-tecnico/5"))
                .andExpect(status().isOk());
    }

    @Test
    void debe_incluir_tecnico_id_en_respuesta_cuando_tiene_tecnico_asignado() throws Exception {
        Tecnico tecnico = mock(Tecnico.class);
        when(tecnico.getId()).thenReturn(5L);

        Solicitud s = mock(Solicitud.class);
        when(s.getEstado()).thenReturn(EstadoSolicitud.EN_PROCESO);
        when(s.getDescripcion()).thenReturn("Incidencia con técnico asignado correctamente");
        when(s.getFechaCreacion()).thenReturn(null);
        when(s.getHistorial()).thenReturn(List.of());
        when(s.getTecnico()).thenReturn(tecnico);

        when(solicitudService.buscarPorId(1L)).thenReturn(s);

        mockMvc.perform(get("/api/solicitudes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tecnicoId").value(5));
    }
}
