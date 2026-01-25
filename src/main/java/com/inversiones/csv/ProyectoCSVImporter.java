package com.inversiones.csv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inversiones.entity.Proyecto;
import com.inversiones.repository.ProyectoRepository;

@Service
public class ProyectoCSVImporter {

	@Autowired
	private ProyectoRepository proyectoRepository;

	private static final String CSV_SEPARATOR = ";";
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final int BATCH_SIZE = 1000;

	/**
	 * Importa proyectos desde un archivo CSV
	 * 
	 * @param csvFilePath Ruta del archivo CSV
	 * @return Número de registros importados
	 */
	@Transactional
	public int importarProyectosDesdeCSV(String csvFilePath) {
		List<Proyecto> proyectos = new ArrayList<>();
		int count = 0;
		String line;

		try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {

			// Saltar la primera línea (encabezados)
			br.readLine();

			while ((line = br.readLine()) != null) {
				try {
					String[] datos = line.split(CSV_SEPARATOR, -1); // -1 para incluir campos vacíos

					Proyecto proyecto = parsearProyecto(datos);
					proyectos.add(proyecto);
					count++;

					// Guardar en lotes para mejor rendimiento
					if (proyectos.size() >= BATCH_SIZE) {
						proyectoRepository.saveAll(proyectos);
						proyectoRepository.flush();
						proyectos.clear();
						System.out.println("Insertados " + count + " registros...");
					}

				} catch (Exception e) {
					System.err.println("Error al procesar línea " + count + ": " + e.getMessage());
					// Continuar con el siguiente registro
				}
			}

			// Guardar los registros restantes
			if (!proyectos.isEmpty()) {
				proyectoRepository.saveAll(proyectos);
				proyectoRepository.flush();
			}

			System.out.println("Importación completada. Total de registros: " + count);

		} catch (IOException e) {
			System.err.println("Error al leer el archivo CSV: " + e.getMessage());
			throw new RuntimeException("Error al importar CSV", e);
		}

		return count;
	}

	/**
	 * Parsea una línea del CSV y crea un objeto Proyecto
	 * 
	 * @param datos Array con los datos de la línea
	 * @return Objeto Proyecto
	 */
	private Proyecto parsearProyecto(String[] datos) {
		Proyecto proyecto = new Proyecto();

		// Nota: El ID se genera automáticamente, no se incluye en el CSV
		// Ajusta los índices según el orden de las columnas en tu CSV

		proyecto.setInmueble(parseString(datos, 0));
		proyecto.setMoneda(parseString(datos, 1));
		proyecto.setIdTipoRendimiento(parseInteger(datos, 2));
		proyecto.setEstado(parseString(datos, 3));
		proyecto.setDuracion(parseDouble(datos, 4));
		proyecto.setEstatus(parseString(datos, 5));

		proyecto.setRentabilidadMensualReentel(parseDouble(datos, 6));
		proyecto.setRentabilidadMensualReentelPro(parseDouble(datos, 7));
		proyecto.setRentabilidadMensualReentelSuper(parseDouble(datos, 8));

		proyecto.setRentabilidadAnualReentel(parseDouble(datos, 9));
		proyecto.setRentabilidadAnualReentelPro(parseDouble(datos, 10));
		proyecto.setRentabilidadAnualReentelSuper(parseDouble(datos, 11));

		proyecto.setRentabilidadTotalReentel(parseDouble(datos, 12));
		proyecto.setRentabilidadTotalReentelPro(parseDouble(datos, 13));
		proyecto.setRentabilidadTotalReentelSuper(parseDouble(datos, 14));

		proyecto.setFechaEntrada(parseDate(datos, 15));
		proyecto.setFechaCierre(parseDate(datos, 16));
		proyecto.setFechaFin(parseDate(datos, 17));
		proyecto.setFechaInicioRenta(parseDate(datos, 18));

		proyecto.setImporteInversionEur(parseDouble(datos, 19));
		proyecto.setImporteInversionDol(parseDouble(datos, 20));
		proyecto.setNumeroTokens(parseDouble(datos, 21));
		proyecto.setColateralizable(parseBoolean(datos, 22));

		return proyecto;
	}

	/**
	 * Métodos auxiliares para parsear datos con manejo de nulos y errores
	 */

	private String parseString(String[] datos, int index) {
		if (index >= datos.length || datos[index] == null || datos[index].trim().isEmpty()) {
			return null;
		}
		return datos[index].trim();
	}

	private Integer parseInteger(String[] datos, int index) {
		String value = parseString(datos, index);
		if (value == null) {
			return null;
		}
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			System.err.println("Error al parsear Integer en índice " + index + ": " + value);
			return null;
		}
	}

	private Double parseDouble(String[] datos, int index) {
		String value = parseString(datos, index);
		if (value == null) {
			return null;
		}
		value = value.replaceAll(",", ".");
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			System.err.println("Error al parsear Double en índice " + index + ": " + value);
			return null;
		}
	}

	private LocalDate parseDate(String[] datos, int index) {
		String value = parseString(datos, index);
		if (value == null) {
			return null;
		}
		try {
			return LocalDate.parse(value, DATE_FORMATTER);
		} catch (DateTimeParseException e) {
			System.err.println("Error al parsear fecha en índice " + index + ": " + value);
			return null;
		}
	}

	private Boolean parseBoolean(String[] datos, int index) {
		String value = parseString(datos, index);
		if (value == null) {
			return null;
		}
		// Acepta: true/false, 1/0, yes/no, si/no
		value = value.toLowerCase();
		if (value.equals("true") || value.equals("1") || value.equals("yes") || value.equals("si")) {
			return true;
		} else if (value.equals("false") || value.equals("0") || value.equals("no")) {
			return false;
		}
		return null;
	}
}