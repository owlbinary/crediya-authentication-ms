package com.crediya.auth.model.exception;

import com.crediya.auth.model.constants.UsuarioConstants;

public class DocumentoYaExisteException extends RuntimeException {
    public DocumentoYaExisteException(String documentoIdentidad) {
        super(String.format(UsuarioConstants.DOCUMENTO_YA_EXISTE, documentoIdentidad));
    }
}
