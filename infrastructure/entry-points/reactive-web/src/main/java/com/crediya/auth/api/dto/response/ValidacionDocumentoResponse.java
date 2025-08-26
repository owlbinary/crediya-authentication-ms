package com.crediya.auth.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidacionDocumentoResponse {

    @JsonProperty("documento_identidad")
    private String documentoIdentidad;

    @JsonProperty("existe")
    private Boolean existe;

    @JsonProperty("mensaje")
    private String mensaje;
}
