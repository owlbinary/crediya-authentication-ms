package com.crediya.auth.model.exception;

public class UsuarioYaExisteException extends RuntimeException {
    public UsuarioYaExisteException(String email) {
        super("El usuario con email " + email + " ya se encuentra registrado");
    }
}
