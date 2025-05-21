// Entrenador.java
package main.java.model;

import main.java.util.TipoPrograma;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Entrenador extends Usuario {
    private static final long serialVersionUID = 1L;

    // Atributos
    private List<String> especialidades;
    private List<HorarioDisponible> horariosDisponibles;
    private List<Miembro> miembrosAsignados;
    private List<ProgramaEntrenamiento> programasCreados;

    // Constructor
    public Entrenador(String nombre, String apellido, String correo, String telefono, String contraseña) {
        super(nombre, apellido, correo, telefono, contraseña);
        this.especialidades = new ArrayList<>();
        this.horariosDisponibles = new ArrayList<>();
        this.miembrosAsignados = new ArrayList<>();
        this.programasCreados = new ArrayList<>();
    }

    // Métodos
    public ProgramaEntrenamiento planificarProgramaEntrenamiento(String nombre, String descripcion, int duracion, TipoPrograma tipo) {
        ProgramaEntrenamiento nuevoPrograma = new ProgramaEntrenamiento(nombre, descripcion, duracion, tipo, this);
        programasCreados.add(nuevoPrograma);
        System.out.println("Programa de entrenamiento creado: " + nombre);
        return nuevoPrograma;
    }

    public void registrarAsistencia(Miembro miembro) {
        if (miembrosAsignados.contains(miembro)) {
            RegistroAsistencia registro = new RegistroAsistencia();
            registro.setFecha(new Date());
            registro.registrarEntrada();
            miembro.agregarRegistroAsistencia(registro);
            System.out.println("Asistencia registrada para: " + miembro.getNombre() + " " + miembro.getApellido());
        } else {
            System.out.println("Este miembro no está asignado a este entrenador");
        }
    }

    public void darSeguimientoProgreso(Miembro miembro, RegistroProgreso registro) {
        if (miembrosAsignados.contains(miembro)) {
            miembro.agregarRegistroProgreso(registro);
            System.out.println("Seguimiento de progreso registrado para: " + miembro.getNombre() + " " + miembro.getApellido());
        } else {
            System.out.println("Este miembro no está asignado a este entrenador");
        }
    }

    public RutinaPersonalizada generarRutinaPersonalizada(Miembro miembro) {
        if (miembrosAsignados.contains(miembro)) {
            // Por simplicidad, creamos una rutina personalizada básica
            RutinaPersonalizada rutina = new RutinaPersonalizada("Rutina personalizada para " + miembro.getNombre(),
                    "Rutina adaptada a los objetivos y condición física del miembro",
                    60, // duración en minutos
                    TipoPrograma.PERSONALIZADO,
                    this);
            System.out.println("Rutina personalizada generada para: " + miembro.getNombre() + " " + miembro.getApellido());
            return rutina;
        } else {
            System.out.println("Este miembro no está asignado a este entrenador");
            return null;
        }
    }

    public List<HorarioDisponible> consultarHorario() {
        return new ArrayList<>(horariosDisponibles);
    }

    // Getters y setters
    public List<String> getEspecialidades() {
        return new ArrayList<>(especialidades);
    }

    public void agregarEspecialidad(String especialidad) {
        if (!especialidades.contains(especialidad)) {
            especialidades.add(especialidad);
        }
    }

    public List<HorarioDisponible> getHorariosDisponibles() {
        return new ArrayList<>(horariosDisponibles);
    }

    public void agregarHorarioDisponible(HorarioDisponible horario) {
        if (!horariosDisponibles.contains(horario)) {
            horariosDisponibles.add(horario);
        }
    }

    public List<Miembro> getMiembrosAsignados() {
        return new ArrayList<>(miembrosAsignados);
    }

    public void asignarMiembro(Miembro miembro) {
        if (!miembrosAsignados.contains(miembro)) {
            miembrosAsignados.add(miembro);
            // Evitar recursión infinita si el método ya fue llamado desde setEntrenadorAsignado
            if (miembro.getEntrenadorAsignado() != this) {
                miembro.setEntrenadorAsignado(this);
            }
        }
    }

    public void desasignarMiembro(Miembro miembro) {
        if (miembrosAsignados.remove(miembro)) {
            // Remover la referencia del entrenador en el miembro
            if (miembro.getEntrenadorAsignado() == this) {
                miembro.setEntrenadorAsignado(null);
            }
            System.out.println("Miembro desasignado: " + miembro.getNombre() + " " + miembro.getApellido());
        } else {
            System.out.println("Este miembro no está asignado a este entrenador");
        }
    }

    public List<ProgramaEntrenamiento> getProgramasCreados() {
        return new ArrayList<>(programasCreados);
    }

    @Override
    public String toString() {
        return "Entrenador{" +
                "id=" + getId() +
                ", nombre='" + getNombre() + '\'' +
                ", apellido='" + getApellido() + '\'' +
                ", correo='" + getCorreo() + '\'' +
                ", especialidades=" + especialidades +
                ", miembrosAsignados=" + miembrosAsignados.size() +
                '}';
    }
}