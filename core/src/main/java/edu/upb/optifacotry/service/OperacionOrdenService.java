package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.OperacionOrdenRequestDto;
import edu.upb.optifacotry.repository.dto.response.OperacionOrdenResponseDto;
import edu.upb.optifacotry.repository.entity.OperacionOrden;
import edu.upb.optifacotry.repository.entity.OperacionProducto;
import edu.upb.optifacotry.repository.entity.OrdenProduccion;
import edu.upb.optifacotry.repository.repos.OperacionOrdenRepository;
import org.springframework.stereotype.Service;

@Service
public class OperacionOrdenService extends CrudService<OperacionOrden, OperacionOrdenRequestDto, OperacionOrdenResponseDto> {

    public OperacionOrdenService(OperacionOrdenRepository repository) {
        super(repository);
    }

    @Override
    protected OperacionOrden newEntity() {
        return new OperacionOrden();
    }

    @Override
    protected void applyRequest(OperacionOrdenRequestDto request, OperacionOrden entity) {
        entity.setOrden(reference(OrdenProduccion.class, request.ordenId()));
        entity.setOperacionProducto(reference(OperacionProducto.class, request.operacionProductoId()));
        entity.setSecuencia(request.secuencia());
        entity.setEstado(request.estado());
    }

    @Override
    protected OperacionOrdenResponseDto toResponse(OperacionOrden entity) {
        return new OperacionOrdenResponseDto(
            entity.getId(),
            referenceId(entity.getOrden()),
            referenceId(entity.getOperacionProducto()),
            entity.getSecuencia(),
            entity.getEstado()
        );
    }
}
