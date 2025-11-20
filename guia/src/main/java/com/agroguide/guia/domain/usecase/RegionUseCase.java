package com.agroguide.guia.domain.usecase;


import com.agroguide.guia.domain.exception.RegionNoExisteException;
import com.agroguide.guia.domain.model.Region;
import com.agroguide.guia.domain.model.UsuarioInfo;
import com.agroguide.guia.domain.model.gateway.RegionGateway;
import com.agroguide.guia.domain.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class RegionUseCase {

    private final RegionGateway regionGateway;
    private final UsuarioGateway usuarioGateway;

    public Region crearRegion(Region region, Long usuarioId) {
        // Validar que el usuario exista
        UsuarioInfo usuarioInfo = usuarioGateway.usuarioExiste(usuarioId);
        if (usuarioInfo == null || usuarioInfo.getNombre() == null) {
            throw new IllegalArgumentException("El usuario no existe en el sistema");
        }

        // Validar rol permitido (SOLO ADMIN)
        String rol = usuarioInfo.getTipoUsuario().trim().toUpperCase();
        if (!(rol.equals("ADMINISTRADOR"))) {
            throw new IllegalArgumentException("Solo ADMINISTRADOR puede crear regiones");
        }

        // Validar campos obligatorios de la región
        if (region.getNombreRegion() == null && region.getDepartamento() == null) {
            throw new NullPointerException("Ingrese atributos correctamente - crearRegion");
        }

        // Guardar la región
        return regionGateway.crear(region);
    }

    public void eliminarRegion(Long id, Long usuarioId){

        // Validar que el usuario exista
        UsuarioInfo usuarioInfo = usuarioGateway.usuarioExiste(usuarioId);
        if (usuarioInfo == null || usuarioInfo.getNombre() == null) {
            throw new IllegalArgumentException("El usuario no existe en el sistema");
        }

        // Validar rol permitido (solo ADMINISTRADOR)
        String rol = usuarioInfo.getTipoUsuario().trim().toUpperCase();
        if (!rol.equals("ADMINISTRADOR")) {
            throw new IllegalArgumentException("Solo ADMINISTRADOR puede eliminar regiones");
        }

        try {
            regionGateway.eliminarPorId(id);
        } catch (Exception e) {
            throw new RegionNoExisteException("Error al eliminar la guía. No existe");
        }
    }

    public Region consultarRegion(Long id){
        //Busca una region por su ID
        //Si no lo encuentra, retorna una region vacía
        //Si lo encuentra, retorna la region entera
        try{
            return regionGateway.consultarPorId(id);
        } catch (Exception e){
            System.out.println("Error al consultar la region");

            //crear un producto vacío para retornarlo
            Region regVacia = new Region();
            return regVacia;
        }
    }

    public Region actualizarRegion(Region region, Long usuarioId) {

        if(region.getIdRegion() == null){
            throw new RegionNoExisteException("Revise que la region exista - actualizarRegion");
        }
        // Validar que el usuario exista
        UsuarioInfo usuarioInfo = usuarioGateway.usuarioExiste(usuarioId);
        if (usuarioInfo == null || usuarioInfo.getNombre() == null) {
            throw new IllegalArgumentException("El usuario no existe en el sistema");
        }

        // Validar rol permitido (solo ADMINISTRADOR)
        String rol = usuarioInfo.getTipoUsuario().trim().toUpperCase();
        if (!rol.equals("ADMINISTRADOR")) {
            throw new IllegalArgumentException("Solo ADMINISTRADOR puede eliminar regiones");
        }

        return regionGateway.actualizarPorId(region);
    }

    public List<Region> consultarRegiones(int page, int size)
    {
        //Obtiene todos las regiones existentes en la BD
        //Retorna una lista
        //si hay error en la consulta, atrapa la excepcion
        try {
            return regionGateway.ListarRegiones(page, size);
        } catch(Exception e) {
            throw new IllegalArgumentException("Error al consultar las regiones existentes");
        }
    }
}
