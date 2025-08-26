package com.crediya.auth.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SwaggerConfigTest {

    private SwaggerConfig swaggerConfig;

    @BeforeEach
    void setUp() {
        swaggerConfig = new SwaggerConfig();
    }

    @Test
    void deberiaCrearOpenAPIBean() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI();

        assertNotNull(openAPI);
    }

    @Test
    void deberiaConfigurarInformacionDelAPI() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI();

        Info info = openAPI.getInfo();
        assertNotNull(info);
        assertEquals("CrediYa - Microservicio de autenticación", info.getTitle());
        assertEquals("1.0.0", info.getVersion());
        assertEquals("API para gestión de usuarios del sistema CrediYa", info.getDescription());
    }

    @Test
    void deberiaConfigurarInformacionDeContacto() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI();

        Info info = openAPI.getInfo();
        Contact contact = info.getContact();
        assertNotNull(contact);
        assertEquals("Equipo de Desarrollo CrediYa", contact.getName());
    }

    @Test
    void deberiaRetornarNuevaInstanciaEnCadaLlamada() {
        OpenAPI openAPI1 = swaggerConfig.customOpenAPI();
        OpenAPI openAPI2 = swaggerConfig.customOpenAPI();

        assertNotNull(openAPI1);
        assertNotNull(openAPI2);
        assertNotSame(openAPI1, openAPI2, "Cada llamada debería retornar una nueva instancia");
    }

    @Test
    void deberiaConfigurarTodosLosCamposRequeridos() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI();

        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
        assertNotNull(openAPI.getInfo().getTitle());
        assertNotNull(openAPI.getInfo().getVersion());
        assertNotNull(openAPI.getInfo().getDescription());
        assertNotNull(openAPI.getInfo().getContact());
        assertNotNull(openAPI.getInfo().getContact().getName());
    }
}
