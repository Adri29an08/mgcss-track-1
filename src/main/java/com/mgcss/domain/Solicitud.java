package com.mgcss.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;

@Entity
public class Solicitud {

    private Long id;
    private EstadoSolicitud estado;
    private LocalDateTime fechaCreacion;
    private Tecnico tecnico; // Necesario para la Regla de Asignación
    private String descripcion; // Necesario para la Regla de Integridad

    public Solicitud(String descripcion) {
        validarDescripcion(descripcion); // Regla 4: Integridad
        this.descripcion = descripcion;
        this.estado = EstadoSolicitud.ABIERTA;
        this.fechaCreacion = LocalDateTime.now();
    }

    // REGLA 1: Solo se puede cerrar si está EN_PROCESO
    public void cerrar() {
        if (this.estado != EstadoSolicitud.EN_PROCESO) {
            throw new IllegalStateException("Solo solicitudes en proceso pueden cerrarse");
        }
        this.estado = EstadoSolicitud.CERRADA;
    }

    // REGLA 2: Solo técnicos activos
    public void asignarTecnico(Tecnico t) {
        verificarSiEstaCerrada(); // Regla 5: Inmutabilidad
        if (t.getEstado() != EstadoTecnico.ACTIVO) {
            throw new IllegalStateException("El técnico debe estar ACTIVO");
        }
        this.tecnico = t;
    }

    // REGLA 3: No se puede iniciar (EN_PROCESO) sin técnico 
    public void iniciarTrabajo() {
        verificarSiEstaCerrada();
        if (this.tecnico == null) {
            throw new IllegalStateException("No se puede iniciar sin un técnico asignado");
        }
        this.estado = EstadoSolicitud.EN_PROCESO;
    }

    private void validarDescripcion(String desc) {
        if (desc == null || desc.trim().length() < 10) {
            throw new IllegalArgumentException("Descripción obligatoria (mínimo 10 carac.)");
        }
    }

    private void verificarSiEstaCerrada() {
        if (this.estado == EstadoSolicitud.CERRADA) {
            throw new IllegalStateException("No se permiten cambios en una solicitud CERRADA");
        }
    }

    // Getters necesarios para SonarCloud 
    public Long getId() { return id; }
    public EstadoSolicitud getEstado() { return estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public String getDescripcion() { return descripcion; }
    public Tecnico getTecnico() { return tecnico; }
    
    // IMPORTANTE: setId se mantiene para JPA/Hibernate si lo usáis,
    // pero setEstado DEBE DESAPARECER
    public void setId(Long id) { this.id = id; }
}