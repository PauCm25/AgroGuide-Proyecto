package com.agroguide.guia.infraestructure.driver_adapter.jpa_repository.categoria;


import com.agroguide.guia.domain.exception.CategoriaNoExisteException;
import com.agroguide.guia.domain.model.Categoria;
import com.agroguide.guia.domain.model.gateway.CategoriaGateway;
import com.agroguide.guia.infraestructure.mapper.MapperCategoria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class CategoriaDataGatewayImpl implements CategoriaGateway {

    private final CategoriaDataJpaRepository repository;
    private final MapperCategoria mapper;

    @Override
    public boolean existeCategoria(Long id) {
        try {
            return repository.existsById(id);

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Categoria crear(Categoria categoria) {
        CategoriaData categoriaData = mapper.toCatData(categoria);
        return mapper.toCateg(repository.save(categoriaData));
    }

    @Override
    public void eliminarPorId(Long idCategoria) {
        if (!repository.existsById(idCategoria)){
            throw new CategoriaNoExisteException("La categoria no existe");
        }
        repository.deleteById(idCategoria);
    }

    @Override
    public Categoria consultarPorId(Long idCategoria) {
        return repository.findById(idCategoria)
                .map(mapper::toCateg).
                orElseThrow(() -> new CategoriaNoExisteException("La categoria no existe"));
    }

    @Override
    public Categoria actualizarPorId(Categoria categoria) {
        CategoriaData categoriaData = mapper.toCatData(categoria);
        if (!repository.existsById(categoriaData.getIdCategoria())){
            throw new CategoriaNoExisteException("La categoria no existe");
        }
        return mapper.toCateg(repository.save(categoriaData));
    }


    @Override
    public List<Categoria> ListarCategorias(int page, int size) {
        //crea un obj Pageable con la página y el tamaño deseado
        Pageable pageable = PageRequest.of(page, size);

        //se usa findAll(Pageable) de Spring Data JPA para obtener los datos paginados
        Page<CategoriaData> categoriasExistentes = repository.findAll(pageable);

        //Convierte cada categoriaData en Categoria utilizando el mapper
        //El getContent() devuelve solo la lista de elementos de la página que se haya seleccionado
        return categoriasExistentes.getContent()
                .stream().map(categoriaData -> mapper.toCateg(categoriaData))
                .collect(Collectors.toList());
    }
}
