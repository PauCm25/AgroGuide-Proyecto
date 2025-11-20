package com.agroguide.guia.domain.usecase;

import com.agroguide.guia.domain.exception.EstadoNoEstablecidoException;
import com.agroguide.guia.domain.exception.GuiaNoExisteException;
import com.agroguide.guia.domain.model.Guia;
import com.agroguide.guia.domain.model.gateway.GuiaGateway;
import com.agroguide.guia.domain.model.gateway.NotificationGateway;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class GuiaUseCase {

    private final GuiaGateway guiaGateway;
    private final NotificationGateway notificationGateway;

    public Guia crearGuia(Guia guia){
        //Condicional para hacer que varios atributos sean obligatorios para guardar
        //si ambos son nulos, no se guarda la guia
        if(guia.getTitulo() == null && guia.getCultivo() == null
                && guia.getDescripcion() == null
                && guia.getRegion() == null){
            throw new NullPointerException("Ingrese atributos correctamente - crearGuia");
        }

        guia.setEstadoGuia("PENDIENTE");
        return guiaGateway.crear(guia); //Si esta bien, guarda la guía
    }

    public void eliminarGuia(Long id){
        //Elimina la guia por el ID
        //Retorna un vacío
        //Lanza excepcion en caso de haber error
        try {
            guiaGateway.eliminarPorId(id);
        } catch (Exception e) {
            throw new GuiaNoExisteException("Error al eliminar la guía. No existe");
        }
    }

    public Guia consultarGuia(Long id){
        //Busca una guia por su ID
        //Si no lo encuentra, retorna una guia vacía
        //Si lo encuentra, retorna la guía entera
        try{
            return guiaGateway.consultarPorId(id);
        } catch (Exception e){
            System.out.println("Error al consultar la guía");

            //crear un producto vacío para retornarlo
            Guia guiaVacia = new Guia();
            return guiaVacia;
        }
    }

    public Guia actualizarGuia(Guia guia){
        //Valida primero que el ID no sea nulo. Si lo es, muestra una excepcion
        //Si no es nulo, actualiza la guia existente en la BD
        //retorna la guia actualizado
        if(guia.getIdGuia() == null){
            throw new NullPointerException("Revise que el ID de la guía exista - actualizarGuia");
        }
        return guiaGateway.actualizarPorId(guia);
    }

    public List<Guia> consultarGuias(int page, int size)
    {
        //Obtiene todos las guias existentes en la BD
        //Retorna una lista
        //si hay error en la consulta, atrapa la excepcion
        try {
            return guiaGateway.ListarGuias(page, size);
        } catch(Exception e) {
            throw new IllegalArgumentException("Error al consultar las guías existentes");
        }
    }

    public Guia actualizarEstGuia (Guia guia){
        if (guia.getIdGuia() == null){
            throw new GuiaNoExisteException("No existe la guía");
        }
        if (guia.getEstadoGuia() == null){
            throw new EstadoNoEstablecidoException("La guía no tiene estado");
        }
        return guiaGateway.actualizarEstadoGuia(guia);
    }

}
