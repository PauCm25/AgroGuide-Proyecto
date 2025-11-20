package com.agroguide.guia.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Notificacion {

    private String tipo;
    private String numeroTelefono;
    private String mensaje;
}
