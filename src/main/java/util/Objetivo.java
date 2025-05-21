package main.java.util;

public enum Objetivo {
    PERDIDA_PESO("Pérdida de peso"),
    GANANCIA_MUSCULAR("Ganancia muscular"),
    DEFINICION("Definición muscular"),
    REHABILITACION("Rehabilitación"),
    MANTENIMIENTO("Mantenimiento"),
    RENDIMIENTO_DEPORTIVO("Rendimiento deportivo");

    private final String nombre;

    Objetivo(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}