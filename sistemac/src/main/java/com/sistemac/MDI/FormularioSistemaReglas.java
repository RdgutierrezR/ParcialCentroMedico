package com.sistemac.MDI;

import com.sistemac.Diagnosticos;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormularioSistemaReglas extends JInternalFrame {
    private JRadioButton q1Si, q1No, q2Si, q2No, q3Si, q3No, q4Si, q4No, q5Si, q5No;
    private JButton btnEnviar;
    private JTextArea txtDiagnostico; // Se reemplazó JLabel por JTextArea
    private int userID;

    // Constructor vacío agregado para evitar errores
    public FormularioSistemaReglas() {
        this(0); // Asigna un ID predeterminado
    }

    public FormularioSistemaReglas(int userID) {
        this.userID = userID;
        setTitle("Sistema de Reglas");
        setSize(500, 400);
        setClosable(true);
        setIconifiable(true);
        setLayout(null);

        JLabel lblUser = new JLabel("Usuario ID: " + userID);
        lblUser.setBounds(20, 20, 300, 20);
        add(lblUser);

        // Preguntas y botones
        addQuestion("¿Sientes dolor en el pecho?", 60, q1Si = new JRadioButton("Sí"), q1No = new JRadioButton("No"));
        addQuestion("¿Tienes dificultad para respirar?", 90, q2Si = new JRadioButton("Sí"), q2No = new JRadioButton("No"));
        addQuestion("¿Tienes palpitaciones?", 120, q3Si = new JRadioButton("Sí"), q3No = new JRadioButton("No"));
        addQuestion("¿Sientes mareos?", 150, q4Si = new JRadioButton("Sí"), q4No = new JRadioButton("No"));
        addQuestion("¿Notas hinchazón en piernas o tobillos?", 180, q5Si = new JRadioButton("Sí"), q5No = new JRadioButton("No"));

        // Área de texto para el diagnóstico
        txtDiagnostico = new JTextArea();
        txtDiagnostico.setBounds(20, 220, 450, 60);
        txtDiagnostico.setLineWrap(true); // Habilita el salto de línea automático
        txtDiagnostico.setWrapStyleWord(true); // Evita cortar palabras
        txtDiagnostico.setEditable(false); // Solo lectura

        // Agregar JScrollPane para permitir desplazamiento si el texto es largo
        JScrollPane scrollPane = new JScrollPane(txtDiagnostico);
        scrollPane.setBounds(20, 220, 450, 60);
        add(scrollPane);

        // Botón para enviar respuestas
        btnEnviar = new JButton("Enviar");
        btnEnviar.setBounds(150, 300, 150, 30);
        add(btnEnviar);

        btnEnviar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ejecutarSistemaDeReglas();
            }
        });
    }

    private void addQuestion(String texto, int y, JRadioButton si, JRadioButton no) {
        JLabel lbl = new JLabel(texto);
        lbl.setBounds(20, y, 300, 20);
        add(lbl);
        si.setBounds(250, y, 50, 20);
        no.setBounds(300, y, 50, 20);
        ButtonGroup group = new ButtonGroup();
        group.add(si);
        group.add(no);
        add(si);
        add(no);
    }

    private boolean validarRespuestas() {
        if (!q1Si.isSelected() && !q1No.isSelected()) return mostrarError(1);
        if (!q2Si.isSelected() && !q2No.isSelected()) return mostrarError(2);
        if (!q3Si.isSelected() && !q3No.isSelected()) return mostrarError(3);
        if (!q4Si.isSelected() && !q4No.isSelected()) return mostrarError(4);
        if (!q5Si.isSelected() && !q5No.isSelected()) return mostrarError(5);
        return true;
    }

    private boolean mostrarError(int pregunta) {
        JOptionPane.showMessageDialog(this, "Por favor, responde la pregunta " + pregunta, "Validación", JOptionPane.WARNING_MESSAGE);
        return false;
    }

    private void ejecutarSistemaDeReglas() {
        if (!validarRespuestas()) return;

        boolean p1 = q1Si.isSelected();
        boolean p2 = q2Si.isSelected();
        boolean p3 = q3Si.isSelected();
        boolean p4 = q4Si.isSelected();
        boolean p5 = q5Si.isSelected();

        // Evaluar el diagnóstico y enviarlo a la API
        String diagnosticoFinal = Diagnosticos.evaluarDiagnostico(userID, p1, p2, p3, p4, p5);
        
        // Mostrar el resultado en la interfaz
        txtDiagnostico.setText("Diagnóstico: " + diagnosticoFinal);
    }
}
