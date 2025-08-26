package com.crediya.auth.api.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderConfigTest {

    private PasswordEncoderConfig passwordEncoderConfig;

    @BeforeEach
    void setUp() {
        passwordEncoderConfig = new PasswordEncoderConfig();
    }

    @Test
    void deberiaCrearPasswordEncoderBean() {
        PasswordEncoder passwordEncoder = passwordEncoderConfig.passwordEncoder();

        assertNotNull(passwordEncoder);
        assertInstanceOf(BCryptPasswordEncoder.class, passwordEncoder);
    }

    @Test
    void deberiaEncriptarPasswordCorrectamente() {
        PasswordEncoder passwordEncoder = passwordEncoderConfig.passwordEncoder();
        String rawPassword = "testPassword123";

        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }

    @Test
    void deberiaGenerarHashesDiferentesParaLaMismaPassword() {
        PasswordEncoder passwordEncoder = passwordEncoderConfig.passwordEncoder();
        String rawPassword = "testPassword123";

        String encodedPassword1 = passwordEncoder.encode(rawPassword);
        String encodedPassword2 = passwordEncoder.encode(rawPassword);

        assertNotEquals(encodedPassword1, encodedPassword2);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword1));
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword2));
    }

    @Test
    void deberiaRetornarFalseParaPasswordIncorrecta() {
        PasswordEncoder passwordEncoder = passwordEncoderConfig.passwordEncoder();
        String rawPassword = "testPassword123";
        String wrongPassword = "wrongPassword";

        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertFalse(passwordEncoder.matches(wrongPassword, encodedPassword));
    }
}
