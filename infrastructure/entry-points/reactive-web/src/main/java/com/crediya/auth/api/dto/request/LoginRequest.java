package com.crediya.auth.api.dto.request;

import com.crediya.auth.model.constants.UsuarioConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    
    @JsonProperty("email")
    @NotBlank(message = UsuarioConstants.EMAIL_REQUERIDO)
    @Email(message = UsuarioConstants.EMAIL_FORMATO_INVALIDO)
    @Size(max = UsuarioConstants.EMAIL_MAX_LENGTH, message = UsuarioConstants.EMAIL_TAMANO_INVALIDO)
    private String email;
    
    @JsonProperty("password")
    @NotBlank(message = UsuarioConstants.PASSWORD_REQUERIDO)
    @Size(min = 6, max = 100, message = UsuarioConstants.PASSWORD_TAMANO_INVALIDO)
    private String password;
}
