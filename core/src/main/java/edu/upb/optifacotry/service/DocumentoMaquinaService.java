package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.DocumentoMaquinaRequestDto;
import edu.upb.optifacotry.repository.dto.response.DocumentoMaquinaResponseDto;
import edu.upb.optifacotry.repository.entity.DocumentoMaquina;
import edu.upb.optifacotry.repository.entity.Maquina;
import edu.upb.optifacotry.repository.repos.DocumentoMaquinaRepository;
import org.springframework.stereotype.Service;

@Service
public class DocumentoMaquinaService extends CrudService<DocumentoMaquina, DocumentoMaquinaRequestDto, DocumentoMaquinaResponseDto> {

    public DocumentoMaquinaService(DocumentoMaquinaRepository repository) {
        super(repository);
    }

    @Override
    protected DocumentoMaquina newEntity() {
        return new DocumentoMaquina();
    }

    @Override
    protected void applyRequest(DocumentoMaquinaRequestDto request, DocumentoMaquina entity) {
        entity.setMaquina(reference(Maquina.class, request.maquinaId()));
        entity.setNombre(request.nombre());
        entity.setArchivo(request.archivo());
    }

    @Override
    protected DocumentoMaquinaResponseDto toResponse(DocumentoMaquina entity) {
        return new DocumentoMaquinaResponseDto(
            entity.getId(),
            referenceId(entity.getMaquina()),
            entity.getNombre(),
            entity.getArchivo()
        );
    }
}
