package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.SuspensionActividadRequestDto;
import edu.upb.optifacotry.repository.dto.response.SuspensionActividadResponseDto;
import edu.upb.optifacotry.repository.entity.Area;
import edu.upb.optifacotry.repository.entity.Linea;
import edu.upb.optifacotry.repository.entity.OperacionOrden;
import edu.upb.optifacotry.repository.entity.ReporteSeguridad;
import edu.upb.optifacotry.repository.entity.SuspensionActividad;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.SuspensionActividadRepository;
import org.springframework.stereotype.Service;

@Service
public class SuspensionActividadService extends CrudService<SuspensionActividad, SuspensionActividadRequestDto, SuspensionActividadResponseDto> {

    public SuspensionActividadService(SuspensionActividadRepository repository) {
        super(repository);
    }

    @Override
    protected SuspensionActividad newEntity() {
        return new SuspensionActividad();
    }

    @Override
    protected void applyRequest(SuspensionActividadRequestDto request, SuspensionActividad entity) {
        entity.setReporte(reference(ReporteSeguridad.class, request.reporteId()));
        entity.setArea(reference(Area.class, request.areaId()));
        entity.setLinea(reference(Linea.class, request.lineaId()));
        entity.setOperacionOrden(reference(OperacionOrden.class, request.operacionOrdenId()));
        entity.setMotivo(request.motivo());
        entity.setInicio(request.inicio());
        entity.setSuspendidoPor(reference(Usuario.class, request.suspendidoPor()));
        entity.setReanudadoEn(request.reanudadoEn());
        entity.setReanudadoPor(reference(Usuario.class, request.reanudadoPor()));
    }

    @Override
    protected SuspensionActividadResponseDto toResponse(SuspensionActividad entity) {
        return new SuspensionActividadResponseDto(
            entity.getId(),
            referenceId(entity.getReporte()),
            referenceId(entity.getArea()),
            referenceId(entity.getLinea()),
            referenceId(entity.getOperacionOrden()),
            entity.getMotivo(),
            entity.getInicio(),
            referenceId(entity.getSuspendidoPor()),
            entity.getReanudadoEn(),
            referenceId(entity.getReanudadoPor())
        );
    }
}
