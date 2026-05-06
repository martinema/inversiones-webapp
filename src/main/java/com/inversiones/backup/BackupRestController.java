package com.inversiones.backup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para los endpoints de backup.
 * 
 * Proporciona endpoints para que la interfaz web acceda a las funcionalidades
 * de backup.
 */
@RestController
@RequestMapping("/api/backup")
public class BackupRestController {

	@Autowired
	private ScheduledBackup scheduledBackup;

	/**
	 * Endpoint para ejecutar un backup bajo demanda.
	 * 
	 * @return respuesta con estado del backup
	 */
	@PostMapping("/execute")
	public BackupController.BackupResponse executeBackup() {
		String result = scheduledBackup.executeBackupOnDemand();

		BackupController.BackupResponse response = new BackupController.BackupResponse();
		response.setMessage(result);
		response.setTimestamp(System.currentTimeMillis());

		if (result.contains("exitosamente")) {
			response.setStatus("SUCCESS");
		} else {
			response.setStatus("ERROR");
		}

		return response;
	}

	/**
	 * Endpoint para obtener estado del servicio de backup.
	 * 
	 * @return informacion del servicio
	 */
	@GetMapping("/status")
	public BackupController.BackupStatusResponse getBackupStatus() {
		BackupController.BackupStatusResponse response = new BackupController.BackupStatusResponse();
		response.setServiceActive(true);
		response.setScheduledBackupEnabled(true);
		response.setScheduleDescription("Cada 15 dias a las 00:00:00");
		response.setBackupDirectory("data/backups");
		response.setMessage("Servicio de backup disponible");
		response.setNextBackupDate(calculateNextBackupDate());

		return response;
	}

	/**
	 * Calcula la fecha de la próxima ejecución de backup.
	 * 
	 * @return fecha formateada de la próxima ejecución
	 */
	private String calculateNextBackupDate() {
		java.util.Calendar now = java.util.Calendar.getInstance();
		int dayOfMonth = now.get(java.util.Calendar.DAY_OF_MONTH);

		// Los backups se ejecutan el día 1 y 16 de cada mes a las 00:00
		int nextDay;
		if (dayOfMonth < 1) {
			nextDay = 1;
		} else if (dayOfMonth >= 1 && dayOfMonth < 16) {
			nextDay = 16;
		} else {
			// Próximo mes, día 1
			now.add(java.util.Calendar.MONTH, 1);
			nextDay = 1;
		}

		now.set(java.util.Calendar.DAY_OF_MONTH, nextDay);
		now.set(java.util.Calendar.HOUR_OF_DAY, 0);
		now.set(java.util.Calendar.MINUTE, 0);
		now.set(java.util.Calendar.SECOND, 0);

		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return sdf.format(now.getTime());
	}

}
