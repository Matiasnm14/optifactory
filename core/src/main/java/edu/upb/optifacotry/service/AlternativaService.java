package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.AlternativaRequestDto;
import edu.upb.optifacotry.repository.dto.response.AlternativaResponseDto;
import edu.upb.optifacotry.repository.entity.Alternativa;
import edu.upb.optifacotry.repository.entity.EvaluacionInterrupcion;
import edu.upb.optifacotry.repository.repos.AlternativaRepository;
import org.springframework.stereotype.Service;

@Service
public class AlternativaService extends CrudService<Alternativa, AlternativaRequestDto, AlternativaResponseDto> {

    public AlternativaService(AlternativaRepository repository) {
        super(repository);
    }

    @Override
    protected Alternativa newEntity() {
        return new Alternativa();
    }

    @Override
    protected void applyRequest(AlternativaRequestDto request, Alternativa entity) {
        entity.setEvaluacion(reference(EvaluacionInterrupcion.class, request.evaluacionId()));
        entity.setNombre(request.nombre());
        entity.setDescripcion(request.descripcion());
        entity.setSupuestos(request.supuestos());
        entity.setFinalizacionEstimada(request.finalizacionEstimada());
        entity.setRetraso(request.retraso());
        entity.setRecursosRequeridos(request.recursosRequeridos());
        entity.setRestricciones(request.restricciones());
        entity.setDatosNoVerificables(request.datosNoVerificables());
    }

    @Override
    protected AlternativaResponseDto toResponse(Alternativa entity) {
        return new AlternativaResponseDto(
            entity.getId(),
            referenceId(entity.getEvaluacion()),
            entity.getNombre(),
            entity.getDescripcion(),
            entity.getSupuestos(),
            entity.getFinalizacionEstimada(),
            entity.getRetraso(),
            entity.getRecursosRequeridos(),
            entity.getRestricciones(),
            entity.getDatosNoVerificables()
        );
    }
}
