package com.crediya.auth.usecase;

import com.crediya.auth.model.Usuario;
import com.crediya.auth.model.exception.DatosInvalidosException;
import com.crediya.auth.model.exception.DocumentoYaExisteException;
import com.crediya.auth.model.exception.UsuarioYaExisteException;
import com.crediya.auth.model.gateway.PasswordEncryptionGateway;
import com.crediya.auth.model.gateway.UsuarioGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarUsuarioUseCaseTest {

    @Mock
    private UsuarioGateway usuarioRepository;
    
    @Mock
    private PasswordEncryptionGateway passwordEncryptionGateway;

    private RegistrarUsuarioUseCase registrarUsuarioUseCase;

    @BeforeEach
    void setUp() {
        registrarUsuarioUseCase = new RegistrarUsuarioUseCase(usuarioRepository, passwordEncryptionGateway);
    }

    @Test
    void deberiaRegistrarUsuarioExitosamente() {
        Usuario usuario = crearUsuarioValido();
        Usuario usuarioGuardado = usuario.toBuilder()
                .idUsuario(1L)
                .password("$2a$10$password1234")
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        when(usuarioRepository.existePorEmail(anyString()))
                .thenReturn(Mono.just(false));
        when(usuarioRepository.existePorDocumentoIdentidad(anyString()))
                .thenReturn(Mono.just(false));
        when(passwordEncryptionGateway.encriptarPassword(anyString()))
                .thenReturn(Mono.just("$2a$10$password1234"));
        when(usuarioRepository.guardar(any(Usuario.class)))
                .thenReturn(Mono.just(usuarioGuardado));

        StepVerifier.create(registrarUsuarioUseCase.ejecutar(usuario))
                .expectNextMatches(resultado ->
                    resultado.getIdUsuario() != null &&
                    resultado.getEmail().equals("test@test.com") &&
                    resultado.getFechaCreacion() != null)
                .verifyComplete();
    }

    @Test
    void deberiaFallarCuandoUsuarioYaExiste() {
        Usuario usuario = crearUsuarioValido();

        when(usuarioRepository.existePorEmail(anyString()))
                .thenReturn(Mono.just(true));
        when(usuarioRepository.existePorDocumentoIdentidad(anyString()))
                .thenReturn(Mono.just(false));
        when(passwordEncryptionGateway.encriptarPassword(anyString()))
                .thenReturn(Mono.just("$2a$10$password1234"));
        StepVerifier.create(registrarUsuarioUseCase.ejecutar(usuario))
                .expectError(UsuarioYaExisteException.class)
                .verify();
    }

    @Test
    void deberiaAgregarFechasDeCreacionYActualizacion() {
        Usuario usuario = crearUsuarioValido();
        LocalDateTime ahora = LocalDateTime.now();
        Usuario usuarioGuardado = usuario.toBuilder()
                .idUsuario(1L)
                .password("$2a$10$password1234")
                .fechaCreacion(ahora)
                .fechaActualizacion(ahora)
                .build();

        when(usuarioRepository.existePorEmail(anyString()))
                .thenReturn(Mono.just(false));
        when(usuarioRepository.existePorDocumentoIdentidad(anyString()))
                .thenReturn(Mono.just(false));
        when(passwordEncryptionGateway.encriptarPassword(anyString()))
                .thenReturn(Mono.just("$2a$10$password1234"));
        when(usuarioRepository.guardar(any(Usuario.class)))
                .thenReturn(Mono.just(usuarioGuardado));

        StepVerifier.create(registrarUsuarioUseCase.ejecutar(usuario))
                .expectNextMatches(resultado ->
                    resultado.getFechaCreacion() != null &&
                    resultado.getFechaActualizacion() != null)
                .verifyComplete();
    }

    @Test
    void deberiaFallarCuandoDocumentoYaExiste() {
        Usuario usuario = crearUsuarioValido();

        when(usuarioRepository.existePorEmail(anyString()))
                .thenReturn(Mono.just(false));
        when(usuarioRepository.existePorDocumentoIdentidad(anyString()))
                .thenReturn(Mono.just(true));
        when(passwordEncryptionGateway.encriptarPassword(anyString()))
                .thenReturn(Mono.just("$2a$10$password1234"));
        
        StepVerifier.create(registrarUsuarioUseCase.ejecutar(usuario))
                .expectError(DocumentoYaExisteException.class)
                .verify();
    }

    @Test
    void deberiaLlamarValidacionDelDominioYPropagErrorSiOcurre() {
        Usuario usuarioConValidacionQueFalla = Usuario.builder()
                .nombre("Juan")
                .apellido("Sierra")
                .email("test@test.com")
                .password("password123")
                .documentoIdentidad("12345678")
                .telefono("3001234567")
                .idRol(1L)
                .salarioBase(new BigDecimal("20000000"))
                .build();

        StepVerifier.create(registrarUsuarioUseCase.ejecutar(usuarioConValidacionQueFalla))
                .expectError(DatosInvalidosException.class)
                .verify();
    }

    private Usuario crearUsuarioValido() {
        return Usuario.builder()
                .nombre("Juan")
                .apellido("Sierra")
                .email("test@test.com")
                .password("password123")
                .documentoIdentidad("12345678")
                .telefono("3001234567")
                .idRol(1L)
                .salarioBase(new BigDecimal("2500000"))
                .build();
    }
}
