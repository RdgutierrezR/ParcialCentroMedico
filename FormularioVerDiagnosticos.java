package com.sistemac.MDI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class FormularioVerDiagnosticos extends JInternalFrame {
    private JTextField txtUserID;
    private JButton btnBuscar;
    private JTextArea txtResultado;

    public FormularioVerDiagnosticos() {
        setTitle("Ver Diagnósticos");
        setSize(500, 400);
        setClosable(true);
        setIconifiable(true);
        setLayout(null);

        JLabel lblUserID = new JLabel("UserID:");
        lblUserID.setBounds(20, 20, 100, 20);
        add(lblUserID);

        txtUserID = new JTextField();
        txtUserID.setBounds(100, 20, 100, 20);
        add(txtUserID);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(220, 20, 100, 20);
        add(btnBuscar);
        
        txtResultado = new JTextArea();
        txtResultado.setBounds(20, 60, 440, 280);
        txtResultado.setEditable(false);
        add(txtResultado);

        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                obtenerDiagnosticos();
            }
        });
    }

    private void obtenerDiagnosticos() {
        String userID = txtUserID.getText().trim();
        if (userID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un UserID válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            URL url = new URL("http://localhost:3000/api/diagnosticos/" + userID);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    mostrarDiagnosticos(response.toString()); // ✅ Llamamos a la función corregida
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error en la solicitud: " + responseCode, "Error", JOptionPane.ERROR_MESSAGE);
            }
            conn.disconnect();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDiagnosticos(String jsonResponse) {
        txtResultado.setText(""); // ✅ Limpiamos el área de texto antes de mostrar datos

        try {
            // ✅ Verificamos si la respuesta es un array o un objeto JSON
            if (jsonResponse.startsWith("[")) { // Si comienza con '[', es un JSONArray
                JSONArray jsonArray = new JSONArray(jsonResponse);

                if (jsonArray.length() == 0) {
                    txtResultado.setText("No hay diagnósticos para este usuario.");
                    return;
                }

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    txtResultado.append("Fecha: " + obj.getString("FechaRepuesta") + "\n");
                    txtResultado.append("Diagnóstico: " + obj.getString("Descripcion") + "\n");
                    txtResultado.append("-----------------------------\n");
                }
            } else { // Si no es un array, lo tratamos como objeto
                JSONObject jsonObj = new JSONObject(jsonResponse);

                if (jsonObj.has("mensaje")) { // ✅ Si tiene un mensaje, mostramos el mensaje
                    txtResultado.setText(jsonObj.getString("mensaje"));
                } else {
                    txtResultado.setText("Respuesta inesperada del servidor.");
                }
            }
        } catch (Exception e) {
            txtResultado.setText("Error al procesar la respuesta.");
            e.printStackTrace();
        }
    }
}
