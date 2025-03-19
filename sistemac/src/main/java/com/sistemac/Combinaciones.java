package com.sistemac;

import org.jeasy.rules.annotation.*;
import java.util.HashMap;
import java.util.Map;

@Rule(name = "Diagnóstico Combinado", description = "Determina el diagnóstico según combinaciones de síntomas.")
public class Combinaciones {
    
    private static final Map<String, String> diagnosticos = new HashMap<>();

    static {
        // ✅ Definir las 32 combinaciones de respuestas
        diagnosticos.put("00000", "Sin síntomas; hallazgo normal según criterios de la Mayo Clinic y la AHA.");
        diagnosticos.put("00001", "Síntoma aislado de mareos; seguimiento recomendado.");
        diagnosticos.put("00010", "Disnea leve; posible inicio de compromiso cardíaco, evaluar clínicamente.");
        diagnosticos.put("00011", "Disnea y mareos; sospecha de compromiso cardiovascular, revisión médica aconsejada.");
        diagnosticos.put("00100", "Edema aislado en miembros inferiores; evaluar función cardíaca y causas de retención.");
        diagnosticos.put("00101", "Edema junto a mareos; posible insuficiencia cardíaca incipiente, se requiere evaluación.");
        diagnosticos.put("00110", "Edema y disnea; signos de insuficiencia cardíaca, evaluación detallada necesaria.");
        diagnosticos.put("00111", "Edema, disnea y mareos; alto riesgo de insuficiencia cardíaca, atención urgente.");
        diagnosticos.put("01000", "Dolor torácico aislado; posible angina, evaluar para descartar enfermedad coronaria.");
        diagnosticos.put("01001", "Dolor torácico y mareos; aumenta la sospecha de angina, se recomienda revisión.");
        diagnosticos.put("01010", "Dolor torácico y disnea; riesgo de angina inestable, evaluación urgente.");
        diagnosticos.put("01011", "Dolor, disnea y mareos; alto riesgo de eventos coronarios, atención inmediata.");
        diagnosticos.put("01100", "Dolor torácico y edema; posible hipertensión o inicio de insuficiencia, evaluar.");
        diagnosticos.put("01101", "Dolor, edema y mareos; compromiso cardiovascular moderado, revisión médica aconsejada.");
        diagnosticos.put("01110", "Dolor torácico, edema y disnea; indicativos de compromiso avanzado, evaluación urgente.");
        diagnosticos.put("01111", "Síntomas múltiples; alta probabilidad de enfermedad cardiovascular severa, atención inmediata.");
        diagnosticos.put("10000", "Palpitaciones aisladas; posibles arritmias, se recomienda monitoreo.");
        diagnosticos.put("10001", "Palpitaciones con mareos; mayor sospecha de arritmia, evaluar clínicamente.");
        diagnosticos.put("10010", "Palpitaciones y disnea; posible arritmia con compromiso funcional, evaluar.");
        diagnosticos.put("10011", "Palpitaciones, disnea y mareos; arritmia preocupante, atención urgente.");
        diagnosticos.put("10100", "Palpitaciones y edema; sugiere alteración en la función cardíaca, evaluación detallada.");
        diagnosticos.put("10101", "Palpitaciones, edema y mareos; compromiso moderado, seguimiento clínico sugerido.");
        diagnosticos.put("10110", "Palpitaciones, edema y disnea; posible insuficiencia cardíaca asociada, evaluar urgentemente.");
        diagnosticos.put("10111", "Alto riesgo cardiovascular por combinación de síntomas; atención médica inmediata.");
        diagnosticos.put("11000", "Palpitaciones con dolor torácico; riesgo de enfermedad coronaria o arritmia, evaluar.");
        diagnosticos.put("11001", "Palpitaciones, dolor torácico y mareos; elevada sospecha de eventos cardíacos, revisión urgente.");
        diagnosticos.put("11010", "Palpitaciones, dolor torácico y disnea; alto riesgo de complicaciones, atención médica inmediata.");
        diagnosticos.put("11011", "Combinación de palpitaciones, dolor, disnea y mareos; alta probabilidad de eventos severos, atención urgente.");
        diagnosticos.put("11100", "Palpitaciones, dolor torácico y edema; sugiere compromiso en la función cardíaca, evaluar.");
        diagnosticos.put("11101", "Palpitaciones, dolor, edema y mareos; combinación de síntomas moderada a alta, revisión completa recomendada.");
        diagnosticos.put("11110", "Palpitaciones, dolor, edema y disnea; hallazgo altamente sugestivo de enfermedad cardiovascular avanzada, atención urgente.");
        diagnosticos.put("11111", "Todos los síntomas presentes; condición crítica, atención médica inmediata es imperativa.");
        //  Agregar más combinaciones según sea necesario
    }

    @Condition
    public boolean evaluar() {
        return true; // La regla siempre se ejecuta para evaluar las respuestas
    }

    @Action
    public void asignarDiagnostico(@Fact("resultado") StringBuilder resultado,
                                   @Fact("q1") boolean q1,
                                   @Fact("q2") boolean q2,
                                   @Fact("q3") boolean q3,
                                   @Fact("q4") boolean q4,
                                   @Fact("q5") boolean q5) {

        // ✅ Convertir respuestas en una cadena de 5 bits (Ejemplo: "11010")
        String clave = (q1 ? "1" : "0") + (q2 ? "1" : "0") + (q3 ? "1" : "0") + (q4 ? "1" : "0") + (q5 ? "1" : "0");

        // ✅ Buscar la combinación en el mapa y agregar el diagnóstico correspondiente
        if (diagnosticos.containsKey(clave)) {
            resultado.append("Diagnóstico basado en síntomas: ").append(diagnosticos.get(clave)).append("\n");
        } else {
            resultado.append("No se encontró un diagnóstico exacto. Se recomienda evaluación médica.\n");
        }
    }
}
