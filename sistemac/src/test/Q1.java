package com.sistemac;

import org.jeasy.rules.annotation.*;

@Rule(name = "Palpitaciones", description = "Palpitaciones aisladas; pueden ser indicativas de arritmia, se recomienda monitoreo del ritmo cardíaco.")
public class Q1 {

    @Condition
    public boolean Pregunta1(@Fact("q1") boolean q1) {
        return q1;
    }
// Cambio el nombre del fact y lo agrego a datos.put
    @Action
    public void Palpitaciones(@Fact("resultado") StringBuilder resultado) {
        resultado.append("Palpitaciones aisladas; pueden ser indicativas de arritmia, se recomienda monitoreo del ritmo cardíaco.\n");
    }
}
