package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.EventoOrdenRequestDto;
import edu.upb.optifacotry.repository.dto.response.EventoOrdenResponseDto;
import edu.upb.optifacotry.repository.entity.EventoOrden;
import edu.upb.optifacotry.repository.entity.OrdenProduccion;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.EventoOrdenRepository;
import org.springframework.stereotype.Service;

@Service
public class EventoOrdenService extends CrudService<EventoOrden, EventoOrdenRequestDto, EventoOrdenResponseDto> {

    public EventoOrdenService(EventoOrdenRepository repository) {
        super(repository);
    }

    @Override
    protected EventoOrden newEntity() {
        return new EventoOrden();
    }

    @Override
    protected void applyRequest(EventoOrdenRequestDto request, EventoOrden entity) {
        entity.setOrden(reference(OrdenProduccion.class, request.ordenId()));
        entity.setTipo(request.tipo());
        entity.setEstadoAnterior(request.estadoAnterior());
        entity.setEstadoNuevo(request.estadoNuevo());
        entity.setMotivo(request.motivo());
        entity.setDetalle(request.detalle());
        entity.setResponsable(reference(Usuario.class, request.responsableId()));
        entity.setOcurridoEn(request.ocurridoEn());
    }

    @Override
    protected EventoOrdenResponseDto toResponse(EventoOrden entity) {
        return new EventoOrdenResponseDto(
            entity.getId(),
            referenceId(entity.getOrden()),
            entity.getTipo(),
            entity.getEstadoAnterior(),
            entity.getEstadoNuevo(),
            entity.getMotivo(),
            entity.getDetalle(),
            referenceId(entity.getResponsable()),
            entity.getOcurridoEn()
        );
    }
}
