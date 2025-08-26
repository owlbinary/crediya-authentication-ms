package com.crediya.auth.model.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TokenInvalidoExceptionTest {

    @Test
    void deberiaCrearExcepcionConMensaje() {
        String mensaje = "Token inválido o expirado";
        TokenInvalidoException exception = new TokenInvalidoException(mensaje);

        assertNotNull(exception);
        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    void deberiaSerSubclaseDeRuntimeException() {
        TokenInvalidoException exception = new TokenInvalidoException("mensaje");

        assertNotNull(exception);
        assertEquals(RuntimeException.class, exception.getClass().getSuperclass());
    }
}
