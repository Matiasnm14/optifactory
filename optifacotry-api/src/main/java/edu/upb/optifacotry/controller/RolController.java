package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.response.RolResponseDto;
import edu.upb.optifacotry.service.RolService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/roles")
public class RolController extends ReadOnlyCrudController<RolResponseDto> {

    public RolController(RolService service) {
        super(service);
    }
}
