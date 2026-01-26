package com.inversiones.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.inversiones.csv.RendimientoCSVImporter;

@RestController
@RequestMapping("/api/rendimiento")
public class RendimientoCsvController {

	@Autowired
	private RendimientoCSVImporter csvImporter;

	@PostMapping("/importar-csv")
	public ResponseEntity<String> importarCSV(@RequestParam("csvFile") MultipartFile file) {
		try {
			// Guardar el archivo temporalmente
			File tempFile = File.createTempFile("proyectos", ".csv");
			try (FileOutputStream fos = new FileOutputStream(tempFile)) {
				fos.write(file.getBytes());
			}

			// Importar los datos
			int registrosImportados = csvImporter.importarRendimientosDesdeCSV(tempFile.getAbsolutePath());

			// Eliminar archivo temporal
			tempFile.delete();

			return ResponseEntity.ok("Importación exitosa. Registros importados: " + registrosImportados);

		} catch (IOException e) {
			return ResponseEntity.badRequest().body("Error al procesar el archivo: " + e.getMessage());
		}
	}
}