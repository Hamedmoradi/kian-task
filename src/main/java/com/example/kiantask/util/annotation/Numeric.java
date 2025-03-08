package com.example.kiantask.util.annotation;

import com.example.kiantask.util.validator.NumericValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NumericValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Numeric {
    String message() default "Value must be numeric";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
