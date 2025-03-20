package com.sistemac.MDI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class FormularioLogin extends JFrame {
    private JTextField txtCedula;
    private JPasswordField txtClave;
    private JButton btnLogin;

    public FormularioLogin() {
        setTitle("Inicio de Sesión");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblCedula = new JLabel("Cédula:");
        lblCedula.setBounds(20, 20, 100, 20);
        add(lblCedula);

        txtCedula = new JTextField();
        txtCedula.setBounds(100, 20, 150, 20);
        add(txtCedula);

        JLabel lblClave = new JLabel("Clave:");
        lblClave.setBounds(20, 60, 100, 20);
        add(lblClave);

        txtClave = new JPasswordField();
        txtClave.setBounds(100, 60, 150, 20);
        add(txtClave);

        btnLogin = new JButton("Ingresar");
        btnLogin.setBounds(100, 100, 100, 30);
        add(btnLogin);

        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                autenticarUsuario();
            }
        });
    }

    private void autenticarUsuario() {
        String cedula = txtCedula.getText().trim();
        String clave = new String(txtClave.getPassword()).trim();

        if (cedula.isEmpty() || clave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese cédula y clave", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            URL url = new URL("http://localhost:3000/api/usuarios/login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            JSONObject json = new JSONObject();
            json.put("cedula", cedula);
            json.put("password", clave);

            OutputStream os = conn.getOutputStream();
            os.write(json.toString().getBytes("UTF-8"));
            os.close();

            int responseCode = conn.getResponseCode();
            BufferedReader br;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }
                br.close();
                procesarRespuesta(response.toString());
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }
                br.close();
                JOptionPane.showMessageDialog(this, "Error: " + response.toString(), "Login fallido", JOptionPane.ERROR_MESSAGE);
            }
            conn.disconnect();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procesarRespuesta(String jsonResponse) {
        try {
            System.out.println("🔍 Respuesta recibida: " + jsonResponse); // Depuración

            JSONObject json = new JSONObject(jsonResponse);

            if (json.has("success") && json.getBoolean("success")) {
                String mensaje = json.getString("message");
                String token = json.getString("token");

                // 📌 Extraer datos del usuario
                JSONObject user = json.getJSONObject("user");
                int userID = user.getInt("UserID");
                String userName = user.getString("UserName");
                String rol = user.getString("Rol");

                JOptionPane.showMessageDialog(this, mensaje);

                System.out.println("🔑 Token recibido: " + token);
                System.out.println("👤 Usuario: " + userID + " - " + userName + " - " + rol);

                // 📌 Abrir MDIPrincipal y pasar los datos del usuario
                MDIPrincipal mdi = new MDIPrincipal();
                mdi.setUsuarioRegistrado(userID, userName, rol);
                mdi.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Login fallido", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error al procesar la respuesta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FormularioLogin().setVisible(true);
        });
    }
}
