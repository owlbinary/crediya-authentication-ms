package com.crediya.auth.api.security;

import com.crediya.auth.model.UsuarioAutenticado;
import com.crediya.auth.usecase.ValidarAutorizacionUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationHelper {
    
    private final ValidarAutorizacionUseCase validarAutorizacionUseCase;
    
    public Mono<UsuarioAutenticado> obtenerUsuarioAutenticado() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .cast(Authentication.class)
                .map(authentication -> (UsuarioAutenticado) authentication.getPrincipal());
    }
    
    public Mono<Void> validarPermisoAdminOAsesor() {
        return obtenerUsuarioAutenticado()
                .flatMap(validarAutorizacionUseCase::validarEsAdminOAsesor);
    }
    
    public Mono<Void> validarPermisoCliente() {
        return obtenerUsuarioAutenticado()
                .flatMap(validarAutorizacionUseCase::validarEsCliente);
    }
    
    public Mono<Void> validarClientePuedeCrearSolicitud(Long idUsuarioSolicitud) {
        return obtenerUsuarioAutenticado()
                .flatMap(usuario -> validarAutorizacionUseCase.validarClientePuedeCrearSolicitudParaSiMismo(usuario, idUsuarioSolicitud));
    }
}
