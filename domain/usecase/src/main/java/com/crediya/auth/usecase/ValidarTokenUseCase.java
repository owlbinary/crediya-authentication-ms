package com.crediya.auth.usecase;

import com.crediya.auth.model.UsuarioAutenticado;
import com.crediya.auth.model.exception.TokenInvalidoException;
import com.crediya.auth.model.gateway.JwtTokenGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ValidarTokenUseCase {
    
    private final JwtTokenGateway jwtTokenGateway;
    
    public Mono<UsuarioAutenticado> ejecutar(String token) {
        return jwtTokenGateway.validarToken(token)
                .switchIfEmpty(Mono.error(new TokenInvalidoException("Token inválido o expirado")));
    }
}
