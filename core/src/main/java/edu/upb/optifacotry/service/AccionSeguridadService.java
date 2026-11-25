package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.AccionSeguridadRequestDto;
import edu.upb.optifacotry.repository.dto.response.AccionSeguridadResponseDto;
import edu.upb.optifacotry.repository.entity.AccionSeguridad;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.AccionSeguridadRepository;
import edu.upb.optifacotry.repository.repos.ReporteSeguridadRepository;
import edu.upb.optifacotry.repository.repos.UsuarioRepository;
import edu.upb.optifacotry.service.exception.InvalidRequestException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Service
public class AccionSeguridadService extends CrudService<AccionSeguridad, AccionSeguridadRequestDto, AccionSeguridadResponseDto> {

    private final ReporteSeguridadRepository reportes;
    private final UsuarioRepository usuarios;

    public AccionSeguridadService(AccionSeguridadRepository repository, ReporteSeguridadRepository reportes,
                                  UsuarioRepository usuarios) {
        super(repository);
        this.reportes = reportes;
        this.usuarios = usuarios;
    }

    @Override
    protected AccionSeguridad newEntity() {
        return new AccionSeguridad();
    }

    @Override
    protected void applyRequest(AccionSeguridadRequestDto request, AccionSeguridad entity) {
        entity.setReporte(reportes.findById(request.reporteId())
                .orElseThrow(() -> new ResourceNotFoundException(request.reporteId())));
        entity.setAccion(request.accion().strip());
        entity.setFechaObjetivo(request.fechaObjetivo());
        entity.setEstado("PENDIENTE");
    }

    @Override
    protected void completarEscritura(AccionSeguridadRequestDto request, AccionSeguridad entity,
                                      PermisosService.Actor actor) {
        Usuario responsable = usuarios.findLockedById(request.responsableId())
                .orElseThrow(() -> new ResourceNotFoundException(request.responsableId()));
        UUID areaId = referenceId(entity.getReporte().getArea());
        UUID[] areas = responsable.getAreasIds();
        if (!Boolean.TRUE.equals(responsable.getActivo()) || areas == null || !Arrays.asList(areas).contains(areaId)) {
            throw new InvalidRequestException("El responsable debe estar activo y tener asignada el área del reporte.");
        }
        entity.setResponsable(responsable);
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
