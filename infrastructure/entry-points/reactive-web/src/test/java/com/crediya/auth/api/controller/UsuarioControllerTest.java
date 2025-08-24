package com.crediya.auth.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.reset;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.crediya.auth.api.dto.request.RegistrarUsuarioRequest;
import com.crediya.auth.api.dto.response.UsuarioResponse;
import com.crediya.auth.api.handler.GlobalExceptionHandler;
import com.crediya.auth.api.mapper.UsuarioDtoMapper;
import com.crediya.auth.model.Usuario;
import com.crediya.auth.model.exception.DatosInvalidosException;
import com.crediya.auth.model.exception.UsuarioYaExisteException;
import com.crediya.auth.usecase.RegistrarUsuarioUseCase;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private RegistrarUsuarioUseCase registrarUsuarioUseCase;

    @Mock
    private UsuarioDtoMapper usuarioDtoMapper;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        reset(registrarUsuarioUseCase, usuarioDtoMapper);
        
        UsuarioController controller = new UsuarioController(registrarUsuarioUseCase, usuarioDtoMapper);
        webTestClient = WebTestClient.bindToController(controller)
                .controllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void deberiaRegistrarUsuarioExitosamente() {
        RegistrarUsuarioRequest request = crearRequestValido();
        Usuario usuario = crearUsuario();
        Usuario usuarioGuardado = usuario.toBuilder().idUsuario(1L).fechaCreacion(LocalDateTime.now()).build();
        UsuarioResponse response = crearResponse();

        when(usuarioDtoMapper.toDomain(any(RegistrarUsuarioRequest.class))).thenReturn(usuario);
        when(registrarUsuarioUseCase.ejecutar(any(Usuario.class))).thenReturn(Mono.just(usuarioGuardado));
        when(usuarioDtoMapper.toResponse(any(Usuario.class))).thenReturn(response);

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(UsuarioResponse.class)
                .value(responseBody -> {
                    assert responseBody.getIdUsuario().equals(1L);
                    assert responseBody.getEmail().equals("test@test.com");
                    assert responseBody.getNombre().equals("Juan");
                });
    }

    @Test
    void deberiaRetornarBadRequestCuandoDatosInvalidos() {
        RegistrarUsuarioRequest request = crearRequestValido();
        Usuario usuario = crearUsuario();

        when(usuarioDtoMapper.toDomain(any(RegistrarUsuarioRequest.class))).thenReturn(usuario);
        when(registrarUsuarioUseCase.ejecutar(any(Usuario.class)))
                .thenReturn(Mono.error(new DatosInvalidosException("El campo 'nombre' es obligatorio")));

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void deberiaRetornarConflictCuandoUsuarioYaExiste() {
        RegistrarUsuarioRequest request = crearRequestValido();
        Usuario usuario = crearUsuario();

        when(usuarioDtoMapper.toDomain(any(RegistrarUsuarioRequest.class))).thenReturn(usuario);
        when(registrarUsuarioUseCase.ejecutar(any(Usuario.class)))
                .thenReturn(Mono.error(new UsuarioYaExisteException("test@test.com")));

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(409);
    }

    private RegistrarUsuarioRequest crearRequestValido() {
        return RegistrarUsuarioRequest.builder()
                .nombre("Juan")
                .apellido("Sierra")
                .email("test@test.com")
                .password("password123")
                .documentoIdentidad("12345678")
                .telefono("3001234567")
                .idRol(1L)
                .salarioBase(new BigDecimal("2500000"))
                .build();
    }

    private Usuario crearUsuario() {
        return Usuario.builder()
                .nombre("Juan")
                .apellido("Sierra")
                .email("test@test.com")
                .password("password123")
                .documentoIdentidad("12345678")
                .telefono("3001234567")
                .idRol(1L)
                .salarioBase(new BigDecimal("2500000"))
                .build();
    }

    private UsuarioResponse crearResponse() {
        return UsuarioResponse.builder()
                .idUsuario(1L)
                .nombre("Juan")
                .apellido("Sierra")
                .email("test@test.com")
                .documentoIdentidad("12345678")
                .telefono("3001234567")
                .idRol(1L)
                .salarioBase(new BigDecimal("2500000"))
                .fechaCreacion(LocalDateTime.now())
                .build();
    }
}
