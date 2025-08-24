package com.crediya.auth.usecase;

import com.crediya.auth.model.Usuario;
import com.crediya.auth.model.exception.UsuarioYaExisteException;
import com.crediya.auth.model.gateway.PasswordEncryptionGateway;
import com.crediya.auth.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class RegistrarUsuarioUseCase {

    private final UsuarioGateway usuarioRepository;
    private final PasswordEncryptionGateway passwordEncryptionGateway;

    public Mono<Usuario> ejecutar(Usuario usuario) {
        return verificarEmailNoExiste(usuario.getEmail())
                .then(encriptarPasswordYAgregarFechas(usuario)
                        .flatMap(usuarioRepository::guardar));
    }

    private Mono<Void> verificarEmailNoExiste(String email) {
        return usuarioRepository.existePorEmail(email)
                .flatMap(existe -> {
                    if (Boolean.TRUE.equals(existe)) {
                        return Mono.error(new UsuarioYaExisteException(email));
                    }
                    return Mono.empty();
                });
    }

    private Mono<Usuario> encriptarPasswordYAgregarFechas(Usuario usuario) {
        LocalDateTime ahora = LocalDateTime.now();

        return passwordEncryptionGateway.encriptarPassword(usuario.getPassword())
                .map(passwordEncriptado -> usuario.toBuilder()
                        .password(passwordEncriptado)
                        .fechaCreacion(ahora)
                        .fechaActualizacion(ahora)
                        .build());
    }
}
