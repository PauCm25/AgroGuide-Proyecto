package com.agroguide.guia.infraestructure.message_broker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventoNotificationDTO {
    private String tipo;
    private String telefono;
    private String mensaje;
}
