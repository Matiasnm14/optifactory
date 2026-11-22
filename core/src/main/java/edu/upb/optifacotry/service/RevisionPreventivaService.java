package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.RevisionPreventivaRequestDto;
import edu.upb.optifacotry.repository.dto.response.RevisionPreventivaResponseDto;
import edu.upb.optifacotry.repository.entity.Intervencion;
import edu.upb.optifacotry.repository.entity.PlanPreventivo;
import edu.upb.optifacotry.repository.entity.RevisionPreventiva;
import edu.upb.optifacotry.repository.repos.RevisionPreventivaRepository;
import org.springframework.stereotype.Service;

@Service
public class RevisionPreventivaService extends CrudService<RevisionPreventiva, RevisionPreventivaRequestDto, RevisionPreventivaResponseDto> {

    public RevisionPreventivaService(RevisionPreventivaRepository repository) {
        super(repository);
    }

    @Override
    protected RevisionPreventiva newEntity() {
        return new RevisionPreventiva();
    }

    @Override
    protected void applyRequest(RevisionPreventivaRequestDto request, RevisionPreventiva entity) {
        entity.setPlan(reference(PlanPreventivo.class, request.planId()));
        entity.setClaveVencimiento(request.claveVencimiento());
        entity.setEstado(request.estado());
        entity.setIntervencion(reference(Intervencion.class, request.intervencionId()));
        entity.setInicioIndisponibilidad(request.inicioIndisponibilidad());
        entity.setFinIndisponibilidad(request.finIndisponibilidad());
        entity.setVentanaConfirmada(request.ventanaConfirmada());
    }

    @Override
    protected RevisionPreventivaResponseDto toResponse(RevisionPreventiva entity) {
        return new RevisionPreventivaResponseDto(
            entity.getId(),
            referenceId(entity.getPlan()),
            entity.getClaveVencimiento(),
            entity.getEstado(),
            referenceId(entity.getIntervencion()),
            entity.getInicioIndisponibilidad(),
            entity.getFinIndisponibilidad(),
            entity.getVentanaConfirmada()
        );
    }
}
