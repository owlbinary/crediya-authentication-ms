package com.crediya.auth.api.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.cors.reactive.CorsWebFilter;

import static org.junit.jupiter.api.Assertions.*;

class CorsConfigTest {

    private CorsConfig corsConfig;

    @BeforeEach
    void setUp() {
        corsConfig = new CorsConfig();
    }

    @Test
    void deberiaCrearCorsWebFilterConUnOrigen() {
        String origins = "http://localhost:3000";

        CorsWebFilter corsWebFilter = corsConfig.corsWebFilter(origins);

        assertNotNull(corsWebFilter);
    }

    @Test
    void deberiaCrearCorsWebFilterConMultiplesOrigenes() {
        String origins = "http://localhost:3000,http://localhost:4200,https://crediya.com";

        CorsWebFilter corsWebFilter = corsConfig.corsWebFilter(origins);

        assertNotNull(corsWebFilter);
    }

    @Test
    void deberiaManejarOrigenVacio() {
        String origins = "";

        assertDoesNotThrow(() -> {
            CorsWebFilter corsWebFilter = corsConfig.corsWebFilter(origins);
            assertNotNull(corsWebFilter);
        });
    }

    @Test
    void deberiaManejarOrigenNulo() {
        assertThrows(NullPointerException.class, () -> {
            corsConfig.corsWebFilter(null);
        });
    }
}
