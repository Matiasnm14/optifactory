package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.ReservaMaterialRequestDto;
import edu.upb.optifacotry.repository.dto.response.ReservaMaterialResponseDto;
import edu.upb.optifacotry.service.ReservaMaterialService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reservas-materiales")
public class ReservaMaterialController extends CrudController<ReservaMaterialRequestDto, ReservaMaterialResponseDto> {

    public ReservaMaterialController(ReservaMaterialService service) {
        super(service, ReservaMaterialResponseDto::id);
    }
}
