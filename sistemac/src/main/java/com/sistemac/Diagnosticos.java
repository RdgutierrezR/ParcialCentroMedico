package com.sistemac;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Diagnosticos {
    public static void main(String[] args) {
        Rules reglas = new Rules();
        reglas.register(new Combinaciones()); // ✅ Agregamos la nueva regla combinada

        Facts datos = new Facts();
        datos.put("q1", true);
        datos.put("q2", true);
        datos.put("q3", true);
        datos.put("q4", true);
        datos.put("q5", true);
        datos.put("resultado", new StringBuilder());

        RulesEngine motor = new DefaultRulesEngine();
        motor.fire(reglas, datos);

        // Obtener diagnóstico final
        String diagnosticoFinal = datos.get("resultado").toString();
        System.out.println("Diagnóstico final:\n" + diagnosticoFinal);

        // Enviar diagnóstico a la API
        enviarDiagnostico(3, datos.get("q1"), datos.get("q2"), datos.get("q3"), datos.get("q4"), datos.get("q5"), diagnosticoFinal);
    }

     
    public static void enviarDiagnostico(int userID, boolean q1, boolean q2, boolean q3, boolean q4, boolean q5, String diagnostico) {
        try {
            URL url = new URL("http://localhost:3000/api/diagnosticos");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
    
            // ✅ Asegurar que JSON tenga la estructura correcta
            String jsonInputString = "{"
                    + "\"UserID\": " + userID + ", "
                    + "\"Pregunta1\": " + q1 + ", "
                    + "\"Pregunta2\": " + q2 + ", "
                    + "\"Pregunta3\": " + q3 + ", "
                    + "\"Pregunta4\": " + q4 + ", "
                    + "\"Pregunta5\": " + q5
                    + "}"; 
    
            System.out.println("JSON enviado: " + jsonInputString); // ✅ Verificar JSON antes de enviarlo
    
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
    
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("Respuesta del servidor: " + response.toString());
                }
            } else {
                System.out.println("Error en la solicitud: " + responseCode);
            }
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    



}
