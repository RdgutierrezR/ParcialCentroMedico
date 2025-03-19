package com.sistemac.MDI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class FormularioBuscarUsuario extends JInternalFrame {
    private JTextField txtCedula;
    private JButton btnBuscar, btnCerrar;
    private MDIPrincipal mdi;

    public FormularioBuscarUsuario(MDIPrincipal mdi) {
        this.mdi = mdi;
        setTitle("Buscar Usuario y Responder Preguntas");
        setSize(400, 200);
        setClosable(true);
        setIconifiable(true);
        setLayout(null);

        JLabel lblCedula = new JLabel("Cédula:");
        lblCedula.setBounds(20, 20, 100, 20);
        add(lblCedula);

        txtCedula = new JTextField();
        txtCedula.setBounds(120, 20, 200, 20);
        add(txtCedula);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(50, 80, 100, 30);
        add(btnBuscar);

        btnCerrar = new JButton("Cerrar");
        btnCerrar.setBounds(200, 80, 100, 30);
        add(btnCerrar);

        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buscarUsuario();
            }
        });
        btnCerrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void buscarUsuario() {
        String cedula = txtCedula.getText().trim();
        if(cedula.isEmpty()){
            JOptionPane.showMessageDialog(this, "Ingrese una cédula.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            // Se asume la existencia de un endpoint para buscar al usuario por cédula
            URL url = new URL("http://localhost:3000/api/personas/getPersonaByCedula/" + cedula);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
                StringBuilder response = new StringBuilder();
                String responseLine;
                while((responseLine = br.readLine()) != null){
                    response.append(responseLine.trim());
                }
                JSONObject jsonResponse = new JSONObject(response.toString());
                // Se asume que la respuesta contiene un objeto "data" con los datos del usuario
                JSONObject data = jsonResponse.getJSONObject("data");
                int userID = data.getInt("UserID"); // Ajustar el nombre del atributo según la API
                String userName = data.getString("UserName");

                // Abrir el formulario para responder las preguntas con el usuario encontrado
                mdi.abrirFormulario(new FormularioResponder(userID, userName));
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            conn.disconnect();
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}