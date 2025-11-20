package com.agroguide.guia.infraestructure.driver_adapter.jpa_repository.guia;

import com.agroguide.guia.domain.exception.GuiaNoExisteException;
import com.agroguide.guia.domain.model.Guia;
import com.agroguide.guia.domain.model.gateway.GuiaGateway;
import com.agroguide.guia.infraestructure.mapper.MapperGuia;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class GuiaDataGatewayImpl implements GuiaGateway {
    private final GuiaDataJpaRepository repository;
    private final MapperGuia mapper;

    @Override
    public Guia crear(Guia guia, Long usuarioId) {
        GuiaData guiaData = mapper.toGuiaData(guia);
        return mapper.toGuia(repository.save(guiaData));
    }

    @Override
    public void eliminarPorId(Long idGuia) {
        if (!repository.existsById(idGuia)){
            throw new GuiaNoExisteException("La guia no existe");
        }
        repository.deleteById(idGuia);
    }

    @Override
    public Guia consultarPorId(Long idGuia) {
        return repository.findById(idGuia)
                .map(mapper::toGuia).
                orElseThrow(() -> new  GuiaNoExisteException("La guia no existe"));
    }

    @Override
    public Guia actualizarPorId(Guia guia) {
        GuiaData guiaData = mapper.toGuiaData(guia);
        if (!repository.existsById(guia.getIdGuia())){
            throw new GuiaNoExisteException("La guia no existe");
        }
        return mapper.toGuia(repository.save(guiaData));
    }


    @Override
    public List<Guia> ListarGuias(int page, int size) {
        //crea un obj Pageable con la página y el tamaño deseado
        Pageable pageable = PageRequest.of(page, size);

        //se usa findAll(Pageable) de Spring Data JPA para obtener los datos paginados
        Page<GuiaData> guiasExistentes = repository.findAll(pageable);

        //Convierte cada GuiaData en Guia utilizando el mapper
        //El getContent() devuelve solo la lista de elementos de la página que se haya seleccionado
        return guiasExistentes.getContent()
                .stream().map(guiaData -> mapper.toGuia(guiaData))
                .collect(Collectors.toList());
    }

    @Override
    public Guia actualizarEstadoGuia(Guia guia) {
        GuiaData guiaData = repository.findById(guia.getIdGuia())
                .orElseThrow(() -> new GuiaNoExisteException("La guía no existe"));

        guiaData.setEstadoGuia(guia.getEstadoGuia());
        return mapper.toGuia(repository.save(guiaData));
    }
}
