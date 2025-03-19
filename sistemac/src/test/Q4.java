package com.sistemac;

import org.jeasy.rules.annotation.*;

@Rule(name = "Disnea", description = "Disnea leve; podría ser el inicio de un compromiso cardíaco, requiere evaluación clínica")
public class Q4 {

    @Condition
    public boolean Pregunta4(@Fact("q4") boolean q4 ) {
        return q4;
    }

    @Action
    public void Disnea(@Fact("resultado") StringBuilder resultado) {
       resultado.append("Disnea leve; podría ser el inicio de un compromiso cardíaco, requiere evaluación clínica");
    }


}
