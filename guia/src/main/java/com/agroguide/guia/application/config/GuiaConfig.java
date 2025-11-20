package com.agroguide.guia.application.config;

import com.agroguide.guia.domain.model.gateway.*;
import com.agroguide.guia.domain.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GuiaConfig {

    @Bean
    public FavoritosUseCase favoritosUseCase(FavoritosGateway favoritosGateway,
                                             GuiaGateway guiaGateway,
                                             UsuarioGateway usuarioGateway) {
        return new FavoritosUseCase(favoritosGateway, guiaGateway, usuarioGateway);
    }

    @Bean
    public CategoriaUseCase categoriaUseCase(CategoriaGateway categoriaGateway) {
        return new CategoriaUseCase(categoriaGateway);
    }

    @Bean
    public CultivoUseCase cultivoUseCase(CultivoGateway cultivoGateway) {
        return new CultivoUseCase(cultivoGateway);
    }

    @Bean
    public RegionUseCase regionUseCase(RegionGateway regionGateway) {
        return new RegionUseCase(regionGateway);
    }

    @Bean
    public GuiaUseCase guiaUseCase(GuiaGateway guiaGateway, NotificationGateway notificationGateway,
                                   UsuarioGateway usuarioGateway) {
        return new  GuiaUseCase(guiaGateway, notificationGateway, usuarioGateway);
    }
}
