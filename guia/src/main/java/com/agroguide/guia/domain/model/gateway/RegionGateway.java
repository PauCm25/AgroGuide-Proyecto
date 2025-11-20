package com.agroguide.guia.domain.model.gateway;



import com.agroguide.guia.domain.model.Region;

import java.util.List;

public interface RegionGateway {

    Region crear (Region region);
    void eliminarPorId(Long idRegion);
    Region consultarPorId(Long idRegion);
    Region actualizarPorId(Region region);
    List<Region> ListarRegiones(int page, int size);
    boolean existeRegion(Long idRegion);
}
