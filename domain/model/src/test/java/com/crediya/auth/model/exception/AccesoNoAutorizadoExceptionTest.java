package com.crediya.auth.model.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AccesoNoAutorizadoExceptionTest {

    @Test
    void deberiaCrearExcepcionConMensaje() {
        String mensaje = "Acceso no autorizado";
        AccesoNoAutorizadoException exception = new AccesoNoAutorizadoException(mensaje);

        assertNotNull(exception);
        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    void deberiaSerSubclaseDeRuntimeException() {
        AccesoNoAutorizadoException exception = new AccesoNoAutorizadoException("mensaje");

        assertNotNull(exception);
        assertEquals(RuntimeException.class, exception.getClass().getSuperclass());
    }
}
