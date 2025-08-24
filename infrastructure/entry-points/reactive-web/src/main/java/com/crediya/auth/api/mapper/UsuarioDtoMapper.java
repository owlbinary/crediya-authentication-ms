package com.crediya.auth.api.mapper;

import com.crediya.auth.api.dto.request.RegistrarUsuarioRequest;
import com.crediya.auth.api.dto.response.UsuarioResponse;
import com.crediya.auth.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UsuarioDtoMapper {

    Usuario toDomain(RegistrarUsuarioRequest request);

    UsuarioResponse toResponse(Usuario usuario);
}
