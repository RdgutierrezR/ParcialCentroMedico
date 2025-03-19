package com.sistemac.MDI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class FormularioLogin extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtClave;
    private JButton btnLogin;

    public FormularioLogin() {
        setTitle("Inicio de Sesión");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setBounds(20, 20, 100, 20);
        add(lblUsuario);

        txtUsuario = new JTextField();
        txtUsuario.setBounds(100, 20, 150, 20);
        add(txtUsuario);

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
        String usuario = txtUsuario.getText().trim();
        String clave = new String(txtClave.getPassword()).trim();

        if (usuario.isEmpty() || clave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese usuario y clave", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            URL url = new URL("http://localhost:3000/api/login"); // Ajustar URL según API
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            JSONObject json = new JSONObject();
            json.put("usuario", usuario);
            json.put("clave", clave);

            OutputStream os = conn.getOutputStream();
            os.write(json.toString().getBytes("UTF-8"));
            os.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }
                br.close();
                procesarRespuesta(response.toString());
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
            }
            conn.disconnect();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procesarRespuesta(String jsonResponse) {
        try {
            JSONObject json = new JSONObject(jsonResponse);
            int userID = json.getInt("id");
            String userName = json.getString("nombre");
            String rol = json.getString("rol");

            JOptionPane.showMessageDialog(this, "Bienvenido " + userName);

            MDIPrincipal mdi = new MDIPrincipal();
            mdi.setUsuarioRegistrado(userID, userName, rol);
            mdi.setVisible(true);
            this.dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al procesar la respuesta", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FormularioLogin().setVisible(true);
        });
    }
}
