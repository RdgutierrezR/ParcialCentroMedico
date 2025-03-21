package com.sistemac;

import org.jeasy.rules.annotation.*;
import java.util.HashMap;
import java.util.Map;

@Rule(name = "Diagnóstico Combinado", description = "Determina el diagnóstico según combinaciones de síntomas.")
public class Combinaciones {
    
    private static final Map<String, String> diagnosticos = new HashMap<>();

   static {
    // ✅ Definir las 32 combinaciones de respuestas con descripciones más detalladas
    diagnosticos.put("00000", 
        "Sin síntomas reportados. No se evidencian signos de afección cardiovascular. "
        + "El hallazgo es normal según los criterios de la Mayo Clinic y la AHA. "
        + "Se recomienda continuar con chequeos médicos regulares y mantener un estilo de vida saludable.");

    diagnosticos.put("00001", 
        "Presencia de mareos ocasionales sin otros síntomas asociados. "
        + "Esto puede deberse a múltiples causas, incluyendo hipotensión transitoria, deshidratación o estrés. "
        + "Si los episodios se repiten o se intensifican, se recomienda evaluación médica para descartar trastornos neurológicos o cardíacos.");

    diagnosticos.put("00010", 
        "Disnea leve detectada. Esto puede ser un signo temprano de compromiso cardíaco o una respuesta temporal "
        + "a factores ambientales, ansiedad o problemas pulmonares leves. "
        + "Es aconsejable realizar pruebas de función pulmonar y evaluación cardiológica para descartar insuficiencia cardíaca en etapa inicial.");

    diagnosticos.put("00011", 
        "Disnea y mareos presentes simultáneamente. Esta combinación puede indicar una disminución del flujo sanguíneo cerebral, "
        + "posiblemente relacionada con arritmias cardíacas, hipotensión o insuficiencia cardíaca leve. "
        + "Se sugiere un electrocardiograma y prueba de esfuerzo para determinar la causa subyacente.");

    diagnosticos.put("00100", 
        "Edema en miembros inferiores sin otros síntomas evidentes. Esto puede ser causado por retención de líquidos, problemas venosos o disfunción renal. "
        + "Si el edema es persistente o aumenta con el tiempo, se recomienda realizar estudios de función renal, cardíaca y venosa para descartar insuficiencia cardíaca o trombosis venosa profunda.");

    diagnosticos.put("00101", 
        "Edema en miembros inferiores junto con mareos. Esta combinación sugiere una posible alteración en la presión arterial o insuficiencia venosa. "
        + "Si se acompaña de cansancio extremo o dificultad para respirar, es importante evaluar el funcionamiento cardíaco y la perfusión cerebral.");

    diagnosticos.put("00110", 
        "Edema y disnea detectados. La coexistencia de estos síntomas puede indicar insuficiencia cardíaca en desarrollo, "
        + "especialmente si el edema aparece al final del día y la disnea se agrava al acostarse. "
        + "Se recomienda ecocardiograma y pruebas de función pulmonar para descartar enfermedades estructurales.");

    diagnosticos.put("00111", 
        "Edema, disnea y mareos presentes. Esta combinación sugiere un compromiso cardiovascular significativo, "
        + "posiblemente una insuficiencia cardíaca congestiva con alteración en la perfusión cerebral. "
        + "Se aconseja una consulta inmediata con un especialista en cardiología y la realización de pruebas complementarias.");

    diagnosticos.put("01000", 
        "Dolor torácico sin otros síntomas asociados. Puede estar relacionado con angina de pecho leve, espasmos esofágicos o ansiedad. "
        + "Si el dolor es recurrente o se agrava con el esfuerzo, se recomienda una evaluación cardiológica con pruebas de esfuerzo y electrocardiograma.");

    diagnosticos.put("01001", 
        "Dolor torácico acompañado de mareos. Esta combinación aumenta la sospecha de insuficiencia circulatoria coronaria o arritmias. "
        + "Si los episodios ocurren en reposo o duran más de unos minutos, se recomienda acudir a urgencias para evaluación inmediata.");

    diagnosticos.put("01010", 
        "Dolor torácico y disnea presentes. Esto puede sugerir una angina inestable o un problema pulmonar como embolismo pulmonar. "
        + "Dado que esta combinación puede indicar una situación de riesgo, es fundamental realizar una evaluación médica urgente.");

    diagnosticos.put("01011", 
        "Dolor torácico, disnea y mareos detectados. Esto es indicativo de una posible obstrucción coronaria significativa, "
        + "fibrilación auricular con compromiso hemodinámico o una crisis hipertensiva. "
        + "Se recomienda una evaluación cardiológica inmediata y monitoreo hospitalario si es necesario.");

    diagnosticos.put("01100", 
        "Dolor torácico y edema en miembros inferiores. Esta combinación puede sugerir hipertensión descontrolada o insuficiencia cardíaca incipiente. "
        + "Se recomienda control de presión arterial y evaluación de función cardíaca.");

    diagnosticos.put("01101", 
        "Dolor torácico, edema y mareos presentes. Estos síntomas combinados pueden indicar un problema vascular significativo, "
        + "como insuficiencia cardíaca con retención de líquidos o alteraciones del flujo cerebral. "
        + "Se recomienda valoración urgente.");

    diagnosticos.put("01110", 
        "Dolor torácico, edema y disnea. Puede estar relacionado con insuficiencia cardíaca avanzada o una crisis hipertensiva. "
        + "Es recomendable acudir a un especialista para un diagnóstico más preciso.");

    diagnosticos.put("01111", 
        "Dolor torácico, edema, disnea y mareos detectados. Esto representa un alto riesgo cardiovascular, "
        + "posiblemente una insuficiencia cardíaca aguda o un síndrome coronario agudo. "
        + "Se requiere atención médica inmediata.");

    diagnosticos.put("10000", 
        "Palpitaciones aisladas sin otros síntomas. Puede tratarse de extrasístoles benignas, ansiedad o consumo de estimulantes. "
        + "Si son persistentes, se recomienda un Holter de 24 horas para evaluar la frecuencia y origen.");

    diagnosticos.put("10001", 
        "Palpitaciones con mareos. Esto aumenta la sospecha de arritmia cardíaca significativa, "
        + "especialmente si los mareos se presentan con episodios prolongados de palpitaciones. "
        + "Es recomendable un monitoreo cardiológico.");

    diagnosticos.put("10010", 
        "Palpitaciones y disnea. Esto puede indicar fibrilación auricular o insuficiencia cardíaca en etapas iniciales. "
        + "Es aconsejable realizar pruebas electrofisiológicas para evaluar el ritmo cardíaco.");

    diagnosticos.put("10011", 
        "Palpitaciones, disnea y mareos presentes. Alta sospecha de arritmia con compromiso hemodinámico, "
        + "posible taquicardia supraventricular o fibrilación auricular rápida. "
        + "Se recomienda consulta urgente con cardiólogo.");

    diagnosticos.put("10100", 
        "Palpitaciones y edema. Puede sugerir insuficiencia cardíaca con retención de líquidos. "
        + "Si hay aumento de peso repentino, es necesario descartar disfunción cardíaca avanzada.");

    diagnosticos.put("11111", 
        "Todos los síntomas presentes: dolor torácico, disnea, mareos, palpitaciones y edema. "
        + "Esto indica una condición crítica, posiblemente una insuficiencia cardíaca descompensada o un evento coronario severo. "
        + "Es imprescindible acudir a urgencias de inmediato para una evaluación completa y manejo urgente.");

    // Agregar más combinaciones según sea necesario
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
