package com.inversiones.controller;

import java.util.ArrayList;
import java.util.List;
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

import com.inversiones.entity.TipoRendimiento;
import com.inversiones.service.TipoRendimientoService;

/**
 * Controlador para la gestión de Tipos de Rendimiento
 */
@Controller
@RequestMapping("/tipoRendimiento")
public class TipoRendimientoController {

	@Autowired
	private TipoRendimientoService service;

	@GetMapping
	public String listar(Model model) {
		model.addAttribute("tiposRendimiento", service.obtenerTodos());
		return "tipoRendimiento/lista";
	}

	@GetMapping("/nuevo")
	public String nuevo(Model model) {
		model.addAttribute("tipoRendimiento", new TipoRendimiento());
		return "tipoRendimiento/formulario";
	}

	@GetMapping("/crear")
	public String crear(Model model) {
		List<TipoRendimiento> lstTR = new ArrayList<>();
		lstTR.add(new TipoRendimiento(null, "Mensual", 1));
		lstTR.add(new TipoRendimiento(null, "Mensual + final", 1));
		lstTR.add(new TipoRendimiento(null, "Trimestral", 3));
		lstTR.add(new TipoRendimiento(null, "Trimestral + final", 3));
		lstTR.add(new TipoRendimiento(null, "Final", 1));
		lstTR.stream().forEach(tr -> {
			service.guardar(tr);
		});
		model.addAttribute("tiposRendimiento", service.obtenerTodos());
		return "tipoRendimiento/lista";
	}

	@PostMapping("/guardar")
	public String guardar(@ModelAttribute TipoRendimiento tipoRendimiento) {
		service.guardar(tipoRendimiento);
		return "redirect:/tipoRendimiento";
	}

	@GetMapping("/editar/{id}")
	public String editar(@PathVariable Integer id, Model model) {
		Optional<TipoRendimiento> tipoRendimiento = service.obtenerPorId(id);
		if (tipoRendimiento.isPresent()) {
			model.addAttribute("tipoRendimiento", tipoRendimiento.get());
			return "tipoRendimiento/formulario";
		}
		return "redirect:/tipoRendimiento";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable Integer id) {
		service.eliminar(id);
		return "redirect:/tipoRendimiento";
	}

	@PostMapping("/eliminarSeleccionados")
	public String eliminarSeleccionados(@RequestParam(value = "ids", required = false) Integer[] ids) {
		if (ids != null && ids.length > 0) {
			for (Integer id : ids) {
				service.eliminar(id);
			}
		}
		return "redirect:/tipoRendimiento";
	}
}
