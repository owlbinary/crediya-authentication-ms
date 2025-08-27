package com.crediya.auth.api.handler;

import com.crediya.auth.api.constants.ErrorCodes;
import com.crediya.auth.api.dto.response.ErrorResponse;
import com.crediya.auth.model.exception.AccesoNoAutorizadoException;
import com.crediya.auth.model.exception.CredencialesInvalidasException;
import com.crediya.auth.model.exception.DatosInvalidosException;
import com.crediya.auth.model.exception.DocumentoYaExisteException;
import com.crediya.auth.model.exception.TokenInvalidoException;
import com.crediya.auth.model.exception.UsuarioYaExisteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleValidationException(
            WebExchangeBindException ex, ServerWebExchange exchange) {

        String errores = ex.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        log.warn("Errores de validación: {}", errores);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .codigo(ErrorCodes.DATOS_INVALIDOS)
                .mensaje(errores)
                .timestamp(LocalDateTime.now())
                .path(exchange.getRequest().getPath().value())
                .build();

        return Mono.just(ResponseEntity.badRequest().body(errorResponse));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleConstraintViolationException(
            ConstraintViolationException ex, ServerWebExchange exchange) {

        String errores = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        log.warn("Violaciones de restricciones: {}", errores);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .codigo(ErrorCodes.DATOS_INVALIDOS)
                .mensaje(errores)
                .timestamp(LocalDateTime.now())
                .path(exchange.getRequest().getPath().value())
                .build();

        return Mono.just(ResponseEntity.badRequest().body(errorResponse));
    }

    @ExceptionHandler(UsuarioYaExisteException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleUsuarioYaExisteException(
            UsuarioYaExisteException ex, ServerWebExchange exchange) {

                log.warn("Usuario ya existe: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .codigo(ErrorCodes.USUARIO_YA_EXISTE)
                .mensaje(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(exchange.getRequest().getPath().value())
                .build();

        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse));
    }

    @ExceptionHandler(DocumentoYaExisteException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleDocumentoYaExisteException(
            DocumentoYaExisteException ex, ServerWebExchange exchange) {

        log.warn("Documento ya existe: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .codigo(ErrorCodes.USUARIO_YA_EXISTE)
                .mensaje(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(exchange.getRequest().getPath().value())
                .build();

        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse));
    }

    @ExceptionHandler(DatosInvalidosException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleDatosInvalidosException(
            DatosInvalidosException ex, ServerWebExchange exchange) {

        log.warn("Datos inválidos: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .codigo(ErrorCodes.DATOS_INVALIDOS)
                .mensaje(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(exchange.getRequest().getPath().value())
                .build();

        return Mono.just(ResponseEntity.badRequest().body(errorResponse));
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleCredencialesInvalidasException(
            CredencialesInvalidasException ex, ServerWebExchange exchange) {

        log.warn("Credenciales inválidas: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .codigo("CREDENCIALES_INVALIDAS")
                .mensaje("Credenciales inválidas")
                .timestamp(LocalDateTime.now())
                .path(exchange.getRequest().getPath().value())
                .build();

        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse));
    }

    @ExceptionHandler(TokenInvalidoException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleTokenInvalidoException(
            TokenInvalidoException ex, ServerWebExchange exchange) {

        log.warn("Token inválido o expirado: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .codigo("TOKEN_INVALIDO")
                .mensaje("Token inválido o expirado")
                .timestamp(LocalDateTime.now())
                .path(exchange.getRequest().getPath().value())
                .build();

        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleAccessDeniedException(
            AccessDeniedException ex, ServerWebExchange exchange) {

        log.warn("Acceso denegado por falta de permisos: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .codigo(ErrorCodes.ACCESO_NO_AUTORIZADO)
                .mensaje(ErrorCodes.ACCESO_NO_AUTORIZADO_MENSAJE)
                .timestamp(LocalDateTime.now())
                .path(exchange.getRequest().getPath().value())
                .build();

        return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse));
    }

    @ExceptionHandler(AccesoNoAutorizadoException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleAccesoNoAutorizadoException(
            AccesoNoAutorizadoException ex, ServerWebExchange exchange) {

        log.warn("Acceso no autorizado: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .codigo("ACCESO_NO_AUTORIZADO")
                .mensaje(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(exchange.getRequest().getPath().value())
                .build();

        return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGenericException(
            Exception ex, ServerWebExchange exchange) {

        log.error("Error inesperado en la aplicación", ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .codigo(ErrorCodes.ERROR_INTERNO)
                .mensaje(ErrorCodes.ERROR_INTERNO_MENSAJE)
                .timestamp(LocalDateTime.now())
                .path(exchange.getRequest().getPath().value())
                .build();

        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
    }
}
