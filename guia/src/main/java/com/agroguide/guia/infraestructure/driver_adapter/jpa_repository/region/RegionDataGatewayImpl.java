package com.agroguide.guia.infraestructure.driver_adapter.jpa_repository.region;


import com.agroguide.guia.domain.exception.RegionNoExisteException;
import com.agroguide.guia.domain.model.Region;
import com.agroguide.guia.domain.model.gateway.RegionGateway;

import com.agroguide.guia.infraestructure.mapper.MapperRegion;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class RegionDataGatewayImpl implements RegionGateway {

    private final RegionDataJpaRepository repository;
    private final MapperRegion mapper;

    @Override
    public Region crear(Region region) {
        RegionData regionData = mapper.toRegData(region);
        return mapper.toRegion(repository.save(regionData));
    }

    @Override
    public void eliminarPorId(Long idRegion) {
        if (!repository.existsById(idRegion)) {
            throw new RegionNoExisteException("La región no existe");
        }
        repository.deleteById(idRegion);
    }

    @Override
    public Region consultarPorId(Long idRegion) {
        return repository.findById(idRegion)
                .map(mapper::toRegion).
                orElseThrow(() -> new RegionNoExisteException("La región no existe"));
    }

    @Override
    public Region actualizarPorId(Region region) {
        RegionData regionData = mapper.toRegData(region);
        if (!repository.existsById(regionData.getIdRegion())) {
            throw new RegionNoExisteException("La región no existe");
        }
        return mapper.toRegion(repository.save(regionData));
    }


    @Override
    public List<Region> ListarRegiones(int page, int size) {
        //crea un obj Pageable con la página y el tamaño deseado
        Pageable pageable = PageRequest.of(page, size);

        //se usa findAll(Pageable) de Spring Data JPA para obtener los datos paginados
        Page<RegionData> regionesExistentes = repository.findAll(pageable);

        //Convierte cada regionData en Region utilizando el mapper
        //El getContent() devuelve solo la lista de elementos de la página que se haya seleccionado
        return regionesExistentes.getContent()
                .stream().map(regionData -> mapper.toRegion(regionData))
                .collect(Collectors.toList());
    }
}
