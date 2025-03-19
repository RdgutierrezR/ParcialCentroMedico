package com.sistemac;

import org.jeasy.rules.annotation.*;

@Rule(name = "Mareos/síncope", description = "Síntoma aislado de mareos; episodios puntuales pueden no ser críticos, pero se sugiere seguimiento.")
public class Q5 {

    @Condition
    public boolean Pregunta5(@Fact("q5") boolean q5 ) {
        return q5;
    }

    @Action
    public void Mareos(@Fact("resultado") StringBuilder resultado) {
        resultado.append("Síntoma aislado de mareos; episodios puntuales pueden no ser críticos, pero se sugiere seguimiento.");
    }


}
