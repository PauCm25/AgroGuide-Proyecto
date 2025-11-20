package com.agroguide.guia.domain.usecase;

import com.agroguide.guia.domain.exception.EstadoNoEstablecidoException;
import com.agroguide.guia.domain.exception.GuiaNoExisteException;
import com.agroguide.guia.domain.model.Guia;
import com.agroguide.guia.domain.model.Notificacion;
import com.agroguide.guia.domain.model.UsuarioInfo;
import com.agroguide.guia.domain.model.gateway.*;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
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
        if (guia.getTitulo() == null || guia.getIdCultivo() == null ||
                guia.getDescripcion() == null || guia.getIdRegion() == null
                || guia.getIdCategoria() == null) {

            throw new IllegalArgumentException("Ingrese atributos correctamente - crearGuia");
        }
        if (!cultivoGateway.existeCultivo(guia.getIdCultivo())) {
            throw new IllegalArgumentException("El cultivo ingresado no existe");
        }

        if (!regionGateway.existeRegion(guia.getIdRegion())) {
            throw new IllegalArgumentException("La región ingresada no existe");
        }

        if (!categoriaGateway.existeCategoria(guia.getIdCategoria())) {
            throw new IllegalArgumentException("La categoría ingresada no existe");
        }



        // Establecer el estado inicial de la guía como pendiente
        guia.setEstadoGuia("PENDIENTE");
        // Registrar quién creó la guía
        guia.setIdTecnico(usuarioId);
        // registrar el nombre de quien creo la guia
        guia.setNombreAutor(usuarioInfo.getNombre());
        //registrar la fecha
        guia.setFechaPublicacion(LocalDate.now());

        Guia guiaGuardada = guiaGateway.crear(guia, usuarioId);

        // Enviar notificación del estado de la guía al creador de la guía

        Notificacion notificacion = Notificacion.builder()
                .tipo("Estado Guía")
                .numeroTelefono(usuarioInfo.getTelefono())
                .mensaje("Tu guía '" + guiaGuardada.getTitulo() +
                        "' fue enviada y está PENDIENTE de revisión.")
                .build();

        notificationGateway.enviarMensaje(notificacion);

        return guiaGuardada;
    }


    public void eliminarGuia(Long id, Long usuarioId) {
        // 1. Validar que el usuario exista
        UsuarioInfo usuarioInfo = usuarioGateway.usuarioExiste(usuarioId);
        if (usuarioInfo == null || usuarioInfo.getNombre() == null) {
            throw new IllegalArgumentException("El usuario no existe en el sistema");
        }

        // 2. Validar rol
        String rol = usuarioInfo.getTipoUsuario().trim().toUpperCase();

        if (!(rol.equals("ADMINISTRADOR") || rol.equals("TECNICO") || rol.equals("TÉCNICO"))) {
            throw new IllegalArgumentException("Solo ADMINISTRADOR o TECNICO pueden eliminar guías");
        }

        try {
            guiaGateway.eliminarPorId(id);
        } catch (Exception e) {
            throw new GuiaNoExisteException("Error al eliminar la guía. No existe");
        }
    }

    public Guia actualizarGuia(Long idGuia, Guia guiaDatosNuevos, Long usuarioId) {

        // 1. Validar que el ID no venga nulo
        if (idGuia == null) {
            throw new IllegalArgumentException("Debe enviar el ID de la guía a actualizar");
        }

        // 2. Verificar que la guía exista en BD
        if (!guiaGateway.existeGuia(idGuia)) {
            throw new IllegalArgumentException("La guía no existe");
        }

        // 3. Validar que el usuario exista
        UsuarioInfo usuarioInfo = usuarioGateway.usuarioExiste(usuarioId);
        if (usuarioInfo == null || usuarioInfo.getNombre() == null) {
            throw new IllegalArgumentException("El usuario no existe en el sistema");
        }

        // 4. Validar roles permitidos (solo admin o técnico)
        String rol = usuarioInfo.getTipoUsuario().trim().toUpperCase();
        if (!(rol.equals("ADMINISTRADOR") || rol.equals("TECNICO") || rol.equals("TÉCNICO"))) {
            throw new IllegalArgumentException("Solo ADMINISTRADOR o TECNICO pueden actualizar guías");
        }

        // 5. Cargar guía original
        Guia guiaOriginal = guiaGateway.consultarPorId(idGuia);

        // 6. Validar datos obligatorios
        if (guiaDatosNuevos.getTitulo() == null ||
                guiaDatosNuevos.getDescripcion() == null ||
                guiaDatosNuevos.getIdRegion() == null ||
                guiaDatosNuevos.getIdCultivo() == null ||
                guiaDatosNuevos.getIdCategoria() == null) {
            throw new IllegalArgumentException("Debe enviar todos los datos obligatorios");
        }

        if (!cultivoGateway.existeCultivo(guiaDatosNuevos.getIdCultivo())) {
            throw new IllegalArgumentException("El cultivo ingresado no existe");
        }

        if (!regionGateway.existeRegion(guiaDatosNuevos.getIdRegion())) {
            throw new IllegalArgumentException("La región ingresada no existe");
        }

        if (!categoriaGateway.existeCategoria(guiaDatosNuevos.getIdCategoria())) {
            throw new IllegalArgumentException("La categoría ingresada no existe");
        }


        // 8. Actualizar los campos permitidos
        guiaOriginal.setTitulo(guiaDatosNuevos.getTitulo());
        guiaOriginal.setDescripcion(guiaDatosNuevos.getDescripcion());
        guiaOriginal.setIdCultivo(guiaDatosNuevos.getIdCultivo());
        guiaOriginal.setIdRegion(guiaDatosNuevos.getIdRegion());
        guiaOriginal.setIdCategoria(guiaDatosNuevos.getIdCategoria());
        // registrar el nombre de quien creo la guia
        guiaOriginal.setNombreAutor(usuarioInfo.getNombre());
        //registrar la fecha
        guiaOriginal.setFechaPublicacion(LocalDate.now());

        // 9. Actualizar en BD
        return guiaGateway.actualizarPorId(guiaOriginal);
    }


    public Guia consultarGuia(Long idGuia, Long usuarioId) {

        try {
            // 1. Validar que el usuario exista
            UsuarioInfo usuarioInfo = usuarioGateway.usuarioExiste(usuarioId);
            if (usuarioInfo == null || usuarioInfo.getTipoUsuario() == null) {
                throw new IllegalArgumentException("El usuario no existe en el sistema");
            }

            // 2. Obtener el rol del usuario
            String rol = usuarioInfo.getTipoUsuario().trim().toUpperCase();

            // 3. Buscar la guía
            Guia guia = guiaGateway.consultarPorId(idGuia);
            if (guia == null) {
                return new Guia(); // retornar vacía si no existe
            }

            // 4. Si es agricultor o técnico, solo puede ver guías aprobadas
            if (rol.equals("AGRICULTOR") || rol.equals("TÉCNICO") || rol.equals("TECNICO")) {

                if (guia.getEstadoGuia() == null ||
                        !guia.getEstadoGuia().equalsIgnoreCase("APROBADA")) {

                    // No está aprobada → devolver guía vacía
                    return new Guia();
                }
            }

            // ADMINISTRADOR ve cualquier estado
            return guia;

        } catch (Exception e) {
            System.out.println("Error al consultar la guía");
            return new Guia();
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
                .numeroTelefono(usuarioInfo.getTelefono())
                .mensaje("La guía '" + guiaActualizada.getTitulo() +
                        "' ha cambiado su estado a: " + guiaActualizada.getEstadoGuia())
                .build();

        notificationGateway.enviarMensaje(notificacion);

        return guiaActualizada;
    }


    public List<Guia> consultarGuias(int page, int size, Long usuarioId) {

        try {
            // 1. Validar que el usuario exista
            UsuarioInfo usuarioInfo = usuarioGateway.usuarioExiste(usuarioId);
            if (usuarioInfo == null || usuarioInfo.getTipoUsuario() == null) {
                throw new IllegalArgumentException("El usuario no existe en el sistema");
            }

            // 2. Obtener el rol del usuario
            String rol = usuarioInfo.getTipoUsuario().trim().toUpperCase();

            // 3. Obtener todas las guías de la BD
            List<Guia> guias = guiaGateway.ListarGuias(page, size);

            // 4. Aplicar restricciones según rol
            if (rol.equals("AGRICULTOR") || rol.equals("TÉCNICO") || rol.equals("TECNICO")) {

                // Solo pueden ver guías aprobadas
                guias = guias.stream()
                        .filter(g -> g.getEstadoGuia() != null &&
                                g.getEstadoGuia().equalsIgnoreCase("APROBADA"))
                        .toList();
            }

            // ADMINISTRADOR ve todo → no se filtra
            return guias;

        } catch (Exception e) {
            throw new IllegalArgumentException("Error al consultar las guías existentes");
        }
    }

}