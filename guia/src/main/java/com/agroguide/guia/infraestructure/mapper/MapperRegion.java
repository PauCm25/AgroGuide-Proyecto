package com.agroguide.guia.infraestructure.mapper;

import com.agroguide.guia.domain.model.Region;
import com.agroguide.guia.infraestructure.driver_adapter.jpa_repository.region.RegionData;
import org.springframework.stereotype.Component;

@Component
public class MapperRegion {
    public Region toDomain(RegionData regionData){
        return new Region(
                regionData.getIdRegion(),
                regionData.getNombreRegion(),
                regionData.getDepartamento()
        );
    }
    public RegionData toData(Region region){
        return new RegionData(
                region.getIdRegion(),
                region.getNombreRegion(),
                region.getDepartamento()
        );
    }
}
