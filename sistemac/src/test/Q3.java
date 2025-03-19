package com.sistemac;

import org.jeasy.rules.annotation.*;

@Rule(name = "Edema", description = "Edema aislado en miembros inferiores; evaluar función cardíaca y causas de retención de líquidos..")
public class Q3 {

    @Condition
    public boolean Pregunta3(@Fact("q3") boolean q3 ) {
        return q3;
    }

    @Action
    public void Edema(@Fact("resultado") StringBuilder resultado) {
        resultado.append("Edema aislado en miembros inferiores; evaluar función cardíaca y causas de retención de líquidos.");
    }


}
