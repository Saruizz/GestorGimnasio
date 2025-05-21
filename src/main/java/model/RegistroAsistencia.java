// RegistroAsistencia.java
package main.java.model;

import java.io.Serializable;
import java.time.Duration;
import java.util.Date;
import java.sql.Time;

public class RegistroAsistencia implements Serializable {
    private static final long serialVersionUID = 1L;

    // Generador de ID único
    private static int contadorId = 1;

    // Atributos
    private int id;
    private Date fecha;
    private Time horaEntrada;
    private Time horaSalida;
    private boolean completada;

    // Constructor
    public RegistroAsistencia() {
        this.id = contadorId++;
        this.fecha = new Date();
        this.completada = false;
    }

    // Métodos
    public void registrarEntrada() {
        this.horaEntrada = new Time(System.currentTimeMillis());
        System.out.println("Entrada registrada a las: " + horaEntrada);
    }

    public void registrarSalida() {
        this.horaSalida = new Time(System.currentTimeMillis());
        this.completada = true;
        System.out.println("Salida registrada a las: " + horaSalida);
    }

    public Time calcularDuracion() {
        if (horaEntrada == null || horaSalida == null) {
            return null;
        }

        // Calcular la diferencia en milisegundos
        long diferenciaMilis = horaSalida.getTime() - horaEntrada.getTime();

        // Convertir la diferencia a formato Time
        return new Time(diferenciaMilis);
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Time getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(Time horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public Time getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(Time horaSalida) {
        this.horaSalida = horaSalida;
        if (horaSalida != null && horaEntrada != null) {
            this.completada = true;
        }
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    // Método para calcular la duración en forma legible
    public String getDuracionFormateada() {
        if (horaEntrada == null || horaSalida == null) {
            return "No completada";
        }

        long diferenciaMilis = horaSalida.getTime() - horaEntrada.getTime();
        Duration duracion = Duration.ofMillis(diferenciaMilis);

        long horas = duracion.toHours();
        long minutos = duracion.toMinutesPart();

        return String.format("%d horas, %d minutos", horas, minutos);
    }

    @Override
    public String toString() {
        return "RegistroAsistencia{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", horaEntrada=" + horaEntrada +
                ", horaSalida=" + horaSalida +
                ", completada=" + completada +
                ", duración=" + (completada ? getDuracionFormateada() : "No completada") +
                '}';
    }
}