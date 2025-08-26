package com.crediya.auth.api.dto.request;

import com.crediya.auth.api.validation.ValidDocumentoIdentidad;
import com.crediya.auth.model.constants.UsuarioConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarUsuarioRequest {

    @JsonProperty("nombre")
    @NotBlank(message = UsuarioConstants.NOMBRE_REQUERIDO)
    @Size(max = UsuarioConstants.NOMBRE_MAX_LENGTH, message = UsuarioConstants.NOMBRE_TAMANO_INVALIDO)
    private String nombre;

    @JsonProperty("apellido")
    @NotBlank(message = UsuarioConstants.APELLIDO_REQUERIDO)
    @Size(max = UsuarioConstants.APELLIDO_MAX_LENGTH, message = UsuarioConstants.APELLIDO_TAMANO_INVALIDO)
    private String apellido;

    @JsonProperty("email")
    @NotBlank(message = UsuarioConstants.EMAIL_REQUERIDO)
    @Email(message = UsuarioConstants.EMAIL_FORMATO_INVALIDO)
    @Size(max = UsuarioConstants.EMAIL_MAX_LENGTH, message = UsuarioConstants.EMAIL_TAMANO_INVALIDO)
    private String email;

    @JsonProperty("password")
    @NotBlank(message = UsuarioConstants.CONTRASENA_OBLIGATORIA)
    @Size(min = 6, max = 100, message = UsuarioConstants.CONTRASENA_TAMANO_INVALIDO)
    private String password;

    @JsonProperty("documento_identidad")
    @NotBlank(message = UsuarioConstants.DOCUMENTO_REQUERIDO)
    @ValidDocumentoIdentidad
    private String documentoIdentidad;

    @JsonProperty("telefono")
    @Pattern(regexp = UsuarioConstants.TELEFONO_PATTERN, message = UsuarioConstants.TELEFONO_FORMATO_INVALIDO)
    private String telefono;

    @JsonProperty("direccion")
    @Size(max = 200, message = UsuarioConstants.DIRECCION_TAMANO_INVALIDO)
    private String direccion;

    @JsonProperty("id_rol")
    @Positive(message = UsuarioConstants.ROL_POSITIVO_REQUERIDO)
    private Long idRol;

    @JsonProperty("salario_base")
    @NotNull(message = UsuarioConstants.SALARIO_BASE_REQUERIDO)
    @Digits(integer = 10, fraction = 2, message = UsuarioConstants.SALARIO_FORMATO_INVALIDO)
    private BigDecimal salarioBase;
}
