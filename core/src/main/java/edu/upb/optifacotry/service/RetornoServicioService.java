package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.RetornoServicioRequestDto;
import edu.upb.optifacotry.repository.dto.response.RetornoServicioResponseDto;
import edu.upb.optifacotry.repository.entity.Intervencion;
import edu.upb.optifacotry.repository.entity.RetornoServicio;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.RetornoServicioRepository;
import org.springframework.stereotype.Service;

@Service
public class RetornoServicioService extends CrudService<RetornoServicio, RetornoServicioRequestDto, RetornoServicioResponseDto> {

    public RetornoServicioService(RetornoServicioRepository repository) {
        super(repository);
    }

    @Override
    protected RetornoServicio newEntity() {
        return new RetornoServicio();
    }

    @Override
    protected void applyRequest(RetornoServicioRequestDto request, RetornoServicio entity) {
        entity.setIntervencion(reference(Intervencion.class, request.intervencionId()));
        entity.setVerificaciones(request.verificaciones());
        entity.setResultados(request.resultados());
        entity.setCondicionesPendientes(request.condicionesPendientes());
        entity.setDecision(request.decision());
        entity.setMotivo(request.motivo());
        entity.setResponsable(reference(Usuario.class, request.responsableId()));
        entity.setDecididoEn(request.decididoEn());
    }

    @Override
    protected RetornoServicioResponseDto toResponse(RetornoServicio entity) {
        return new RetornoServicioResponseDto(
            entity.getId(),
            referenceId(entity.getIntervencion()),
            entity.getVerificaciones(),
            entity.getResultados(),
            entity.getCondicionesPendientes(),
            entity.getDecision(),
            entity.getMotivo(),
            referenceId(entity.getResponsable()),
            entity.getDecididoEn()
        );
    }
}
