// Usuario.java
package main.java.model;

import java.io.Serializable;

public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    // Generador de ID único
    private static int contadorId = 1;

    // Atributos
    private int id;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String contraseña;

    // Constructor
    public Usuario(String nombre, String apellido, String correo, String telefono, String contraseña) {
        this.id = contadorId++;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.contraseña = contraseña;
    }

    // Métodos
    public void registrarse() {
        // Lógica para registrar un usuario en el sistema
        System.out.println("Usuario registrado: " + nombre + " " + apellido);
    }

    public boolean iniciarSesion(String correoIngresado, String contraseñaIngresada) {
        // Verificar credenciales
        return this.correo.equals(correoIngresado) && this.contraseña.equals(contraseñaIngresada);
    }

    public void cerrarSesion() {
        System.out.println("Sesión cerrada para: " + nombre + " " + apellido);
    }

    public void actualizarPerfil() {
        // Lógica para actualizar el perfil del usuario
        System.out.println("Perfil actualizado para: " + nombre + " " + apellido);
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}