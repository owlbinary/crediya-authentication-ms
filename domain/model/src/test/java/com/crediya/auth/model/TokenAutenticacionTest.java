package com.crediya.auth.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TokenAutenticacionTest {

    @Test
    void deberiaCrearTokenAutenticacionConBuilder() {
        LocalDateTime fechaExpiracion = LocalDateTime.now().plusHours(1);
        Usuario usuario = Usuario.builder()
                .idUsuario(1L)
                .email("test@test.com")
                .build();
        
        TokenAutenticacion token = TokenAutenticacion.builder()
                .accessToken("jwt-token-123")
                .tokenType("Bearer")
                .fechaExpiracion(fechaExpiracion)
                .usuario(usuario)
                .build();

        assertNotNull(token);
        assertEquals("jwt-token-123", token.getAccessToken());
        assertEquals("Bearer", token.getTokenType());
        assertEquals(fechaExpiracion, token.getFechaExpiracion());
        assertEquals(usuario, token.getUsuario());
    }

    @Test
    void deberiaCrearTokenAutenticacionVacio() {
        TokenAutenticacion token = new TokenAutenticacion();

        assertNotNull(token);
        assertNull(token.getAccessToken());
        assertNull(token.getTokenType());
        assertNull(token.getFechaExpiracion());
        assertNull(token.getUsuario());
    }

    @Test
    void deberiaCrearTokenAutenticacionConConstructorCompleto() {
        LocalDateTime fechaExpiracion = LocalDateTime.now().plusHours(1);
        Usuario usuario = Usuario.builder()
                .idUsuario(1L)
                .email("test@test.com")
                .build();
        
        TokenAutenticacion token = new TokenAutenticacion("jwt-token-123", "Bearer", fechaExpiracion, usuario);

        assertNotNull(token);
        assertEquals("jwt-token-123", token.getAccessToken());
        assertEquals("Bearer", token.getTokenType());
        assertEquals(fechaExpiracion, token.getFechaExpiracion());
        assertEquals(usuario, token.getUsuario());
    }
}
