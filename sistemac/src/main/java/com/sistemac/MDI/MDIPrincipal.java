package com.sistemac.MDI;

import javax.swing.*;

public class MDIPrincipal extends JFrame {
    private JDesktopPane desktopPane;
    private Integer userID;
    private String userName;
    private String rolUsuario;

    public MDIPrincipal(int userID, String userName, String rol) {
        this.userID = userID;
        this.userName = userName;
        this.rolUsuario = rol;

        inicializarVentana();
        inicializarMenu();
    }

    private void inicializarVentana() {
        setTitle("Sistema de Diagnóstico");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        desktopPane = new JDesktopPane();
        setContentPane(desktopPane);
    }

    private void inicializarMenu() {
        if (rolUsuario == null) return;

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Opciones");

        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(
                this, 
                "¿Está seguro de que desea salir?", 
                "Confirmar salida", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE
            );

            if (respuesta == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        if ("Paciente".equalsIgnoreCase(rolUsuario)) {
            JMenuItem itemReglas = new JMenuItem("Sistema de Reglas");
            JMenuItem itemHistorial = new JMenuItem("Historial");

            itemReglas.addActionListener(e -> abrirFormulario(new FormularioSistemaReglas(userID, userName)));
            itemHistorial.addActionListener(e -> abrirFormulario(new FormularioHistorial(userID)));

            menu.add(itemReglas);
            menu.add(itemHistorial);
        } else if ("Médico".equalsIgnoreCase(rolUsuario)) {
            JMenuItem itemReglas = new JMenuItem("Sistema de Reglas");
            JMenuItem itemSeguimiento = new JMenuItem("Seguimiento");
            JMenuItem itemReportes = new JMenuItem("Generar Reportes");

            itemReglas.addActionListener(e -> abrirFormulario(new FormularioSistemaReglas(userID, userName)));
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
        SwingUtilities.invokeLater(() -> {
            if (!formulario.isVisible()) {
                desktopPane.add(formulario);
                formulario.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FormularioLogin().setVisible(true);
        });
    }
}
