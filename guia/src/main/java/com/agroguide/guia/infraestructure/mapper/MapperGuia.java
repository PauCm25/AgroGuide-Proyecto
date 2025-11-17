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
        guia.setEtiquetas(guiaData.getEtiquetas());
        guia.setEstadoGuia(guiaData.getEstadoGuia());

        guia.setCultivo(mapperCultivo.toGuia(guiaData.getCultivos()));
        guia.setRegion(mapperRegion.toGuia(guiaData.getRegiones()));
        guia.setCategoria(mapperCategoria.toGuia(guiaData.getCategorias()));
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
        guiaData.setEtiquetas(guia.getEtiquetas());
        guiaData.setEstadoGuia(guia.getEstadoGuia());

        guiaData.setCultivos(mapperCultivo.toData(guia.getCultivo()));
        guiaData.setRegiones(mapperRegion.toData(guia.getRegion()));
        guiaData.setCategorias(mapperCategoria.toData(guia.getCategoria()));
        return guiaData;
    }
}

