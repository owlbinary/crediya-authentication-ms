package com.crediya.auth.api.controller;

import com.crediya.auth.api.dto.request.LoginRequest;
import com.crediya.auth.api.dto.response.LoginResponse;
import com.crediya.auth.api.mapper.AuthDtoMapper;
import com.crediya.auth.usecase.LoginUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
@Tag(name = "Autenticación", description = "API para autenticación de usuarios")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final AuthDtoMapper authDtoMapper;

    @PostMapping(
            value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario y devuelve un token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Mono<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Recibida solicitud de login para el usuario: {}", request.getEmail());

        return Mono.fromCallable(() -> authDtoMapper.toDomain(request))
                .flatMap(loginUseCase::ejecutar)
                .map(authDtoMapper::toResponse)
                .doOnSuccess(response ->
                    log.info("Login exitoso para el usuario: {}", request.getEmail()))
                .doOnError(error ->
                    log.warn("Error en login para el usuario: {}", request.getEmail(), error));
    }
}
