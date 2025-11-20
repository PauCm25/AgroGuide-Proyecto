package com.agroguide.guia.domain.model.gateway;

import com.agroguide.guia.domain.model.Categoria;


import java.util.List;

public interface CategoriaGateway {

    Categoria crear (Categoria categoria);
    void eliminarPorId(Long idCategoria);
    Categoria consultarPorId(Long idCategoria);
    Categoria actualizarPorId(Categoria categoria);
    List<Categoria> ListarCategorias(int page, int size);
    boolean existeCategoria(Long idCategoria);
}
