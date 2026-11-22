package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.DisposicionLoteRequestDto;
import edu.upb.optifacotry.repository.dto.response.DisposicionLoteResponseDto;
import edu.upb.optifacotry.repository.entity.BloqueoLote;
import edu.upb.optifacotry.repository.entity.DisposicionLote;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.DisposicionLoteRepository;
import org.springframework.stereotype.Service;

@Service
public class DisposicionLoteService extends CrudService<DisposicionLote, DisposicionLoteRequestDto, DisposicionLoteResponseDto> {

    public DisposicionLoteService(DisposicionLoteRepository repository) {
        super(repository);
    }

    @Override
    protected DisposicionLote newEntity() {
        return new DisposicionLote();
    }

    @Override
    protected void applyRequest(DisposicionLoteRequestDto request, DisposicionLote entity) {
        entity.setBloqueo(reference(BloqueoLote.class, request.bloqueoId()));
        entity.setCantidad(request.cantidad());
        entity.setDecision(request.decision());
        entity.setJustificacion(request.justificacion());
        entity.setResponsable(reference(Usuario.class, request.responsableId()));
        entity.setDecididaEn(request.decididaEn());
    }

    @Override
    protected DisposicionLoteResponseDto toResponse(DisposicionLote entity) {
        return new DisposicionLoteResponseDto(
            entity.getId(),
            referenceId(entity.getBloqueo()),
            entity.getCantidad(),
            entity.getDecision(),
            entity.getJustificacion(),
            referenceId(entity.getResponsable()),
            entity.getDecididaEn()
        );
    }
}
