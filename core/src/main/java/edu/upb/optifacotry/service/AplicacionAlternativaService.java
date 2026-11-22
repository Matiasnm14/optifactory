package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.AplicacionAlternativaRequestDto;
import edu.upb.optifacotry.repository.dto.response.AplicacionAlternativaResponseDto;
import edu.upb.optifacotry.repository.entity.AplicacionAlternativa;
import edu.upb.optifacotry.repository.entity.EvaluacionInterrupcion;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.AplicacionAlternativaRepository;
import org.springframework.stereotype.Service;

@Service
public class AplicacionAlternativaService extends CrudService<AplicacionAlternativa, AplicacionAlternativaRequestDto, AplicacionAlternativaResponseDto> {

    public AplicacionAlternativaService(AplicacionAlternativaRepository repository) {
        super(repository);
    }

    @Override
    protected AplicacionAlternativa newEntity() {
        return new AplicacionAlternativa();
    }

    @Override
    protected void applyRequest(AplicacionAlternativaRequestDto request, AplicacionAlternativa entity) {
        entity.setEvaluacion(reference(EvaluacionInterrupcion.class, request.evaluacionId()));
        entity.setResponsable(reference(Usuario.class, request.responsableId()));
        entity.setAplicadaEn(request.aplicadaEn());
        entity.setNuevaVerificacion(request.nuevaVerificacion());
        entity.setEstado(request.estado());
    }

    @Override
    protected AplicacionAlternativaResponseDto toResponse(AplicacionAlternativa entity) {
        return new AplicacionAlternativaResponseDto(
            entity.getId(),
            referenceId(entity.getEvaluacion()),
            referenceId(entity.getResponsable()),
            entity.getAplicadaEn(),
            entity.getNuevaVerificacion(),
            entity.getEstado()
        );
    }
}
