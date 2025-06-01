# AgendaApp

Una aplicación de gestión de tareas multiplataforma desarrollada con Kotlin Multiplatform que permite organizar y gestionar tus actividades diarias con sistema de alarmas integrado.

## 📱 Características Principales

### Gestión de Tareas
- **Creación de tareas**: Añade tareas con descripción, fecha, hora de inicio y fin
- **Sistema de prioridades**: Organiza tus tareas con 3 niveles de prioridad
- **Edición de tareas**: Modifica tareas existentes manteniendo toda la información

### Sistema de Alarmas
- **Notificaciones programadas**: Configura alarmas para recordar tus tareas
- **Persistencia de alarmas**: Las alarmas se restauran automáticamente después del reinicio del dispositivo
- **Diálogo de alarma**: Interfaz dedicada que se muestra cuando se activa una alarma

### Funcionalidades de Búsqueda
- **Búsqueda por texto**: Encuentra tareas por descripción
- **Filtrado por prioridad**: Visualiza tareas según su nivel de importancia

### Interfaz de Usuario
- **Diseño responsivo**: Soporte para orientación vertical y horizontal
- **Eliminación por deslizamiento**: Funcionalidad swipe-to-delete para gestión rápida
- **Contador de tareas pendientes**: Visualización en tiempo real de tareas por completar

## 🛠️ Tecnologías Utilizadas

### Multiplataforma
- **Kotlin Multiplatform**: Código compartido entre Android e iOS
- **Targets soportados**: Android API 23+ e iOS

### Android
- **Material Design**: Interfaz moderna con FloatingActionButton y RecyclerView
- **SQLite**: Base de datos local para persistencia 
- **AlarmManager**: Sistema de notificaciones nativo

### iOS
- **Framework nativo**: Integración con Xcode para distribución

## 📦 Estructura del Proyecto

```
AgendaApp/
├── app/                          # Módulo principal Kotlin Multiplatform
│   ├── src/main/java/app/
│   │   ├── bd/                   # Gestión de base de datos
│   │   ├── logic/                # Lógica de negocio
│   │   │   ├── entities/         # Modelos de datos
│   │   │   ├── adapters/         # Adaptadores para RecyclerView
│   │   │   ├── utils/            # Utilidades (alarmas, calendario)
│   │   │   └── callbacks/        # Callbacks para interacciones
│   │   └── ui/                   # Interfaz de usuario
│   │       ├── activities/       # Actividades principales
│   │       └── fragments/        # Fragmentos reutilizables
├── iosApp/                       # Aplicación iOS nativa
└── appC.uml                      # Diagrama UML del proyecto
```

## 🚀 Instalación y Configuración

### Requisitos Previos
- Android Studio 4.0+
- Kotlin 1.3.72+
- SDK de Android API 23+
- Xcode 11+ (para desarrollo iOS)

### Configuración Android
1. Clona el repositorio
2. Abre el proyecto en Android Studio
3. Sincroniza las dependencias de Gradle
4. Ejecuta la aplicación en un dispositivo o emulador

### Configuración iOS
1. Ejecuta la tarea `copyFramework` de Gradle
2. Abre `iosApp/iosApp.xcodeproj` en Xcode
3. Compila y ejecuta en simulador o dispositivo iOS

## 📋 Funcionalidades Detalladas

### Gestión de Tareas
La aplicación permite crear tareas completas con todos los detalles necesarios:

### Sistema de Búsqueda
Incluye un buscador avanzado que permite filtrar tareas tanto por contenido como por prioridad, facilitando la localización rápida de información específica.

### Interfaz Adaptable
La aplicación se adapta automáticamente a diferentes orientaciones de pantalla, proporcionando una experiencia de usuario optimizada en cualquier dispositivo.

## 🔧 Configuración de Permisos

La aplicación requiere los siguientes permisos:
- `READ_EXTERNAL_STORAGE`: Para acceso a archivos del sistema
- `RECEIVE_BOOT_COMPLETED`: Para restaurar alarmas después del reinicio

## 🎯 Casos de Uso Principales

1. **Planificación diaria**: Organiza tus actividades con horarios específicos
2. **Recordatorios importantes**: Configura alarmas para eventos críticos
3. **Gestión de prioridades**: Clasifica tareas según su importancia
4. **Búsqueda rápida**: Localiza tareas específicas mediante filtros
5. **Seguimiento de progreso**: Visualiza tareas pendientes en tiempo real

## 📱 Compatibilidad

- **Android**: API 23 (Android 6.0) en adelante
- **iOS**: Compatible con iPhone y iPad

---

**Nota**: Esta aplicación está diseñada como un proyecto educativo/demostrativo de Kotlin Multiplatform, ideal para aprender sobre desarrollo multiplataforma y gestión de tareas móviles.
