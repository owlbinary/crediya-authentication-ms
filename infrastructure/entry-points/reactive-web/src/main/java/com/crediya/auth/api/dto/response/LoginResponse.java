package com.crediya.auth.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    
    @JsonProperty("access_token")
    private String accessToken;
    
    @JsonProperty("token_type")
    private String tokenType;
    
    @JsonProperty("expires_at")
    private LocalDateTime expiresAt;
    
    @JsonProperty("usuario")
    private UsuarioAutenticadoResponse usuario;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UsuarioAutenticadoResponse {
        
        @JsonProperty("id_usuario")
        private Long idUsuario;
        
        @JsonProperty("email")
        private String email;
        
        @JsonProperty("nombre")
        private String nombre;
        
        @JsonProperty("apellido")
        private String apellido;
        
        @JsonProperty("id_rol")
        private Long idRol;
    }
}
