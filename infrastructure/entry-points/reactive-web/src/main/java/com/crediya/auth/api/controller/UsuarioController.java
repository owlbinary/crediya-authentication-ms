package com.crediya.auth.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.crediya.auth.api.dto.request.RegistrarUsuarioRequest;
import com.crediya.auth.api.dto.response.UsuarioResponse;
import com.crediya.auth.api.mapper.UsuarioDtoMapper;
import com.crediya.auth.usecase.RegistrarUsuarioUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Validated
@Tag(name = "Usuarios", description = "API para gestión de usuarios")
public class UsuarioController {

        private final RegistrarUsuarioUseCase registrarUsuarioUseCase;
        private final UsuarioDtoMapper usuarioDtoMapper;

        @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
        @ResponseStatus(HttpStatus.CREATED)
        @Operation(summary = "Registrar nuevo usuario", description = "Registra un nuevo usuario en el sistema")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente", content = @Content(schema = @Schema(implementation = UsuarioResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
                        @ApiResponse(responseCode = "401", description = "No autenticado"),
                        @ApiResponse(responseCode = "403", description = "No autorizado"),
                        @ApiResponse(responseCode = "409", description = "El usuario ya existe"),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        public Mono<UsuarioResponse> registrarUsuario(@Valid @RequestBody RegistrarUsuarioRequest request) {
                log.info("Solicitud de registro de usuario: {}", request.getEmail());

                return Mono.fromCallable(() -> usuarioDtoMapper.toDomain(request))
                                .flatMap(registrarUsuarioUseCase::ejecutar)
                                .map(usuarioDtoMapper::toResponse)
                                .doOnSuccess(response -> log.info("Usuario registrado exitosamente con ID: {}",
                                                response.getIdUsuario()))
                                .doOnError(error -> log.error("Error en el registro de usuario: {}", request.getEmail(),
                                                error));
        }
}
