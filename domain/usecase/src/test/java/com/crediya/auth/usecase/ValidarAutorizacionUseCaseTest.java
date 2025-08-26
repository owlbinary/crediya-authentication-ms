package com.crediya.auth.usecase;

import com.crediya.auth.model.UsuarioAutenticado;
import com.crediya.auth.model.exception.AccesoNoAutorizadoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class ValidarAutorizacionUseCaseTest {

    private ValidarAutorizacionUseCase validarAutorizacionUseCase;

    @BeforeEach
    void setUp() {
        validarAutorizacionUseCase = new ValidarAutorizacionUseCase();
    }

    @Test
    void deberiaPermitirAccesoParaAdmin() {
        UsuarioAutenticado usuario = UsuarioAutenticado.builder()
                .idUsuario(1L)
                .email("admin@test.com")
                .idRol(1L)
                .build();

        StepVerifier.create(validarAutorizacionUseCase.validarEsAdminOAsesor(usuario))
                .verifyComplete();
    }

    @Test
    void deberiaPermitirAccesoParaAsesor() {
        UsuarioAutenticado usuario = UsuarioAutenticado.builder()
                .idUsuario(2L)
                .email("asesor@test.com")
                .idRol(2L)
                .build();

        StepVerifier.create(validarAutorizacionUseCase.validarEsAdminOAsesor(usuario))
                .verifyComplete();
    }

    @Test
    void deberiaDenegarAccesoParaClienteEnValidacionAdminOAsesor() {
        UsuarioAutenticado usuario = UsuarioAutenticado.builder()
                .idUsuario(3L)
                .email("cliente@test.com")
                .idRol(3L)
                .build();

        StepVerifier.create(validarAutorizacionUseCase.validarEsAdminOAsesor(usuario))
                .expectError(AccesoNoAutorizadoException.class)
                .verify();
    }

    @Test
    void deberiaPermitirAccesoParaCliente() {
        UsuarioAutenticado usuario = UsuarioAutenticado.builder()
                .idUsuario(3L)
                .email("cliente@test.com")
                .idRol(3L)
                .build();

        StepVerifier.create(validarAutorizacionUseCase.validarEsCliente(usuario))
                .verifyComplete();
    }

    @Test
    void deberiaDenegarAccesoParaAdminEnValidacionCliente() {
        UsuarioAutenticado usuario = UsuarioAutenticado.builder()
                .idUsuario(1L)
                .email("admin@test.com")
                .idRol(1L)
                .build();

        StepVerifier.create(validarAutorizacionUseCase.validarEsCliente(usuario))
                .expectError(AccesoNoAutorizadoException.class)
                .verify();
    }

    @Test
    void deberiaPermitirAccesoAlMismoUsuario() {
        UsuarioAutenticado usuario = UsuarioAutenticado.builder()
                .idUsuario(1L)
                .email("usuario@test.com")
                .idRol(3L)
                .build();

        StepVerifier.create(validarAutorizacionUseCase.validarEsMismoUsuario(usuario, 1L))
                .verifyComplete();
    }

    @Test
    void deberiaDenegarAccesoADiferenteUsuario() {
        UsuarioAutenticado usuario = UsuarioAutenticado.builder()
                .idUsuario(1L)
                .email("usuario@test.com")
                .idRol(3L)
                .build();

        StepVerifier.create(validarAutorizacionUseCase.validarEsMismoUsuario(usuario, 2L))
                .expectError(AccesoNoAutorizadoException.class)
                .verify();
    }

    @Test
    void deberiaPermitirClienteCrearSolicitudParaSiMismo() {
        UsuarioAutenticado usuario = UsuarioAutenticado.builder()
                .idUsuario(3L)
                .email("cliente@test.com")
                .idRol(3L)
                .build();

        StepVerifier.create(validarAutorizacionUseCase.validarClientePuedeCrearSolicitudParaSiMismo(usuario, 3L))
                .verifyComplete();
    }

    @Test
    void deberiaDenegarClienteCrearSolicitudParaOtroUsuario() {
        UsuarioAutenticado usuario = UsuarioAutenticado.builder()
                .idUsuario(3L)
                .email("cliente@test.com")
                .idRol(3L)
                .build();

        StepVerifier.create(validarAutorizacionUseCase.validarClientePuedeCrearSolicitudParaSiMismo(usuario, 4L))
                .expectError(AccesoNoAutorizadoException.class)
                .verify();
    }

    @Test
    void deberiaDenegarAdminCrearSolicitudComoCliente() {
        UsuarioAutenticado usuario = UsuarioAutenticado.builder()
                .idUsuario(1L)
                .email("admin@test.com")
                .idRol(1L)
                .build();

        StepVerifier.create(validarAutorizacionUseCase.validarClientePuedeCrearSolicitudParaSiMismo(usuario, 1L))
                .expectError(AccesoNoAutorizadoException.class)
                .verify();
    }
}
