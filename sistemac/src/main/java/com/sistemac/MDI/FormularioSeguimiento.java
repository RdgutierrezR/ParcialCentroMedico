package com.sistemac.MDI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class FormularioSeguimiento extends JInternalFrame {
    private JTextField txtCedula;
    private JButton btnBuscar;
    private JTable tableHistorial;
    private DefaultTableModel tableModel;

    public FormularioSeguimiento() {
        setTitle("Seguimiento de Pacientes");
        setSize(600, 400);
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setLayout(new BorderLayout());

        // Panel superior con campo de búsqueda
        JPanel panelBusqueda = new JPanel();
        panelBusqueda.add(new JLabel("Cédula:"));
        txtCedula = new JTextField(10);
        panelBusqueda.add(txtCedula);
        btnBuscar = new JButton("Buscar");
        panelBusqueda.add(btnBuscar);

        add(panelBusqueda, BorderLayout.NORTH);

        // Tabla para mostrar historial
        String[] columnas = {"HistorialID", "DiagnosticoID", "Fecha", "Descripcion"};
        tableModel = new DefaultTableModel(columnas, 0);
        tableHistorial = new JTable(tableModel);
        add(new JScrollPane(tableHistorial), BorderLayout.CENTER);

        // Acción del botón
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarHistorial();
            }
        });
    }

    // Método para obtener historial desde la API
    private void buscarHistorial() {
        String cedula = txtCedula.getText().trim();
        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una cédula", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // URL de la API (Asegúrate de que la API esté en ejecución)
            String apiUrl = "http://localhost:3000/api/historial/cedula/" + cedula;

            // Configurar la conexión HTTP
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            // Verificar si la respuesta es exitosa (código 200)
            if (conn.getResponseCode() != 200) {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado o sin historial", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Leer la respuesta de la API
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                response.append(output);
            }
            conn.disconnect();

            // Convertir la respuesta en JSON
            JSONArray jsonArray = new JSONArray(response.toString());

            // Limpiar tabla antes de insertar nuevos datos
            tableModel.setRowCount(0);

            // Llenar la tabla con los datos del historial
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                int historialID = obj.getInt("HistorialID");
                int diagnosticoID = obj.getInt("DiagnosticoID");
                String fecha = obj.getString("FechaDiagnostico");
                String descripcion = obj.getString("Descripcion");

                tableModel.addRow(new Object[]{historialID, diagnosticoID, fecha, descripcion});
            }

            // Si no hay registros, mostrar mensaje
            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No hay historiales para este usuario.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
