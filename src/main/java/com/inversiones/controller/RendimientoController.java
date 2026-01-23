package com.inversiones.controller;

import com.inversiones.entity.Rendimiento;
import com.inversiones.service.RendimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

/**
 * Controlador para la gestión de Rendimientos
 */
@Controller
@RequestMapping("/rendimiento")
public class RendimientoController {

    @Autowired
    private RendimientoService service;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("rendimientos", service.obtenerTodos());
        return "rendimiento/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("rendimiento", new Rendimiento());
        return "rendimiento/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Rendimiento rendimiento) {
        service.guardar(rendimiento);
        return "redirect:/rendimiento";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Optional<Rendimiento> rendimiento = service.obtenerPorId(id);
        if (rendimiento.isPresent()) {
            model.addAttribute("rendimiento", rendimiento.get());
            return "rendimiento/formulario";
        }
        return "redirect:/rendimiento";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return "redirect:/rendimiento";
    }
}
