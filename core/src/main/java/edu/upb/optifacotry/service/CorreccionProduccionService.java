package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.CorreccionProduccionRequestDto;
import edu.upb.optifacotry.repository.dto.response.CorreccionProduccionResponseDto;
import edu.upb.optifacotry.repository.entity.CorreccionProduccion;
import edu.upb.optifacotry.repository.entity.RegistroProduccion;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.CorreccionProduccionRepository;
import org.springframework.stereotype.Service;

@Service
public class CorreccionProduccionService extends CrudService<CorreccionProduccion, CorreccionProduccionRequestDto, CorreccionProduccionResponseDto> {

    public CorreccionProduccionService(CorreccionProduccionRepository repository) {
        super(repository);
    }

    @Override
    protected CorreccionProduccion newEntity() {
        return new CorreccionProduccion();
    }

    @Override
    protected void applyRequest(CorreccionProduccionRequestDto request, CorreccionProduccion entity) {
        entity.setRegistro(reference(RegistroProduccion.class, request.registroId()));
        entity.setValoresAnteriores(request.valoresAnteriores());
        entity.setValoresNuevos(request.valoresNuevos());
        entity.setMotivo(request.motivo());
        entity.setResponsable(reference(Usuario.class, request.responsableId()));
        entity.setCorregidoEn(request.corregidoEn());
    }

    @Override
    protected CorreccionProduccionResponseDto toResponse(CorreccionProduccion entity) {
        return new CorreccionProduccionResponseDto(
            entity.getId(),
            referenceId(entity.getRegistro()),
            entity.getValoresAnteriores(),
            entity.getValoresNuevos(),
            entity.getMotivo(),
            referenceId(entity.getResponsable()),
            entity.getCorregidoEn()
        );
    }
}
