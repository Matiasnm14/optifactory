package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.ParadaRequestDto;
import edu.upb.optifacotry.repository.dto.response.ParadaResponseDto;
import edu.upb.optifacotry.repository.entity.CausaParada;
import edu.upb.optifacotry.repository.entity.Linea;
import edu.upb.optifacotry.repository.entity.Maquina;
import edu.upb.optifacotry.repository.entity.OrdenProduccion;
import edu.upb.optifacotry.repository.entity.Parada;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.ParadaRepository;
import org.springframework.stereotype.Service;

@Service
public class ParadaService extends CrudService<Parada, ParadaRequestDto, ParadaResponseDto> {

    public ParadaService(ParadaRepository repository) {
        super(repository);
    }

    @Override
    protected Parada newEntity() {
        return new Parada();
    }

    @Override
    protected void applyRequest(ParadaRequestDto request, Parada entity) {
        entity.setMaquina(reference(Maquina.class, request.maquinaId()));
        entity.setLinea(reference(Linea.class, request.lineaId()));
        entity.setOrden(reference(OrdenProduccion.class, request.ordenId()));
        entity.setCausa(reference(CausaParada.class, request.causaId()));
        entity.setInicio(request.inicio());
        entity.setFin(request.fin());
        entity.setDescripcion(request.descripcion());
        entity.setCreadoPor(reference(Usuario.class, request.creadoPor()));
    }

    @Override
    protected ParadaResponseDto toResponse(Parada entity) {
        return new ParadaResponseDto(
            entity.getId(),
            referenceId(entity.getMaquina()),
            referenceId(entity.getLinea()),
            referenceId(entity.getOrden()),
            referenceId(entity.getCausa()),
            entity.getInicio(),
            entity.getFin(),
            entity.getDescripcion(),
            referenceId(entity.getCreadoPor())
        );
    }
}
