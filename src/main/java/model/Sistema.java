// Sistema.java
package main.java.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sistema implements Serializable {
    private static final long serialVersionUID = 1L;

    // Singleton
    private static Sistema instancia;

    // Atributos
    private Administrador adminDefecto;
    private List<Usuario> usuariosRegistrados;
    private Map<String, String> configuraciones;

    // Constructor privado (patrón Singleton)
    private Sistema() {
        this.usuariosRegistrados = new ArrayList<>();
        this.configuraciones = new HashMap<>();

        // Crear administrador por defecto
        this.adminDefecto = new Administrador("Admin", "Sistema", "admin@gimnasio.com", "123456789", "admin123");
        usuariosRegistrados.add(adminDefecto);

        // Configuraciones por defecto
        configuraciones.put("nombreGimnasio", "GYM Fitness");
        configuraciones.put("horarioApertura", "06:00");
        configuraciones.put("horarioCierre", "22:00");
        configuraciones.put("direccion", "Calle Principal #123");
        configuraciones.put("telefono", "555-1234");
    }

    // Método para obtener instancia (patrón Singleton)
    public static Sistema getInstancia() {
        if (instancia == null) {
            instancia = new Sistema();
        }
        return instancia;
    }

    // Métodos
    public void iniciarSistema() {
        System.out.println("Sistema iniciado correctamente");
        System.out.println("Bienvenido a " + configuraciones.get("nombreGimnasio"));
    }

    public void realizarBackup() {
        // Simulación de respaldo
        System.out.println("Backup realizado correctamente");
    }

    public void restaurarDatos() {
        // Simulación de restauración
        System.out.println("Datos restaurados correctamente");
    }

    public Map<String, Object> generarEstadisticas() {
        Map<String, Object> estadisticas = new HashMap<>();

        // Conteo de tipos de usuarios
        int totalMiembros = 0;
        int totalEntrenadores = 0;
        int totalAdministradores = 0;

        for (Usuario usuario : usuariosRegistrados) {
            if (usuario instanceof Miembro) {
                totalMiembros++;
            } else if (usuario instanceof Entrenador) {
                totalEntrenadores++;
            } else if (usuario instanceof Administrador) {
                totalAdministradores++;
            }
        }

        estadisticas.put("totalUsuarios", usuariosRegistrados.size());
        estadisticas.put("totalMiembros", totalMiembros);
        estadisticas.put("totalEntrenadores", totalEntrenadores);
        estadisticas.put("totalAdministradores", totalAdministradores);

        // Estadísticas de membresías
        int membresiasActivas = 0;
        int membresiasInactivas = 0;

        for (Usuario usuario : usuariosRegistrados) {
            if (usuario instanceof Miembro) {
                Miembro miembro = (Miembro) usuario;
                Membresia membresia = miembro.getMembresia();

                if (membresia != null) {
                    if (membresia.isActiva() && membresia.esVigente()) {
                        membresiasActivas++;
                    } else {
                        membresiasInactivas++;
                    }
                }
            }
        }

        estadisticas.put("membresiasActivas", membresiasActivas);
        estadisticas.put("membresiasInactivas", membresiasInactivas);

        return estadisticas;
    }

    public List<Usuario> getUsuariosPorTipo(String tipo) {
        List<Usuario> usuariosFiltrados = new ArrayList<>();

        for (Usuario usuario : usuariosRegistrados) {
            if (tipo.equalsIgnoreCase("administrador") && usuario instanceof Administrador) {
                usuariosFiltrados.add(usuario);
            } else if (tipo.equalsIgnoreCase("miembro") && usuario instanceof Miembro) {
                usuariosFiltrados.add(usuario);
            } else if (tipo.equalsIgnoreCase("entrenador") && usuario instanceof Entrenador) {
                usuariosFiltrados.add(usuario);
            }
        }

        return usuariosFiltrados;
    }

    public Usuario buscarUsuarioPorId(int id) {
        for (Usuario usuario : usuariosRegistrados) {
            if (usuario.getId() == id) {
                return usuario;
            }
        }
        return null;
    }

    public Usuario buscarUsuarioPorCorreo(String correo) {
        for (Usuario usuario : usuariosRegistrados) {
            if (usuario.getCorreo().equals(correo)) {
                return usuario;
            }
        }
        return null;
    }

    public boolean registrarUsuario(Usuario usuario) {
        // Verificar si ya existe un usuario con el mismo correo
        if (buscarUsuarioPorCorreo(usuario.getCorreo()) != null) {
            System.out.println("Error: Ya existe un usuario con el correo " + usuario.getCorreo());
            return false;
        }

        // Agregar el usuario
        usuariosRegistrados.add(usuario);
        System.out.println("Usuario registrado: " + usuario.getNombre() + " " + usuario.getApellido());

        // Actualizar administrador si es necesario
        if (usuario instanceof Miembro) {
            adminDefecto.agregarMiembro((Miembro) usuario);
        } else if (usuario instanceof Entrenador) {
            adminDefecto.agregarEntrenador((Entrenador) usuario);
        }

        return true;
    }

    public boolean eliminarUsuario(Usuario usuario) {
        // Verificar si el usuario existe
        if (!usuariosRegistrados.contains(usuario)) {
            System.out.println("Error: El usuario no existe en el sistema");
            return false;
        }

        // No permitir eliminar al administrador por defecto
        if (usuario == adminDefecto) {
            System.out.println("Error: No se puede eliminar al administrador por defecto");
            return false;
        }

        // Eliminar el usuario
        usuariosRegistrados.remove(usuario);
        System.out.println("Usuario eliminado: " + usuario.getNombre() + " " + usuario.getApellido());

        // Actualizar administrador si es necesario
        if (usuario instanceof Miembro) {
            adminDefecto.eliminarMiembro((Miembro) usuario);
        } else if (usuario instanceof Entrenador) {
            adminDefecto.eliminarEntrenador((Entrenador) usuario);
        }

        return true;
    }

    public Administrador getAdminDefecto() {
        return adminDefecto;
    }

    public List<Usuario> getUsuariosRegistrados() {
        return new ArrayList<>(usuariosRegistrados);
    }

    public void guardarConfiguracion(String clave, String valor) {
        configuraciones.put(clave, valor);
        System.out.println("Configuración guardada: " + clave + " = " + valor);
    }

    public String obtenerConfiguracion(String clave) {
        return configuraciones.getOrDefault(clave, "");
    }

    public Map<String, String> getConfiguraciones() {
        return new HashMap<>(configuraciones);
    }
}