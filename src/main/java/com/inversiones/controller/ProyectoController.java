package com.inversiones.controller;

import com.inversiones.entity.Proyecto;
import com.inversiones.service.ProyectoService;
import com.inversiones.service.TipoRendimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

/**
 * Controlador para la gestión de Proyectos
 */
@Controller
@RequestMapping("/proyecto")
public class ProyectoController {

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private TipoRendimientoService tipoRendimientoService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("proyectos", proyectoService.obtenerTodos());
        return "proyecto/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("proyecto", new Proyecto());
        model.addAttribute("tiposRendimiento", tipoRendimientoService.obtenerTodos());
        return "proyecto/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Proyecto proyecto) {
        proyectoService.guardar(proyecto);
        return "redirect:/proyecto";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Optional<Proyecto> proyecto = proyectoService.obtenerPorId(id);
        if (proyecto.isPresent()) {
            model.addAttribute("proyecto", proyecto.get());
            model.addAttribute("tiposRendimiento", tipoRendimientoService.obtenerTodos());
            return "proyecto/formulario";
        }
        return "redirect:/proyecto";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        proyectoService.eliminar(id);
        return "redirect:/proyecto";
    }
}
