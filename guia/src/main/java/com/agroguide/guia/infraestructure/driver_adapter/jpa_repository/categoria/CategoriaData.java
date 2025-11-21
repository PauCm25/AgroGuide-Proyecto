package com.agroguide.guia.infraestructure.driver_adapter.jpa_repository.categoria;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity //Indica que la clase es una entidad de BD
@AllArgsConstructor //genera constructor con todos los parámetros
@NoArgsConstructor //genera constructor vacío
@Table(name = "categorias") //Crear otra tabla en la BD llamada "categorias"
@Data //Con Data se obtienen getters, setters, y se utiliza en entidades JPA
public class CategoriaData {

    @Id
    //Marca el campo como clase primaria en la tabla
    //AUTO: genera el id incrementa, secuecnia
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idCategoria;

    private String nombreCategoria;
    private String descripcionCategoria;
}
