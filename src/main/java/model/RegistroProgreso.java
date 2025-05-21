// RegistroProgreso.java
package main.java.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegistroProgreso implements Serializable {
    private static final long serialVersionUID = 1L;

    // Generador de ID único
    private static int contadorId = 1;

    // Atributos
    private int id;
    private Date fecha;
    private float peso;
    private Map<String, Float> medidas;
    private Map<Ejercicio, Float> rendimiento;
    private String comentarios;

    // Constructor
    public RegistroProgreso(Date fecha, float peso) {
        this.id = contadorId++;
        this.fecha = fecha;
        this.peso = peso;
        this.medidas = new HashMap<>();
        this.rendimiento = new HashMap<>();
        this.comentarios = "";
    }

    // Métodos
    public void registrarNuevoValor(String tipo, float valor) {
        medidas.put(tipo, valor);
        System.out.println("Nuevo valor registrado: " + tipo + " = " + valor);
    }

    public float compararConAnterior(RegistroProgreso registroAnterior) {
        if (registroAnterior == null) {
            return 0;
        }

        // Por simplicidad, comparamos solo el peso
        return this.peso - registroAnterior.getPeso();
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public Date getFecha() {
        return fecha;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public Map<String, Float> getMedidas() {
        return new HashMap<>(medidas);
    }

    public void agregarMedida(String parte, float valor) {
        medidas.put(parte, valor);
    }

    public Map<Ejercicio, Float> getRendimiento() {
        return new HashMap<>(rendimiento);
    }

    public void agregarRendimiento(Ejercicio ejercicio, float valor) {
        rendimiento.put(ejercicio, valor);
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    @Override
    public String toString() {
        return "RegistroProgreso{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", peso=" + peso +
                ", medidas=" + medidas.size() +
                ", rendimiento=" + rendimiento.size() +
                '}';
    }
}