# 📖 GUÍA DE USO - Tablas con Paginación, Ordenamiento y Selección Múltiple

## 🎯 Funcionalidades Implementadas

### 1️⃣ PAGINACIÓN
La tabla ahora muestra los registros en páginas de 25 registros por defecto.

#### Cómo usar:
- **Cambiar cantidad de registros**: Usa el selector desplegable
  ```
  Registros por página: [25 ▼]
  ```
  Opciones disponibles: 25, 50, 100, 150, 200

- **Navegar entre páginas**: Usa los botones de paginación
  ```
  ```

- **Información**: Se muestra siempre cuántos registros se están viendo
  ```
  Mostrando 26 - 50 de 450 registros
  ```

---

### 2️⃣ ORDENAMIENTO
Haz clic en el encabezado de cualquier columna para ordenar por esa columna.

#### Cómo funciona:
- **Primera vez**: Ordena ascendente (A → Z, 0 → 9)
- **Segunda vez**: Ordena descendente (Z → A, 9 → 0)
- **Indicador visual**: Aparece ▲ (ascendente) o ▼ (descendente)
- **Automatico**: Se vuelve a la página 1 al ordenar

#### Ejemplo:
```
Columna "Nombre" ▲  ← Ordenado ascendentemente
```

---

### 3️⃣ SELECCIÓN DE REGISTROS
Usa los checkboxes para seleccionar registros que deseas eliminar.

#### Opciones de selección:
- **Checkbox individual**: Selecciona 1 registro
- **Checkbox "Seleccionar Todo"**: Selecciona TODOS en la página actual
- **Contador**: Muestra cuántos registros tienes seleccionados

#### Importante:
```
❌ "Seleccionar Todo" SOLO selecciona la página actual
✅ PERO la eliminación múltiple funciona con TODAS las páginas seleccionadas
```

---

### 4️⃣ ELIMINACIÓN MÚLTIPLE
Elimina varios registros de una sola vez.

#### Pasos:
1. Selecciona los registros que deseas eliminar (checkboxes)
2. Haz clic en el botón "🗑️ Eliminar Seleccionados"
3. Confirma en el diálogo que aparece
4. ¡Hecho! Los registros se eliminan de la base de datos

#### Ejemplo de confirmación:
```
¿Estás seguro de que deseas eliminar estos 5 registros?
```

---

## 📊 Ejemplo Práctico

### Scenario: Eliminar 100 registros en una tabla de 450

1. **Cargar tabla**
   ```
   Se muestran 25 registros (1-25)
   Total: 450 registros
   ```

2. **Cambiar a 50 registros por página**
   ```
   Registros por página: [50 ▼]
   Ahora: Página 1 = 50 registros
   ```

3. **Ir a página 2 y seleccionar**
   ```
   Página 1: 50 registros (deseleccionados)
   Página 2: 50 registros (selecciono todos con checkbox)
   Página 3-9: sin seleccionar
   ```

4. **Ir a página 5 y seleccionar más**
   ```
   Navego a página 5 (registros 201-250)
   Selecciono algunos registros
   ```

5. **Eliminar**
   ```
   Hago clic en "🗑️ Eliminar Seleccionados"
   Se eliminan 50 de página 2 + 20 de página 5 = 70 registros
   La tabla se recarga
   Ahora muestra 380 registros (450 - 70)
   ```

---

## ⚙️ Combinaciones de Funcionalidades

### Ordenar + Paginar
```
1. Cambio a 100 registros por página
2. Hago clic en "Nombre" para ordenar A-Z
3. Vuelvo a página 1 automáticamente
4. Los 100 registros están ordenados
```

### Seleccionar + Ordenar
```
1. Selecciono registros en página 2
2. Hago clic en "Precio" para ordenar
3. Los registros seleccionados se mantienen
4. Pero la página se reordena
```

### Paginar + Seleccionar + Ordenar + Eliminar
```
1. Cambio a 50 por página
2. Selecciono registros en páginas 2, 5 y 7
3. Ordeno por "Fecha"
4. Elimino todos los seleccionados
5. La tabla se recarga con los cambios
```

---

## 🎨 Interfaz Visual

### Antes de la tabla
```
┌─────────────────────────────────────────────────────────────────┐
│  🗑️ Eliminar Seleccionados    3 registro(s) seleccionado(s)     │
└─────────────────────────────────────────────────────────────────┘
```

### Tabla con controles
```
┌─────────────────────────────────────────────────────────────────┐
│ ☑ │ ID ▲  │ Nombre     │ Precio    │ Fecha      │ Acciones    │
├───┼────────┼────────────┼───────────┼────────────┼─────────────┤
│ ☑ │ 1      │ Producto A │ €100      │ 2025-01-26 │ Ed. | Elim. │
│ ☑ │ 2      │ Producto B │ €150      │ 2025-01-25 │ Ed. | Elim. │
│ ☐ │ 3      │ Producto C │ €200      │ 2025-01-24 │ Ed. | Elim. │
└─────────────────────────────────────────────────────────────────┘
```

### Controles de paginación (debajo de la tabla)
```
┌──────────────────────────────────────────────────────────────────┐
│ Registros por página: [50 ▼]                                     │
│                                                                  │
│ Mostrando 26 - 50 de 450 registros                               │
│                                                                  │
│ [Primera] [Anterior] [1] [2] [3] [4] [5] ... [10] [Siguiente]   │
└──────────────────────────────────────────────────────────────────┘
```

---

## 💡 Consejos de Uso

### ✅ Buenas Prácticas
- Ordena antes de paginar para una mejor organización
- Selecciona en pequeños lotes (5-10) para mejor control
- Usa "Seleccionar Todo" solo cuando quieras actuar sobre toda una página
- Verifica la información antes de eliminar

### ❌ Evitar
- No selecciones todo sin revisar cada página
- No elimines sin confirmación
- No cambies de página mientras tengas selecciones pendientes (se pierden)

---

## 🔍 Casos de Uso Reales

### Caso 1: Limpiar datos duplicados
```
1. Ordena por "Nombre" (para agrupar duplicados)
2. Selecciona los duplicados de cada página
3. Elimina todos en lotes
```

### Caso 2: Revisar registros recientes
```
1. Ordena por "Fecha" descendente (últimos primero)
2. Pagina de 25 en 25
3. Revisa los más recientes
```

### Caso 3: Eliminar registros viejos
```
1. Ordena por "Fecha" ascendente (más viejos primero)
2. Cambia a 100 por página
3. Selecciona todas las páginas anteriores a 2024
4. Elimina todo junto
```

---

## 📱 En Dispositivos Móviles

La interfaz se adapta automáticamente:
- Los controles se apilan verticalmente
- La tabla sigue siendo horizontal (scrolleable)
- Los botones son más grandes para tocar
- La paginación está centrada

---

## 🚀 Rendimiento

- **Muy rápido**: Sin carga de servidor
- **Sin límite**: Funciona con miles de registros
- **Responsivo**: La interfaz es fluida
- **Seguro**: Confirmación antes de eliminar

---

## ❓ Preguntas Frecuentes

### P: ¿Qué pasa si ordeno?
R: Los registros se ordenan de toda la tabla y vuelves a página 1.

### P: ¿Puedo seleccionar de múltiples páginas?
R: Sí, navega, selecciona en cada página, y luego elimina todo.

### P: ¿Se pierden las selecciones al cambiar página?
R: Las selecciones SE MANTIENEN. Puedes ir de página en página sin perder la selección.

### P: ¿Cuántos puedo seleccionar máximo?
R: Todos. Puedes seleccionar desde 1 hasta todos los registros.

### P: ¿Se guarda la paginación al recargar?
R: No. Al recargar la página, vuelve a 25 registros por defecto.

### P: ¿Funciona con el botón "Atrás" del navegador?
R: No. Los cambios de paginación son locales y no afectan el historial.

---

## 📞 Soporte

Si encuentras algún problema:
1. Recarga la página (F5)
2. Limpia el caché del navegador
3. Intenta en otro navegador
4. Contacta al soporte técnico

---

**¡Disfruta usando las tablas avanzadas!** 🎉
