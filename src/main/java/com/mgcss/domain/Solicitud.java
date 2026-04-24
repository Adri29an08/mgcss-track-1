package com.mgcss.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tecnico_id")
    private Tecnico tecnico;

    private String descripcion;

    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estado;

    private LocalDateTime fechaCreacion;

    protected Solicitud() {}

    public Solicitud(String descripcion) {
        validarDescripcion(descripcion);
        this.descripcion = descripcion;
        this.estado = EstadoSolicitud.ABIERTA;
        this.fechaCreacion = LocalDateTime.now();
    }

    // --- MÉTODOS DE NEGOCIO REFACTORIZADOS ---

    public void cerrar() {
        validarEstadoParaCierre();
        this.estado = EstadoSolicitud.CERRADA;
    }

    public void asignarTecnico(Tecnico t) {
        asegurarQueSePuedeModificar();
        validarEstadoTecnico(t);
        this.tecnico = t;
    }

    public void iniciarTrabajo() {
        asegurarQueSePuedeModificar();
        validarAsignacionPrevia();
        this.estado = EstadoSolicitud.EN_PROCESO;
    }

    // --- PASO 4: REFACTORIZACIÓN (EXTRACT METHOD) ---
    // Hemos extraído cada 'if' a un método privado para bajar la complejidad [cite: 74, 78]

    private void validarDescripcion(String desc) {
        validarPresencia(desc);
        validarLongitudMinima(desc);
    }

    private void validarPresencia(String desc) {
        if (desc == null || desc.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción es obligatoria");
        }
    }

    private void validarLongitudMinima(String desc) {
        if (desc.trim().length() < 10) {
            throw new IllegalArgumentException("Descripción obligatoria (mínimo 10 carac.)");
        }
    }

    private void asegurarQueSePuedeModificar() {
        if (this.estado == EstadoSolicitud.CERRADA) {
            throw new IllegalStateException("No se permiten cambios en una solicitud CERRADA");
        }
    }

    private void validarEstadoParaCierre() {
        if (this.estado != EstadoSolicitud.EN_PROCESO) {
            throw new IllegalStateException("Solo solicitudes en proceso pueden cerrarse");
        }
    }

    private void validarEstadoTecnico(Tecnico t) {
        if (t.getEstado() != EstadoTecnico.ACTIVO) {
            throw new IllegalStateException("El técnico debe estar ACTIVO");
        }
    }

    private void validarAsignacionPrevia() {
        if (this.tecnico == null) {
            throw new IllegalStateException("No se puede iniciar sin un técnico asignado");
        }
    }

    // Getters y Setters necesarios para persistencia y Sonar
    public Long getId() { return id; }
    public EstadoSolicitud getEstado() { return estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public String getDescripcion() { return descripcion; }
    public Tecnico getTecnico() { return tecnico; }
    public void setId(Long id) { this.id = id; }
}