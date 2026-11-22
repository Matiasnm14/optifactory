package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.FuenteIntegracionRequestDto;
import edu.upb.optifacotry.repository.dto.response.FuenteIntegracionResponseDto;
import edu.upb.optifacotry.repository.entity.FuenteIntegracion;
import edu.upb.optifacotry.repository.repos.FuenteIntegracionRepository;
import org.springframework.stereotype.Service;

@Service
public class FuenteIntegracionService extends CrudService<FuenteIntegracion, FuenteIntegracionRequestDto, FuenteIntegracionResponseDto> {

    public FuenteIntegracionService(FuenteIntegracionRepository repository) {
        super(repository);
    }

    @Override
    protected FuenteIntegracion newEntity() {
        return new FuenteIntegracion();
    }

    @Override
    protected void applyRequest(FuenteIntegracionRequestDto request, FuenteIntegracion entity) {
        entity.setNombre(request.nombre());
        entity.setTipo(request.tipo());
        entity.setTiempoMaximoSilencio(request.tiempoMaximoSilencio());
        entity.setUltimaComunicacionValida(request.ultimaComunicacionValida());
        entity.setEstado(request.estado());
    }

    @Override
    protected FuenteIntegracionResponseDto toResponse(FuenteIntegracion entity) {
        return new FuenteIntegracionResponseDto(
            entity.getId(),
            entity.getNombre(),
            entity.getTipo(),
            entity.getTiempoMaximoSilencio(),
            entity.getUltimaComunicacionValida(),
            entity.getEstado()
        );
    }
}
