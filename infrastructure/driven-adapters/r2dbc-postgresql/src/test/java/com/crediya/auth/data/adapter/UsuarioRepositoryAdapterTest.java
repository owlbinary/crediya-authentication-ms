package com.crediya.auth.data.adapter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.reactive.TransactionalOperator;

import com.crediya.auth.data.entity.UsuarioEntity;
import com.crediya.auth.data.mapper.UsuarioMapper;
import com.crediya.auth.data.repository.UsuarioR2dbcRepository;
import com.crediya.auth.model.Usuario;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class UsuarioRepositoryAdapterTest {

    @Mock
    private UsuarioR2dbcRepository usuarioR2dbcRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private TransactionalOperator operadorTransaccional;

    private UsuarioRepositoryAdapter usuarioRepositoryAdapter;

    @BeforeEach
    void setUp() {
        usuarioRepositoryAdapter = new UsuarioRepositoryAdapter(
                usuarioR2dbcRepository, 
                usuarioMapper, 
                operadorTransaccional
        );
    }

        @Test
    @SuppressWarnings("unchecked")
    void deberiaGuardarUsuarioCorrectamente() {
        Usuario usuario = Usuario.builder()
                .email("test@test.com")
                .nombre("Test User")
                .password("password123")
                .build();

        UsuarioEntity entidad = UsuarioEntity.builder()
                .email("test@test.com")
                .nombre("Test User")
                .password("password123")
                .build();
        
        UsuarioEntity entidadGuardada = UsuarioEntity.builder()
                .idUsuario(1L)
                .email("test@test.com")
                .nombre("Test User")
                .password("password123")
                .build();
        
        Usuario usuarioGuardado = Usuario.builder()
                .idUsuario(1L)
                .email("test@test.com")
                .nombre("Test User")
                .password("password123")
                .build();

        when(usuarioMapper.toEntity(any(Usuario.class))).thenReturn(entidad);
        when(usuarioR2dbcRepository.save(any(UsuarioEntity.class))).thenReturn(Mono.just(entidadGuardada));
        when(usuarioMapper.toDomain(any(UsuarioEntity.class))).thenReturn(usuarioGuardado);
        
        doAnswer(invocation -> invocation.getArgument(0))
            .when(operadorTransaccional).transactional(any(Mono.class));

        Mono<Usuario> result = usuarioRepositoryAdapter.guardar(usuario);

        StepVerifier.create(result)
                .expectNext(usuarioGuardado)
                .verifyComplete();

        verify(usuarioMapper, times(1)).toEntity(any(Usuario.class));
        verify(usuarioR2dbcRepository, times(1)).save(any(UsuarioEntity.class));
        verify(usuarioMapper, times(1)).toDomain(any(UsuarioEntity.class));
    }

    @Test
    void deberiaVerificarExistenciaPorEmailCorrectamente() {
        String email = "test@test.com";
        when(usuarioR2dbcRepository.existsByEmail(email)).thenReturn(Mono.just(true));

        Mono<Boolean> result = usuarioRepositoryAdapter.existePorEmail(email);

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        verify(usuarioR2dbcRepository).existsByEmail(email);
    }

    @Test
    void deberiaRetornarFalsoCuandoUsuarioNoExiste() {
        String email = "noexiste@test.com";
        when(usuarioR2dbcRepository.existsByEmail(email)).thenReturn(Mono.just(false));

        Mono<Boolean> result = usuarioRepositoryAdapter.existePorEmail(email);

        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();

        verify(usuarioR2dbcRepository).existsByEmail(email);
    }

    @Test
    void deberiaVerificarExistenciaPorDocumentoCorrectamente() {
        String documento = "1234567890";
        when(usuarioR2dbcRepository.existsByDocumentoIdentidad(documento)).thenReturn(Mono.just(true));

        Mono<Boolean> result = usuarioRepositoryAdapter.existePorDocumentoIdentidad(documento);

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        verify(usuarioR2dbcRepository).existsByDocumentoIdentidad(documento);
    }

    @Test
    void deberiaRetornarFalsoCuandoDocumentoNoExiste() {
        String documento = "9876543210";
        when(usuarioR2dbcRepository.existsByDocumentoIdentidad(documento)).thenReturn(Mono.just(false));

        Mono<Boolean> result = usuarioRepositoryAdapter.existePorDocumentoIdentidad(documento);

        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();

        verify(usuarioR2dbcRepository).existsByDocumentoIdentidad(documento);
    }

    @Test
    void deberiaBuscarUsuarioPorEmailCorrectamente() {
        String email = "test@test.com";
        UsuarioEntity entidad = crearUsuarioEntityTest();
        Usuario usuario = crearUsuarioTest();

        when(usuarioR2dbcRepository.findByEmail(email)).thenReturn(Mono.just(entidad));
        when(usuarioMapper.toDomain(entidad)).thenReturn(usuario);

        Mono<Usuario> result = usuarioRepositoryAdapter.buscarPorEmail(email);

        StepVerifier.create(result)
                .expectNext(usuario)
                .verifyComplete();

        verify(usuarioR2dbcRepository).findByEmail(email);
        verify(usuarioMapper).toDomain(entidad);
    }

    @Test
    void deberiaRetornarMonoVacioCuandoUsuarioNoSeEncuentra() {
        String email = "noexiste@test.com";
        when(usuarioR2dbcRepository.findByEmail(email)).thenReturn(Mono.empty());

        Mono<Usuario> result = usuarioRepositoryAdapter.buscarPorEmail(email);

        StepVerifier.create(result)
                .verifyComplete();

        verify(usuarioR2dbcRepository).findByEmail(email);
        verifyNoInteractions(usuarioMapper);
    }

    @Test
    @SuppressWarnings("unchecked")
    void deberiaControlarErrorEnGuardarUsuario() {
        Usuario usuario = Usuario.builder()
                .email("test@test.com")
                .nombre("Test User")
                .build();

        UsuarioEntity entidad = UsuarioEntity.builder()
                .email("test@test.com")
                .nombre("Test User")
                .build();

        when(usuarioMapper.toEntity(any(Usuario.class))).thenReturn(entidad);
        when(usuarioR2dbcRepository.save(any(UsuarioEntity.class))).thenReturn(Mono.error(new RuntimeException("Error de base de datos")));
        
        doAnswer(invocation -> invocation.getArgument(0))
            .when(operadorTransaccional).transactional(any(Mono.class));

        Mono<Usuario> result = usuarioRepositoryAdapter.guardar(usuario);

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(usuarioMapper, times(1)).toEntity(any(Usuario.class));
        verify(usuarioR2dbcRepository, times(1)).save(any(UsuarioEntity.class));
    }

    @Test
    void deberiaControlarErrorEnExistePorEmail() {
        String email = "test@test.com";
        RuntimeException excepcion = new RuntimeException("Error de conexión");

        when(usuarioR2dbcRepository.existsByEmail(email)).thenReturn(Mono.error(excepcion));

        Mono<Boolean> result = usuarioRepositoryAdapter.existePorEmail(email);

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(usuarioR2dbcRepository).existsByEmail(email);
    }

    @Test
    void deberiaControlarErrorEnBuscarPorEmail() {
        String email = "test@test.com";
        RuntimeException excepcion = new RuntimeException("Error de consulta");

        when(usuarioR2dbcRepository.findByEmail(email)).thenReturn(Mono.error(excepcion));

        Mono<Usuario> result = usuarioRepositoryAdapter.buscarPorEmail(email);

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(usuarioR2dbcRepository).findByEmail(email);
    }

    @Test
    void deberiaControlarErrorEnExistePorDocumento() {
        String documento = "1234567890";
        RuntimeException excepcion = new RuntimeException("Error de conexión");

        when(usuarioR2dbcRepository.existsByDocumentoIdentidad(documento)).thenReturn(Mono.error(excepcion));

        Mono<Boolean> result = usuarioRepositoryAdapter.existePorDocumentoIdentidad(documento);

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(usuarioR2dbcRepository).existsByDocumentoIdentidad(documento);
    }

    private Usuario crearUsuarioTest() {
        return Usuario.builder()
                .nombre("Test")
                .apellido("Usuario")
                .email("test@test.com")
                .password("password123")
                .documentoIdentidad("12345678")
                .telefono("3001234567")
                .direccion("Calle Test")
                .idRol(1L)
                .salarioBase(new BigDecimal("2500000"))
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();
    }

    private UsuarioEntity crearUsuarioEntityTest() {
        UsuarioEntity entidad = new UsuarioEntity();
        entidad.setNombre("Test");
        entidad.setApellido("Usuario");
        entidad.setEmail("test@test.com");
        entidad.setPassword("password123");
        entidad.setDocumentoIdentidad("12345678");
        entidad.setTelefono("3001234567");
        entidad.setDireccion("Calle Test");
        entidad.setIdRol(1L);
        entidad.setSalarioBase(new BigDecimal("2500000"));
        entidad.setFechaCreacion(LocalDateTime.now());
        entidad.setFechaActualizacion(LocalDateTime.now());
        return entidad;
    }
}
