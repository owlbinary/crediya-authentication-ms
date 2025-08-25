package com.crediya.auth.usecase;

import com.crediya.auth.model.UsuarioAutenticado;
import com.crediya.auth.model.exception.AccesoNoAutorizadoException;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ValidarAutorizacionUseCase {
    
    private static final Long ROL_ADMIN = 1L;
    private static final Long ROL_ASESOR = 2L;
    private static final Long ROL_CLIENTE = 3L;
    private static final String ACCESO_NO_AUTORIZADO = "Acceso no autorizado";
    
    public Mono<Void> validarEsAdminOAsesor(UsuarioAutenticado usuario) {
        if (esAdminOAsesor(usuario.getIdRol())) {
            return Mono.empty();
        } else {
            return Mono.error(new AccesoNoAutorizadoException(ACCESO_NO_AUTORIZADO));
        }
    }
    
    public Mono<Void> validarEsCliente(UsuarioAutenticado usuario) {
        if (ROL_CLIENTE.equals(usuario.getIdRol())) {
            return Mono.empty();
        } else {
            return Mono.error(new AccesoNoAutorizadoException(ACCESO_NO_AUTORIZADO));
        }
    }
    
    public Mono<Void> validarEsMismoUsuario(UsuarioAutenticado usuarioAutenticado, Long idUsuarioSolicitado) {
        if (usuarioAutenticado.getIdUsuario().equals(idUsuarioSolicitado)) {
            return Mono.empty();
        } else {
            return Mono.error(new AccesoNoAutorizadoException(ACCESO_NO_AUTORIZADO));
        }
    }
    
    public Mono<Void> validarClientePuedeCrearSolicitudParaSiMismo(UsuarioAutenticado usuario, Long idUsuarioSolicitud) {
        return validarEsCliente(usuario)
                .then(validarEsMismoUsuario(usuario, idUsuarioSolicitud));
    }
    
    private boolean esAdminOAsesor(Long idRol) {
        return ROL_ADMIN.equals(idRol) || ROL_ASESOR.equals(idRol);
    }
}
