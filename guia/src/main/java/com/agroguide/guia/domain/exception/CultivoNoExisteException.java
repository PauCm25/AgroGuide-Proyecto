package com.agroguide.guia.domain.exception;

public class CultivoNoExisteException extends RuntimeException {
    public CultivoNoExisteException(String message) {
        super(message);
    }
}
