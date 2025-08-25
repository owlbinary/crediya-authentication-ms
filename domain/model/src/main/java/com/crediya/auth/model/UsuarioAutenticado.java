package com.crediya.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioAutenticado {
    private Long idUsuario;
    private String email;
    private String nombre;
    private String apellido;
    private Long idRol;
    private List<String> permisos;
}
