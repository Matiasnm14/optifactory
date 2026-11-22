package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.RegistroProduccionRequestDto;
import edu.upb.optifacotry.repository.dto.response.RegistroProduccionResponseDto;
import edu.upb.optifacotry.repository.entity.Linea;
import edu.upb.optifacotry.repository.entity.OperacionOrden;
import edu.upb.optifacotry.repository.entity.RegistroProduccion;
import edu.upb.optifacotry.repository.entity.Turno;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.RegistroProduccionRepository;
import org.springframework.stereotype.Service;

@Service
public class RegistroProduccionService extends CrudService<RegistroProduccion, RegistroProduccionRequestDto, RegistroProduccionResponseDto> {

    public RegistroProduccionService(RegistroProduccionRepository repository) {
        super(repository);
    }

    @Override
    protected RegistroProduccion newEntity() {
        return new RegistroProduccion();
    }

    @Override
    protected void applyRequest(RegistroProduccionRequestDto request, RegistroProduccion entity) {
        entity.setOperacionOrden(reference(OperacionOrden.class, request.operacionOrdenId()));
        entity.setLinea(reference(Linea.class, request.lineaId()));
        entity.setTurno(reference(Turno.class, request.turnoId()));
        entity.setTotal(request.total());
        entity.setAceptadas(request.aceptadas());
        entity.setRechazadas(request.rechazadas());
        entity.setPendientes(request.pendientes());
        entity.setResponsable(reference(Usuario.class, request.responsableId()));
        entity.setRegistradoEn(request.registradoEn());
        entity.setClaveOperacion(request.claveOperacion());
    }

    @Override
    protected RegistroProduccionResponseDto toResponse(RegistroProduccion entity) {
        return new RegistroProduccionResponseDto(
            entity.getId(),
            referenceId(entity.getOperacionOrden()),
            referenceId(entity.getLinea()),
            referenceId(entity.getTurno()),
            entity.getTotal(),
            entity.getAceptadas(),
            entity.getRechazadas(),
            entity.getPendientes(),
            referenceId(entity.getResponsable()),
            entity.getRegistradoEn(),
            entity.getClaveOperacion()
        );
    }
}
