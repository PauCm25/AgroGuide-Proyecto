package com.agroguide.guia.domain.usecase;


import com.agroguide.guia.domain.exception.CultivoNoExisteException;

import com.agroguide.guia.domain.model.Cultivo;
import com.agroguide.guia.domain.model.UsuarioInfo;
import com.agroguide.guia.domain.model.gateway.CultivoGateway;
import com.agroguide.guia.domain.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CultivoUseCase {

    private final CultivoGateway cultivoGateway;
    private final UsuarioGateway usuarioGateway;

    public Cultivo crearCultivo(Cultivo cultivo, Long usuarioId) {
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

        if(cultivo.getNombreCultivo() == null && cultivo.getTipoSuelo() == null
                && cultivo.getClimaRecomendado() == null){
            throw new NullPointerException("Ingrese atributos correctamente - crearCultivo");
        }
        return cultivoGateway.crear(cultivo); //Si esta bien, guarda el cultivo
    }

    public void eliminarCultivo(Long id, Long usuarioId){
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
        try {
            cultivoGateway.eliminarPorId(id);
        } catch (Exception e) {
            throw new CultivoNoExisteException("Error al eliminar la guía. No existe");
        }
    }

    public Cultivo consultarCultivo(Long id){
        //Busca un cultivo por su ID
        //Si no lo encuentra, retorna un cultivo vacío
        //Si lo encuentra, retorna el cultivo entera
        try{
            return cultivoGateway.consultarPorId(id);
        } catch (Exception e){
            System.out.println("Error al consultar el cultivo");

            //crear un producto vacío para retornarlo
            Cultivo cultVacio = new Cultivo();
            return cultVacio;
        }
    }

    public Cultivo actualizarCultivo(Cultivo cultivo, Long usuarioId){
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
        if(cultivo.getIdCultivo() == null){
            throw new CultivoNoExisteException("Revise que el cultivo exista - actualizarCultivo");
        }
        return cultivoGateway.actualizarPorId(cultivo);
    }

    public List<Cultivo> consultarCultivos(int page, int size)
    {
        //Obtiene todos los cultivos existentes en la BD
        //Retorna una lista
        //si hay error en la consulta, atrapa la excepcion
        try {
            return cultivoGateway.ListarCultivos(page, size);
        } catch(Exception e) {
            throw new IllegalArgumentException("Error al consultar los cultivos existentes");
        }
    }
}
