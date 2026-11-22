package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.OperacionProductoRequestDto;
import edu.upb.optifacotry.repository.dto.response.OperacionProductoResponseDto;
import edu.upb.optifacotry.repository.entity.OperacionProducto;
import edu.upb.optifacotry.repository.entity.VersionProducto;
import edu.upb.optifacotry.repository.repos.OperacionProductoRepository;
import org.springframework.stereotype.Service;

@Service
public class OperacionProductoService extends CrudService<OperacionProducto, OperacionProductoRequestDto, OperacionProductoResponseDto> {

    public OperacionProductoService(OperacionProductoRepository repository) {
        super(repository);
    }

    @Override
    protected OperacionProducto newEntity() {
        return new OperacionProducto();
    }

    @Override
    protected void applyRequest(OperacionProductoRequestDto request, OperacionProducto entity) {
        entity.setVersion(reference(VersionProducto.class, request.versionId()));
        entity.setNombre(request.nombre());
        entity.setSecuencia(request.secuencia());
    }

    @Override
    protected OperacionProductoResponseDto toResponse(OperacionProducto entity) {
        return new OperacionProductoResponseDto(
            entity.getId(),
            referenceId(entity.getVersion()),
            entity.getNombre(),
            entity.getSecuencia()
        );
    }
}
