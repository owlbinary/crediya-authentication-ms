package com.crediya.auth.data.repository;

import com.crediya.auth.data.entity.UsuarioEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UsuarioR2dbcRepository extends ReactiveCrudRepository<UsuarioEntity, Long> {

    Mono<Boolean> existsByEmail(String email);

    Mono<Boolean> existsByDocumentoIdentidad(String documentoIdentidad);

    Mono<UsuarioEntity> findByEmail(String email);

    Mono<UsuarioEntity> findByDocumentoIdentidad(String documentoIdentidad);
}
