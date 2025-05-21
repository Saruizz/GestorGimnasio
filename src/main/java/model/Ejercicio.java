// Ejercicio.java
package main.java.model;

import java.io.Serializable;

public class Ejercicio implements Serializable {
    private static final long serialVersionUID = 1L;

    // Generador de ID único
    private static int contadorId = 1;

    // Atributos
    private int id;
    private String nombre;
    private String descripcion;
    private String grupoMuscular;
    private String equipoNecesario;
    private int series;
    private int repeticiones;
    private float peso;

    // Constructor
    public Ejercicio(String nombre, String descripcion, String grupoMuscular, String equipoNecesario,
                     int series, int repeticiones, float peso) {
        this.id = contadorId++;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.grupoMuscular = grupoMuscular;
        this.equipoNecesario = equipoNecesario;
        this.series = series;
        this.repeticiones = repeticiones;
        this.peso = peso;
    }

    // Métodos
    public void actualizarParametros(int series, int repeticiones, float peso) {
        this.series = series;
        this.repeticiones = repeticiones;
        this.peso = peso;
        System.out.println("Parámetros actualizados para: " + nombre);
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

    public String getGrupoMuscular() {
        return grupoMuscular;
    }

    public void setGrupoMuscular(String grupoMuscular) {
        this.grupoMuscular = grupoMuscular;
    }

    public String getEquipoNecesario() {
        return equipoNecesario;
    }

    public void setEquipoNecesario(String equipoNecesario) {
        this.equipoNecesario = equipoNecesario;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public int getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(int repeticiones) {
        this.repeticiones = repeticiones;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    @Override
    public String toString() {
        return "Ejercicio{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", grupoMuscular='" + grupoMuscular + '\'' +
                ", series=" + series +
                ", repeticiones=" + repeticiones +
                ", peso=" + peso + " kg" +
                '}';
    }
}