package com.mgcss.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mgcss.domain.Solicitud;
import com.mgcss.domain.SolicitudRepository;
import com.mgcss.domain.Tecnico;
import com.mgcss.domain.TecnicoRepository;

@Service // Imprescindible para que el controlador lo encuentre
public class SolicitudService {
    
    private final SolicitudRepository solicitudRepo;
    private final TecnicoRepository tecnicoRepo;

    public SolicitudService(SolicitudRepository sRepo, TecnicoRepository tRepo) {
        this.solicitudRepo = sRepo;
        this.tecnicoRepo = tRepo;
    }

    // --- MÉTODOS REQUERIDOS POR EL CONTROLADOR (FASE 2) ---

    @Transactional
    public Solicitud crearSolicitud(String descripcion) {
        // La validación de la descripción ocurre dentro del constructor del dominio
        Solicitud nueva = new Solicitud(descripcion);
        return solicitudRepo.save(nueva);
    }

    public Solicitud buscarPorId(Long id) {
        return solicitudRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));
    }

    public List<Solicitud> listarTodas() {
        return solicitudRepo.findAll(); // Requisito: Listar solicitudes [cite: 52]
    }

    @Transactional
    public Solicitud reabrirSolicitud(Long id) {
        Solicitud s = buscarPorId(id);
        s.reabrir(); // Delegamos lógica al dominio 
        return solicitudRepo.save(s);
    }

    @Transactional
    public Solicitud cerrarSolicitud(Long id) {
        Solicitud s = buscarPorId(id);
        s.cerrar(); // Delegamos lógica al dominio 
        return solicitudRepo.save(s);
    }

    @Transactional
    public void asignarTecnico(Long solicitudId, Long tecnicoId) {
        Solicitud s = buscarPorId(solicitudId);
        Tecnico t = tecnicoRepo.findById(tecnicoId)
                .orElseThrow(() -> new IllegalArgumentException("Técnico no encontrado"));

        s.asignarTecnico(t); 
        solicitudRepo.save(s);
    }
}