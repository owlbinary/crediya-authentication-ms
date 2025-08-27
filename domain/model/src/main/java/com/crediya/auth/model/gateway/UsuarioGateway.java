package com.crediya.auth.model.gateway;

import com.crediya.auth.model.Usuario;
import reactor.core.publisher.Mono;

public interface UsuarioGateway {
    Mono<Usuario> guardar(Usuario usuario);
    Mono<Boolean> existePorEmail(String email);
    Mono<Boolean> existePorDocumentoIdentidad(String documentoIdentidad);
    Mono<Usuario> buscarPorEmail(String email);
    Mono<Usuario> buscarPorDocumentoIdentidad(String documentoIdentidad);
}
