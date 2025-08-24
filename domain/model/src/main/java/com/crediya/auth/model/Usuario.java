package com.crediya.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    private Long idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String documentoIdentidad;
    private String telefono;
    private String direccion;
    private Long idRol;
    private BigDecimal salarioBase;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
