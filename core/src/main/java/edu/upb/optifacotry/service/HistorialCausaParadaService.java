package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.HistorialCausaParadaRequestDto;
import edu.upb.optifacotry.repository.dto.response.HistorialCausaParadaResponseDto;
import edu.upb.optifacotry.repository.entity.CausaParada;
import edu.upb.optifacotry.repository.entity.HistorialCausaParada;
import edu.upb.optifacotry.repository.entity.Parada;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.HistorialCausaParadaRepository;
import org.springframework.stereotype.Service;

@Service
public class HistorialCausaParadaService extends CrudService<HistorialCausaParada, HistorialCausaParadaRequestDto, HistorialCausaParadaResponseDto> {

    public HistorialCausaParadaService(HistorialCausaParadaRepository repository) {
        super(repository);
    }

    @Override
    protected HistorialCausaParada newEntity() {
        return new HistorialCausaParada();
    }

    @Override
    protected void applyRequest(HistorialCausaParadaRequestDto request, HistorialCausaParada entity) {
        entity.setParada(reference(Parada.class, request.paradaId()));
        entity.setCausaAnterior(reference(CausaParada.class, request.causaAnteriorId()));
        entity.setCausaNueva(reference(CausaParada.class, request.causaNuevaId()));
        entity.setMotivo(request.motivo());
        entity.setResponsable(reference(Usuario.class, request.responsableId()));
        entity.setCambiadoEn(request.cambiadoEn());
    }

    @Override
    protected HistorialCausaParadaResponseDto toResponse(HistorialCausaParada entity) {
        return new HistorialCausaParadaResponseDto(
            entity.getId(),
            referenceId(entity.getParada()),
            referenceId(entity.getCausaAnterior()),
            referenceId(entity.getCausaNueva()),
            entity.getMotivo(),
            referenceId(entity.getResponsable()),
            entity.getCambiadoEn()
        );
    }
}
