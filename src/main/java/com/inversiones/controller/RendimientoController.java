package com.inversiones.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.inversiones.entity.Rendimiento;
import com.inversiones.service.RendimientoService;

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

	@GetMapping("/importCsv")
	public String importarCSV(Model model) {
		return "rendimiento/importCsv";
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

	@PostMapping("/eliminarSeleccionados")
	public String eliminarSeleccionados(@RequestParam(value = "ids", required = false) Integer[] ids) {
		if (ids != null && ids.length > 0) {
			for (Integer id : ids) {
				service.eliminar(id);
			}
		}
		return "redirect:/rendimiento";
	}
}
