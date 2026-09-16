package br.com.fiap.campusride.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = CapacidadeVeiculoCompativelValidator.class)
public @interface CapacidadeVeiculoCompativel {

    String message() default "Quantidade de vagas incompatível com o tipo de veículo";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
