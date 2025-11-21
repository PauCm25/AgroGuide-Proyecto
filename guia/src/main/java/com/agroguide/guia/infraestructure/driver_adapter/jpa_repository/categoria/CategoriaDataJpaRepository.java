package com.agroguide.guia.infraestructure.driver_adapter.jpa_repository.categoria;


import org.springframework.data.jpa.repository.JpaRepository;
// el extends hereda todos los metodos crud
public interface CategoriaDataJpaRepository extends JpaRepository<CategoriaData,Long> {
}
