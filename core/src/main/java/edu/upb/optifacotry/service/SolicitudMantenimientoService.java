package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.SolicitudMantenimientoRequestDto;
import edu.upb.optifacotry.repository.dto.response.SolicitudMantenimientoResponseDto;
import edu.upb.optifacotry.repository.entity.Maquina;
import edu.upb.optifacotry.repository.entity.Parada;
import edu.upb.optifacotry.repository.entity.SolicitudMantenimiento;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.SolicitudMantenimientoRepository;
import org.springframework.stereotype.Service;

@Service
public class SolicitudMantenimientoService extends CrudService<SolicitudMantenimiento, SolicitudMantenimientoRequestDto, SolicitudMantenimientoResponseDto> {

    public SolicitudMantenimientoService(SolicitudMantenimientoRepository repository) {
        super(repository);
    }

    @Override
    protected SolicitudMantenimiento newEntity() {
        return new SolicitudMantenimiento();
    }

    @Override
    protected void applyRequest(SolicitudMantenimientoRequestDto request, SolicitudMantenimiento entity) {
        entity.setMaquina(reference(Maquina.class, request.maquinaId()));
        entity.setParada(reference(Parada.class, request.paradaId()));
        entity.setDescripcion(request.descripcion());
        entity.setEstado(request.estado());
        entity.setSolicitante(reference(Usuario.class, request.solicitanteId()));
        entity.setCreadaEn(request.creadaEn());
    }

    @Override
    protected SolicitudMantenimientoResponseDto toResponse(SolicitudMantenimiento entity) {
        return new SolicitudMantenimientoResponseDto(
            entity.getId(),
            referenceId(entity.getMaquina()),
            referenceId(entity.getParada()),
            entity.getDescripcion(),
            entity.getEstado(),
            referenceId(entity.getSolicitante()),
            entity.getCreadaEn()
        );
    }
}
