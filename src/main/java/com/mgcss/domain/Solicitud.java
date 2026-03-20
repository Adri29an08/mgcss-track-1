package com.mgcss.domain;

import java.time.LocalDateTime;

public class Solicitud {

    private Long id;
    private EstadoSolicitud estado;
    private LocalDateTime fechaCreacion;

    public Solicitud() {
        this.estado = EstadoSolicitud.ABIERTA;
        this.fechaCreacion = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public EstadoSolicitud getEstado() { return estado; }
    public void setEstado(EstadoSolicitud estado) { this.estado = estado; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}