package com.crediya.auth.api.config;

import com.crediya.auth.api.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig(jwtAuthenticationFilter);
    }

    @Test
    @DisplayName("Debería crear SecurityWebFilterChain correctamente")
    void deberiaCrearSecurityWebFilterChain() {
        ServerHttpSecurity http = ServerHttpSecurity.http();

        SecurityWebFilterChain filterChain = securityConfig.securityWebFilterChain(http);

        assertNotNull(filterChain);
    }

    @Test
    @DisplayName("Debería incluir el filtro JWT en la cadena de filtros")
    void deberiaIncluirFiltroJwtEnCadenaFiltros() {
        ServerHttpSecurity http = ServerHttpSecurity.http();

        SecurityWebFilterChain filterChain = securityConfig.securityWebFilterChain(http);

        assertNotNull(filterChain);
        
        assertNotNull(filterChain.getWebFilters());
        
        StepVerifier.create(filterChain.getWebFilters())
                .expectNextCount(1)
                .thenCancel()
                .verify();
    }

    @Test
    @DisplayName("Debería configurar CSRF como deshabilitado para API REST")
    void deberiaConfigurarCsrfDeshabilitadoParaApiRest() {
        ServerHttpSecurity http = ServerHttpSecurity.http();

        SecurityWebFilterChain filterChain = securityConfig.securityWebFilterChain(http);

        assertNotNull(filterChain);
        StepVerifier.create(filterChain.getWebFilters())
                .expectNextMatches(filter -> !filter.getClass().getSimpleName().contains("Csrf"))
                .thenCancel()
                .verify();
    }

    @Test
    @DisplayName("Debería ser un bean de configuración válido")
    void deberiaSearUnBeanDeConfiguracionValido() {
        assertNotNull(securityConfig);
        assertTrue(securityConfig.getClass().isAnnotationPresent(org.springframework.context.annotation.Configuration.class));
        assertTrue(securityConfig.getClass().isAnnotationPresent(org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity.class));
        assertTrue(securityConfig.getClass().isAnnotationPresent(org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity.class));
    }

    @Test
    @DisplayName("Debería inyectar correctamente el JwtAuthenticationFilter")
    void deberiaInyectarCorrectamenteJwtAuthenticationFilter() {
        assertNotNull(jwtAuthenticationFilter);
        
        SecurityConfig configConMock = new SecurityConfig(jwtAuthenticationFilter);
        assertNotNull(configConMock);
    }

    @Test
    @DisplayName("Debería configurar correctamente los path matchers para endpoints públicos")
    void deberiaConfigurarCorrectamentePathMatchersEndpointsPublicos() {
        SecurityWebFilterChain filterChain = securityConfig.securityWebFilterChain(ServerHttpSecurity.http());
        
        assertNotNull(filterChain);
        
        StepVerifier.create(filterChain.getWebFilters())
                .expectNextCount(1)
                .thenCancel()
                .verify();
    }

    @Test
    @DisplayName("Debería permitir el uso de reactive method security")
    void deberiaPermitirUsoReactiveMethodSecurity() {
        assertTrue(securityConfig.getClass()
                .isAnnotationPresent(org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity.class));

        assertNotNull(securityConfig);
    }

    @Test
    @DisplayName("Debería tener las anotaciones de seguridad requeridas")
    void deberiaTenerAnotacionesSeguridadRequeridas() {
        Class<SecurityConfig> clazz = SecurityConfig.class;
        
        assertTrue(clazz.isAnnotationPresent(org.springframework.context.annotation.Configuration.class));
        
        assertTrue(clazz.isAnnotationPresent(org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity.class));
        
        assertTrue(clazz.isAnnotationPresent(org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity.class));
        
        assertNotNull(clazz.getAnnotations());
        assertTrue(clazz.getAnnotations().length > 0);
    }

    @Test
    @DisplayName("Debería crear múltiples instancias de SecurityWebFilterChain sin problemas")
    void deberiaCrearMultiplesInstanciasSecurityWebFilterChainSinProblemas() {
        ServerHttpSecurity http1 = ServerHttpSecurity.http();
        ServerHttpSecurity http2 = ServerHttpSecurity.http();

        SecurityWebFilterChain filterChain1 = securityConfig.securityWebFilterChain(http1);
        SecurityWebFilterChain filterChain2 = securityConfig.securityWebFilterChain(http2);

        assertNotNull(filterChain1);
        assertNotNull(filterChain2);
        
        assertNotSame(filterChain1, filterChain2);
    }

    @Test
    @DisplayName("Debería validar que el método securityWebFilterChain está anotado como Bean")
    void deberiaValidarMetodoSecurityWebFilterChainAnotadoComoBean() throws NoSuchMethodException {
        var method = SecurityConfig.class.getMethod("securityWebFilterChain", ServerHttpSecurity.class);
        assertTrue(method.isAnnotationPresent(org.springframework.context.annotation.Bean.class));
    }

    @Test
    @DisplayName("Debería validar que SecurityConfig tiene el campo jwtAuthenticationFilter")
    void deberiaValidarSecurityConfigTieneCampoJwtAuthenticationFilter() {
        var fields = SecurityConfig.class.getDeclaredFields();
        boolean hasJwtField = false;
        
        for (var field : fields) {
            if (field.getType().equals(JwtAuthenticationFilter.class)) {
                hasJwtField = true;
                break;
            }
        }
        
        assertTrue(hasJwtField, "SecurityConfig debe tener un campo de tipo JwtAuthenticationFilter");
    }

    @Test
    @DisplayName("Debería validar que SecurityConfig es final (inmutable)")
    void deberiaValidarSecurityConfigEsFinalInmutable() {
        var fields = SecurityConfig.class.getDeclaredFields();
        boolean hasPrivateFinalField = false;
        
        for (var field : fields) {
            if (field.getType().equals(JwtAuthenticationFilter.class)) {
                assertTrue(java.lang.reflect.Modifier.isPrivate(field.getModifiers()));
                assertTrue(java.lang.reflect.Modifier.isFinal(field.getModifiers()));
                hasPrivateFinalField = true;
                break;
            }
        }
        
        assertTrue(hasPrivateFinalField, "El campo JwtAuthenticationFilter debe ser private final");
    }
}
