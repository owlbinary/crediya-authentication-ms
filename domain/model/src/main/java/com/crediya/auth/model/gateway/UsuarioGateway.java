package com.crediya.auth.model.gateway;

import com.crediya.auth.model.Usuario;
import reactor.core.publisher.Mono;

public interface UsuarioGateway {
    Mono<Usuario> guardar(Usuario usuario);
    Mono<Boolean> existePorEmail(String email);
    Mono<Usuario> buscarPorEmail(String email);
}
