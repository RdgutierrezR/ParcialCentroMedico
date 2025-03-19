package com.sistemac;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RuleBuilder;

import java.util.HashMap;
import java.util.Map;

public class CardioDiagnosisApp {

    // Mapa que relaciona la combinación (cadena de 5 dígitos) con un diagnóstico
    private static final Map<String, String> DIAGNOSIS_MAP = new HashMap<>();
    static {
        DIAGNOSIS_MAP.put("00000", "Sin síntomas; hallazgo normal según criterios de la Mayo Clinic y la AHA.");
        DIAGNOSIS_MAP.put("00001", "Síntoma aislado de mareos; seguimiento recomendado.");
        DIAGNOSIS_MAP.put("00010", "Disnea leve; posible inicio de compromiso cardíaco, evaluar clínicamente.");
        DIAGNOSIS_MAP.put("00011", "Disnea y mareos; sospecha de compromiso cardiovascular, revisión médica aconsejada.");
        DIAGNOSIS_MAP.put("00100", "Edema aislado en miembros inferiores; evaluar función cardíaca y causas de retención.");
        DIAGNOSIS_MAP.put("00101", "Edema junto a mareos; posible insuficiencia cardíaca incipiente, se requiere evaluación.");
        DIAGNOSIS_MAP.put("00110", "Edema y disnea; signos de insuficiencia cardíaca, evaluación detallada necesaria.");
        DIAGNOSIS_MAP.put("00111", "Edema, disnea y mareos; alto riesgo de insuficiencia cardíaca, atención urgente.");
        DIAGNOSIS_MAP.put("01000", "Dolor torácico aislado; posible angina, evaluar para descartar enfermedad coronaria.");
        DIAGNOSIS_MAP.put("01001", "Dolor torácico y mareos; aumenta la sospecha de angina, se recomienda revisión.");
        DIAGNOSIS_MAP.put("01010", "Dolor torácico y disnea; riesgo de angina inestable, evaluación urgente.");
        DIAGNOSIS_MAP.put("01011", "Dolor, disnea y mareos; alto riesgo de eventos coronarios, atención inmediata.");
        DIAGNOSIS_MAP.put("01100", "Dolor torácico y edema; posible hipertensión o inicio de insuficiencia, evaluar.");
        DIAGNOSIS_MAP.put("01101", "Dolor, edema y mareos; compromiso cardiovascular moderado, revisión médica aconsejada.");
        DIAGNOSIS_MAP.put("01110", "Dolor torácico, edema y disnea; indicativos de compromiso avanzado, evaluación urgente.");
        DIAGNOSIS_MAP.put("01111", "Síntomas múltiples; alta probabilidad de enfermedad cardiovascular severa, atención inmediata.");
        DIAGNOSIS_MAP.put("10000", "Palpitaciones aisladas; posibles arritmias, se recomienda monitoreo.");
        DIAGNOSIS_MAP.put("10001", "Palpitaciones con mareos; mayor sospecha de arritmia, evaluar clínicamente.");
        DIAGNOSIS_MAP.put("10010", "Palpitaciones y disnea; posible arritmia con compromiso funcional, evaluar.");
        DIAGNOSIS_MAP.put("10011", "Palpitaciones, disnea y mareos; arritmia preocupante, atención urgente.");
        DIAGNOSIS_MAP.put("10100", "Palpitaciones y edema; sugiere alteración en la función cardíaca, evaluación detallada.");
        DIAGNOSIS_MAP.put("10101", "Palpitaciones, edema y mareos; compromiso moderado, seguimiento clínico sugerido.");
        DIAGNOSIS_MAP.put("10110", "Palpitaciones, edema y disnea; posible insuficiencia cardíaca asociada, evaluar urgentemente.");
        DIAGNOSIS_MAP.put("10111", "Alto riesgo cardiovascular por combinación de síntomas; atención médica inmediata.");
        DIAGNOSIS_MAP.put("11000", "Palpitaciones con dolor torácico; riesgo de enfermedad coronaria o arritmia, evaluar.");
        DIAGNOSIS_MAP.put("11001", "Palpitaciones, dolor torácico y mareos; elevada sospecha de eventos cardíacos, revisión urgente.");
        DIAGNOSIS_MAP.put("11010", "Palpitaciones, dolor torácico y disnea; alto riesgo de complicaciones, atención médica inmediata.");
        DIAGNOSIS_MAP.put("11011", "Combinación de palpitaciones, dolor, disnea y mareos; alta probabilidad de eventos severos, atención urgente.");
        DIAGNOSIS_MAP.put("11100", "Palpitaciones, dolor torácico y edema; sugiere compromiso en la función cardíaca, evaluar.");
        DIAGNOSIS_MAP.put("11101", "Palpitaciones, dolor, edema y mareos; combinación de síntomas moderada a alta, revisión completa recomendada.");
        DIAGNOSIS_MAP.put("11110", "Palpitaciones, dolor, edema y disnea; hallazgo altamente sugestivo de enfermedad cardiovascular avanzada, atención urgente.");
        DIAGNOSIS_MAP.put("11111", "Todos los síntomas presentes; condición crítica, atención médica inmediata es imperativa.");
    }

    public static void main(String[] args) {
        // Simulación de respuestas: true = "Sí", false = "No"
        // Estas respuestas podrían provenir, por ejemplo, de una interfaz de usuario
        Facts facts = new Facts();
        facts.put("q1", true);   // ¿Palpitaciones?
        facts.put("q2", true);   // ¿Dolor torácico?
        facts.put("q3", true);  // ¿Edema?
        facts.put("q4", true);   // ¿Disnea?
        facts.put("q5", true);  // ¿Mareos/síncope?

        // Regla genérica: calcula la combinación y asigna el diagnóstico correspondiente
        Rule diagnosisRule = new RuleBuilder()
                .name("CardioDiagnosisRule")
                .description("Asigna diagnóstico basado en la combinación de respuestas")
                .when(factsInput -> {
                    String combination = getCombination(factsInput);
                    return DIAGNOSIS_MAP.containsKey(combination);
                })
                .then(factsOutput -> {
                    String combination = getCombination(factsOutput);
                    String diagnosis = DIAGNOSIS_MAP.get(combination);
                    factsOutput.put("diagnosis", diagnosis);
                    System.out.println("Combinación: " + combination);
                    System.out.println("Diagnóstico asignado: " + diagnosis);
                })
                .build();

        // Registra la regla en el motor de reglas
        Rules rules = new Rules();
        rules.register(diagnosisRule);

        // Crea el motor de reglas y ejecuta las reglas
        DefaultRulesEngine rulesEngine = new DefaultRulesEngine();
        rulesEngine.fire(rules, facts);

        // Obtén el diagnóstico desde Facts (si se requiere para otros procesos)
        String diagnosis = facts.get("diagnosis");
        System.out.println("Diagnóstico final: " + diagnosis);
    }

    // Método auxiliar que genera la cadena de combinación a partir de los hechos
    private static String getCombination(Facts facts) {
        boolean q1 = facts.get("q1");
        boolean q2 = facts.get("q2");
        boolean q3 = facts.get("q3");
        boolean q4 = facts.get("q4");
        boolean q5 = facts.get("q5");

        return (q1 ? "1" : "0") +
               (q2 ? "1" : "0") +
               (q3 ? "1" : "0") +
               (q4 ? "1" : "0") +
               (q5 ? "1" : "0");
    }
}
