package com.crediya.auth.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioAutenticadoTest {

    @Test
    void deberiaCrearUsuarioAutenticadoConBuilder() {
        List<String> permisos = Arrays.asList("READ", "WRITE");
        
        UsuarioAutenticado usuario = UsuarioAutenticado.builder()
                .idUsuario(1L)
                .email("test@test.com")
                .nombre("Test")
                .apellido("User")
                .idRol(1L)
                .permisos(permisos)
                .build();

        assertNotNull(usuario);
        assertEquals(1L, usuario.getIdUsuario());
        assertEquals("test@test.com", usuario.getEmail());
        assertEquals("Test", usuario.getNombre());
        assertEquals("User", usuario.getApellido());
        assertEquals(1L, usuario.getIdRol());
        assertEquals(permisos, usuario.getPermisos());
    }

    @Test
    void deberiaCrearUsuarioAutenticadoVacio() {
        UsuarioAutenticado usuario = new UsuarioAutenticado();

        assertNotNull(usuario);
        assertNull(usuario.getIdUsuario());
        assertNull(usuario.getEmail());
        assertNull(usuario.getNombre());
        assertNull(usuario.getApellido());
        assertNull(usuario.getIdRol());
        assertNull(usuario.getPermisos());
    }

    @Test
    void deberiaCrearUsuarioAutenticadoConConstructorCompleto() {
        List<String> permisos = Arrays.asList("READ");
        
        UsuarioAutenticado usuario = new UsuarioAutenticado(1L, "test@test.com", "Test", "User", 1L, permisos);

        assertNotNull(usuario);
        assertEquals(1L, usuario.getIdUsuario());
        assertEquals("test@test.com", usuario.getEmail());
        assertEquals("Test", usuario.getNombre());
        assertEquals("User", usuario.getApellido());
        assertEquals(1L, usuario.getIdRol());
        assertEquals(permisos, usuario.getPermisos());
    }
}
