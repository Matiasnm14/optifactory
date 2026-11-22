package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.CambioAplicacionRequestDto;
import edu.upb.optifacotry.repository.dto.response.CambioAplicacionResponseDto;
import edu.upb.optifacotry.repository.entity.AplicacionAlternativa;
import edu.upb.optifacotry.repository.entity.CambioAplicacion;
import edu.upb.optifacotry.repository.repos.CambioAplicacionRepository;
import org.springframework.stereotype.Service;

@Service
public class CambioAplicacionService extends CrudService<CambioAplicacion, CambioAplicacionRequestDto, CambioAplicacionResponseDto> {

    public CambioAplicacionService(CambioAplicacionRepository repository) {
        super(repository);
    }

    @Override
    protected CambioAplicacion newEntity() {
        return new CambioAplicacion();
    }

    @Override
    protected void applyRequest(CambioAplicacionRequestDto request, CambioAplicacion entity) {
        entity.setAplicacion(reference(AplicacionAlternativa.class, request.aplicacionId()));
        entity.setEntidad(request.entidad());
        entity.setRegistroId(request.registroId());
        entity.setCambioPropuesto(request.cambioPropuesto());
        entity.setCambioRealizado(request.cambioRealizado());
        entity.setEstado(request.estado());
        entity.setError(request.error());
    }

    @Override
    protected CambioAplicacionResponseDto toResponse(CambioAplicacion entity) {
        return new CambioAplicacionResponseDto(
            entity.getId(),
            referenceId(entity.getAplicacion()),
            entity.getEntidad(),
            entity.getRegistroId(),
            entity.getCambioPropuesto(),
            entity.getCambioRealizado(),
            entity.getEstado(),
            entity.getError()
        );
    }
}
