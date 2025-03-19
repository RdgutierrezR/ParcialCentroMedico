package com.sistemac;

import org.jeasy.rules.annotation.*;

@Rule(name = "Dolor torácico", description = "Dolor torácico aislado, posible angina; se recomienda evaluación para descartar enfermedad coronaria..")
public class Q2 {

    @Condition
    public boolean Pregunta2(@Fact("q2") boolean q2 ) {
        return q2;
    }

    @Action
    public void DolorToracico(@Fact("resultado") StringBuilder resultado) {
        resultado.append("Dolor torácico aislado, posible angina; se recomienda evaluación para descartar enfermedad coronaria.");
    }


}
