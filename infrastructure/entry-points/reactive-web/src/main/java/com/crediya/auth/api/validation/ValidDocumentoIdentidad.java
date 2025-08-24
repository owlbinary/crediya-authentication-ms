package com.crediya.auth.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DocumentoIdentidadValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDocumentoIdentidad {
    String message() default "El documento de identidad debe tener entre 6 y 15 caracteres y solo puede contener números";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
