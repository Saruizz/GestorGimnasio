// HorarioDisponible.java
package main.java.model;

import main.java.util.DiaSemana;
import java.io.Serializable;
import java.sql.Time;

public class HorarioDisponible implements Serializable {
    private static final long serialVersionUID = 1L;

    // Generador de ID único
    private static int contadorId = 1;

    // Atributos
    private int id;
    private DiaSemana dia;
    private Time horaInicio;
    private Time horaFin;

    // Constructor
    public HorarioDisponible(DiaSemana dia, Time horaInicio, Time horaFin) {
        this.id = contadorId++;
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    // Métodos
    public boolean estaDisponible(Time hora) {
        if (horaInicio == null || horaFin == null || hora == null) {
            return false;
        }

        // Verificar si la hora está dentro del rango
        return hora.after(horaInicio) && hora.before(horaFin);
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public DiaSemana getDia() {
        return dia;
    }

    public void setDia(DiaSemana dia) {
        this.dia = dia;
    }

    public Time getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Time horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Time getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Time horaFin) {
        this.horaFin = horaFin;
    }

    @Override
    public String toString() {
        return "HorarioDisponible{" +
                "id=" + id +
                ", dia=" + dia +
                ", horaInicio=" + horaInicio +
                ", horaFin=" + horaFin +
                '}';
    }
}