package com.sistemac.MDI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

public class FormularioHistorial extends JInternalFrame {
    private JTextArea txtHistorial;
    private JButton btnGuardar;
    private int userID; // 📌 Atributo para almacenar el ID del usuario

    public FormularioHistorial(int userID) { // ✅ Recibe el ID del usuario
        super("Historial Médico", true, true, true, true);
        this.userID = userID; // 📌 Asigna el ID del usuario
        setSize(500, 350);
        setLayout(new BorderLayout());

        // Área de texto para el historial
        txtHistorial = new JTextArea(10, 40);
        txtHistorial.setLineWrap(true);
        txtHistorial.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(txtHistorial);

        // Botón para guardar el historial
        btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarHistorial();
            }
        });

        // Panel inferior con botón
        JPanel panelInferior = new JPanel();
        panelInferior.add(btnGuardar);

        // Agregar componentes al formulario
        add(scrollPane, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }
private void guardarHistorial() {
    String historialTexto = txtHistorial.getText().trim();

    if (historialTexto.isEmpty()) {
        JOptionPane.showMessageDialog(this, "El historial no puede estar vacío.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        // 🔥 Agregar DiagnosticoID (debes definir su valor)
        int diagnosticoID = 2; // 📌 Ajusta este valor según corresponda

        // Crear JSON con los datos requeridos
        JSONObject json = new JSONObject();
        json.put("UserID", userID);
        json.put("DiagnosticoID", diagnosticoID);
        json.put("Descripcion", historialTexto);

        // Cliente HTTP
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:3000/api/historial"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        // Enviar solicitud
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Mostrar respuesta de la API
        System.out.println("Código de respuesta: " + response.statusCode());
        System.out.println("Cuerpo de respuesta: " + response.body());

        if (response.statusCode() == 201) {
            JOptionPane.showMessageDialog(this, "Historial guardado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            txtHistorial.setText(""); // Limpiar campo
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar el historial: " + response.body(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error en la conexión con la API: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

}
