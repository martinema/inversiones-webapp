# 📊 GUÍA DE USO - Resumen Mensual de Rendimientos

## 🎯 ¿Qué es?

Una nueva página que muestra los rendimientos agrupados por meses en una **tabla dinámica similar a Excel**, con totales automáticos por mes y resumen general.

---

## 🚀 Cómo Acceder

### Opción 1: Desde la página de Rendimientos
1. Ve a **Rendimientos** (menú lateral o navegación)
2. Haz clic en el botón **📊 Resumen Mensual**
3. ¡Listo! Se carga la página con el resumen

### Opción 2: URL Directa
```
/inversiones/rendimiento/resumenMensual
```

### Opción 3: Menú Lateral (Próxima mejora)
Se puede agregar un enlace directo al menú lateral

---

## 📊 Qué Ves en la Página

### 1. Encabezado
```
📊 Resumen Mensual de Rendimientos    [← Volver a Rendimientos]
```
- Título descriptivo
- Botón para volver a la lista de rendimientos

### 2. Resumen General (Parte Superior)
```
Total Distribuido: €450,000.00
Total Retenido: €135,000.00
Total Reinvertido: €225,000.00
Total Reclamado: €90,000.00
```
Muestra los totales de **todos los meses juntos**

### 3. Tablas por Mes
Para cada mes hay:
- **Encabezado**: Mes, año y cantidad de registros
- **Tabla**: Datos de todos los rendimientos del mes
- **Totales**: Suma de cada columna al final de la tabla

---

## 📋 Estructura de Cada Tabla de Mes

### Encabezados de Columnas
| Columna | Descripción |
|---------|-------------|
| **Wallet** | Dirección de wallet (primeros 10 caracteres) |
| **Distribuido** | Cantidad distribuida (€) |
| **Retenido** | Cantidad retenida (€) |
| **Reinvertido** | Cantidad reinvertida (€) |
| **Reclamado** | Cantidad reclamada (€) |
| **Hash ID** | ID de hash (primeros 20 caracteres) |

### Datos de Ejemplo
```
Wallet       │ Distribuido │ Retenido │ Reinvertido │ Reclamado │ Hash ID
─────────────┼─────────────┼──────────┼─────────────┼───────────┼─────────
0x1234...    │ €5,000.00   │ €1,500   │ €2,500      │ €1,000    │ 0x98765...
0x5678...    │ €3,500.00   │ €1,050   │ €1,750      │ €700      │ 0x54321...
...          │ ...         │ ...      │ ...         │ ...       │ ...
TOTAL MES    │ €60,000.00  │ €18,000  │ €30,000.00  │ €12,000   │ 
```

---

## 🔍 Características

### ✅ Agrupación Automática
- Los rendimientos se agrupan automáticamente por mes
- Se ordena de forma cronológica (más antiguos primero)
- Cada mes es una tabla independiente

### ✅ Cálculos Automáticos
- **Totales por mes**: Se suman automáticamente
- **Contador de registros**: Muestra cuántos registros hay por mes
- **Resumen general**: Muestra totales de todos los meses

### ✅ Formato Excel
- Tablas limpias y organizadas
- Totales en fila separada
- Números con 2 decimales
- Moneda en euros (€)

### ✅ Valores Dinámicos
- Si un valor es 0: Se muestra en gris
- Si un valor es positivo: Se muestra en verde
- Número de decimales: Siempre 2

### ✅ Responsive
- Funciona en desktop
- Funciona en tablet
- Funciona en móvil (tabla scrolleable)

---

## 📱 Vista en Diferentes Dispositivos

### Desktop
```
Tabla completa, horizontal
Todas las columnas visibles
Cómodo para ver datos
```

### Tablet
```
Tabla scrolleable horizontalmente
Columnas se adaptan
Sigue siendo legible
```

### Móvil
```
Tabla muy compacta
Scrolleable horizontalmente
Botones y texto adaptados
```

---

## 🎨 Colores y Estilos

### Encabezado de Mes
- **Fondo**: Gradiente azul-púrpura
- **Texto**: Blanco
- **Icono**: 📅
- **Información**: Mes, año, cantidad de registros

### Tabla
- **Encabezados**: Fondo gris, texto azul
- **Datos**: Fondo blanco, hover gris claro
- **Totales**: Fondo azul claro, texto azul oscuro

### Valores
- **Positivos (>0)**: Verde (#28a745)
- **Cero (0)**: Gris (#6c757d)
- **Moneda**: Siempre con €

---

## 💡 Casos de Uso

### 📈 Análisis Mensual
**Pregunta**: ¿Cuánto gané en distribuido cada mes?
**Solución**: 
1. Abre Resumen Mensual
2. Busca cada mes
3. Mira fila TOTAL MES en columna "Distribuido"
4. Compara entre meses

### 📊 Comparativa
**Pregunta**: ¿Cuál fue mi mejor mes?
**Solución**:
1. Abre Resumen Mensual
2. Mira la fila TOTAL MES de cada mes
3. Suma todos los valores (Distrib. + Reten. + Reinvert. + Reclam.)
4. Compara totales

### 🔍 Auditoria
**Pregunta**: ¿Cuántos registros tengo en enero?
**Solución**:
1. Abre Resumen Mensual
2. Busca enero
3. Mira el contador: "15 registros"

### 📑 Seguimiento
**Pregunta**: ¿Cuál es mi total en todos los meses?
**Solución**:
1. Abre Resumen Mensual
2. Mira "Resumen Total" arriba
3. Ve los 4 totales principales

---

## 🔢 Cálculos Explicados

### Total de un Mes
```
Enero 2025:
Registro 1: €5,000 + €1,500 + €2,500 + €1,000 = €10,000
Registro 2: €3,500 + €1,050 + €1,750 + €700   = €7,000
...
TOTAL MES: Suma de todos = €60,000
```

### Total General
```
Enero:    €60,000
Febrero:  €48,000
Marzo:    €52,000
...
TOTAL:    €450,000  ← Suma de todos los meses
```

---

## ⚙️ Funcionalidades Técnicas

### Agrupación
- **Método**: YearMonth (mes-año)
- **Orden**: Cronológico ascendente
- **Contenedor**: LinkedHashMap (mantiene orden)

### Datos Mostrados
```
Wallet:        Primeros 10 caracteres + "..."
Hash ID:       Primeros 20 caracteres + "..."
Valores:       Formato número con 2 decimales
Moneda:        Euros (€)
```

### Totales Calculados
- Distribuido: SUM(Distribuido por mes)
- Retenido: SUM(Retenido por mes)
- Reinvertido: SUM(Reinvertido por mes)
- Reclamado: SUM(Reclamado por mes)
- Contador: COUNT(Registros por mes)

---

## ❓ Preguntas Frecuentes

### P: ¿Por qué veo "0.00" en algunos valores?
R: Significa que no hay valor registrado para ese rendimiento en esa categoría.

### P: ¿Puedo exportar los datos?
R: No aún. Puedes copiar la tabla o tomar una captura de pantalla.

### P: ¿Los datos se actualizan automáticamente?
R: Sí. Si agregas nuevos rendimientos, aparecerán en la próxima vez que abras la página.

### P: ¿Puedo filtrar por mes?
R: No aún. Se muestra todo. Puedes usar Ctrl+F para buscar en la página.

### P: ¿Por qué algunos datos están truncados?
R: Para que la tabla no sea demasiado ancha. Pasa el mouse para ver el valor completo.

### P: ¿Hay decimales limitados?
R: Sí, siempre 2 decimales para consistencia.

### P: ¿Funciona en móvil?
R: Sí, pero la tabla es scrolleable horizontalmente.

---

## 🎯 Comparativa: Antes vs Después

### ANTES (Sin Resumen Mensual)
```
- Veías todos los rendimientos en lista
- Difícil ver totales por mes
- Necesitabas calcular manualmente
- Muchas filas que ver
```

### DESPUÉS (Con Resumen Mensual)
```
✅ Datos agrupados por mes
✅ Totales calculados automáticamente
✅ Fácil comparar entre meses
✅ Resumen general visible
✅ Tabla dinámica tipo Excel
```

---

## 🔄 Integración

La nueva página se integra perfectamente con:
- ✅ Menú lateral (funciona)
- ✅ Estilos consistentes (Bootstrap 5)
- ✅ Botón de volver
- ✅ Datos en tiempo real

---

## 📞 Soporte

Si encuentras algún problema:
1. Recarga la página (F5)
2. Limpia el caché (Ctrl+Shift+Del)
3. Intenta en otro navegador
4. Contacta a soporte si persiste

---

## ✨ Mejoras Futuras

Posibles mejoras que se pueden agregar:
- [ ] Exportar a Excel
- [ ] Exportar a PDF
- [ ] Filtro por rango de fechas
- [ ] Gráficos de barras por mes
- [ ] Comparativa año a año
- [ ] Búsqueda por wallet
- [ ] Ordenamiento por columnas
- [ ] Agregar al menú lateral

---

## 🎓 Ejemplos Prácticos

### Ejemplo 1: Ver el mes más rentable
1. Abre Resumen Mensual
2. Busca las filas "TOTAL MES" de cada mes
3. Compara el total de "Distribuido" entre meses
4. Identifica el mes con mayor distribuido

**Resultado**: Puedes ver rápidamente cuál fue tu mejor mes

### Ejemplo 2: Auditar ingresos
1. Abre Resumen Mensual
2. Mira el "Resumen Total" arriba
3. Suma: Distribuido + Retenido + Reinvertido + Reclamado
4. Ese es tu ingreso total

**Resultado**: Tienes una auditoría completa de tus ingresos

### Ejemplo 3: Seguimiento mensual
1. Abre Resumen Mensual todos los meses
2. Toma nota de los totales
3. Crea tu propia hoja de seguimiento
4. Detecta tendencias

**Resultado**: Puedes ver si tus ingresos aumentan o disminuyen

---

## 📊 Formato de Datos

Cada fila muestra:
```json
{
  "wallet": "0x1234...",
  "distributed": 5000.00,
  "retained": 1500.00,
  "reinvested": 2500.00,
  "claimed": 1000.00,
  "hashid": "0x98765...",
  "date": "2025-01-15"
}
```

---

## 🌍 Disponibilidad

- **Desktop**: ✅ Totalmente optimizado
- **Tablet**: ✅ Funcional con scroll
- **Mobile**: ✅ Responsive
- **Navegadores**: ✅ Chrome, Firefox, Safari, Edge
- **Idioma**: 🇪🇸 Español

---

**¡Disfruta del Resumen Mensual!** 📊✨
