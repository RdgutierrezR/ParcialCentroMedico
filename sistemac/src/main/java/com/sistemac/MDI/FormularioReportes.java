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
            String urlString = "http://localhost:3000/api/reportes/" + cedula;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                JOptionPane.showMessageDialog(null, "Error al obtener datos del servidor", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder jsonResponse = new StringBuilder();
            while (scanner.hasNext()) {
                jsonResponse.append(scanner.nextLine());
            }
            scanner.close();
            conn.disconnect();

            JSONObject json = new JSONObject(jsonResponse.toString());
            String diagnostico = json.optString("UltimoDiagnostico", "No disponible");
            String fechaDiagnostico = json.optString("FechaDiagnostico", "No disponible");
            String historial = json.optString("UltimoHistorial", "No disponible");
            String fechaHistorial = json.optString("FechaHistorial", "No disponible");

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
            contentStream.close(); // Cerrar el stream del título

            // Escribir contenido con manejo de páginas
            escribirContenido(document, cedula, diagnostico, fechaDiagnostico, historial, fechaHistorial);

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

    private void escribirContenido(PDDocument document, String cedula, String diagnostico, 
                                  String fechaDiag, String historial, String fechaHist) throws Exception {
        float yPosition = 670; // Posición inicial después del título
        PDPage currentPage = document.getPage(0);
        PDPageContentStream contentStream = new PDPageContentStream(document, currentPage, PDPageContentStream.AppendMode.APPEND, true);
        
        // Función helper para agregar texto con manejo de páginas
        yPosition = agregarTexto(contentStream, document, "Cédula: " + cedula, 50, yPosition);
        yPosition = agregarTexto(contentStream, document, "Último Diagnóstico: " + diagnostico, 50, yPosition);
        yPosition = agregarTexto(contentStream, document, "Fecha Diagnóstico: " + fechaDiag, 50, yPosition);
        yPosition = agregarTexto(contentStream, document, "Último Historial: " + historial, 50, yPosition);
        agregarTexto(contentStream, document, "Fecha Historial: " + fechaHist, 50, yPosition);
        
        contentStream.close();
    }

    private float agregarTexto(PDPageContentStream contentStream, PDDocument document, 
                              String texto, float x, float y) throws Exception {
        final float LINE_HEIGHT = 20f;
        final float BOTTOM_MARGIN = 50f;
        
        PDPage currentPage = document.getPage(document.getNumberOfPages() - 1);
        float currentY = y;
        
        String[] lineas = texto.split("\n");
        for (String linea : lineas) {
            String[] fragmentos = linea.split("(?<=\\G.{80})");
            
            for (String fragmento : fragmentos) {
                if (currentY < BOTTOM_MARGIN) {
                    contentStream.close();
                    currentPage = new PDPage();
                    document.addPage(currentPage);
                    contentStream = new PDPageContentStream(document, currentPage);
                    currentY = 700; // Reiniciar posición en nueva página
                }
                
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(x, currentY);
                contentStream.showText(fragmento);
                contentStream.endText();
                
                currentY -= LINE_HEIGHT;
            }
        }
        return currentY;
    }
}
