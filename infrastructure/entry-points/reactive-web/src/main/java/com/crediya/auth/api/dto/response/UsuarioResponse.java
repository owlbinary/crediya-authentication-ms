package com.crediya.auth.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {

    @JsonProperty("id_usuario")
    private Long idUsuario;

    @JsonProperty("nombre")
    private String nombre;

    @JsonProperty("apellido")
    private String apellido;

    @JsonProperty("email")
    private String email;

    @JsonProperty("documento_identidad")
    private String documentoIdentidad;

    @JsonProperty("telefono")
    private String telefono;

    @JsonProperty("direccion")
    private String direccion;

    @JsonProperty("id_rol")
    private Long idRol;

    @JsonProperty("salario_base")
    private BigDecimal salarioBase;

    @JsonProperty("fecha_creacion")
    private LocalDateTime fechaCreacion;
}
