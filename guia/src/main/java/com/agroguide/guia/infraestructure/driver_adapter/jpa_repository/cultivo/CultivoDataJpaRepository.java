package com.agroguide.guia.infraestructure.driver_adapter.jpa_repository.cultivo;

import org.springframework.data.jpa.repository.JpaRepository;
//extends= hereda  los metodos crus
public interface CultivoDataJpaRepository extends JpaRepository<CultivoData,Long> {
}
