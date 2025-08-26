package com.crediya.auth.model.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocumentoYaExisteExceptionTest {

    @Test
    void deberiaCrearExcepcionConDocumento() {
        String documento = "1234567890";
        DocumentoYaExisteException exception = new DocumentoYaExisteException(documento);
        
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains(documento));
        assertTrue(exception.getMessage().contains("ya se encuentra registrado"));
    }

    @Test
    void deberiaCrearExcepcionConDocumentoComplejo() {
        String documento = "987654321012345";
        DocumentoYaExisteException exception = new DocumentoYaExisteException(documento);
        
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains(documento));
        assertTrue(exception.getMessage().contains("ya se encuentra registrado"));
    }

    @Test
    void deberiaHeredarDeRuntimeException() {
        DocumentoYaExisteException exception = new DocumentoYaExisteException("123456789");
        
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void deberiaFormatearMensajeCorrectamente() {
        String documento = "555666777";
        DocumentoYaExisteException exception = new DocumentoYaExisteException(documento);
        
        String expectedMessage = "El usuario con documento de identidad " + documento + " ya se encuentra registrado";
        assertEquals(expectedMessage, exception.getMessage());
    }
}
