// LoginView.java
package main.java.view;

import main.java.controller.SistemaController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Vista para el inicio de sesión de usuarios
 */
public class LoginView extends JFrame {

    private SistemaController controller;

    private JTextField txtCorreo;
    private JPasswordField txtPassword;
    private JButton btnIngresar;
    private JLabel lblError;

    public LoginView(SistemaController controller) {
        this.controller = controller;
        initComponents();
    }

    private void initComponents() {
        // Configurar ventana
        setTitle("Sistema de Gestión de Gimnasio - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel de título
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel lblTitle = new JLabel("SISTEMA DE GESTIÓN DE GIMNASIO");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        titlePanel.add(lblTitle);

        // Panel de formulario
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel lblCorreo = new JLabel("Correo electrónico:");
        txtCorreo = new JTextField();

        JLabel lblPassword = new JLabel("Contraseña:");
        txtPassword = new JPasswordField();

        lblError = new JLabel("");
        lblError.setForeground(Color.RED);

        formPanel.add(lblCorreo);
        formPanel.add(txtCorreo);
        formPanel.add(lblPassword);
        formPanel.add(txtPassword);
        formPanel.add(new JLabel(""));
        formPanel.add(lblError);

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        btnIngresar = new JButton("Ingresar");
        btnIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSesion();
            }
        });

        buttonPanel.add(btnIngresar);

        // Agregar paneles al panel principal
        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Agregar panel principal al frame
        getContentPane().add(panel);

        // Configurar acción Enter en campos
        txtCorreo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtPassword.requestFocus();
            }
        });

        txtPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSesion();
            }
        });
    }

    private void iniciarSesion() {
        String correo = txtCorreo.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (correo.isEmpty() || password.isEmpty()) {
            lblError.setText("Debe ingresar correo y contraseña");
            return;
        }

        if (controller.iniciarSesion(correo, password)) {
            lblError.setText("");
            abrirVistaPrincipal();
        } else {
            lblError.setText("Credenciales incorrectas");
            txtPassword.setText("");
            txtPassword.requestFocus();
        }
    }

    private void abrirVistaPrincipal() {
        String tipoUsuario = controller.getTipoUsuarioActual();

        switch (tipoUsuario) {
            case "Administrador":
                AdminView adminView = new AdminView(controller);
                adminView.setVisible(true);
                break;

            case "Entrenador":
                EntrenadorView entrenadorView = new EntrenadorView(controller);
                entrenadorView.setVisible(true);
                break;

            case "Miembro":
                MiembroView miembroView = new MiembroView(controller);
                miembroView.setVisible(true);
                break;

            default:
                JOptionPane.showMessageDialog(this,
                        "Tipo de usuario no reconocido: " + tipoUsuario,
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }

        // Cerrar ventana de login
        this.dispose();
    }
}