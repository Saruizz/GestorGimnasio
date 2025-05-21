// EntrenadorView.java
package main.java.view;

import main.java.controller.SistemaController;
import main.java.model.*;
import main.java.util.DiaSemana;
import main.java.util.TipoPrograma;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Vista principal para el Entrenador
 */
public class EntrenadorView extends JFrame {

    private SistemaController controller;
    private Entrenador entrenadorActual;


    private JTabbedPane tabbedPane;
    private JPanel panelMiembros;
    private JPanel panelProgramas;
    private JPanel panelHorarios;
    private JPanel panelSeguimiento;

    // Componentes para panel de miembros
    private JTable tablaMiembros;
    private DefaultTableModel modeloTablaMiembros;
    private JButton btnRegistrarAsistencia;
    private JButton btnSeguimientoProgreso;
    private JButton btnGenerarRutina;

    // Componentes para panel de programas
    private JTable tablaProgramas;
    private DefaultTableModel modeloTablaProgramas;
    private JButton btnCrearPrograma;
    private JButton btnEditarPrograma;
    private JButton btnVerRutinas;

    private JTable tablaRutinas;
    private DefaultTableModel modeloTablaRutinas;

    // Componentes para panel de horarios
    private JTable tablaHorarios;
    private DefaultTableModel modeloTablaHorarios;
    private JButton btnAgregarHorario;
    private JButton btnEliminarHorario;

    // Componentes para panel de seguimiento
    private JComboBox<String> cmbMiembroSeguimiento;
    private JTextArea txtAreaSeguimiento;
    private JButton btnRegistrarProgreso;

    public EntrenadorView(SistemaController controller) {
        this.controller = controller;
        this.entrenadorActual = (Entrenador) controller.getUsuarioActual();
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        // Configurar ventana
        setTitle("Sistema de Gestión de Gimnasio - Entrenador");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Panel principal con pestañas
        tabbedPane = new JTabbedPane();

        // Inicializar paneles
        panelMiembros = new JPanel();
        panelProgramas = new JPanel();
        panelHorarios = new JPanel();
        panelSeguimiento = new JPanel();

        // Configurar paneles
        configurarPanelMiembros();
        configurarPanelProgramas();
        configurarPanelHorarios();
        configurarPanelSeguimiento();

        // Agregar paneles a las pestañas
        tabbedPane.addTab("Miembros", new ImageIcon(), panelMiembros, "Gestión de miembros asignados");
        tabbedPane.addTab("Programas", new ImageIcon(), panelProgramas, "Gestión de programas de entrenamiento");
        tabbedPane.addTab("Horarios", new ImageIcon(), panelHorarios, "Gestión de horarios disponibles");
        tabbedPane.addTab("Seguimiento", new ImageIcon(), panelSeguimiento, "Seguimiento de progreso de miembros");

        // Agregar listener para cambio de pestaña
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                switch (selectedIndex) {
                    case 0: // Miembros
                        cargarTablaMiembros();
                        break;
                    case 1: // Programas
                        cargarTablaProgramas();
                        break;
                    case 2: // Horarios
                        cargarTablaHorarios();
                        break;
                    case 3: // Seguimiento
                        cargarComboMiembros();
                        break;
                }
            }
        });

        // Crear barra de menú
        JMenuBar menuBar = new JMenuBar();

        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemPerfil = new JMenuItem("Mi Perfil");
        JMenuItem itemSalir = new JMenuItem("Cerrar Sesión");

        itemPerfil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarPerfilEntrenador();
            }
        });

        itemSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarSesion();
            }
        });

        menuArchivo.add(itemPerfil);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);

        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem itemAcerca = new JMenuItem("Acerca de");
        itemAcerca.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(EntrenadorView.this,
                        "Sistema de Gestión de Gimnasio\nVersión 1.0\nDesarrollado para la materia de POO",
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

    private void configurarPanelMiembros() {
        panelMiembros.setLayout(new BorderLayout());

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.LEFT));

        btnRegistrarAsistencia = new JButton("Registrar Asistencia");
        btnSeguimientoProgreso = new JButton("Seguimiento");
        btnGenerarRutina = new JButton("Generar Rutina");

        btnRegistrarAsistencia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarAsistenciaMiembro();
            }
        });

        btnSeguimientoProgreso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seguimientoProgresoMiembro();
            }
        });

        btnGenerarRutina.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarRutinaMiembro();
            }
        });

        panelBotones.add(btnRegistrarAsistencia);
        panelBotones.add(btnSeguimientoProgreso);
        panelBotones.add(btnGenerarRutina);

        // Tabla de miembros
        String[] columnas = {"ID", "Nombre", "Apellido", "Edad", "Objetivos", "Programas", "Última Asistencia"};
        modeloTablaMiembros = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No permitir edición directa
            }
        };

        tablaMiembros = new JTable(modeloTablaMiembros);
        tablaMiembros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaMiembros.getTableHeader().setReorderingAllowed(false);

        // Agregar doble clic en la tabla
        tablaMiembros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    verDetalleMiembro();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaMiembros);

        // Agregar componentes al panel
        panelMiembros.add(panelBotones, BorderLayout.NORTH);
        panelMiembros.add(scrollPane, BorderLayout.CENTER);
    }

    private void configurarPanelProgramas() {
        panelProgramas.setLayout(new BorderLayout());

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.LEFT));

        btnCrearPrograma = new JButton("Crear Programa");
        btnEditarPrograma = new JButton("Editar Programa");
        btnVerRutinas = new JButton("Ver Rutinas");

        btnCrearPrograma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearPrograma();
            }
        });

        btnEditarPrograma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarPrograma();
            }
        });

        btnVerRutinas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verRutinasPrograma();
            }
        });

        panelBotones.add(btnCrearPrograma);
        panelBotones.add(btnEditarPrograma);
        panelBotones.add(btnVerRutinas);

        // Tabla de programas
        String[] columnas = {"ID", "Nombre", "Descripción", "Duración", "Tipo", "Rutinas", "Miembros"};
        modeloTablaProgramas = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No permitir edición directa
            }
        };

        tablaProgramas = new JTable(modeloTablaProgramas);
        tablaProgramas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaProgramas.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tablaProgramas);

        // Agregar componentes al panel
        panelProgramas.add(panelBotones, BorderLayout.NORTH);
        panelProgramas.add(scrollPane, BorderLayout.CENTER);
    }

    private void configurarPanelHorarios() {
        panelHorarios.setLayout(new BorderLayout());

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.LEFT));

        btnAgregarHorario = new JButton("Agregar Horario");
        btnEliminarHorario = new JButton("Eliminar Horario");

        btnAgregarHorario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarHorario();
            }
        });

        btnEliminarHorario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarHorario();
            }
        });

        panelBotones.add(btnAgregarHorario);
        panelBotones.add(btnEliminarHorario);

        // Tabla de horarios
        String[] columnas = {"ID", "Día", "Hora Inicio", "Hora Fin"};
        modeloTablaHorarios = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No permitir edición directa
            }
        };

        tablaHorarios = new JTable(modeloTablaHorarios);
        tablaHorarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaHorarios.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tablaHorarios);

        // Agregar componentes al panel
        panelHorarios.add(panelBotones, BorderLayout.NORTH);
        panelHorarios.add(scrollPane, BorderLayout.CENTER);
    }

    private void configurarPanelSeguimiento() {
        panelSeguimiento.setLayout(new BorderLayout());

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel lblMiembro = new JLabel("Miembro:");
        cmbMiembroSeguimiento = new JComboBox<>();
        btnRegistrarProgreso = new JButton("Registrar Progreso");

        btnRegistrarProgreso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarProgresoMiembro();
            }
        });

        cmbMiembroSeguimiento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarSeguimientoMiembro();
            }
        });

        panelSuperior.add(lblMiembro);
        panelSuperior.add(cmbMiembroSeguimiento);
        panelSuperior.add(btnRegistrarProgreso);

        // Panel central
        txtAreaSeguimiento = new JTextArea();
        txtAreaSeguimiento.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(txtAreaSeguimiento);

        // Agregar componentes al panel
        panelSeguimiento.add(panelSuperior, BorderLayout.NORTH);
        panelSeguimiento.add(scrollPane, BorderLayout.CENTER);
    }

    private void cargarDatos() {
        cargarTablaMiembros();
        cargarTablaProgramas();
        cargarTablaHorarios();
        cargarComboMiembros();
    }

    private void cargarTablaMiembros() {
        // Limpiar tabla
        modeloTablaMiembros.setRowCount(0);

        // Obtener miembros asignados al entrenador
        List<Miembro> miembros = entrenadorActual.getMiembrosAsignados();

        // Formato de fecha
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Agregar datos a la tabla
        for (Miembro miembro : miembros) {
            // Obtener última asistencia
            String ultimaAsistencia = "No registrada";
            List<RegistroAsistencia> asistencias = miembro.verHistorialAsistencia();
            if (!asistencias.isEmpty()) {
                RegistroAsistencia ultima = asistencias.get(asistencias.size() - 1);
                ultimaAsistencia = sdf.format(ultima.getFecha());
            }

            Object[] fila = {
                    miembro.getId(),
                    miembro.getNombre(),
                    miembro.getApellido(),
                    miembro.getEdad(),
                    miembro.getObjetivos(),
                    miembro.getProgramasAsignados().size(),
                    ultimaAsistencia
            };

            modeloTablaMiembros.addRow(fila);
        }
    }

    private void cargarTablaProgramas() {
        // Limpiar tabla
        modeloTablaProgramas.setRowCount(0);

        // Obtener programas creados por el entrenador
        List<ProgramaEntrenamiento> programas = entrenadorActual.getProgramasCreados();

        // Agregar datos a la tabla
        for (ProgramaEntrenamiento programa : programas) {
            Object[] fila = {
                    programa.getId(),
                    programa.getNombre(),
                    programa.getDescripcion(),
                    programa.getDuracionSemanas() + " semanas",
                    programa.getTipo().getNombre(),
                    programa.getRutinas().size(),
                    programa.getMiembrosAsignados().size()
            };

            modeloTablaProgramas.addRow(fila);
        }
    }

    private void cargarTablaHorarios() {
        // Limpiar tabla
        modeloTablaHorarios.setRowCount(0);

        // Obtener horarios disponibles del entrenador
        List<HorarioDisponible> horarios = entrenadorActual.getHorariosDisponibles();

        // Agregar datos a la tabla
        for (HorarioDisponible horario : horarios) {
            Object[] fila = {
                    horario.getId(),
                    horario.getDia().getNombre(),
                    horario.getHoraInicio().toString(),
                    horario.getHoraFin().toString()
            };

            modeloTablaHorarios.addRow(fila);
        }
    }

    private void cargarComboMiembros() {
        // Limpiar combo
        cmbMiembroSeguimiento.removeAllItems();

        // Obtener miembros asignados al entrenador
        List<Miembro> miembros = entrenadorActual.getMiembrosAsignados();

        // Agregar miembros al combo
        for (Miembro miembro : miembros) {
            cmbMiembroSeguimiento.addItem(miembro.getNombre() + " " + miembro.getApellido());
        }

        // Si hay miembros, cargar seguimiento del primero
        if (!miembros.isEmpty()) {
            cargarSeguimientoMiembro();
        }
    }

    private void cargarSeguimientoMiembro() {
        int indice = cmbMiembroSeguimiento.getSelectedIndex();

        if (indice >= 0) {
            // Obtener miembro seleccionado
            List<Miembro> miembros = entrenadorActual.getMiembrosAsignados();
            Miembro miembro = miembros.get(indice);

            // Construir texto de seguimiento
            StringBuilder seguimiento = new StringBuilder();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            seguimiento.append("SEGUIMIENTO DE PROGRESO\n");
            seguimiento.append("=====================\n\n");
            seguimiento.append("Miembro: ").append(miembro.getNombre()).append(" ").append(miembro.getApellido()).append("\n");
            seguimiento.append("Edad: ").append(miembro.getEdad()).append(" años\n");
            seguimiento.append("Objetivos: ").append(miembro.getObjetivos()).append("\n\n");

            // Datos actuales
            seguimiento.append("DATOS ACTUALES\n");
            seguimiento.append("-------------\n");
            seguimiento.append("Peso: ").append(miembro.getPeso()).append(" kg\n");
            seguimiento.append("Altura: ").append(miembro.getAltura()).append(" cm\n");
            seguimiento.append("IMC: ").append(Math.round((miembro.getPeso() / Math.pow(miembro.getAltura() / 100.0, 2)) * 10) / 10.0).append("\n\n");

            // Historial de progreso
            List<RegistroProgreso> historialProgreso = miembro.verMetricasProgreso();

            if (historialProgreso.isEmpty()) {
                seguimiento.append("No hay registros de progreso\n");
            } else {
                seguimiento.append("HISTORIAL DE PROGRESO\n");
                seguimiento.append("--------------------\n");

                for (RegistroProgreso registro : historialProgreso) {
                    seguimiento.append("Fecha: ").append(sdf.format(registro.getFecha())).append("\n");
                    seguimiento.append("Peso: ").append(registro.getPeso()).append(" kg\n");

                    Map<String, Float> medidas = registro.getMedidas();
                    if (!medidas.isEmpty()) {
                        seguimiento.append("Medidas:\n");
                        for (Map.Entry<String, Float> entry : medidas.entrySet()) {
                            seguimiento.append("- ").append(entry.getKey()).append(": ").append(entry.getValue()).append(" cm\n");
                        }
                    }

                    if (registro.getComentarios() != null && !registro.getComentarios().isEmpty()) {
                        seguimiento.append("Comentarios: ").append(registro.getComentarios()).append("\n");
                    }

                    seguimiento.append("\n");
                }
            }

            // Asistencias recientes
            List<RegistroAsistencia> asistencias = miembro.verHistorialAsistencia();

            if (asistencias.isEmpty()) {
                seguimiento.append("No hay registros de asistencia\n");
            } else {
                seguimiento.append("ASISTENCIAS RECIENTES\n");
                seguimiento.append("---------------------\n");

                // Mostrar las últimas 5 asistencias
                int inicio = Math.max(0, asistencias.size() - 5);
                for (int i = asistencias.size() - 1; i >= inicio; i--) {
                    RegistroAsistencia asistencia = asistencias.get(i);
                    seguimiento.append("Fecha: ").append(sdf.format(asistencia.getFecha())).append("\n");
                    seguimiento.append("Entrada: ").append(asistencia.getHoraEntrada()).append("\n");

                    if (asistencia.isCompletada()) {
                        seguimiento.append("Salida: ").append(asistencia.getHoraSalida()).append("\n");
                        seguimiento.append("Duración: ").append(asistencia.getDuracionFormateada()).append("\n");
                    } else {
                        seguimiento.append("Salida: No registrada\n");
                    }

                    seguimiento.append("\n");
                }
            }

            txtAreaSeguimiento.setText(seguimiento.toString());
            txtAreaSeguimiento.setCaretPosition(0);
        } else {
            txtAreaSeguimiento.setText("No hay miembros asignados");
        }
    }

    private void mostrarPerfilEntrenador() {
        JDialog dialog = new JDialog(this, "Mi Perfil", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Panel de información
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new GridLayout(6, 2, 10, 10));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblNombre = new JLabel("Nombre:");
        JLabel lblNombreValor = new JLabel(entrenadorActual.getNombre() + " " + entrenadorActual.getApellido());

        JLabel lblCorreo = new JLabel("Correo:");
        JLabel lblCorreoValor = new JLabel(entrenadorActual.getCorreo());

        JLabel lblTelefono = new JLabel("Teléfono:");
        JLabel lblTelefonoValor = new JLabel(entrenadorActual.getTelefono());

        JLabel lblEspecialidades = new JLabel("Especialidades:");
        JLabel lblEspecialidadesValor = new JLabel();

        // Construir string de especialidades
        StringBuilder especialidades = new StringBuilder();
        for (String especialidad : entrenadorActual.getEspecialidades()) {
            if (especialidades.length() > 0) {
                especialidades.append(", ");
            }
            especialidades.append(especialidad);
        }
        lblEspecialidadesValor.setText(especialidades.toString());

        JLabel lblMiembros = new JLabel("Miembros asignados:");
        JLabel lblMiembrosValor = new JLabel(String.valueOf(entrenadorActual.getMiembrosAsignados().size()));

        JLabel lblProgramas = new JLabel("Programas creados:");
        JLabel lblProgramasValor = new JLabel(String.valueOf(entrenadorActual.getProgramasCreados().size()));

        panelInfo.add(lblNombre);
        panelInfo.add(lblNombreValor);
        panelInfo.add(lblCorreo);
        panelInfo.add(lblCorreoValor);
        panelInfo.add(lblTelefono);
        panelInfo.add(lblTelefonoValor);
        panelInfo.add(lblEspecialidades);
        panelInfo.add(lblEspecialidadesValor);
        panelInfo.add(lblMiembros);
        panelInfo.add(lblMiembrosValor);
        panelInfo.add(lblProgramas);
        panelInfo.add(lblProgramasValor);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        panelBotones.add(btnCerrar);

        // Agregar componentes al panel principal
        panel.add(panelInfo, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        dialog.getContentPane().add(panel);
        dialog.setVisible(true);
    }

    private void verDetalleMiembro() {
        int filaSeleccionada = tablaMiembros.getSelectedRow();

        if (filaSeleccionada >= 0) {
            int idMiembro = (Integer) tablaMiembros.getValueAt(filaSeleccionada, 0);
            Miembro miembro = buscarMiembroPorId(idMiembro);

            if (miembro != null) {
                JDialog dialog = new JDialog(this, "Detalle del Miembro", true);
                dialog.setSize(500, 400);
                dialog.setLocationRelativeTo(this);
                dialog.setResizable(false);

                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());

                // Panel de información
                JPanel panelInfo = new JPanel();
                panelInfo.setLayout(new GridLayout(10, 2, 10, 10));
                panelInfo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                JLabel lblNombre = new JLabel("Nombre:");
                JLabel lblNombreValor = new JLabel(miembro.getNombre() + " " + miembro.getApellido());

                JLabel lblCorreo = new JLabel("Correo:");
                JLabel lblCorreoValor = new JLabel(miembro.getCorreo());

                JLabel lblTelefono = new JLabel("Teléfono:");
                JLabel lblTelefonoValor = new JLabel(miembro.getTelefono());

                JLabel lblEdad = new JLabel("Edad:");
                JLabel lblEdadValor = new JLabel(String.valueOf(miembro.getEdad()));

                JLabel lblPeso = new JLabel("Peso (kg):");
                JLabel lblPesoValor = new JLabel(String.valueOf(miembro.getPeso()));

                JLabel lblAltura = new JLabel("Altura (cm):");
                JLabel lblAlturaValor = new JLabel(String.valueOf(miembro.getAltura()));

                JLabel lblImc = new JLabel("IMC:");
                float imc = miembro.getPeso() / (miembro.getAltura() / 100.0f * miembro.getAltura() / 100.0f);
                JLabel lblImcValor = new JLabel(String.format("%.1f", imc));

                JLabel lblObjetivos = new JLabel("Objetivos:");
                JTextArea txtObjetivos = new JTextArea(miembro.getObjetivos());
                txtObjetivos.setEditable(false);
                txtObjetivos.setLineWrap(true);
                txtObjetivos.setWrapStyleWord(true);
                JScrollPane scrollObjetivos = new JScrollPane(txtObjetivos);

                JLabel lblMembresia = new JLabel("Membresía:");
                JLabel lblMembresiaValor = new JLabel();

                if (miembro.getMembresia() != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String estado = miembro.getMembresia().isActiva() ?
                            (miembro.getMembresia().esVigente() ? "Activa" : "Vencida") :
                            "Cancelada";

                    lblMembresiaValor.setText(miembro.getMembresia().getTipo() + " - " + estado +
                            " (Vence: " + sdf.format(miembro.getMembresia().getFechaFin()) + ")");
                } else {
                    lblMembresiaValor.setText("No asignada");
                }

                JLabel lblProgramas = new JLabel("Programas asignados:");
                JComboBox<String> cmbProgramas = new JComboBox<>();

                for (ProgramaEntrenamiento programa : miembro.getProgramasAsignados()) {
                    cmbProgramas.addItem(programa.getNombre() + " (" + programa.getTipo().getNombre() + ")");
                }

                if (miembro.getProgramasAsignados().isEmpty()) {
                    cmbProgramas.addItem("No tiene programas asignados");
                }

                panelInfo.add(lblNombre);
                panelInfo.add(lblNombreValor);
                panelInfo.add(lblCorreo);
                panelInfo.add(lblCorreoValor);
                panelInfo.add(lblTelefono);
                panelInfo.add(lblTelefonoValor);
                panelInfo.add(lblEdad);
                panelInfo.add(lblEdadValor);
                panelInfo.add(lblPeso);
                panelInfo.add(lblPesoValor);
                panelInfo.add(lblAltura);
                panelInfo.add(lblAlturaValor);
                panelInfo.add(lblImc);
                panelInfo.add(lblImcValor);
                panelInfo.add(lblMembresia);
                panelInfo.add(lblMembresiaValor);
                panelInfo.add(lblProgramas);
                panelInfo.add(cmbProgramas);
                panelInfo.add(lblObjetivos);
                panelInfo.add(scrollObjetivos);

                // Panel de botones
                JPanel panelBotones = new JPanel();
                panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER));

                JButton btnCerrar = new JButton("Cerrar");
                btnCerrar.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dialog.dispose();
                    }
                });

                panelBotones.add(btnCerrar);

                // Agregar componentes al panel principal
                panel.add(panelInfo, BorderLayout.CENTER);
                panel.add(panelBotones, BorderLayout.SOUTH);

                dialog.getContentPane().add(panel);
                dialog.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un miembro",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarAsistenciaMiembro() {
        int filaSeleccionada = tablaMiembros.getSelectedRow();

        if (filaSeleccionada >= 0) {
            int idMiembro = (Integer) tablaMiembros.getValueAt(filaSeleccionada, 0);
            Miembro miembro = buscarMiembroPorId(idMiembro);

            if (miembro != null) {
                RegistroAsistencia registroAsistencia = controller.registrarAsistencia(miembro);

                if (registroAsistencia != null) {
                    JOptionPane.showMessageDialog(this,
                            "Asistencia registrada correctamente para " + miembro.getNombre() + " " + miembro.getApellido(),
                            "Información", JOptionPane.INFORMATION_MESSAGE);

                    cargarTablaMiembros();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Error al registrar asistencia",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un miembro",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void seguimientoProgresoMiembro() {
        int filaSeleccionada = tablaMiembros.getSelectedRow();

        if (filaSeleccionada >= 0) {
            int idMiembro = (Integer) tablaMiembros.getValueAt(filaSeleccionada, 0);
            Miembro miembro = buscarMiembroPorId(idMiembro);

            if (miembro != null) {
                // Seleccionar el miembro en el combo de seguimiento
                List<Miembro> miembros = entrenadorActual.getMiembrosAsignados();
                for (int i = 0; i < miembros.size(); i++) {
                    if (miembros.get(i).getId() == miembro.getId()) {
                        cmbMiembroSeguimiento.setSelectedIndex(i);
                        break;
                    }
                }

                // Cambiar a la pestaña de seguimiento
                tabbedPane.setSelectedIndex(3);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un miembro",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generarRutinaMiembro() {
        int filaSeleccionada = tablaMiembros.getSelectedRow();

        if (filaSeleccionada >= 0) {
            int idMiembro = (Integer) tablaMiembros.getValueAt(filaSeleccionada, 0);
            Miembro miembro = buscarMiembroPorId(idMiembro);

            if (miembro != null) {
                // Implementar lógica para generar rutina personalizada
                RutinaPersonalizada rutina = entrenadorActual.generarRutinaPersonalizada(miembro);

                if (rutina != null) {
                    // Asignar rutina al miembro
                    miembro.asignarPrograma(rutina);

                    JOptionPane.showMessageDialog(this,
                            "Rutina personalizada generada correctamente para " +
                                    miembro.getNombre() + " " + miembro.getApellido(),
                            "Información", JOptionPane.INFORMATION_MESSAGE);

                    // Mostrar detalle de la rutina
                    JDialog dialog = new JDialog(this, "Rutina Personalizada", true);
                    dialog.setSize(600, 400);
                    dialog.setLocationRelativeTo(this);

                    JPanel panel = new JPanel();
                    panel.setLayout(new BorderLayout());

                    // Panel de información
                    JPanel panelInfo = new JPanel();
                    panelInfo.setLayout(new GridLayout(4, 2, 10, 10));
                    panelInfo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                    JLabel lblNombre = new JLabel("Nombre:");
                    JLabel lblNombreValor = new JLabel(rutina.getNombre());

                    JLabel lblDescripcion = new JLabel("Descripción:");
                    JLabel lblDescripcionValor = new JLabel(rutina.getDescripcion());

                    JLabel lblTipo = new JLabel("Tipo:");
                    JLabel lblTipoValor = new JLabel(rutina.getTipo().getNombre());

                    JLabel lblDuracion = new JLabel("Duración:");
                    JLabel lblDuracionValor = new JLabel(rutina.getDuracionSemanas() + " semanas");

                    panelInfo.add(lblNombre);
                    panelInfo.add(lblNombreValor);
                    panelInfo.add(lblDescripcion);
                    panelInfo.add(lblDescripcionValor);
                    panelInfo.add(lblTipo);
                    panelInfo.add(lblTipoValor);
                    panelInfo.add(lblDuracion);
                    panelInfo.add(lblDuracionValor);

                    // Tabla de ejercicios
                    String[] columnas = {"Ejercicio", "Grupo Muscular", "Series", "Repeticiones", "Peso"};
                    DefaultTableModel modeloTablaEjercicios = new DefaultTableModel(columnas, 0) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return false; // No permitir edición directa
                        }
                    };

                    JTable tablaEjercicios = new JTable(modeloTablaEjercicios);
                    tablaEjercicios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    tablaEjercicios.getTableHeader().setReorderingAllowed(false);

                    JScrollPane scrollEjercicios = new JScrollPane(tablaEjercicios);

                    // Cargar ejercicios en la tabla
                    List<Ejercicio> ejercicios = rutina.generarEjerciciosAdaptados();
                    for (Ejercicio ejercicio : ejercicios) {
                        Object[] fila = {
                                ejercicio.getNombre(),
                                ejercicio.getGrupoMuscular(),
                                ejercicio.getSeries(),
                                ejercicio.getRepeticiones(),
                                ejercicio.getPeso() + " kg"
                        };

                        modeloTablaEjercicios.addRow(fila);
                    }

                    // Panel de botones
                    JPanel panelBotones = new JPanel();
                    panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER));

                    JButton btnCerrar = new JButton("Cerrar");
                    btnCerrar.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            dialog.dispose();
                        }
                    });

                    panelBotones.add(btnCerrar);

                    // Agregar componentes al panel principal
                    panel.add(panelInfo, BorderLayout.NORTH);
                    panel.add(scrollEjercicios, BorderLayout.CENTER);
                    panel.add(panelBotones, BorderLayout.SOUTH);

                    dialog.getContentPane().add(panel);
                    dialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Error al generar rutina personalizada",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un miembro",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearPrograma() {
        JDialog dialog = new JDialog(this, "Crear Programa de Entrenamiento", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField();

        JLabel lblDescripcion = new JLabel("Descripción:");
        JTextArea txtDescripcion = new JTextArea();
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);

        JLabel lblDuracion = new JLabel("Duración (semanas):");
        JTextField txtDuracion = new JTextField();

        JLabel lblTipo = new JLabel("Tipo:");
        JComboBox<String> cmbTipo = new JComboBox<>();
        for (TipoPrograma tipo : TipoPrograma.values()) {
            cmbTipo.addItem(tipo.getNombre());
        }

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validar campos
                if (txtNombre.getText().trim().isEmpty() ||
                        txtDescripcion.getText().trim().isEmpty() ||
                        txtDuracion.getText().trim().isEmpty()) {

                    JOptionPane.showMessageDialog(dialog,
                            "Todos los campos son obligatorios",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // Parsear duración
                    int duracion = Integer.parseInt(txtDuracion.getText().trim());

                    // Obtener tipo seleccionado
                    TipoPrograma tipoSeleccionado = TipoPrograma.values()[cmbTipo.getSelectedIndex()];

                    // Crear programa
                    ProgramaEntrenamiento programa = controller.crearProgramaEntrenamiento(
                            txtNombre.getText().trim(),
                            txtDescripcion.getText().trim(),
                            duracion,
                            tipoSeleccionado
                    );

                    if (programa != null) {
                        JOptionPane.showMessageDialog(dialog,
                                "Programa creado correctamente",
                                "Información", JOptionPane.INFORMATION_MESSAGE);

                        cargarTablaProgramas();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog,
                                "Error al crear el programa",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Formato de número incorrecto en duración",
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

        panel.add(lblNombre);
        panel.add(txtNombre);
        panel.add(lblDescripcion);
        panel.add(scrollDescripcion);
        panel.add(lblDuracion);
        panel.add(txtDuracion);
        panel.add(lblTipo);
        panel.add(cmbTipo);
        panel.add(btnCancelar);
        panel.add(btnGuardar);

        dialog.getContentPane().add(panel);
        dialog.setVisible(true);
    }

    private void editarPrograma() {
        int filaSeleccionada = tablaProgramas.getSelectedRow();

        if (filaSeleccionada >= 0) {
            int idPrograma = (Integer) tablaProgramas.getValueAt(filaSeleccionada, 0);
            ProgramaEntrenamiento programa = buscarProgramaPorId(idPrograma);

            if (programa != null) {
                JDialog dialog = new JDialog(this, "Editar Programa de Entrenamiento", true);
                dialog.setSize(500, 400);
                dialog.setLocationRelativeTo(this);
                dialog.setResizable(false);

                JPanel panel = new JPanel();
                panel.setLayout(new GridLayout(5, 2, 10, 10));
                panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                JLabel lblNombre = new JLabel("Nombre:");
                JTextField txtNombre = new JTextField(programa.getNombre());

                JLabel lblDescripcion = new JLabel("Descripción:");
                JTextArea txtDescripcion = new JTextArea(programa.getDescripcion());
                txtDescripcion.setLineWrap(true);
                txtDescripcion.setWrapStyleWord(true);
                JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);

                JLabel lblDuracion = new JLabel("Duración (semanas):");
                JTextField txtDuracion = new JTextField(String.valueOf(programa.getDuracionSemanas()));

                JLabel lblTipo = new JLabel("Tipo:");
                JComboBox<String> cmbTipo = new JComboBox<>();
                for (TipoPrograma tipo : TipoPrograma.values()) {
                    cmbTipo.addItem(tipo.getNombre());
                }

                // Seleccionar tipo actual
                for (int i = 0; i < TipoPrograma.values().length; i++) {
                    if (TipoPrograma.values()[i] == programa.getTipo()) {
                        cmbTipo.setSelectedIndex(i);
                        break;
                    }
                }

                JButton btnGuardar = new JButton("Guardar");
                JButton btnCancelar = new JButton("Cancelar");

                btnGuardar.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Validar campos
                        if (txtNombre.getText().trim().isEmpty() ||
                                txtDescripcion.getText().trim().isEmpty() ||
                                txtDuracion.getText().trim().isEmpty()) {

                            JOptionPane.showMessageDialog(dialog,
                                    "Todos los campos son obligatorios",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        try {
                            // Parsear duración
                            int duracion = Integer.parseInt(txtDuracion.getText().trim());

                            // Obtener tipo seleccionado
                            TipoPrograma tipoSeleccionado = TipoPrograma.values()[cmbTipo.getSelectedIndex()];

                            // Actualizar programa
                            programa.setNombre(txtNombre.getText().trim());
                            programa.setDescripcion(txtDescripcion.getText().trim());
                            programa.setDuracionSemanas(duracion);
                            programa.setTipo(tipoSeleccionado);

                            JOptionPane.showMessageDialog(dialog,
                                    "Programa actualizado correctamente",
                                    "Información", JOptionPane.INFORMATION_MESSAGE);

                            cargarTablaProgramas();
                            dialog.dispose();
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(dialog,
                                    "Formato de número incorrecto en duración",
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

                panel.add(lblNombre);
                panel.add(txtNombre);
                panel.add(lblDescripcion);
                panel.add(scrollDescripcion);
                panel.add(lblDuracion);
                panel.add(txtDuracion);
                panel.add(lblTipo);
                panel.add(cmbTipo);
                panel.add(btnCancelar);
                panel.add(btnGuardar);

                dialog.getContentPane().add(panel);
                dialog.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un programa",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verRutinasPrograma() {
        int filaSeleccionada = tablaProgramas.getSelectedRow();

        if (filaSeleccionada >= 0) {
            int idPrograma = (Integer) tablaProgramas.getValueAt(filaSeleccionada, 0);
            ProgramaEntrenamiento programa = buscarProgramaPorId(idPrograma);

            if (programa != null) {
                JDialog dialog = new JDialog(this, "Rutinas del Programa: " + programa.getNombre(), true);
                dialog.setSize(600, 400);
                dialog.setLocationRelativeTo(this);

                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());

                // Panel de botones
                JPanel panelBotones = new JPanel();
                panelBotones.setLayout(new FlowLayout(FlowLayout.LEFT));

                JButton btnAgregarRutina = new JButton("Agregar Rutina");
                JButton btnVerEjercicios = new JButton("Ver Ejercicios");

                btnAgregarRutina.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Implementar formulario para agregar rutina
                        agregarRutina(programa, dialog);
                    }
                });

                btnVerEjercicios.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Implementar visualización de ejercicios
                        int filaSeleccionada = tablaRutinas.getSelectedRow();

                        if (filaSeleccionada >= 0) {
                            Rutina rutina = programa.getRutinas().get(filaSeleccionada);
                            verEjerciciosRutina(rutina);
                        } else {
                            JOptionPane.showMessageDialog(dialog,
                                    "Debe seleccionar una rutina",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                panelBotones.add(btnAgregarRutina);
                panelBotones.add(btnVerEjercicios);

                // Tabla de rutinas
                String[] columnas = {"Nombre", "Descripción", "Duración", "Día", "Ejercicios"};
                modeloTablaRutinas = new DefaultTableModel(columnas, 0) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false; // No permitir edición directa
                    }
                };

                tablaRutinas = new JTable(modeloTablaRutinas);
                tablaRutinas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tablaRutinas.getTableHeader().setReorderingAllowed(false);

                JScrollPane scrollPane = new JScrollPane(tablaRutinas);

                // Cargar rutinas en la tabla
                List<Rutina> rutinas = programa.getRutinas();
                for (Rutina rutina : rutinas) {
                    Object[] fila = {
                            rutina.getNombre(),
                            rutina.getDescripcion(),
                            rutina.getDuracionMinutos() + " min",
                            rutina.getDia().getNombre(),
                            rutina.getEjercicios().size()
                    };

                    modeloTablaRutinas.addRow(fila);
                }

                panel.add(panelBotones, BorderLayout.NORTH);
                panel.add(scrollPane, BorderLayout.CENTER);

                dialog.getContentPane().add(panel);
                dialog.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un programa",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarRutina(ProgramaEntrenamiento programa, JDialog parentDialog) {
        JDialog dialog = new JDialog(parentDialog, "Agregar Rutina", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(parentDialog);
        dialog.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField();

        JLabel lblDescripcion = new JLabel("Descripción:");
        JTextField txtDescripcion = new JTextField();

        JLabel lblDuracion = new JLabel("Duración (minutos):");
        JTextField txtDuracion = new JTextField();

        JLabel lblDia = new JLabel("Día:");
        JComboBox<String> cmbDia = new JComboBox<>();
        for (DiaSemana dia : DiaSemana.values()) {
            cmbDia.addItem(dia.getNombre());
        }

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validar campos
                if (txtNombre.getText().trim().isEmpty() ||
                        txtDescripcion.getText().trim().isEmpty() ||
                        txtDuracion.getText().trim().isEmpty()) {

                    JOptionPane.showMessageDialog(dialog,
                            "Todos los campos son obligatorios",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // Parsear duración
                    int duracion = Integer.parseInt(txtDuracion.getText().trim());

                    // Obtener día seleccionado
                    DiaSemana diaSeleccionado = DiaSemana.values()[cmbDia.getSelectedIndex()];

                    // Crear rutina
                    Rutina rutina = controller.crearRutina(
                            txtNombre.getText().trim(),
                            txtDescripcion.getText().trim(),
                            duracion,
                            diaSeleccionado,
                            programa
                    );

                    if (rutina != null) {
                        JOptionPane.showMessageDialog(dialog,
                                "Rutina agregada correctamente",
                                "Información", JOptionPane.INFORMATION_MESSAGE);

                        // Actualizar ventana padre
                        parentDialog.dispose();
                        verRutinasPrograma();

                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog,
                                "Error al agregar la rutina",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Formato de número incorrecto en duración",
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

        panel.add(lblNombre);
        panel.add(txtNombre);
        panel.add(lblDescripcion);
        panel.add(txtDescripcion);
        panel.add(lblDuracion);
        panel.add(txtDuracion);
        panel.add(lblDia);
        panel.add(cmbDia);
        panel.add(btnCancelar);
        panel.add(btnGuardar);

        dialog.getContentPane().add(panel);
        dialog.setVisible(true);
    }

    private void verEjerciciosRutina(Rutina rutina) {
        JDialog dialog = new JDialog(this, "Ejercicios de la Rutina: " + rutina.getNombre(), true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton btnAgregarEjercicio = new JButton("Agregar Ejercicio");

        btnAgregarEjercicio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implementar formulario para agregar ejercicio
                agregarEjercicio(rutina, dialog);
            }
        });

        panelBotones.add(btnAgregarEjercicio);

        // Tabla de ejercicios
        String[] columnas = {"Nombre", "Grupo Muscular", "Equipo", "Series", "Repeticiones", "Peso"};
        DefaultTableModel modeloTablaEjercicios = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No permitir edición directa
            }
        };

        JTable tablaEjercicios = new JTable(modeloTablaEjercicios);
        tablaEjercicios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaEjercicios.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tablaEjercicios);

        // Cargar ejercicios en la tabla
        List<Ejercicio> ejercicios = rutina.getEjercicios();
        for (Ejercicio ejercicio : ejercicios) {
            Object[] fila = {
                    ejercicio.getNombre(),
                    ejercicio.getGrupoMuscular(),
                    ejercicio.getEquipoNecesario(),
                    ejercicio.getSeries(),
                    ejercicio.getRepeticiones(),
                    ejercicio.getPeso() + " kg"
            };

            modeloTablaEjercicios.addRow(fila);
        }

        panel.add(panelBotones, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        dialog.getContentPane().add(panel);
        dialog.setVisible(true);
    }

    private void agregarEjercicio(Rutina rutina, JDialog parentDialog) {
        JDialog dialog = new JDialog(parentDialog, "Agregar Ejercicio", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(parentDialog);
        dialog.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField();

        JLabel lblDescripcion = new JLabel("Descripción:");
        JTextField txtDescripcion = new JTextField();

        JLabel lblGrupoMuscular = new JLabel("Grupo Muscular:");
        JTextField txtGrupoMuscular = new JTextField();

        JLabel lblEquipo = new JLabel("Equipo Necesario:");
        JTextField txtEquipo = new JTextField();

        JLabel lblSeries = new JLabel("Series:");
        JTextField txtSeries = new JTextField();

        JLabel lblRepeticiones = new JLabel("Repeticiones:");
        JTextField txtRepeticiones = new JTextField();

        JLabel lblPeso = new JLabel("Peso (kg):");
        JTextField txtPeso = new JTextField();

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validar campos
                if (txtNombre.getText().trim().isEmpty() ||
                        txtDescripcion.getText().trim().isEmpty() ||
                        txtGrupoMuscular.getText().trim().isEmpty() ||
                        txtSeries.getText().trim().isEmpty() ||
                        txtRepeticiones.getText().trim().isEmpty() ||
                        txtPeso.getText().trim().isEmpty()) {

                    JOptionPane.showMessageDialog(dialog,
                            "Todos los campos son obligatorios",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // Parsear valores numéricos
                    int series = Integer.parseInt(txtSeries.getText().trim());
                    int repeticiones = Integer.parseInt(txtRepeticiones.getText().trim());
                    float peso = Float.parseFloat(txtPeso.getText().trim());

                    // Crear ejercicio
                    Ejercicio ejercicio = controller.crearEjercicio(
                            txtNombre.getText().trim(),
                            txtDescripcion.getText().trim(),
                            txtGrupoMuscular.getText().trim(),
                            txtEquipo.getText().trim(),
                            series,
                            repeticiones,
                            peso,
                            rutina
                    );

                    if (ejercicio != null) {
                        JOptionPane.showMessageDialog(dialog,
                                "Ejercicio agregado correctamente",
                                "Información", JOptionPane.INFORMATION_MESSAGE);

                        // Actualizar ventana padre
                        parentDialog.dispose();
                        verEjerciciosRutina(rutina);

                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog,
                                "Error al agregar el ejercicio",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Formato de número incorrecto en series, repeticiones o peso",
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

        panel.add(lblNombre);
        panel.add(txtNombre);
        panel.add(lblDescripcion);
        panel.add(txtDescripcion);
        panel.add(lblGrupoMuscular);
        panel.add(txtGrupoMuscular);
        panel.add(lblEquipo);
        panel.add(txtEquipo);
        panel.add(lblSeries);
        panel.add(txtSeries);
        panel.add(lblRepeticiones);
        panel.add(txtRepeticiones);
        panel.add(lblPeso);
        panel.add(txtPeso);
        panel.add(btnCancelar);
        panel.add(btnGuardar);

        dialog.getContentPane().add(panel);
        dialog.setVisible(true);
    }

    private void agregarHorario() {
        JDialog dialog = new JDialog(this, "Agregar Horario Disponible", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblDia = new JLabel("Día:");
        JComboBox<String> cmbDia = new JComboBox<>();
        for (DiaSemana dia : DiaSemana.values()) {
            cmbDia.addItem(dia.getNombre());
        }

        JLabel lblHoraInicio = new JLabel("Hora Inicio (HH:MM):");
        JTextField txtHoraInicio = new JTextField();

        JLabel lblHoraFin = new JLabel("Hora Fin (HH:MM):");
        JTextField txtHoraFin = new JTextField();

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validar campos
                if (txtHoraInicio.getText().trim().isEmpty() ||
                        txtHoraFin.getText().trim().isEmpty()) {

                    JOptionPane.showMessageDialog(dialog,
                            "Todos los campos son obligatorios",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // Parsear hora inicio
                    String[] horaInicioPartes = txtHoraInicio.getText().trim().split(":");
                    int horaInicio = Integer.parseInt(horaInicioPartes[0]);
                    int minutoInicio = Integer.parseInt(horaInicioPartes[1]);

                    if (horaInicio < 0 || horaInicio > 23 || minutoInicio < 0 || minutoInicio > 59) {
                        throw new NumberFormatException("Formato de hora incorrecto");
                    }

                    // Parsear hora fin
                    String[] horaFinPartes = txtHoraFin.getText().trim().split(":");
                    int horaFin = Integer.parseInt(horaFinPartes[0]);
                    int minutoFin = Integer.parseInt(horaFinPartes[1]);

                    if (horaFin < 0 || horaFin > 23 || minutoFin < 0 || minutoFin > 59) {
                        throw new NumberFormatException("Formato de hora incorrecto");
                    }

                    // Obtener día seleccionado
                    DiaSemana diaSeleccionado = DiaSemana.values()[cmbDia.getSelectedIndex()];

                    // Crear horario disponible
                    HorarioDisponible horario = new HorarioDisponible(
                            diaSeleccionado,
                            new java.sql.Time(horaInicio, minutoInicio, 0),
                            new java.sql.Time(horaFin, minutoFin, 0)
                    );

                    // Agregar horario al entrenador
                    entrenadorActual.agregarHorarioDisponible(horario);

                    JOptionPane.showMessageDialog(dialog,
                            "Horario agregado correctamente",
                            "Información", JOptionPane.INFORMATION_MESSAGE);

                    cargarTablaHorarios();
                    dialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Formato de hora incorrecto. Use HH:MM",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Error al agregar horario: " + ex.getMessage(),
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

        panel.add(lblDia);
        panel.add(cmbDia);
        panel.add(lblHoraInicio);
        panel.add(txtHoraInicio);
        panel.add(lblHoraFin);
        panel.add(txtHoraFin);
        panel.add(btnCancelar);
        panel.add(btnGuardar);

        dialog.getContentPane().add(panel);
        dialog.setVisible(true);
    }

    private void eliminarHorario() {
        int filaSeleccionada = tablaHorarios.getSelectedRow();

        if (filaSeleccionada >= 0) {
            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar este horario?",
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                // Eliminar horario
                List<HorarioDisponible> horarios = entrenadorActual.getHorariosDisponibles();
                horarios.remove(filaSeleccionada);

                cargarTablaHorarios();

                JOptionPane.showMessageDialog(this,
                        "Horario eliminado correctamente",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un horario",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarProgresoMiembro() {
        int indice = cmbMiembroSeguimiento.getSelectedIndex();

        if (indice >= 0) {
            // Obtener miembro seleccionado
            List<Miembro> miembros = entrenadorActual.getMiembrosAsignados();
            Miembro miembro = miembros.get(indice);

            JDialog dialog = new JDialog(this, "Registrar Progreso", true);
            dialog.setSize(400, 400);
            dialog.setLocationRelativeTo(this);
            dialog.setResizable(false);

            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());

            // Panel de formulario
            JPanel panelFormulario = new JPanel();
            panelFormulario.setLayout(new GridLayout(7, 2, 10, 10));
            panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel lblMiembro = new JLabel("Miembro:");
            JLabel lblMiembroValor = new JLabel(miembro.getNombre() + " " + miembro.getApellido());

            JLabel lblPeso = new JLabel("Peso (kg):");
            JTextField txtPeso = new JTextField(String.valueOf(miembro.getPeso()));

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

            panelFormulario.add(lblMiembro);
            panelFormulario.add(lblMiembroValor);
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
                                miembro,
                                peso,
                                medidas,
                                txtComentarios.getText().trim()
                        );

                        if (registro != null) {
                            JOptionPane.showMessageDialog(dialog,
                                    "Progreso registrado correctamente",
                                    "Información", JOptionPane.INFORMATION_MESSAGE);

                            cargarSeguimientoMiembro();
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
        } else {
            JOptionPane.showMessageDialog(this,
                    "No hay miembros para registrar progreso",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Miembro buscarMiembroPorId(int id) {
        for (Miembro miembro : entrenadorActual.getMiembrosAsignados()) {
            if (miembro.getId() == id) {
                return miembro;
            }
        }
        return null;
    }

    private ProgramaEntrenamiento buscarProgramaPorId(int id) {
        for (ProgramaEntrenamiento programa : entrenadorActual.getProgramasCreados()) {
            if (programa.getId() == id) {
                return programa;
            }
        }
        return null;
    }

    private void cerrarSesion() {
        controller.cerrarSesion();

        LoginView loginView = new LoginView(controller);
        loginView.setVisible(true);

        this.dispose();
    }
}