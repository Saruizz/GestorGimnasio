package main.java.view;

import main.java.controller.SistemaController;
import main.java.model.Miembro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistroView extends JFrame implements ActionListener {
    private JTextField txtNombre, txtApellido, txtCorreo, txtTelefono, txtPeso, txtAltura;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JTextArea txtObjetivos;
    private JComboBox<String> cmbTipoMembresia;
    private JTextField txtFechaNacimiento;
    private JButton btnRegistrar, btnCancelar;

    private SistemaController sistemaController;
    private JFrame parentFrame;

    public RegistroView(SistemaController sistemaController, JFrame parentFrame) {
        this.sistemaController = sistemaController;
        this.parentFrame = parentFrame;

        setTitle("Registro de Nuevo Miembro");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel de título
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblTitle = new JLabel("REGISTRO DE NUEVO MIEMBRO");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(lblTitle);

        // Panel de formulario
        JPanel formPanel = new JPanel(new GridLayout(11, 2, 10, 10));

        // Campos de formulario
        JLabel lblNombre = new JLabel("Nombre:");
        txtNombre = new JTextField(20);

        JLabel lblApellido = new JLabel("Apellido:");
        txtApellido = new JTextField(20);

        JLabel lblCorreo = new JLabel("Correo electrónico:");
        txtCorreo = new JTextField(20);

        JLabel lblTelefono = new JLabel("Teléfono:");
        txtTelefono = new JTextField(20);

        JLabel lblPassword = new JLabel("Contraseña:");
        txtPassword = new JPasswordField(20);

        JLabel lblConfirmPassword = new JLabel("Confirmar contraseña:");
        txtConfirmPassword = new JPasswordField(20);

        JLabel lblFechaNacimiento = new JLabel("Fecha de nacimiento (dd/mm/aaaa):");
        txtFechaNacimiento = new JTextField(20);

        JLabel lblPeso = new JLabel("Peso (kg):");
        txtPeso = new JTextField(20);

        JLabel lblAltura = new JLabel("Altura (cm):");
        txtAltura = new JTextField(20);

        JLabel lblTipoMembresia = new JLabel("Tipo de membresía:");
        String[] tiposMembresia = {"Mensual", "Trimestral", "Semestral", "Anual"};
        cmbTipoMembresia = new JComboBox<>(tiposMembresia);

        // Agregar componentes al panel del formulario
        formPanel.add(lblNombre); formPanel.add(txtNombre);
        formPanel.add(lblApellido); formPanel.add(txtApellido);
        formPanel.add(lblCorreo); formPanel.add(txtCorreo);
        formPanel.add(lblTelefono); formPanel.add(txtTelefono);
        formPanel.add(lblPassword); formPanel.add(txtPassword);
        formPanel.add(lblConfirmPassword); formPanel.add(txtConfirmPassword);
        formPanel.add(lblFechaNacimiento); formPanel.add(txtFechaNacimiento);
        formPanel.add(lblPeso); formPanel.add(txtPeso);
        formPanel.add(lblAltura); formPanel.add(txtAltura);
        formPanel.add(lblTipoMembresia); formPanel.add(cmbTipoMembresia);

        // Panel para objetivos
        JPanel objetivosPanel = new JPanel(new BorderLayout(5, 5));
        JLabel lblObjetivos = new JLabel("Objetivos personales:");
        txtObjetivos = new JTextArea(4, 20);
        txtObjetivos.setLineWrap(true);
        txtObjetivos.setWrapStyleWord(true);
        JScrollPane scrollObjetivos = new JScrollPane(txtObjetivos);

        objetivosPanel.add(lblObjetivos, BorderLayout.NORTH);
        objetivosPanel.add(scrollObjetivos, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        btnRegistrar = new JButton("Registrar");
        btnCancelar = new JButton("Cancelar");

        btnRegistrar.addActionListener(this);
        btnCancelar.addActionListener(this);

        buttonPanel.add(btnRegistrar);
        buttonPanel.add(btnCancelar);

        // Añadir paneles al panel principal
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(objetivosPanel, BorderLayout.AFTER_LAST_LINE);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Añadir panel principal al frame
        add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRegistrar) {
            // Validar que los campos no estén vacíos
            if (camposVacios()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos obligatorios",
                        "Error de registro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar que las contraseñas coincidan
            if (!passwordsCoinciden()) {
                JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden",
                        "Error de registro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar formato de fecha
            Date fechaNacimiento = validarFecha(txtFechaNacimiento.getText());
            if (fechaNacimiento == null) {
                JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use dd/mm/aaaa",
                        "Error de registro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar peso y altura como números
            float peso, altura;
            try {
                peso = Float.parseFloat(txtPeso.getText());
                altura = Float.parseFloat(txtAltura.getText());

                if (peso <= 0 || altura <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El peso y la altura deben ser números válidos",
                        "Error de registro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Crear nuevo miembro
            try {
                String tipoMembresia = (String) cmbTipoMembresia.getSelectedItem();
                Miembro nuevoMiembro = sistemaController.registrarMiembro(
                        txtNombre.getText(),
                        txtApellido.getText(),
                        txtCorreo.getText(),
                        txtTelefono.getText(),
                        new String(txtPassword.getPassword()),
                        fechaNacimiento,
                        peso,
                        altura,
                        txtObjetivos.getText(),
                        tipoMembresia
                );

                if (nuevoMiembro != null) {
                    JOptionPane.showMessageDialog(this, "Registro exitoso. Ya puede iniciar sesión.",
                            "Registro completado", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                    parentFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Error al registrar. El correo posiblemente ya está en uso.",
                            "Error de registro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar: " + ex.getMessage(),
                        "Error de registro", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == btnCancelar) {
            this.dispose();
            parentFrame.setVisible(true);
        }
    }

    private boolean camposVacios() {
        return txtNombre.getText().isEmpty() ||
                txtApellido.getText().isEmpty() ||
                txtCorreo.getText().isEmpty() ||
                txtTelefono.getText().isEmpty() ||
                txtPassword.getPassword().length == 0 ||
                txtConfirmPassword.getPassword().length == 0 ||
                txtFechaNacimiento.getText().isEmpty() ||
                txtPeso.getText().isEmpty() ||
                txtAltura.getText().isEmpty();
    }

    private boolean passwordsCoinciden() {
        String pass1 = new String(txtPassword.getPassword());
        String pass2 = new String(txtConfirmPassword.getPassword());
        return pass1.equals(pass2);
    }

    private Date validarFecha(String fechaStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            return sdf.parse(fechaStr);
        } catch (ParseException e) {
            return null;
        }
    }
}