package com.agroguide.guia.infraestructure.driver_adapter.jpa_repository.favoritos;


import com.agroguide.guia.domain.exception.FavoritoNoExisteException;
import com.agroguide.guia.domain.model.Favoritos;
import com.agroguide.guia.domain.model.gateway.FavoritosGateway;
import com.agroguide.guia.infraestructure.mapper.MapperFavoritos;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class FavoritosDataGatewayImpl implements FavoritosGateway {

    private final MapperFavoritos mapper; //Obj q convierte producto a productoData y viceversa
    private final FavoritosDataJpaRepository repository;

    @Override
    public Favoritos agregarFavs(Favoritos favoritos) {
        FavoritosData favoritosData = mapper.tofavoritosData(favoritos);
        return mapper.toFavs(repository.save(favoritosData));
    }

    @Override
    public void eliminarDeFavoritos(Long usuarioId, Long idGuia) {
        FavoritosData favorito = repository.findByUsuarioIdAndIdGuia(usuarioId, idGuia);
        if (favorito == null) {
            throw new FavoritoNoExisteException("No se encontró el favorito para eliminar");
        }
        repository.delete(favorito);
    }

    @Override
    public List<Favoritos> listarFavoritosPorUsuario(Long usuarioId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FavoritosData> favoritosPaginados = repository.findByUsuarioId(usuarioId, pageable);

        return favoritosPaginados.getContent()
                .stream().map(favoritosData -> mapper.toFavs(favoritosData))
                .collect(Collectors.toList());
    }


}
