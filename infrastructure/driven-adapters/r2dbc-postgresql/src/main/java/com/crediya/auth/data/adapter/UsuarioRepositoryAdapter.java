package com.crediya.auth.data.adapter;

import com.crediya.auth.data.mapper.UsuarioMapper;
import com.crediya.auth.data.repository.UsuarioR2dbcRepository;
import com.crediya.auth.model.Usuario;
import com.crediya.auth.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UsuarioRepositoryAdapter implements UsuarioGateway {

    private final UsuarioR2dbcRepository usuarioR2dbcRepository;
    private final UsuarioMapper usuarioMapper;
    private final TransactionalOperator operadorTransaccional;
    @Override
    public Mono<Usuario> guardar(Usuario usuario) {
        log.debug("Iniciando guardar usuario: {}", usuario.getEmail());

        return Mono.just(usuario)
            .map(usuarioMapper::toEntity)
            .flatMap(entidad -> {
                log.debug("Persistiendo entidad: {}", entidad.getEmail());
                return usuarioR2dbcRepository.save(entidad);
            })
            .map(usuarioMapper::toDomain)
            .doOnSuccess(resultado -> 
                log.info("Usuario guardado exitosamente: {}", resultado.getEmail())
            )
            .doOnError(excepcion -> 
                log.error("Error al guardar usuario: {}", excepcion.getMessage(), excepcion)
            )
            .as(operadorTransaccional::transactional);
    }

    @Override
    public Mono<Boolean> existePorEmail(String email) {
        log.debug("Verificando existencia de usuario por email: {}", email);

        return usuarioR2dbcRepository.existsByEmail(email)
            .doOnNext(existe -> {
                String estado = Boolean.TRUE.equals(existe) ? "existe" : "no existe";
                log.debug("Usuario con email {}: {}", email, estado);
            })
            .doOnError(excepcion -> 
                log.error("Error en consulta de usuario por email {}: {}", 
                    email, excepcion.getMessage())
            );
    }

    @Override
    public Mono<Boolean> existePorDocumentoIdentidad(String documentoIdentidad) {
        log.debug("Verificando existencia de usuario por documento de identidad: {}", documentoIdentidad);

        return usuarioR2dbcRepository.existsByDocumentoIdentidad(documentoIdentidad)
            .doOnNext(existe -> {
                String estado = Boolean.TRUE.equals(existe) ? "existe" : "no existe";
                log.debug("Usuario con documento {}: {}", documentoIdentidad, estado);
            })
            .doOnError(excepcion -> 
                log.error("Error en consulta de usuario por documento {}: {}", 
                    documentoIdentidad, excepcion.getMessage())
            );
    }

    @Override
    public Mono<Usuario> buscarPorEmail(String email) {
        log.debug("Consultando usuario por email: {}", email);
        
        return usuarioR2dbcRepository.findByEmail(email)
            .map(usuarioMapper::toDomain)
            .doOnNext(usuario -> log.debug("Usuario: {}", usuario.getEmail()))
            .doOnError(excepcion -> 
                log.error("Error en consulta por email {}: {}", email, excepcion.getMessage())
            );
    }

    @Override
    public Mono<Usuario> buscarPorDocumentoIdentidad(String documentoIdentidad) {
        log.debug("Consultando usuario por documento de identidad: {}", documentoIdentidad);
        
        return usuarioR2dbcRepository.findByDocumentoIdentidad(documentoIdentidad)
            .map(usuarioMapper::toDomain)
            .doOnNext(usuario -> log.debug("Usuario encontrado: {}", usuario.getEmail()))
            .doOnError(excepcion -> 
                log.error("Error en consulta por documento {}: {}", documentoIdentidad, excepcion.getMessage())
            );
    }
}
