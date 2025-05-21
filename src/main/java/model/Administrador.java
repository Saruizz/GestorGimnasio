// Administrador.java
package main.java.model;

import java.util.ArrayList;
import java.util.List;

public class Administrador extends Usuario {
    private static final long serialVersionUID = 1L;

    // Atributos
    private List<Miembro> miembros;
    private List<Entrenador> entrenadores;
    private List<ProgramaEntrenamiento> programasEntrenamiento;

    // Constructor
    public Administrador(String nombre, String apellido, String correo, String telefono, String contraseña) {
        super(nombre, apellido, correo, telefono, contraseña);
        this.miembros = new ArrayList<>();
        this.entrenadores = new ArrayList<>();
        this.programasEntrenamiento = new ArrayList<>();
    }

    // Métodos para gestionar miembros
    public void agregarMiembro(Miembro miembro) {
        if (!miembros.contains(miembro)) {
            miembros.add(miembro);
            System.out.println("Miembro agregado: " + miembro.getNombre() + " " + miembro.getApellido());
        } else {
            System.out.println("El miembro ya existe en el sistema");
        }
    }

    public void eliminarMiembro(Miembro miembro) {
        if (miembros.remove(miembro)) {
            System.out.println("Miembro eliminado: " + miembro.getNombre() + " " + miembro.getApellido());
        } else {
            System.out.println("El miembro no existe en el sistema");
        }
    }

    public void modificarMiembro(Miembro miembro) {
        for (int i = 0; i < miembros.size(); i++) {
            if (miembros.get(i).getId() == miembro.getId()) {
                miembros.set(i, miembro);
                System.out.println("Miembro modificado: " + miembro.getNombre() + " " + miembro.getApellido());
                return;
            }
        }
        System.out.println("El miembro no existe en el sistema");
    }

    // Métodos para gestionar entrenadores
    public void agregarEntrenador(Entrenador entrenador) {
        if (!entrenadores.contains(entrenador)) {
            entrenadores.add(entrenador);
            System.out.println("Entrenador agregado: " + entrenador.getNombre() + " " + entrenador.getApellido());
        } else {
            System.out.println("El entrenador ya existe en el sistema");
        }
    }

    public void eliminarEntrenador(Entrenador entrenador) {
        if (entrenadores.remove(entrenador)) {
            System.out.println("Entrenador eliminado: " + entrenador.getNombre() + " " + entrenador.getApellido());
        } else {
            System.out.println("El entrenador no existe en el sistema");
        }
    }

    public void modificarEntrenador(Entrenador entrenador) {
        for (int i = 0; i < entrenadores.size(); i++) {
            if (entrenadores.get(i).getId() == entrenador.getId()) {
                entrenadores.set(i, entrenador);
                System.out.println("Entrenador modificado: " + entrenador.getNombre() + " " + entrenador.getApellido());
                return;
            }
        }
        System.out.println("El entrenador no existe en el sistema");
    }

    // Métodos para gestionar programas de entrenamiento
    public void registrarProgramaEntrenamiento(ProgramaEntrenamiento programa) {
        if (!programasEntrenamiento.contains(programa)) {
            programasEntrenamiento.add(programa);
            System.out.println("Programa de entrenamiento registrado: " + programa.getNombre());
        } else {
            System.out.println("El programa ya existe en el sistema");
        }
    }

    public void eliminarProgramaEntrenamiento(ProgramaEntrenamiento programa) {
        if (programasEntrenamiento.remove(programa)) {
            System.out.println("Programa de entrenamiento eliminado: " + programa.getNombre());
        } else {
            System.out.println("El programa no existe en el sistema");
        }
    }

    // Métodos adicionales según el diagrama
    public void administrarMembresias() {
        System.out.println("Administrando membresías...");
        // Lógica para administrar membresías
    }

    public void generarReportes() {
        System.out.println("Generando reportes...");
        // Lógica para generar reportes
    }

    public void configurarSistema() {
        System.out.println("Configurando sistema...");
        // Lógica para configurar el sistema
    }

    // Getters
    public List<Miembro> getMiembros() {
        return new ArrayList<>(miembros);
    }

    public List<Entrenador> getEntrenadores() {
        return new ArrayList<>(entrenadores);
    }

    public List<ProgramaEntrenamiento> getProgramasEntrenamiento() {
        return new ArrayList<>(programasEntrenamiento);
    }

    // Búsqueda de miembros por ID o correo
    public Miembro buscarMiembroPorId(int id) {
        for (Miembro miembro : miembros) {
            if (miembro.getId() == id) {
                return miembro;
            }
        }
        return null;
    }

    public Miembro buscarMiembroPorCorreo(String correo) {
        for (Miembro miembro : miembros) {
            if (miembro.getCorreo().equals(correo)) {
                return miembro;
            }
        }
        return null;
    }

    // Búsqueda de entrenadores por ID o correo
    public Entrenador buscarEntrenadorPorId(int id) {
        for (Entrenador entrenador : entrenadores) {
            if (entrenador.getId() == id) {
                return entrenador;
            }
        }
        return null;
    }

    public Entrenador buscarEntrenadorPorCorreo(String correo) {
        for (Entrenador entrenador : entrenadores) {
            if (entrenador.getCorreo().equals(correo)) {
                return entrenador;
            }
        }
        return null;
    }

    // Búsqueda de programas por ID o nombre
    public ProgramaEntrenamiento buscarProgramaPorId(int id) {
        for (ProgramaEntrenamiento programa : programasEntrenamiento) {
            if (programa.getId() == id) {
                return programa;
            }
        }
        return null;
    }

    public ProgramaEntrenamiento buscarProgramaPorNombre(String nombre) {
        for (ProgramaEntrenamiento programa : programasEntrenamiento) {
            if (programa.getNombre().equals(nombre)) {
                return programa;
            }
        }
        return null;
    }
}