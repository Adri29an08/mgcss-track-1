package com.mgcss.service;

import com.mgcss.domain.Solicitud;
import com.mgcss.domain.SolicitudRepository;
import com.mgcss.domain.TecnicoRepository;
import com.mgcss.domain.Tecnico;

public class SolicitudService {
    
    private final SolicitudRepository solicitudRepo;
    private final TecnicoRepository tecnicoRepo;

    public SolicitudService(SolicitudRepository sRepo, TecnicoRepository tRepo) {
        this.solicitudRepo = sRepo;
        this.tecnicoRepo = tRepo;
    }

    public void asignarTecnico(Long solicitudId, Long tecnicoId) {
        // 1. Recuperar de infraestructura
        Solicitud s = solicitudRepo.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));
        Tecnico t = tecnicoRepo.findById(tecnicoId)
                .orElseThrow(() -> new IllegalArgumentException("Técnico no encontrado"));

        // 2. Delegar lógica al dominio (¡No hagas el 'if' aquí!) 
        s.asignarTecnico(t); 

        // 3. Guardar cambios 
        solicitudRepo.save(s);
    }
}
