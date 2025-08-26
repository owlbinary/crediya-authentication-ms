package com.crediya.auth.data.mapper;

import com.crediya.auth.data.entity.UsuarioEntity;
import com.crediya.auth.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {UsuarioMapperImpl.class})
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class UsuarioMapperImplTest {

    @Autowired
    private UsuarioMapper usuarioMapper;

    private Usuario usuarioDominio;
    private UsuarioEntity usuarioEntity;
    private LocalDateTime fechaActual;

    @BeforeEach
    void setUp() {
        fechaActual = LocalDateTime.now();
        
        usuarioDominio = Usuario.builder()
                .idUsuario(1L)
                .nombre("Juan")
                .apellido("Perez")
                .email("juan.perez@test.com")
                .password("password123")
                .documentoIdentidad("12345678")
                .telefono("3001234567")
                .direccion("Calle 123 #45-67")
                .idRol(1L)
                .salarioBase(new BigDecimal("5000000"))
                .fechaCreacion(fechaActual)
                .fechaActualizacion(fechaActual)
                .build();

        usuarioEntity = UsuarioEntity.builder()
                .idUsuario(2L)
                .nombre("Maria")
                .apellido("Garcia")
                .email("maria.garcia@test.com")
                .password("password456")
                .documentoIdentidad("87654321")
                .telefono("3009876543")
                .direccion("Carrera 456 #78-90")
                .idRol(2L)
                .salarioBase(new BigDecimal("6000000"))
                .fechaCreacion(fechaActual.minusDays(1))
                .fechaActualizacion(fechaActual.minusDays(1))
                .build();
    }

    @Test
    void deberiaMappearUsuarioDominioAEntidad() {
        UsuarioEntity resultado = usuarioMapper.toEntity(usuarioDominio);

        assertNotNull(resultado);
        assertEquals(usuarioDominio.getIdUsuario(), resultado.getIdUsuario());
        assertEquals(usuarioDominio.getNombre(), resultado.getNombre());
        assertEquals(usuarioDominio.getApellido(), resultado.getApellido());
        assertEquals(usuarioDominio.getEmail(), resultado.getEmail());
        assertEquals(usuarioDominio.getPassword(), resultado.getPassword());
        assertEquals(usuarioDominio.getDocumentoIdentidad(), resultado.getDocumentoIdentidad());
        assertEquals(usuarioDominio.getTelefono(), resultado.getTelefono());
        assertEquals(usuarioDominio.getDireccion(), resultado.getDireccion());
        assertEquals(usuarioDominio.getIdRol(), resultado.getIdRol());
        assertEquals(usuarioDominio.getSalarioBase(), resultado.getSalarioBase());
        assertEquals(usuarioDominio.getFechaCreacion(), resultado.getFechaCreacion());
        assertEquals(usuarioDominio.getFechaActualizacion(), resultado.getFechaActualizacion());
    }

    @Test
    void deberiaMappearUsuarioEntidadADominio() {
        Usuario resultado = usuarioMapper.toDomain(usuarioEntity);

        assertNotNull(resultado);
        assertEquals(usuarioEntity.getIdUsuario(), resultado.getIdUsuario());
        assertEquals(usuarioEntity.getNombre(), resultado.getNombre());
        assertEquals(usuarioEntity.getApellido(), resultado.getApellido());
        assertEquals(usuarioEntity.getEmail(), resultado.getEmail());
        assertEquals(usuarioEntity.getPassword(), resultado.getPassword());
        assertEquals(usuarioEntity.getDocumentoIdentidad(), resultado.getDocumentoIdentidad());
        assertEquals(usuarioEntity.getTelefono(), resultado.getTelefono());
        assertEquals(usuarioEntity.getDireccion(), resultado.getDireccion());
        assertEquals(usuarioEntity.getIdRol(), resultado.getIdRol());
        assertEquals(usuarioEntity.getSalarioBase(), resultado.getSalarioBase());
        assertEquals(usuarioEntity.getFechaCreacion(), resultado.getFechaCreacion());
        assertEquals(usuarioEntity.getFechaActualizacion(), resultado.getFechaActualizacion());
    }

    @Test
    void deberiaRetornarNullCuandoUsuarioDominioEsNull() {
        UsuarioEntity resultado = usuarioMapper.toEntity(null);
        assertNull(resultado);
    }

    @Test
    void deberiaRetornarNullCuandoUsuarioEntidadEsNull() {
        Usuario resultado = usuarioMapper.toDomain(null);
        assertNull(resultado);
    }

    @Test
    void deberiaMappearUsuarioConCamposNulos() {
        Usuario usuarioConNulos = Usuario.builder()
                .idUsuario(null)
                .nombre(null)
                .apellido(null)
                .email("test@test.com")
                .password(null)
                .documentoIdentidad(null)
                .telefono(null)
                .direccion(null)
                .idRol(null)
                .salarioBase(null)
                .fechaCreacion(null)
                .fechaActualizacion(null)
                .build();

        UsuarioEntity resultado = usuarioMapper.toEntity(usuarioConNulos);

        assertNotNull(resultado);
        assertNull(resultado.getIdUsuario());
        assertNull(resultado.getNombre());
        assertNull(resultado.getApellido());
        assertEquals("test@test.com", resultado.getEmail());
        assertNull(resultado.getPassword());
        assertNull(resultado.getDocumentoIdentidad());
        assertNull(resultado.getTelefono());
        assertNull(resultado.getDireccion());
        assertNull(resultado.getIdRol());
        assertNull(resultado.getSalarioBase());
        assertNull(resultado.getFechaCreacion());
        assertNull(resultado.getFechaActualizacion());
    }

    @Test
    void deberiaMappearEntidadConCamposNulos() {
        UsuarioEntity entidadConNulos = UsuarioEntity.builder()
                .idUsuario(null)
                .nombre(null)
                .apellido(null)
                .email("test@test.com")
                .password(null)
                .documentoIdentidad(null)
                .telefono(null)
                .direccion(null)
                .idRol(null)
                .salarioBase(null)
                .fechaCreacion(null)
                .fechaActualizacion(null)
                .build();

        Usuario resultado = usuarioMapper.toDomain(entidadConNulos);

        assertNotNull(resultado);
        assertNull(resultado.getIdUsuario());
        assertNull(resultado.getNombre());
        assertNull(resultado.getApellido());
        assertEquals("test@test.com", resultado.getEmail());
        assertNull(resultado.getPassword());
        assertNull(resultado.getDocumentoIdentidad());
        assertNull(resultado.getTelefono());
        assertNull(resultado.getDireccion());
        assertNull(resultado.getIdRol());
        assertNull(resultado.getSalarioBase());
        assertNull(resultado.getFechaCreacion());
        assertNull(resultado.getFechaActualizacion());
    }

    @Test
    void deberiaMappearCorrectamenteSalarioBaseCeroYNegativo() {
        Usuario usuarioConSalarioCero = Usuario.builder()
                .idUsuario(1L)
                .email("test@test.com")
                .salarioBase(BigDecimal.ZERO)
                .build();

        UsuarioEntity resultado = usuarioMapper.toEntity(usuarioConSalarioCero);

        assertNotNull(resultado);
        assertEquals(BigDecimal.ZERO, resultado.getSalarioBase());
    }

    @Test
    void deberiaMappearCorrectamenteFechasEnElPasadoYFuturo() {
        LocalDateTime fechaPasada = LocalDateTime.of(2020, 1, 1, 10, 0, 0);
        LocalDateTime fechaFutura = LocalDateTime.of(2030, 12, 31, 23, 59, 59);

        Usuario usuarioConFechas = Usuario.builder()
                .idUsuario(1L)
                .email("test@test.com")
                .fechaCreacion(fechaPasada)
                .fechaActualizacion(fechaFutura)
                .build();

        UsuarioEntity resultado = usuarioMapper.toEntity(usuarioConFechas);

        assertNotNull(resultado);
        assertEquals(fechaPasada, resultado.getFechaCreacion());
        assertEquals(fechaFutura, resultado.getFechaActualizacion());
    }

    @Test
    void deberiaSerIdempotenteMappeoIdaYVuelta() {
        UsuarioEntity entidadMapeada = usuarioMapper.toEntity(usuarioDominio);
        Usuario dominioMapeado = usuarioMapper.toDomain(entidadMapeada);

        assertNotNull(dominioMapeado);
        assertEquals(usuarioDominio.getIdUsuario(), dominioMapeado.getIdUsuario());
        assertEquals(usuarioDominio.getNombre(), dominioMapeado.getNombre());
        assertEquals(usuarioDominio.getApellido(), dominioMapeado.getApellido());
        assertEquals(usuarioDominio.getEmail(), dominioMapeado.getEmail());
        assertEquals(usuarioDominio.getPassword(), dominioMapeado.getPassword());
        assertEquals(usuarioDominio.getDocumentoIdentidad(), dominioMapeado.getDocumentoIdentidad());
        assertEquals(usuarioDominio.getTelefono(), dominioMapeado.getTelefono());
        assertEquals(usuarioDominio.getDireccion(), dominioMapeado.getDireccion());
        assertEquals(usuarioDominio.getIdRol(), dominioMapeado.getIdRol());
        assertEquals(usuarioDominio.getSalarioBase(), dominioMapeado.getSalarioBase());
        assertEquals(usuarioDominio.getFechaCreacion(), dominioMapeado.getFechaCreacion());
        assertEquals(usuarioDominio.getFechaActualizacion(), dominioMapeado.getFechaActualizacion());
    }

    @Test
    void deberiaSerIdempotenteMappeoVueltaEIda() {
        Usuario dominioMapeado = usuarioMapper.toDomain(usuarioEntity);
        UsuarioEntity entidadMapeada = usuarioMapper.toEntity(dominioMapeado);

        assertNotNull(entidadMapeada);
        assertEquals(usuarioEntity.getIdUsuario(), entidadMapeada.getIdUsuario());
        assertEquals(usuarioEntity.getNombre(), entidadMapeada.getNombre());
        assertEquals(usuarioEntity.getApellido(), entidadMapeada.getApellido());
        assertEquals(usuarioEntity.getEmail(), entidadMapeada.getEmail());
        assertEquals(usuarioEntity.getPassword(), entidadMapeada.getPassword());
        assertEquals(usuarioEntity.getDocumentoIdentidad(), entidadMapeada.getDocumentoIdentidad());
        assertEquals(usuarioEntity.getTelefono(), entidadMapeada.getTelefono());
        assertEquals(usuarioEntity.getDireccion(), entidadMapeada.getDireccion());
        assertEquals(usuarioEntity.getIdRol(), entidadMapeada.getIdRol());
        assertEquals(usuarioEntity.getSalarioBase(), entidadMapeada.getSalarioBase());
        assertEquals(usuarioEntity.getFechaCreacion(), entidadMapeada.getFechaCreacion());
        assertEquals(usuarioEntity.getFechaActualizacion(), entidadMapeada.getFechaActualizacion());
    }
}
