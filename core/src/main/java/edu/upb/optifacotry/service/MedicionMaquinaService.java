package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.MedicionMaquinaRequestDto;
import edu.upb.optifacotry.repository.dto.response.MedicionMaquinaResponseDto;
import edu.upb.optifacotry.repository.entity.MedicionMaquina;
import edu.upb.optifacotry.repository.entity.MensajeRecibido;
import edu.upb.optifacotry.repository.repos.MedicionMaquinaRepository;
import org.springframework.stereotype.Service;

@Service
public class MedicionMaquinaService extends CrudService<MedicionMaquina, MedicionMaquinaRequestDto, MedicionMaquinaResponseDto> {

    public MedicionMaquinaService(MedicionMaquinaRepository repository) {
        super(repository);
    }

    @Override
    protected MedicionMaquina newEntity() {
        return new MedicionMaquina();
    }

    @Override
    protected void applyRequest(MedicionMaquinaRequestDto request, MedicionMaquina entity) {
        entity.setMensaje(reference(MensajeRecibido.class, request.mensajeId()));
        entity.setVariable(request.variable());
        entity.setValor(request.valor());
        entity.setUnidad(request.unidad());
        entity.setOrigenEn(request.origenEn());
    }

    @Override
    protected MedicionMaquinaResponseDto toResponse(MedicionMaquina entity) {
        return new MedicionMaquinaResponseDto(
            entity.getId(),
            referenceId(entity.getMensaje()),
            entity.getVariable(),
            entity.getValor(),
            entity.getUnidad(),
            entity.getOrigenEn()
        );
    }
}
