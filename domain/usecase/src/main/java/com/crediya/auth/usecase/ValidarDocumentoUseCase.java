package com.crediya.auth.usecase;

import com.crediya.auth.model.Usuario;
import com.crediya.auth.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ValidarDocumentoUseCase {

    private final UsuarioGateway usuarioRepository;

    public Mono<Boolean> documentoExiste(String documentoIdentidad) {
        return usuarioRepository.existePorDocumentoIdentidad(documentoIdentidad);
    }

    public Mono<Usuario> buscarUsuarioPorDocumento(String documentoIdentidad) {
        return usuarioRepository.buscarPorDocumentoIdentidad(documentoIdentidad);
    }
}
