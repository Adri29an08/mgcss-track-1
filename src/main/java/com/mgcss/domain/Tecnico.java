package com.mgcss.domain;

public class Tecnico {

    private Long id;
    private String nombre;
    private EstadoTecnico estado;

    // Constructor para tests y creación rápida
    public Tecnico(EstadoTecnico estado) {
        this.estado = estado;
    }

    // Constructor completo
    public Tecnico(Long id, String nombre, EstadoTecnico estado) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
    }

    // Getters: SonarCloud los usará para verificar el estado en los tests 
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public EstadoTecnico getEstado() { return estado; }

    // No ponemos setter para 'estado' para evitar mutaciones accidentales 
    // desde fuera del dominio (Encapsulación estricta) 
    public void desactivar() {
        this.estado = EstadoTecnico.INACTIVO;
    }

    public void activar() {
        this.estado = EstadoTecnico.ACTIVO;
    }
}