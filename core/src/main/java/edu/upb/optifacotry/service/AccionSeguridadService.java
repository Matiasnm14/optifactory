package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.AccionSeguridadRequestDto;
import edu.upb.optifacotry.repository.dto.response.AccionSeguridadResponseDto;
import edu.upb.optifacotry.repository.entity.AccionSeguridad;
import edu.upb.optifacotry.repository.entity.ReporteSeguridad;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.AccionSeguridadRepository;
import org.springframework.stereotype.Service;

@Service
public class AccionSeguridadService extends CrudService<AccionSeguridad, AccionSeguridadRequestDto, AccionSeguridadResponseDto> {

    public AccionSeguridadService(AccionSeguridadRepository repository) {
        super(repository);
    }

    @Override
    protected AccionSeguridad newEntity() {
        return new AccionSeguridad();
    }

    @Override
    protected void applyRequest(AccionSeguridadRequestDto request, AccionSeguridad entity) {
        entity.setReporte(reference(ReporteSeguridad.class, request.reporteId()));
        entity.setAccion(request.accion());
        entity.setResponsable(reference(Usuario.class, request.responsableId()));
        entity.setFechaObjetivo(request.fechaObjetivo());
        entity.setEstado(request.estado());
    }

    @Override
    protected AccionSeguridadResponseDto toResponse(AccionSeguridad entity) {
        return new AccionSeguridadResponseDto(
            entity.getId(),
            referenceId(entity.getReporte()),
            entity.getAccion(),
            referenceId(entity.getResponsable()),
            entity.getFechaObjetivo(),
            entity.getEstado()
        );
    }
}
