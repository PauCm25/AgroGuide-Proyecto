package com.agroguide.guia.domain.model.gateway;

import com.agroguide.guia.domain.model.UsuarioInfo;

public interface UsuarioGateway {
    UsuarioInfo usuarioExiste(Long usuarioId);
}
