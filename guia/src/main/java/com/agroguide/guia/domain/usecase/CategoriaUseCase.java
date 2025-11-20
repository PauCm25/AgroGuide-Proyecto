package com.agroguide.guia.domain.usecase;

import com.agroguide.guia.domain.exception.CategoriaNoExisteException;
import com.agroguide.guia.domain.model.Categoria;
import com.agroguide.guia.domain.model.UsuarioInfo;
import com.agroguide.guia.domain.model.gateway.CategoriaGateway;
import com.agroguide.guia.domain.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CategoriaUseCase {

    private final CategoriaGateway categoriaGateway;
    private final UsuarioGateway usuarioGateway;

    public Categoria crearCategoria(Categoria categoria, Long usuarioId) {
        // Validar que el usuario exista
        UsuarioInfo usuarioInfo = usuarioGateway.usuarioExiste(usuarioId);
        if (usuarioInfo == null || usuarioInfo.getNombre() == null) {
            throw new IllegalArgumentException("El usuario no existe en el sistema");
        }

        // Validar rol permitido (SOLO ADMIN)
        String rol = usuarioInfo.getTipoUsuario().trim().toUpperCase();
        if (!(rol.equals("ADMINISTRADOR"))) {
            throw new IllegalArgumentException("Solo ADMINISTRADOR puede crear categorias");
        }
        if(categoria.getNombreCategoria() == null && categoria.getDescripcionCategoria() == null){
            throw new NullPointerException("Ingrese atributos correctamente - crearCategoria");
        }
        return categoriaGateway.crear(categoria); //Si esta bien, guarda la categoria
    }

    public void eliminarCategoria(Long id, Long usuarioId){
        // Validar que el usuario exista
        UsuarioInfo usuarioInfo = usuarioGateway.usuarioExiste(usuarioId);
        if (usuarioInfo == null || usuarioInfo.getNombre() == null) {
            throw new IllegalArgumentException("El usuario no existe en el sistema");
        }

        // Validar rol permitido (SOLO ADMIN)
        String rol = usuarioInfo.getTipoUsuario().trim().toUpperCase();
        if (!(rol.equals("ADMINISTRADOR"))) {
            throw new IllegalArgumentException("Solo ADMINISTRADOR puede eliminar categorias");
        }
        try {
            categoriaGateway.eliminarPorId(id);
        } catch (Exception e) {
            throw new CategoriaNoExisteException("Error al eliminar la categoria. No existe");
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

    public Categoria actualizarCategoria(Categoria categoria, Long usuarioId){
        // Validar que el usuario exista
        UsuarioInfo usuarioInfo = usuarioGateway.usuarioExiste(usuarioId);
        if (usuarioInfo == null || usuarioInfo.getNombre() == null) {
            throw new IllegalArgumentException("El usuario no existe en el sistema");
        }

        // Validar rol permitido (SOLO ADMIN)
        String rol = usuarioInfo.getTipoUsuario().trim().toUpperCase();
        if (!(rol.equals("ADMINISTRADOR"))) {
            throw new IllegalArgumentException("Solo ADMINISTRADOR puede actualizar categorias");
        }
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
