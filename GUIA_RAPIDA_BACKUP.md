# GUÍA RÁPIDA: SISTEMA DE BACKUP

## 🚀 INICIO RÁPIDO

### Acceder a la Interfaz de Backup

**Opción 1: Desde el Menú**
```
Página Principal → Menú Superior → 💾 Copia de seguridad
```

**Opción 2: Desde la Página de Inicio**
```
http://localhost:8080/inversiones/
↓
Click en botón "Copia de seguridad"
```

**Opción 3: URL Directa**
```
http://localhost:8080/inversiones/backup
```

---

## 📋 TAREAS COMUNES

### ✅ Crear un Backup Ahora

```
1. Ve a la página de backup
2. Haz click en botón: "🔄 Crear Backup Ahora"
3. Espera a que aparezca el mensaje de confirmación
4. Verifica que el backup aparezca en la tabla
```

**Resultado esperado:**
- Mensaje verde: "✓ Backup realizado exitosamente"
- Nuevo archivo en la tabla de backups
- Archivo guardado en: `data/backups/inversiones_backup_TIMESTAMP.mv.db`

---

### 📥 Descargar un Backup

```
1. Localiza el backup en la tabla
2. Haz click en botón azul: "📥 Descargar"
3. El navegador descargará el archivo
```

**Archivo descargado:**
- Nombre: `inversiones_backup_YYYY-MM-DD_HH-mm-ss.mv.db`
- Ubicación: Tu carpeta de descargas
- Tamaño: Depende de la base de datos

---

### ℹ️ Ver Información del Backup

```
1. Localiza el backup en la tabla
2. Haz click en botón azul: "ℹ️ Info"
3. Se abrirá una ventana emergente con detalles
4. Cierra haciendo click en X o fuera de la ventana
```

**Información mostrada:**
- Timestamp exacto
- Nombre de la base de datos
- Tamaño del archivo en MB/GB
- Fecha de creación
- Estado (SUCCESS/ERROR)

---

### 🗑️ Eliminar un Backup

```
1. Localiza el backup a eliminar
2. Haz click en botón rojo: "🗑️ Eliminar"
3. Confirma en el diálogo que aparece
4. El backup será eliminado de la tabla
```

**Archivos eliminados:**
- `.mv.db` (archivo principal)
- `.trace.db` (si existe)
- `.info` (metadatos)

---

### 📊 Ver Estado del Servicio

La información aparece automáticamente al cargar la página en la sección "Estado del Servicio":

```
Estado del Servicio: ✓ Activo
Backups Programados: ✓ Habilitados
Programación: Cada 15 días a las 00:00:00
Directorio: data/backups
```

---

## 🕐 HORARIOS Y FRECUENCIA

### Backup Automático

| Parámetro | Valor |
|-----------|-------|
| **Frecuencia** | Cada 15 días |
| **Hora** | 00:00:00 (medianoche) |
| **Primer backup** | 15 días después del inicio de la app |
| **Días de ejecución** | 1, 16 de cada mes |

### Backup Bajo Demanda
- Se puede ejecutar en cualquier momento
- Desde la interfaz web
- Click en "Crear Backup Ahora"

---

## 📁 UBICACIÓN DE ARCHIVOS

### Dónde se guardan los backups:
```
inversiones-webapp/
└── data/
    └── backups/
        ├── inversiones_backup_2026-05-06_10-30-45.mv.db
        ├── inversiones_backup_2026-05-06_10-30-45.trace.db
        ├── inversiones_backup_2026-05-06_10-30-45.info
        ├── inversiones_backup_2026-05-06_11-45-30.mv.db
        └── ...
```

### Base de datos H2 original:
```
inversiones-webapp/
└── data/
    ├── inversiones.mv.db
    └── inversiones.trace.db
```

---

## 🔧 FORMATO DE NOMBRES

### Patrón de nombres de archivo:
```
inversiones_backup_YYYY-MM-DD_HH-mm-ss.{tipo}
```

### Ejemplo:
```
inversiones_backup_2026-05-06_14-30-45.mv.db
inversiones_backup_2026-05-06_14-30-45.trace.db
inversiones_backup_2026-05-06_14-30-45.info
```

### Componentes:
- `YYYY` = Año (2026)
- `MM` = Mes (05 = mayo)
- `DD` = Día (06)
- `HH` = Hora (00-23)
- `mm` = Minuto (00-59)
- `ss` = Segundo (00-59)

---

## 📊 TAMAÑOS Y FORMATOS

### Tamaños mostrados en interfaz:
- **B** = Bytes
- **KB** = Kilobytes
- **MB** = Megabytes
- **GB** = Gigabytes

Ejemplo: `45.32 MB`

### Archivos de un backup típico:
- `.mv.db` = 40-50 MB (archivo principal)
- `.trace.db` = 0.01-0.1 MB (traza de errores)
- `.info` = 0.001 MB (metadatos)

---

## ⚠️ CONSIDERACIONES IMPORTANTES

### Espacio en Disco
- Cada backup ocupa aproximadamente el tamaño de la BD
- Verifica que tengas suficiente espacio en `data/backups/`
- 2-3 backups mensuales = espacio necesario para 120-150 MB

### Permisos
- La aplicación necesita permisos de escritura en `data/backups/`
- Si obtienes error de permisos, contacta al administrador

### Servidor en Ejecución
- Los backups no afectan operaciones en curso
- Se pueden crear backups mientras la app está en uso
- No se bloquea la aplicación

### Restauración
- Para restaurar, reemplaza `data/inversiones.mv.db` con un backup
- Reinicia la aplicación
- Verifica que los datos se hayan restaurado correctamente

---

## 🐛 SOLUCIÓN DE PROBLEMAS

### Problema: "Error al crear el backup"
```
✗ Solución:
  1. Verifica que data/backups/ exista
  2. Comprueba permisos de escritura
  3. Asegúrate que no hay archivos corruptos
  4. Reinicia la aplicación
```

### Problema: No aparecen backups en la tabla
```
✗ Solución:
  1. Recarga la página (F5)
  2. Verifica que data/backups/ tenga archivos
  3. Comprueba que son archivos .mv.db
  4. Abre consola del navegador para ver errores
```

### Problema: No puedo descargar un backup
```
✗ Solución:
  1. Verifica el nombre del archivo
  2. Comprueba que el archivo existe en data/backups/
  3. Intenta con otro archivo
  4. Abre consola del navegador para ver errores
```

### Problema: El servicio dice "Inactivo"
```
✗ Solución:
  1. Reinicia la aplicación
  2. Verifica que @EnableScheduling está en la clase principal
  3. Comprueba los logs para errores
  4. Contacta al administrador
```

---

## 📞 CONTACTO Y SOPORTE

Para reportar problemas o solicitar mejoras:

1. **Verificar logs**: `logs/iet-application.log`
2. **Consola del navegador**: F12 → Pestaña "Console"
3. **Contactar administrador**: [contacto]

---

## 📚 DOCUMENTACIÓN COMPLETA

Para información técnica detallada:
- `DOCUMENTACION_BACKUP.md` - Guía técnica de backup
- `DOCUMENTACION_INTERFAZ_BACKUP.md` - Guía técnica de interfaz
- `RESUMEN_IMPLEMENTACION.md` - Resumen ejecutivo

---

**Última Actualización**: 2026-05-06
**Versión**: 1.0
**Estado**: Activo ✅
