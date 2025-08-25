package com.crediya.auth.usecase;

import com.crediya.auth.model.CredencialesLogin;
import com.crediya.auth.model.TokenAutenticacion;
import com.crediya.auth.model.Usuario;
import com.crediya.auth.model.exception.CredencialesInvalidasException;
import com.crediya.auth.model.gateway.JwtTokenGateway;
import com.crediya.auth.model.gateway.PasswordEncryptionGateway;
import com.crediya.auth.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class LoginUseCase {

    private final UsuarioGateway usuarioGateway;
    private final PasswordEncryptionGateway passwordEncryptionGateway;
    private final JwtTokenGateway jwtTokenGateway;
    
    public Mono<TokenAutenticacion> ejecutar(CredencialesLogin credenciales) {
        return usuarioGateway.buscarPorEmail(credenciales.getEmail())
                .switchIfEmpty(Mono.error(new CredencialesInvalidasException("Credenciales inválidas")))
                .flatMap(usuario -> validarPassword(credenciales.getPassword(), usuario))
                .flatMap(this::generarTokenAutenticacion);
    }

    private Mono<Usuario> validarPassword(String passwordPlano, Usuario usuario) {
        return passwordEncryptionGateway.validarPassword(passwordPlano, usuario.getPassword())
                .flatMap(esValido -> {
                    if (Boolean.TRUE.equals(esValido)) {
                        return Mono.just(usuario);
                    } else {
                        return Mono.error(new CredencialesInvalidasException("Credenciales inválidas"));
                    }
                });
    }
    
    private Mono<TokenAutenticacion> generarTokenAutenticacion(Usuario usuario) {
        return jwtTokenGateway.generarToken(usuario)
                .map(token -> TokenAutenticacion.builder()
                        .accessToken(token)
                        .tokenType("Bearer")
                        .fechaExpiracion(LocalDateTime.now().plusHours(24))
                        .usuario(usuario)
                        .build());
    }
}
