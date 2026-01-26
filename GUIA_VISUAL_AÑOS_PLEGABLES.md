# 📊 GUÍA VISUAL - Años Plegables en Resumen Mensual

## 🎬 Vista General

```
╔═══════════════════════════════════════════════════════════════════╗
║  📊 Resumen Mensual de Rendimientos           [← Volver]        ║
╚═══════════════════════════════════════════════════════════════════╝

┌───────────────────────────────────────────────────────────────────┐
│ 📈 Resumen Total                                                  │
├───────────────────────────────────────────────────────────────────┤
│  Total Distribuido: $450,000.00                                   │
│  Total Retenido: $135,000.00                                      │
│  Total Reinvertido: $225,000.00                                   │
│  Total Reclamado: $90,000.00                                      │
└───────────────────────────────────────────────────────────────────┘

[📂 Expandir Todo]  [📁 Contraer Todo]

┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
┃ ▼ 📅 2025                                                      ┃
┃  Distribuido: $250,000 | Retenido: $75,000 | Reinvertido:    ┃
┃  $125,000 | Reclamado: $50,000 | Registros: 52                ┃
┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
  ↓ Año expandido, mostrando meses

  ┌─────────────────────────────────────────────────────────────┐
  │ ▼ Enero 2025                                                │
  │  Distribuido: $20,000 | Retenido: $6,000 | ...  5 registros│
  └─────────────────────────────────────────────────────────────┘
    ↓ Mes expandido, mostrando tabla

    ┌───────────────────────────────────────────────────────────┐
    │ Wallet      Dividend  Distribuido  Retenido  ...         │
    ├───────────────────────────────────────────────────────────┤
    │ 0x1234...   #01/2025  $5,000       $1,500                │
    │ 0x5678...   #02/2025  $3,500       $1,050                │
    │ ... (3 más registros)
    ├───────────────────────────────────────────────────────────┤
    │ TOTAL MES   -         $20,000      $6,000                │
    └───────────────────────────────────────────────────────────┘

  ┌─────────────────────────────────────────────────────────────┐
  │ ▼ Febrero 2025                                              │
  │  Distribuido: $18,000 | Retenido: $5,400 | ...  4 registros│
  └─────────────────────────────────────────────────────────────┘
    (Tabla de febrero)

  ┌─────────────────────────────────────────────────────────────┐
  │ ▼ Marzo 2025                                                │
  │  Distribuido: $22,000 | Retenido: $6,600 | ...  6 registros│
  └─────────────────────────────────────────────────────────────┘
    (Tabla de marzo)

┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
┃ ▼ 📅 2024                                                      ┃
┃  Distribuido: $200,000 | Retenido: $60,000 | Reinvertido:    ┃
┃  $100,000 | Reclamado: $40,000 | Registros: 48                ┃
┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
  (12 meses de 2024 plegables)
```

---

## 🎯 Estados

### 1. Estado Inicial (Todo Expandido)

```
▼ 📅 2025
  Distribuido: $250,000 | ... | Registros: 52
  
  ▼ Enero 2025
    [Tabla con datos]
    
  ▼ Febrero 2025
    [Tabla con datos]
    
  ▼ Marzo 2025
    [Tabla con datos]

▼ 📅 2024
  Distribuido: $200,000 | ... | Registros: 48
  
  ▼ Enero 2024
    [Tabla con datos]
  ... más meses
```

### 2. Después de "Contraer Todo"

```
► 📅 2025
  Distribuido: $250,000 | ... | Registros: 52

► 📅 2024
  Distribuido: $200,000 | ... | Registros: 48
```

Solo se ven los encabezados de años, todo contraído.

### 3. Año Expandido, Mes Contraído

```
▼ 📅 2025
  Distribuido: $250,000 | ...
  
  ► Enero 2025
    Distribuido: $20,000 | ... | 5 registros
    
  ► Febrero 2025
    Distribuido: $18,000 | ... | 4 registros
    
  ► Marzo 2025
    Distribuido: $22,000 | ... | 6 registros
```

Se ven los meses pero no las tablas.

### 4. Año y Mes Expandido

```
▼ 📅 2025
  Distribuido: $250,000 | ...
  
  ▼ Enero 2025
    Distribuido: $20,000 | ... | 5 registros
    
    ┌─────────────────┐
    │ [Tabla datos]   │
    └─────────────────┘
    
  ► Febrero 2025
    Distribuido: $18,000 | ...
    
  ► Marzo 2025
    Distribuido: $22,000 | ...
```

Se ve la tabla de enero expandida.

---

## 🖱️ Interactividad

### Click en Año (▼)

**Antes:**
```
▼ 📅 2025
  [Meses visibles]
```

**Después (Click):**
```
► 📅 2025
  [Meses ocultos]
```

El icono rota de ▼ a ► y los meses desaparecen.

### Click en Mes (▼)

**Antes:**
```
▼ Enero 2025
  [Tabla visible]
```

**Después (Click):**
```
► Enero 2025
  [Tabla oculta]
```

El icono rota de ▼ a ► y la tabla desaparece.

### Click en "Expandir Todo"

```
Antes:
► 📅 2025
► 📅 2024

Después:
▼ 📅 2025
  ▼ Enero 2025
    [Tabla]
  ▼ Febrero 2025
    [Tabla]
  ...
▼ 📅 2024
  ▼ Enero 2024
    [Tabla]
  ...
```

Todo se abre automáticamente.

### Click en "Contraer Todo"

```
Antes:
▼ 📅 2025
  ▼ Enero 2025
    [Tabla]

Después:
► 📅 2025
► 📅 2024
```

Todo se cierra automáticamente.

---

## 📊 Encabezado de Año (Clickeable)

```
┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
┃ ▼ 📅 2025        Distribuido: $250K | Retenido: $75K     ┃
┃                  Reinvertido: $125K | Reclamado: $50K    ┃
┃                  Registros: 52                            ┃
┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
  └─→ Click aquí para expandir/contraer este año
```

**Información mostrada:**
- Icono plegable (▼/►)
- Año (2025)
- Total Distribuido del año
- Total Retenido del año
- Total Reinvertido del año
- Total Reclamado del año
- Cantidad de registros en el año

---

## 📊 Encabezado de Mes (Clickeable)

```
┌─────────────────────────────────────────────────────────────┐
│ ▼ Enero 2025                                                │
│  Distribuido: $20K | Retenido: $6K | Reinvertido: $10K    │
│  Reclamado: $4K | 5 registros                              │
└─────────────────────────────────────────────────────────────┘
  └─→ Click aquí para expandir/contraer este mes
```

**Información mostrada:**
- Icono plegable (▼/►)
- Mes y año
- Total Distribuido del mes
- Total Retenido del mes
- Total Reinvertido del mes
- Total Reclamado del mes
- Cantidad de registros en el mes

---

## 🎨 Colores y Estilos

### Encabezado de Año
- **Fondo**: Gradiente azul-púrpura (#667eea → #764ba2)
- **Texto**: Blanco
- **Hover**: Gradiente invertido
- **Cursor**: Pointer (manita)

### Encabezado de Mes
- **Fondo**: Gradiente azul-púrpura más claro
- **Texto**: Blanco
- **Hover**: Gradiente invertido
- **Cursor**: Pointer (manita)

### Tabla
- **Encabezados**: Gris claro con texto azul
- **Datos**: Blanco con hover gris
- **Totales**: Fondo azul claro con texto azul oscuro

### Iconos
- **Expandido**: ▼ (hacia abajo)
- **Contraído**: ► (hacia la derecha)
- **Animación**: Rotación suave (0.3s)

---

## 📱 En Móviles

```
Encabezado de año se adapta:

▼ 📅 2025
Distribuido: $250K
Retenido: $75K
Reinvertido: $125K
Reclamado: $50K
Registros: 52
```

Los datos se apilan verticalmente.

---

## 🔄 Flujo de Usuario Típico

1. **Abre la página**
   - Ve todos los años expandidos
   - Ve todos los meses expandidos
   - Las tablas están visibles

2. **Quiere ver solo 2024**
   - Click en "Contraer Todo"
   - Contrae todo

3. **Click en año 2024**
   - Se expande solo 2024
   - Sus meses se expanden
   - Todas las tablas de 2024 visibles

4. **Quiere ver solo un mes**
   - Click en meses de 2024 que no quiere
   - Se contraen esos meses
   - Solo quedan las tablas deseadas

5. **Quiere verlo todo de nuevo**
   - Click en "Expandir Todo"
   - Todo se abre automáticamente

---

## ✨ Animaciones

### Rotación de Icono
- Cuando se expande: El icono rota suavemente
- Duración: 0.3 segundos
- Efecto: transform: rotate(-90deg)

### Fade de Contenido
- Cuando se contrae: Desaparece inmediatamente
- Cuando se expande: Aparece inmediatamente
- Cambio: display: none / display: block

---

**Así funciona la interfaz plegable de años y meses** ✨
