package com.agroguide.guia.domain.model.gateway;

import com.agroguide.guia.domain.model.Cultivo;
import com.agroguide.guia.domain.model.Guia;

import java.util.List;

public interface CultivoGateway {

    Cultivo crear (Cultivo cultivo);
    void eliminarPorId(Long idCultivo);
    Cultivo consultarPorId(Long idCultivo);
    Cultivo actualizarPorId(Cultivo cultivo);
    List<Cultivo> ListarCultivos(int page, int size);
}
