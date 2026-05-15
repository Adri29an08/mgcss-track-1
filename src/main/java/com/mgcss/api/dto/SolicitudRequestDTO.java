package com.mgcss.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para la creación de solicitudes.
 * No contiene lógica ni expone estructura interna  */
public class SolicitudRequestDTO {
    @Schema(description = "Descripción detallada de la incidencia", example = "El servidor de correo no responde desde las 9:00", required = true)
    private String descripcion;

     public SolicitudRequestDTO() {}

    public SolicitudRequestDTO(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}