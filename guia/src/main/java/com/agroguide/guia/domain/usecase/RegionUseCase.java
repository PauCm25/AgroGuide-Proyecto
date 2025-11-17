package com.agroguide.guia.domain.usecase;


import com.agroguide.guia.domain.exception.RegionNoExisteException;
import com.agroguide.guia.domain.model.Region;
import com.agroguide.guia.domain.model.gateway.RegionGateway;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class RegionUseCase {

    private final RegionGateway regionGateway;

    public Region crearRegion(Region region) {
        //Condicional para hacer que nombre y departamento sean obligatorios
        //si ambos son nulos, no se guarda la region
        if(region.getNombreRegion() == null && region.getDepartamento() == null){
            throw new NullPointerException("Ingrese atributos correctamente - crearRegion");
        }
        return regionGateway.crear(region); //Si esta bien, guarda la region
    }

    public void eliminarRegion(Long id){
        //Elimina la region por el ID
        //Retorna un vacío
        //Lanza excepcion en caso de haber error
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

    public Region actualizarRegion(Region region){
        //Valida primero que el ID no sea nulo. Si lo es, muestra una excepcion
        //Si no es nulo, actualiza la region existente en la BD
        //retorna la categoria actualizado
        if(region.getIdRegion() == null){
            throw new RegionNoExisteException("Revise que la region exista - actualizarRegion");
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
