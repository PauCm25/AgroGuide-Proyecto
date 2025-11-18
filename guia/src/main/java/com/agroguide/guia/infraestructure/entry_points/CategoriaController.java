package com.agroguide.guia.infraestructure.entry_points;


import com.agroguide.guia.domain.model.Categoria;
import com.agroguide.guia.domain.usecase.CategoriaUseCase;
import com.agroguide.guia.infraestructure.driver_adapter.jpa_repository.categoria.CategoriaData;
import com.agroguide.guia.infraestructure.mapper.MapperCategoria;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agroguide/categoria")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaUseCase categoriaUseCase;
    private final MapperCategoria mapper;

    @PostMapping("/save")
    public ResponseEntity<Categoria> saveCategoria(@RequestBody CategoriaData categoriaData) {
        Categoria categoria = mapper.toCateg(categoriaData);
        Categoria catValidadaGuardada = categoriaUseCase.crearCategoria(categoria); //Ejecuta la lógica de negocio para guardar

        if (catValidadaGuardada.getIdCategoria() != null) {
            return ResponseEntity.ok(catValidadaGuardada); //Valida si se guardó bien por medio del ID.
            //Si no es nulo, devuelve un 200 con la categoria guardada
        }
        return new  ResponseEntity<>(catValidadaGuardada, HttpStatus.CONFLICT);
        //Si el id es nulo, devuelve un 409
    }

    @DeleteMapping("/{idCategoria}")
    public ResponseEntity<String> deleteCategoria(@PathVariable Long idCategoria) {
        try{
            categoriaUseCase.eliminarCategoria(idCategoria);
            //Si elimina bien, devuelve un 200 con un mensaje de categoria eliminada
            return new  ResponseEntity<>("Categoria eliminada", HttpStatus.OK);
        }catch (Exception e){
            //Si hay error, atrapa excepcion y retorna un 404
            return new  ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{idCategoria}")
    public ResponseEntity<Categoria> findByIdCategoria(@PathVariable Long idCategoria) {
        //Ejecuta lógica de negocio para buscar por ID
        Categoria categoriaValidadaEncontrada = categoriaUseCase.consultarCategoria(idCategoria);
        if (categoriaValidadaEncontrada.getIdCategoria() != null) {
            return new  ResponseEntity<>(categoriaValidadaEncontrada, HttpStatus.OK);
            //Si existe, retorna 200 con la categoria
        }
        //Si no existe, retorna un 404 con una categoria vacía
        return new  ResponseEntity<>(categoriaValidadaEncontrada, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update")
    public ResponseEntity<Categoria> updateCategoria(@RequestBody CategoriaData categoriaData) {
        try {
            //Convierte categoriaData a categoria
            Categoria categoria = mapper.toCateg(categoriaData);
            //Usa la lógica de negocio para actualizar
            Categoria categoriaValidadaActualizada = categoriaUseCase.actualizarCategoria(categoria);
            //Si todoo esta bien, retorna 200 con categoria actualizada
            return new  ResponseEntity<>(categoriaValidadaActualizada, HttpStatus.OK);
        } catch (Exception e) {
            //si hay error, atrapa excepcion y retorna un conflict sin cuerpo
            return  ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }


    @GetMapping("/categorias")
    public ResponseEntity<List<Categoria>> findAllCategorias(
            @RequestParam(defaultValue = "0") int page, //la pagina por defecto es 0
            @RequestParam(defaultValue = "10")int size)  //El tamaño por defecto es de 10
    {

        //Ejecuta lógica de negocio para obtener las categorias paginadas
        List<Categoria> categorias = categoriaUseCase.consultarCategorias(page, size);
        if (categorias.isEmpty()) {
            return new  ResponseEntity<>(HttpStatus.CONFLICT); //Si no hay categorias, retorna 409
        }
        return new  ResponseEntity<>(categorias, HttpStatus.OK); //Si hay categorias, retorna la lista paginada con un 200
    }
}
