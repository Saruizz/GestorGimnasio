// AdminView.java
package main.java.view;

import main.java.controller.SistemaController;
import main.java.model.*;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Vista principal para el Administrador
 */
public class AdminView extends JFrame {

    private SistemaController controller;

    private JTabbedPane tabbedPane;
    private JPanel panelMiembros;
    private JPanel panelEntrenadores;
    private JPanel panelProgramas;
    private JPanel panelMembresias;
    private JPanel panelReportes;
    private JPanel panelConfiguracion;

    // Componentes para panel de miembros
    private JTable tablaMiembros;
    private DefaultTableModel modeloTablaMiembros;
    private JButton btnAgregarMiembro;
    private JButton btnEditarMiembro;
    private JButton btnEliminarMiembro;

    // Componentes para panel de entrenadores
    private JTable tablaEntrenadores;
    private DefaultTableModel modeloTablaEntrenadores;
    private JButton btnAgregarEntrenador;
    private JButton btnEditarEntrenador;
    private JButton btnEliminarEntrenador;

    // Componentes para panel de programas
    private JTable tablaProgramas;
    private DefaultTableModel modeloTablaProgramas;
    private JButton btnAgregarPrograma;
    private JButton btnEditarPrograma;
    private JButton btnEliminarPrograma;
    private JButton btnVerRutinas;

    // Componentes para panel de membresías
    private JTable tablaMembresias;
    private DefaultTableModel modeloTablaMembresias;
    private JButton btnNuevaMembresia;
    private JButton btnRenovarMembresia;
    private JButton btnCancelarMembresia;

    // Componentes para panel de reportes
    private JComboBox<String> cmbTipoReporte;
    private JTextArea txtAreaReporte;
    private JButton btnGenerarReporte;
    private JButton btnExportarReporte;

    public AdminView(SistemaController controller) {
        this.controller = controller;
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        // Configurar ventana
        setTitle("Sistema de Gestión de Gimnasio - Administrador");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Agregar icono a la ventana
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/resources/gym-icon.png"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("No se pudo cargar el icono: " + e.getMessage());
        }

        // Panel principal con pestañas
        tabbedPane = new JTabbedPane();

        // Inicializar paneles
        panelMiembros = new JPanel();
        panelEntrenadores = new JPanel();
        panelProgramas = new JPanel();
        panelMembresias = new JPanel();
        panelReportes = new JPanel();
        panelConfiguracion = new JPanel();

        // Configurar paneles
        configurarPanelMiembros();
        configurarPanelEntrenadores();
        configurarPanelProgramas();
        configurarPanelMembresias();
        configurarPanelReportes();
        configurarPanelConfiguracion();

        // Agregar paneles a las pestañas
        tabbedPane.addTab("Miembros", new ImageIcon(), panelMiembros, "Gestión de miembros");
        tabbedPane.addTab("Entrenadores", new ImageIcon(), panelEntrenadores, "Gestión de entrenadores");
        tabbedPane.addTab("Programas", new ImageIcon(), panelProgramas, "Gestión de programas de entrenamiento");
        tabbedPane.addTab("Membresías", new ImageIcon(), panelMembresias, "Gestión de membresías");
        tabbedPane.addTab("Reportes", new ImageIcon(), panelReportes, "Generación de reportes");
        tabbedPane.addTab("Configuración", new ImageIcon(), panelConfiguracion, "Configuración del sistema");

        // Agregar listener para cambio de pestaña
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                switch (selectedIndex) {
                    case 0: // Miembros
                        cargarTablaMiembros();
                        break;
                    case 1: // Entrenadores
                        cargarTablaEntrenadores();
                        break;
                    case 2: // Programas
                        cargarTablaProgramas();
                        break;
                    case 3: // Membresías
                        cargarTablaMembresias();
                        break;
                }
            }
        });

        // Crear barra de menú
        JMenuBar menuBar = new JMenuBar();

        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemSalir = new JMenuItem("Salir");
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
                JOptionPane.showMessageDialog(AdminView.this,
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

    private void configurarPanelMiembros() {
        panelMiembros.setLayout(new BorderLayout());

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.LEFT));

        btnAgregarMiembro = new JButton("Agregar");
        btnEditarMiembro = new JButton("Editar");
        btnEliminarMiembro = new JButton("Eliminar");

        btnAgregarMiembro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarFormularioMiembro(null);
            }
        });

        btnEditarMiembro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = tablaMiembros.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    int idMiembro = (Integer) tablaMiembros.getValueAt(filaSeleccionada, 0);
                    Miembro miembro = buscarMiembroPorId(idMiembro);
                    if (miembro != null) {
                        mostrarFormularioMiembro(miembro);
                    }
                } else {
                    JOptionPane.showMessageDialog(AdminView.this,
                            "Debe seleccionar un miembro",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnEliminarMiembro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = tablaMiembros.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    int idMiembro = (Integer) tablaMiembros.getValueAt(filaSeleccionada, 0);
                    Miembro miembro = buscarMiembroPorId(idMiembro);
                    if (miembro != null) {
                        int confirmacion = JOptionPane.showConfirmDialog(AdminView.this,
                                "¿Está seguro de eliminar al miembro " + miembro.getNombre() + " " + miembro.getApellido() + "?",
                                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

                        if (confirmacion == JOptionPane.YES_OPTION) {
                            if (controller.eliminarUsuario(miembro)) {
                                cargarTablaMiembros();
                                JOptionPane.showMessageDialog(AdminView.this,
                                        "Miembro eliminado correctamente",
                                        "Información", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(AdminView.this,
                                        "Error al eliminar el miembro",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(AdminView.this,
                            "Debe seleccionar un miembro",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panelBotones.add(btnAgregarMiembro);
        panelBotones.add(btnEditarMiembro);
        panelBotones.add(btnEliminarMiembro);

        // Tabla de miembros
        String[] columnas = {"ID", "Nombre", "Apellido", "Correo", "Teléfono", "Edad", "Membresía", "Entrenador"};
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
                    int filaSeleccionada = tablaMiembros.getSelectedRow();
                    if (filaSeleccionada >= 0) {
                        int idMiembro = (Integer) tablaMiembros.getValueAt(filaSeleccionada, 0);
                        Miembro miembro = buscarMiembroPorId(idMiembro);
                        if (miembro != null) {
                            mostrarFormularioMiembro(miembro);
                        }
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaMiembros);

        // Agregar componentes al panel
        panelMiembros.add(panelBotones, BorderLayout.NORTH);
        panelMiembros.add(scrollPane, BorderLayout.CENTER);
    }

    private void configurarPanelEntrenadores() {
        panelEntrenadores.setLayout(new BorderLayout());

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.LEFT));

        btnAgregarEntrenador = new JButton("Agregar");
        btnEditarEntrenador = new JButton("Editar");
        btnEliminarEntrenador = new JButton("Eliminar");

        btnAgregarEntrenador.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarFormularioEntrenador(null);
            }
        });

        btnEditarEntrenador.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = tablaEntrenadores.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    int idEntrenador = (Integer) tablaEntrenadores.getValueAt(filaSeleccionada, 0);
                    Entrenador entrenador = buscarEntrenadorPorId(idEntrenador);
                    if (entrenador != null) {
                        mostrarFormularioEntrenador(entrenador);
                    }
                } else {
                    JOptionPane.showMessageDialog(AdminView.this,
                            "Debe seleccionar un entrenador",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnEliminarEntrenador.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = tablaEntrenadores.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    int idEntrenador = (Integer) tablaEntrenadores.getValueAt(filaSeleccionada, 0);
                    Entrenador entrenador = buscarEntrenadorPorId(idEntrenador);
                    if (entrenador != null) {
                        int confirmacion = JOptionPane.showConfirmDialog(AdminView.this,
                                "¿Está seguro de eliminar al entrenador " + entrenador.getNombre() + " " + entrenador.getApellido() + "?",
                                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

                        if (confirmacion == JOptionPane.YES_OPTION) {
                            if (controller.eliminarUsuario(entrenador)) {
                                cargarTablaEntrenadores();
                                JOptionPane.showMessageDialog(AdminView.this,
                                        "Entrenador eliminado correctamente",
                                        "Información", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(AdminView.this,
                                        "Error al eliminar el entrenador",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(AdminView.this,
                            "Debe seleccionar un entrenador",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panelBotones.add(btnAgregarEntrenador);
        panelBotones.add(btnEditarEntrenador);
        panelBotones.add(btnEliminarEntrenador);

        // Tabla de entrenadores
        String[] columnas = {"ID", "Nombre", "Apellido", "Correo", "Teléfono", "Especialidades", "Miembros"};
        modeloTablaEntrenadores = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No permitir edición directa
            }
        };

        tablaEntrenadores = new JTable(modeloTablaEntrenadores);
        tablaEntrenadores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaEntrenadores.getTableHeader().setReorderingAllowed(false);

        // Agregar doble clic en la tabla
        tablaEntrenadores.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int filaSeleccionada = tablaEntrenadores.getSelectedRow();
                    if (filaSeleccionada >= 0) {
                        int idEntrenador = (Integer) tablaEntrenadores.getValueAt(filaSeleccionada, 0);
                        Entrenador entrenador = buscarEntrenadorPorId(idEntrenador);
                        if (entrenador != null) {
                            mostrarFormularioEntrenador(entrenador);
                        }
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaEntrenadores);

        // Agregar componentes al panel
        panelEntrenadores.add(panelBotones, BorderLayout.NORTH);
        panelEntrenadores.add(scrollPane, BorderLayout.CENTER);
    }

    private void configurarPanelProgramas() {
        panelProgramas.setLayout(new BorderLayout());

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.LEFT));

        btnAgregarPrograma = new JButton("Agregar");
        btnEditarPrograma = new JButton("Editar");
        btnEliminarPrograma = new JButton("Eliminar");
        btnVerRutinas = new JButton("Ver Rutinas");

        btnAgregarPrograma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarFormularioPrograma(null);
            }
        });

        btnEditarPrograma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = tablaProgramas.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    int idPrograma = (Integer) tablaProgramas.getValueAt(filaSeleccionada, 0);
                    ProgramaEntrenamiento programa = buscarProgramaPorId(idPrograma);
                    if (programa != null) {
                        mostrarFormularioPrograma(programa);
                    }
                } else {
                    JOptionPane.showMessageDialog(AdminView.this,
                            "Debe seleccionar un programa",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnEliminarPrograma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = tablaProgramas.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    int idPrograma = (Integer) tablaProgramas.getValueAt(filaSeleccionada, 0);
                    ProgramaEntrenamiento programa = buscarProgramaPorId(idPrograma);
                    if (programa != null) {
                        int confirmacion = JOptionPane.showConfirmDialog(AdminView.this,
                                "¿Está seguro de eliminar el programa " + programa.getNombre() + "?",
                                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

                        if (confirmacion == JOptionPane.YES_OPTION) {
                            if (controller.eliminarProgramaEntrenamiento(programa)) {
                                cargarTablaProgramas();
                                JOptionPane.showMessageDialog(AdminView.this,
                                        "Programa eliminado correctamente",
                                        "Información", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(AdminView.this,
                                        "Error al eliminar el programa",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(AdminView.this,
                            "Debe seleccionar un programa",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnVerRutinas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = tablaProgramas.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    int idPrograma = (Integer) tablaProgramas.getValueAt(filaSeleccionada, 0);
                    ProgramaEntrenamiento programa = buscarProgramaPorId(idPrograma);
                    if (programa != null) {
                        mostrarRutinasPrograma(programa);
                    }
                } else {
                    JOptionPane.showMessageDialog(AdminView.this,
                            "Debe seleccionar un programa",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panelBotones.add(btnAgregarPrograma);
        panelBotones.add(btnEditarPrograma);
        panelBotones.add(btnEliminarPrograma);
        panelBotones.add(btnVerRutinas);

        // Tabla de programas
        String[] columnas = {"ID", "Nombre", "Descripción", "Duración", "Tipo", "Entrenador", "Rutinas"};
        modeloTablaProgramas = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No permitir edición directa
            }
        };

        tablaProgramas = new JTable(modeloTablaProgramas);
        tablaProgramas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaProgramas.getTableHeader().setReorderingAllowed(false);

        // Agregar doble clic en la tabla
        tablaProgramas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int filaSeleccionada = tablaProgramas.getSelectedRow();
                    if (filaSeleccionada >= 0) {
                        int idPrograma = (Integer) tablaProgramas.getValueAt(filaSeleccionada, 0);
                        ProgramaEntrenamiento programa = buscarProgramaPorId(idPrograma);
                        if (programa != null) {
                            mostrarFormularioPrograma(programa);
                        }
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaProgramas);

        // Agregar componentes al panel
        panelProgramas.add(panelBotones, BorderLayout.NORTH);
        panelProgramas.add(scrollPane, BorderLayout.CENTER);
    }

    private void configurarPanelMembresias() {
        panelMembresias.setLayout(new BorderLayout());

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.LEFT));

        btnNuevaMembresia = new JButton("Nueva");
        btnRenovarMembresia = new JButton("Renovar");
        btnCancelarMembresia = new JButton("Cancelar");

        btnNuevaMembresia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarFormularioMembresia(null);
            }
        });

        btnRenovarMembresia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = tablaMembresias.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    int idMiembro = (Integer) tablaMembresias.getValueAt(filaSeleccionada, 0);
                    Miembro miembro = buscarMiembroPorId(idMiembro);
                    if (miembro != null && miembro.getMembresia() != null) {
                        mostrarFormularioRenovarMembresia(miembro);
                    } else {
                        JOptionPane.showMessageDialog(AdminView.this,
                                "El miembro seleccionado no tiene membresía",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(AdminView.this,
                            "Debe seleccionar un miembro",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCancelarMembresia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = tablaMembresias.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    int idMiembro = (Integer) tablaMembresias.getValueAt(filaSeleccionada, 0);
                    Miembro miembro = buscarMiembroPorId(idMiembro);
                    if (miembro != null && miembro.getMembresia() != null) {
                        int confirmacion = JOptionPane.showConfirmDialog(AdminView.this,
                                "¿Está seguro de cancelar la membresía de " + miembro.getNombre() + " " + miembro.getApellido() + "?",
                                "Confirmar cancelación", JOptionPane.YES_NO_OPTION);

                        if (confirmacion == JOptionPane.YES_OPTION) {
                            if (controller.cancelarMembresia(miembro)) {
                                cargarTablaMembresias();
                                JOptionPane.showMessageDialog(AdminView.this,
                                        "Membresía cancelada correctamente",
                                        "Información", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(AdminView.this,
                                        "Error al cancelar la membresía",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(AdminView.this,
                                "El miembro seleccionado no tiene membresía",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(AdminView.this,
                            "Debe seleccionar un miembro",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panelBotones.add(btnNuevaMembresia);
        panelBotones.add(btnRenovarMembresia);
        panelBotones.add(btnCancelarMembresia);

        // Tabla de membresías
        String[] columnas = {"ID Miembro", "Nombre", "Apellido", "Tipo", "Inicio", "Fin", "Costo", "Estado"};
        modeloTablaMembresias = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No permitir edición directa
            }
        };

        tablaMembresias = new JTable(modeloTablaMembresias);
        tablaMembresias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaMembresias.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tablaMembresias);

        // Agregar componentes al panel
        panelMembresias.add(panelBotones, BorderLayout.NORTH);
        panelMembresias.add(scrollPane, BorderLayout.CENTER);
    }

    private void configurarPanelReportes() {
        panelReportes.setLayout(new BorderLayout());

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel lblTipoReporte = new JLabel("Tipo de reporte:");
        cmbTipoReporte = new JComboBox<>();
        cmbTipoReporte.addItem("Asistencia");
        cmbTipoReporte.addItem("Membresías");
        cmbTipoReporte.addItem("Entrenadores");
        cmbTipoReporte.addItem("Programas");

        btnGenerarReporte = new JButton("Generar");
        btnExportarReporte = new JButton("Exportar");

        btnGenerarReporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarReporte();
            }
        });

        btnExportarReporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportarReporte();
            }
        });

        panelSuperior.add(lblTipoReporte);
        panelSuperior.add(cmbTipoReporte);
        panelSuperior.add(btnGenerarReporte);
        panelSuperior.add(btnExportarReporte);

        // Panel central
        txtAreaReporte = new JTextArea();
        txtAreaReporte.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(txtAreaReporte);

        // Agregar componentes al panel
        panelReportes.add(panelSuperior, BorderLayout.NORTH);
        panelReportes.add(scrollPane, BorderLayout.CENTER);
    }

    private void configurarPanelConfiguracion() {
        panelConfiguracion.setLayout(new BorderLayout());

        // Panel de formulario
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new GridLayout(5, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblNombreGimnasio = new JLabel("Nombre del gimnasio:");
        JTextField txtNombreGimnasio = new JTextField();

        JLabel lblDireccion = new JLabel("Dirección:");
        JTextField txtDireccion = new JTextField();

        JLabel lblTelefono = new JLabel("Teléfono:");
        JTextField txtTelefono = new JTextField();

        JLabel lblHorario = new JLabel("Horario de atención:");
        JTextField txtHorario = new JTextField();

        JButton btnGuardar = new JButton("Guardar configuración");

        panelFormulario.add(lblNombreGimnasio);
        panelFormulario.add(txtNombreGimnasio);
        panelFormulario.add(lblDireccion);
        panelFormulario.add(txtDireccion);
        panelFormulario.add(lblTelefono);
        panelFormulario.add(txtTelefono);
        panelFormulario.add(lblHorario);
        panelFormulario.add(txtHorario);
        panelFormulario.add(new JLabel(""));
        panelFormulario.add(btnGuardar);

        // Cargar datos actuales
        // Estos valores podrían cargarse desde el controlador en un sistema real
        txtNombreGimnasio.setText("GYM Fitness");
        txtDireccion.setText("Calle Principal #123");
        txtTelefono.setText("555-1234");
        txtHorario.setText("Lun-Vie: 6:00-22:00, Sáb-Dom: 8:00-20:00");

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(AdminView.this,
                        "Configuración guardada correctamente",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Agregar componentes al panel
        panelConfiguracion.add(panelFormulario, BorderLayout.NORTH);
    }

    private void cargarDatos() {
        cargarTablaMiembros();
        cargarTablaEntrenadores();
        cargarTablaProgramas();
        cargarTablaMembresias();
    }

    private void cargarTablaMiembros() {
        // Limpiar tabla
        modeloTablaMiembros.setRowCount(0);

        // Obtener lista de miembros
        List<Miembro> miembros = controller.obtenerMiembros();

        // Agregar datos a la tabla
        for (Miembro miembro : miembros) {
            String nombreEntrenador = (miembro.getEntrenadorAsignado() != null) ?
                    miembro.getEntrenadorAsignado().getNombre() + " " + miembro.getEntrenadorAsignado().getApellido() :
                    "No asignado";

            String tipoMembresia = (miembro.getMembresia() != null) ?
                    miembro.getMembresia().getTipo() :
                    "No asignada";

            Object[] fila = {
                    miembro.getId(),
                    miembro.getNombre(),
                    miembro.getApellido(),
                    miembro.getCorreo(),
                    miembro.getTelefono(),
                    miembro.getEdad(),
                    tipoMembresia,
                    nombreEntrenador
            };

            modeloTablaMiembros.addRow(fila);
        }
    }

    private void cargarTablaEntrenadores() {
        // Limpiar tabla
        modeloTablaEntrenadores.setRowCount(0);

        // Obtener lista de entrenadores
        List<Entrenador> entrenadores = controller.obtenerEntrenadores();

        // Agregar datos a la tabla
        for (Entrenador entrenador : entrenadores) {
            StringBuilder especialidades = new StringBuilder();
            for (String especialidad : entrenador.getEspecialidades()) {
                if (especialidades.length() > 0) {
                    especialidades.append(", ");
                }
                especialidades.append(especialidad);
            }

            Object[] fila = {
                    entrenador.getId(),
                    entrenador.getNombre(),
                    entrenador.getApellido(),
                    entrenador.getCorreo(),
                    entrenador.getTelefono(),
                    especialidades.toString(),
                    entrenador.getMiembrosAsignados().size()
            };

            modeloTablaEntrenadores.addRow(fila);
        }
    }

    private void cargarTablaProgramas() {
        // Limpiar tabla
        modeloTablaProgramas.setRowCount(0);

        // Obtener lista de programas
        List<ProgramaEntrenamiento> programas = controller.obtenerProgramasEntrenamiento();

        // Agregar datos a la tabla
        for (ProgramaEntrenamiento programa : programas) {
            String nombreEntrenador = (programa.getEntrenadorAsignado() != null) ?
                    programa.getEntrenadorAsignado().getNombre() + " " + programa.getEntrenadorAsignado().getApellido() :
                    "No asignado";

            Object[] fila = {
                    programa.getId(),
                    programa.getNombre(),
                    programa.getDescripcion(),
                    programa.getDuracionSemanas() + " semanas",
                    programa.getTipo().getNombre(),
                    nombreEntrenador,
                    programa.getRutinas().size()
            };

            modeloTablaProgramas.addRow(fila);
        }
    }

    private void cargarTablaMembresias() {
        // Limpiar tabla
        modeloTablaMembresias.setRowCount(0);

        // Obtener lista de miembros
        List<Miembro> miembros = controller.obtenerMiembros();

        // Formato de fecha
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Agregar datos a la tabla
        for (Miembro miembro : miembros) {
            if (miembro.getMembresia() != null) {
                Membresia membresia = miembro.getMembresia();

                String estado = membresia.isActiva() ?
                        (membresia.esVigente() ? "Activa" : "Vencida") :
                        "Cancelada";

                Object[] fila = {
                        miembro.getId(),
                        miembro.getNombre(),
                        miembro.getApellido(),
                        membresia.getTipo(),
                        sdf.format(membresia.getFechaInicio()),
                        sdf.format(membresia.getFechaFin()),
                        membresia.getCosto(),
                        estado
                };

                modeloTablaMembresias.addRow(fila);
            }
        }
    }

    private Miembro buscarMiembroPorId(int id) {
        for (Miembro miembro : controller.obtenerMiembros()) {
            if (miembro.getId() == id) {
                return miembro;
            }
        }
        return null;
    }

    private Entrenador buscarEntrenadorPorId(int id) {
        for (Entrenador entrenador : controller.obtenerEntrenadores()) {
            if (entrenador.getId() == id) {
                return entrenador;
            }
        }
        return null;
    }

    private ProgramaEntrenamiento buscarProgramaPorId(int id) {
        for (ProgramaEntrenamiento programa : controller.obtenerProgramasEntrenamiento()) {
            if (programa.getId() == id) {
                return programa;
            }
        }
        return null;
    }

    private void mostrarFormularioMiembro(Miembro miembro) {
        JDialog dialog = new JDialog(this, miembro == null ? "Agregar Miembro" : "Editar Miembro", true);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(12, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField();

        JLabel lblApellido = new JLabel("Apellido:");
        JTextField txtApellido = new JTextField();

        JLabel lblCorreo = new JLabel("Correo:");
        JTextField txtCorreo = new JTextField();

        JLabel lblTelefono = new JLabel("Teléfono:");
        JTextField txtTelefono = new JTextField();

        JLabel lblPassword = new JLabel("Contraseña:");
        JPasswordField txtPassword = new JPasswordField();

        JLabel lblFechaNacimiento = new JLabel("Fecha de nacimiento (dd/mm/aaaa):");
        JTextField txtFechaNacimiento = new JTextField();

        JLabel lblPeso = new JLabel("Peso (kg):");
        JTextField txtPeso = new JTextField();

        JLabel lblAltura = new JLabel("Altura (cm):");
        JTextField txtAltura = new JTextField();

        JLabel lblObjetivos = new JLabel("Objetivos:");
        JTextArea txtObjetivos = new JTextArea();
        txtObjetivos.setLineWrap(true);
        txtObjetivos.setWrapStyleWord(true);
        JScrollPane scrollObjetivos = new JScrollPane(txtObjetivos);

        JLabel lblEntrenador = new JLabel("Entrenador:");
        JComboBox<String> cmbEntrenador = new JComboBox<>();
        // Agregar opción para no asignar entrenador
        cmbEntrenador.addItem("Sin asignar");

        // Cargar entrenadores
        List<Entrenador> entrenadores = controller.obtenerEntrenadores();
        for (Entrenador entrenador : entrenadores) {
            cmbEntrenador.addItem(entrenador.getNombre() + " " + entrenador.getApellido());
        }

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        // Si estamos editando, cargar datos del miembro
        if (miembro != null) {
            txtNombre.setText(miembro.getNombre());
            txtApellido.setText(miembro.getApellido());
            txtCorreo.setText(miembro.getCorreo());
            txtTelefono.setText(miembro.getTelefono());
            txtPassword.setText(miembro.getContraseña());

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            txtFechaNacimiento.setText(sdf.format(miembro.getFechaNacimiento()));

            txtPeso.setText(String.valueOf(miembro.getPeso()));
            txtAltura.setText(String.valueOf(miembro.getAltura()));
            txtObjetivos.setText(miembro.getObjetivos());

            // Seleccionar entrenador
            if (miembro.getEntrenadorAsignado() != null) {
                for (int i = 0; i < entrenadores.size(); i++) {
                    if (entrenadores.get(i).getId() == miembro.getEntrenadorAsignado().getId()) {
                        cmbEntrenador.setSelectedIndex(i + 1); // +1 por la opción "Sin asignar"
                        break;
                    }
                }
            }
        }

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validar campos
                if (txtNombre.getText().trim().isEmpty() ||
                        txtApellido.getText().trim().isEmpty() ||
                        txtCorreo.getText().trim().isEmpty() ||
                        txtTelefono.getText().trim().isEmpty() ||
                        txtFechaNacimiento.getText().trim().isEmpty() ||
                        txtPeso.getText().trim().isEmpty() ||
                        txtAltura.getText().trim().isEmpty()) {

                    JOptionPane.showMessageDialog(dialog,
                            "Todos los campos son obligatorios",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // Parsear fecha de nacimiento
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date fechaNacimiento = sdf.parse(txtFechaNacimiento.getText().trim());

                    // Parsear peso y altura
                    float peso = Float.parseFloat(txtPeso.getText().trim());
                    float altura = Float.parseFloat(txtAltura.getText().trim());

                    // Obtener entrenador seleccionado
                    Entrenador entrenadorSeleccionado = null;
                    int indiceEntrenador = cmbEntrenador.getSelectedIndex();
                    if (indiceEntrenador > 0) { // 0 es "Sin asignar"
                        entrenadorSeleccionado = entrenadores.get(indiceEntrenador - 1);
                    }

                    if (miembro == null) {
                        // Crear nuevo miembro
                        Miembro nuevoMiembro = controller.crearMiembro(
                                txtNombre.getText().trim(),
                                txtApellido.getText().trim(),
                                txtCorreo.getText().trim(),
                                txtTelefono.getText().trim(),
                                new String(txtPassword.getPassword()),
                                fechaNacimiento,
                                peso,
                                altura,
                                txtObjetivos.getText().trim()
                        );

                        if (nuevoMiembro != null) {
                            // Asignar entrenador si se seleccionó
                            if (entrenadorSeleccionado != null) {
                                controller.asignarEntrenadorAMiembro(entrenadorSeleccionado, nuevoMiembro);
                            }

                            JOptionPane.showMessageDialog(dialog,
                                    "Miembro agregado correctamente",
                                    "Información", JOptionPane.INFORMATION_MESSAGE);

                            cargarTablaMiembros();
                            dialog.dispose();
                        } else {
                            JOptionPane.showMessageDialog(dialog,
                                    "Error al agregar el miembro",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // Actualizar miembro existente
                        miembro.setNombre(txtNombre.getText().trim());
                        miembro.setApellido(txtApellido.getText().trim());
                        miembro.setCorreo(txtCorreo.getText().trim());
                        miembro.setTelefono(txtTelefono.getText().trim());
                        miembro.setContraseña(new String(txtPassword.getPassword()));
                        miembro.setFechaNacimiento(fechaNacimiento);
                        miembro.setPeso(peso);
                        miembro.setAltura(altura);
                        miembro.setObjetivos(txtObjetivos.getText().trim());

                        // Actualizar entrenador
                        if (entrenadorSeleccionado != null) {
                            miembro.setEntrenadorAsignado(entrenadorSeleccionado);
                        } else {
                            miembro.setEntrenadorAsignado(null);
                        }

                        JOptionPane.showMessageDialog(dialog,
                                "Miembro actualizado correctamente",
                                "Información", JOptionPane.INFORMATION_MESSAGE);

                        cargarTablaMiembros();
                        dialog.dispose();
                    }
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Formato de fecha incorrecto. Use dd/mm/aaaa",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Formato de número incorrecto en peso o altura",
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
        panel.add(lblApellido);
        panel.add(txtApellido);
        panel.add(lblCorreo);
        panel.add(txtCorreo);
        panel.add(lblTelefono);
        panel.add(txtTelefono);
        panel.add(lblPassword);
        panel.add(txtPassword);
        panel.add(lblFechaNacimiento);
        panel.add(txtFechaNacimiento);
        panel.add(lblPeso);
        panel.add(txtPeso);
        panel.add(lblAltura);
        panel.add(txtAltura);
        panel.add(lblObjetivos);
        panel.add(scrollObjetivos);
        panel.add(lblEntrenador);
        panel.add(cmbEntrenador);
        panel.add(btnCancelar);
        panel.add(btnGuardar);

        dialog.getContentPane().add(panel);
        dialog.setVisible(true);
    }

    private void mostrarFormularioEntrenador(Entrenador entrenador) {
        JDialog dialog = new JDialog(this, entrenador == null ? "Agregar Entrenador" : "Editar Entrenador", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField();

        JLabel lblApellido = new JLabel("Apellido:");
        JTextField txtApellido = new JTextField();

        JLabel lblCorreo = new JLabel("Correo:");
        JTextField txtCorreo = new JTextField();

        JLabel lblTelefono = new JLabel("Teléfono:");
        JTextField txtTelefono = new JTextField();

        JLabel lblPassword = new JLabel("Contraseña:");
        JPasswordField txtPassword = new JPasswordField();

        JLabel lblEspecialidades = new JLabel("Especialidades (separadas por coma):");
        JTextField txtEspecialidades = new JTextField();

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        // Si estamos editando, cargar datos del entrenador
        if (entrenador != null) {
            txtNombre.setText(entrenador.getNombre());
            txtApellido.setText(entrenador.getApellido());
            txtCorreo.setText(entrenador.getCorreo());
            txtTelefono.setText(entrenador.getTelefono());
            txtPassword.setText(entrenador.getContraseña());

            // Cargar especialidades
            StringBuilder especialidades = new StringBuilder();
            for (String especialidad : entrenador.getEspecialidades()) {
                if (especialidades.length() > 0) {
                    especialidades.append(", ");
                }
                especialidades.append(especialidad);
            }
            txtEspecialidades.setText(especialidades.toString());
        }

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validar campos
                if (txtNombre.getText().trim().isEmpty() ||
                        txtApellido.getText().trim().isEmpty() ||
                        txtCorreo.getText().trim().isEmpty() ||
                        txtTelefono.getText().trim().isEmpty()) {

                    JOptionPane.showMessageDialog(dialog,
                            "Los campos nombre, apellido, correo y teléfono son obligatorios",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (entrenador == null) {
                    // Crear nuevo entrenador
                    // Extraer y procesar las especialidades
                    String[] especialidadesArray = txtEspecialidades.getText().split(",");
                    List<String> especialidadesList = new ArrayList<>();
                    for (String especialidad : especialidadesArray) {
                        if (!especialidad.trim().isEmpty()) {
                            especialidadesList.add(especialidad.trim());
                        }
                    }

                    // Usar el método corregido que acepta una lista de especialidades
                    Entrenador nuevoEntrenador = controller.crearEntrenador(
                            txtNombre.getText().trim(),
                            txtApellido.getText().trim(),
                            txtCorreo.getText().trim(),
                            txtTelefono.getText().trim(),
                            new String(txtPassword.getPassword()),
                            especialidadesList
                    );

                    if (nuevoEntrenador != null) {
                        JOptionPane.showMessageDialog(dialog,
                                "Entrenador agregado correctamente",
                                "Información", JOptionPane.INFORMATION_MESSAGE);

                        cargarTablaEntrenadores();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog,
                                "Error al agregar el entrenador",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // Actualizar entrenador existente
                    entrenador.setNombre(txtNombre.getText().trim());
                    entrenador.setApellido(txtApellido.getText().trim());
                    entrenador.setCorreo(txtCorreo.getText().trim());
                    entrenador.setTelefono(txtTelefono.getText().trim());
                    entrenador.setContraseña(new String(txtPassword.getPassword()));

                    // Actualizar especialidades
                    // Limpiamos las actuales
                    entrenador.getEspecialidades().clear();

                    // Agregamos las nuevas especialidades
                    String[] especialidades = txtEspecialidades.getText().split(",");
                    for (String especialidad : especialidades) {
                        if (!especialidad.trim().isEmpty()) {
                            entrenador.agregarEspecialidad(especialidad.trim());
                        }
                    }

                    JOptionPane.showMessageDialog(dialog,
                            "Entrenador actualizado correctamente",
                            "Información", JOptionPane.INFORMATION_MESSAGE);

                    cargarTablaEntrenadores();
                    dialog.dispose();
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
        panel.add(lblApellido);
        panel.add(txtApellido);
        panel.add(lblCorreo);
        panel.add(txtCorreo);
        panel.add(lblTelefono);
        panel.add(txtTelefono);
        panel.add(lblPassword);
        panel.add(txtPassword);
        panel.add(lblEspecialidades);
        panel.add(txtEspecialidades);
        panel.add(btnCancelar);
        panel.add(btnGuardar);

        dialog.getContentPane().add(panel);
        dialog.setVisible(true);
    }

    private void mostrarFormularioPrograma(ProgramaEntrenamiento programa) {
        JDialog dialog = new JDialog(this, programa == null ? "Agregar Programa" : "Editar Programa", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, 10, 10));
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

        JLabel lblEntrenador = new JLabel("Entrenador:");
        JComboBox<String> cmbEntrenador = new JComboBox<>();
        // Agregar opción para no asignar entrenador
        cmbEntrenador.addItem("Sin asignar");

        // Cargar entrenadores
        List<Entrenador> entrenadores = controller.obtenerEntrenadores();
        for (Entrenador entrenador : entrenadores) {
            cmbEntrenador.addItem(entrenador.getNombre() + " " + entrenador.getApellido());
        }

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        // Si estamos editando, cargar datos del programa
        if (programa != null) {
            txtNombre.setText(programa.getNombre());
            txtDescripcion.setText(programa.getDescripcion());
            txtDuracion.setText(String.valueOf(programa.getDuracionSemanas()));

            // Seleccionar tipo
            for (int i = 0; i < TipoPrograma.values().length; i++) {
                if (TipoPrograma.values()[i] == programa.getTipo()) {
                    cmbTipo.setSelectedIndex(i);
                    break;
                }
            }

            // Seleccionar entrenador
            if (programa.getEntrenadorAsignado() != null) {
                for (int i = 0; i < entrenadores.size(); i++) {
                    if (entrenadores.get(i).getId() == programa.getEntrenadorAsignado().getId()) {
                        cmbEntrenador.setSelectedIndex(i + 1); // +1 por la opción "Sin asignar"
                        break;
                    }
                }
            }
        }

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

                    // Obtener entrenador seleccionado
                    Entrenador entrenadorSeleccionado = null;
                    int indiceEntrenador = cmbEntrenador.getSelectedIndex();
                    if (indiceEntrenador > 0) { // 0 es "Sin asignar"
                        entrenadorSeleccionado = entrenadores.get(indiceEntrenador - 1);
                    }

                    if (programa == null) {
                        // Crear nuevo programa
                        ProgramaEntrenamiento nuevoPrograma = controller.crearProgramaEntrenamiento(
                                txtNombre.getText().trim(),
                                txtDescripcion.getText().trim(),
                                duracion,
                                tipoSeleccionado
                        );

                        if (nuevoPrograma != null) {
                            JOptionPane.showMessageDialog(dialog,
                                    "Programa agregado correctamente",
                                    "Información", JOptionPane.INFORMATION_MESSAGE);

                            cargarTablaProgramas();
                            dialog.dispose();
                        } else {
                            JOptionPane.showMessageDialog(dialog,
                                    "Error al agregar el programa",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // Actualizar programa existente
                        programa.setNombre(txtNombre.getText().trim());
                        programa.setDescripcion(txtDescripcion.getText().trim());
                        programa.setDuracionSemanas(duracion);
                        programa.setTipo(tipoSeleccionado);

                        JOptionPane.showMessageDialog(dialog,
                                "Programa actualizado correctamente",
                                "Información", JOptionPane.INFORMATION_MESSAGE);

                        cargarTablaProgramas();
                        dialog.dispose();
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
        panel.add(lblEntrenador);
        panel.add(cmbEntrenador);
        panel.add(btnCancelar);
        panel.add(btnGuardar);

        dialog.getContentPane().add(panel);
        dialog.setVisible(true);
    }

    private void mostrarRutinasPrograma(ProgramaEntrenamiento programa) {
        JDialog dialog = new JDialog(this, "Rutinas del Programa: " + programa.getNombre(), true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton btnAgregarRutina = new JButton("Agregar Rutina");
        JButton btnEditarRutina = new JButton("Editar Rutina");
        JButton btnEliminarRutina = new JButton("Eliminar Rutina");
        JButton btnVerEjercicios = new JButton("Ver Ejercicios");

        panelBotones.add(btnAgregarRutina);
        panelBotones.add(btnEditarRutina);
        panelBotones.add(btnEliminarRutina);
        panelBotones.add(btnVerEjercicios);

        // Tabla de rutinas
        String[] columnas = {"ID", "Nombre", "Descripción", "Duración", "Día", "Ejercicios"};
        DefaultTableModel modeloTablaRutinas = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No permitir edición directa
            }
        };

        JTable tablaRutinas = new JTable(modeloTablaRutinas);
        tablaRutinas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaRutinas.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tablaRutinas);

        // Cargar rutinas en la tabla
        List<Rutina> rutinas = programa.getRutinas();
        for (Rutina rutina : rutinas) {
            Object[] fila = {
                    rutina.getId(),
                    rutina.getNombre(),
                    rutina.getDescripcion(),
                    rutina.getDuracionMinutos() + " min",
                    rutina.getDia().getNombre(),
                    rutina.getEjercicios().size()
            };

            modeloTablaRutinas.addRow(fila);
        }

        // Acciones de los botones
        btnAgregarRutina.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implementar formulario para agregar rutina
                JOptionPane.showMessageDialog(dialog,
                        "Funcionalidad para agregar rutina no implementada",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnEditarRutina.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implementar formulario para editar rutina
                JOptionPane.showMessageDialog(dialog,
                        "Funcionalidad para editar rutina no implementada",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnEliminarRutina.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implementar eliminación de rutina
                JOptionPane.showMessageDialog(dialog,
                        "Funcionalidad para eliminar rutina no implementada",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnVerEjercicios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implementar visualización de ejercicios
                JOptionPane.showMessageDialog(dialog,
                        "Funcionalidad para ver ejercicios no implementada",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        panel.add(panelBotones, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        dialog.getContentPane().add(panel);
        dialog.setVisible(true);
    }

    private void mostrarFormularioMembresia(Miembro miembro) {
        JDialog dialog = new JDialog(this, "Nueva Membresía", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblMiembro = new JLabel("Miembro:");
        JComboBox<String> cmbMiembro = new JComboBox<>();

        // Cargar miembros
        List<Miembro> miembros = controller.obtenerMiembros();
        for (Miembro m : miembros) {
            cmbMiembro.addItem(m.getNombre() + " " + m.getApellido());
        }

        // Si se proporcionó un miembro, seleccionarlo
        if (miembro != null) {
            for (int i = 0; i < miembros.size(); i++) {
                if (miembros.get(i).getId() == miembro.getId()) {
                    cmbMiembro.setSelectedIndex(i);
                    break;
                }
            }
        }

        JLabel lblTipo = new JLabel("Tipo:");
        JComboBox<String> cmbTipo = new JComboBox<>();
        cmbTipo.addItem("Básica");
        cmbTipo.addItem("Estándar");
        cmbTipo.addItem("Premium");

        JLabel lblFechaInicio = new JLabel("Fecha inicio (dd/mm/aaaa):");
        JTextField txtFechaInicio = new JTextField();

        // Por defecto, fecha de hoy
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        txtFechaInicio.setText(sdf.format(new Date()));

        JLabel lblDuracion = new JLabel("Duración (meses):");
        JComboBox<String> cmbDuracion = new JComboBox<>();
        cmbDuracion.addItem("1");
        cmbDuracion.addItem("3");
        cmbDuracion.addItem("6");
        cmbDuracion.addItem("12");

        JLabel lblCosto = new JLabel("Costo:");
        JTextField txtCosto = new JTextField();

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validar campos
                if (txtFechaInicio.getText().trim().isEmpty() ||
                        txtCosto.getText().trim().isEmpty()) {

                    JOptionPane.showMessageDialog(dialog,
                            "Todos los campos son obligatorios",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // Parsear fecha de inicio
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date fechaInicio = sdf.parse(txtFechaInicio.getText().trim());

                    // Calcular fecha fin basada en duración
                    int duracionMeses = Integer.parseInt(cmbDuracion.getSelectedItem().toString());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(fechaInicio);
                    calendar.add(Calendar.MONTH, duracionMeses);
                    Date fechaFin = calendar.getTime();

                    // Parsear costo
                    float costo = Float.parseFloat(txtCosto.getText().trim());

                    // Obtener miembro seleccionado
                    Miembro miembroSeleccionado = miembros.get(cmbMiembro.getSelectedIndex());

                    // Obtener tipo seleccionado
                    String tipo = cmbTipo.getSelectedItem().toString();

                    // Crear membresía
                    if (controller.crearMembresia(miembroSeleccionado, tipo, fechaInicio, fechaFin, costo)) {
                        JOptionPane.showMessageDialog(dialog,
                                "Membresía creada correctamente",
                                "Información", JOptionPane.INFORMATION_MESSAGE);

                        cargarTablaMembresias();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog,
                                "Error al crear la membresía",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Formato de fecha incorrecto. Use dd/mm/aaaa",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Formato de número incorrecto en costo",
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

        panel.add(lblMiembro);
        panel.add(cmbMiembro);
        panel.add(lblTipo);
        panel.add(cmbTipo);
        panel.add(lblFechaInicio);
        panel.add(txtFechaInicio);
        panel.add(lblDuracion);
        panel.add(cmbDuracion);
        panel.add(lblCosto);
        panel.add(txtCosto);
        panel.add(btnCancelar);
        panel.add(btnGuardar);

        dialog.getContentPane().add(panel);
        dialog.setVisible(true);
    }

    private void mostrarFormularioRenovarMembresia(Miembro miembro) {
        JDialog dialog = new JDialog(this, "Renovar Membresía", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblMiembro = new JLabel("Miembro:");
        JLabel lblMiembroValor = new JLabel(miembro.getNombre() + " " + miembro.getApellido());

        JLabel lblTipo = new JLabel("Tipo:");
        JLabel lblTipoValor = new JLabel(miembro.getMembresia().getTipo());

        JLabel lblDuracion = new JLabel("Duración (meses):");
        JComboBox<String> cmbDuracion = new JComboBox<>();
        cmbDuracion.addItem("1");
        cmbDuracion.addItem("3");
        cmbDuracion.addItem("6");
        cmbDuracion.addItem("12");

        JButton btnGuardar = new JButton("Renovar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Calcular nueva fecha fin basada en la actual + duración
                    int duracionMeses = Integer.parseInt(cmbDuracion.getSelectedItem().toString());
                    Calendar calendar = Calendar.getInstance();

                    // Si la membresía ya venció, comenzar desde hoy
                    if (miembro.getMembresia().esVigente()) {
                        calendar.setTime(miembro.getMembresia().getFechaFin());
                    } else {
                        calendar.setTime(new Date());
                    }

                    calendar.add(Calendar.MONTH, duracionMeses);
                    Date nuevaFechaFin = calendar.getTime();

                    // Renovar membresía
                    if (controller.renovarMembresia(miembro, nuevaFechaFin)) {
                        JOptionPane.showMessageDialog(dialog,
                                "Membresía renovada correctamente",
                                "Información", JOptionPane.INFORMATION_MESSAGE);

                        cargarTablaMembresias();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog,
                                "Error al renovar la membresía",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Error al renovar la membresía: " + ex.getMessage(),
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

        panel.add(lblMiembro);
        panel.add(lblMiembroValor);
        panel.add(lblTipo);
        panel.add(lblTipoValor);
        panel.add(lblDuracion);
        panel.add(cmbDuracion);
        panel.add(btnCancelar);
        panel.add(btnGuardar);

        dialog.getContentPane().add(panel);
        dialog.setVisible(true);
    }

    private void generarReporte() {
        String tipoReporte = cmbTipoReporte.getSelectedItem().toString();
        StringBuilder reporte = new StringBuilder();

        switch (tipoReporte) {
            case "Asistencia":
                reporte.append("REPORTE DE ASISTENCIA\n");
                reporte.append("=====================\n\n");
                reporte.append("Fecha de generación: ").append(new Date()).append("\n\n");

                // Este es un reporte de ejemplo, en una implementación real
                // se obtendría la información real del sistema
                reporte.append("Total de asistencias registradas: 156\n");
                reporte.append("Promedio de asistencias diarias: 15.6\n");
                reporte.append("Día con mayor asistencia: Lunes (25)\n");
                reporte.append("Día con menor asistencia: Domingo (8)\n\n");

                reporte.append("Asistencia por horario:\n");
                reporte.append("- Mañana (6:00-12:00): 45%\n");
                reporte.append("- Tarde (12:00-18:00): 35%\n");
                reporte.append("- Noche (18:00-22:00): 20%\n");
                break;

            case "Membresías":
                reporte.append("REPORTE DE MEMBRESÍAS\n");
                reporte.append("=====================\n\n");
                reporte.append("Fecha de generación: ").append(new Date()).append("\n\n");

                List<Miembro> miembros = controller.obtenerMiembros();
                int totalMembresias = 0;
                int membresiasActivas = 0;
                int membresiasCanceladas = 0;
                int membresiasVencidas = 0;

                for (Miembro miembro : miembros) {
                    if (miembro.getMembresia() != null) {
                        totalMembresias++;
                        if (miembro.getMembresia().isActiva()) {
                            if (miembro.getMembresia().esVigente()) {
                                membresiasActivas++;
                            } else {
                                membresiasVencidas++;
                            }
                        } else {
                            membresiasCanceladas++;
                        }
                    }
                }

                reporte.append("Total de membresías: ").append(totalMembresias).append("\n");
                reporte.append("Membresías activas: ").append(membresiasActivas).append("\n");
                reporte.append("Membresías vencidas: ").append(membresiasVencidas).append("\n");
                reporte.append("Membresías canceladas: ").append(membresiasCanceladas).append("\n\n");

                reporte.append("Miembros por tipo de membresía:\n");
                reporte.append("- Premium: 35%\n");
                reporte.append("- Estándar: 45%\n");
                reporte.append("- Básica: 20%\n");
                break;

            case "Entrenadores":
                reporte.append("REPORTE DE ENTRENADORES\n");
                reporte.append("=======================\n\n");
                reporte.append("Fecha de generación: ").append(new Date()).append("\n\n");

                List<Entrenador> entrenadores = controller.obtenerEntrenadores();

                reporte.append("Total de entrenadores: ").append(entrenadores.size()).append("\n\n");

                reporte.append("Entrenadores por especialidad:\n");
                reporte.append("- Musculación: 40%\n");
                reporte.append("- Cardio: 30%\n");
                reporte.append("- Yoga: 15%\n");
                reporte.append("- Pilates: 10%\n");
                reporte.append("- Otros: 5%\n\n");

                reporte.append("Entrenadores con más miembros asignados:\n");
                for (int i = 0; i < entrenadores.size() && i < 5; i++) {
                    Entrenador entrenador = entrenadores.get(i);
                    reporte.append("- ").append(entrenador.getNombre()).append(" ").append(entrenador.getApellido())
                            .append(": ").append(entrenador.getMiembrosAsignados().size()).append(" miembros\n");
                }
                break;

            case "Programas":
                reporte.append("REPORTE DE PROGRAMAS DE ENTRENAMIENTO\n");
                reporte.append("====================================\n\n");
                reporte.append("Fecha de generación: ").append(new Date()).append("\n\n");

                List<ProgramaEntrenamiento> programas = controller.obtenerProgramasEntrenamiento();

                reporte.append("Total de programas: ").append(programas.size()).append("\n\n");

                reporte.append("Programas por tipo:\n");
                reporte.append("- Fuerza: 30%\n");
                reporte.append("- Hipertrofia: 25%\n");
                reporte.append("- Cardio: 20%\n");
                reporte.append("- Flexibilidad: 15%\n");
                reporte.append("- Otros: 10%\n\n");

                reporte.append("Programas con más miembros asignados:\n");
                for (int i = 0; i < programas.size() && i < 5; i++) {
                    ProgramaEntrenamiento programa = programas.get(i);
                    reporte.append("- ").append(programa.getNombre())
                            .append(": ").append(programa.getMiembrosAsignados().size()).append(" miembros\n");
                }
                break;
        }

        txtAreaReporte.setText(reporte.toString());
    }

    private void exportarReporte() {
        if (txtAreaReporte.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Primero debe generar un reporte",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Funcionalidad para exportar reporte no implementada",
                "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void cerrarSesion() {
        controller.cerrarSesion();

        LoginView loginView = new LoginView(controller);
        loginView.setVisible(true);

        this.dispose();
    }
}