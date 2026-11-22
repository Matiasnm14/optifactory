package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.CausaParadaRequestDto;
import edu.upb.optifacotry.repository.dto.response.CausaParadaResponseDto;
import edu.upb.optifacotry.repository.entity.CausaParada;
import edu.upb.optifacotry.repository.repos.CausaParadaRepository;
import org.springframework.stereotype.Service;

@Service
public class CausaParadaService extends CrudService<CausaParada, CausaParadaRequestDto, CausaParadaResponseDto> {

    public CausaParadaService(CausaParadaRepository repository) {
        super(repository);
    }

    @Override
    protected CausaParada newEntity() {
        return new CausaParada();
    }

    @Override
    protected void applyRequest(CausaParadaRequestDto request, CausaParada entity) {
        entity.setCodigo(request.codigo());
        entity.setNombre(request.nombre());
        entity.setCategoria(request.categoria());
        entity.setActivo(request.activo());
    }

    @Override
    protected CausaParadaResponseDto toResponse(CausaParada entity) {
        return new CausaParadaResponseDto(
            entity.getId(),
            entity.getCodigo(),
            entity.getNombre(),
            entity.getCategoria(),
            entity.getActivo()
        );
    }
}
