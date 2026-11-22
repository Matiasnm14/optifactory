package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.AccionAlertaRequestDto;
import edu.upb.optifacotry.repository.dto.response.AccionAlertaResponseDto;
import edu.upb.optifacotry.repository.entity.AccionAlerta;
import edu.upb.optifacotry.repository.entity.Alerta;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.AccionAlertaRepository;
import org.springframework.stereotype.Service;

@Service
public class AccionAlertaService extends CrudService<AccionAlerta, AccionAlertaRequestDto, AccionAlertaResponseDto> {

    public AccionAlertaService(AccionAlertaRepository repository) {
        super(repository);
    }

    @Override
    protected AccionAlerta newEntity() {
        return new AccionAlerta();
    }

    @Override
    protected void applyRequest(AccionAlertaRequestDto request, AccionAlerta entity) {
        entity.setAlerta(reference(Alerta.class, request.alertaId()));
        entity.setDescripcion(request.descripcion());
        entity.setResponsable(reference(Usuario.class, request.responsableId()));
        entity.setRealizadaEn(request.realizadaEn());
    }

    @Override
    protected AccionAlertaResponseDto toResponse(AccionAlerta entity) {
        return new AccionAlertaResponseDto(
            entity.getId(),
            referenceId(entity.getAlerta()),
            entity.getDescripcion(),
            referenceId(entity.getResponsable()),
            entity.getRealizadaEn()
        );
    }
}
