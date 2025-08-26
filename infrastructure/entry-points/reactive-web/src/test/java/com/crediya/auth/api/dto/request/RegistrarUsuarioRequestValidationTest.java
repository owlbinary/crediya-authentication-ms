package com.crediya.auth.api.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RegistrarUsuarioRequestValidationTest {

        private Validator validator;

        @BeforeEach
        void setUp() {
                ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
                validator = factory.getValidator();
        }

        @Test
        void deberiaValidarRequestValido() {

                RegistrarUsuarioRequest request = RegistrarUsuarioRequest.builder()
                                .nombre("Juan")
                                .apellido("Sierra")
                                .email("juan@test.com")
                                .password("password123")
                                .documentoIdentidad("1234567890")
                                .telefono("3001234567")
                                .idRol(1L)
                                .salarioBase(new BigDecimal("2500000"))
                                .build();

                Set<ConstraintViolation<RegistrarUsuarioRequest>> violations = validator.validate(request);

                assertTrue(violations.isEmpty(), "OK");
        }

        @Test
        void deberiaFallarCuandoNombreEsVacio() {

                RegistrarUsuarioRequest request = crearRequestValido().toBuilder()
                                .nombre("")
                                .build();

                Set<ConstraintViolation<RegistrarUsuarioRequest>> violations = validator.validate(request);

                assertFalse(violations.isEmpty());
                assertTrue(violations.stream()
                                .anyMatch(v -> v.getMessage().contains("nombre")
                                                && v.getMessage().contains("obligatorio")));
        }

        @Test
        void deberiaFallarCuandoEmailTieneFormatoInvalido() {

                RegistrarUsuarioRequest request = crearRequestValido().toBuilder()
                                .email("email-invalido")
                                .build();

                Set<ConstraintViolation<RegistrarUsuarioRequest>> violations = validator.validate(request);

                assertFalse(violations.isEmpty());
                assertTrue(violations.stream()
                                .anyMatch(v -> v.getMessage().contains("formato") && v.getMessage().contains("email")));
        }

        @Test
        void deberiaFallarCuandoDocumentoIdentidadEsInvalido() {

                RegistrarUsuarioRequest request = crearRequestValido().toBuilder()
                                .documentoIdentidad("123")
                                .build();

                Set<ConstraintViolation<RegistrarUsuarioRequest>> violations = validator.validate(request);

                assertFalse(violations.isEmpty());
                assertTrue(violations.stream()
                                .anyMatch(v -> v.getMessage().contains("documento de identidad")));
        }

        @Test
        void deberiaFallarCuandoDocumentoIdentidadContieneLetras() {

                RegistrarUsuarioRequest request = crearRequestValido().toBuilder()
                                .documentoIdentidad("12345ABC6789")
                                .build();

                Set<ConstraintViolation<RegistrarUsuarioRequest>> violations = validator.validate(request);

                assertFalse(violations.isEmpty());
                assertTrue(violations.stream()
                                .anyMatch(v -> v.getMessage().contains("documento de identidad")));
        }

        @Test
        void deberiaFallarCuandoSalarioBaseEsNulo() {

                RegistrarUsuarioRequest request = crearRequestValido().toBuilder()
                                .salarioBase(null)
                                .build();

                Set<ConstraintViolation<RegistrarUsuarioRequest>> violations = validator.validate(request);

                assertFalse(violations.isEmpty());
                assertTrue(violations.stream()
                                .anyMatch(v -> v.getMessage().contains("salario_base")
                                                && v.getMessage().contains("obligatorio")));
        }

        @Test
        void deberiaPermitirSalarioBaseValido() {

                RegistrarUsuarioRequest request = crearRequestValido().toBuilder()
                                .salarioBase(new BigDecimal("15000001"))
                                .build();

                Set<ConstraintViolation<RegistrarUsuarioRequest>> violations = validator.validate(request);

                assertTrue(violations.isEmpty());
        }

        @Test
        void deberiaPermitirTelefonoNulo() {

                RegistrarUsuarioRequest request = crearRequestValido().toBuilder()
                                .telefono(null)
                                .build();

                Set<ConstraintViolation<RegistrarUsuarioRequest>> violations = validator.validate(request);

                assertTrue(violations.isEmpty(), "Teléfono nulo debería ser válido");
        }

        @Test
        void deberiaFallarCuandoPasswordEsVacio() {
                RegistrarUsuarioRequest request = crearRequestValido().toBuilder()
                                .password("")
                                .build();

                Set<ConstraintViolation<RegistrarUsuarioRequest>> violations = validator.validate(request);
                assertFalse(violations.isEmpty());
                assertTrue(violations.stream()
                                .anyMatch(v -> v.getMessage().contains("contraseña")
                                                && v.getMessage().contains("obligatoria")));
        }

        @Test
        void deberiaFallarCuandoPasswordEsNulo() {
                RegistrarUsuarioRequest request = crearRequestValido().toBuilder()
                                .password(null)
                                .build();

                Set<ConstraintViolation<RegistrarUsuarioRequest>> violations = validator.validate(request);

                assertFalse(violations.isEmpty());
                assertTrue(violations.stream()
                                .anyMatch(v -> v.getMessage().contains("contraseña")
                                                && v.getMessage().contains("obligatoria")));
        }

        @Test
        void deberiaFallarCuandoPasswordEsMuyCorto() {
                RegistrarUsuarioRequest request = crearRequestValido().toBuilder()
                                .password("123")
                                .build();

                Set<ConstraintViolation<RegistrarUsuarioRequest>> violations = validator.validate(request);
                assertFalse(violations.isEmpty());
                assertTrue(violations.stream()
                                .anyMatch(v -> v.getMessage()
                                                .contains("contraseña debe tener entre 6 y 100 caracteres")));
        }

        private RegistrarUsuarioRequest crearRequestValido() {
                return RegistrarUsuarioRequest.builder()
                                .nombre("Juan")
                                .apellido("Sierra")
                                .email("juan@test.com")
                                .password("password123")
                                .documentoIdentidad("1234567890")
                                .telefono("3001234567")
                                .idRol(1L)
                                .salarioBase(new BigDecimal("2500000"))
                                .build();
        }
}
