package com.crediya.auth.api.controller;

import com.crediya.auth.api.dto.request.LoginRequest;
import com.crediya.auth.api.dto.response.LoginResponse;
import com.crediya.auth.api.handler.GlobalExceptionHandler;
import com.crediya.auth.api.mapper.AuthDtoMapper;
import com.crediya.auth.model.CredencialesLogin;
import com.crediya.auth.model.TokenAutenticacion;
import com.crediya.auth.model.Usuario;
import com.crediya.auth.model.exception.CredencialesInvalidasException;
import com.crediya.auth.usecase.LoginUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private LoginUseCase loginUseCase;

    @Mock
    private AuthDtoMapper authDtoMapper;

    private AuthController authController;
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        authController = new AuthController(loginUseCase, authDtoMapper);
        webTestClient = WebTestClient.bindToController(authController)
                .controllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void deberiaLoginExitosamente() {
        LoginRequest request = crearLoginRequest();
        CredencialesLogin credenciales = crearCredenciales();
        TokenAutenticacion token = crearTokenAutenticacion();
        LoginResponse response = crearLoginResponse();

        when(authDtoMapper.toDomain(any(LoginRequest.class))).thenReturn(credenciales);
        when(loginUseCase.ejecutar(any(CredencialesLogin.class))).thenReturn(Mono.just(token));
        when(authDtoMapper.toResponse(any(TokenAutenticacion.class))).thenReturn(response);

        webTestClient.post()
                .uri("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(LoginResponse.class);
    }

    @Test
    void deberiaRetornarUnauthorizedCuandoCredencialesInvalidas() {
        LoginRequest request = crearLoginRequest();
        CredencialesLogin credenciales = crearCredenciales();

        when(authDtoMapper.toDomain(any(LoginRequest.class))).thenReturn(credenciales);
        when(loginUseCase.ejecutar(any(CredencialesLogin.class)))
                .thenReturn(Mono.error(new CredencialesInvalidasException("Credenciales inválidas")));

        webTestClient.post()
                .uri("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    private LoginRequest crearLoginRequest() {
        return LoginRequest.builder()
                .email("test@test.com")
                .password("password123")
                .build();
    }

    private CredencialesLogin crearCredenciales() {
        return CredencialesLogin.builder()
                .email("test@test.com")
                .password("password123")
                .build();
    }

    private TokenAutenticacion crearTokenAutenticacion() {
        Usuario usuario = Usuario.builder()
                .idUsuario(1L)
                .nombre("Test")
                .apellido("User")
                .email("test@test.com")
                .idRol(3L)
                .build();

        return TokenAutenticacion.builder()
                .accessToken("jwt-token-123")
                .tokenType("Bearer")
                .fechaExpiracion(LocalDateTime.now().plusHours(24))
                .usuario(usuario)
                .build();
    }

    private LoginResponse crearLoginResponse() {
        LoginResponse.UsuarioAutenticadoResponse usuario = LoginResponse.UsuarioAutenticadoResponse.builder()
                .idUsuario(1L)
                .email("test@test.com")
                .nombre("Test")
                .apellido("User")
                .idRol(3L)
                .build();

        return LoginResponse.builder()
                .accessToken("jwt-token-123")
                .tokenType("Bearer")
                .expiresAt(LocalDateTime.now().plusHours(24))
                .usuario(usuario)
                .build();
    }
}
