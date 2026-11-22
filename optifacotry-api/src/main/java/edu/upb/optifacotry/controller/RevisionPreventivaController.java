package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.RevisionPreventivaRequestDto;
import edu.upb.optifacotry.repository.dto.response.RevisionPreventivaResponseDto;
import edu.upb.optifacotry.service.RevisionPreventivaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/revisiones-preventivas")
public class RevisionPreventivaController extends CrudController<RevisionPreventivaRequestDto, RevisionPreventivaResponseDto> {

    public RevisionPreventivaController(RevisionPreventivaService service) {
        super(service, RevisionPreventivaResponseDto::id);
    }
}
