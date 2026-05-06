# Documentacion: Sistema de Backup Programado para Base de Datos H2

## Descripcion General

Se ha implementado un sistema completo de copias de seguridad programadas para la base de datos H2 de la aplicacion. El sistema realiza backups automaticos cada 15 dias y permite ejecutar backups bajo demanda a traves de una API REST.

## Caracteristicas

- **Backups Automaticos**: Se ejecutan automaticamente cada 15 dias a las 00:00:00 (medianoche)
- **Backups Bajo Demanda**: Pueden ser lanzados manualmente a traves de endpoints REST
- **Almacenamiento**: Los backups se guardan en el directorio `data/backups`
- **Archivos Generados**: Para cada backup se crean:
  - Archivo principal de base de datos (*.mv.db)
  - Archivo trace si existe (*.trace.db)
  - Archivo de informacion (*.info)
- **Logging**: Todos los eventos de backup se registran en los logs con informacion detallada
- **Manejo de Errores**: El sistema captura y registra excepciones sin detener la aplicacion

## Archivos Modificados/Creados

### 1. ScheduledBackup.java
Ubicacion: `src/main/java/com/inversiones/backup/ScheduledBackup.java`

Clase de servicio que contiene la logica principal de backup:
- `@Scheduled(cron = "0 0 0 */15 * *")`: Programacion automatica cada 15 dias
- `executeBackupOnDemand()`: Metodo publico para backup bajo demanda
- `performBackup()`: Realiza la copia de seguridad
- `extractDatabasePath()`: Extrae la ruta de la BD desde la configuracion
- `copyFile()`: Copia archivos de forma eficiente con buffer
- `createBackupInfoFile()`: Crea archivo de informacion del backup
- `formatFileSize()`: Formatea el tamano de archivos en formato legible

### 2. BackupController.java (NUEVO)
Ubicacion: `src/main/java/com/inversiones/backup/BackupController.java`

Controlador REST que expone endpoints para:
- `POST /api/backup/execute`: Ejecuta un backup bajo demanda
- `GET /api/backup/status`: Obtiene el estado del servicio de backup

### 3. InversionesWebAppApplication.java
Ubicacion: `src/main/java/com/inversiones/InversionesWebAppApplication.java`

Clase principal de la aplicacion. Se ha anadido:
- Import: `org.springframework.scheduling.annotation.EnableScheduling`
- Anotacion: `@EnableScheduling` en la clase

## Endpoints REST Disponibles

### Ejecutar Backup Bajo Demanda

**Metodo**: POST  
**URL**: `http://localhost:8080/inversiones/api/backup/execute`  
**Respuesta Exitosa** (200 OK):
```json
{
    "status": "SUCCESS",
    "message": "Backup realizado exitosamente",
    "timestamp": 1715000000000
}
```

**Respuesta Error** (500 Internal Server Error):
```json
{
    "status": "ERROR",
    "message": "Error al realizar el backup: [detalles del error]",
    "timestamp": 1715000000000
}
```

### Obtener Estado del Servicio

**Metodo**: GET  
**URL**: `http://localhost:8080/inversiones/api/backup/status`  
**Respuesta** (200 OK):
```json
{
    "serviceActive": true,
    "scheduledBackupEnabled": true,
    "scheduleDescription": "Cada 15 dias a las 00:00:00",
    "backupDirectory": "data/backups",
    "message": "Servicio de backup disponible"
}
```

## Configuracion

La configuracion de la base de datos se obtiene del archivo `application.properties`:

```properties
spring.datasource.url=jdbc:h2:file:./data/inversiones
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver
```

El sistema extrae automaticamente la ruta de la base de datos desde esta configuracion.

## Estructura de Directorios de Backup

```
data/backups/
  inversiones_backup_2026-05-06_10-30-45.mv.db
  inversiones_backup_2026-05-06_10-30-45.trace.db
  inversiones_backup_2026-05-06_10-30-45.info
  ...
```

Cada backup genera tres archivos con un timestamp unico para identificarlos.

## Ejemplo de Archivo .info

```
Backup Information
==================
Timestamp: 2026-05-06_10-30-45
Database: inversiones
Source Size: 45.32 MB
Backup Date: 2026-05-06T10:30:45
Status: SUCCESS
```

## Logs

Los eventos se registran en los logs de la aplicacion con el nivel INFO o ERROR:

```
Iniciando backup programado...
Directorio de backups creado: data/backups
Archivo de base de datos copiado: data/backups/inversiones_backup_2026-05-06_10-30-45.mv.db
Archivo trace copiado: data/backups/inversiones_backup_2026-05-06_10-30-45.trace.db
Archivo de informacion del backup creado: data/backups/inversiones_backup_2026-05-06_10-30-45.info
Backup programado completado exitosamente
```

## Expresion Cron Explicada

`0 0 0 */15 * *`

- Campo 1: Segundo (0 = segundo 0)
- Campo 2: Minuto (0 = minuto 0)
- Campo 3: Hora (0 = hora 0 = medianoche)
- Campo 4: Dia del mes (*/15 = cada 15 dias)
- Campo 5: Mes (* = todos los meses)
- Campo 6: Dia de la semana (* = todos los dias)

**Nota**: La expresion cron comienza a contar desde el 1 de enero, por lo que se ejecutara en los dias: 1, 16 de enero, 1, 16 de febrero, etc.

## Consideraciones Importantes

1. **Inicio de la Aplicacion**: El primer backup programado se ejecutara 15 dias despues del inicio de la aplicacion.

2. **Persistencia**: Los backups se guardan en el directorio del sistema de archivos, lo que permite su restauracion incluso si la aplicacion se reinicia.

3. **Espacio en Disco**: Asegurate de que haya suficiente espacio en disco en el directorio `data/` para almacenar los backups.

4. **Permisos**: El usuario que ejecuta la aplicacion debe tener permisos de escritura en el directorio `data/backups`.

5. **Performance**: La operacion de backup no bloquea otras operaciones de la aplicacion gracias al uso de `@Scheduled`.

## Prueba de Funcionamiento

Para probar manualmente el sistema de backup:

1. Abre una herramienta como **Postman** o **curl**
2. Realiza una peticion POST a `http://localhost:8080/inversiones/api/backup/execute`
3. Verifica que la respuesta sea exitosa
4. Comprueba que se han creado archivos en `data/backups`
5. Revisa los logs de la aplicacion para confirmar la ejecucion

Ejemplo con curl:
```bash
curl -X POST http://localhost:8080/inversiones/api/backup/execute
```

## Futuras Mejoras

- Implementar politica de retencion de backups (eliminar backups antiguos)
- Agregar compresion de archivos de backup
- Implementar backup incremental
- Agregar email de notificacion cuando fallan los backups
- Crear interfaz web para visualizar y descargar backups
