package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.HistorialEstadoIntervencionRequestDto;
import edu.upb.optifacotry.repository.dto.response.HistorialEstadoIntervencionResponseDto;
import edu.upb.optifacotry.repository.entity.HistorialEstadoIntervencion;
import edu.upb.optifacotry.repository.entity.Intervencion;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.HistorialEstadoIntervencionRepository;
import org.springframework.stereotype.Service;

@Service
public class HistorialEstadoIntervencionService extends CrudService<HistorialEstadoIntervencion, HistorialEstadoIntervencionRequestDto, HistorialEstadoIntervencionResponseDto> {

    public HistorialEstadoIntervencionService(HistorialEstadoIntervencionRepository repository) {
        super(repository);
    }

    @Override
    protected HistorialEstadoIntervencion newEntity() {
        return new HistorialEstadoIntervencion();
    }

    @Override
    protected void applyRequest(HistorialEstadoIntervencionRequestDto request, HistorialEstadoIntervencion entity) {
        entity.setIntervencion(reference(Intervencion.class, request.intervencionId()));
        entity.setEstadoAnterior(request.estadoAnterior());
        entity.setEstadoNuevo(request.estadoNuevo());
        entity.setResponsable(reference(Usuario.class, request.responsableId()));
        entity.setCambiadoEn(request.cambiadoEn());
    }

    @Override
    protected HistorialEstadoIntervencionResponseDto toResponse(HistorialEstadoIntervencion entity) {
        return new HistorialEstadoIntervencionResponseDto(
            entity.getId(),
            referenceId(entity.getIntervencion()),
            entity.getEstadoAnterior(),
            entity.getEstadoNuevo(),
            referenceId(entity.getResponsable()),
            entity.getCambiadoEn()
        );
    }
}
