package com.crediya.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenAutenticacion {
    private String accessToken;
    private String tokenType;
    private LocalDateTime fechaExpiracion;
    private Usuario usuario;
}
