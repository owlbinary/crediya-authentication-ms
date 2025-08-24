package com.crediya.auth.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DocumentoIdentidadValidator implements ConstraintValidator<ValidDocumentoIdentidad, String> {

    @Override
    public boolean isValid(String documento, ConstraintValidatorContext context) {
        if (documento == null || documento.trim().isEmpty()) {
            return true;
        }

        return documento.matches("^\\d{6,15}$");
    }
}
