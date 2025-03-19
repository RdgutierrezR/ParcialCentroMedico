package com.sistemac.MDI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class FormularioRegistroUsuario extends JInternalFrame {
    private JTextField txtUserName, txtCedula;
    private JButton btnRegistrar, btnCerrar;
    private MDIPrincipal mdi;

    public FormularioRegistroUsuario(MDIPrincipal mdi) {
        this.mdi = mdi;
        setTitle("Registrar Usuario");
        setSize(400, 250);
        setClosable(true);
        setIconifiable(true);
        setLayout(null);

        JLabel lblUserName = new JLabel("Nombre:");
        lblUserName.setBounds(20, 20, 100, 20);
        add(lblUserName);

        txtUserName = new JTextField();
        txtUserName.setBounds(120, 20, 200, 20);
        add(txtUserName);

        JLabel lblCedula = new JLabel("Cédula:");
        lblCedula.setBounds(20, 60, 100, 20);
        add(lblCedula);

        txtCedula = new JTextField();
        txtCedula.setBounds(120, 60, 200, 20);
        add(txtCedula);

        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBounds(50, 120, 100, 30);
        add(btnRegistrar);

        btnCerrar = new JButton("Cerrar");
        btnCerrar.setBounds(200, 120, 100, 30);
        add(btnCerrar);

        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registrarUsuario();
            }
        });

        btnCerrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void registrarUsuario() {
        String userName = txtUserName.getText().trim();
        String cedula = txtCedula.getText().trim();

        if (userName.isEmpty() || cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            URL url = new URL("http://localhost:3000/api/personas/postPersona");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            JSONObject json = new JSONObject();
            json.put("UserName", userName);
            json.put("Cedula", cedula);

            try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8")) {
                writer.write(json.toString());
                writer.flush();
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            JSONObject jsonResponse = new JSONObject(response.toString());
            int userID = jsonResponse.getJSONObject("data").getInt("idInsertado");

            JOptionPane.showMessageDialog(this, "Usuario registrado: " + userName, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            mdi.abrirFormulario(new FormularioResponder(userID, userName));
            dispose();

            conn.disconnect();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
