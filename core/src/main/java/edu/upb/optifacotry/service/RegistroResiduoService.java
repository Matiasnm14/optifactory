package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.RegistroResiduoRequestDto;
import edu.upb.optifacotry.repository.dto.response.RegistroResiduoResponseDto;
import edu.upb.optifacotry.repository.entity.Area;
import edu.upb.optifacotry.repository.entity.RegistroResiduo;
import edu.upb.optifacotry.repository.entity.TipoResiduo;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.RegistroResiduoRepository;
import org.springframework.stereotype.Service;

@Service
public class RegistroResiduoService extends CrudService<RegistroResiduo, RegistroResiduoRequestDto, RegistroResiduoResponseDto> {

    public RegistroResiduoService(RegistroResiduoRepository repository) {
        super(repository);
    }

    @Override
    protected RegistroResiduo newEntity() {
        return new RegistroResiduo();
    }

    @Override
    protected void applyRequest(RegistroResiduoRequestDto request, RegistroResiduo entity) {
        entity.setTipo(reference(TipoResiduo.class, request.tipoId()));
        entity.setArea(reference(Area.class, request.areaId()));
        entity.setCantidad(request.cantidad());
        entity.setUnidad(request.unidad());
        entity.setGeneradoEn(request.generadoEn());
        entity.setTipoDato(request.tipoDato());
        entity.setDestinoFinal(request.destinoFinal());
        entity.setFechaDestino(request.fechaDestino());
        entity.setResponsable(reference(Usuario.class, request.responsableId()));
    }

    @Override
    protected RegistroResiduoResponseDto toResponse(RegistroResiduo entity) {
        return new RegistroResiduoResponseDto(
            entity.getId(),
            referenceId(entity.getTipo()),
            referenceId(entity.getArea()),
            entity.getCantidad(),
            entity.getUnidad(),
            entity.getGeneradoEn(),
            entity.getTipoDato(),
            entity.getDestinoFinal(),
            entity.getFechaDestino(),
            referenceId(entity.getResponsable())
        );
    }
}
