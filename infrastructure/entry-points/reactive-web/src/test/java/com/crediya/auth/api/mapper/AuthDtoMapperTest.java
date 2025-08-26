package com.crediya.auth.api.mapper;

import com.crediya.auth.api.dto.request.LoginRequest;
import com.crediya.auth.api.dto.response.LoginResponse;
import com.crediya.auth.model.CredencialesLogin;
import com.crediya.auth.model.TokenAutenticacion;
import com.crediya.auth.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {AuthDtoMapperImpl.class})
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class AuthDtoMapperTest {

    @Autowired
    private AuthDtoMapper authDtoMapper;

    @Test
    void deberiaMappearLoginRequestACredencialesDomain() {
        LoginRequest request = LoginRequest.builder()
                .email("test@test.com")
                .password("password123")
                .build();

        CredencialesLogin credenciales = authDtoMapper.toDomain(request);

        assertNotNull(credenciales);
        assertEquals("test@test.com", credenciales.getEmail());
        assertEquals("password123", credenciales.getPassword());
    }

    @Test
    void deberiaMappearTokenAutenticacionALoginResponse() {
        Usuario usuario = Usuario.builder()
                .idUsuario(1L)
                .email("test@test.com")
                .nombre("Test")
                .apellido("User")
                .idRol(1L)
                .build();

        LocalDateTime fechaExpiracion = LocalDateTime.now().plusHours(1);
        
        TokenAutenticacion token = TokenAutenticacion.builder()
                .accessToken("jwt-token-123")
                .tokenType("Bearer")
                .fechaExpiracion(fechaExpiracion)
                .usuario(usuario)
                .build();

        LoginResponse response = authDtoMapper.toResponse(token);

        assertNotNull(response);
        assertEquals("jwt-token-123", response.getAccessToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(fechaExpiracion, response.getExpiresAt());
        assertNotNull(response.getUsuario());
        assertEquals(1L, response.getUsuario().getIdUsuario());
        assertEquals("test@test.com", response.getUsuario().getEmail());
        assertEquals("Test", response.getUsuario().getNombre());
        assertEquals("User", response.getUsuario().getApellido());
        assertEquals(1L, response.getUsuario().getIdRol());
    }

    @Test
    void deberiaManejiarLoginRequestNulo() {
        CredencialesLogin credenciales = authDtoMapper.toDomain(null);
        assertNull(credenciales);
    }

    @Test
    void deberiaManejiarTokenAutenticacionNulo() {
        LoginResponse response = authDtoMapper.toResponse(null);
        assertNull(response);
    }
}
