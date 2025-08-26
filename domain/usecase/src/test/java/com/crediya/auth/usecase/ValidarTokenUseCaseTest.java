package com.crediya.auth.usecase;

import com.crediya.auth.model.UsuarioAutenticado;
import com.crediya.auth.model.exception.TokenInvalidoException;
import com.crediya.auth.model.gateway.JwtTokenGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidarTokenUseCaseTest {

    @Mock
    private JwtTokenGateway jwtTokenGateway;

    private ValidarTokenUseCase validarTokenUseCase;

    @BeforeEach
    void setUp() {
        validarTokenUseCase = new ValidarTokenUseCase(jwtTokenGateway);
    }

    @Test
    void deberiaValidarTokenCorrectamente() {
        String token = "valid-token";
        UsuarioAutenticado usuarioEsperado = UsuarioAutenticado.builder()
                .idUsuario(1L)
                .email("test@test.com")
                .idRol(1L)
                .build();

        when(jwtTokenGateway.validarToken(token))
                .thenReturn(Mono.just(usuarioEsperado));

        StepVerifier.create(validarTokenUseCase.ejecutar(token))
                .expectNext(usuarioEsperado)
                .verifyComplete();
    }

    @Test
    void deberiaLanzarExcepcionCuandoTokenEsInvalido() {
        String token = "invalid-token";

        when(jwtTokenGateway.validarToken(token))
                .thenReturn(Mono.empty());

        StepVerifier.create(validarTokenUseCase.ejecutar(token))
                .expectError(TokenInvalidoException.class)
                .verify();
    }

    @Test
    void deberiaLanzarExcepcionCuandoGatewayFalla() {
        String token = "token";
        RuntimeException error = new RuntimeException("Gateway error");

        when(jwtTokenGateway.validarToken(token))
                .thenReturn(Mono.error(error));

        StepVerifier.create(validarTokenUseCase.ejecutar(token))
                .expectError(RuntimeException.class)
                .verify();
    }
}
