package com.agroguide.guia.infraestructure.entry_points;



import com.agroguide.guia.domain.model.Guia;
import com.agroguide.guia.domain.usecase.GuiaUseCase;
import com.agroguide.guia.infraestructure.driver_adapter.jpa_repository.guia.GuiaData;
import com.agroguide.guia.infraestructure.mapper.MapperGuia;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agroguide/guia")
@RequiredArgsConstructor
public class GuiaController {

    private final GuiaUseCase guiaUseCase;
    private final MapperGuia mapper;

    @PostMapping("/save/{usuarioId}")
    public ResponseEntity<Guia> saveGuia(@RequestBody GuiaData guiaData,
                                         @PathVariable Long usuarioId) {
        Guia guia = mapper.toGuia(guiaData);
        Guia GuiaValidadaGuardada = guiaUseCase.crearGuia(guia, usuarioId); //Ejecuta la lógica de negocio para guardar

        if (GuiaValidadaGuardada.getIdGuia() != null) {
            return ResponseEntity.ok(GuiaValidadaGuardada); //Valida si se guardó bien por medio del ID.
            //Si no es nulo, devuelve un 200 con la guia guardada
        }
        return new  ResponseEntity<>(GuiaValidadaGuardada, HttpStatus.CONFLICT);
        //Si el id es nulo, devuelve un 409
    }

    @DeleteMapping("/{idGuia}")
    public ResponseEntity<String> deleteGuia(@PathVariable Long idGuia) {
        try{
            guiaUseCase.eliminarGuia(idGuia);
            //Si elimina bien, devuelve un 200 con un mensaje de guia eliminada
            return new  ResponseEntity<>("Guia eliminada", HttpStatus.OK);
        }catch (Exception e){
            //Si hay error, atrapa excepcion y retorna un 404
            return new  ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{idGuia}")
    public ResponseEntity<Guia> findByIdGuia(@PathVariable Long idGuia) {
        //Ejecuta lógica de negocio para buscar por ID
        Guia guiaValidadaEncontrada = guiaUseCase.consultarGuia(idGuia);
        if (guiaValidadaEncontrada.getIdGuia() != null) {
            return new  ResponseEntity<>(guiaValidadaEncontrada, HttpStatus.OK);
            //Si existe, retorna 200 con la region
        }
        //Si no existe, retorna un 404 con una guia vacía
        return new  ResponseEntity<>(guiaValidadaEncontrada, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{idGuia}/update")
    public ResponseEntity<Guia> updateGuia(@PathVariable Long idGuia, @RequestParam Long usuarioId,
                                           @RequestBody GuiaData guiaData) {
        try {
            // Convierte GuiaData a dominio Guia
            Guia guiaDatosNuevos = mapper.toGuia(guiaData);

            // Llama al caso de uso con idGuia, datos nuevos y usuarioId
            Guia guiaActualizada = guiaUseCase.actualizarGuia(idGuia, guiaDatosNuevos, usuarioId);

            return new ResponseEntity<>(guiaActualizada, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            // Errores de validación enviados por el caso de uso
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            // Otros errores inesperados
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }



    @GetMapping("/guias")
    public ResponseEntity<List<Guia>> findAllGuias(
            @RequestParam(defaultValue = "0") int page, //la pagina por defecto es 0
            @RequestParam(defaultValue = "10")int size)  //El tamaño por defecto es de 10
    {

        //Ejecuta lógica de negocio para obtener las guias paginadas
        List<Guia> guias = guiaUseCase.consultarGuias(page, size);
        if (guias.isEmpty()) {
            return new  ResponseEntity<>(HttpStatus.CONFLICT); //Si no hay guias, retorna 409
        }
        return new  ResponseEntity<>(guias, HttpStatus.OK); //Si hay guias, retorna la lista paginada con un 200
    }


    @PutMapping("/{idGuia}/estado")
    public ResponseEntity<Guia> updateEstadoGuia(@PathVariable Long idGuia,
                                                 @RequestParam Long usuarioId,
                                                 @RequestParam String nuevoEstado) {
        try {
            // Llamamos al caso de uso con los parámetros directamente
            Guia guiaActualizada = guiaUseCase.actualizarEstGuia(idGuia, nuevoEstado, usuarioId);
            return new ResponseEntity<>(guiaActualizada, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }


}
