package com.agroguide.guia.infraestructure.driver_adapter.jpa_repository.favoritos;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoritosDataJpaRepository extends JpaRepository<FavoritosData,Long> {
    Page<FavoritosData> findByUsuarioId(Long usuarioId, Pageable pageable);
    Optional<FavoritosData> findByUsuarioIdAndGuia_IdGuia(Long usuarioId, Long idGuia);
}
