// RutinaPersonalizada.java
package main.java.model;

import main.java.util.NivelCondicionFisica;
import main.java.util.Objetivo;
import main.java.util.TipoPrograma;

import java.util.ArrayList;
import java.util.List;

public class RutinaPersonalizada extends ProgramaEntrenamiento {
    private static final long serialVersionUID = 1L;

    // Atributos
    private List<Objetivo> objetivos;
    private NivelCondicionFisica nivelCondicion;
    private List<String> limitacionesFisicas;

    // Constructor
    public RutinaPersonalizada(String nombre, String descripcion, int duracionMinutos,
                               TipoPrograma tipo, Entrenador entrenadorAsignado) {
        super(nombre, descripcion, 1, tipo, entrenadorAsignado); // Por defecto 1 semana
        this.objetivos = new ArrayList<>();
        this.nivelCondicion = NivelCondicionFisica.PRINCIPIANTE; // Por defecto
        this.limitacionesFisicas = new ArrayList<>();
    }

    // Métodos
    public List<Ejercicio> generarEjerciciosAdaptados() {
        List<Ejercicio> ejerciciosAdaptados = new ArrayList<>();

        // Lógica para generar ejercicios adaptados según objetivos, nivel y limitaciones
        // Por simplicidad, creamos algunos ejercicios básicos

        if (objetivos.contains(Objetivo.PERDIDA_PESO)) {
            ejerciciosAdaptados.add(new Ejercicio(
                    "Cardio intensivo",
                    "Ejercicio cardiovascular para quemar calorías",
                    "Cardiovascular",
                    "Cinta o elíptica",
                    3,
                    15,
                    0));
        }

        if (objetivos.contains(Objetivo.GANANCIA_MUSCULAR)) {
            ejerciciosAdaptados.add(new Ejercicio(
                    "Press de banca",
                    "Ejercicio para desarrollar pectorales",
                    "Pecho",
                    "Banca y mancuernas",
                    4,
                    12,
                    ajustarPesoSegunNivel(40)));
        }

        if (objetivos.contains(Objetivo.DEFINICION)) {
            ejerciciosAdaptados.add(new Ejercicio(
                    "Superseries de bíceps y tríceps",
                    "Ejercicio para definir brazos",
                    "Brazos",
                    "Mancuernas",
                    3,
                    15,
                    ajustarPesoSegunNivel(10)));
        }

        if (objetivos.contains(Objetivo.REHABILITACION)) {
            ejerciciosAdaptados.add(new Ejercicio(
                    "Estiramientos asistidos",
                    "Ejercicio para recuperar movilidad",
                    "Flexibilidad",
                    "Ninguno",
                    2,
                    10,
                    0));
        }

        // Si no hay objetivos específicos o para completar, agregar ejercicios básicos
        if (ejerciciosAdaptados.isEmpty() || ejerciciosAdaptados.size() < 3) {
            ejerciciosAdaptados.add(new Ejercicio(
                    "Sentadillas",
                    "Ejercicio básico para piernas",
                    "Piernas",
                    "Ninguno o pesas",
                    3,
                    12,
                    ajustarPesoSegunNivel(20)));

            ejerciciosAdaptados.add(new Ejercicio(
                    "Flexiones",
                    "Ejercicio básico para pecho",
                    "Pecho",
                    "Ninguno",
                    3,
                    10,
                    0));
        }

        return ejerciciosAdaptados;
    }

    private float ajustarPesoSegunNivel(float pesoBase) {
        switch (nivelCondicion) {
            case PRINCIPIANTE:
                return pesoBase * 0.6f;
            case INTERMEDIO:
                return pesoBase;
            case AVANZADO:
                return pesoBase * 1.3f;
            case ELITE:
                return pesoBase * 1.6f;
            default:
                return pesoBase;
        }
    }

    public void ajustarIntensidad(NivelCondicionFisica nivel) {
        this.nivelCondicion = nivel;
        System.out.println("Intensidad ajustada a nivel: " + nivel);

        // Generar nuevos ejercicios adaptados al nivel
        List<Ejercicio> nuevosEjercicios = generarEjerciciosAdaptados();

        // Crear una rutina con estos ejercicios
        for (Rutina rutina : getRutinas()) {
            // Limpiamos los ejercicios actuales
            List<Ejercicio> ejerciciosActuales = rutina.getEjercicios();
            for (Ejercicio ejercicio : ejerciciosActuales) {
                rutina.eliminarEjercicio(ejercicio);
            }

            // Añadimos los nuevos ejercicios
            for (Ejercicio ejercicio : nuevosEjercicios) {
                rutina.agregarEjercicio(ejercicio);
            }
        }
    }

    // Getters y setters
    public List<Objetivo> getObjetivos() {
        return new ArrayList<>(objetivos);
    }

    public void setObjetivos(List<Objetivo> objetivos) {
        this.objetivos = objetivos;
    }

    public void agregarObjetivo(Objetivo objetivo) {
        if (!objetivos.contains(objetivo)) {
            objetivos.add(objetivo);
        }
    }

    public NivelCondicionFisica getNivelCondicion() {
        return nivelCondicion;
    }

    public void setNivelCondicion(NivelCondicionFisica nivelCondicion) {
        this.nivelCondicion = nivelCondicion;
    }

    public List<String> getLimitacionesFisicas() {
        return new ArrayList<>(limitacionesFisicas);
    }

    public void agregarLimitacionFisica(String limitacion) {
        if (!limitacionesFisicas.contains(limitacion)) {
            limitacionesFisicas.add(limitacion);
        }
    }

    public void eliminarLimitacionFisica(String limitacion) {
        limitacionesFisicas.remove(limitacion);
    }

    @Override
    public String toString() {
        return "RutinaPersonalizada{" +
                "id=" + getId() +
                ", nombre='" + getNombre() + '\'' +
                ", objetivos=" + objetivos +
                ", nivelCondicion=" + nivelCondicion +
                ", limitacionesFisicas=" + limitacionesFisicas +
                '}';
    }
}