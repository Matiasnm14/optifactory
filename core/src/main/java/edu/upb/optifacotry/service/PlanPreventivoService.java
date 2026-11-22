package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.PlanPreventivoRequestDto;
import edu.upb.optifacotry.repository.dto.response.PlanPreventivoResponseDto;
import edu.upb.optifacotry.repository.entity.Maquina;
import edu.upb.optifacotry.repository.entity.PlanPreventivo;
import edu.upb.optifacotry.repository.repos.PlanPreventivoRepository;
import org.springframework.stereotype.Service;

@Service
public class PlanPreventivoService extends CrudService<PlanPreventivo, PlanPreventivoRequestDto, PlanPreventivoResponseDto> {

    public PlanPreventivoService(PlanPreventivoRepository repository) {
        super(repository);
    }

    @Override
    protected PlanPreventivo newEntity() {
        return new PlanPreventivo();
    }

    @Override
    protected void applyRequest(PlanPreventivoRequestDto request, PlanPreventivo entity) {
        entity.setMaquina(reference(Maquina.class, request.maquinaId()));
        entity.setTipoVencimiento(request.tipoVencimiento());
        entity.setFrecuenciaTiempo(request.frecuenciaTiempo());
        entity.setContador(request.contador());
        entity.setFrecuenciaUso(request.frecuenciaUso());
        entity.setFechaBase(request.fechaBase());
        entity.setLecturaBase(request.lecturaBase());
        entity.setActividades(request.actividades());
        entity.setActivo(request.activo());
    }

    @Override
    protected PlanPreventivoResponseDto toResponse(PlanPreventivo entity) {
        return new PlanPreventivoResponseDto(
            entity.getId(),
            referenceId(entity.getMaquina()),
            entity.getTipoVencimiento(),
            entity.getFrecuenciaTiempo(),
            entity.getContador(),
            entity.getFrecuenciaUso(),
            entity.getFechaBase(),
            entity.getLecturaBase(),
            entity.getActividades(),
            entity.getActivo()
        );
    }
}
