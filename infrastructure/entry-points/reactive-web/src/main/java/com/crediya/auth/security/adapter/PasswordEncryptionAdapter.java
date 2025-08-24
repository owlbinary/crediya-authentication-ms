package com.crediya.auth.security.adapter;

import com.crediya.auth.model.gateway.PasswordEncryptionGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordEncryptionAdapter implements PasswordEncryptionGateway {
    
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public Mono<String> encriptarPassword(String passwordPlano) {
        log.debug("Encriptando contrasena");
        return Mono.fromCallable(() -> passwordEncoder.encode(passwordPlano))
                .subscribeOn(reactor.core.scheduler.Schedulers.boundedElastic());
    }
    
    @Override
    public Mono<Boolean> validarPassword(String passwordPlano, String passwordEncriptado) {
        log.debug("Validando contrasena");
        return Mono.fromCallable(() -> passwordEncoder.matches(passwordPlano, passwordEncriptado))
                .subscribeOn(reactor.core.scheduler.Schedulers.boundedElastic());
    }
}
