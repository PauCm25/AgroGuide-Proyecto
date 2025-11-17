package com.agroguide.guia.infraestructure.driver_adapter.jpa_repository.favoritos;

import com.agroguide.guia.domain.model.Guia;
import com.agroguide.guia.infraestructure.driver_adapter.jpa_repository.guia.GuiaData;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity //Indica que la clase es una entidad de BD
@AllArgsConstructor //genera constructor con todos los parámetros
@NoArgsConstructor //genera constructor vacío
@Table(name = "favoritos") //Crear otra tabla en la BD llamada "favoritos"
@Data //Con Data se obtienen getters, setters, y se utiliza en entidades JPA
public class FavoritosData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idFav;

    private Long idUsuario;
    private String tituloGuia;
    private String autorGuia;

    @ManyToOne
    @JoinColumn(name ="id_guia")
    //ESTO SE CAMBIO de Guia a GuiaData
    private GuiaData guia;

}
