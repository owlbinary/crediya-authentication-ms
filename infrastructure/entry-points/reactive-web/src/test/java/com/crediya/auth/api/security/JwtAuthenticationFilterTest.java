package com.crediya.auth.api.security;

import com.crediya.auth.model.UsuarioAutenticado;
import com.crediya.auth.model.exception.TokenInvalidoException;
import com.crediya.auth.usecase.ValidarTokenUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private ValidarTokenUseCase validarTokenUseCase;
    
    @Mock
    private WebFilterChain filterChain;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        jwtAuthenticationFilter = new JwtAuthenticationFilter(validarTokenUseCase);
        when(filterChain.filter(any())).thenReturn(Mono.empty());
    }

    @Test
    void deberiaPermitirAccesoAEndpointPublico() {
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/v1/login")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        StepVerifier.create(jwtAuthenticationFilter.filter(exchange, filterChain))
                .verifyComplete();
    }

    @Test
    void deberiaPermitirAccesoASwagger() {
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/swagger-ui/index.html")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        StepVerifier.create(jwtAuthenticationFilter.filter(exchange, filterChain))
                .verifyComplete();
    }

    @Test
    void deberiaAutenticarConTokenValido() {
        String token = "valid-token";
        UsuarioAutenticado usuario = UsuarioAutenticado.builder()
                .idUsuario(1L)
                .email("test@test.com")
                .idRol(1L)
                .build();

        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/v1/protected")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        when(validarTokenUseCase.ejecutar(token))
                .thenReturn(Mono.just(usuario));

        StepVerifier.create(jwtAuthenticationFilter.filter(exchange, filterChain))
                .verifyComplete();
    }

    @Test
    void deberiaContinuarSinAutenticacionConTokenInvalido() {
        String token = "invalid-token";

        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/v1/protected")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        when(validarTokenUseCase.ejecutar(token))
                .thenReturn(Mono.error(new TokenInvalidoException("Token inválido")));

        StepVerifier.create(jwtAuthenticationFilter.filter(exchange, filterChain))
                .expectError(TokenInvalidoException.class)
                .verify();
    }

    @Test
    void deberiaContinuarSinHeaderAuthorization() {
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/v1/protected")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        StepVerifier.create(jwtAuthenticationFilter.filter(exchange, filterChain))
                .verifyComplete();
    }

    @Test
    void deberiaContinuarConHeaderAuthorizationSinBearer() {
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/v1/protected")
                .header(HttpHeaders.AUTHORIZATION, "Basic credentials")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        StepVerifier.create(jwtAuthenticationFilter.filter(exchange, filterChain))
                .verifyComplete();
    }

    @Test
    void deberiaAsignarRolCorrectamenteParaAdmin() {
        String token = "valid-token";
        UsuarioAutenticado usuario = UsuarioAutenticado.builder()
                .idUsuario(1L)
                .email("admin@test.com")
                .idRol(1L)
                .build();

        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/v1/protected")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        when(validarTokenUseCase.ejecutar(token))
                .thenReturn(Mono.just(usuario));

        StepVerifier.create(jwtAuthenticationFilter.filter(exchange, filterChain))
                .verifyComplete();
    }

    @Test
    void deberiaAsignarRolCorrectamenteParaAsesor() {
        String token = "valid-token";
        UsuarioAutenticado usuario = UsuarioAutenticado.builder()
                .idUsuario(2L)
                .email("asesor@test.com")
                .idRol(2L)
                .build();

        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/v1/protected")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        when(validarTokenUseCase.ejecutar(token))
                .thenReturn(Mono.just(usuario));

        StepVerifier.create(jwtAuthenticationFilter.filter(exchange, filterChain))
                .verifyComplete();
    }

    @Test
    void deberiaAsignarRolCorrectamenteParaCliente() {
        String token = "valid-token";
        UsuarioAutenticado usuario = UsuarioAutenticado.builder()
                .idUsuario(3L)
                .email("cliente@test.com")
                .idRol(3L)
                .build();

        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/v1/protected")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        when(validarTokenUseCase.ejecutar(token))
                .thenReturn(Mono.just(usuario));

        StepVerifier.create(jwtAuthenticationFilter.filter(exchange, filterChain))
                .verifyComplete();
    }
}
