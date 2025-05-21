// Miembro.java
package main.java.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Miembro extends Usuario {
    private static final long serialVersionUID = 1L;

    // Atributos
    private Date fechaNacimiento;
    private int edad;
    private float peso;
    private float altura;
    private String objetivos;
    private List<RegistroAsistencia> historialAsistencia;
    private List<RegistroProgreso> historialProgreso;
    private Membresia membresia;
    private List<ProgramaEntrenamiento> programasAsignados;
    private Entrenador entrenadorAsignado;

    // Constructor
    public Miembro(String nombre, String apellido, String correo, String telefono, String contraseña,
                   Date fechaNacimiento, float peso, float altura, String objetivos) {
        super(nombre, apellido, correo, telefono, contraseña);
        this.fechaNacimiento = fechaNacimiento;
        this.calcularEdad();
        this.peso = peso;
        this.altura = altura;
        this.objetivos = objetivos;
        this.historialAsistencia = new ArrayList<>();
        this.historialProgreso = new ArrayList<>();
        this.programasAsignados = new ArrayList<>();
    }

    // Métodos
    private void calcularEdad() {
        // Lógica para calcular la edad basada en la fecha de nacimiento
        if (fechaNacimiento != null) {
            Date hoy = new Date();
            long diferencia = hoy.getTime() - fechaNacimiento.getTime();
            long edadEnDias = diferencia / (1000 * 60 * 60 * 24);
            this.edad = (int) (edadEnDias / 365.25);
        } else {
            this.edad = 0;
        }
    }

    public Rutina consultarRutina() {
        // Por simplicidad, retornamos la primera rutina del primer programa
        if (programasAsignados.isEmpty()) {
            System.out.println("No tiene programas asignados");
            return null;
        }

        ProgramaEntrenamiento programa = programasAsignados.get(0);
        List<Rutina> rutinas = programa.getRutinas();
        if (rutinas.isEmpty()) {
            System.out.println("El programa no tiene rutinas asignadas");
            return null;
        }

        return rutinas.get(0);
    }

    public void registrarProgresoPersonal(RegistroProgreso registro) {
        historialProgreso.add(registro);
        System.out.println("Progreso registrado para: " + getNombre() + " " + getApellido());
    }

    public List<RegistroAsistencia> verHistorialAsistencia() {
        return new ArrayList<>(historialAsistencia);
    }

    public List<RegistroProgreso> verMetricasProgreso() {
        return new ArrayList<>(historialProgreso);
    }

    // Getters y setters
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
        calcularEdad();
    }

    public int getEdad() {
        return edad;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public float getAltura() {
        return altura;
    }

    public void setAltura(float altura) {
        this.altura = altura;
    }

    public String getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = objetivos;
    }

    public Membresia getMembresia() {
        return membresia;
    }

    public void setMembresia(Membresia membresia) {
        this.membresia = membresia;
    }

    public List<ProgramaEntrenamiento> getProgramasAsignados() {
        return new ArrayList<>(programasAsignados);
    }

    public void asignarPrograma(ProgramaEntrenamiento programa) {
        if (!programasAsignados.contains(programa)) {
            programasAsignados.add(programa);
            programa.asignarAMiembro(this);
            System.out.println("Programa asignado a: " + getNombre() + " " + getApellido());
        } else {
            System.out.println("El programa ya está asignado a este miembro");
        }
    }

    public void eliminarPrograma(ProgramaEntrenamiento programa) {
        if (programasAsignados.remove(programa)) {
            System.out.println("Programa eliminado del miembro: " + getNombre() + " " + getApellido());
        } else {
            System.out.println("El programa no está asignado a este miembro");
        }
    }

    public Entrenador getEntrenadorAsignado() {
        return entrenadorAsignado;
    }

    public void setEntrenadorAsignado(Entrenador entrenador) {
        this.entrenadorAsignado = entrenador;
        if (entrenador != null) {
            entrenador.asignarMiembro(this);
        }
    }

    public void agregarRegistroAsistencia(RegistroAsistencia registro) {
        historialAsistencia.add(registro);
    }

    public void agregarRegistroProgreso(RegistroProgreso registro) {
        historialProgreso.add(registro);
    }

    @Override
    public String toString() {
        return "Miembro{" +
                "id=" + getId() +
                ", nombre='" + getNombre() + '\'' +
                ", apellido='" + getApellido() + '\'' +
                ", correo='" + getCorreo() + '\'' +
                ", edad=" + edad +
                ", peso=" + peso +
                ", altura=" + altura +
                ", membresía=" + (membresia != null ? membresia.getTipo() : "No asignada") +
                ", entrenador=" + (entrenadorAsignado != null ? entrenadorAsignado.getNombre() + " " + entrenadorAsignado.getApellido() : "No asignado") +
                '}';
    }
}