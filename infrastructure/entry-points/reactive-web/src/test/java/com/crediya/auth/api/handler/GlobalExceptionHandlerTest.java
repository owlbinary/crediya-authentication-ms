package com.crediya.auth.api.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;

import com.crediya.auth.api.constants.ErrorCodes;
import com.crediya.auth.api.dto.response.ErrorResponse;
import com.crediya.auth.model.exception.DatosInvalidosException;
import com.crediya.auth.model.exception.UsuarioYaExisteException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

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
        when(requestPath.value()).thenReturn("/api/v1/usuarios");
    }

    @Test
    void deberiaControlarWebExchangeBindException() {
        WebExchangeBindException ex = mock(WebExchangeBindException.class);
        FieldError fieldError1 = new FieldError("objeto", "campo1", "Error campo 1");
        FieldError fieldError2 = new FieldError("objeto", "campo2", "Error campo 2");
        when(ex.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        Mono<ResponseEntity<ErrorResponse>> result = globalExceptionHandler.handleValidationException(ex, exchange);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                    assertNotNull(response.getBody());
                    
                    ErrorResponse errorResponse = response.getBody();
                    assertEquals(ErrorCodes.DATOS_INVALIDOS, errorResponse.getCodigo());
                    assertTrue(errorResponse.getMensaje().contains("Error campo 1"));
                    assertTrue(errorResponse.getMensaje().contains("Error campo 2"));
                    assertEquals("/api/v1/usuarios", errorResponse.getPath());
                    assertNotNull(errorResponse.getTimestamp());
                })
                .verifyComplete();
    }

    @Test
    void deberiaControlarConstraintViolationException() {
        ConstraintViolationException ex = mock(ConstraintViolationException.class);
        ConstraintViolation<?> violation1 = mock(ConstraintViolation.class);
        ConstraintViolation<?> violation2 = mock(ConstraintViolation.class);
        
        when(violation1.getMessage()).thenReturn("Violación 1");
        when(violation2.getMessage()).thenReturn("Violación 2");
        when(ex.getConstraintViolations()).thenReturn(Set.of(violation1, violation2));

        Mono<ResponseEntity<ErrorResponse>> result = globalExceptionHandler.handleConstraintViolationException(ex, exchange);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                    assertNotNull(response.getBody());
                    
                    ErrorResponse errorResponse = response.getBody();
                    assertEquals(ErrorCodes.DATOS_INVALIDOS, errorResponse.getCodigo());
                    assertTrue(errorResponse.getMensaje().contains("Violación"));
                    assertEquals("/api/v1/usuarios", errorResponse.getPath());
                })
                .verifyComplete();
    }

    @Test
    void deberiaControlarUsuarioYaExisteException() {
        UsuarioYaExisteException ex = new UsuarioYaExisteException("El usuario con email test@test.com ya se encuentra registrado");

        Mono<ResponseEntity<ErrorResponse>> result = globalExceptionHandler.handleUsuarioYaExisteException(ex, exchange);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
                    assertNotNull(response.getBody());
                    
                    ErrorResponse errorResponse = response.getBody();
                    assertEquals(ErrorCodes.USUARIO_YA_EXISTE, errorResponse.getCodigo());
                    assertTrue(errorResponse.getMensaje().contains("test@test.com"));
                    assertEquals("/api/v1/usuarios", errorResponse.getPath());
                    assertNotNull(errorResponse.getTimestamp());
                })
                .verifyComplete();
    }

    @Test
    void deberiaControlarDatosInvalidosException() {
        DatosInvalidosException ex = new DatosInvalidosException("Datos inválidos proporcionados");

        Mono<ResponseEntity<ErrorResponse>> result = globalExceptionHandler.handleDatosInvalidosException(ex, exchange);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                    assertNotNull(response.getBody());
                    
                    ErrorResponse errorResponse = response.getBody();
                    assertEquals(ErrorCodes.DATOS_INVALIDOS, errorResponse.getCodigo());
                    assertEquals("Datos inválidos proporcionados", errorResponse.getMensaje());
                    assertEquals("/api/v1/usuarios", errorResponse.getPath());
                    assertNotNull(errorResponse.getTimestamp());
                })
                .verifyComplete();
    }

    @Test
    void deberiaControlarExcepcionGenerica() {
        Exception ex = new RuntimeException("Error inesperado");

        Mono<ResponseEntity<ErrorResponse>> result = globalExceptionHandler.handleGenericException(ex, exchange);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
                    assertNotNull(response.getBody());
                    
                    ErrorResponse errorResponse = response.getBody();
                    assertEquals(ErrorCodes.ERROR_INTERNO, errorResponse.getCodigo());
                    assertEquals(ErrorCodes.ERROR_INTERNO_MENSAJE, errorResponse.getMensaje());
                    assertEquals("/api/v1/usuarios", errorResponse.getPath());
                    assertNotNull(errorResponse.getTimestamp());
                })
                .verifyComplete();
    }

    @Test
    void deberiaControlarWebExchangeBindExceptionConErrorUnico() {
        WebExchangeBindException ex = mock(WebExchangeBindException.class);
        FieldError fieldError = new FieldError("objeto", "campo", "Error único");
        when(ex.getFieldErrors()).thenReturn(List.of(fieldError));

        Mono<ResponseEntity<ErrorResponse>> result = globalExceptionHandler.handleValidationException(ex, exchange);

        StepVerifier.create(result)
                .assertNext(response -> {
                    ErrorResponse errorResponse = response.getBody();
                    assertEquals("Error único", errorResponse.getMensaje());
                })
                .verifyComplete();
    }

    @Test
    void deberiaControlarConstraintViolationExceptionConViolacionUnica() {
        ConstraintViolationException ex = mock(ConstraintViolationException.class);
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Violación única");
        when(ex.getConstraintViolations()).thenReturn(Set.of(violation));

        Mono<ResponseEntity<ErrorResponse>> result = globalExceptionHandler.handleConstraintViolationException(ex, exchange);

        StepVerifier.create(result)
                .assertNext(response -> {
                    ErrorResponse errorResponse = response.getBody();
                    assertEquals("Violación única", errorResponse.getMensaje());
                })
                .verifyComplete();
    }

    @Test
    void deberiaUsarRutaCorrectaEnTodasLasRespuestas() {
        when(requestPath.value()).thenReturn("/api/v1/auth/login");
        DatosInvalidosException ex = new DatosInvalidosException("Test");

        Mono<ResponseEntity<ErrorResponse>> result = globalExceptionHandler.handleDatosInvalidosException(ex, exchange);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals("/api/v1/auth/login", response.getBody().getPath());
                })
                .verifyComplete();
    }
}
