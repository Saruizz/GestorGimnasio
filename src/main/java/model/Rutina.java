// Rutina.java
package main.java.model;

import main.java.util.DiaSemana;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Rutina implements Serializable {
    private static final long serialVersionUID = 1L;

    // Generador de ID único
    private static int contadorId = 1;

    // Atributos
    private int id;
    private String nombre;
    private String descripcion;
    private int duracionMinutos;
    private DiaSemana dia;
    private List<Ejercicio> ejercicios;

    // Constructor
    public Rutina(String nombre, String descripcion, int duracionMinutos, DiaSemana dia) {
        this.id = contadorId++;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracionMinutos = duracionMinutos;
        this.dia = dia;
        this.ejercicios = new ArrayList<>();
    }

    // Métodos
    public void agregarEjercicio(Ejercicio ejercicio) {
        if (!ejercicios.contains(ejercicio)) {
            ejercicios.add(ejercicio);
            System.out.println("Ejercicio agregado a la rutina: " + ejercicio.getNombre());
        } else {
            System.out.println("El ejercicio ya existe en esta rutina");
        }
    }

    public void eliminarEjercicio(Ejercicio ejercicio) {
        if (ejercicios.remove(ejercicio)) {
            System.out.println("Ejercicio eliminado de la rutina: " + ejercicio.getNombre());
        } else {
            System.out.println("El ejercicio no existe en esta rutina");
        }
    }

    public void modificarEjercicio(Ejercicio ejercicioModificado) {
        for (int i = 0; i < ejercicios.size(); i++) {
            if (ejercicios.get(i).getId() == ejercicioModificado.getId()) {
                ejercicios.set(i, ejercicioModificado);
                System.out.println("Ejercicio modificado: " + ejercicioModificado.getNombre());
                return;
            }
        }
        System.out.println("El ejercicio no existe en esta rutina");
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

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public DiaSemana getDia() {
        return dia;
    }

    public void setDia(DiaSemana dia) {
        this.dia = dia;
    }

    public List<Ejercicio> getEjercicios() {
        return new ArrayList<>(ejercicios);
    }

    @Override
    public String toString() {
        return "Rutina{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", duracionMinutos=" + duracionMinutos +
                ", dia=" + dia +
                ", ejercicios=" + ejercicios.size() +
                '}';
    }
}