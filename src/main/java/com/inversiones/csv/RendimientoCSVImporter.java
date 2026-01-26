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

import com.inversiones.entity.Rendimiento;
import com.inversiones.repository.RendimientoRepository;

@Service
public class RendimientoCSVImporter {

	@Autowired
	private RendimientoRepository RendimientoRepository;

	private static final String CSV_SEPARATOR = ";";
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy H:mm");
	private static final int BATCH_SIZE = 1000;

	/**
	 * Importa Rendimientos desde un archivo CSV
	 * 
	 * @param csvFilePath Ruta del archivo CSV
	 * @return Número de registros importados
	 */
	@Transactional
	public int importarRendimientosDesdeCSV(String csvFilePath) {
		List<Rendimiento> rendimientos = new ArrayList<>();
		int count = 0;
		String line;

		try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {

			// Saltar la primera línea (encabezados)
			br.readLine();

			while ((line = br.readLine()) != null) {
				try {
					String[] datos = line.split(CSV_SEPARATOR, -1); // -1 para incluir campos vacíos

					Rendimiento Rendimiento = parsearRendimiento(datos);
					rendimientos.add(Rendimiento);
					count++;

					// Guardar en lotes para mejor rendimiento
					if (rendimientos.size() >= BATCH_SIZE) {
						RendimientoRepository.saveAll(rendimientos);
						RendimientoRepository.flush();
						rendimientos.clear();
						System.out.println("Insertados " + count + " registros...");
					}

				} catch (Exception e) {
					System.err.println("Error al procesar línea " + count + ": " + e.getMessage());
					// Continuar con el siguiente registro
				}
			}

			// Guardar los registros restantes
			if (!rendimientos.isEmpty()) {
				RendimientoRepository.saveAll(rendimientos);
				RendimientoRepository.flush();
			}

			System.out.println("Importación completada. Total de registros: " + count);

		} catch (IOException e) {
			System.err.println("Error al leer el archivo CSV: " + e.getMessage());
			throw new RuntimeException("Error al importar CSV", e);
		}

		return count;
	}

	/**
	 * Parsea una línea del CSV y crea un objeto Rendimiento
	 * 
	 * @param datos Array con los datos de la línea
	 * @return Objeto Rendimiento
	 */
	private Rendimiento parsearRendimiento(String[] datos) {
		Rendimiento rendimiento = new Rendimiento();

		// Nota: El ID se genera automáticamente, no se incluye en el CSV
		// Ajusta los índices según el orden de las columnas en tu CSV
		// id;dividend;hashId;wallet;distributed;retained;reinvested;claimed;claims;reinvests;date
		rendimiento.setDividend(parseString(datos, 1));
		rendimiento.setHashid(parseString(datos, 2));
		rendimiento.setWallet(parseString(datos, 3));
		rendimiento.setDistributed(parseDouble(datos, 4));
		rendimiento.setRetained(parseDouble(datos, 5));

		rendimiento.setReinvested(parseDouble(datos, 6));
		rendimiento.setClaimed(parseDouble(datos, 7));
		rendimiento.setClaims(parseString(datos, 8));

		rendimiento.setReinvests(parseString(datos, 9));
		rendimiento.setDate(parseDate(datos, 10));// 02/12/2025 9:31
		return rendimiento;
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