package com.crediya.auth.api.security;

import com.crediya.auth.model.UsuarioAutenticado;
import com.crediya.auth.model.exception.AccesoNoAutorizadoException;
import com.crediya.auth.usecase.ValidarAutorizacionUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorizationHelperTest {

    @Mock
    private ValidarAutorizacionUseCase validarAutorizacionUseCase;
    
    @Mock
    private SecurityContext securityContext;
    
    @Mock
    private Authentication authentication;

    private AuthorizationHelper authorizationHelper;

    @BeforeEach
    void setUp() {
        authorizationHelper = new AuthorizationHelper(validarAutorizacionUseCase);
    }

    @Test
    void deberiaObtenerUsuarioAutenticado() {
        UsuarioAutenticado usuario = UsuarioAutenticado.builder()
                .idUsuario(1L)
                .email("test@test.com")
                .idRol(1L)
                .build();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(usuario);

        StepVerifier.create(
                authorizationHelper.obtenerUsuarioAutenticado()
                        .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)))
        )
        .expectNext(usuario)
        .verifyComplete();
    }

    @Test
    void deberiaValidarPermisoAdminOAsesor() {
        UsuarioAutenticado usuario = UsuarioAutenticado.builder()
                .idUsuario(1L)
                .email("admin@test.com")
                .idRol(1L)
                .build();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(usuario);
        when(validarAutorizacionUseCase.validarEsAdminOAsesor(usuario))
                .thenReturn(Mono.empty());

        StepVerifier.create(
                authorizationHelper.validarPermisoAdminOAsesor()
                        .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)))
        )
        .verifyComplete();
    }

    @Test
    void deberiaDenegarPermisoAdminOAsesorParaCliente() {
        UsuarioAutenticado usuario = UsuarioAutenticado.builder()
                .idUsuario(3L)
                .email("cliente@test.com")
                .idRol(3L)
                .build();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(usuario);
        when(validarAutorizacionUseCase.validarEsAdminOAsesor(usuario))
                .thenReturn(Mono.error(new AccesoNoAutorizadoException("Acceso no autorizado")));

        StepVerifier.create(
                authorizationHelper.validarPermisoAdminOAsesor()
                        .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)))
        )
        .expectError(AccesoNoAutorizadoException.class)
        .verify();
    }

    @Test
    void deberiaValidarPermisoCliente() {
        UsuarioAutenticado usuario = UsuarioAutenticado.builder()
                .idUsuario(3L)
                .email("cliente@test.com")
                .idRol(3L)
                .build();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(usuario);
        when(validarAutorizacionUseCase.validarEsCliente(usuario))
                .thenReturn(Mono.empty());

        StepVerifier.create(
                authorizationHelper.validarPermisoCliente()
                        .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)))
        )
        .verifyComplete();
    }

    @Test
    void deberiaValidarClientePuedeCrearSolicitud() {
        UsuarioAutenticado usuario = UsuarioAutenticado.builder()
                .idUsuario(3L)
                .email("cliente@test.com")
                .idRol(3L)
                .build();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(usuario);
        when(validarAutorizacionUseCase.validarClientePuedeCrearSolicitudParaSiMismo(usuario, 3L))
                .thenReturn(Mono.empty());

        StepVerifier.create(
                authorizationHelper.validarClientePuedeCrearSolicitud(3L)
                        .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)))
        )
        .verifyComplete();
    }
}
