package main.java;

import main.java.controller.SistemaController;
import main.java.view.LoginView;

public class Main {
    public static void main(String[] args) {
        // Inicializar el controlador del sistema
        SistemaController sistemaController = new SistemaController();

        // Cargar datos iniciales para pruebas
        sistemaController.cargarDatosDePrueba();

        // Iniciar la vista de login
        LoginView loginView = new LoginView(sistemaController);
        loginView.setVisible(true);
    }
}