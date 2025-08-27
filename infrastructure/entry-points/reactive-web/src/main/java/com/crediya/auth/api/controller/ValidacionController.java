package com.crediya.auth.api.controller;

import com.crediya.auth.api.dto.response.ValidacionDocumentoResponse;
import com.crediya.auth.api.mapper.UsuarioDtoMapper;
import com.crediya.auth.api.validation.ValidDocumentoIdentidad;
import com.crediya.auth.usecase.ValidarDocumentoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/validaciones")
@RequiredArgsConstructor
@Validated
@Tag(name = "Validaciones", description = "API para validaciones de documentos y datos")
public class ValidacionController {

    private final ValidarDocumentoUseCase validarDocumentoUseCase;
    private final UsuarioDtoMapper usuarioDtoMapper;

    @GetMapping(value = "/documento/{documentoIdentidad}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasRole('ASESOR')")
    @Operation(summary = "Validar existencia de documento de identidad", 
               description = "Valida si un documento de identidad existe en el sistema para determinar si puede continuar con el proceso o debe crear el usuario primero. Si el usuario existe, incluye la información detallada del mismo.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Validación realizada exitosamente", 
                    content = @Content(schema = @Schema(implementation = ValidacionDocumentoResponse.class))),
        @ApiResponse(responseCode = "400", description = "Formato de documento inválido"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "No autorizado - Requiere rol ADMIN o ASESOR"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Mono<ValidacionDocumentoResponse> validarDocumento(
            @Parameter(description = "Documento de identidad a validar", required = true)
            @PathVariable @ValidDocumentoIdentidad String documentoIdentidad) {
        
        log.info("Validando existencia del documento: {}", documentoIdentidad);
        
        return validarDocumentoUseCase.documentoExiste(documentoIdentidad)
                .flatMap(existe -> {
                    if (Boolean.TRUE.equals(existe)) {
                        return validarDocumentoUseCase.buscarUsuarioPorDocumento(documentoIdentidad)
                                .map(usuario -> {
                                    final String mensaje = "El documento existe, puede continuar con el proceso";
                                    
                                    return ValidacionDocumentoResponse.builder()
                                            .existe(true)
                                            .mensaje(mensaje)
                                            .usuario(usuarioDtoMapper.toResponse(usuario))
                                            .build();
                                })
                                .switchIfEmpty(Mono.fromCallable(() -> {
                                    final String mensaje = "El documento existe, puede continuar con el proceso";
                                    
                                    return ValidacionDocumentoResponse.builder()
                                            .existe(true)
                                            .mensaje(mensaje)
                                            .usuario(null)
                                            .build();
                                }));
                    } else {
                        final String mensaje = "El documento no existe, debe realizar la creación del usuario para continuar con el proceso";
                        
                        return Mono.just(ValidacionDocumentoResponse.builder()
                                .existe(false)
                                .mensaje(mensaje)
                                .usuario(null)
                                .build());
                    }
                })
                .doOnSuccess(response -> log.info("Validación de documento {} completada: existe={}", 
                        documentoIdentidad, response.getExiste()))
                .doOnError(error -> log.error("Error al validar documento {}: {}", 
                        documentoIdentidad, error.getMessage(), error));
    }
}
