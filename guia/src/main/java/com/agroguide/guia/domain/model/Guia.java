package com.agroguide.guia.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

//SIEMPRE DEBEN IR ESTAS ETIQUETAS
@AllArgsConstructor //CREACION DEL CONSTRUCTOR
@NoArgsConstructor
@Setter //MODIFICA ATRIBUTO
@Getter //OBTENER ATRIBUTO
public class Guia {

    private Long idGuia;
    private Long idTecnico;
    private String titulo;
    private String descripcion;
    private LocalDate fechaPublicacion;
    private String nombreAutor;
    private String estadoGuia;

    // SOLO IDS DEL JSON
    private Long idCultivo;
    private Long idRegion;
    private Long idCategoria;

}


