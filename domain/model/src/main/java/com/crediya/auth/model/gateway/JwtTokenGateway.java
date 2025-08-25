package com.crediya.auth.model.gateway;

import com.crediya.auth.model.Usuario;
import com.crediya.auth.model.UsuarioAutenticado;
import reactor.core.publisher.Mono;

public interface JwtTokenGateway {
    Mono<String> generarToken(Usuario usuario);
    Mono<UsuarioAutenticado> validarToken(String token);
    Mono<String> extraerEmailDelToken(String token);
}
