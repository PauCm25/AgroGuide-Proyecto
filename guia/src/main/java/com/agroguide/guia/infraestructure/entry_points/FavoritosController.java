package com.agroguide.guia.infraestructure.entry_points;


import com.agroguide.guia.domain.exception.FavoritoNoExisteException;
import com.agroguide.guia.domain.exception.GuiaNoExisteException;
import com.agroguide.guia.domain.exception.UsuarioNoEncontradoException;
import com.agroguide.guia.domain.model.Favoritos;
import com.agroguide.guia.domain.usecase.FavoritosUseCase;
import com.agroguide.guia.infraestructure.mapper.MapperFavoritos;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agroguide/favoritos")
@RequiredArgsConstructor
public class FavoritosController {

    private final FavoritosUseCase favoritosUseCase;
    private final MapperFavoritos mapper;

    @PostMapping("/favoritos/{idGuia}/usuario/{usuarioId}")
    public ResponseEntity<Favoritos> agregarAFavoritos(@PathVariable Long idGuia, @PathVariable Long usuarioId) {
        try {
            Favoritos favorito = favoritosUseCase.agregarAFavoritos(usuarioId, idGuia);
            return new ResponseEntity<>(favorito, HttpStatus.OK);
        } catch (UsuarioNoEncontradoException | GuiaNoExisteException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/favoritos/{idGuia}/usuario/{usuarioId}")
    public ResponseEntity<String> eliminarDeFavoritos(@PathVariable Long idGuia, @PathVariable Long usuarioId) {
        try {
            favoritosUseCase.eliminarDeFavoritos(idGuia, usuarioId);
            return ResponseEntity.ok("Guia eliminada de favoritos");
        } catch (UsuarioNoEncontradoException | FavoritoNoExisteException e) {
            return ResponseEntity.status(HttpStatus.OK).body("No se encontró la guia en favoritos, ni el usuario");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("Error al eliminar la guia de favoritos");
        }
    }

    @GetMapping("/favoritos/usuario/{usuarioId}")
    public ResponseEntity<List<Favoritos>> ListarFavoritos(
            @PathVariable Long usuarioId, //Se pide el id del usuario para ver solo su lista de favoritos
            @RequestParam(defaultValue = "0") int page, //la pagina por defecto es 0
            @RequestParam(defaultValue = "10")int size)  //El tamaño por defecto es de 10
    {
        //Ejecuta lógica de negocio para obtener los productos paginados
        List<Favoritos> favoritos = favoritosUseCase.consultarFavoritos(usuarioId, page, size);
        if (favoritos.isEmpty()) {
            return new  ResponseEntity<>(HttpStatus.CONFLICT); //Si no hay productos, retorna 409
        }
        return new  ResponseEntity<>(favoritos, HttpStatus.OK); //Si hay productos, retorna la lista paginada con un 200
    }

}
