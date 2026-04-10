package com.mgcss.domain;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.mgcss.domain.*;
import com.mgcss.service.*;
import java.util.Optional;

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