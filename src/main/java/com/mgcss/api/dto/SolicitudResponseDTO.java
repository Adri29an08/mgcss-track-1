package com.mgcss.api.dto;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de una solicitud expuestos al cliente externo")
public class SolicitudResponseDTO {

    @Schema(description = "Identificador único de la solicitud", example = "1")
    private Long id;

    @Schema(description = "Descripción detallada de la incidencia", example = "El servidor de correo no responde desde las 9:00")
    private String descripcion;

    @Schema(description = "Estado actual de la solicitud", example = "ABIERTA", allowableValues = {"ABIERTA", "EN_PROCESO", "CERRADA"})
    private String estado;

    @Schema(description = "ID del técnico asignado, null si no tiene técnico", example = "5")
    private Long tecnicoId;

    @Schema(description = "Fecha y hora de creación de la solicitud")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Historial de transiciones de estado")
    private List<String> historial;

    public SolicitudResponseDTO() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Long getTecnicoId() { return tecnicoId; }
    public void setTecnicoId(Long tecnicoId) { this.tecnicoId = tecnicoId; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public List<String> getHistorial() { return historial; }
    public void setHistorial(List<String> historial) { this.historial = historial; }
}