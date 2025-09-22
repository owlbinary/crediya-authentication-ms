package com.crediya.auth.api.security;

import com.crediya.auth.model.UsuarioAutenticado;
import com.crediya.auth.usecase.ValidarTokenUseCase;

import io.micrometer.common.lang.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {
    
    private final ValidarTokenUseCase validarTokenUseCase;
    
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTH_HEADER = HttpHeaders.AUTHORIZATION;
    
    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        
        if (isPublicEndpoint(path)) {
            return chain.filter(exchange);
        }
        
        return extractToken(exchange)
                .flatMap(this::authenticateToken)
                .flatMap(authentication -> {
                    SecurityContext securityContext = new SecurityContextImpl(authentication);
                    return chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
                })
                .switchIfEmpty(chain.filter(exchange));
    }
    
    private Mono<String> extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(AUTH_HEADER);
        
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            String token = authHeader.substring(BEARER_PREFIX.length());
            return Mono.just(token);
        }
        
        return Mono.empty();
    }
    
    private Mono<UsernamePasswordAuthenticationToken> authenticateToken(String token) {
        return validarTokenUseCase.ejecutar(token)
                .map(this::createAuthentication)
                .doOnError(error -> 
                    log.debug("Error al validar token: {}", error.getMessage()))
                .onErrorResume(error -> {
                    log.debug("Token inválido o expirado, continuando sin autenticación");
                    return Mono.empty();
                });
    }
    
    private UsernamePasswordAuthenticationToken createAuthentication(UsuarioAutenticado usuario) {
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + getRoleName(usuario.getIdRol()))
        );
        
        return new UsernamePasswordAuthenticationToken(
                usuario, 
                null, 
                authorities
        );
    }
    
    private String getRoleName(Long idRol) {
        return switch (idRol.intValue()) {
            case 1 -> "ADMIN";
            case 2 -> "ASESOR";
            case 3 -> "CLIENTE";
            default -> "USER";
        };
    }
    
    private boolean isPublicEndpoint(String path) {
        return path.equals("/api/v1/login") ||
               path.startsWith("/actuator") ||
               path.startsWith("/auth/actuator") ||
               path.startsWith("/swagger-ui") ||
               path.startsWith("/auth/swagger-ui") ||
               path.startsWith("/v3/api-docs") ||
               path.startsWith("/auth/v3/api-docs") ||
               path.startsWith("/webjars") ||
               path.startsWith("/auth/webjars") ||
               path.equals("/swagger-ui.html") ||
               path.equals("/auth/swagger-ui.html");
    }
}
