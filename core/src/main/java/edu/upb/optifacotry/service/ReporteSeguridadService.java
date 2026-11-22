package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.ReporteSeguridadRequestDto;
import edu.upb.optifacotry.repository.dto.response.ReporteSeguridadResponseDto;
import edu.upb.optifacotry.repository.entity.Area;
import edu.upb.optifacotry.repository.entity.ReporteSeguridad;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.ReporteSeguridadRepository;
import org.springframework.stereotype.Service;

@Service
public class ReporteSeguridadService extends CrudService<ReporteSeguridad, ReporteSeguridadRequestDto, ReporteSeguridadResponseDto> {

    public ReporteSeguridadService(ReporteSeguridadRepository repository) {
        super(repository);
    }

    @Override
    protected ReporteSeguridad newEntity() {
        return new ReporteSeguridad();
    }

    @Override
    protected void applyRequest(ReporteSeguridadRequestDto request, ReporteSeguridad entity) {
        entity.setArea(reference(Area.class, request.areaId()));
        entity.setUbicacion(request.ubicacion());
        entity.setDescripcion(request.descripcion());
        entity.setEstado(request.estado());
        entity.setAutor(reference(Usuario.class, request.autorId()));
        entity.setReportadoEn(request.reportadoEn());
    }

    @Override
    protected ReporteSeguridadResponseDto toResponse(ReporteSeguridad entity) {
        return new ReporteSeguridadResponseDto(
            entity.getId(),
            referenceId(entity.getArea()),
            entity.getUbicacion(),
            entity.getDescripcion(),
            entity.getEstado(),
            referenceId(entity.getAutor()),
            entity.getReportadoEn()
        );
    }
}
