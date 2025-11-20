package com.agroguide.guia.domain.model.gateway;

import com.agroguide.guia.domain.model.Notificacion;

public interface NotificationGateway {
    void enviarMensaje (Notificacion mensajeJson);
}
