package com.crediya.auth.api.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.ConstraintValidatorContext;

@ExtendWith(MockitoExtension.class)
class DocumentoIdentidadValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    private DocumentoIdentidadValidator validator;

    @BeforeEach
    void setUp() {
        validator = new DocumentoIdentidadValidator();
    }

    @Test
    void deberiaValidarDocumentoNulo() {
        boolean result = validator.isValid(null, context);

        assertTrue(result, "Un documento nulo debería ser válido (se maneja con @NotNull separadamente)");
    }

    @Test
    void deberiaValidarDocumentoVacio() {
        boolean result = validator.isValid("", context);

        assertTrue(result, "Un documento vacío debería ser válido");
    }

    @Test
    void deberiaValidarDocumentoSoloEspacios() {
        boolean result = validator.isValid("   ", context);

        assertTrue(result, "Un documento con solo espacios debería ser válido");
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456", "1234567", "12345678", "123456789", "1234567890", "123456789012345"})
    void deberiaValidarDocumentosValidosConDiferentesLongitudes(String documento) {
        boolean result = validator.isValid(documento, context);

        assertTrue(result, "El documento " + documento + " debería ser válido");
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345", "1234567890123456", "abcdef", "123abc", "12-34-56", "123.456.789", "123 456 789"})
    void deberiaRechazarDocumentosInvalidos(String documento) {
        boolean result = validator.isValid(documento, context);

        assertFalse(result, "El documento " + documento + " debería ser inválido");
    }

    @Test
    void deberiaRechazarDocumentoConLetras() {
        boolean result = validator.isValid("1234567a", context);

        assertFalse(result, "Un documento con letras debería ser inválido");
    }

    @Test
    void deberiaRechazarDocumentoConCaracteresEspeciales() {
        boolean result = validator.isValid("1234567-", context);

        assertFalse(result, "Un documento con caracteres especiales debería ser inválido");
    }

    @Test
    void deberiaRechazarDocumentoMuyCorto() {
        boolean result = validator.isValid("12345", context);

        assertFalse(result, "Un documento de 5 dígitos debería ser inválido (mínimo 6)");
    }

    @Test
    void deberiaRechazarDocumentoMuyLargo() {
        boolean result = validator.isValid("1234567890123456", context);

        assertFalse(result, "Un documento de 16 dígitos debería ser inválido (máximo 15)");
    }

    @Test
    void deberiaValidarDocumentoEnLimitesExactos() {
        boolean resultMinimo = validator.isValid("123456", context);
        boolean resultMaximo = validator.isValid("123456789012345", context);

        assertTrue(resultMinimo, "Un documento de 6 dígitos debería ser válido");
        assertTrue(resultMaximo, "Un documento de 15 dígitos debería ser válido");
    }

    @Test
    void deberiaValidarDocumentoConEspaciosAlInicio() {
        boolean result = validator.isValid(" 1234567", context);

        assertFalse(result, "Un documento con espacios al inicio no debería ser válido");
    }

    @Test
    void deberiaValidarDocumentoConEspaciosAlFinal() {
        boolean result = validator.isValid("1234567 ", context);

        assertFalse(result, "Un documento con espacios al final no debería ser válido");
    }

    @Test
    void deberiaValidarDocumentoCerosSeguidos() {
        boolean result = validator.isValid("000000", context);

        assertTrue(result, "Un documento de solo ceros debería ser válido como formato");
    }
}
