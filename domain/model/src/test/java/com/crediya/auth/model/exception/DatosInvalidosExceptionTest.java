package com.crediya.auth.model.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatosInvalidosExceptionTest {

    @Test
    void deberiaCrearExcepcionConMensaje() {
        String mensaje = "Los datos proporcionados son inválidos";
        DatosInvalidosException exception = new DatosInvalidosException(mensaje);
        
        assertNotNull(exception);
        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    void deberiaCrearExcepcionConMensajeVacio() {
        String mensaje = "";
        DatosInvalidosException exception = new DatosInvalidosException(mensaje);
        
        assertNotNull(exception);
        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    void deberiaHeredarDeRuntimeException() {
        DatosInvalidosException exception = new DatosInvalidosException("test");
        
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void deberiaPermitirMensajeNulo() {
        DatosInvalidosException exception = new DatosInvalidosException(null);
        
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }
}
