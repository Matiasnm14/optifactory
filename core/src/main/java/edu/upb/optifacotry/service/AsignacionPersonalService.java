package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.AsignacionPersonalRequestDto;
import edu.upb.optifacotry.repository.dto.response.AsignacionPersonalResponseDto;
import edu.upb.optifacotry.repository.entity.AsignacionPersonal;
import edu.upb.optifacotry.repository.entity.Linea;
import edu.upb.optifacotry.repository.entity.OperacionOrden;
import edu.upb.optifacotry.repository.entity.Persona;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.AsignacionPersonalRepository;
import org.springframework.stereotype.Service;

@Service
public class AsignacionPersonalService extends CrudService<AsignacionPersonal, AsignacionPersonalRequestDto, AsignacionPersonalResponseDto> {

    public AsignacionPersonalService(AsignacionPersonalRepository repository) {
        super(repository);
    }

    @Override
    protected AsignacionPersonal newEntity() {
        return new AsignacionPersonal();
    }

    @Override
    protected void applyRequest(AsignacionPersonalRequestDto request, AsignacionPersonal entity) {
        entity.setPersona(reference(Persona.class, request.personaId()));
        entity.setLinea(reference(Linea.class, request.lineaId()));
        entity.setOperacionOrden(reference(OperacionOrden.class, request.operacionOrdenId()));
        entity.setInicio(request.inicio());
        entity.setFin(request.fin());
        entity.setResponsable(reference(Usuario.class, request.responsableId()));
    }

    @Override
    protected AsignacionPersonalResponseDto toResponse(AsignacionPersonal entity) {
        return new AsignacionPersonalResponseDto(
            entity.getId(),
            referenceId(entity.getPersona()),
            referenceId(entity.getLinea()),
            referenceId(entity.getOperacionOrden()),
            entity.getInicio(),
            entity.getFin(),
            referenceId(entity.getResponsable())
        );
    }
}
