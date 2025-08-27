package com.crediya.auth.api.handler;

import com.crediya.auth.api.constants.ErrorCodes;
import com.crediya.auth.api.dto.response.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler - AccessDeniedException Tests")
class GlobalExceptionHandlerAccessDeniedTest {

    @Mock
    private ServerWebExchange exchange;

    @Mock
    private ServerHttpRequest request;

    @Mock
    private RequestPath requestPath;

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
        when(exchange.getRequest()).thenReturn(request);
        when(request.getPath()).thenReturn(requestPath);
        when(requestPath.value()).thenReturn("/api/v1/validaciones/documento/12345678");
    }

    @Test
    @DisplayName("Debería manejar AccessDeniedException y retornar 403 Forbidden")
    void deberiaManejarAccessDeniedExceptionYRetornar403() {
        AccessDeniedException ex = new AccessDeniedException("Access Denied");

        Mono<ResponseEntity<ErrorResponse>> result = globalExceptionHandler.handleAccessDeniedException(ex, exchange);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
                    assertNotNull(response.getBody());
                    
                    ErrorResponse errorResponse = response.getBody();
                    assertNotNull(errorResponse);
                    assertEquals(ErrorCodes.ACCESO_NO_AUTORIZADO, errorResponse.getCodigo());
                    assertEquals(ErrorCodes.ACCESO_NO_AUTORIZADO_MENSAJE, errorResponse.getMensaje());
                    assertEquals("/api/v1/validaciones/documento/12345678", errorResponse.getPath());
                    assertNotNull(errorResponse.getTimestamp());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería usar mensaje estándar independientemente del mensaje de la excepción")
    void deberiaUsarMensajeEstandarIndependientementeDelMensajeExcepcion() {
        AccessDeniedException ex = new AccessDeniedException("Mensaje específico de la excepción");

        Mono<ResponseEntity<ErrorResponse>> result = globalExceptionHandler.handleAccessDeniedException(ex, exchange);

        StepVerifier.create(result)
                .assertNext(response -> {
                    ErrorResponse errorResponse = response.getBody();
                    assertNotNull(errorResponse);
                    assertEquals(ErrorCodes.ACCESO_NO_AUTORIZADO_MENSAJE, errorResponse.getMensaje());
                    assertEquals("No tiene permisos para acceder a este recurso", errorResponse.getMensaje());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería incluir la ruta correcta del endpoint")
    void deberiaIncluirRutaCorrectaDelEndpoint() {
        when(requestPath.value()).thenReturn("/api/v1/usuarios");
        AccessDeniedException ex = new AccessDeniedException("Access Denied");

        Mono<ResponseEntity<ErrorResponse>> result = globalExceptionHandler.handleAccessDeniedException(ex, exchange);

        StepVerifier.create(result)
                .assertNext(response -> {
                    ErrorResponse errorResponse = response.getBody();
                    assertNotNull(errorResponse);
                    assertEquals("/api/v1/usuarios", errorResponse.getPath());
                })
                .verifyComplete();
    }
}
