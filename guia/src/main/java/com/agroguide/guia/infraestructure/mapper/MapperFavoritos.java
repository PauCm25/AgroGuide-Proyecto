package com.agroguide.guia.infraestructure.mapper;

import com.agroguide.guia.domain.model.Favoritos;
import com.agroguide.guia.infraestructure.driver_adapter.jpa_repository.favoritos.FavoritosData;
import com.agroguide.guia.infraestructure.driver_adapter.jpa_repository.guia.GuiaData;
import org.springframework.stereotype.Component;

@Component
public class MapperFavoritos {

    public Favoritos toFavs(FavoritosData favoritosData) {
        Favoritos fav = new Favoritos();
        fav.setIdFav(favoritosData.getIdFav());
        fav.setIdUsuario(favoritosData.getIdUsuario());
        fav.setIdGuia(favoritosData.getGuia().getIdGuia());
        fav.setTituloGuia(favoritosData.getTituloGuia());
        fav.setAutorGuia(favoritosData.getAutorGuia());
        return fav;
    }
    public FavoritosData tofavoritosData(Favoritos favoritosData) {
        FavoritosData favData = new FavoritosData();
        favData.setIdFav(favoritosData.getIdFav());
        favData.setIdUsuario(favoritosData.getIdUsuario());

        GuiaData guiaData = new GuiaData();
        guiaData.setIdGuia(favoritosData.getIdGuia());
        favData.setGuia(guiaData);

        favData.setTituloGuia(favoritosData.getTituloGuia());
        favData.setAutorGuia(favoritosData.getAutorGuia());
        return favData;
    }
}
