package com.agroguide.guia.domain.exception;

public class RegionNoExisteException extends RuntimeException {
    public RegionNoExisteException(String message) {
        super(message);
    }
}
