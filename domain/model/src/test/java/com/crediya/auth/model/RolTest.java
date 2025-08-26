package com.crediya.auth.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RolTest {

    @Test
    void deberiaCrearRolConBuilder() {
        Rol rol = Rol.builder()
                .uniqueId(1L)
                .nombre("ADMIN")
                .descripcion("Administrador del sistema")
                .build();

        assertNotNull(rol);
        assertEquals(1L, rol.getUniqueId());
        assertEquals("ADMIN", rol.getNombre());
        assertEquals("Administrador del sistema", rol.getDescripcion());
    }

    @Test
    void deberiaCrearRolVacio() {
        Rol rol = new Rol();

        assertNotNull(rol);
        assertNull(rol.getUniqueId());
        assertNull(rol.getNombre());
        assertNull(rol.getDescripcion());
    }

    @Test
    void deberiaCrearRolConConstructorCompleto() {
        Rol rol = new Rol(1L, "ADMIN", "Administrador del sistema");

        assertNotNull(rol);
        assertEquals(1L, rol.getUniqueId());
        assertEquals("ADMIN", rol.getNombre());
        assertEquals("Administrador del sistema", rol.getDescripcion());
    }

    @Test
    void deberiaUsarToBuilder() {
        Rol rolOriginal = Rol.builder()
                .uniqueId(1L)
                .nombre("USER")
                .build();

        Rol rolModificado = rolOriginal.toBuilder()
                .descripcion("Usuario básico")
                .build();

        assertNotNull(rolModificado);
        assertEquals(1L, rolModificado.getUniqueId());
        assertEquals("USER", rolModificado.getNombre());
        assertEquals("Usuario básico", rolModificado.getDescripcion());
    }
}
