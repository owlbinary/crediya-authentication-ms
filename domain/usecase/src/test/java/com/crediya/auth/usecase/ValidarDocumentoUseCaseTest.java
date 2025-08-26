package com.crediya.auth.usecase;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.crediya.auth.model.gateway.UsuarioGateway;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
@DisplayName("ValidarDocumentoUseCase Tests")
class ValidarDocumentoUseCaseTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    @InjectMocks
    private ValidarDocumentoUseCase validarDocumentoUseCase;

    @Test
    @DisplayName("Debería retornar true cuando el documento existe")
    void deberiaRetornarTrueCuandoDocumentoExiste() {
        String documentoIdentidad = "12345678";
        when(usuarioGateway.existePorDocumentoIdentidad(documentoIdentidad))
                .thenReturn(Mono.just(true));

        Mono<Boolean> resultado = validarDocumentoUseCase.documentoExiste(documentoIdentidad);

        StepVerifier.create(resultado)
                .expectNext(true)
                .verifyComplete();

        verify(usuarioGateway).existePorDocumentoIdentidad(documentoIdentidad);
    }

    @Test
    @DisplayName("Debería retornar false cuando el documento no existe")
    void deberiaRetornarFalseCuandoDocumentoNoExiste() {
        String documentoIdentidad = "87654321";
        when(usuarioGateway.existePorDocumentoIdentidad(documentoIdentidad))
                .thenReturn(Mono.just(false));

        Mono<Boolean> resultado = validarDocumentoUseCase.documentoExiste(documentoIdentidad);

        StepVerifier.create(resultado)
                .expectNext(false)
                .verifyComplete();

        verify(usuarioGateway).existePorDocumentoIdentidad(documentoIdentidad);
    }

    @ParameterizedTest
    @DisplayName("Debería validar documentos con diferentes formatos")
    @ValueSource(strings = { "12345678", "1234567890", "CC12345678", "PE87654321", "A1-B2-C3" })
    void deberiaValidarDocumentosConDiferentesFormatos(String documentoIdentidad) {
        when(usuarioGateway.existePorDocumentoIdentidad(documentoIdentidad))
                .thenReturn(Mono.just(true));

        Mono<Boolean> resultado = validarDocumentoUseCase.documentoExiste(documentoIdentidad);

        StepVerifier.create(resultado)
                .expectNext(true)
                .verifyComplete();

        verify(usuarioGateway).existePorDocumentoIdentidad(documentoIdentidad);
    }

    @Test
    @DisplayName("Debería propagar error cuando el gateway falla")
    void deberiaPropagaErrorCuandoGatewayFalla() {
        String documentoIdentidad = "12345678";
        RuntimeException exception = new RuntimeException("Error de base de datos");
        when(usuarioGateway.existePorDocumentoIdentidad(documentoIdentidad))
                .thenReturn(Mono.error(exception));

        Mono<Boolean> resultado = validarDocumentoUseCase.documentoExiste(documentoIdentidad);

        StepVerifier.create(resultado)
                .expectError(RuntimeException.class)
                .verify();

        verify(usuarioGateway).existePorDocumentoIdentidad(documentoIdentidad);
    }

    @Test
    @DisplayName("Debería manejar documento nulo o vacío")
    void deberiaManejarDocumentoNuloOVacio() {
        when(usuarioGateway.existePorDocumentoIdentidad(null))
                .thenReturn(Mono.just(false));

        Mono<Boolean> resultado = validarDocumentoUseCase.documentoExiste(null);

        StepVerifier.create(resultado)
                .expectNext(false)
                .verifyComplete();

        when(usuarioGateway.existePorDocumentoIdentidad(""))
                .thenReturn(Mono.just(false));

        Mono<Boolean> resultadoVacio = validarDocumentoUseCase.documentoExiste("");

        StepVerifier.create(resultadoVacio)
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería llamar al gateway una sola vez por documento")
    void deberiaLlamarGatewayUnaSolaVezPorDocumento() {
        String documentoIdentidad = "12345678";
        when(usuarioGateway.existePorDocumentoIdentidad(documentoIdentidad))
                .thenReturn(Mono.just(true));

        validarDocumentoUseCase.documentoExiste(documentoIdentidad).block();

        verify(usuarioGateway, times(1)).existePorDocumentoIdentidad(documentoIdentidad);
        verifyNoMoreInteractions(usuarioGateway);
    }

    @Test
    @DisplayName("Debería manejar respuesta vacía del gateway")
    void deberiaManejarRespuestaVaciaDelGateway() {
        String documentoIdentidad = "12345678";
        when(usuarioGateway.existePorDocumentoIdentidad(documentoIdentidad))
                .thenReturn(Mono.empty());

        Mono<Boolean> resultado = validarDocumentoUseCase.documentoExiste(documentoIdentidad);

        StepVerifier.create(resultado)
                .verifyComplete();

        verify(usuarioGateway).existePorDocumentoIdentidad(documentoIdentidad);
    }

    @Test
    @DisplayName("Debería ser reactivo y no bloquear")
    void deberiaSerReactivoYNoBloquear() {
        String documentoIdentidad = "12345678";
        when(usuarioGateway.existePorDocumentoIdentidad(anyString()))
                .thenReturn(Mono.just(false).delayElement(java.time.Duration.ofMillis(100)));

        Mono<Boolean> resultado1 = validarDocumentoUseCase.documentoExiste(documentoIdentidad);
        Mono<Boolean> resultado2 = validarDocumentoUseCase.documentoExiste("87654321");

        StepVerifier.create(Mono.zip(resultado1, resultado2))
                .expectNextMatches(tuple -> !tuple.getT1() && !tuple.getT2())
                .verifyComplete();

        verify(usuarioGateway, times(2)).existePorDocumentoIdentidad(anyString());
    }
}
