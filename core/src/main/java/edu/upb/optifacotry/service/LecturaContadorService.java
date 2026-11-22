package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.LecturaContadorRequestDto;
import edu.upb.optifacotry.repository.dto.response.LecturaContadorResponseDto;
import edu.upb.optifacotry.repository.entity.LecturaContador;
import edu.upb.optifacotry.repository.entity.Maquina;
import edu.upb.optifacotry.repository.repos.LecturaContadorRepository;
import org.springframework.stereotype.Service;

@Service
public class LecturaContadorService extends CrudService<LecturaContador, LecturaContadorRequestDto, LecturaContadorResponseDto> {

    public LecturaContadorService(LecturaContadorRepository repository) {
        super(repository);
    }

    @Override
    protected LecturaContador newEntity() {
        return new LecturaContador();
    }

    @Override
    protected void applyRequest(LecturaContadorRequestDto request, LecturaContador entity) {
        entity.setMaquina(reference(Maquina.class, request.maquinaId()));
        entity.setContador(request.contador());
        entity.setValor(request.valor());
        entity.setUnidad(request.unidad());
        entity.setFuente(request.fuente());
        entity.setLeidoEn(request.leidoEn());
    }

    @Override
    protected LecturaContadorResponseDto toResponse(LecturaContador entity) {
        return new LecturaContadorResponseDto(
            entity.getId(),
            referenceId(entity.getMaquina()),
            entity.getContador(),
            entity.getValor(),
            entity.getUnidad(),
            entity.getFuente(),
            entity.getLeidoEn()
        );
    }
}
