package com.inversiones.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

	private LocalDate dameFecha(Rendimiento r) {
		String strFecha = r.getDividend();
		strFecha = "01/" + strFecha.substring(strFecha.indexOf("#") + 1, strFecha.length());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/M/yyyy"); // 12/2024
		return LocalDate.parse(strFecha, formatter);

	}

	@GetMapping("/resumenMensual")
	public String resumenMensual(Model model) {
		List<Rendimiento> rendimientos = service.obtenerTodos();

		// Agrupar por mes
		Map<YearMonth, List<Rendimiento>> rendimientosPorMes = rendimientos.stream().filter(r -> dameFecha(r) != null)
				.collect(Collectors.groupingBy(r -> YearMonth.from(dameFecha(r)), LinkedHashMap::new,
						Collectors.toList()));

		// Calcular totales por mes
		Map<YearMonth, Map<String, Double>> totalesPorMes = new LinkedHashMap<>();
		rendimientosPorMes.forEach((mes, lista) -> {
			Map<String, Double> totales = new LinkedHashMap<>();
			totales.put("distributed",
					lista.stream().mapToDouble(r -> r.getDistributed() != null ? r.getDistributed() : 0).sum());
			totales.put("retained",
					lista.stream().mapToDouble(r -> r.getRetained() != null ? r.getRetained() : 0).sum());
			totales.put("reinvested",
					lista.stream().mapToDouble(r -> r.getReinvested() != null ? r.getReinvested() : 0).sum());
			totales.put("claimed", lista.stream().mapToDouble(r -> r.getClaimed() != null ? r.getClaimed() : 0).sum());
			totales.put("count", (double) lista.size());
			totalesPorMes.put(mes, totales);
		});

		// Agrupar por año
		Map<Integer, Map<YearMonth, List<Rendimiento>>> rendimientosPorAno = rendimientosPorMes.entrySet()
				.stream()
				.collect(Collectors.groupingBy(
					e -> e.getKey().getYear(),
					LinkedHashMap::new,
					Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
				));

		// Calcular totales por año
		Map<Integer, Map<String, Double>> totalesPorAno = new LinkedHashMap<>();
		for (Integer ano : rendimientosPorAno.keySet()) {
			Map<String, Double> totalesAno = new LinkedHashMap<>();
			double distributed = 0, retained = 0, reinvested = 0, claimed = 0;
			int count = 0;

			for (YearMonth mes : rendimientosPorAno.get(ano).keySet()) {
				Map<String, Double> totalesMes = totalesPorMes.get(mes);
				distributed += totalesMes.get("distributed");
				retained += totalesMes.get("retained");
				reinvested += totalesMes.get("reinvested");
				claimed += totalesMes.get("claimed");
				count += totalesMes.get("count").intValue();
			}

			totalesAno.put("distributed", distributed);
			totalesAno.put("retained", retained);
			totalesAno.put("reinvested", reinvested);
			totalesAno.put("claimed", claimed);
			totalesAno.put("count", (double) count);
			totalesPorAno.put(ano, totalesAno);
		}

		model.addAttribute("totalDistributed",
				totalesPorMes.values().stream().mapToDouble(m -> m.get("distributed")).sum());
		model.addAttribute("totalRetained", totalesPorMes.values().stream().mapToDouble(m -> m.get("retained")).sum());
		model.addAttribute("totalReinvested",
				totalesPorMes.values().stream().mapToDouble(m -> m.get("reinvested")).sum());
		model.addAttribute("totalClaimed", totalesPorMes.values().stream().mapToDouble(m -> m.get("claimed")).sum());
		
		model.addAttribute("rendimientosPorMes", rendimientosPorMes);
		model.addAttribute("totalesPorMes", totalesPorMes);
		model.addAttribute("rendimientosPorAno", rendimientosPorAno);
		model.addAttribute("totalesPorAno", totalesPorAno);
		model.addAttribute("meses", rendimientosPorMes.keySet());
		model.addAttribute("anos", rendimientosPorAno.keySet());
		
		return "rendimiento/resumenMensual";
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
