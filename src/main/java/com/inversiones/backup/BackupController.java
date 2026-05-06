package com.inversiones.backup;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controlador para gestionar las operaciones de backup de la base de datos.
 * 
 * Proporciona endpoints REST y vistas para ejecutar backups, visualizar y
 * descargar copias.
 */
@Controller
@RequestMapping("/backup")
public class BackupController {

	@Autowired
	private ScheduledBackup scheduledBackup;

	@Autowired
	private BackupService backupService;

	/**
	 * Muestra la pagina principal de backup.
	 * 
	 * @param model modelo de datos para la vista
	 * @return nombre de la plantilla
	 */
	@GetMapping
	public String backupPage(Model model) {
		return "backup/lista";
	}

	/**
	 * Endpoint REST para listar backups disponibles.
	 * 
	 * @return lista de backups en formato JSON
	 */
	@GetMapping("/list")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> listBackups() {
		Map<String, Object> response = new HashMap<>();

		try {
			List<BackupService.BackupInfo> backups = backupService.listBackups();
			response.put("success", true);
			response.put("backups", backups);
		} catch (Exception e) {
			response.put("success", false);
			response.put("error", e.getMessage());
		}

		return ResponseEntity.ok(response);
	}

	/**
	 * Endpoint para descargar un archivo de backup.
	 * 
	 * @param filename nombre del archivo a descargar
	 * @return archivo para descargar
	 */
	@GetMapping("/download/{filename}")
	public ResponseEntity<Resource> downloadBackup(@PathVariable String filename) {
		try {
			File file = backupService.getBackupFile(filename);

			if (file == null || !file.exists()) {
				return ResponseEntity.notFound().build();
			}

			Resource resource = new FileSystemResource(file);

			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
					.header(HttpHeaders.CONTENT_TYPE, "application/octet-stream").body(resource);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Endpoint para eliminar un backup.
	 * 
	 * @param filename nombre del archivo a eliminar
	 * @return respuesta de exito o error
	 */
	@DeleteMapping("/delete/{filename}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> deleteBackup(@PathVariable String filename) {
		Map<String, Object> response = new HashMap<>();

		try {
			boolean deleted = backupService.deleteBackup(filename);

			if (deleted) {
				response.put("success", true);
				response.put("message", "Backup eliminado exitosamente");
			} else {
				response.put("success", false);
				response.put("message", "No se pudo eliminar el backup");
			}
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", e.getMessage());
		}

		return ResponseEntity.ok(response);
	}

	/**
	 * Clase interna para la respuesta de backup.
	 */
	public static class BackupResponse {
		private String status;
		private String message;
		private long timestamp;

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}
	}

	/**
	 * Clase interna para la respuesta de estado del backup.
	 */
	public static class BackupStatusResponse {
		private boolean serviceActive;
		private boolean scheduledBackupEnabled;
		private String scheduleDescription;
		private String backupDirectory;
		private String message;
		private String nextBackupDate;

		public boolean isServiceActive() {
			return serviceActive;
		}

		public void setServiceActive(boolean serviceActive) {
			this.serviceActive = serviceActive;
		}

		public boolean isScheduledBackupEnabled() {
			return scheduledBackupEnabled;
		}

		public void setScheduledBackupEnabled(boolean scheduledBackupEnabled) {
			this.scheduledBackupEnabled = scheduledBackupEnabled;
		}

		public String getScheduleDescription() {
			return scheduleDescription;
		}

		public void setScheduleDescription(String scheduleDescription) {
			this.scheduleDescription = scheduleDescription;
		}

		public String getBackupDirectory() {
			return backupDirectory;
		}

		public void setBackupDirectory(String backupDirectory) {
			this.backupDirectory = backupDirectory;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getNextBackupDate() {
			return nextBackupDate;
		}

		public void setNextBackupDate(String nextBackupDate) {
			this.nextBackupDate = nextBackupDate;
		}
	}
}
