// MiembroView.java
package main.java.view;

import main.java.controller.SistemaController;
import main.java.model.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Vista principal para el Miembro
 */
public class MiembroView extends JFrame {

    private SistemaController controller;
    private Miembro miembroActual;

    private JTabbedPane tabbedPane;
    private JPanel panelRutinas;
    private JPanel panelProgreso;
    private JPanel panelAsistencia;
    private JPanel panelPerfil;

    // Componentes para panel de rutinas
    private JComboBox<String> cmbProgramas;
    private JTextArea txtAreaRutina;
    private JButton btnRegistrarCompletada;

    // Componentes para panel de progreso
    private JTable tablaProgreso;
    private DefaultTableModel modeloTablaProgreso;
    private JButton btnRegistrarProgreso;

    // Componentes para panel de asistencia
    private JTable tablaAsistencia;
    private DefaultTableModel modeloTablaAsistencia;

    public MiembroView(SistemaController controller) {
        this.controller = controller;
        this.miembroActual = (Miembro) controller.getUsuarioActual();
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        // Configurar ventana
        setTitle("Sistema de Gestión de Gimnasio - Miembro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Panel principal con pestañas
        tabbedPane = new JTabbedPane();

        // Inicializar paneles
        panelRutinas = new JPanel();
        panelProgreso = new JPanel();
        panelAsistencia = new JPanel();
        panelPerfil = new JPanel();

        // Configurar paneles
        configurarPanelRutinas();
        configurarPanelProgreso();
        configurarPanelAsistencia();
        configurarPanelPerfil();

        // Agregar paneles a las pestañas
        tabbedPane.addTab("Mis Rutinas", new ImageIcon(), panelRutinas, "Consultar rutinas de entrenamiento");
        tabbedPane.addTab("Mi Progreso", new ImageIcon(), panelProgreso, "Ver métricas de progreso");
        tabbedPane.addTab("Mi Asistencia", new ImageIcon(), panelAsistencia, "Ver historial de asistencia");
        tabbedPane.addTab("Mi Perfil", new ImageIcon(), panelPerfil, "Ver y editar perfil");

        // Agregar listener para cambio de pestaña
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                switch (selectedIndex) {
                    case 0: // Rutinas
                        cargarProgramas();
                        break;
                    case 1: // Progreso
                        cargarTablaProgreso();
                        break;
                    case 2: // Asistencia
                        cargarTablaAsistencia();
                        break;
                }
            }
        });

        // Crear barra de menú
        JMenuBar menuBar = new JMenuBar();

        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemSalir = new JMenuItem("Cerrar Sesión");

        itemSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarSesion();
            }
        });

        menuArchivo.add(itemSalir);

        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem itemAcerca = new JMenuItem("Acerca de");
        itemAcerca.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MiembroView.this,
                        "Sistema de Gestión de Gimnasio\nVersión 1.0\nDesarrollado por Sebastian Ruiz y Daniela Diaz",
                        "Acerca de", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        menuAyuda.add(itemAcerca);

        menuBar.add(menuArchivo);
        menuBar.add(menuAyuda);

        setJMenuBar(menuBar);

        // Agregar panel principal al frame
        getContentPane().add(tabbedPane);
    }

    private void configurarPanelRutinas() {
        panelRutinas.setLayout(new BorderLayout());

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel lblPrograma = new JLabel("Programa de entrenamiento:");
        cmbProgramas = new JComboBox<>();
        btnRegistrarCompletada = new JButton("Marcar como Completada");

        btnRegistrarCompletada.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarRutinaCompletada();
            }
        });

        cmbProgramas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarRutina();
            }
        });

        panelSuperior.add(lblPrograma);
        panelSuperior.add(cmbProgramas);
        panelSuperior.add(btnRegistrarCompletada);

        // Panel central
        txtAreaRutina = new JTextArea();
        txtAreaRutina.setEditable(false);
        txtAreaRutina.setLineWrap(true);
        txtAreaRutina.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(txtAreaRutina);

        // Agregar componentes al panel
        panelRutinas.add(panelSuperior, BorderLayout.NORTH);
        panelRutinas.add(scrollPane, BorderLayout.CENTER);
    }

    private void configurarPanelProgreso() {
        panelProgreso.setLayout(new BorderLayout());

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.LEFT));

        btnRegistrarProgreso = new JButton("Registrar Progreso");

        btnRegistrarProgreso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarProgreso();
            }
        });

        panelBotones.add(btnRegistrarProgreso);

        // Tabla de progreso
        String[] columnas = {"Fecha", "Peso", "IMC", "Medidas", "Comentarios"};
        modeloTablaProgreso = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No permitir edición directa
            }
        };

        tablaProgreso = new JTable(modeloTablaProgreso);
        tablaProgreso.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaProgreso.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tablaProgreso);

        // Panel de gráfico (simulado)
        JPanel panelGrafico = new JPanel();
        panelGrafico.setLayout(new BorderLayout());
        panelGrafico.setBorder(BorderFactory.createTitledBorder("Gráfico de Progreso"));

        JLabel lblGrafico = new JLabel("Gráfico de progreso (simulado)", JLabel.CENTER);
        lblGrafico.setPreferredSize(new Dimension(0, 200));
        panelGrafico.add(lblGrafico, BorderLayout.CENTER);

        // Agregar componentes al panel
        panelProgreso.add(panelBotones, BorderLayout.NORTH);
        panelProgreso.add(scrollPane, BorderLayout.CENTER);
        panelProgreso.add(panelGrafico, BorderLayout.SOUTH);
    }

    private void configurarPanelAsistencia() {
        panelAsistencia.setLayout(new BorderLayout());

        // Tabla de asistencia
        String[] columnas = {"Fecha", "Hora Entrada", "Hora Salida", "Duración"};
        modeloTablaAsistencia = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No permitir edición directa
            }
        };

        tablaAsistencia = new JTable(modeloTablaAsistencia);
        tablaAsistencia.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaAsistencia.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tablaAsistencia);

        // Panel de estadísticas
        JPanel panelEstadisticas = new JPanel();
        panelEstadisticas.setLayout(new GridLayout(3, 2, 10, 10));
        panelEstadisticas.setBorder(BorderFactory.createTitledBorder("Estadísticas de Asistencia"));

        JLabel lblTotal = new JLabel("Total de asistencias:");
        JLabel lblTotalValor = new JLabel(String.valueOf(miembroActual.verHistorialAsistencia().size()));

        JLabel lblPromedio = new JLabel("Promedio semanal:");

        // Calcular promedio
        int totalAsistencias = miembroActual.verHistorialAsistencia().size();
        float promedio = 0;
        if (totalAsistencias > 0) {
            // Suponemos un periodo de las últimas 4 semanas
            promedio = totalAsistencias / 4.0f;
        }

        JLabel lblPromedioValor = new JLabel(String.format("%.1f", promedio));

        JLabel lblUltima = new JLabel("Última asistencia:");

        // Obtener última asistencia
        String ultimaAsistencia = "No registrada";
        List<RegistroAsistencia> asistencias = miembroActual.verHistorialAsistencia();
        if (!asistencias.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            RegistroAsistencia ultima = asistencias.get(asistencias.size() - 1);
            ultimaAsistencia = sdf.format(ultima.getFecha());
        }

        JLabel lblUltimaValor = new JLabel(ultimaAsistencia);

        panelEstadisticas.add(lblTotal);
        panelEstadisticas.add(lblTotalValor);
        panelEstadisticas.add(lblPromedio);
        panelEstadisticas.add(lblPromedioValor);
        panelEstadisticas.add(lblUltima);
        panelEstadisticas.add(lblUltimaValor);

        panelEstadisticas.setPreferredSize(new Dimension(0, 150));

        // Agregar componentes al panel
        panelAsistencia.add(scrollPane, BorderLayout.CENTER);
        panelAsistencia.add(panelEstadisticas, BorderLayout.SOUTH);
    }

    private void configurarPanelPerfil() {
        panelPerfil.setLayout(new BorderLayout());

        // Panel de información
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new GridLayout(11, 2, 10, 10));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField(miembroActual.getNombre());

        JLabel lblApellido = new JLabel("Apellido:");
        JTextField txtApellido = new JTextField(miembroActual.getApellido());

        JLabel lblCorreo = new JLabel("Correo:");
        JTextField txtCorreo = new JTextField(miembroActual.getCorreo());

        JLabel lblTelefono = new JLabel("Teléfono:");
        JTextField txtTelefono = new JTextField(miembroActual.getTelefono());

        JLabel lblContrasena = new JLabel("Contraseña:");
        JPasswordField txtContrasena = new JPasswordField(miembroActual.getContraseña());

        JLabel lblFechaNacimiento = new JLabel("Fecha de nacimiento:");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        JTextField txtFechaNacimiento = new JTextField(
                miembroActual.getFechaNacimiento() != null ?
                        sdf.format(miembroActual.getFechaNacimiento()) : ""
        );
        txtFechaNacimiento.setEditable(false);

        JLabel lblEdad = new JLabel("Edad:");
        JTextField txtEdad = new JTextField(String.valueOf(miembroActual.getEdad()));
        txtEdad.setEditable(false);

        JLabel lblPeso = new JLabel("Peso (kg):");
        JTextField txtPeso = new JTextField(String.valueOf(miembroActual.getPeso()));

        JLabel lblAltura = new JLabel("Altura (cm):");
        JTextField txtAltura = new JTextField(String.valueOf(miembroActual.getAltura()));

        JLabel lblObjetivos = new JLabel("Objetivos:");
        JTextArea txtObjetivos = new JTextArea(miembroActual.getObjetivos());
        txtObjetivos.setLineWrap(true);
        txtObjetivos.setWrapStyleWord(true);
        JScrollPane scrollObjetivos = new JScrollPane(txtObjetivos);

        JLabel lblMembresia = new JLabel("Membresía:");
        JTextField txtMembresia = new JTextField();

        if (miembroActual.getMembresia() != null) {
            String estado = miembroActual.getMembresia().isActiva() ?
                    (miembroActual.getMembresia().esVigente() ? "Activa" : "Vencida") :
                    "Cancelada";

            txtMembresia.setText(miembroActual.getMembresia().getTipo() + " - " + estado +
                    " (Vence: " + sdf.format(miembroActual.getMembresia().getFechaFin()) + ")");
        } else {
            txtMembresia.setText("No asignada");
        }
        txtMembresia.setEditable(false);

        panelInfo.add(lblNombre);
        panelInfo.add(txtNombre);
        panelInfo.add(lblApellido);
        panelInfo.add(txtApellido);
        panelInfo.add(lblCorreo);
        panelInfo.add(txtCorreo);
        panelInfo.add(lblTelefono);
        panelInfo.add(txtTelefono);
        panelInfo.add(lblContrasena);
        panelInfo.add(txtContrasena);
        panelInfo.add(lblFechaNacimiento);
        panelInfo.add(txtFechaNacimiento);
        panelInfo.add(lblEdad);
        panelInfo.add(txtEdad);
        panelInfo.add(lblPeso);
        panelInfo.add(txtPeso);
        panelInfo.add(lblAltura);
        panelInfo.add(txtAltura);
        panelInfo.add(lblMembresia);
        panelInfo.add(txtMembresia);
        panelInfo.add(lblObjetivos);
        panelInfo.add(scrollObjetivos);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton btnGuardar = new JButton("Guardar Cambios");

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validar campos
                if (txtNombre.getText().trim().isEmpty() ||
                        txtApellido.getText().trim().isEmpty() ||
                        txtCorreo.getText().trim().isEmpty() ||
                        txtTelefono.getText().trim().isEmpty() ||
                        txtPeso.getText().trim().isEmpty() ||
                        txtAltura.getText().trim().isEmpty()) {

                    JOptionPane.showMessageDialog(MiembroView.this,
                            "Todos los campos son obligatorios",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // Parsear valores numéricos
                    float peso = Float.parseFloat(txtPeso.getText().trim());
                    float altura = Float.parseFloat(txtAltura.getText().trim());

                    // Actualizar miembro
                    miembroActual.setNombre(txtNombre.getText().trim());
                    miembroActual.setApellido(txtApellido.getText().trim());
                    miembroActual.setCorreo(txtCorreo.getText().trim());
                    miembroActual.setTelefono(txtTelefono.getText().trim());
                    miembroActual.setContraseña(new String(txtContrasena.getPassword()));
                    miembroActual.setPeso(peso);
                    miembroActual.setAltura(altura);
                    miembroActual.setObjetivos(txtObjetivos.getText().trim());

                    JOptionPane.showMessageDialog(MiembroView.this,
                            "Perfil actualizado correctamente",
                            "Información", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MiembroView.this,
                            "Formato de número incorrecto en peso o altura",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panelBotones.add(btnGuardar);

        // Agregar componentes al panel
        panelPerfil.add(panelInfo, BorderLayout.CENTER);
        panelPerfil.add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarDatos() {
        cargarProgramas();
        cargarTablaProgreso();
        cargarTablaAsistencia();
    }

    private void cargarProgramas() {
        // Limpiar combo
        cmbProgramas.removeAllItems();

        // Obtener programas asignados al miembro
        List<ProgramaEntrenamiento> programas = miembroActual.getProgramasAsignados();

        // Agregar programas al combo
        for (ProgramaEntrenamiento programa : programas) {
            cmbProgramas.addItem(programa.getNombre() + " (" + programa.getTipo().getNombre() + ")");
        }

        // Si no hay programas, agregar mensaje
        if (programas.isEmpty()) {
            cmbProgramas.addItem("No hay programas asignados");
            txtAreaRutina.setText("No tiene programas de entrenamiento asignados.\n\n" +
                    "Contacte a su entrenador para que le asigne un programa.");
            btnRegistrarCompletada.setEnabled(false);
        } else {
            // Cargar primera rutina
            cargarRutina();
            btnRegistrarCompletada.setEnabled(true);
        }
    }

    private void cargarRutina() {
        int indice = cmbProgramas.getSelectedIndex();

        if (indice >= 0 && !miembroActual.getProgramasAsignados().isEmpty()) {
            // Obtener programa seleccionado
            ProgramaEntrenamiento programa = miembroActual.getProgramasAsignados().get(indice);

            // Obtener rutinas del programa
            List<Rutina> rutinas = programa.getRutinas();

            if (rutinas.isEmpty()) {
                txtAreaRutina.setText("El programa no tiene rutinas definidas.\n\n" +
                        "Contacte a su entrenador para que agregue rutinas al programa.");
            } else {
                // Construir texto de la rutina
                StringBuilder rutinaText = new StringBuilder();

                rutinaText.append("PROGRAMA: ").append(programa.getNombre()).append("\n");
                rutinaText.append("Tipo: ").append(programa.getTipo().getNombre()).append("\n");
                rutinaText.append("Duración: ").append(programa.getDuracionSemanas()).append(" semanas\n\n");

                rutinaText.append("RUTINAS:\n");
                rutinaText.append("========\n\n");

                for (Rutina rutina : rutinas) {
                    rutinaText.append("DÍA: ").append(rutina.getDia().getNombre()).append("\n");
                    rutinaText.append("Rutina: ").append(rutina.getNombre()).append("\n");
                    rutinaText.append("Descripción: ").append(rutina.getDescripcion()).append("\n");
                    rutinaText.append("Duración: ").append(rutina.getDuracionMinutos()).append(" minutos\n\n");

                    rutinaText.append("Ejercicios:\n");

                    List<Ejercicio> ejercicios = rutina.getEjercicios();
                    if (ejercicios.isEmpty()) {
                        rutinaText.append("- No hay ejercicios definidos para esta rutina\n");
                    } else {
                        for (Ejercicio ejercicio : ejercicios) {
                            rutinaText.append("- ").append(ejercicio.getNombre())
                                    .append(" (").append(ejercicio.getGrupoMuscular()).append(")\n");
                            rutinaText.append("  ").append(ejercicio.getSeries()).append(" series x ")
                                    .append(ejercicio.getRepeticiones()).append(" repeticiones");

                            if (ejercicio.getPeso() > 0) {
                                rutinaText.append(" x ").append(ejercicio.getPeso()).append(" kg");
                            }

                            rutinaText.append("\n");

                            if (ejercicio.getDescripcion() != null && !ejercicio.getDescripcion().isEmpty()) {
                                rutinaText.append("  Descripción: ").append(ejercicio.getDescripcion()).append("\n");
                            }

                            if (ejercicio.getEquipoNecesario() != null && !ejercicio.getEquipoNecesario().isEmpty()) {
                                rutinaText.append("  Equipo: ").append(ejercicio.getEquipoNecesario()).append("\n");
                            }

                            rutinaText.append("\n");
                        }
                    }

                    rutinaText.append("----------------------\n\n");
                }

                txtAreaRutina.setText(rutinaText.toString());
                txtAreaRutina.setCaretPosition(0);
            }
        }
    }

    private void cargarTablaProgreso() {
        // Limpiar tabla
        modeloTablaProgreso.setRowCount(0);

        // Obtener historial de progreso
        List<RegistroProgreso> historialProgreso = miembroActual.verMetricasProgreso();

        // Formato de fecha
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Agregar datos a la tabla
        for (RegistroProgreso registro : historialProgreso) {
            // Calcular IMC
            float imc = registro.getPeso() / (miembroActual.getAltura() / 100.0f * miembroActual.getAltura() / 100.0f);

            // Construir string de medidas
            StringBuilder medidas = new StringBuilder();
            Map<String, Float> mapaMedias = registro.getMedidas();
            for (Map.Entry<String, Float> entry : mapaMedias.entrySet()) {
                if (medidas.length() > 0) {
                    medidas.append(", ");
                }
                medidas.append(entry.getKey()).append(": ").append(entry.getValue()).append(" cm");
            }

            Object[] fila = {
                    sdf.format(registro.getFecha()),
                    registro.getPeso() + " kg",
                    String.format("%.1f", imc),
                    medidas.toString(),
                    registro.getComentarios()
            };

            modeloTablaProgreso.addRow(fila);
        }

        if (historialProgreso.isEmpty()) {
            btnRegistrarProgreso.setText("Registrar Primer Progreso");
        } else {
            btnRegistrarProgreso.setText("Registrar Nuevo Progreso");
        }
    }

    private void cargarTablaAsistencia() {
        // Limpiar tabla
        modeloTablaAsistencia.setRowCount(0);

        // Obtener historial de asistencia
        List<RegistroAsistencia> historialAsistencia = miembroActual.verHistorialAsistencia();

        // Formato de fecha
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Agregar datos a la tabla
        for (RegistroAsistencia registro : historialAsistencia) {
            Object[] fila = {
                    sdf.format(registro.getFecha()),
                    registro.getHoraEntrada(),
                    registro.isCompletada() ? registro.getHoraSalida() : "No registrada",
                    registro.isCompletada() ? registro.getDuracionFormateada() : "N/A"
            };

            modeloTablaAsistencia.addRow(fila);
        }
    }

    private void registrarRutinaCompletada() {
        int indice = cmbProgramas.getSelectedIndex();

        if (indice >= 0 && !miembroActual.getProgramasAsignados().isEmpty()) {
            // Obtener programa seleccionado
            ProgramaEntrenamiento programa = miembroActual.getProgramasAsignados().get(indice);

            // Mostrar diálogo de confirmación
            JOptionPane.showMessageDialog(this,
                    "Rutina marcada como completada. ¡Felicitaciones!",
                    "Rutina Completada", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void registrarProgreso() {
        JDialog dialog = new JDialog(this, "Registrar Progreso", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Panel de formulario
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new GridLayout(6, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblPeso = new JLabel("Peso (kg):");
        JTextField txtPeso = new JTextField(String.valueOf(miembroActual.getPeso()));

        JLabel lblPecho = new JLabel("Pecho (cm):");
        JTextField txtPecho = new JTextField();

        JLabel lblCintura = new JLabel("Cintura (cm):");
        JTextField txtCintura = new JTextField();

        JLabel lblBrazos = new JLabel("Brazos (cm):");
        JTextField txtBrazos = new JTextField();

        JLabel lblPiernas = new JLabel("Piernas (cm):");
        JTextField txtPiernas = new JTextField();

        JLabel lblComentarios = new JLabel("Comentarios:");
        JTextArea txtComentarios = new JTextArea();
        txtComentarios.setLineWrap(true);
        txtComentarios.setWrapStyleWord(true);
        JScrollPane scrollComentarios = new JScrollPane(txtComentarios);

        panelFormulario.add(lblPeso);
        panelFormulario.add(txtPeso);
        panelFormulario.add(lblPecho);
        panelFormulario.add(txtPecho);
        panelFormulario.add(lblCintura);
        panelFormulario.add(txtCintura);
        panelFormulario.add(lblBrazos);
        panelFormulario.add(txtBrazos);
        panelFormulario.add(lblPiernas);
        panelFormulario.add(txtPiernas);
        panelFormulario.add(lblComentarios);
        panelFormulario.add(scrollComentarios);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validar campos
                if (txtPeso.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog,
                            "El peso es obligatorio",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // Parsear peso
                    float peso = Float.parseFloat(txtPeso.getText().trim());

                    // Actualizar peso del miembro
                    miembroActual.setPeso(peso);

                    // Crear mapa de medidas
                    Map<String, Float> medidas = new HashMap<>();

                    // Agregar medidas si no están vacías
                    if (!txtPecho.getText().trim().isEmpty()) {
                        medidas.put("Pecho", Float.parseFloat(txtPecho.getText().trim()));
                    }

                    if (!txtCintura.getText().trim().isEmpty()) {
                        medidas.put("Cintura", Float.parseFloat(txtCintura.getText().trim()));
                    }

                    if (!txtBrazos.getText().trim().isEmpty()) {
                        medidas.put("Brazos", Float.parseFloat(txtBrazos.getText().trim()));
                    }

                    if (!txtPiernas.getText().trim().isEmpty()) {
                        medidas.put("Piernas", Float.parseFloat(txtPiernas.getText().trim()));
                    }

                    // Registrar progreso
                    RegistroProgreso registro = controller.registrarProgreso(
                            miembroActual,
                            peso,
                            medidas,
                            txtComentarios.getText().trim()
                    );

                    if (registro != null) {
                        JOptionPane.showMessageDialog(dialog,
                                "Progreso registrado correctamente",
                                "Información", JOptionPane.INFORMATION_MESSAGE);

                        cargarTablaProgreso();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog,
                                "Error al registrar progreso",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Formato de número incorrecto",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        // Agregar paneles al panel principal
        panel.add(panelFormulario, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        dialog.getContentPane().add(panel);
        dialog.setVisible(true);
    }

    private void cerrarSesion() {
        controller.cerrarSesion();

        LoginView loginView = new LoginView(controller);
        loginView.setVisible(true);

        this.dispose();
    }
}