package com.mgcss.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mgcss.api.dto.SolicitudRequestDTO;
import com.mgcss.api.dto.SolicitudResponseDTO;
import com.mgcss.domain.Solicitud;
import com.mgcss.service.SolicitudService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    // POST -> Crear solicitud
    @PostMapping
    public ResponseEntity<SolicitudResponseDTO> crear(@RequestBody SolicitudRequestDTO request) {
        Solicitud nueva = solicitudService.crearSolicitud(request.getDescripcion());
        return ResponseEntity.ok(mapToResponseDTO(nueva));
    }

    // GET -> Consultar por ID
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudResponseDTO> consultar(@PathVariable Long id) {
        Solicitud solicitud = solicitudService.buscarPorId(id);
        return ResponseEntity.ok(mapToResponseDTO(solicitud));
    }

    // GET -> Listar todas
    @GetMapping
    public ResponseEntity<List<SolicitudResponseDTO>> listar() {
        List<SolicitudResponseDTO> lista = solicitudService.listarTodas().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    // PATCH -> Reabrir solicitud 
    @Operation(summary = "Reabrir una solicitud", description = "Cambia el estado de una solicitud CERRADA a EN_PROCESO")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud reabierta con éxito"),
        @ApiResponse(responseCode = "400", description = "La solicitud no estaba CERRADA"),
        @ApiResponse(responseCode = "404", description = "No existe la solicitud")
    })
    @PatchMapping("/{id}/reabrir")
    public ResponseEntity<SolicitudResponseDTO> reabrir(@PathVariable Long id) {
        Solicitud reabierta = solicitudService.reabrirSolicitud(id);
        return ResponseEntity.ok(mapToResponseDTO(reabierta));
    }

    // PUT -> Cerrar solicitud 
    @PutMapping("/{id}/cerrar")
    public ResponseEntity<SolicitudResponseDTO> cerrar(@PathVariable Long id) {
        Solicitud cerrada = solicitudService.cerrarSolicitud(id);
        return ResponseEntity.ok(mapToResponseDTO(cerrada));
    }

    // MÉTODOS DE MAPEO Fase 2.3
    private SolicitudResponseDTO mapToResponseDTO(Solicitud s) {
        SolicitudResponseDTO dto = new SolicitudResponseDTO();
        dto.setId(s.getId());
        dto.setDescripcion(s.getDescripcion());
        dto.setEstado(s.getEstado().toString());
        dto.setFechaCreacion(s.getFechaCreacion());
        dto.setHistorial(s.getHistorial());
        
        if (s.getTecnico() != null) {
            dto.setTecnicoId(s.getTecnico().getId());
        }
        
        return dto;
    }
}