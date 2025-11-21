package com.agroguide.guia.application.config;

import com.agroguide.guia.domain.model.gateway.*;
import com.agroguide.guia.domain.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//se utiliza para crear casos de uso
@Configuration
public class GuiaConfig {

    @Bean
    public FavoritosUseCase favoritosUseCase(FavoritosGateway favoritosGateway,
                                             GuiaGateway guiaGateway,
                                             UsuarioGateway usuarioGateway) {
        //Caso inyectado en el caso de uso de los gateway
        return new FavoritosUseCase(favoritosGateway, guiaGateway, usuarioGateway);
    }

    @Bean
    public CategoriaUseCase categoriaUseCase(CategoriaGateway categoriaGateway,
                                             UsuarioGateway usuarioGateway) {
        return new CategoriaUseCase(categoriaGateway, usuarioGateway);
    }

    @Bean
    public CultivoUseCase cultivoUseCase(CultivoGateway cultivoGateway,
                                         UsuarioGateway usuarioGateway) {
        return new CultivoUseCase(cultivoGateway, usuarioGateway);
    }

    @Bean
    public RegionUseCase regionUseCase(RegionGateway regionGateway,
                                       UsuarioGateway usuarioGateway) {
        return new RegionUseCase(regionGateway, usuarioGateway);
    }

    @Bean
    public GuiaUseCase guiaUseCase(GuiaGateway guiaGateway, NotificationGateway notificationGateway,
                                   UsuarioGateway usuarioGateway, CultivoGateway cultivoGateway,
                                   CategoriaGateway categoriaGateway,
                                   RegionGateway regionGateway) {
        return new  GuiaUseCase(guiaGateway, notificationGateway, usuarioGateway,
                cultivoGateway, categoriaGateway, regionGateway);
    }
}
