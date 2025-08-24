package com.crediya.auth.api.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@WebFluxTest(excludeAutoConfiguration = {
    org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration.class,
    org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration.class
})
@AutoConfigureWebTestClient
@ContextConfiguration(classes = {CorsConfig.class, SecurityHeadersConfig.class})
@TestPropertySource(properties = {
    "cors.allowed-origins=http://localhost:3000,http://localhost:8080",
    "spring.security.enabled=false"
})
class ConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired(required = false)
    private CorsWebFilter corsWebFilter;

    @Test
    void corsConfigurationShouldBeConfigured() {
        assertThat(corsWebFilter).isNotNull();
    }

    @Test 
    void securityHeadersShouldBePresent() {
        webTestClient
                .get()
                .uri("/any-path")
                .exchange()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }

    @Test
    void corsFilterShouldHandlePreflightRequests() {
        webTestClient
                .options()
                .uri("/test")
                .header("Origin", "http://localhost:3000")
                .header("Access-Control-Request-Method", "GET")
                .exchange()
                .expectHeader().exists("Vary");
    }

}