package com.agroguide.guia.domain.usecase;

import com.agroguide.guia.domain.exception.CategoriaNoExisteException;
import com.agroguide.guia.domain.model.Categoria;
import com.agroguide.guia.domain.model.gateway.CategoriaGateway;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CategoriaUseCase {

    private final CategoriaGateway categoriaGateway;

    public Categoria crearCategoria(Categoria categoria) {
        //Condicional para hacer que nombre y descripción sean obligatorios
        //si ambos son nulos, no se guarda la categoría
        if(categoria.getNombreCategoria() == null && categoria.getDescripcionCategoria() == null){
            throw new NullPointerException("Ingrese atributos correctamente - crearCategoria");
        }
        return categoriaGateway.crear(categoria); //Si esta bien, guarda la categoria
    }

    public void eliminarCategoria(Long id){
        //Elimina la categoria por el ID
        //Retorna un vacío
        //Lanza excepcion en caso de haber error
        try {
            categoriaGateway.eliminarPorId(id);
        } catch (Exception e) {
            throw new CategoriaNoExisteException("Error al eliminar la guía. No existe");
        }
    }

    public Categoria consultarCategoria(Long id){
        //Busca una categoria por su ID
        //Si no lo encuentra, retorna una categoria vacía
        //Si lo encuentra, retorna la categoria entera
        try{
            return categoriaGateway.consultarPorId(id);
        } catch (Exception e){
            System.out.println("Error al consultar la categoria");

            //crear un producto vacío para retornarlo
            Categoria catVacia = new Categoria();
            return catVacia;
        }
    }

    public Categoria actualizarCategoria(Categoria categoria){
        //Valida primero que el ID no sea nulo. Si lo es, muestra una excepcion
        //Si no es nulo, actualiza la categoria existente en la BD
        //retorna la categoria actualizado
        if(categoria.getIdCategoria() == null){
            throw new CategoriaNoExisteException("Revise que la categoria exista - actualizarCategoria");
        }
        return categoriaGateway.actualizarPorId(categoria);
    }

    public List<Categoria> consultarCategorias(int page, int size)
    {
        //Obtiene todos las categorias existentes en la BD
        //Retorna una lista
        //si hay error en la consulta, atrapa la excepcion
        try {
            return categoriaGateway.ListarCategorias(page, size);
        } catch(Exception e) {
            throw new IllegalArgumentException("Error al consultar las categorias existentes");
        }
    }
}
