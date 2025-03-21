package com.sistemac.MDI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.json.JSONObject;

public class FormularioReportes extends JInternalFrame {
    private JTextField txtCedula;
    private JButton btnGenerarReporte;

    public FormularioReportes() {
        setTitle("Generación de Reportes");
        setSize(500, 350);
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setLayout(new FlowLayout());

        JLabel lblCedula = new JLabel("Ingrese la Cédula:");
        txtCedula = new JTextField(15);
        btnGenerarReporte = new JButton("Generar PDF");

        add(lblCedula);
        add(txtCedula);
        add(btnGenerarReporte);

        btnGenerarReporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cedula = txtCedula.getText().trim();
                if (!cedula.isEmpty()) {
                    generarReporte(cedula);
                } else {
                    JOptionPane.showMessageDialog(null, "Ingrese una cédula válida", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void generarReporte(String cedula) {
        try {
            // Hacer la petición GET al endpoint
            String urlString = "http://localhost:3000/api/reportes/" + cedula;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                JOptionPane.showMessageDialog(null, "Error al obtener datos del servidor", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Leer la respuesta JSON
            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder jsonResponse = new StringBuilder();
            while (scanner.hasNext()) {
                jsonResponse.append(scanner.nextLine());
            }
            scanner.close();
            conn.disconnect();

            // Convertir respuesta en JSON
            JSONObject json = new JSONObject(jsonResponse.toString());
            String diagnostico = json.optString("UltimoDiagnostico", "No disponible");
            String fechaDiagnostico = json.optString("FechaDiagnostico", "No disponible");
            String historial = json.optString("UltimoHistorial", "No disponible");
            String fechaHistorial = json.optString("FechaHistorial", "No disponible");

            // Generar PDF con PDFBox
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            
            // Título
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 700);
            contentStream.showText("Reporte Médico");
            contentStream.endText();

            // Contenido del PDF con manejo correcto de saltos de línea
            agregarTexto(contentStream, "Cédula: " + cedula, 50, 670);
            agregarTexto(contentStream, "Último Diagnóstico: " + diagnostico, 50, 650);
            agregarTexto(contentStream, "Fecha Diagnóstico: " + fechaDiagnostico, 50, 630);
            agregarTexto(contentStream, "Último Historial: " + historial, 50, 610);
            agregarTexto(contentStream, "Fecha Historial: " + fechaHistorial, 50, 590);

            contentStream.close();

            // Guardar el PDF en el escritorio
            String filePath = System.getProperty("user.home") + "/Documentos/reporte_" + cedula + ".pdf";
            document.save(filePath);
            document.close();

            JOptionPane.showMessageDialog(null, "Reporte generado: " + filePath, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            Desktop.getDesktop().open(new File(filePath));

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al generar el reporte", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarTexto(PDPageContentStream contentStream, String texto, float startX, float startY) throws Exception {
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.newLineAtOffset(startX, startY);

        // Dividir el texto por saltos de línea explícitos
        String[] lineas = texto.split("\n");
        for (String linea : lineas) {
            // Dividir cada línea en fragmentos de máximo 80 caracteres
            String[] fragmentos = linea.split("(?<=\\G.{80})");
            for (String fragmento : fragmentos) {
                contentStream.showText(fragmento);
                contentStream.newLineAtOffset(0, -20); // Mover a la siguiente línea
            }
        }

        contentStream.endText();
    }
}
