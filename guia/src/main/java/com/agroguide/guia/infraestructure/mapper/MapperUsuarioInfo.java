package com.agroguide.guia.infraestructure.mapper;

import com.agroguide.guia.domain.model.UsuarioInfo;
import com.agroguide.guia.infraestructure.message_broker.dto.UsuarioInfoDTO;
import org.springframework.stereotype.Component;

@Component
public class MapperUsuarioInfo {

    public  UsuarioInfo toUsuarioInfo(UsuarioInfoDTO dto) {
        if (dto == null) {
            return null;
        }

        return new UsuarioInfo(
                dto.getId(),
                dto.getNombre(),
                dto.getTipoUsuario()
        );
    }

}