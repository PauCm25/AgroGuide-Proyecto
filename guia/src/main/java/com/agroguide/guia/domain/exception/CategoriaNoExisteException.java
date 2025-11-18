package com.agroguide.guia.domain.exception;

public class CategoriaNoExisteException extends RuntimeException {
    public CategoriaNoExisteException(String message) {
        super(message);
    }
}
