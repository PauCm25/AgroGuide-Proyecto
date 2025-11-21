package com.agroguide.guia.application.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    //Consumir APS rest externas
    //hacer peticiones htpp como get, post,etc
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
