package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.InspeccionRequestDto;
import edu.upb.optifacotry.repository.dto.response.InspeccionResponseDto;
import edu.upb.optifacotry.repository.entity.Inspeccion;
import edu.upb.optifacotry.repository.entity.Lote;
import edu.upb.optifacotry.repository.entity.OrdenProduccion;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.InspeccionRepository;
import org.springframework.stereotype.Service;

@Service
public class InspeccionService extends CrudService<Inspeccion, InspeccionRequestDto, InspeccionResponseDto> {

    public InspeccionService(InspeccionRepository repository) {
        super(repository);
    }

    @Override
    protected Inspeccion newEntity() {
        return new Inspeccion();
    }

    @Override
    protected void applyRequest(InspeccionRequestDto request, Inspeccion entity) {
        entity.setOrden(reference(OrdenProduccion.class, request.ordenId()));
        entity.setLote(reference(Lote.class, request.loteId()));
        entity.setInspector(reference(Usuario.class, request.inspectorId()));
        entity.setRealizadaEn(request.realizadaEn());
        entity.setCantidadMuestras(request.cantidadMuestras());
        entity.setEstado(request.estado());
    }

    @Override
    protected InspeccionResponseDto toResponse(Inspeccion entity) {
        return new InspeccionResponseDto(
            entity.getId(),
            referenceId(entity.getOrden()),
            referenceId(entity.getLote()),
            referenceId(entity.getInspector()),
            entity.getRealizadaEn(),
            entity.getCantidadMuestras(),
            entity.getEstado()
        );
    }
}
