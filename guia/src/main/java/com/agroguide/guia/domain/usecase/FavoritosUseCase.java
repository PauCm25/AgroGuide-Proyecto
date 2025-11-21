package com.agroguide.guia.domain.usecase;


import com.agroguide.guia.domain.model.Favoritos;
import com.agroguide.guia.domain.model.Guia;
import com.agroguide.guia.domain.model.UsuarioInfo;
import com.agroguide.guia.domain.model.gateway.FavoritosGateway;
import com.agroguide.guia.domain.model.gateway.GuiaGateway;
import com.agroguide.guia.domain.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;
import com.agroguide.guia.domain.exception.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class FavoritosUseCase {

    //Inyectar caso de uso de Guia

    private final FavoritosGateway favoritosGateway;
    private final GuiaGateway guiaGateway;
    private final UsuarioGateway usuarioGateway;

    public Favoritos agregarAFavoritos (Long usuarioId, Long idGuia) {
        //verificar que el usuario exista
        UsuarioInfo usuarioInfo = usuarioGateway.usuarioExiste(usuarioId);
        if (usuarioInfo == null || usuarioInfo.getNombre() == null) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + usuarioId);
        }

        //verificar que la guía que se va a agregar exista
        Guia guia = guiaGateway.consultarPorId(idGuia);
        if (guia == null || guia.getIdGuia() == null) {
            throw new GuiaNoExisteException("Guia no encontrada - AgregarAFavoritos");
        }

        // Validar estado de la guía (solo se permiten guías APROBADAS)
        if (guia.getEstadoGuia() == null ||
                !guia.getEstadoGuia().equalsIgnoreCase("APROBADA")) {

            throw new GuiaNoDisponibleException(
                    "La guía no puede agregarse a favoritos porque no está aprobada. Estado actual: "
                            + guia.getEstadoGuia());
        }
            //Crear el obj favoritos
            Favoritos favoritos = new Favoritos();
            favoritos.setIdGuia(idGuia);
            favoritos.setIdUsuario(usuarioId);
            favoritos.setTituloGuia(guia.getTitulo());
            favoritos.setAutorGuia(guia.getNombreAutor());

            //guardar el objeto
            return favoritosGateway.agregarFavs(favoritos);
        }


    public List<Favoritos> consultarFavoritos(Long usuarioId, int page, int size) {

        UsuarioInfo usuarioInfo = usuarioGateway.usuarioExiste(usuarioId);
        if (usuarioInfo == null || usuarioInfo.getNombre() == null) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + usuarioId);
        }

        try {
            return favoritosGateway.listarFavoritosPorUsuario(usuarioId, page, size);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al consultar los favoritos del usuario con ID: " + usuarioId);
        }
    }

    public void eliminarDeFavoritos(Long idGuia, Long usuarioId) {
        UsuarioInfo usuarioInfo = usuarioGateway.usuarioExiste(usuarioId);
        if (usuarioInfo == null || usuarioInfo.getNombre() == null) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + usuarioId);
        }
        favoritosGateway.eliminarDeFavoritos(usuarioId, idGuia);
    }

}
