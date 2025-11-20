package com.agroguide.guia.infraestructure.mapper;

import com.agroguide.guia.domain.model.Guia;
import com.agroguide.guia.infraestructure.driver_adapter.jpa_repository.guia.GuiaData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
@RequiredArgsConstructor
@Component
public class MapperGuia {

    private final MapperCultivo mapperCultivo;
    private final MapperRegion mapperRegion;
    private final MapperCategoria mapperCategoria;

    public Guia toGuia(GuiaData guiaData) {
        Guia guia = new Guia();
        guia.setIdGuia(guiaData.getIdGuia());
        guia.setIdTecnico(guiaData.getIdTecnico());
        guia.setTitulo(guiaData.getTitulo());
        guia.setDescripcion(guiaData.getDescripcion());
        guia.setFechaPublicacion(guiaData.getFechaPublicacion());
        guia.setNombreAutor(guiaData.getNombreAutor());
        guia.setEstadoGuia(guiaData.getEstadoGuia());

        guia.setCultivo(mapperCultivo.toCultivo(guiaData.getCultivo()));
        guia.setRegion(mapperRegion.toRegion(guiaData.getRegion()));
        guia.setCategoria(mapperCategoria.toCateg(guiaData.getCategoria()));
        return guia;
    }
    public GuiaData toGuiaData(Guia guia) {
        GuiaData guiaData = new GuiaData();
        guiaData.setIdGuia(guia.getIdGuia());
        guiaData.setIdTecnico(guia.getIdTecnico());
        guiaData.setTitulo(guia.getTitulo());
        guiaData.setDescripcion(guia.getDescripcion());
        guiaData.setFechaPublicacion(guia.getFechaPublicacion());
        guiaData.setNombreAutor(guia.getNombreAutor());
        guiaData.setEstadoGuia(guia.getEstadoGuia());

        guiaData.setCultivo(mapperCultivo.toCultData(guia.getCultivo()));
        guiaData.setRegion(mapperRegion.toRegData(guia.getRegion()));
        guiaData.setCategoria(mapperCategoria.toCatData(guia.getCategoria()));
        return guiaData;
    }
}

