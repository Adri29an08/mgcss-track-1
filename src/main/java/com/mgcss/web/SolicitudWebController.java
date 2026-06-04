package com.mgcss.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mgcss.domain.EstadoSolicitud;
import com.mgcss.domain.Solicitud;
import com.mgcss.service.SolicitudService;

@Controller
@RequestMapping("/solicitudes")
public class SolicitudWebController {

    private final SolicitudService solicitudService;

    public SolicitudWebController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @GetMapping
    public String listar(Model model) {
        List<Solicitud> solicitudes = solicitudService.listarTodas();
        model.addAttribute("solicitudes", solicitudes);
        model.addAttribute("tecnicos", solicitudService.listarTecnicos());
        model.addAttribute("totalAbiertas",
                solicitudes.stream().filter(s -> s.getEstado() == EstadoSolicitud.ABIERTA).count());
        model.addAttribute("totalEnProceso",
                solicitudes.stream().filter(s -> s.getEstado() == EstadoSolicitud.EN_PROCESO).count());
        model.addAttribute("totalCerradas",
                solicitudes.stream().filter(s -> s.getEstado() == EstadoSolicitud.CERRADA).count());
        return "solicitudes";
    }

    @PostMapping("/nueva")
    public String crear(@RequestParam String descripcion) {
        solicitudService.crearSolicitud(descripcion);
        return "redirect:/solicitudes";
    }

    @PostMapping("/tecnicos/nuevo")
    public String crearTecnico(@RequestParam String nombre) {
        System.out.println("LLEGÓ EL TÉCNICO DESDE LA WEB: " + nombre); // para ver si llega el nombre del técnico
        solicitudService.crearTecnico(nombre);
        return "redirect:/solicitudes";
    }

    @PostMapping("/{id}/iniciar")
    public String iniciar(@PathVariable Long id, @RequestParam Long tecnicoId) {
        solicitudService.asignarTecnico(id, tecnicoId);
        solicitudService.iniciarTrabajo(id);
        return "redirect:/solicitudes";
    }

    @PostMapping("/{id}/cerrar")
    public String cerrar(@PathVariable Long id) {
        solicitudService.cerrarSolicitud(id);
        return "redirect:/solicitudes";
    }

    @PostMapping("/{id}/reabrir")
    public String reabrir(@PathVariable Long id) {
        solicitudService.reabrirSolicitud(id);
        return "redirect:/solicitudes";
    }
}
