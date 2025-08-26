package com.crediya.auth.model;

import com.crediya.auth.model.exception.DatosInvalidosException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void deberiaCrearUsuarioConBuilder() {
        LocalDateTime ahora = LocalDateTime.now();
        BigDecimal salario = new BigDecimal("5000000");
        
        Usuario usuario = Usuario.builder()
                .idUsuario(1L)
                .nombre("Juan")
                .apellido("Perez")
                .email("juan@test.com")
                .password("password123")
                .documentoIdentidad("12345678")
                .telefono("3001234567")
                .direccion("Calle 123")
                .idRol(1L)
                .salarioBase(salario)
                .fechaCreacion(ahora)
                .fechaActualizacion(ahora)
                .build();

        assertNotNull(usuario);
        assertEquals(1L, usuario.getIdUsuario());
        assertEquals("Juan", usuario.getNombre());
        assertEquals("Perez", usuario.getApellido());
        assertEquals("juan@test.com", usuario.getEmail());
        assertEquals("password123", usuario.getPassword());
        assertEquals("12345678", usuario.getDocumentoIdentidad());
        assertEquals("3001234567", usuario.getTelefono());
        assertEquals("Calle 123", usuario.getDireccion());
        assertEquals(1L, usuario.getIdRol());
        assertEquals(salario, usuario.getSalarioBase());
        assertEquals(ahora, usuario.getFechaCreacion());
        assertEquals(ahora, usuario.getFechaActualizacion());
    }

    @Test
    void deberiaCrearUsuarioVacio() {
        Usuario usuario = new Usuario();

        assertNotNull(usuario);
        assertNull(usuario.getIdUsuario());
        assertNull(usuario.getNombre());
        assertNull(usuario.getApellido());
        assertNull(usuario.getEmail());
        assertNull(usuario.getPassword());
        assertNull(usuario.getDocumentoIdentidad());
        assertNull(usuario.getTelefono());
        assertNull(usuario.getDireccion());
        assertNull(usuario.getIdRol());
        assertNull(usuario.getSalarioBase());
        assertNull(usuario.getFechaCreacion());
        assertNull(usuario.getFechaActualizacion());
    }

    @Test
    void deberiaUsarToBuilder() {
        Usuario usuarioOriginal = Usuario.builder()
                .idUsuario(1L)
                .nombre("Juan")
                .email("juan@test.com")
                .build();

        Usuario usuarioModificado = usuarioOriginal.toBuilder()
                .apellido("Perez")
                .telefono("3001234567")
                .build();

        assertNotNull(usuarioModificado);
        assertEquals(1L, usuarioModificado.getIdUsuario());
        assertEquals("Juan", usuarioModificado.getNombre());
        assertEquals("Perez", usuarioModificado.getApellido());
        assertEquals("juan@test.com", usuarioModificado.getEmail());
        assertEquals("3001234567", usuarioModificado.getTelefono());
    }

    @Test
    void deberiaValidarSalarioBaseCorrectamente() {
        Usuario usuario = crearUsuarioValido();
        
        assertDoesNotThrow(usuario::validarSalarioBase);
    }

    @Test
    void deberiaFallarCuandoSalarioEsNulo() {
        Usuario usuario = crearUsuarioValido().toBuilder()
                .salarioBase(null)
                .build();

        DatosInvalidosException exception = assertThrows(DatosInvalidosException.class, 
                usuario::validarSalarioBase);
        assertTrue(exception.getMessage().contains("salario_base"));
    }

    @Test
    void deberiaFallarCuandoSalarioSuperaMaximo() {
        Usuario usuario = crearUsuarioValido().toBuilder()
                .salarioBase(new BigDecimal("15000001"))
                .build();

        DatosInvalidosException exception = assertThrows(DatosInvalidosException.class, 
                usuario::validarSalarioBase);
        assertTrue(exception.getMessage().contains("15,000,000"));
    }

    @Test
    void deberiaFallarCuandoSalarioEsCero() {
        Usuario usuario = crearUsuarioValido().toBuilder()
                .salarioBase(BigDecimal.ZERO)
                .build();

        assertThrows(DatosInvalidosException.class, usuario::validarSalarioBase);
    }

    @Test
    void deberiaTenerSalarioValido() {
        Usuario usuario = crearUsuarioValido();
        
        assertTrue(usuario.tieneSalarioValido());
    }

    @Test
    void deberiaValidarSalarioEnElLimite() {
        Usuario usuario = crearUsuarioValido().toBuilder()
                .salarioBase(new BigDecimal("15000000"))
                .build();
        
        assertDoesNotThrow(usuario::validarSalarioBase);
        assertTrue(usuario.tieneSalarioValido());
    }

    @Test
    void deberiaValidarSalarioMinimo() {
        Usuario usuario = crearUsuarioValido().toBuilder()
                .salarioBase(new BigDecimal("0.01"))
                .build();
        
        assertDoesNotThrow(usuario::validarSalarioBase);
        assertTrue(usuario.tieneSalarioValido());
    }

    @ParameterizedTest
    @ValueSource(strings = {"0.01", "5000000", "15000000", "7500000.50", "1234567.89"})
    void tieneSalarioValido_deberiaRetornarTrueParaSalariosValidos(String salario) {
        Usuario usuario = crearUsuarioValido().toBuilder()
                .salarioBase(new BigDecimal(salario))
                .build();
        
        assertTrue(usuario.tieneSalarioValido());
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1000", "15000001", "0.001"})
    void tieneSalarioValido_deberiaRetornarFalseParaSalariosInvalidos(String salario) {
        Usuario usuario = crearUsuarioValido().toBuilder()
                .salarioBase(new BigDecimal(salario))
                .build();
        
        assertFalse(usuario.tieneSalarioValido());
    }

    @Test
    void tieneSalarioValido_deberiaRetornarFalseParaSalarioNulo() {
        Usuario usuario = crearUsuarioValido().toBuilder()
                .salarioBase(null)
                .build();
        
        assertFalse(usuario.tieneSalarioValido());
    }

    private Usuario crearUsuarioValido() {
        return Usuario.builder()
                .nombre("Juan")
                .apellido("Sierra")
                .email("juan@test.com")
                .password("password123")
                .documentoIdentidad("12345678")
                .telefono("3001234567")
                .direccion("Calle 123")
                .idRol(1L)
                .salarioBase(new BigDecimal("2500000"))
                .build();
    }
}
