package com.mgcss.unit;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mgcss.domain.EstadoTecnico;
import com.mgcss.domain.Solicitud;
import com.mgcss.domain.SolicitudRepository;
import com.mgcss.domain.Tecnico;
import com.mgcss.domain.TecnicoRepository;
import com.mgcss.service.SolicitudService;

@Tag("unit")
class SolicitudServiceTest {


    @Test
    void debeAsignarTecnicoYGuardar() {
        // 1. SETUP: Mocks de infraestructura
        SolicitudRepository repoSol = mock(SolicitudRepository.class);
        TecnicoRepository repoTec = mock(TecnicoRepository.class);
        SolicitudService service = new SolicitudService(repoSol, repoTec);

        Solicitud s = new Solicitud("Descripción de prueba válida");
        Tecnico t = new Tecnico(EstadoTecnico.ACTIVO);

        // Definimos qué responde el "universo simulado"
        when(repoSol.findById(1L)).thenReturn(java.util.Optional.of(s));
        when(repoTec.findById(99L)).thenReturn(java.util.Optional.of(t));

        // 2. ACT: Ejecutar el servicio 
        service.asignarTecnico(1L, 99L);

        // 3. ASSERT/VERIFY: ¿Se llamó al save? 
        verify(repoSol).save(s);
        assertEquals(t, s.getTecnico());
    }

    @Test
    void debeLanzarExcepcionSiLaSolicitudNoExiste() {
        // Arrange
        SolicitudRepository repoS = mock(SolicitudRepository.class);
        TecnicoRepository repoT = mock(TecnicoRepository.class);
        SolicitudService service = new SolicitudService(repoS, repoT);

        // Simular que el repositorio devuelve vacío [cite: 64, 332]
        when(repoS.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            service.asignarTecnico(1L, 10L);
        });
        
        // Verificar que NUNCA se intentó guardar nada si falló antes [cite: 305, 336]
        verify(repoS, never()).save(any());
    }
}