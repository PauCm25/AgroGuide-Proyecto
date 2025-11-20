package com.agroguide.guia.infraestructure.message_broker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioInfoDTO {
    private Long id;
    private String nombre;
    private String tipoUsuario;
}