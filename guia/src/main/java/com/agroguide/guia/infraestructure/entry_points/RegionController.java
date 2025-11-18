package com.agroguide.guia.infraestructure.entry_points;



import com.agroguide.guia.domain.model.Region;
import com.agroguide.guia.domain.usecase.RegionUseCase;
import com.agroguide.guia.infraestructure.driver_adapter.jpa_repository.region.RegionData;
import com.agroguide.guia.infraestructure.mapper.MapperRegion;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agroguide/region")
@RequiredArgsConstructor
public class RegionController {

    private final RegionUseCase regionUseCase;
    private final MapperRegion mapper;

    @PostMapping("/save")
    public ResponseEntity<Region> saveRegion(@RequestBody RegionData regionData) {
        Region region = mapper.toRegion(regionData);
        Region RegionValidadaGuardada = regionUseCase.crearRegion(region); //Ejecuta la lógica de negocio para guardar

        if (RegionValidadaGuardada.getIdRegion() != null) {
            return ResponseEntity.ok(RegionValidadaGuardada); //Valida si se guardó bien por medio del ID.
            //Si no es nulo, devuelve un 200 con la region guardada
        }
        return new  ResponseEntity<>(RegionValidadaGuardada, HttpStatus.CONFLICT);
        //Si el id es nulo, devuelve un 409
    }

    @DeleteMapping("/{idRegion}")
    public ResponseEntity<String> deleteRegion(@PathVariable Long idRegion) {
        try{
            regionUseCase.eliminarRegion(idRegion);
            //Si elimina bien, devuelve un 200 con un mensaje de region eliminada
            return new  ResponseEntity<>("Region eliminada", HttpStatus.OK);
        }catch (Exception e){
            //Si hay error, atrapa excepcion y retorna un 404
            return new  ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{idRegion}")
    public ResponseEntity<Region> findByIdRegion(@PathVariable Long idRegion) {
        //Ejecuta lógica de negocio para buscar por ID
        Region regionValidadaEncontrada = regionUseCase.consultarRegion(idRegion);
        if (regionValidadaEncontrada.getIdRegion() != null) {
            return new  ResponseEntity<>(regionValidadaEncontrada, HttpStatus.OK);
            //Si existe, retorna 200 con la region
        }
        //Si no existe, retorna un 404 con una region vacía
        return new  ResponseEntity<>(regionValidadaEncontrada, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update")
    public ResponseEntity<Region> updateRegion(@RequestBody RegionData regionData) {
        try {
            //Convierte regionData a region
            Region region = mapper.toRegion(regionData);
            //Usa la lógica de negocio para actualizar
            Region regionValidadaActualizada = regionUseCase.actualizarRegion(region);
            //Si todoo esta bien, retorna 200 con region actualizada
            return new  ResponseEntity<>(regionValidadaActualizada, HttpStatus.OK);
        } catch (Exception e) {
            //si hay error, atrapa excepcion y retorna un conflict sin cuerpo
            return  ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }


    @GetMapping("/regiones")
    public ResponseEntity<List<Region>> findAllRegiones(
            @RequestParam(defaultValue = "0") int page, //la pagina por defecto es 0
            @RequestParam(defaultValue = "10")int size)  //El tamaño por defecto es de 10
    {

        //Ejecuta lógica de negocio para obtener las regiones paginadas
        List<Region> regiones = regionUseCase.consultarRegiones(page, size);
        if (regiones.isEmpty()) {
            return new  ResponseEntity<>(HttpStatus.CONFLICT); //Si no hay regiones, retorna 409
        }
        return new  ResponseEntity<>(regiones, HttpStatus.OK); //Si hay regiones, retorna la lista paginada con un 200
    }
}
