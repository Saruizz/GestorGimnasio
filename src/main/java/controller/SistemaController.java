// SistemaController.java
package main.java.controller;

import main.java.model.*;
import main.java.util.*;

import java.sql.Time;
import java.util.*;

/**
 * Controlador principal que maneja la lógica del sistema y sirve de intermediario
 * entre el modelo y las vistas.
 */
public class SistemaController {

    private Sistema sistema;
    private Usuario usuarioActual;

    public SistemaController() {
        // Inicializar el sistema
        sistema = Sistema.getInstancia();
        sistema.iniciarSistema();
    }

    /**
     * Carga datos de prueba para demostración
     */
    public void cargarDatosDePrueba() {
        try {
            // Crear algunos entrenadores
            Entrenador entrenador1 = new Entrenador("Carlos", "Pérez", "carlos@gym.com", "555123456", "carlos123");
            entrenador1.agregarEspecialidad("Musculación");
            entrenador1.agregarEspecialidad("CrossFit");

            Entrenador entrenador2 = new Entrenador("Laura", "Gómez", "laura@gym.com", "555987654", "laura123");
            entrenador2.agregarEspecialidad("Yoga");
            entrenador2.agregarEspecialidad("Pilates");

            // Agregar horarios disponibles a los entrenadores
            HorarioDisponible horario1 = new HorarioDisponible(
                    DiaSemana.LUNES,
                    new Time(8, 0, 0),
                    new Time(12, 0, 0)
            );

            HorarioDisponible horario2 = new HorarioDisponible(
                    DiaSemana.MIERCOLES,
                    new Time(14, 0, 0),
                    new Time(18, 0, 0)
            );

            entrenador1.agregarHorarioDisponible(horario1);
            entrenador1.agregarHorarioDisponible(horario2);

            // Registrar entrenadores en el sistema
            sistema.registrarUsuario(entrenador1);
            sistema.registrarUsuario(entrenador2);

            // Crear algunos programas de entrenamiento
            ProgramaEntrenamiento programa1 = entrenador1.planificarProgramaEntrenamiento(
                    "Programa de Fuerza",
                    "Programa enfocado en el desarrollo de fuerza",
                    8, // 8 semanas
                    TipoPrograma.FUERZA
            );

            ProgramaEntrenamiento programa2 = entrenador2.planificarProgramaEntrenamiento(
                    "Programa de Flexibilidad",
                    "Programa para mejorar la flexibilidad y equilibrio",
                    12, // 12 semanas
                    TipoPrograma.FLEXIBILIDAD
            );

            // Crear ejercicios
            Ejercicio ejercicio1 = new Ejercicio(
                    "Sentadilla",
                    "Ejercicio para piernas y glúteos",
                    "Piernas",
                    "Barra y discos",
                    4, // series
                    10, // repeticiones
                    60.0f // peso
            );

            Ejercicio ejercicio2 = new Ejercicio(
                    "Press de banca",
                    "Ejercicio para pecho y tríceps",
                    "Pecho",
                    "Banca y barra",
                    3, // series
                    12, // repeticiones
                    50.0f // peso
            );

            // Crear rutinas
            Rutina rutina1 = new Rutina(
                    "Día de piernas",
                    "Rutina centrada en piernas y glúteos",
                    60, // 60 minutos
                    DiaSemana.LUNES
            );
            rutina1.agregarEjercicio(ejercicio1);

            Rutina rutina2 = new Rutina(
                    "Día de pecho",
                    "Rutina centrada en pecho y brazos",
                    45, // 45 minutos
                    DiaSemana.MIERCOLES
            );
            rutina2.agregarEjercicio(ejercicio2);

            // Agregar rutinas a los programas
            programa1.agregarRutina(rutina1);
            programa1.agregarRutina(rutina2);

            // Registrar programas en el sistema
            sistema.getAdminDefecto().registrarProgramaEntrenamiento(programa1);
            sistema.getAdminDefecto().registrarProgramaEntrenamiento(programa2);

            // Crear algunos miembros
            Calendar cal = Calendar.getInstance();
            cal.set(1990, 5, 15); // 15 de junio de 1990
            Date fechaNacimiento1 = cal.getTime();

            cal.set(1985, 8, 22); // 22 de septiembre de 1985
            Date fechaNacimiento2 = cal.getTime();

            Miembro miembro1 = new Miembro(
                    "Ana",
                    "Martínez",
                    "ana@ejemplo.com",
                    "555111222",
                    "ana123",
                    fechaNacimiento1,
                    65.0f, // peso en kg
                    165.0f, // altura en cm
                    "Tonificar y perder peso"
            );

            Miembro miembro2 = new Miembro(
                    "Juan",
                    "García",
                    "juan@ejemplo.com",
                    "555333444",
                    "juan123",
                    fechaNacimiento2,
                    80.0f, // peso en kg
                    178.0f, // altura en cm
                    "Ganar masa muscular"
            );

            // Crear membresías para los miembros
            Calendar fechaInicio = Calendar.getInstance();
            Date fechaInicioMem1 = fechaInicio.getTime();

            fechaInicio.add(Calendar.MONTH, 1);
            Date fechaFinMem1 = fechaInicio.getTime();

            Membresia membresia1 = new Membresia("Mensual", fechaInicioMem1, fechaFinMem1, 50.0f);

            // Reset y configurar fecha para membresia2
            fechaInicio = Calendar.getInstance();
            fechaInicio.add(Calendar.MONTH, -1); // Comenzó hace un mes
            Date fechaInicioMem2 = fechaInicio.getTime();

            fechaInicio.add(Calendar.MONTH, 3); // Dura 3 meses desde el inicio
            Date fechaFinMem2 = fechaInicio.getTime();

            Membresia membresia2 = new Membresia("Trimestral", fechaInicioMem2, fechaFinMem2, 135.0f);

            // Asignar membresías a los miembros
            miembro1.setMembresia(membresia1);
            miembro2.setMembresia(membresia2);

            // Asignar entrenadores a miembros
            miembro1.setEntrenadorAsignado(entrenador2); // Ana con Laura (Yoga/Pilates)
            miembro2.setEntrenadorAsignado(entrenador1); // Juan con Carlos (Musculación)

            // Asignar programas a miembros
            miembro1.asignarPrograma(programa2); // Ana - Flexibilidad
            miembro2.asignarPrograma(programa1); // Juan - Fuerza

            // Crear registros de asistencia
            cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -5);
            Date fechaAsistencia1 = cal.getTime();

            RegistroAsistencia asistencia1 = new RegistroAsistencia();
            asistencia1.setFecha(fechaAsistencia1);
            asistencia1.setHoraEntrada(new Time(10, 0, 0));
            asistencia1.setHoraSalida(new Time(11, 30, 0));
            asistencia1.setCompletada(true);

            cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -2);
            Date fechaAsistencia2 = cal.getTime();

            RegistroAsistencia asistencia2 = new RegistroAsistencia();
            asistencia2.setFecha(fechaAsistencia2);
            asistencia2.setHoraEntrada(new Time(17, 0, 0));
            asistencia2.setHoraSalida(new Time(18, 45, 0));
            asistencia2.setCompletada(true);

            // Agregar asistencias a los miembros
            miembro1.agregarRegistroAsistencia(asistencia1);
            miembro2.agregarRegistroAsistencia(asistencia2);

            // Crear registros de progreso para miembros
            cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            Date fechaProgreso1 = cal.getTime();

            RegistroProgreso progreso1 = new RegistroProgreso(fechaProgreso1, 67.5f);
            progreso1.agregarMedida("Cintura", 75.0f);
            progreso1.agregarMedida("Cadera", 90.0f);
            progreso1.setComentarios("Inicio del programa");

            cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -7);
            Date fechaProgreso2 = cal.getTime();

            RegistroProgreso progreso2 = new RegistroProgreso(fechaProgreso2, 65.8f);
            progreso2.agregarMedida("Cintura", 73.5f);
            progreso2.agregarMedida("Cadera", 89.0f);
            progreso2.setComentarios("Progreso después de 3 semanas");

            // Agregar registros de progreso a los miembros
            miembro1.agregarRegistroProgreso(progreso1);
            miembro1.agregarRegistroProgreso(progreso2);

            // Registrar miembros en el sistema
            sistema.registrarUsuario(miembro1);
            sistema.registrarUsuario(miembro2);

            System.out.println("Datos de prueba cargados exitosamente");

        } catch (Exception e) {
            System.out.println("Error al cargar datos de prueba: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Intenta iniciar sesión con las credenciales proporcionadas
     * @param correo Correo del usuario
     * @param contraseña Contraseña del usuario
     * @return true si el inicio de sesión fue exitoso, false en caso contrario
     */
    public boolean iniciarSesion(String correo, String contraseña) {
        Usuario usuario = sistema.buscarUsuarioPorCorreo(correo);

        if (usuario != null && verificarContraseña(usuario, contraseña)) {
            this.usuarioActual = usuario;
            return true;
        }

        return false;
    }

    /**
     * Verifica si la contraseña es correcta para el usuario dado
     * @param usuario Usuario a verificar
     * @param contraseña Contraseña a comprobar
     * @return true si la contraseña es correcta, false en caso contrario
     */
    private boolean verificarContraseña(Usuario usuario, String contraseña) {
        // En una implementación real, compararíamos hashes de contraseñas
        // Por simplicidad, comparamos directamente para pruebas
        return usuario.getContraseña().equals(contraseña);
    }

    /**
     * Cierra la sesión del usuario actual
     */
    public void cerrarSesion() {
        if (usuarioActual != null) {
            usuarioActual.cerrarSesion();
            usuarioActual = null;
        }
    }

    /**
     * Registra un nuevo miembro en el sistema con una membresía
     * @param nombre Nombre del miembro
     * @param apellido Apellido del miembro
     * @param correo Correo del miembro
     * @param telefono Teléfono del miembro
     * @param contraseña Contraseña del miembro
     * @param fechaNacimiento Fecha de nacimiento
     * @param peso Peso en kg
     * @param altura Altura en cm
     * @param objetivos Objetivos del miembro
     * @param tipoMembresia Tipo de membresía a asignar
     * @return El miembro creado o null si ocurrió un error
     */
    public Miembro registrarMiembro(String nombre, String apellido, String correo, String telefono,
                                    String contraseña, Date fechaNacimiento, float peso, float altura,
                                    String objetivos, String tipoMembresia) {

        // Verificar si ya existe un usuario con ese correo
        if (sistema.buscarUsuarioPorCorreo(correo) != null) {
            return null;
        }

        // Crear una nueva membresía
        Calendar calendar = Calendar.getInstance();
        Date fechaInicio = calendar.getTime();

        calendar.add(Calendar.MONTH, getTiempoMembresia(tipoMembresia));
        Date fechaFin = calendar.getTime();

        Membresia membresia = new Membresia(tipoMembresia, fechaInicio, fechaFin, calcularCostoMembresia(tipoMembresia));

        // Crear nuevo miembro
        Miembro miembro = new Miembro(nombre, apellido, correo, telefono, contraseña,
                fechaNacimiento, peso, altura, objetivos);
        miembro.setMembresia(membresia);

        // Registrar miembro en el sistema
        if (sistema.registrarUsuario(miembro)) {
            return miembro;
        }

        return null;
    }

    /**
     * Determina la duración en meses según el tipo de membresía
     * @param tipoMembresia Tipo de membresía
     * @return Duración en meses
     */
    private int getTiempoMembresia(String tipoMembresia) {
        switch (tipoMembresia) {
            case "Mensual": return 1;
            case "Trimestral": return 3;
            case "Semestral": return 6;
            case "Anual": return 12;
            default: return 1;
        }
    }

    /**
     * Calcula el costo de la membresía según su tipo
     * @param tipoMembresia Tipo de membresía
     * @return Costo en la moneda local
     */
    private float calcularCostoMembresia(String tipoMembresia) {
        switch (tipoMembresia) {
            case "Mensual": return 50.0f;
            case "Trimestral": return 135.0f;
            case "Semestral": return 250.0f;
            case "Anual": return 450.0f;
            default: return 50.0f;
        }
    }

    /**
     * Obtiene todos los miembros registrados en el sistema
     * @return Lista de miembros
     */
    public List<Miembro> obtenerMiembros() {
        if (usuarioActual instanceof Administrador) {
            return ((Administrador) usuarioActual).getMiembros();
        } else if (usuarioActual instanceof Entrenador) {
            return ((Entrenador) usuarioActual).getMiembrosAsignados();
        }

        return new ArrayList<>();
    }

    /**
     * Obtiene todos los entrenadores registrados en el sistema
     * @return Lista de entrenadores
     */
    public List<Entrenador> obtenerEntrenadores() {
        if (usuarioActual instanceof Administrador) {
            return ((Administrador) usuarioActual).getEntrenadores();
        }

        return new ArrayList<>();
    }

    /**
     * Obtiene todos los programas de entrenamiento registrados en el sistema
     * @return Lista de programas de entrenamiento
     */
    public List<ProgramaEntrenamiento> obtenerProgramasEntrenamiento() {
        if (usuarioActual instanceof Administrador) {
            return ((Administrador) usuarioActual).getProgramasEntrenamiento();
        } else if (usuarioActual instanceof Entrenador) {
            return ((Entrenador) usuarioActual).getProgramasCreados();
        } else if (usuarioActual instanceof Miembro) {
            return ((Miembro) usuarioActual).getProgramasAsignados();
        }

        return new ArrayList<>();
    }

    /**
     * Crea un nuevo miembro en el sistema
     * @param nombre Nombre del miembro
     * @param apellido Apellido del miembro
     * @param correo Correo del miembro
     * @param telefono Teléfono del miembro
     * @param contraseña Contraseña del miembro
     * @param fechaNacimiento Fecha de nacimiento del miembro
     * @param peso Peso del miembro en kg
     * @param altura Altura del miembro en cm
     * @param objetivos Objetivos del miembro
     * @return El miembro creado o null si ocurrió un error
     */
    public Miembro crearMiembro(String nombre, String apellido, String correo, String telefono,
                                String contraseña, Date fechaNacimiento, float peso, float altura,
                                String objetivos) {
        try {
            Miembro miembro = new Miembro(nombre, apellido, correo, telefono, contraseña,
                    fechaNacimiento, peso, altura, objetivos);

            if (sistema.registrarUsuario(miembro)) {
                return miembro;
            }

            return null;
        } catch (Exception e) {
            System.out.println("Error al crear miembro: " + e.getMessage());
            return null;
        }
    }

    /**
     * Crea un nuevo entrenador en el sistema
     * @param nombre Nombre del entrenador
     * @param apellido Apellido del entrenador
     * @param correo Correo del entrenador
     * @param telefono Teléfono del entrenador
     * @param contraseña Contraseña del entrenador
     * @param especialidades Lista de especialidades del entrenador
     * @return El entrenador creado o null si ocurrió un error
     */
    public Entrenador crearEntrenador(String nombre, String apellido, String correo,
                                      String telefono, String contraseña, List<String> especialidades) {
        try {
            Entrenador entrenador = new Entrenador(nombre, apellido, correo, telefono, contraseña);

            // Agregar especialidades
            for (String especialidad : especialidades) {
                entrenador.agregarEspecialidad(especialidad);
            }

            if (sistema.registrarUsuario(entrenador)) {
                return entrenador;
            }

            return null;
        } catch (Exception e) {
            System.out.println("Error al crear entrenador: " + e.getMessage());
            return null;
        }
    }

    /**
     * Elimina un usuario del sistema
     * @param usuario El usuario a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    public boolean eliminarUsuario(Usuario usuario) {
        return sistema.eliminarUsuario(usuario);
    }

    /**
     * Crea un nuevo programa de entrenamiento
     * @param nombre Nombre del programa
     * @param descripcion Descripción del programa
     * @param duracionSemanas Duración en semanas
     * @param tipo Tipo de programa
     * @return El programa creado o null si ocurrió un error
     */
    public ProgramaEntrenamiento crearProgramaEntrenamiento(String nombre, String descripcion,
                                                            int duracionSemanas, TipoPrograma tipo) {
        try {
            if (usuarioActual instanceof Entrenador) {
                Entrenador entrenador = (Entrenador) usuarioActual;
                ProgramaEntrenamiento programa = entrenador.planificarProgramaEntrenamiento(
                        nombre, descripcion, duracionSemanas, tipo);

                sistema.getAdminDefecto().registrarProgramaEntrenamiento(programa);
                return programa;
            } else if (usuarioActual instanceof Administrador) {
                // Si es administrador, crear programa sin entrenador asignado
                ProgramaEntrenamiento programa = new ProgramaEntrenamiento(
                        nombre, descripcion, duracionSemanas, tipo, null);

                ((Administrador) usuarioActual).registrarProgramaEntrenamiento(programa);
                return programa;
            }

            return null;
        } catch (Exception e) {
            System.out.println("Error al crear programa de entrenamiento: " + e.getMessage());
            return null;
        }
    }

    /**
     * Elimina un programa de entrenamiento
     * @param programa El programa a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    public boolean eliminarProgramaEntrenamiento(ProgramaEntrenamiento programa) {
        try {
            if (usuarioActual instanceof Administrador) {
                ((Administrador) usuarioActual).eliminarProgramaEntrenamiento(programa);
                return true;
            }

            return false;
        } catch (Exception e) {
            System.out.println("Error al eliminar programa de entrenamiento: " + e.getMessage());
            return false;
        }
    }

    /**
     * Asigna un programa de entrenamiento a un miembro
     * @param programa El programa a asignar
     * @param miembro El miembro al que se asignará el programa
     * @return true si la asignación fue exitosa, false en caso contrario
     */
    public boolean asignarProgramaAMiembro(ProgramaEntrenamiento programa, Miembro miembro) {
        try {
            miembro.asignarPrograma(programa);
            return true;
        } catch (Exception e) {
            System.out.println("Error al asignar programa a miembro: " + e.getMessage());
            return false;
        }
    }

    /**
     * Asigna un entrenador a un miembro
     * @param entrenador El entrenador a asignar
     * @param miembro El miembro al que se asignará el entrenador
     * @return true si la asignación fue exitosa, false en caso contrario
     */
    public boolean asignarEntrenadorAMiembro(Entrenador entrenador, Miembro miembro) {
        try {
            miembro.setEntrenadorAsignado(entrenador);
            return true;
        } catch (Exception e) {
            System.out.println("Error al asignar entrenador a miembro: " + e.getMessage());
            return false;
        }
    }

    /**
     * Crea una nueva membresía para un miembro
     * @param miembro El miembro al que se asignará la membresía
     * @param tipo Tipo de membresía
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de finalización
     * @param costo Costo de la membresía
     * @return true si la creación fue exitosa, false en caso contrario
     */
    public boolean crearMembresia(Miembro miembro, String tipo, Date fechaInicio,
                                  Date fechaFin, float costo) {
        try {
            Membresia membresia = new Membresia(tipo, fechaInicio, fechaFin, costo);
            miembro.setMembresia(membresia);
            return true;
        } catch (Exception e) {
            System.out.println("Error al crear membresía: " + e.getMessage());
            return false;
        }
    }

    /**
     * Renueva una membresía existente
     * @param miembro El miembro cuya membresía se renovará
     * @param fechaFin Nueva fecha de finalización
     * @return true si la renovación fue exitosa, false en caso contrario
     */
    public boolean renovarMembresia(Miembro miembro, Date fechaFin) {
        try {
            Membresia membresia = miembro.getMembresia();

            if (membresia != null) {
                membresia.renovar(fechaFin);
                return true;
            }

            return false;
        } catch (Exception e) {
            System.out.println("Error al renovar membresía: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cancela una membresía existente
     * @param miembro El miembro cuya membresía se cancelará
     * @return true si la cancelación fue exitosa, false en caso contrario
     */
    public boolean cancelarMembresia(Miembro miembro) {
        try {
            Membresia membresia = miembro.getMembresia();

            if (membresia != null) {
                membresia.cancelar();
                return true;
            }

            return false;
        } catch (Exception e) {
            System.out.println("Error al cancelar membresía: " + e.getMessage());
            return false;
        }
    }

    /**
     * Crea una nueva rutina y la asigna a un programa de entrenamiento
     * @param nombre Nombre de la rutina
     * @param descripcion Descripción de la rutina
     * @param duracionMinutos Duración en minutos
     * @param dia Día de la semana
     * @param programa Programa al que se asignará la rutina
     * @return La rutina creada o null si ocurrió un error
     */
    public Rutina crearRutina(String nombre, String descripcion, int duracionMinutos,
                              DiaSemana dia, ProgramaEntrenamiento programa) {
        try {
            Rutina rutina = new Rutina(nombre, descripcion, duracionMinutos, dia);
            programa.agregarRutina(rutina);
            return rutina;
        } catch (Exception e) {
            System.out.println("Error al crear rutina: " + e.getMessage());
            return null;
        }
    }

    /**
     * Crea un nuevo ejercicio y lo asigna a una rutina
     * @param nombre Nombre del ejercicio
     * @param descripcion Descripción del ejercicio
     * @param grupoMuscular Grupo muscular objetivo
     * @param equipoNecesario Equipo necesario
     * @param series Número de series
     * @param repeticiones Número de repeticiones
     * @param peso Peso en kg
     * @param rutina Rutina a la que se asignará el ejercicio
     * @return El ejercicio creado o null si ocurrió un error
     */
    public Ejercicio crearEjercicio(String nombre, String descripcion, String grupoMuscular,
                                    String equipoNecesario, int series, int repeticiones,
                                    float peso, Rutina rutina) {
        try {
            Ejercicio ejercicio = new Ejercicio(nombre, descripcion, grupoMuscular,
                    equipoNecesario, series, repeticiones, peso);
            rutina.agregarEjercicio(ejercicio);
            return ejercicio;
        } catch (Exception e) {
            System.out.println("Error al crear ejercicio: " + e.getMessage());
            return null;
        }
    }

    /**
     * Registra la asistencia de un miembro
     * @param miembro El miembro cuya asistencia se registrará
     * @return El registro de asistencia creado o null si ocurrió un error
     */
    public RegistroAsistencia registrarAsistencia(Miembro miembro) {
        try {
            RegistroAsistencia registro = new RegistroAsistencia();
            registro.registrarEntrada();
            miembro.agregarRegistroAsistencia(registro);
            return registro;
        } catch (Exception e) {
            System.out.println("Error al registrar asistencia: " + e.getMessage());
            return null;
        }
    }

    /**
     * Registra la salida de un miembro
     * @param registro El registro de asistencia a completar
     * @return true si el registro de salida fue exitoso, false en caso contrario
     */
    public boolean registrarSalida(RegistroAsistencia registro) {
        try {
            registro.registrarSalida();
            return true;
        } catch (Exception e) {
            System.out.println("Error al registrar salida: " + e.getMessage());
            return false;
        }
    }

    /**
     * Crea un nuevo registro de progreso para un miembro
     * @param miembro El miembro cuyo progreso se registrará
     * @param peso Peso actual en kg
     * @param medidas Mapa de medidas corporales (nombre de la parte -> medida en cm)
     * @param comentarios Comentarios adicionales
     * @return El registro de progreso creado o null si ocurrió un error
     */
    public RegistroProgreso registrarProgreso(Miembro miembro, float peso,
                                              Map<String, Float> medidas, String comentarios) {
        try {
            RegistroProgreso registro = new RegistroProgreso(new Date(), peso);

            // Agregar medidas
            for (Map.Entry<String, Float> entry : medidas.entrySet()) {
                registro.agregarMedida(entry.getKey(), entry.getValue());
            }

            registro.setComentarios(comentarios);
            miembro.agregarRegistroProgreso(registro);
            return registro;
        } catch (Exception e) {
            System.out.println("Error al registrar progreso: " + e.getMessage());
            return null;
        }
    }

    /**
     * Agrega un horario disponible para un entrenador
     * @param entrenador El entrenador al que se agregará el horario
     * @param dia Día de la semana
     * @param horaInicio Hora de inicio
     * @param horaFin Hora de finalización
     * @return true si la operación fue exitosa, false en caso contrario
     */
    public boolean agregarHorarioDisponible(Entrenador entrenador, DiaSemana dia, Time horaInicio, Time horaFin) {
        try {
            HorarioDisponible horario = new HorarioDisponible(dia, horaInicio, horaFin);
            entrenador.agregarHorarioDisponible(horario);
            return true;
        } catch (Exception e) {
            System.out.println("Error al agregar horario disponible: " + e.getMessage());
            return false;
        }
    }

    /**
     * Genera una rutina personalizada para un miembro según sus características
     * @param miembro El miembro para el que se generará la rutina
     * @param objetivos Objetivos del miembro
     * @param nivelCondicion Nivel de condición física del miembro
     * @param limitacionesFisicas Limitaciones físicas a considerar
     * @return La rutina personalizada creada o null si ocurrió un error
     */
    public RutinaPersonalizada generarRutinaPersonalizada(Miembro miembro, List<Objetivo> objetivos,
                                                          NivelCondicionFisica nivelCondicion,
                                                          List<String> limitacionesFisicas) {
        try {
            if (usuarioActual instanceof Entrenador &&
                    ((Entrenador) usuarioActual).getMiembrosAsignados().contains(miembro)) {

                Entrenador entrenador = (Entrenador) usuarioActual;
                RutinaPersonalizada rutina = entrenador.generarRutinaPersonalizada(miembro);

                // Configurar objetivos y nivel de condición
                for (Objetivo objetivo : objetivos) {
                    rutina.agregarObjetivo(objetivo);
                }

                rutina.setNivelCondicion(nivelCondicion);

                // Agregar limitaciones físicas
                for (String limitacion : limitacionesFisicas) {
                    rutina.agregarLimitacionFisica(limitacion);
                }

                // Generar ejercicios adaptados
                List<Ejercicio> ejerciciosAdaptados = rutina.generarEjerciciosAdaptados();

                // Crear una rutina para cada día de la semana y agregar ejercicios
                DiaSemana[] dias = DiaSemana.values();
                for (int i = 0; i < Math.min(ejerciciosAdaptados.size(), 3); i++) {
                    Rutina rutinaDia = new Rutina(
                            "Rutina de " + dias[i].getNombre(),
                            "Rutina personalizada para " + miembro.getNombre(),
                            60, // 60 minutos por defecto
                            dias[i]
                    );

                    // Agregar ejercicio a la rutina
                    rutinaDia.agregarEjercicio(ejerciciosAdaptados.get(i));

                    // Agregar rutina al programa
                    rutina.agregarRutina(rutinaDia);
                }

                // Asignar la rutina personalizada al miembro
                miembro.asignarPrograma(rutina);

                return rutina;
            }

            return null;
        } catch (Exception e) {
            System.out.println("Error al generar rutina personalizada: " + e.getMessage());
            return null;
        }
    }

    /**
     * Guarda una configuración del sistema (solo Administrador)
     * @param clave Clave de la configuración
     * @param valor Valor de la configuración
     * @return true si la operación fue exitosa, false en caso contrario
     */
    public boolean guardarConfiguracion(String clave, String valor) {
        if (usuarioActual instanceof Administrador) {
            sistema.guardarConfiguracion(clave, valor);
            return true;
        }
        return false;
    }

    /**
     * Obtiene una configuración del sistema
     * @param clave Clave de la configuración
     * @return Valor de la configuración o cadena vacía si no existe
     */
    public String obtenerConfiguracion(String clave) {
        return sistema.obtenerConfiguracion(clave);
    }

    /**
     * Obtiene estadísticas del sistema (solo Administrador)
     * @return Mapa con las estadísticas
     */
    public Map<String, Object> obtenerEstadisticas() {
        if (usuarioActual instanceof Administrador) {
            return sistema.generarEstadisticas();
        }
        return new HashMap<>();
    }

    /**
     * Obtiene el usuario que ha iniciado sesión actualmente
     * @return El usuario actual o null si no hay sesión activa
     */
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    /**
     * Obtiene el tipo de usuario que ha iniciado sesión
     * @return String con el tipo de usuario ("Administrador", "Entrenador", "Miembro" o "Desconocido")
     */
    public String getTipoUsuarioActual() {
        if (usuarioActual instanceof Administrador) {
            return "Administrador";
        } else if (usuarioActual instanceof Entrenador) {
            return "Entrenador";
        } else if (usuarioActual instanceof Miembro) {
            return "Miembro";
        } else {
            return "Desconocido";
        }
    }

    /**
     * Verifica si el usuario actual tiene permisos de administrador
     * @return true si el usuario es un administrador, false en caso contrario
     */
    public boolean esAdministrador() {
        return usuarioActual instanceof Administrador;
    }

    /**
     * Verifica si el usuario actual tiene permisos de entrenador
     * @return true si el usuario es un entrenador, false en caso contrario
     */
    public boolean esEntrenador() {
        return usuarioActual instanceof Entrenador;
    }

    /**
     * Verifica si el usuario actual tiene permisos de miembro
     * @return true si el usuario es un miembro, false en caso contrario
     */
    public boolean esMiembro() {
        return usuarioActual instanceof Miembro;
    }
}