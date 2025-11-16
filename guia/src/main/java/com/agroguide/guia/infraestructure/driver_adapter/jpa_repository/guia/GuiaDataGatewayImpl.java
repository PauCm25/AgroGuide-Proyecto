package com.agroguide.guia.infraestructure.driver_adapter.jpa_repository.guia;

import com.agroguide.guia.domain.model.Guia;
import com.agroguide.guia.domain.model.gateway.GuiaGateway;
import com.agroguide.guia.infraestructure.mapper.MapperGuia;
import lombok.RequiredArgsConstructor;
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
    public Guia crear(Guia guia){
        GuiaData guiaData = mapper.toGuiaData(guia);
        return mapper.toDomain(repository.save(guiaData));
    }

    @Override
    public void eliminarPorId(Long idGuia) {
        repository.deleteById(idGuia);

    }

    @Override
    public Guia consultarPorId(Long idGuia) {
        return repository.findById(idGuia).map(mapper::toDomain).orElse(null);
    }

    @Override
    public Guia actualizarPorId(Guia guia) {
        GuiaData guiaData = mapper.toGuiaData(guia);
        return mapper.toDomain(repository.save(guiaData));
    }
//PENDIENTE
    @Override
    public List<Guia> ListarGuias(int pagina, int tamaño) {
        //cuanros elementos se desea cargar y que pagina
        Pageable pageable = PageRequest.of(pagina, tamaño);
        //trae solo los registro de esa pagina
        return repository.findAll(pageable).stream()
                .map(mapper::toDomain)
                //lo convierte en una lista
                .collect(Collectors.toList());
    }

    @Override
    public Guia actualizarEstadoGuia(Guia guia) {
        GuiaData guiaData = mapper.toGuiaData(guia);
        return mapper.toDomain(repository.save(guiaData));
    }
}
