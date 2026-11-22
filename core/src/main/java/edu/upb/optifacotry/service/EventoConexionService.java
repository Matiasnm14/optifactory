package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.EventoConexionRequestDto;
import edu.upb.optifacotry.repository.dto.response.EventoConexionResponseDto;
import edu.upb.optifacotry.repository.entity.EventoConexion;
import edu.upb.optifacotry.repository.entity.FuenteIntegracion;
import edu.upb.optifacotry.repository.repos.EventoConexionRepository;
import org.springframework.stereotype.Service;

@Service
public class EventoConexionService extends CrudService<EventoConexion, EventoConexionRequestDto, EventoConexionResponseDto> {

    public EventoConexionService(EventoConexionRepository repository) {
        super(repository);
    }

    @Override
    protected EventoConexion newEntity() {
        return new EventoConexion();
    }

    @Override
    protected void applyRequest(EventoConexionRequestDto request, EventoConexion entity) {
        entity.setFuente(reference(FuenteIntegracion.class, request.fuenteId()));
        entity.setTipo(request.tipo());
        entity.setOcurridoEn(request.ocurridoEn());
    }

    @Override
    protected EventoConexionResponseDto toResponse(EventoConexion entity) {
        return new EventoConexionResponseDto(
            entity.getId(),
            referenceId(entity.getFuente()),
            entity.getTipo(),
            entity.getOcurridoEn()
        );
    }
}
