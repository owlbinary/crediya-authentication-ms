package com.crediya.auth.api.reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

class ReactivePatternsTest {

    @Test
    void deberiaExecutarOperacionesNoBloqueantes() {
        Mono<String> operacionAsincrona = Mono.fromCallable(() -> {
            return "Operación completada";
        }).delayElement(Duration.ofMillis(100));

        StepVerifier.create(operacionAsincrona)
                .expectNext("Operación completada")
                .verifyComplete();
    }

    @Test
    void deberiaManejarBackpressureCorrectamente() {
        Flux<Integer> streamConBackpressure = Flux.range(1, 100)
                .delayElements(Duration.ofMillis(10))
                .onBackpressureBuffer(10);

        StepVerifier.create(streamConBackpressure.take(5))
                .expectNext(1, 2, 3, 4, 5)
                .verifyComplete();
    }

    @Test
    void deberiaPropagrarErroresCorrectamente() {
        Mono<String> operacionConError = Mono.fromCallable(() -> {
            throw new RuntimeException("Error simulado");
        });

        StepVerifier.create(operacionConError)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void deberiaRespetarTimeouts() {
        Mono<String> operacionLenta = Mono.just("Resultado")
                .delayElement(Duration.ofSeconds(2))
                .timeout(Duration.ofSeconds(1));

        StepVerifier.create(operacionLenta)
                .expectError()
                .verify();
    }

    @Test
    void deberiaComponerOperacionesReactivasCorrectamente() {
        Mono<String> verificacion = Mono.just("email@test.com")
                .filter(email -> email.contains("@"))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Email inválido")));

        Mono<String> procesamiento = verificacion
                .map(email -> "Usuario: " + email)
                .flatMap(usuario -> Mono.just(usuario + " - Procesado"));

        StepVerifier.create(procesamiento)
                .expectNext("Usuario: email@test.com - Procesado")
                .verifyComplete();
    }

    @Test
    void deberiaManejarOperacionesParalelas() {
        List<String> emails = Arrays.asList("user1@test.com", "user2@test.com", "user3@test.com");

        Flux<String> procesosParalelos = Flux.fromIterable(emails)
                .parallel(2)
                .map(email -> "Procesado: " + email)
                .sequential();

        StepVerifier.create(procesosParalelos)
                .expectNextCount(3)
                .verifyComplete();
    }
}
