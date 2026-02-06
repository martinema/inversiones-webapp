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

import com.inversiones.entity.Proyecto;
import com.inversiones.entity.Rendimiento;
import com.inversiones.repository.RendimientoRepository;
import com.inversiones.service.ProyectoService;
import com.inversiones.service.RendimientoService;

/**
 * Controlador para la gestión de Rendimientos
 */
@Controller
@RequestMapping("/rendimiento")
public class RendimientoController {

	@Autowired
	private RendimientoService service;

	@Autowired
	private ProyectoService proyectoService;

	@Autowired
	private RendimientoRepository rendimientoRepository;

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

		// Ordenar alfabéticamente los rendimientos dentro de cada mes por dividend
		rendimientosPorMes.forEach((mes, lista) -> {
			lista.sort((r1, r2) -> {
				String d1 = r1.getDividend() != null ? r1.getDividend() : "";
				String d2 = r2.getDividend() != null ? r2.getDividend() : "";
				// Ordenar por la parte antes del símbolo # (nombre del proyecto/inmueble)
				String name1 = d1.contains("#") ? d1.substring(0, d1.indexOf("#")) : d1;
				String name2 = d2.contains("#") ? d2.substring(0, d2.indexOf("#")) : d2;
				return name1.compareTo(name2);
			});
		});

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

		// Agrupar por año (ordenado por YearMonth descendente)
		Map<Integer, Map<YearMonth, List<Rendimiento>>> rendimientosPorAno = rendimientosPorMes.entrySet().stream()
				.collect(Collectors.groupingBy(e -> e.getKey().getYear(), LinkedHashMap::new,
						Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1,
								() -> new LinkedHashMap<YearMonth, List<Rendimiento>>() {
									@Override
									public List<Rendimiento> put(YearMonth key, List<Rendimiento> value) {
										return super.put(key, value);
									}
								})))
				.entrySet().stream()
				.collect(Collectors.toMap(
						Map.Entry::getKey, e -> e.getValue().entrySet().stream()
								.sorted((a, b) -> b.getKey().compareTo(a.getKey())).collect(Collectors.toMap(
										Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, LinkedHashMap::new)),
						(v1, v2) -> v1, LinkedHashMap::new));

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

		// Ordenar los años en orden descendente
		List<Integer> anosOrdenados = rendimientosPorAno.keySet().stream()
				.sorted((a, b) -> b.compareTo(a))
				.collect(Collectors.toList());

		model.addAttribute("rendimientosPorMes", rendimientosPorMes);
		model.addAttribute("totalesPorMes", totalesPorMes);
		model.addAttribute("rendimientosPorAno", rendimientosPorAno);
		model.addAttribute("totalesPorAno", totalesPorAno);
		model.addAttribute("meses", rendimientosPorMes.keySet());
		model.addAttribute("anos", anosOrdenados);

		return "rendimiento/resumenMensual";
	}

	@GetMapping("/estimacionMensual")
	public String estimacionMensual(Model model) {
		List<Proyecto> proyectos = proyectoService.obtenerTodos();
		List<Rendimiento> rendimientos = rendimientoRepository.findAll();

		// Filtrar proyectos con fechas de inicio de renta válidas
		List<Proyecto> proyectosConRenta = proyectos.stream()
				.filter(p -> p.getFechaInicioRenta() != null && p.getFechaFin() != null).collect(Collectors.toList());

		// Crear estructura de estimaciones por mes y año
		Map<YearMonth, List<Proyecto>> estimacionesPorMes = new LinkedHashMap<>();

		// Generar estimaciones mensuales para cada proyecto
		for (Proyecto proyecto : proyectosConRenta) {
			LocalDate fechaInicio = proyecto.getFechaInicioRenta();
			LocalDate fechaFin = proyecto.getFechaFin();
			LocalDate fechaCiere = proyecto.getFechaCierre();

			// Recorrer cada mes desde la fecha de inicio hasta la fecha fin
			LocalDate mesActual = fechaInicio.withDayOfMonth(1);
			while (!mesActual.isAfter(fechaFin)) {
				if (fechaCiere == null || mesActual.isBefore(fechaCiere)) {
					YearMonth ym = YearMonth.from(mesActual);
					estimacionesPorMes.putIfAbsent(ym, new java.util.ArrayList<>());
					estimacionesPorMes.get(ym).add(proyecto);
				}
				mesActual = mesActual.plusMonths(1);
			}
		}

		// Ordenar por YearMonth descendente
		Map<YearMonth, List<Proyecto>> estimacionesPorMesOrdenadas = estimacionesPorMes.entrySet().stream()
				.sorted((a, b) -> b.getKey().compareTo(a.getKey()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, LinkedHashMap::new));

		// Ordenar alfabéticamente los proyectos dentro de cada mes
		estimacionesPorMesOrdenadas.forEach((mes, proyectosList) -> {
			proyectosList.sort((p1, p2) -> p1.getInmueble().compareTo(p2.getInmueble()));
		});

		// Crear mapa para almacenar rendimientos distribuidos por proyecto-mes
		Map<String, Double> rendimientosDistribuidosPorProyectoMes = new LinkedHashMap<>();

		// Calcular rendimientos distribuidos por proyecto y mes
		estimacionesPorMesOrdenadas.forEach((mes, proyectosList) -> {
			proyectosList.forEach(proyecto -> {
				String inmueblePrefix = proyecto.getInmueble().trim();
				double rendimientoDistribuidoProyecto = rendimientos.stream().filter(r -> r.getDividend() != null)
						.filter(r -> {
							// Extraer la parte anterior a "#" del dividend y comparar
							String dividendPart = r.getDividend().contains("#")
									? r.getDividend().substring(0, r.getDividend().indexOf("#")).trim()
									: r.getDividend().trim();
							return dividendPart.equalsIgnoreCase(inmueblePrefix);
						})
						.filter(r -> dameFecha(r) != null && dameFecha(r).getYear() == mes.getYear()
								&& dameFecha(r).getMonthValue() == mes.getMonthValue())
						.mapToDouble(r -> r.getDistributed() != null ? r.getDistributed() : 0).sum();
				String key = proyecto.getId() + "-" + mes.toString();
				rendimientosDistribuidosPorProyectoMes.put(key, rendimientoDistribuidoProyecto);
			});
		});

		// Calcular totales por mes (estimaciones)
		Map<YearMonth, Map<String, Double>> totalesEstimacionesPorMes = new LinkedHashMap<>();
		estimacionesPorMesOrdenadas.forEach((mes, proyectosList) -> {
			Map<String, Double> totales = new LinkedHashMap<>();

			// Calcular rentabilidad mensual por tipo
			double rentMensualReentel = proyectosList.stream()
					.mapToDouble(p -> p.getRentabilidadMensualReentel() != null
							? p.getRentabilidadMensualReentel() * p.getImporteInversionDol() / 100
							: 0)
					.sum();

			double rentMensualReentelPro = proyectosList.stream()
					.mapToDouble(p -> p.getRentabilidadMensualReentelPro() != null
							? p.getRentabilidadMensualReentelPro() * p.getImporteInversionDol() / 100
							: 0)
					.sum();

			double rentMensualReentelSuper = proyectosList.stream()
					.mapToDouble(p -> p.getRentabilidadMensualReentelSuper() != null
							? p.getRentabilidadMensualReentelSuper() * p.getImporteInversionDol() / 100
							: 0)
					.sum();

			double totalMensual = rentMensualReentel + rentMensualReentelPro + rentMensualReentelSuper;

			// Calcular rendimiento distribuido real del mes
			double rendimientoDistribuido = proyectosList.stream().mapToDouble(p -> {
				String inmueblePrefix = p.getInmueble().trim();
				rendimientos.stream().forEach(r -> {
					r.setDate(dameFecha(r));
				});
				return rendimientos.stream().filter(r -> r.getDividend() != null).filter(r -> {
					// Extraer la parte anterior a "#" del dividend y comparar
					String dividendPart = r.getDividend().contains("#")
							? r.getDividend().substring(0, r.getDividend().indexOf("#")).trim()
							: r.getDividend().trim();
					return dividendPart.equalsIgnoreCase(inmueblePrefix);
				}).filter(r -> r.getDate() != null && r.getDate().getYear() == mes.getYear()
						&& r.getDate().getMonthValue() == mes.getMonthValue())
						.mapToDouble(r -> r.getDistributed() != null ? r.getDistributed() : 0).sum();
			}).sum();

			totales.put("rentMensualReentel", rentMensualReentel);
			totales.put("rentMensualReentelPro", rentMensualReentelPro);
			totales.put("rentMensualReentelSuper", rentMensualReentelSuper);
			totales.put("totalMensual", totalMensual);
			totales.put("rendimientoDistribuido", rendimientoDistribuido);
			totales.put("count", (double) proyectosList.size());

			totalesEstimacionesPorMes.put(mes, totales);
		});

		// Agrupar por año
		Map<Integer, Map<YearMonth, List<Proyecto>>> estimacionesPorAno = estimacionesPorMesOrdenadas.entrySet()
				.stream().collect(Collectors.groupingBy(e -> e.getKey().getYear(), LinkedHashMap::new,
						Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, LinkedHashMap::new)));

		// Calcular totales por año (estimaciones)
		Map<Integer, Map<String, Double>> totalesEstimacionesPorAno = new LinkedHashMap<>();
		for (Integer ano : estimacionesPorAno.keySet()) {
			Map<String, Double> totalesAno = new LinkedHashMap<>();
			double rentMensualReentel = 0, rentMensualReentelPro = 0, rentMensualReentelSuper = 0, totalMensual = 0,
					rendimientoDistribuido = 0;
			int count = 0;

			for (YearMonth mes : estimacionesPorAno.get(ano).keySet()) {
				Map<String, Double> totalesMes = totalesEstimacionesPorMes.get(mes);
				rentMensualReentel += totalesMes.get("rentMensualReentel");
				rentMensualReentelPro += totalesMes.get("rentMensualReentelPro");
				rentMensualReentelSuper += totalesMes.get("rentMensualReentelSuper");
				totalMensual += totalesMes.get("totalMensual");
				rendimientoDistribuido += totalesMes.get("rendimientoDistribuido");
				count += totalesMes.get("count").intValue();
			}

			totalesAno.put("rentMensualReentel", rentMensualReentel);
			totalesAno.put("rentMensualReentelPro", rentMensualReentelPro);
			totalesAno.put("rentMensualReentelSuper", rentMensualReentelSuper);
			totalesAno.put("totalMensual", totalMensual);
			totalesAno.put("rendimientoDistribuido", rendimientoDistribuido);
			totalesAno.put("count", (double) count);
			totalesEstimacionesPorAno.put(ano, totalesAno);
		}

		// Calcular totales globales
		double totalGlobalReentel = totalesEstimacionesPorMes.values().stream()
				.mapToDouble(m -> m.get("rentMensualReentel")).sum();
		double totalGlobalReentelPro = totalesEstimacionesPorMes.values().stream()
				.mapToDouble(m -> m.get("rentMensualReentelPro")).sum();
		double totalGlobalReentelSuper = totalesEstimacionesPorMes.values().stream()
				.mapToDouble(m -> m.get("rentMensualReentelSuper")).sum();
		double totalGlobalMensual = totalGlobalReentel + totalGlobalReentelPro + totalGlobalReentelSuper;
		double totalGlobalRendimientoDistribuido = totalesEstimacionesPorMes.values().stream()
				.mapToDouble(m -> m.get("rendimientoDistribuido")).sum();

		model.addAttribute("totalGlobalReentel", totalGlobalReentel);
		model.addAttribute("totalGlobalReentelPro", totalGlobalReentelPro);
		model.addAttribute("totalGlobalReentelSuper", totalGlobalReentelSuper);
		model.addAttribute("totalGlobalMensual", totalGlobalMensual);
		model.addAttribute("totalGlobalRendimientoDistribuido", totalGlobalRendimientoDistribuido);

		model.addAttribute("estimacionesPorMes", estimacionesPorMes);
		model.addAttribute("totalesEstimacionesPorMes", totalesEstimacionesPorMes);
		model.addAttribute("estimacionesPorAno", estimacionesPorAno);
		model.addAttribute("totalesEstimacionesPorAno", totalesEstimacionesPorAno);
		model.addAttribute("rendimientosDistribuidosPorProyectoMes", rendimientosDistribuidosPorProyectoMes);
		model.addAttribute("meses", estimacionesPorMesOrdenadas.keySet());
		model.addAttribute("anos", estimacionesPorAno.keySet());
		model.addAttribute("rendimientos", rendimientos);

		return "rendimiento/estimacionMensual";
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
