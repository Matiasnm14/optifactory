package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.LoteRequestDto;
import edu.upb.optifacotry.repository.dto.response.LoteResponseDto;
import edu.upb.optifacotry.repository.entity.Articulo;
import edu.upb.optifacotry.repository.entity.Lote;
import edu.upb.optifacotry.repository.entity.OperacionOrden;
import edu.upb.optifacotry.repository.repos.LoteRepository;
import org.springframework.stereotype.Service;

@Service
public class LoteService extends CrudService<Lote, LoteRequestDto, LoteResponseDto> {

    public LoteService(LoteRepository repository) {
        super(repository);
    }

    @Override
    protected Lote newEntity() {
        return new Lote();
    }

    @Override
    protected void applyRequest(LoteRequestDto request, Lote entity) {
        entity.setArticulo(reference(Articulo.class, request.articuloId()));
        entity.setCodigo(request.codigo());
        entity.setFecha(request.fecha());
        entity.setVencimiento(request.vencimiento());
        entity.setOperacionOrigen(reference(OperacionOrden.class, request.operacionOrigenId()));
    }

    @Override
    protected LoteResponseDto toResponse(Lote entity) {
        return new LoteResponseDto(
            entity.getId(),
            referenceId(entity.getArticulo()),
            entity.getCodigo(),
            entity.getFecha(),
            entity.getVencimiento(),
            referenceId(entity.getOperacionOrigen())
        );
    }
}
