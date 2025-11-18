package com.agroguide.guia.domain.model.gateway;

import com.agroguide.guia.domain.model.Favoritos;
import com.agroguide.guia.domain.model.Guia;
import java.util.List;

public interface FavoritosGateway {

    Favoritos agregarFavs(Favoritos favoritos);
    void eliminarDeFavoritos(Long idGuia, Long usuarioId);
    List<Favoritos> listarFavoritosPorUsuario(Long usuarioId, int page, int size);
}
