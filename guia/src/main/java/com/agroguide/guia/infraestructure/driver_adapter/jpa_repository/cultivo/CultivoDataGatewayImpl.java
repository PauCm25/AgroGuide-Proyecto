package com.agroguide.guia.infraestructure.driver_adapter.jpa_repository.cultivo;


import com.agroguide.guia.domain.exception.CultivoNoExisteException;
import com.agroguide.guia.domain.model.Cultivo;
import com.agroguide.guia.domain.model.gateway.CultivoGateway;

import com.agroguide.guia.infraestructure.mapper.MapperCultivo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
//El dominio solo conoce CultivoGateway, nunca JPA.
public class CultivoDataGatewayImpl implements CultivoGateway {
    //el repositorio se comunica con la bd
    private final CultivoDataJpaRepository repository;
    //convierte entidades del dominio y de la  BD
    private final MapperCultivo mapper;

    @Override
    public boolean existeCultivo(Long id) {
        try {
            //consulta a la bd usando existsById
            return repository.existsById(id);

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Cultivo crear(Cultivo cultivo) {
        CultivoData cultivoData = mapper.toCultData(cultivo);
        //guarda en la Bd
        return mapper.toCultivo(repository.save(cultivoData));
    }

    @Override
    public void eliminarPorId(Long idCultivo) {
        //valida si existe
        if (!repository.existsById(idCultivo)){
            throw new CultivoNoExisteException("El cultivo no existe");
        }
        //si existe, lo elimina
        repository.deleteById(idCultivo);
    }

    @Override
    public Cultivo consultarPorId(Long idCultivo) {
        //busca en la bd por su id
        return repository.findById(idCultivo)
                //convierte cultivoData a Cultivo
                .map(mapper::toCultivo).
                orElseThrow(() -> new CultivoNoExisteException("El cultivo no existe"));
    }

    @Override
    public Cultivo actualizarPorId(Cultivo cultivo) {
        CultivoData cultivoData = mapper.toCultData(cultivo);
        if (!repository.existsById(cultivoData.getIdCultivo())){
            throw new CultivoNoExisteException("El cultivo no existe");
        }
        return mapper.toCultivo(repository.save(cultivoData));
    }


    @Override
    public List<Cultivo> ListarCultivos(int page, int size) {
        //crea un obj Pageable con la página y el tamaño deseado
        Pageable pageable = PageRequest.of(page, size);

        //se usa findAll(Pageable) de Spring Data JPA para obtener los datos paginados
        Page<CultivoData> cultivosExistentes = repository.findAll(pageable);

        //Convierte cada cultivoData en Cultivo utilizando el mapper
        //El getContent() devuelve solo la lista de elementos de la página que se haya seleccionado
        return cultivosExistentes.getContent()
                .stream().map(cultivoData -> mapper.toCultivo(cultivoData))
                .collect(Collectors.toList());
    }
}
