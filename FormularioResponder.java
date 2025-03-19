package com.sistemac.MDI;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class FormularioResponder extends JInternalFrame {
    private JRadioButton q1Si, q1No, q2Si, q2No, q3Si, q3No, q4Si, q4No, q5Si, q5No;
    private JButton btnEnviar;
    private JLabel lblDiagnostico;
    private int userID;
    private String userName;

    public FormularioResponder(int userID, String userName) {
        this.userID = userID;
        this.userName = userName;
        setTitle("Responder Preguntas");
        setSize(500, 400);
        setClosable(true);
        setIconifiable(true);
        setLayout(null);

        JLabel lblUser = new JLabel("Usuario: " + userName);
        lblUser.setBounds(20, 20, 300, 20);
        add(lblUser);

        ButtonGroup group1 = new ButtonGroup();
        JLabel lblQ1 = new JLabel("¿Sientes dolor en el pecho?");
        lblQ1.setBounds(20, 60, 300, 20);
        add(lblQ1);
        q1Si = new JRadioButton("Sí");
        q1No = new JRadioButton("No");
        q1Si.setBounds(250, 60, 50, 20);
        q1No.setBounds(300, 60, 50, 20);
        group1.add(q1Si);
        group1.add(q1No);
        add(q1Si);
        add(q1No);

        ButtonGroup group2 = new ButtonGroup();
        JLabel lblQ2 = new JLabel("¿Tienes dificultad para respirar?");
        lblQ2.setBounds(20, 90, 300, 20);
        add(lblQ2);
        q2Si = new JRadioButton("Sí");
        q2No = new JRadioButton("No");
        q2Si.setBounds(250, 90, 50, 20);
        q2No.setBounds(300, 90, 50, 20);
        group2.add(q2Si);
        group2.add(q2No);
        add(q2Si);
        add(q2No);

        ButtonGroup group3 = new ButtonGroup();
        JLabel lblQ3 = new JLabel("¿Tienes palpitaciones?");
        lblQ3.setBounds(20, 120, 300, 20);
        add(lblQ3);
        q3Si = new JRadioButton("Sí");
        q3No = new JRadioButton("No");
        q3Si.setBounds(250, 120, 50, 20);
        q3No.setBounds(300, 120, 50, 20);
        group3.add(q3Si);
        group3.add(q3No);
        add(q3Si);
        add(q3No);

        ButtonGroup group4 = new ButtonGroup();
        JLabel lblQ4 = new JLabel("¿Sientes mareos?");
        lblQ4.setBounds(20, 150, 300, 20);
        add(lblQ4);
        q4Si = new JRadioButton("Sí");
        q4No = new JRadioButton("No");
        q4Si.setBounds(250, 150, 50, 20);
        q4No.setBounds(300, 150, 50, 20);
        group4.add(q4Si);
        group4.add(q4No);
        add(q4Si);
        add(q4No);

        ButtonGroup group5 = new ButtonGroup();
        JLabel lblQ5 = new JLabel("¿Notas hinchazón en piernas o tobillos?");
        lblQ5.setBounds(20, 180, 300, 20);
        add(lblQ5);
        q5Si = new JRadioButton("Sí");
        q5No = new JRadioButton("No");
        q5Si.setBounds(250, 180, 50, 20);
        q5No.setBounds(300, 180, 50, 20);
        group5.add(q5Si);
        group5.add(q5No);
        add(q5Si);
        add(q5No);

        lblDiagnostico = new JLabel("Diagnóstico: ");
        lblDiagnostico.setBounds(20, 220, 450, 30);
        add(lblDiagnostico);

        btnEnviar = new JButton("Enviar");
        btnEnviar.setBounds(150, 270, 150, 30);
        add(btnEnviar);

        btnEnviar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enviarDatos();
            }
        });
    }

    private void enviarDatos() {
    // Validaciones: verificar que cada pregunta tenga respuesta
    if (!q1Si.isSelected() && !q1No.isSelected()) {
        JOptionPane.showMessageDialog(this, "Por favor, responde la pregunta 1.", "Validación", JOptionPane.WARNING_MESSAGE);
        return;
    }
    if (!q2Si.isSelected() && !q2No.isSelected()) {
        JOptionPane.showMessageDialog(this, "Por favor, responde la pregunta 2.", "Validación", JOptionPane.WARNING_MESSAGE);
        return;
    }
    if (!q3Si.isSelected() && !q3No.isSelected()) {
        JOptionPane.showMessageDialog(this, "Por favor, responde la pregunta 3.", "Validación", JOptionPane.WARNING_MESSAGE);
        return;
    }
    if (!q4Si.isSelected() && !q4No.isSelected()) {
        JOptionPane.showMessageDialog(this, "Por favor, responde la pregunta 4.", "Validación", JOptionPane.WARNING_MESSAGE);
        return;
    }
    if (!q5Si.isSelected() && !q5No.isSelected()) {
        JOptionPane.showMessageDialog(this, "Por favor, responde la pregunta 5.", "Validación", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Obtener las respuestas de cada pregunta
    boolean p1 = q1Si.isSelected();
    boolean p2 = q2Si.isSelected();
    boolean p3 = q3Si.isSelected();
    boolean p4 = q4Si.isSelected();
    boolean p5 = q5Si.isSelected();

    String jsonInputString = "{" +
            "\"UserID\": " + userID + ", " +
            "\"Pregunta1\": " + p1 + ", " +
            "\"Pregunta2\": " + p2 + ", " +
            "\"Pregunta3\": " + p3 + ", " +
            "\"Pregunta4\": " + p4 + ", " +
            "\"Pregunta5\": " + p5 + "}";
    
    try {
        URL url = new URL("http://localhost:3000/api/diagnosticos");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                JSONObject jsonResponse = new JSONObject(response.toString());
                String descripcion = jsonResponse.getString("Descripcion");
                lblDiagnostico.setText("Diagnóstico: " + descripcion);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error en la solicitud: " + responseCode, "Error", JOptionPane.ERROR_MESSAGE);
        }
        conn.disconnect();
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error de conexión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

}
