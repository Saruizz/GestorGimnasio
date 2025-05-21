package main.java.util;

public enum TipoPrograma {
    FUERZA("Fuerza"),
    RESISTENCIA("Resistencia"),
    HIPERTROFIA("Hipertrofia"),
    CARDIO("Cardio"),
    FLEXIBILIDAD("Flexibilidad"),
    FUNCIONAL("Funcional"),
    PERSONALIZADO("Personalizado");

    private final String nombre;

    TipoPrograma(String nombre) {
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