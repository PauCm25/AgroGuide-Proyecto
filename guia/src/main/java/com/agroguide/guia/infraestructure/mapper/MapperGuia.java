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

        guia.setCultivo(mapperCultivo.toCultivo(guiaData.getCultivos()));
        guia.setRegion(mapperRegion.toRegion(guiaData.getRegiones()));
        guia.setCategoria(mapperCategoria.toCateg(guiaData.getCategorias()));
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

        guiaData.setCultivos(mapperCultivo.toCultData(guia.getCultivo()));
        guiaData.setRegiones(mapperRegion.toRegData(guia.getRegion()));
        guiaData.setCategorias(mapperCategoria.toCatData(guia.getCategoria()));
        return guiaData;
    }
}

