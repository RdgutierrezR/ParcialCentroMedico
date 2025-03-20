package com.sistemac.MDI;

import javax.swing.*;

public class MDIPrincipal extends JFrame {
    private JDesktopPane desktopPane;
    private Integer userID = null;
    private String userName = null;
    private String rolUsuario = null;  // Nuevo: Variable para el rol

    public MDIPrincipal() {
        setTitle("Sistema de Diagnóstico");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        desktopPane = new JDesktopPane();
        setContentPane(desktopPane);

        // Si se ejecuta sin usuario, mostrar mensaje y abrir login
      if (userID == null) {
    // Puedes mostrar un mensaje indicando que debe loguearse
    JOptionPane.showMessageDialog(this, "Por favor inicie sesión.");
}

    }

    public void inicializarMenu() {
        if (rolUsuario == null) return; // Evita inicializar sin usuario

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Opciones");

        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> System.exit(0));

        if ("Paciente".equals(rolUsuario)) {
            JMenuItem itemReglas = new JMenuItem("Sistema de Reglas");
            JMenuItem itemHistorial = new JMenuItem("Historial");

            itemReglas.addActionListener(e -> abrirFormulario(new FormularioSistemaReglas()));
            itemHistorial.addActionListener(e -> abrirFormulario(new FormularioHistorial()));

            menu.add(itemReglas);
            menu.add(itemHistorial);
        }

        if ("Médico".equals(rolUsuario)) {
            JMenuItem itemReglas = new JMenuItem("Sistema de Reglas");
            JMenuItem itemSeguimiento = new JMenuItem("Seguimiento");
            JMenuItem itemReportes = new JMenuItem("Generar Reportes");

            itemReglas.addActionListener(e -> abrirFormulario(new FormularioSistemaReglas()));
            itemSeguimiento.addActionListener(e -> abrirFormulario(new FormularioSeguimiento()));
            itemReportes.addActionListener(e -> abrirFormulario(new FormularioReportes()));

            menu.add(itemReglas);
            menu.add(itemSeguimiento);
            menu.add(itemReportes);
        }

        menu.addSeparator();
        menu.add(itemSalir);

        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    public void abrirFormulario(JInternalFrame formulario) {
        desktopPane.add(formulario);
        formulario.setVisible(true);
    }

    public void setUsuarioRegistrado(int userID, String userName, String rol) {
        this.userID = userID;
        this.userName = userName;
        this.rolUsuario = rol;
        inicializarMenu();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FormularioLogin().setVisible(true);
        });
    }
}

