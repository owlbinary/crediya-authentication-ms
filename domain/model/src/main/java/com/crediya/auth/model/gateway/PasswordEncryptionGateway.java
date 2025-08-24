package com.crediya.auth.model.gateway;

import reactor.core.publisher.Mono;

public interface PasswordEncryptionGateway {
    Mono<String> encriptarPassword(String passwordPlano);
    Mono<Boolean> validarPassword(String passwordPlano, String passwordEncriptado);
}
