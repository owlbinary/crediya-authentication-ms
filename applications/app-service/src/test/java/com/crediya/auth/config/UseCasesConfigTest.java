package com.crediya.auth.config;

import com.crediya.auth.usecase.LoginUseCase;
import com.crediya.auth.usecase.RegistrarUsuarioUseCase;
import com.crediya.auth.usecase.ValidarAutorizacionUseCase;
import com.crediya.auth.usecase.ValidarDocumentoUseCase;
import com.crediya.auth.usecase.ValidarTokenUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.crediya.auth.model.gateway.UsuarioGateway;
import com.crediya.auth.model.gateway.PasswordEncryptionGateway;
import com.crediya.auth.model.gateway.JwtTokenGateway;

@SpringBootTest
@ContextConfiguration(classes = {UseCasesConfig.class, UseCasesConfigTest.TestConfiguration.class})
class UseCasesConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void deberiaConfigurarComponentScanCorrectamente() {
        assertNotNull(applicationContext);
    }

    @Test
    void deberiaRegistrarRegistrarUsuarioUseCaseComoBean() {
        assertTrue(applicationContext.containsBean("registrarUsuarioUseCase"));
        RegistrarUsuarioUseCase useCase = applicationContext.getBean(RegistrarUsuarioUseCase.class);
        assertNotNull(useCase);
    }

    @Test
    void deberiaRegistrarLoginUseCaseComoBean() {
        assertTrue(applicationContext.containsBean("loginUseCase"));
        LoginUseCase useCase = applicationContext.getBean(LoginUseCase.class);
        assertNotNull(useCase);
    }

    @Test
    void deberiaRegistrarValidarDocumentoUseCaseComoBean() {
        assertTrue(applicationContext.containsBean("validarDocumentoUseCase"));
        ValidarDocumentoUseCase useCase = applicationContext.getBean(ValidarDocumentoUseCase.class);
        assertNotNull(useCase);
    }

    @Test
    void deberiaRegistrarValidarTokenUseCaseComoBean() {
        assertTrue(applicationContext.containsBean("validarTokenUseCase"));
        ValidarTokenUseCase useCase = applicationContext.getBean(ValidarTokenUseCase.class);
        assertNotNull(useCase);
    }

    @Test
    void deberiaRegistrarValidarAutorizacionUseCaseComoBean() {
        assertTrue(applicationContext.containsBean("validarAutorizacionUseCase"));
        ValidarAutorizacionUseCase useCase = applicationContext.getBean(ValidarAutorizacionUseCase.class);
        assertNotNull(useCase);
    }

    @Test
    void deberiaTenerElPatronCorrectoPareaUseCases() {
        String[] beanNames = applicationContext.getBeanNamesForType(Object.class);
        long useCaseCount = java.util.Arrays.stream(beanNames)
                .filter(name -> name.endsWith("UseCase"))
                .count();
        
        assertTrue(useCaseCount >= 5, "Debería haber al menos 5 Use Cases registrados");
    }

    @Test
    void deberiaCrearInstanciasUnicasDeUseCases() {
        RegistrarUsuarioUseCase useCase1 = applicationContext.getBean(RegistrarUsuarioUseCase.class);
        RegistrarUsuarioUseCase useCase2 = applicationContext.getBean(RegistrarUsuarioUseCase.class);
        
        assertSame(useCase1, useCase2, "Los Use Cases deberían ser singleton por defecto");
    }

    @Configuration
    static class TestConfiguration {
        
        @Bean
        public UsuarioGateway usuarioGateway() {
            return mock(UsuarioGateway.class);
        }
        
        @Bean
        public PasswordEncryptionGateway passwordEncryptionGateway() {
            return mock(PasswordEncryptionGateway.class);
        }
        
        @Bean
        public JwtTokenGateway jwtTokenGateway() {
            return mock(JwtTokenGateway.class);
        }
    }
}