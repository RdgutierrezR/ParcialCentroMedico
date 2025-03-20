package com.sistemac;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class Diagnosticos {

    public static String evaluarDiagnostico(int userID, boolean q1, boolean q2, boolean q3, boolean q4, boolean q5) {
        Rules reglas = new Rules();
        reglas.register(new Combinaciones());

        Facts datos = new Facts();
        datos.put("q1", q1);
        datos.put("q2", q2);
        datos.put("q3", q3);
        datos.put("q4", q4);
        datos.put("q5", q5);
        datos.put("resultado", new StringBuilder());

        RulesEngine motor = new DefaultRulesEngine();
        motor.fire(reglas, datos);

        // Obtener el diagnóstico final generado
        String diagnosticoFinal = datos.get("resultado").toString();
        System.out.println("Diagnóstico final:\n" + diagnosticoFinal);

        // Enviar diagnóstico y síntomas a la API
        enviarDiagnosticoSintomas(userID, q1, q2, q3, q4, q5, diagnosticoFinal);

        return diagnosticoFinal;
    }

    public static void enviarDiagnosticoSintomas(int userID, boolean q1, boolean q2, boolean q3, boolean q4, boolean q5, String descripcion) {
        try {
            URL url = new URL("http://localhost:3000/api/diagnostico-sintomas");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Convertir booleanos a enteros (0 o 1)
            int pregunta1 = q1 ? 1 : 0;
            int pregunta2 = q2 ? 1 : 0;
            int pregunta3 = q3 ? 1 : 0;
            int pregunta4 = q4 ? 1 : 0;
            int pregunta5 = q5 ? 1 : 0;

            // Crear JSON con los datos completos
            JSONObject json = new JSONObject();
            json.put("UserID", userID);
            json.put("Pregunta1", pregunta1);
            json.put("Pregunta2", pregunta2);
            json.put("Pregunta3", pregunta3);
            json.put("Pregunta4", pregunta4);
            json.put("Pregunta5", pregunta5);
            json.put("Descripcion", descripcion);

            String jsonInputString = json.toString();
            System.out.println("JSON enviado: " + jsonInputString);

            // Enviar la solicitud
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Leer la respuesta del servidor
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Datos guardados correctamente.");
            } else {
                System.out.println("Error en la solicitud: " + responseCode);
            }
            conn.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
