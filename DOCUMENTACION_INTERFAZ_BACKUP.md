# Documentacion: Interfaz Web de Backup

## Descripcion General

Se ha creado una interfaz web completa para gestionar copias de seguridad de la base de datos. Esta interfaz permite:

- Crear nuevos backups bajo demanda
- Visualizar el estado del servicio de backup
- Listar todos los backups disponibles
- Descargar backups existentes
- Eliminar backups
- Ver informacion detallada de cada backup

## Cambios Realizados

### 1. Menu Principal (menu.html)
- Anadido enlace "💾 Copia de seguridad" en el menu principal
- Disponible en desktop y version movil

### 2. Pagina de Inicio (index.html)
- Anadido boton "Copia de seguridad" en el panel principal
- Acceso rapido desde la pagina de inicio

### 3. Pagina de Backup (backup/lista.html) - NUEVA
- Interfaz completa para gestionar backups
- Ubicacion: `src/main/resources/templates/backup/lista.html`
- Caracteristicas:
  - Boton "Crear Backup Ahora" con efecto visual de carga
  - Seccion de estado del servicio (hora de ejecucion, estado, directorio)
  - Tabla de backups disponibles
  - Acciones para cada backup: Descargar, Ver informacion, Eliminar
  - Mensajes de estado (exito/error) con desaparicion automatica
  - Modal para visualizar informacion del backup
  - Diseno responsive para movil y desktop

### 4. Controladores

#### BackupController (Modificado)
- URL Base: `/backup`
- Gestiona vistas y descargas de archivos
- Endpoints:
  - `GET /backup` - Muestra la pagina de backup
  - `GET /backup/list` - Lista backups en JSON
  - `GET /backup/download/{filename}` - Descarga un backup
  - `DELETE /backup/delete/{filename}` - Elimina un backup

#### BackupRestController (NUEVO)
- URL Base: `/api/backup`
- Maneja operaciones REST de backup
- Endpoints:
  - `POST /api/backup/execute` - Ejecuta un backup bajo demanda
  - `GET /api/backup/status` - Obtiene estado del servicio

#### BackupService (NUEVO)
- Servicio para gestionar archivos de backup
- Metodos principales:
  - `listBackups()` - Lista todos los backups con informacion
  - `getBackupFile(filename)` - Obtiene un archivo para descargar
  - `deleteBackup(filename)` - Elimina un backup y sus archivos asociados

## Flujo de la Interfaz

### Crear Backup
1. Usuario hace clic en "Crear Backup Ahora"
2. Se envía POST a `/inversiones/api/backup/execute`
3. Se muestra spinner de carga
4. Tras completarse, se recarga la lista de backups
5. Se muestra mensaje de exito o error

### Ver Backups
1. Al cargar la pagina, se obtiene la lista de backups vía `/inversiones/backup/list`
2. Se muestran en una tabla con:
   - Nombre del archivo
   - Tamano (formateado)
   - Fecha y hora
   - Botones de accion

### Descargar Backup
1. Usuario hace clic en "Descargar"
2. Se descarga vía `/inversiones/backup/download/{filename}`
3. Navegador maneja la descarga automaticamente

### Ver Informacion
1. Usuario hace clic en "Info"
2. Se abre modal con contenido del archivo .info
3. Muestra detalles del backup

### Eliminar Backup
1. Usuario hace clic en "Eliminar"
2. Confirmacion de dialogo
3. Se envía DELETE a `/inversiones/backup/delete/{filename}`
4. Se eliminan archivos .mv.db, .trace.db (si existe) e .info
5. Se recarga la lista

## Rutas de Acceso

### Desde el Menu
- Click en "💾 Copia de seguridad" en el menu principal

### Desde el Inicio
- Click en boton "Copia de seguridad"

### URL Directa
- `http://localhost:8080/inversiones/backup`

## Archivos Creados/Modificados

### Nuevos
- `src/main/resources/templates/backup/lista.html` - Interfaz principal
- `src/main/java/com/inversiones/backup/BackupService.java` - Servicio de gestion
- `src/main/java/com/inversiones/backup/BackupRestController.java` - Controlador REST
- `src/main/java/com/inversiones/backup/BackupController.java` - Controlador principal (modificado)

### Modificados
- `src/main/resources/templates/menu.html` - Anadido enlace en menu
- `src/main/resources/templates/index.html` - Anadido boton en inicio

## Tecnologias Utilizadas

- **HTML5**: Estructura semántica
- **CSS3**: Estilos modernos y responsive
- **JavaScript Vanilla**: Interacciones sin dependencias externas
- **Bootstrap 5**: Framework CSS
- **Fetch API**: Llamadas HTTP a endpoints REST
- **Thymeleaf**: Plantillas server-side
- **Spring MVC**: Controladores web
- **Spring REST**: Endpoints JSON

## Validacion de Seguridad

- Prevención de directory traversal en descargas/eliminaciones
- Validación de nombres de archivo
- Solo se permiten archivos .mv.db, .trace.db e .info
- Los archivos se validan antes de cualquier operacion

## Estilos y Diseno

- Paleta de colores: Purpura/Azul (#667eea - #764ba2)
- Gradientes modernos
- Efectos hover suave
- Responsive design
- Animaciones de carga (spinner)
- Mensajes de estado con auto-desaparicion

## Validacion de Errores

- Manejo de excepciones en todos los endpoints
- Mensajes de error descriptivos
- Modal de confirmacion para operaciones destructivas
- Estados visuales claros (cargando, exito, error)

## Mejoras Futuras

- Compresion de backups (ZIP)
- Programacion de backups adicionales
- Restauracion desde backup
- Estadisticas de backups (cantidad, tamano total, frecuencia)
- Notificaciones por email
- Backup a almacenamiento en la nube
- Historial de operaciones
- Filtrado y busqueda de backups por fecha
