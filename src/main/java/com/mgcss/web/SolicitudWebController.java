package com.mgcss.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        model.addAttribute("solicitudes", solicitudService.listarTodas());
        return "solicitudes";
    }

    @PostMapping("/nueva")
    public String crear(@RequestParam String descripcion) {
        solicitudService.crearSolicitud(descripcion);
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
