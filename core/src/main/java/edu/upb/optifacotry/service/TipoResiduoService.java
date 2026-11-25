package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.TipoResiduoRequestDto;
import edu.upb.optifacotry.repository.dto.response.TipoResiduoResponseDto;
import edu.upb.optifacotry.repository.entity.TipoResiduo;
import edu.upb.optifacotry.repository.repos.TipoResiduoRepository;
import org.springframework.stereotype.Service;

@Service
public class TipoResiduoService extends CrudService<TipoResiduo, TipoResiduoRequestDto, TipoResiduoResponseDto> {

    public TipoResiduoService(TipoResiduoRepository repository) {
        super(repository);
    }

    @Override
    protected TipoResiduo newEntity() {
        return new TipoResiduo();
    }

    @Override
    protected void applyRequest(TipoResiduoRequestDto request, TipoResiduo entity) {
        entity.setCodigo(request.codigo().strip());
        entity.setNombre(request.nombre().strip());
        entity.setUnidad(request.unidad().strip());
        entity.setActivo(request.activo());
    }

    @Override
    protected TipoResiduoResponseDto toResponse(TipoResiduo entity) {
        return new TipoResiduoResponseDto(
            entity.getId(),
            entity.getCodigo(),
            entity.getNombre(),
            entity.getUnidad(),
            entity.getActivo()
        );
    }
}
