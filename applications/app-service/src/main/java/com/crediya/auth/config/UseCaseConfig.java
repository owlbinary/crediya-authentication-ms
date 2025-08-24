package com.crediya.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.crediya.auth.model.gateway.PasswordEncryptionGateway;
import com.crediya.auth.model.gateway.UsuarioGateway;
import com.crediya.auth.usecase.RegistrarUsuarioUseCase;

@Configuration
public class UseCaseConfig {

    @Bean
    public RegistrarUsuarioUseCase registrarUsuarioUseCase(
            UsuarioGateway usuarioRepository, PasswordEncryptionGateway passwordEncryptionGateway) {
        return new RegistrarUsuarioUseCase(usuarioRepository, passwordEncryptionGateway);
    }
}
