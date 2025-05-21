// ProgramaEntrenamiento.java
package main.java.model;

import main.java.util.TipoPrograma;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProgramaEntrenamiento implements Serializable {
    private static final long serialVersionUID = 1L;

    // Generador de ID único
    private static int contadorId = 1;

    // Atributos
    private int id;
    private String nombre;
    private String descripcion;
    private int duracionSemanas;
    private TipoPrograma tipo;
    private Entrenador entrenadorAsignado;
    private List<Rutina> rutinas;
    private List<Miembro> miembrosAsignados;

    // Constructor
    public ProgramaEntrenamiento(String nombre, String descripcion, int duracionSemanas, TipoPrograma tipo, Entrenador entrenadorAsignado) {
        this.id = contadorId++;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracionSemanas = duracionSemanas;
        this.tipo = tipo;
        this.entrenadorAsignado = entrenadorAsignado;
        this.rutinas = new ArrayList<>();
        this.miembrosAsignados = new ArrayList<>();
    }

    // Métodos
    public void asignarAMiembro(Miembro miembro) {
        if (!miembrosAsignados.contains(miembro)) {
            miembrosAsignados.add(miembro);
            // Evitar recursión infinita si el método ya fue llamado desde asignarPrograma
            if (!miembro.getProgramasAsignados().contains(this)) {
                miembro.asignarPrograma(this);
            }
        }
    }

    public void actualizarPrograma() {
        System.out.println("Programa actualizado: " + nombre);
        // Lógica para actualizar el programa
    }

    public void eliminarPrograma() {
        System.out.println("Programa eliminado: " + nombre);
        // Lógica para eliminar el programa

        // Eliminar referencia en todos los miembros asignados
        for (Miembro miembro : new ArrayList<>(miembrosAsignados)) {
            miembro.eliminarPrograma(this);
        }

        // Limpiar las rutinas
        rutinas.clear();
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDuracionSemanas() {
        return duracionSemanas;
    }

    public void setDuracionSemanas(int duracionSemanas) {
        this.duracionSemanas = duracionSemanas;
    }

    public TipoPrograma getTipo() {
        return tipo;
    }

    public void setTipo(TipoPrograma tipo) {
        this.tipo = tipo;
    }

    public Entrenador getEntrenadorAsignado() {
        return entrenadorAsignado;
    }

    public List<Rutina> getRutinas() {
        return new ArrayList<>(rutinas);
    }

    public void agregarRutina(Rutina rutina) {
        if (!rutinas.contains(rutina)) {
            rutinas.add(rutina);
            System.out.println("Rutina agregada al programa: " + rutina.getNombre());
        }
    }

    public void eliminarRutina(Rutina rutina) {
        if (rutinas.remove(rutina)) {
            System.out.println("Rutina eliminada del programa: " + rutina.getNombre());
        } else {
            System.out.println("La rutina no existe en este programa");
        }
    }

    public List<Miembro> getMiembrosAsignados() {
        return new ArrayList<>(miembrosAsignados);
    }

    @Override
    public String toString() {
        return "ProgramaEntrenamiento{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", duracionSemanas=" + duracionSemanas +
                ", tipo=" + tipo +
                ", entrenador=" + (entrenadorAsignado != null ? entrenadorAsignado.getNombre() + " " + entrenadorAsignado.getApellido() : "No asignado") +
                ", rutinas=" + rutinas.size() +
                ", miembrosAsignados=" + miembrosAsignados.size() +
                '}';
    }
}