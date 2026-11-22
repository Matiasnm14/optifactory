package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.BloqueoLoteRequestDto;
import edu.upb.optifacotry.repository.dto.response.BloqueoLoteResponseDto;
import edu.upb.optifacotry.repository.entity.Almacen;
import edu.upb.optifacotry.repository.entity.BloqueoLote;
import edu.upb.optifacotry.repository.entity.Lote;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.BloqueoLoteRepository;
import org.springframework.stereotype.Service;

@Service
public class BloqueoLoteService extends CrudService<BloqueoLote, BloqueoLoteRequestDto, BloqueoLoteResponseDto> {

    public BloqueoLoteService(BloqueoLoteRepository repository) {
        super(repository);
    }

    @Override
    protected BloqueoLote newEntity() {
        return new BloqueoLote();
    }

    @Override
    protected void applyRequest(BloqueoLoteRequestDto request, BloqueoLote entity) {
        entity.setLote(reference(Lote.class, request.loteId()));
        entity.setAlmacen(reference(Almacen.class, request.almacenId()));
        entity.setCantidad(request.cantidad());
        entity.setUsosRestringidos(request.usosRestringidos());
        entity.setMotivo(request.motivo());
        entity.setResponsable(reference(Usuario.class, request.responsableId()));
        entity.setBloqueadoEn(request.bloqueadoEn());
    }

    @Override
    protected BloqueoLoteResponseDto toResponse(BloqueoLote entity) {
        return new BloqueoLoteResponseDto(
            entity.getId(),
            referenceId(entity.getLote()),
            referenceId(entity.getAlmacen()),
            entity.getCantidad(),
            entity.getUsosRestringidos(),
            entity.getMotivo(),
            referenceId(entity.getResponsable()),
            entity.getBloqueadoEn()
        );
    }
}
