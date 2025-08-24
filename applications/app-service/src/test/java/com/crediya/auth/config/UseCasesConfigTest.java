package com.crediya.auth.config;

import com.crediya.auth.model.gateway.PasswordEncryptionGateway;
import com.crediya.auth.model.gateway.UsuarioGateway;
import com.crediya.auth.usecase.RegistrarUsuarioUseCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class UseCasesConfigTest {

    @Test
    void testUseCaseBeansExist() {
        UsuarioGateway usuarioRepository = mock(UsuarioGateway.class);
        PasswordEncryptionGateway passwordEncryptionGateway = mock(PasswordEncryptionGateway.class);
        RegistrarUsuarioUseCase registrarUsuarioUseCase = new RegistrarUsuarioUseCase(usuarioRepository, passwordEncryptionGateway);
        
        assertNotNull(registrarUsuarioUseCase, "RegistrarUsuarioUseCase should not be null");
        assertNotNull(usuarioRepository, "UsuarioRepository should not be null");
        assertNotNull(passwordEncryptionGateway, "PasswordEncryptionGateway should not be null");
    }
}