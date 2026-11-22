package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.OrdenProduccionRequestDto;
import edu.upb.optifacotry.repository.dto.response.OrdenProduccionResponseDto;
import edu.upb.optifacotry.repository.entity.Linea;
import edu.upb.optifacotry.repository.entity.OrdenProduccion;
import edu.upb.optifacotry.repository.entity.Turno;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.entity.VersionProducto;
import edu.upb.optifacotry.repository.repos.OrdenProduccionRepository;
import org.springframework.stereotype.Service;

@Service
public class OrdenProduccionService extends CrudService<OrdenProduccion, OrdenProduccionRequestDto, OrdenProduccionResponseDto> {

    public OrdenProduccionService(OrdenProduccionRepository repository) {
        super(repository);
    }

    @Override
    protected OrdenProduccion newEntity() {
        return new OrdenProduccion();
    }

    @Override
    protected void applyRequest(OrdenProduccionRequestDto request, OrdenProduccion entity) {
        entity.setCodigo(request.codigo());
        entity.setVersion(reference(VersionProducto.class, request.versionId()));
        entity.setCantidadObjetivo(request.cantidadObjetivo());
        entity.setPrioridad(request.prioridad());
        entity.setFechaObjetivo(request.fechaObjetivo());
        entity.setEstado(request.estado());
        entity.setLinea(reference(Linea.class, request.lineaId()));
        entity.setTurno(reference(Turno.class, request.turnoId()));
        entity.setInicioProgramado(request.inicioProgramado());
        entity.setFinProgramado(request.finProgramado());
        entity.setProgramadoPor(reference(Usuario.class, request.programadoPor()));
        entity.setProgramadoEn(request.programadoEn());
        entity.setCreadoPor(reference(Usuario.class, request.creadoPor()));
        entity.setCreadoEn(request.creadoEn());
    }

    @Override
    protected OrdenProduccionResponseDto toResponse(OrdenProduccion entity) {
        return new OrdenProduccionResponseDto(
            entity.getId(),
            entity.getCodigo(),
            referenceId(entity.getVersion()),
            entity.getCantidadObjetivo(),
            entity.getPrioridad(),
            entity.getFechaObjetivo(),
            entity.getEstado(),
            referenceId(entity.getLinea()),
            referenceId(entity.getTurno()),
            entity.getInicioProgramado(),
            entity.getFinProgramado(),
            referenceId(entity.getProgramadoPor()),
            entity.getProgramadoEn(),
            referenceId(entity.getCreadoPor()),
            entity.getCreadoEn()
        );
    }
}
