package com.sistemac.MDI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class FormularioRegistroUsuario extends JFrame {
    private JTextField txtCedula, txtUserName;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JComboBox<String> cmbRol;
    private JButton btnRegistrar, btnCerrar;
    private MDIPrincipal mdi;

    private static final Map<String, Integer> ROLES_MAP = new HashMap<>();

    static {
        ROLES_MAP.put("Administrador", 1);
        ROLES_MAP.put("Médico", 2);
        ROLES_MAP.put("Paciente", 3);
    }

    public FormularioRegistroUsuario(MDIPrincipal mdi) {
        this.mdi = mdi;
        setTitle("Registrar Usuario");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblCedula = new JLabel("Cédula:");
        lblCedula.setBounds(20, 20, 100, 20);
        add(lblCedula);

        txtCedula = new JTextField();
        txtCedula.setBounds(120, 20, 200, 20);
        add(txtCedula);

        JLabel lblUserName = new JLabel("Nombre:");
        lblUserName.setBounds(20, 60, 100, 20);
        add(lblUserName);

        txtUserName = new JTextField();
        txtUserName.setBounds(120, 60, 200, 20);
        add(txtUserName);

        JLabel lblPassword = new JLabel("Clave:");
        lblPassword.setBounds(20, 100, 100, 20);
        add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(120, 100, 200, 20);
        add(txtPassword);

        JLabel lblConfirmPassword = new JLabel("Confirmar Clave:");
        lblConfirmPassword.setBounds(20, 140, 120, 20);
        add(lblConfirmPassword);

        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setBounds(120, 140, 200, 20);
        add(txtConfirmPassword);

        JLabel lblRol = new JLabel("Rol:");
        lblRol.setBounds(20, 180, 100, 20);
        add(lblRol);

        String[] roles = {"Administrador", "Médico", "Paciente"};
        cmbRol = new JComboBox<>(roles);
        cmbRol.setBounds(120, 180, 200, 20);
        add(cmbRol);

        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBounds(50, 220, 100, 30);
        add(btnRegistrar);

        btnCerrar = new JButton("Cerrar");
        btnCerrar.setBounds(200, 220, 100, 30);
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
        String cedula = txtCedula.getText().trim();
        String userName = txtUserName.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String confirmPassword = new String(txtConfirmPassword.getPassword()).trim();
        String rolName = (String) cmbRol.getSelectedItem();

        if (cedula.isEmpty() || userName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Las claves no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Integer rolId = ROLES_MAP.get(rolName);
        if (rolId == null) {
            JOptionPane.showMessageDialog(this, "Rol no válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            URL url = new URL("http://localhost:3000/api/usuarios/register");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            JSONObject json = new JSONObject();
            json.put("cedula", cedula);
            json.put("userName", userName);
            json.put("password", password);
            json.put("rolId", rolId);

            try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8")) {
                writer.write(json.toString());
                writer.flush();
            }

            int responseCode = conn.getResponseCode();
            InputStream inputStream = responseCode < HttpURLConnection.HTTP_BAD_REQUEST ? conn.getInputStream() : conn.getErrorStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String response = reader.lines().reduce("", (acc, line) -> acc + line);
            reader.close();

            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                JOptionPane.showMessageDialog(this, "Usuario registrado: " + userName, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error: " + response, "Error", JOptionPane.ERROR_MESSAGE);
            }
            conn.disconnect();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
