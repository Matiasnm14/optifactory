package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.EstimacionReparacionRequestDto;
import edu.upb.optifacotry.repository.dto.response.EstimacionReparacionResponseDto;
import edu.upb.optifacotry.repository.entity.EstimacionReparacion;
import edu.upb.optifacotry.repository.entity.Intervencion;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.EstimacionReparacionRepository;
import org.springframework.stereotype.Service;

@Service
public class EstimacionReparacionService extends CrudService<EstimacionReparacion, EstimacionReparacionRequestDto, EstimacionReparacionResponseDto> {

    public EstimacionReparacionService(EstimacionReparacionRepository repository) {
        super(repository);
    }

    @Override
    protected EstimacionReparacion newEntity() {
        return new EstimacionReparacion();
    }

    @Override
    protected void applyRequest(EstimacionReparacionRequestDto request, EstimacionReparacion entity) {
        entity.setIntervencion(reference(Intervencion.class, request.intervencionId()));
        entity.setFinalizacionEstimada(request.finalizacionEstimada());
        entity.setMotivo(request.motivo());
        entity.setAutor(reference(Usuario.class, request.autorId()));
        entity.setRegistradaEn(request.registradaEn());
    }

    @Override
    protected EstimacionReparacionResponseDto toResponse(EstimacionReparacion entity) {
        return new EstimacionReparacionResponseDto(
            entity.getId(),
            referenceId(entity.getIntervencion()),
            entity.getFinalizacionEstimada(),
            entity.getMotivo(),
            referenceId(entity.getAutor()),
            entity.getRegistradaEn()
        );
    }
}
