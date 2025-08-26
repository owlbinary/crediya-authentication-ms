package com.crediya.auth.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CredencialesLoginTest {

    @Test
    void deberiaCrearCredencialesLoginConBuilder() {
        CredencialesLogin credenciales = CredencialesLogin.builder()
                .email("test@test.com")
                .password("password123")
                .build();

        assertNotNull(credenciales);
        assertEquals("test@test.com", credenciales.getEmail());
        assertEquals("password123", credenciales.getPassword());
    }

    @Test
    void deberiaCrearCredencialesLoginVacias() {
        CredencialesLogin credenciales = new CredencialesLogin();

        assertNotNull(credenciales);
        assertNull(credenciales.getEmail());
        assertNull(credenciales.getPassword());
    }

    @Test
    void deberiaCrearCredencialesLoginConConstructorCompleto() {
        CredencialesLogin credenciales = new CredencialesLogin("test@test.com", "password123");

        assertNotNull(credenciales);
        assertEquals("test@test.com", credenciales.getEmail());
        assertEquals("password123", credenciales.getPassword());
    }
}
