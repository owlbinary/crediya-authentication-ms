package com.crediya.auth.jwt.adapter;

import com.crediya.auth.model.Usuario;
import com.crediya.auth.model.exception.TokenInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.time.Duration;

class JwtTokenAdapterTest {

    private JwtTokenAdapter jwtTokenAdapter;
    private final String secretKey = "test-secret-key-for-jwt-token-generation-minimum-256-bits";
    private final long jwtExpirationMs = 86400000L;

    @BeforeEach
    void setUp() {
        jwtTokenAdapter = new JwtTokenAdapter(secretKey, jwtExpirationMs);
    }

    @Test
    void deberiaGenerarTokenCorrectamente() {
        Usuario usuario = Usuario.builder()
                .idUsuario(1L)
                .email("test@test.com")
                .nombre("Test")
                .apellido("User")
                .idRol(1L)
                .build();

        StepVerifier.create(jwtTokenAdapter.generarToken(usuario))
                .expectNextMatches(token -> token != null && !token.isEmpty())
                .verifyComplete();
    }

    @Test
    void deberiaValidarTokenCorrectamente() {
        Usuario usuario = Usuario.builder()
                .idUsuario(1L)
                .email("test@test.com")
                .nombre("Test")
                .apellido("User")
                .idRol(1L)
                .build();

        StepVerifier.create(jwtTokenAdapter.generarToken(usuario)
                .flatMap(token -> jwtTokenAdapter.validarToken(token)))
                .expectNextMatches(usuarioAutenticado -> 
                        usuarioAutenticado.getIdUsuario().equals(1L) &&
                        usuarioAutenticado.getEmail().equals("test@test.com") &&
                        usuarioAutenticado.getNombre().equals("Test") &&
                        usuarioAutenticado.getApellido().equals("User") &&
                        usuarioAutenticado.getIdRol().equals(1L))
                .verifyComplete();
    }

    @Test
    void deberiaExtraerEmailDelTokenCorrectamente() {
        Usuario usuario = Usuario.builder()
                .idUsuario(1L)
                .email("test@test.com")
                .nombre("Test")
                .apellido("User")
                .idRol(1L)
                .build();

        StepVerifier.create(jwtTokenAdapter.generarToken(usuario)
                .flatMap(token -> jwtTokenAdapter.extraerEmailDelToken(token)))
                .expectNext("test@test.com")
                .verifyComplete();
    }

    @Test
    void deberiaLanzarExcepcionParaTokenInvalido() {
        String tokenInvalido = "token-invalido";

        StepVerifier.create(jwtTokenAdapter.validarToken(tokenInvalido))
                .expectError(TokenInvalidoException.class)
                .verify();
    }

    @Test
    void deberiaLanzarExcepcionParaTokenVacio() {
        String tokenVacio = "";

        StepVerifier.create(jwtTokenAdapter.validarToken(tokenVacio))
                .expectError(TokenInvalidoException.class)
                .verify();
    }

    @Test
    void deberiaLanzarExcepcionParaTokenNulo() {
        StepVerifier.create(jwtTokenAdapter.validarToken(null))
                .expectError(TokenInvalidoException.class)
                .verify();
    }

    @Test
    void deberiaLanzarExcepcionAlExtraerEmailDeTokenInvalido() {
        String tokenInvalido = "token-invalido";

        StepVerifier.create(jwtTokenAdapter.extraerEmailDelToken(tokenInvalido))
                .expectError(TokenInvalidoException.class)
                .verify();
    }

    @Test
    void deberiaGenerarTokenConTiempoExpiracionCorto() {
        JwtTokenAdapter adapterConExpiracionCorta = new JwtTokenAdapter(secretKey, 1000L);
        
        Usuario usuario = Usuario.builder()
                .idUsuario(1L)
                .email("test@test.com")
                .nombre("Test")
                .apellido("User")
                .idRol(1L)
                .build();

        StepVerifier.create(adapterConExpiracionCorta.generarToken(usuario)
                .delayElement(Duration.ofSeconds(2))
                .flatMap(adapterConExpiracionCorta::validarToken))
                .expectError(TokenInvalidoException.class)
                .verify();
    }
}
