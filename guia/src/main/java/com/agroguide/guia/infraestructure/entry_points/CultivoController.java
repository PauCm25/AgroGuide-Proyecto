package com.agroguide.guia.infraestructure.entry_points;


import com.agroguide.guia.domain.model.Cultivo;

import com.agroguide.guia.domain.usecase.CultivoUseCase;
import com.agroguide.guia.infraestructure.driver_adapter.jpa_repository.cultivo.CultivoData;
import com.agroguide.guia.infraestructure.mapper.MapperCultivo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agroguide/cultivo")
@RequiredArgsConstructor
public class CultivoController {

    private final CultivoUseCase cultivoUseCase;
    private final MapperCultivo mapper;

    @PostMapping("/save/{usuarioId}")
    public ResponseEntity<Cultivo> saveCultivo(@RequestBody CultivoData cultivoData,
                                               @PathVariable Long usuarioId) {
        Cultivo cultivo = mapper.toCultivo(cultivoData);
        Cultivo cultivoValidadaGuardada = cultivoUseCase.crearCultivo(cultivo, usuarioId); //Ejecuta la lógica de negocio para guardar

        if (cultivoValidadaGuardada.getIdCultivo() != null) {
            return ResponseEntity.ok(cultivoValidadaGuardada); //Valida si se guardó bien por medio del ID.
            //Si no es nulo, devuelve un 200 con el cultivo guardado
        }
        return new  ResponseEntity<>(cultivoValidadaGuardada, HttpStatus.CONFLICT);
        //Si el id es nulo, devuelve un 409
    }

    @DeleteMapping("/{idCultivo}/{usuarioId}")
    public ResponseEntity<String> deleteCultivo(@PathVariable Long idCultivo,
                                                @PathVariable Long usuarioId) {
        try{
            cultivoUseCase.eliminarCultivo(idCultivo, usuarioId);
            //Si elimina bien, devuelve un 200 con un mensaje de cultivo eliminado
            return new  ResponseEntity<>("Cultivo eliminado", HttpStatus.OK);
        }catch (Exception e){
            //Si hay error, atrapa excepcion y retorna un 404
            return new  ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{idCultivo}")
    public ResponseEntity<Cultivo> findByIdCultivo(@PathVariable Long idCultivo) {
        //Ejecuta lógica de negocio para buscar por ID
        Cultivo cultivoValidadaEncontrada = cultivoUseCase.consultarCultivo(idCultivo);
        if (cultivoValidadaEncontrada.getIdCultivo() != null) {
            return new  ResponseEntity<>(cultivoValidadaEncontrada, HttpStatus.OK);
            //Si existe, retorna 200 con el cultivo
        }
        //Si no existe, retorna un 404 con un cultivo vacío
        return new  ResponseEntity<>(cultivoValidadaEncontrada, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{usuarioId}")
    public ResponseEntity<Cultivo> updateCultivo(@RequestBody CultivoData cultivoData,
                                                 @PathVariable Long usuarioId) {
        try {
            //Convierte cultivoData a cultivo
            Cultivo cultivo = mapper.toCultivo(cultivoData);
            //Usa la lógica de negocio para actualizar
            Cultivo cultivoValidadaActualizada = cultivoUseCase.actualizarCultivo(cultivo,usuarioId);
            //Si todoo esta bien, retorna 200 con cultivo actualizado
            return new  ResponseEntity<>(cultivoValidadaActualizada, HttpStatus.OK);
        } catch (Exception e) {
            //si hay error, atrapa excepcion y retorna un conflict
            return  ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }


    @GetMapping("/cultivos")
    public ResponseEntity<List<Cultivo>> findAllCultivos(
            @RequestParam(defaultValue = "0") int page, //la pagina por defecto es 0
            @RequestParam(defaultValue = "10")int size)  //El tamaño por defecto es de 10
    {

        //Ejecuta lógica de negocio para obtener los cultivos paginados
        List<Cultivo> cultivos = cultivoUseCase.consultarCultivos(page, size);
        if (cultivos.isEmpty()) {
            return new  ResponseEntity<>(HttpStatus.CONFLICT); //Si no hay cultivos, retorna 409
        }
        return new  ResponseEntity<>(cultivos, HttpStatus.OK); //Si hay cultivos, retorna la lista paginada con un 200
    }
}
