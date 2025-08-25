package com.crediya.auth.jwt.adapter;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.crediya.auth.model.Usuario;
import com.crediya.auth.model.UsuarioAutenticado;
import com.crediya.auth.model.exception.TokenInvalidoException;
import com.crediya.auth.model.gateway.JwtTokenGateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class JwtTokenAdapter implements JwtTokenGateway {
    
    private final SecretKey secretKey;
    private final long jwtExpirationMs;
    
    public JwtTokenAdapter(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms:86400000}") long jwtExpirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.jwtExpirationMs = jwtExpirationMs;
    }
    
    @Override
    public Mono<String> generarToken(Usuario usuario) {
        return Mono.fromCallable(() -> {
            Date fechaExpiracion = new Date(System.currentTimeMillis() + jwtExpirationMs);
            
            return Jwts.builder()
                    .subject(usuario.getEmail())
                    .claim("userId", usuario.getIdUsuario())
                    .claim("nombre", usuario.getNombre())
                    .claim("apellido", usuario.getApellido())
                    .claim("idRol", usuario.getIdRol())
                    .issuedAt(new Date())
                    .expiration(fechaExpiracion)
                    .signWith(secretKey)
                    .compact();
        }).subscribeOn(reactor.core.scheduler.Schedulers.boundedElastic());
    }
    
    @Override
    public Mono<UsuarioAutenticado> validarToken(String token) {
        return Mono.fromCallable(() -> {
            try {
                Claims claims = extraerClaims(token);
                
                return UsuarioAutenticado.builder()
                        .idUsuario(claims.get("userId", Long.class))
                        .email(claims.getSubject())
                        .nombre(claims.get("nombre", String.class))
                        .apellido(claims.get("apellido", String.class))
                        .idRol(claims.get("idRol", Long.class))
                        .permisos(List.of())
                        .build();
            } catch (JwtException | IllegalArgumentException e) {
                log.warn("Token inválido: {}", e.getMessage());
                throw new TokenInvalidoException("Token inválido o expirado");
            }
        }).subscribeOn(reactor.core.scheduler.Schedulers.boundedElastic());
    }
    
    @Override
    public Mono<String> extraerEmailDelToken(String token) {
        return Mono.fromCallable(() -> {
            try {
                Claims claims = extraerClaims(token);
                return claims.getSubject();
            } catch (JwtException | IllegalArgumentException e) {
                log.warn("No se pudo extraer el email del token: {}", e.getMessage());
                throw new TokenInvalidoException("Token inválido");
            }
        }).subscribeOn(reactor.core.scheduler.Schedulers.boundedElastic());
    }
    
    private Claims extraerClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
