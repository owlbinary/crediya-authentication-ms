package com.crediya.auth.usecase;

import com.crediya.auth.model.CredencialesLogin;
import com.crediya.auth.model.Usuario;
import com.crediya.auth.model.exception.CredencialesInvalidasException;
import com.crediya.auth.model.gateway.JwtTokenGateway;
import com.crediya.auth.model.gateway.PasswordEncryptionGateway;
import com.crediya.auth.model.gateway.UsuarioGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock
    private UsuarioGateway usuarioGateway;
    
    @Mock
    private PasswordEncryptionGateway passwordEncryptionGateway;
    
    @Mock
    private JwtTokenGateway jwtTokenGateway;

    private LoginUseCase loginUseCase;

    @BeforeEach
    void setUp() {
        loginUseCase = new LoginUseCase(usuarioGateway, passwordEncryptionGateway, jwtTokenGateway);
    }

    @Test
    void deberiaAutenticarUsuarioExitosamente() {
        CredencialesLogin credenciales = CredencialesLogin.builder()
                .email("test@test.com")
                .password("password123")
                .build();

        Usuario usuario = crearUsuario();
        String token = "jwt-token-123";

        when(usuarioGateway.buscarPorEmail(credenciales.getEmail()))
                .thenReturn(Mono.just(usuario));
        when(passwordEncryptionGateway.validarPassword(credenciales.getPassword(), usuario.getPassword()))
                .thenReturn(Mono.just(true));
        when(jwtTokenGateway.generarToken(usuario))
                .thenReturn(Mono.just(token));

        StepVerifier.create(loginUseCase.ejecutar(credenciales))
                .expectNextMatches(tokenAuth -> {
                    return tokenAuth.getAccessToken().equals(token) &&
                           tokenAuth.getTokenType().equals("Bearer") &&
                           tokenAuth.getUsuario().equals(usuario);
                })
                .verifyComplete();
    }

    @Test
    void deberiaFallarCuandoUsuarioNoExiste() {
        CredencialesLogin credenciales = CredencialesLogin.builder()
                .email("test@test.com")
                .password("password123")
                .build();

        when(usuarioGateway.buscarPorEmail(credenciales.getEmail()))
                .thenReturn(Mono.empty());

        StepVerifier.create(loginUseCase.ejecutar(credenciales))
                .expectError(CredencialesInvalidasException.class)
                .verify();
    }

    @Test
    void deberiaFallarCuandoPasswordEsIncorrecta() {
        CredencialesLogin credenciales = CredencialesLogin.builder()
                .email("test@test.com")
                .password("test")
                .build();

        Usuario usuario = crearUsuario();

        when(usuarioGateway.buscarPorEmail(credenciales.getEmail()))
                .thenReturn(Mono.just(usuario));
        when(passwordEncryptionGateway.validarPassword(credenciales.getPassword(), usuario.getPassword()))
                .thenReturn(Mono.just(false));

        StepVerifier.create(loginUseCase.ejecutar(credenciales))
                .expectError(CredencialesInvalidasException.class)
                .verify();
    }

    private Usuario crearUsuario() {
        return Usuario.builder()
                .idUsuario(1L)
                .nombre("Test")
                .apellido("User")
                .email("test@test.com")
                .password("$2a$10$contrasena")
                .documentoIdentidad("123456789")
                .telefono("3123456789")
                .idRol(3L)
                .salarioBase(BigDecimal.valueOf(50000))
                .fechaCreacion(LocalDateTime.now())
                .build();
    }
}
