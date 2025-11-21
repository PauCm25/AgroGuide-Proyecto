package com.agroguide.guia.infraestructure.driver_adapter.jpa_repository.cultivo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity //Indica que la clase es una entidad de BD
@AllArgsConstructor //genera constructor con todos los parámetros
@NoArgsConstructor //genera constructor vacío
@Table(name = "cultivos") //Crear otra tabla en la BD llamada "cultivos"
@Data //Con Data se obtienen getters, setters, y se utiliza en entidades JPA
public class CultivoData {
    //indica que es el atributo clave primaria de la tabla
    @Id
    //auto: la bd decide como generar automaticamente el id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idCultivo;

    private String nombreCultivo;
    private String tipoSuelo;
    private String climaRecomendado;
}
