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

    private final RestTemplate restTemplate;
    private final MapperUsuarioInfo mapper;

    @Override
    public UsuarioInfo usuarioExiste(Long usuarioId) {
        try {
            UsuarioInfoDTO dto = restTemplate
                    .getForEntity(
                            "http://localhost:8080/api/agroguide/usuario/" + usuarioId,
                            UsuarioInfoDTO.class
                    )
                    .getBody();

            if (dto == null) {
                throw new RuntimeException("Respuesta vacía desde Auth");
            }

            return mapper.toUsuarioInfo(dto);

        } catch (HttpClientErrorException.NotFound e) {
            // Auth respondió 404 → usuario NO existe
            throw new IllegalArgumentException("Usuario no existe");
        } catch (Exception e) {
            // Cualquier otro error → Auth caído / timeout / 500 / etc
            throw new RuntimeException("Error al consultar el microservicio de Auth");
        }
    }
}
