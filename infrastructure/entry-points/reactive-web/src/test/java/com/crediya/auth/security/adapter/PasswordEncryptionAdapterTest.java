package com.crediya.auth.security.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordEncryptionAdapterTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    private PasswordEncryptionAdapter passwordEncryptionAdapter;

    @BeforeEach
    void setUp() {
        passwordEncryptionAdapter = new PasswordEncryptionAdapter(passwordEncoder);
    }

    @Test
    void deberiaEncriptarPasswordCorrectamente() {
        String passwordPlano = "miPassword123";
        String passwordEncriptado = "$2a$10$encrypted";
        when(passwordEncoder.encode(passwordPlano)).thenReturn(passwordEncriptado);

        Mono<String> result = passwordEncryptionAdapter.encriptarPassword(passwordPlano);

        StepVerifier.create(result)
                .expectNext(passwordEncriptado)
                .verifyComplete();

        verify(passwordEncoder).encode(passwordPlano);
    }

    @Test
    void deberiaEncriptarPasswordVacio() {
        String passwordVacio = "";
        String passwordEncriptado = "$2a$10$empty";
        when(passwordEncoder.encode(passwordVacio)).thenReturn(passwordEncriptado);

        Mono<String> result = passwordEncryptionAdapter.encriptarPassword(passwordVacio);

        StepVerifier.create(result)
                .expectNext(passwordEncriptado)
                .verifyComplete();

        verify(passwordEncoder).encode(passwordVacio);
    }

    @Test
    void deberiaValidarPasswordCorrectoCuandoCoinciden() {
        String passwordPlano = "miPassword123";
        String passwordEncriptado = "$2a$10$encrypted";
        when(passwordEncoder.matches(passwordPlano, passwordEncriptado)).thenReturn(true);

        Mono<Boolean> result = passwordEncryptionAdapter.validarPassword(passwordPlano, passwordEncriptado);

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        verify(passwordEncoder).matches(passwordPlano, passwordEncriptado);
    }

    @Test
    void deberiaValidarPasswordIncorrectoCuandoNoCoinciden() {
        String passwordPlano = "passwordIncorrecto";
        String passwordEncriptado = "$2a$10$encrypted";
        when(passwordEncoder.matches(passwordPlano, passwordEncriptado)).thenReturn(false);

        Mono<Boolean> result = passwordEncryptionAdapter.validarPassword(passwordPlano, passwordEncriptado);

        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();

        verify(passwordEncoder).matches(passwordPlano, passwordEncriptado);
    }

    @Test
    void deberiaValidarPasswordConValoresNulos() {
        String passwordPlano = null;
        String passwordEncriptado = "$2a$10$encrypted";
        when(passwordEncoder.matches(passwordPlano, passwordEncriptado)).thenReturn(false);

        Mono<Boolean> result = passwordEncryptionAdapter.validarPassword(passwordPlano, passwordEncriptado);

        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();

        verify(passwordEncoder).matches(passwordPlano, passwordEncriptado);
    }

    @Test
    void deberiaEncriptarPasswordsConCaracteresEspeciales() {
        String passwordComplejo = "P@ssw0rd!#$%^&*()";
        String passwordEncriptado = "$2a$10$complex";
        when(passwordEncoder.encode(passwordComplejo)).thenReturn(passwordEncriptado);

        Mono<String> result = passwordEncryptionAdapter.encriptarPassword(passwordComplejo);

        StepVerifier.create(result)
                .expectNext(passwordEncriptado)
                .verifyComplete();

        verify(passwordEncoder).encode(passwordComplejo);
    }
}
