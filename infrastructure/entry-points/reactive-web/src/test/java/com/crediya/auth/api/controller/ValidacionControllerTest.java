package com.crediya.auth.api.controller;

import com.crediya.auth.api.dto.response.ValidacionDocumentoResponse;
import com.crediya.auth.usecase.ValidarDocumentoUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ValidacionController Tests")
class ValidacionControllerTest {

    @Mock
    private ValidarDocumentoUseCase validarDocumentoUseCase;

    @InjectMocks
    private ValidacionController validacionController;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(validacionController).build();
    }

    @Test
    @DisplayName("Debería validar que el documento existe exitosamente")
    void deberiaValidarQueDocumentoExiste() {
        String documentoIdentidad = "12345678";
        when(validarDocumentoUseCase.documentoExiste(documentoIdentidad))
                .thenReturn(Mono.just(true));

        webTestClient.get()
                .uri("/api/v1/validaciones/documento/{documentoIdentidad}", documentoIdentidad)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ValidacionDocumentoResponse.class)
                .value(response -> {
                    assert response.getDocumentoIdentidad().equals(documentoIdentidad);
                    assert response.getExiste().equals(true);
                    assert response.getMensaje().equals("El documento existe, puede continuar con el proceso");
                });
    }

    @Test
    @DisplayName("Debería validar que el documento no existe exitosamente")
    void deberiaValidarQueDocumentoNoExiste() {
        String documentoIdentidad = "87654321";
        when(validarDocumentoUseCase.documentoExiste(documentoIdentidad))
                .thenReturn(Mono.just(false));

        webTestClient.get()
                .uri("/api/v1/validaciones/documento/{documentoIdentidad}", documentoIdentidad)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ValidacionDocumentoResponse.class)
                .value(response -> {
                    assert response.getDocumentoIdentidad().equals(documentoIdentidad);
                    assert response.getExiste().equals(false);
                    assert response.getMensaje().equals("El documento no existe, debe realizar la creación del usuario para continuar con el proceso");
                });
    }

    @Test
    @DisplayName("Debería retornar response reactivo correctamente")
    void deberiaRetornarResponseReactivo() {
        String documentoIdentidad = "11111111";
        when(validarDocumentoUseCase.documentoExiste(documentoIdentidad))
                .thenReturn(Mono.just(false));

        Mono<ValidacionDocumentoResponse> resultado = validacionController.validarDocumento(documentoIdentidad);

        StepVerifier.create(resultado)
                .expectNextMatches(response -> 
                        response.getDocumentoIdentidad().equals(documentoIdentidad) &&
                        response.getExiste().equals(false) &&
                        response.getMensaje().equals("El documento no existe, debe realizar la creación del usuario para continuar con el proceso"))
                .verifyComplete();
    }

    @ParameterizedTest
    @DisplayName("Debería validar documentos con diferentes formatos válidos")
    @ValueSource(strings = {"12345678", "1234567890", "CC12345678", "PE87654321"})
    void deberiaValidarDocumentosConDiferentesFormatos(String documentoIdentidad) {
        when(validarDocumentoUseCase.documentoExiste(documentoIdentidad))
                .thenReturn(Mono.just(true));

        webTestClient.get()
                .uri("/api/v1/validaciones/documento/{documentoIdentidad}", documentoIdentidad)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ValidacionDocumentoResponse.class)
                .value(response -> {
                    assert response.getDocumentoIdentidad().equals(documentoIdentidad);
                    assert response.getExiste().equals(true);
                });
    }

    @Test
    @DisplayName("Debería manejar error cuando el use case falla")
    void deberiaManejarErrorCuandoUseCaseFalla() {
        String documentoIdentidad = "12345678";
        when(validarDocumentoUseCase.documentoExiste(documentoIdentidad))
                .thenReturn(Mono.error(new RuntimeException("Error de base de datos")));

        webTestClient.get()
                .uri("/api/v1/validaciones/documento/{documentoIdentidad}", documentoIdentidad)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName("Debería validar documento con caracteres especiales en URL")
    void deberiaValidarDocumentoConCaracteresEspeciales() {
        String documentoIdentidad = "A1-B2-C3";
        when(validarDocumentoUseCase.documentoExiste(anyString()))
                .thenReturn(Mono.just(false));

        webTestClient.get()
                .uri("/api/v1/validaciones/documento/{documentoIdentidad}", documentoIdentidad)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ValidacionDocumentoResponse.class)
                .value(response -> {
                    assert response.getDocumentoIdentidad().equals(documentoIdentidad);
                    assert response.getExiste().equals(false);
                });
    }

    @Test
    @DisplayName("Debería validar endpoint con content type correcto")
    void deberiaValidarEndpointConContentTypeCorrecto() {
        String documentoIdentidad = "12345678";
        when(validarDocumentoUseCase.documentoExiste(documentoIdentidad))
                .thenReturn(Mono.just(true));

        webTestClient.get()
                .uri("/api/v1/validaciones/documento/{documentoIdentidad}", documentoIdentidad)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.documento_identidad").isEqualTo(documentoIdentidad)
                .jsonPath("$.existe").isEqualTo(true)
                .jsonPath("$.mensaje").isEqualTo("El documento existe, puede continuar con el proceso");
    }
}
