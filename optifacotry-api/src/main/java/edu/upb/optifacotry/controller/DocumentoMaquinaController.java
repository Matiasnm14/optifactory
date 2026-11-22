package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.DocumentoMaquinaRequestDto;
import edu.upb.optifacotry.repository.dto.response.DocumentoMaquinaResponseDto;
import edu.upb.optifacotry.service.DocumentoMaquinaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/documentos-maquina")
public class DocumentoMaquinaController extends CrudController<DocumentoMaquinaRequestDto, DocumentoMaquinaResponseDto> {

    public DocumentoMaquinaController(DocumentoMaquinaService service) {
        super(service, DocumentoMaquinaResponseDto::id);
    }
}
