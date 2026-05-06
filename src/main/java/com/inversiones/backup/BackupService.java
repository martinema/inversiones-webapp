package com.inversiones.backup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Servicio para gestionar y listar copias de seguridad.
 * 
 * Trabaja con archivos ZIP generados por el comando BACKUP de H2.
 */
@Service
public class BackupService {

    private static final Logger logger = LoggerFactory.getLogger(BackupService.class);
    private static final String BACKUP_DIR = "data/backups";

    /**
     * Obtiene la lista de backups disponibles.
     * 
     * @return lista de informacion de backups
     */
    public List<BackupInfo> listBackups() {
        List<BackupInfo> backups = new ArrayList<>();

        try {
            File backupDir = new File(BACKUP_DIR);
            if (!backupDir.exists()) {
                logger.warn("Directorio de backups no existe: {}", BACKUP_DIR);
                return backups;
            }

            File[] files = backupDir.listFiles();
            if (files == null || files.length == 0) {
                return backups;
            }

            // Agrupar archivos por backup (zip + info)
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".zip")) {
                    BackupInfo info = createBackupInfo(file);
                    backups.add(info);
                }
            }

            // Ordenar por fecha descendente (mas nuevos primero)
            backups.sort(Comparator.comparing(BackupInfo::getLastModified).reversed());

        } catch (Exception e) {
            logger.error("Error al listar backups", e);
        }

        return backups;
    }

    /**
     * Crea informacion de un backup basada en el archivo .zip.
     * 
     * @param zipFile archivo .zip del backup
     * @return informacion del backup
     */
    private BackupInfo createBackupInfo(File zipFile) {
        BackupInfo info = new BackupInfo();
        
        String filename = zipFile.getName();
        info.setFilename(filename);
        
        // Obtener tamano
        long size = zipFile.length();
        info.setSize(formatFileSize(size));
        info.setRawSize(size);
        
        // Obtener fecha
        long lastModified = zipFile.lastModified();
        info.setLastModified(lastModified);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        info.setDate(sdf.format(new Date(lastModified)));
        
        // Intentar cargar el archivo .info si existe
        String infoFilename = filename.replace(".zip", ".info");
        File infoFile = new File(BACKUP_DIR, infoFilename);
        if (infoFile.exists()) {
            try {
                String infoContent = new String(Files.readAllBytes(infoFile.toPath()));
                info.setInfo(infoContent);
            } catch (IOException e) {
                logger.warn("Error al leer archivo .info: {}", infoFile.getAbsolutePath(), e);
                info.setInfo("No disponible");
            }
        } else {
            info.setInfo("No disponible");
        }
        
        return info;
    }

    /**
     * Obtiene un archivo de backup para descargar.
     * 
     * @param filename nombre del archivo
     * @return archivo del backup
     */
    public File getBackupFile(String filename) {
        // Validar que el nombre sea seguro (prevenir directory traversal)
        if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
            logger.warn("Intento de acceso a archivo invalido: {}", filename);
            return null;
        }

        File file = new File(BACKUP_DIR, filename);
        
        if (!file.exists() || !file.isFile()) {
            logger.warn("Archivo de backup no encontrado: {}", filename);
            return null;
        }

        return file;
    }

    /**
     * Elimina un archivo de backup.
     * 
     * @param filename nombre del archivo
     * @return true si fue eliminado exitosamente
     */
    public boolean deleteBackup(String filename) {
        // Validar que el nombre sea seguro
        if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
            logger.warn("Intento de eliminacion de archivo invalido: {}", filename);
            return false;
        }

        try {
            File zipFile = new File(BACKUP_DIR, filename);
            
            if (!zipFile.exists()) {
                logger.warn("Archivo de backup no encontrado: {}", filename);
                return false;
            }

            // Eliminar archivos relacionados
            String baseName = filename.replace(".zip", "");
            File infoFile = new File(BACKUP_DIR, baseName + ".info");

            boolean deleted = zipFile.delete();
            if (infoFile.exists()) {
                infoFile.delete();
            }

            if (deleted) {
                logger.info("Backup eliminado exitosamente: {}", filename);
            }
            return deleted;

        } catch (Exception e) {
            logger.error("Error al eliminar backup: {}", filename, e);
            return false;
        }
    }

    /**
     * Formatea el tamano del archivo en formato legible.
     * 
     * @param bytes tamano en bytes
     * @return tamano formateado
     */
    private String formatFileSize(long bytes) {
        if (bytes <= 0) return "0 B";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));
        return String.format("%.2f %s", bytes / Math.pow(1024, digitGroups), units[digitGroups]);
    }

    /**
     * Clase interna para informacion del backup.
     */
    public static class BackupInfo {
        private String filename;
        private String size;
        private long rawSize;
        private String date;
        private long lastModified;
        private String info;

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public long getRawSize() {
            return rawSize;
        }

        public void setRawSize(long rawSize) {
            this.rawSize = rawSize;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public long getLastModified() {
            return lastModified;
        }

        public void setLastModified(long lastModified) {
            this.lastModified = lastModified;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }
}
