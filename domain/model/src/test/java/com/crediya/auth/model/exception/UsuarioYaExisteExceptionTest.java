package com.crediya.auth.model.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioYaExisteExceptionTest {

    @Test
    void deberiaCrearExcepcionConEmail() {
        String email = "test@example.com";
        UsuarioYaExisteException exception = new UsuarioYaExisteException(email);
        
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains(email));
        assertTrue(exception.getMessage().contains("ya se encuentra registrado"));
    }

    @Test
    void deberiaCrearExcepcionConEmailComplejo() {
        String email = "usuario.complejo+test@empresa.com.co";
        UsuarioYaExisteException exception = new UsuarioYaExisteException(email);
        
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains(email));
        assertTrue(exception.getMessage().contains("ya se encuentra registrado"));
    }

    @Test
    void deberiaHeredarDeRuntimeException() {
        UsuarioYaExisteException exception = new UsuarioYaExisteException("test@test.com");
        
        assertTrue(exception instanceof RuntimeException);
    }
}
