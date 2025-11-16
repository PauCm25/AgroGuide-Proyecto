package com.agroguide.guia.infraestructure.driver_adapter.jpa_repository.guia;

import com.agroguide.guia.domain.model.Favoritos;
import com.agroguide.guia.infraestructure.driver_adapter.jpa_repository.categoria.CategoriaData;
import com.agroguide.guia.infraestructure.driver_adapter.jpa_repository.cultivo.CultivoData;
import com.agroguide.guia.infraestructure.driver_adapter.jpa_repository.region.RegionData;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity //Indica que la clase es una entidad de BD
@AllArgsConstructor //genera constructor con todos los parámetros
@NoArgsConstructor //genera constructor vacío
@Table(name = "guias") //Crear otra tabla en la BD llamada "guias"
@Data //Con Data se obtienen getters, setters, y se utiliza en entidades JPA
public class GuiaData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idGuia;


    private Long idTecnico;
    private String titulo;
    private String descripcion;

    private String fechaPublicacion;
    private String nombreAutor;
    private String etiquetas;

    private List<Favoritos> favs;

    private String estadoGuia;

    @ManyToOne
    @JoinColumn(name = "id_cultivo")
    private CultivoData cultivos;

    @ManyToOne
    @JoinColumn(name = "id_region")
    private RegionData regiones;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private CategoriaData categorias;
}
