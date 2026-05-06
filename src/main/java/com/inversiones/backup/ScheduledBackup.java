package com.inversiones.backup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Servicio para realizar copias de seguridad programadas de la base de datos H2.
 * 
 * Caracteristicas:
 * - Realiza backups automaticos cada 15 dias
 * - Permite ejecutar backups bajo demanda a traves de endpoint web
 * - Almacena los backups en el directorio data/backups
 * - Usa comandos SQL BACKUP para no interferir con la BD en uso
 */
@Service
@EnableScheduling
public class ScheduledBackup {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledBackup.class);
    private static final String BACKUP_DIR = "data/backups";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    @Value("${spring.datasource.url:jdbc:h2:file:./data/inversiones}")
    private String datasourceUrl;

    @Autowired
    private DataSource dataSource;

    /**
     * Ejecuta el backup de forma programada cada 15 dias.
     */
    @Scheduled(cron = "0 0 0 */15 * *")
    public void executeScheduledBackup() {
        logger.info("Iniciando backup programado...");
        try {
            performBackup();
            logger.info("Backup programado completado exitosamente");
        } catch (Exception e) {
            logger.error("Error al realizar el backup programado", e);
        }
    }

    /**
     * Metodo publico para ejecutar el backup bajo demanda (desde controlador REST).
     * 
     * @return mensaje con el resultado del backup
     */
    public String executeBackupOnDemand() {
        logger.info("Ejecutando backup bajo demanda...");
        try {
            performBackup();
            logger.info("Backup bajo demanda completado exitosamente");
            return "Backup realizado exitosamente";
        } catch (Exception e) {
            logger.error("Error al realizar el backup bajo demanda", e);
            return "Error al realizar el backup: " + e.getMessage();
        }
    }

    /**
     * Realiza la copia de seguridad de la base de datos H2 usando comando SQL BACKUP.
     * Este metodo es seguro de usar mientras la aplicacion sigue en ejecucion.
     * 
     * @throws Exception si hay error al realizar el backup
     */
    private void performBackup() throws Exception {
        // Crear directorio de backups si no existe
        Path backupPath = Paths.get(BACKUP_DIR);
        if (!Files.exists(backupPath)) {
            Files.createDirectories(backupPath);
            logger.info("Directorio de backups creado: {}", BACKUP_DIR);
        }

        // Crear nombre del backup con timestamp
        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
        String backupFileName = "inversiones_backup_" + timestamp + ".zip";
        String backupFilePath = new File(BACKUP_DIR, backupFileName).getAbsolutePath();

        // Usar comando SQL BACKUP de H2 que es seguro para BD en uso
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Comando BACKUP de H2 crea un archivo ZIP
            String backupCommand = String.format("BACKUP TO '%s'", backupFilePath);
            logger.info("Ejecutando comando de backup H2: {}", backupCommand);
            
            stmt.execute(backupCommand);
            
            logger.info("Backup SQL ejecutado exitosamente");
            
            // Obtener tamaño del archivo creado
            File backupFile = new File(backupFilePath);
            if (backupFile.exists()) {
                long fileSize = backupFile.length();
                logger.info("Archivo de backup creado: {} ({} bytes)", backupFilePath, fileSize);
                
                // Crear archivo de informacion del backup
                createBackupInfoFile(timestamp, fileSize);
            } else {
                throw new Exception("No se creo el archivo de backup en: " + backupFilePath);
            }
            
        } catch (Exception e) {
            logger.error("Error al ejecutar comando BACKUP de H2", e);
            throw new Exception("Error al realizar el backup de la base de datos: " + e.getMessage(), e);
        }
    }

    /**
     * Extrae la ruta de la base de datos desde la URL de conexion.
     * 
     * @return ruta de la base de datos sin extension
     */
    private String extractDatabasePath() {
        // Formato esperado: jdbc:h2:file:./data/inversiones
        String url = datasourceUrl;
        if (url.contains("jdbc:h2:file:")) {
            return url.replace("jdbc:h2:file:", "");
        }
        // Por defecto, si no se encuentra el patron
        return "./data/inversiones";
    }

    /**
     * Crea un archivo de informacion del backup con detalles de la operacion.
     * 
     * @param timestamp timestamp del backup
     * @param fileSize tamano del archivo de base de datos
     * @throws IOException si hay error al crear el archivo
     */
    private void createBackupInfoFile(String timestamp, long fileSize) throws IOException {
        String infoFileName = "inversiones_backup_" + timestamp + ".info";
        File infoFile = new File(BACKUP_DIR, infoFileName);

        StringBuilder info = new StringBuilder();
        info.append("Backup Information\n");
        info.append("==================\n");
        info.append("Timestamp: ").append(timestamp).append("\n");
        info.append("Database: inversiones\n");
        info.append("Source Size: ").append(formatFileSize(fileSize)).append("\n");
        info.append("Backup Date: ").append(LocalDateTime.now()).append("\n");
        info.append("Status: SUCCESS\n");
        info.append("Backup Type: H2 SQL BACKUP (ZIP Format)\n");
        info.append("Safe While Running: YES\n");

        Files.write(infoFile.toPath(), info.toString().getBytes());
        logger.info("Archivo de informacion del backup creado: {}", infoFile.getAbsolutePath());
    }

    /**
     * Formatea el tamano del archivo en formato legible.
     * 
     * @param bytes tamano en bytes
     * @return tamano formateado (KB, MB, GB)
     */
    private String formatFileSize(long bytes) {
        if (bytes <= 0) return "0 B";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));
        return String.format("%.2f %s", bytes / Math.pow(1024, digitGroups), units[digitGroups]);
    }
}