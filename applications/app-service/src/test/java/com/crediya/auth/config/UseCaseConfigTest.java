package com.crediya.auth.config;

import com.crediya.auth.model.gateway.PasswordEncryptionGateway;
import com.crediya.auth.model.gateway.UsuarioGateway;
import com.crediya.auth.usecase.RegistrarUsuarioUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UseCaseConfigTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    @Mock
    private PasswordEncryptionGateway passwordEncryptionGateway;

    private UseCaseConfig useCaseConfig;

    @BeforeEach
    void setUp() {
        useCaseConfig = new UseCaseConfig();
    }

    @Test
    void deberiaCrearRegistrarUsuarioUseCaseBean() {
        RegistrarUsuarioUseCase registrarUsuarioUseCase = useCaseConfig
                .registrarUsuarioUseCase(usuarioGateway, passwordEncryptionGateway);

        assertNotNull(registrarUsuarioUseCase);
    }

    @Test
    void deberiaInyectarDependenciasCorrectamente() {
        RegistrarUsuarioUseCase registrarUsuarioUseCase = useCaseConfig
                .registrarUsuarioUseCase(usuarioGateway, passwordEncryptionGateway);

        assertNotNull(registrarUsuarioUseCase);
        assertNotNull(usuarioGateway);
        assertNotNull(passwordEncryptionGateway);
    }

    @Test
    void deberiaRetornarNuevaInstanciaEnCadaLlamada() {
        RegistrarUsuarioUseCase useCase1 = useCaseConfig
                .registrarUsuarioUseCase(usuarioGateway, passwordEncryptionGateway);
        RegistrarUsuarioUseCase useCase2 = useCaseConfig
                .registrarUsuarioUseCase(usuarioGateway, passwordEncryptionGateway);

        assertNotNull(useCase1);
        assertNotNull(useCase2);
        assertNotSame(useCase1, useCase2, "Cada llamada debería retornar una nueva instancia");
    }

    @Test
    void deberiaCrearUseCaseConParametrosNulos() {
        RegistrarUsuarioUseCase useCaseConUsuarioNulo = useCaseConfig
                .registrarUsuarioUseCase(null, passwordEncryptionGateway);
        assertNotNull(useCaseConUsuarioNulo);

        RegistrarUsuarioUseCase useCaseConPasswordNulo = useCaseConfig
                .registrarUsuarioUseCase(usuarioGateway, null);
        assertNotNull(useCaseConPasswordNulo);

        RegistrarUsuarioUseCase useCaseConAmbosNulos = useCaseConfig
                .registrarUsuarioUseCase(null, null);
        assertNotNull(useCaseConAmbosNulos);
    }
}
