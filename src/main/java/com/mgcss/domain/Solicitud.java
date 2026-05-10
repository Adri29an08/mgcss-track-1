package com.mgcss.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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

    @ElementCollection
    @CollectionTable(name = "solicitud_historial", joinColumns = @JoinColumn(name = "solicitud_id"))
    @Column(name = "estado_cambio")
    //Aquí vamos a guardar los datos de las solicitudes
    private List<String> historial = new ArrayList<>();

    protected Solicitud() {}

    public Solicitud(String descripcion) {
        validarDescripcion(descripcion);
        this.descripcion = descripcion;
        registrarCambioEstado(EstadoSolicitud.ABIERTA);
        this.fechaCreacion = LocalDateTime.now();
    }

    public void cerrar() {
        validarEstadoParaCierre();
        registrarCambioEstado(EstadoSolicitud.CERRADA);
    }

    public void reabrir() {
        if (this.estado != EstadoSolicitud.CERRADA) {
            throw new IllegalStateException("Solo se pueden reabrir solicitudes CERRADAS");
        }
        registrarCambioEstado(EstadoSolicitud.EN_PROCESO);
    }

    public void asignarTecnico(Tecnico t) {
        asegurarQueSePuedeModificar();
        validarEstadoTecnico(t);
        this.tecnico = t;
    }

    public void iniciarTrabajo() {
        asegurarQueSePuedeModificar();
        validarAsignacionPrevia();
        registrarCambioEstado(EstadoSolicitud.EN_PROCESO);
    }

    private void registrarCambioEstado(EstadoSolicitud nuevoEstado) {
        this.estado = nuevoEstado;
        this.historial.add("Estado cambiado a " + nuevoEstado + " el " + LocalDateTime.now());
    }

    // --- MÉTODOS DE VALIDACIÓN (Sesión 8) ---
    private void validarDescripcion(String desc) {
        if (desc == null || desc.trim().length() < 10) {
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

    // --- GETTERS (CRÍTICOS PARA LOS TESTS) ---
    public Long getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public EstadoSolicitud getEstado() { return estado; }
    public Tecnico getTecnico() { return tecnico; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public List<String> getHistorial() { return new ArrayList<>(historial); }

    public void setId(Long id) { this.id = id; }
}