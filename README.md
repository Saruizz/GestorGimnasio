# Sistema de Gestión de Gimnasio

Este proyecto es una aplicación Java para la gestión de un gimnasio, desarrollada como proyecto final para la materia de Programación Orientada a Objetos.

## Características

- **Arquitectura MVC**: Modelo-Vista-Controlador para una mejor organización del código.
- **Interfaz Gráfica de Usuario (GUI)**: Implementada con Swing.
- **Persistencia en memoria**: Los datos se almacenan en memoria durante la ejecución del programa.
- **Tres tipos de usuarios**: Administrador, Entrenador y Miembro.
- **Manejo de excepciones**: Para una mejor experiencia de usuario.

## Estructura del Proyecto

```
gym-management/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── model/           // Clases del modelo
│   │   │   ├── controller/      // Controladores lógicos
│   │   │   ├── util/            // Utilidades y enums
│   │   │   ├── view/            // Interfaces gráficas
│   │   │   └── Main.java        // Clase principal
│   │   └── resources/           // Recursos como imágenes, etc.
│   └── test/                    // Pruebas unitarias (si fueran necesarias)
```

## Requisitos

- Java JDK 8 o superior
- IDE Java (Eclipse, IntelliJ IDEA, NetBeans)

## Cómo Compilar y Ejecutar

### Desde un IDE

1. Importa el proyecto en tu IDE favorito
2. Localiza la clase `Main.java` en el paquete `main.java`
3. Ejecuta esta clase como aplicación Java

### Desde la línea de comandos

1. Navega hasta la carpeta raíz del proyecto
2. Compila el proyecto:
   ```
   javac -d bin src/main/java/Main.java
   ```
3. Ejecuta la aplicación:
   ```
   java -cp bin main.java.Main
   ```

## Credenciales de Prueba

Al iniciar la aplicación, se cargan automáticamente datos de prueba con los siguientes usuarios:

### Administrador
- **Correo**: admin@gimnasio.com
- **Contraseña**: admin123

### Entrenadores
- **Correo**: carlos@gym.com
- **Contraseña**: carlos123

- **Correo**: laura@gym.com
- **Contraseña**: laura123

### Miembros
- **Correo**: ana@ejemplo.com
- **Contraseña**: ana123

- **Correo**: juan@ejemplo.com
- **Contraseña**: juan123

## Casos de Uso Implementados

### Administrador
- Gestión de miembros (agregar, editar, eliminar)
- Gestión de entrenadores (agregar, editar, eliminar)
- Gestión de programas de entrenamiento
- Administración de membresías
- Generación de reportes
- Configuración del sistema

### Entrenador
- Planificación de programas de entrenamiento
- Registro de asistencia para miembros
- Seguimiento de progreso de miembros
- Generación de rutinas personalizadas
- Gestión de horarios disponibles

### Miembro
- Consulta de rutinas asignadas
- Registro de progreso personal
- Visualización de historial de asistencia
- Visualización de métricas de progreso
- Edición de perfil personal

## Diagramas

### Diagrama de Clases

El diagrama de clases se encuentra implementado según las especificaciones proporcionadas, con las correcciones solicitadas por el profesor:

1. Implementación de métodos específicos para el administrador (agregar, eliminar, modificar)
2. Definición clara de los programas de entrenamiento
3. Agregado de una lista de programas de entrenamiento en el administrador

### Casos de Uso

Los casos de uso implementados corresponden a los especificados en el documento proporcionado.

## Mejoras Futuras

- Implementación de base de datos para persistencia
- Mejoras en la interfaz gráfica
- Generación de reportes en PDF
- Visualización de gráficos para métricas de progreso
- Exportación de datos a Excel
- Sistema de notificaciones