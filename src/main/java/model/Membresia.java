// Membresia.java
package main.java.model;

import java.io.Serializable;
import java.util.Date;

public class Membresia implements Serializable {
    private static final long serialVersionUID = 1L;

    // Generador de ID único
    private static int contadorId = 1;

    // Atributos
    private int id;
    private String tipo;
    private Date fechaInicio;
    private Date fechaFin;
    private float costo;
    private boolean activa;

    // Constructor
    public Membresia(String tipo, Date fechaInicio, Date fechaFin, float costo) {
        this.id = contadorId++;
        this.tipo = tipo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.costo = costo;
        this.activa = true;
    }

    // Métodos
    public void activar() {
        this.activa = true;
        System.out.println("Membresía activada: ID " + id);
    }

    public void renovar(Date fechaFin) {
        this.fechaFin = fechaFin;
        this.activa = true;
        System.out.println("Membresía renovada hasta: " + fechaFin);
    }

    public void cancelar() {
        this.activa = false;
        System.out.println("Membresía cancelada: ID " + id);
    }

    public void cambiarTipo(String tipo) {
        this.tipo = tipo;
        System.out.println("Tipo de membresía cambiado a: " + tipo);
    }

    // Verificar si la membresía está vigente
    public boolean esVigente() {
        if (!activa) {
            return false;
        }

        Date hoy = new Date();
        return hoy.compareTo(fechaInicio) >= 0 && hoy.compareTo(fechaFin) <= 0;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    @Override
    public String toString() {
        return "Membresía{" +
                "id=" + id +
                ", tipo='" + tipo + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", costo=" + costo +
                ", activa=" + activa +
                ", vigente=" + esVigente() +
                '}';
    }
}