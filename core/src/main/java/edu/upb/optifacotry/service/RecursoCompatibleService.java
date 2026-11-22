package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.RecursoCompatibleRequestDto;
import edu.upb.optifacotry.repository.dto.response.RecursoCompatibleResponseDto;
import edu.upb.optifacotry.repository.entity.Linea;
import edu.upb.optifacotry.repository.entity.Maquina;
import edu.upb.optifacotry.repository.entity.OperacionProducto;
import edu.upb.optifacotry.repository.entity.RecursoCompatible;
import edu.upb.optifacotry.repository.repos.RecursoCompatibleRepository;
import org.springframework.stereotype.Service;

@Service
public class RecursoCompatibleService extends CrudService<RecursoCompatible, RecursoCompatibleRequestDto, RecursoCompatibleResponseDto> {

    public RecursoCompatibleService(RecursoCompatibleRepository repository) {
        super(repository);
    }

    @Override
    protected RecursoCompatible newEntity() {
        return new RecursoCompatible();
    }

    @Override
    protected void applyRequest(RecursoCompatibleRequestDto request, RecursoCompatible entity) {
        entity.setOperacion(reference(OperacionProducto.class, request.operacionId()));
        entity.setLinea(reference(Linea.class, request.lineaId()));
        entity.setMaquina(reference(Maquina.class, request.maquinaId()));
    }

    @Override
    protected RecursoCompatibleResponseDto toResponse(RecursoCompatible entity) {
        return new RecursoCompatibleResponseDto(
            entity.getId(),
            referenceId(entity.getOperacion()),
            referenceId(entity.getLinea()),
            referenceId(entity.getMaquina())
        );
    }
}
