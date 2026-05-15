package com.mgcss.api.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para exponer los datos de una solicitud al exterior.
 */

public class SolicitudResponseDTO {

    private Long id;
    private String descripcion;
    private String estado;
    private Long tecnicoId; // Solo exponemos el ID, no la entidad completa 
    private LocalDateTime fechaCreacion;
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