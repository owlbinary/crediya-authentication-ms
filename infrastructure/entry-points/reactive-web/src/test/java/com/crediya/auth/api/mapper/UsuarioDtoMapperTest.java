package com.crediya.auth.api.mapper;

import com.crediya.auth.api.dto.request.RegistrarUsuarioRequest;
import com.crediya.auth.api.dto.response.UsuarioResponse;
import com.crediya.auth.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {UsuarioDtoMapperImpl.class})
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class UsuarioDtoMapperTest {

    @Autowired
    private UsuarioDtoMapper usuarioDtoMapper;

    @Test
    void deberiaMappearRequestADomain() {
        RegistrarUsuarioRequest request = RegistrarUsuarioRequest.builder()
                .nombre("Juan")
                .apellido("Perez")
                .email("juan@test.com")
                .password("password123")
                .documentoIdentidad("12345678")
                .telefono("3001234567")
                .direccion("Calle 123")
                .idRol(1L)
                .salarioBase(new BigDecimal("5000000"))
                .build();

        Usuario usuario = usuarioDtoMapper.toDomain(request);

        assertNotNull(usuario);
        assertEquals("Juan", usuario.getNombre());
        assertEquals("Perez", usuario.getApellido());
        assertEquals("juan@test.com", usuario.getEmail());
        assertEquals("password123", usuario.getPassword());
        assertEquals("12345678", usuario.getDocumentoIdentidad());
        assertEquals("3001234567", usuario.getTelefono());
        assertEquals("Calle 123", usuario.getDireccion());
        assertEquals(1L, usuario.getIdRol());
        assertEquals(new BigDecimal("5000000"), usuario.getSalarioBase());
    }

    @Test
    void deberiaMappearDomainAResponse() {
        Usuario usuario = Usuario.builder()
                .idUsuario(1L)
                .nombre("Juan")
                .apellido("Perez")
                .email("juan@test.com")
                .documentoIdentidad("12345678")
                .telefono("3001234567")
                .direccion("Calle 123")
                .idRol(1L)
                .salarioBase(new BigDecimal("5000000"))
                .build();

        UsuarioResponse response = usuarioDtoMapper.toResponse(usuario);

        assertNotNull(response);
        assertEquals(1L, response.getIdUsuario());
        assertEquals("Juan", response.getNombre());
        assertEquals("Perez", response.getApellido());
        assertEquals("juan@test.com", response.getEmail());
        assertEquals("12345678", response.getDocumentoIdentidad());
        assertEquals("3001234567", response.getTelefono());
        assertEquals("Calle 123", response.getDireccion());
        assertEquals(1L, response.getIdRol());
        assertEquals(new BigDecimal("5000000"), response.getSalarioBase());
    }

    @Test
    void deberiaManejiarRequestNulo() {
        Usuario usuario = usuarioDtoMapper.toDomain(null);
        assertNull(usuario);
    }

    @Test
    void deberiaManejiarDomainNulo() {
        UsuarioResponse response = usuarioDtoMapper.toResponse(null);
        assertNull(response);
    }
}
