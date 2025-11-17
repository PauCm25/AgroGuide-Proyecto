package com.agroguide.guia.infraestructure.mapper;

import com.agroguide.guia.domain.model.Cultivo;
import com.agroguide.guia.infraestructure.driver_adapter.jpa_repository.cultivo.CultivoData;
import org.springframework.stereotype.Component;

@Component
public class MapperCultivo {
    public Cultivo toCultivo (CultivoData cultivoData) {
        return new Cultivo(
                cultivoData.getIdCultivo(),
                cultivoData.getTipoSuelo(),
                cultivoData.getClimaRecomendado(),
                cultivoData.getNombreCultivo()
        );
    }
    public CultivoData toCultData (Cultivo cultivo) {
      return new CultivoData(
              cultivo.getIdCultivo(),
              cultivo.getNombreCultivo(),
              cultivo.getTipoSuelo(),
              cultivo.getClimaRecomendado()
      );
    }
}
