package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.SolicitudReposicionRequestDto;
import edu.upb.optifacotry.repository.dto.response.SolicitudReposicionResponseDto;
import edu.upb.optifacotry.service.SolicitudReposicionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/solicitudes-reposicion")
public class SolicitudReposicionController extends CrudController<SolicitudReposicionRequestDto, SolicitudReposicionResponseDto> {

    public SolicitudReposicionController(SolicitudReposicionService service) {
        super(service, SolicitudReposicionResponseDto::id);
    }
}
