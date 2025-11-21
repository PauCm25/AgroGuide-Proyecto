package com.agroguide.guia.infraestructure.driver_adapter.external_repository;


import com.agroguide.guia.domain.model.UsuarioInfo;
import com.agroguide.guia.domain.model.gateway.UsuarioGateway;
import com.agroguide.guia.infraestructure.mapper.MapperUsuarioInfo;
import com.agroguide.guia.infraestructure.message_broker.dto.UsuarioInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Component
public class UsuarioGatewayImpl implements UsuarioGateway {
//Inyecta dependencias por el contructtir
    //Rest template: se utiliza para consumir el microservicio auth
    private final RestTemplate restTemplate;
    private final MapperUsuarioInfo mapper;

    @Override
    public UsuarioInfo usuarioExiste(Long usuarioId) {
        try {
            //llama al  get del microservicio auth para la info de usuario
            UsuarioInfoDTO dto = restTemplate
                    .getForEntity(
                            "https://agroguide-auth-1zd2.onrender.com/api/agroguide/usuario/" + usuarioId,
                            UsuarioInfoDTO.class
                    )
                    .getBody();//extrae el cuerpo de respuesta
            //valida el microservicio
            if (dto == null) {
                throw new RuntimeException("Respuesta vacía desde Auth");
            }
            //convierte el dto en un objeto dominio
            return mapper.toUsuarioInfo(dto);

        } catch (HttpClientErrorException.NotFound e) {
            // Auth respondió 404,  significa que usuario NO existe
            throw new IllegalArgumentException("Usuario no existe");
        } catch (Exception e) {
            // Cualquier otro error, Auth caído / timeout / 500 / etc
            throw new RuntimeException("Error al consultar el microservicio de Auth");
        }
    }
}
