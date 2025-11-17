package com.agroguide.guia.infraestructure.mapper;

import com.agroguide.guia.domain.model.Categoria;
import com.agroguide.guia.infraestructure.driver_adapter.jpa_repository.categoria.CategoriaData;
import org.springframework.stereotype.Component;

@Component
public class MapperCategoria {
    public Categoria toCateg(CategoriaData  categoriaData){
        return new Categoria(
                categoriaData.getIdCategoria(),
                categoriaData.getNombreCategoria(),
                categoriaData.getDescripcionCategoria()
        );
    }
    public CategoriaData toCatData(Categoria categoria){
        return new CategoriaData(
                categoria.getIdCategoria(),
                categoria.getNombreCategoria(),
                categoria.getDescripcionCategoria()
        );
    }
}
