package com.crediya.auth.api.mapper;

import com.crediya.auth.api.dto.request.LoginRequest;
import com.crediya.auth.api.dto.response.LoginResponse;
import com.crediya.auth.model.CredencialesLogin;
import com.crediya.auth.model.TokenAutenticacion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthDtoMapper {
    
    CredencialesLogin toDomain(LoginRequest request);
    
    @Mapping(target = "usuario.idUsuario", source = "usuario.idUsuario")
    @Mapping(target = "usuario.email", source = "usuario.email")
    @Mapping(target = "usuario.nombre", source = "usuario.nombre")
    @Mapping(target = "usuario.apellido", source = "usuario.apellido")
    @Mapping(target = "usuario.idRol", source = "usuario.idRol")
    @Mapping(target = "expiresAt", source = "fechaExpiracion")
    LoginResponse toResponse(TokenAutenticacion tokenAutenticacion);
}
