package com.agroguide.guia.infraestructure.driver_adapter.jpa_repository.region;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity //Indica que la clase es una entidad de BD
@AllArgsConstructor //genera constructor con todos los parámetros
@NoArgsConstructor //genera constructor vacío
@Table(name = "regiones") //Crear otra tabla en la BD llamada "regiones"
@Data //Con Data se obtienen getters, setters, y se utiliza en entidades JPA
public class RegionData {
}
