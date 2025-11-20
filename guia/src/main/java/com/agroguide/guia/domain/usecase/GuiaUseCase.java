package com.agroguide.guia.domain.usecase;

import com.agroguide.guia.domain.exception.EstadoNoEstablecidoException;
import com.agroguide.guia.domain.exception.GuiaNoExisteException;
import com.agroguide.guia.domain.model.Guia;
import com.agroguide.guia.domain.model.Notificacion;
import com.agroguide.guia.domain.model.UsuarioInfo;
import com.agroguide.guia.domain.model.gateway.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class GuiaUseCase {

    private final GuiaGateway guiaGateway;
    private final NotificationGateway notificationGateway;
    private final UsuarioGateway usuarioGateway;
    private final CultivoGateway cultivoGateway;
    private final CategoriaGateway categoriaGateway;
    private final RegionGateway regionGateway;

    public Guia crearGuia(Guia guia, Long usuarioId) {

        //Validar que el usuario exista
        UsuarioInfo usuarioInfo = usuarioGateway.usuarioExiste(usuarioId);
        if (usuarioInfo == null || usuarioInfo.getNombre() == null) {
            throw new IllegalArgumentException("El usuario no existe en el sistema");
        }

        // Validar roles permitidos
        String rol = usuarioInfo.getTipoUsuario().trim().toUpperCase();

        if (!(rol.equals("ADMINISTRADOR") || rol.equals("TECNICO") || rol.equals("TÉCNICO"))) {
            throw new IllegalArgumentException("Solo ADMINISTRADOR o TECNICO pueden crear guías");
        }

        // Validar atributos obligatorios de la guía
        if (guia.getTitulo() == null || guia.getCultivo() == null ||
                guia.getDescripcion() == null || guia.getRegion() == null
                || guia.getCategoria() == null) {

            throw new IllegalArgumentException("Ingrese atributos correctamente - crearGuia");
        }
        //vaidacion para ver si existen cultivo, region y categoria en la BD
        if (!cultivoGateway.existeCultivo(guia.getCultivo().getIdCultivo())) {
            throw new IllegalArgumentException("El cultivo ingresado no existe");
        }

        if (!regionGateway.existeRegion(guia.getRegion().getIdRegion())) {
            throw new IllegalArgumentException("La región ingresada no existe");
        }

        if (!categoriaGateway.existeCategoria(guia.getCategoria().getIdCategoria())) {
            throw new IllegalArgumentException("La categoría ingresada no existe");
        }

        // Establecer el estado inicial de la guía como pendiente
        guia.setEstadoGuia("PENDIENTE");
        // Registrar quién creó la guía
        guia.setIdTecnico(usuarioId);

        Guia guiaGuardada = guiaGateway.crear(guia, usuarioId);

        // Enviar notificación del estado de la guía al creador de la guía

        Notificacion notificacion = Notificacion.builder()
                .tipo("Estado Guía")
                .email(usuarioInfo.getEmail())
                .numeroTelefono(usuarioInfo.getNumeroTelefono())
                .mensaje("Tu guía '" + guiaGuardada.getTitulo() +
                        "' fue enviada y está PENDIENTE de revisión.")
                .build();

        notificationGateway.enviarMensaje(notificacion);

        return guiaGuardada;
    }


    public void eliminarGuia(Long id) {
        //Elimina la guia por el ID
        //Retorna un vacío
        //Lanza excepcion en caso de haber error
        try {
            guiaGateway.eliminarPorId(id);
        } catch (Exception e) {
            throw new GuiaNoExisteException("Error al eliminar la guía. No existe");
        }
    }

    public Guia consultarGuia(Long id) {
        //Busca una guia por su ID
        //Si no lo encuentra, retorna una guia vacía
        //Si lo encuentra, retorna la guía entera
        try {
            return guiaGateway.consultarPorId(id);
        } catch (Exception e) {
            System.out.println("Error al consultar la guía");

            //crear un producto vacío para retornarlo
            Guia guiaVacia = new Guia();
            return guiaVacia;
        }
    }

    public Guia actualizarEstGuia(Long idGuia, String nuevoEstado, Long usuarioId) {

        //Validar id
        if (idGuia == null) {
            throw new IllegalArgumentException("Debe enviar el ID de la guía");
        }

        // Buscar guía
        Guia guia = guiaGateway.consultarPorId(idGuia);
        if (guia == null) {
            throw new IllegalArgumentException("La guía no existe");
        }

        // Validar usuario
        UsuarioInfo usuarioInfo = usuarioGateway.usuarioExiste(usuarioId);
        if (usuarioInfo == null || usuarioInfo.getNombre() == null) {
            throw new IllegalArgumentException("El usuario no existe");
        }

        // Validar que SOLO ADMIN cambie estado
        String rol = usuarioInfo.getTipoUsuario().trim().toUpperCase();
        if (!rol.equals("ADMINISTRADOR")) {
            throw new IllegalArgumentException("Solo ADMINISTRADORES pueden cambiar el estado de una guía");
        }

        // Validar que mande un nuevo estado
        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            throw new IllegalArgumentException("Debe enviar un estado válido");
        }

        // Actualizar solo el estado
        guia.setEstadoGuia(nuevoEstado);

        // Persistir cambios
        Guia guiaActualizada = guiaGateway.actualizarEstadoGuia(guia);

        // Notificar al creador
        Notificacion notificacion = Notificacion.builder()
                .tipo("Estado Guía")
                .email(usuarioInfo.getEmail())
                .numeroTelefono(usuarioInfo.getNumeroTelefono())
                .mensaje("La guía '" + guiaActualizada.getTitulo() +
                        "' ha cambiado su estado a: " + guiaActualizada.getEstadoGuia())
                .build();

        notificationGateway.enviarMensaje(notificacion);

        return guiaActualizada;
    }


    public List<Guia> consultarGuias(int page, int size) {
        //Obtiene todos las guias existentes en la BD
        //Retorna una lista
        //si hay error en la consulta, atrapa la excepcion
        try {
            return guiaGateway.ListarGuias(page, size);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al consultar las guías existentes");
        }
    }
}