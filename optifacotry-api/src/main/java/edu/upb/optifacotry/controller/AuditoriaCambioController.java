package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.response.AuditoriaCambioResponseDto;
import edu.upb.optifacotry.service.AuditoriaCambioService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auditoria-cambios")
public class AuditoriaCambioController extends ReadOnlyCrudController<AuditoriaCambioResponseDto> {

    public AuditoriaCambioController(AuditoriaCambioService service) {
        super(service);
    }
}
