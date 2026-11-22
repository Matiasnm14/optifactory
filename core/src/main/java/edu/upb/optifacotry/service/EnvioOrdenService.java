package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.EnvioOrdenRequestDto;
import edu.upb.optifacotry.repository.dto.response.EnvioOrdenResponseDto;
import edu.upb.optifacotry.repository.entity.EnvioOrden;
import edu.upb.optifacotry.repository.entity.FuenteIntegracion;
import edu.upb.optifacotry.repository.entity.OrdenProduccion;
import edu.upb.optifacotry.repository.repos.EnvioOrdenRepository;
import org.springframework.stereotype.Service;

@Service
public class EnvioOrdenService extends CrudService<EnvioOrden, EnvioOrdenRequestDto, EnvioOrdenResponseDto> {

    public EnvioOrdenService(EnvioOrdenRepository repository) {
        super(repository);
    }

    @Override
    protected EnvioOrden newEntity() {
        return new EnvioOrden();
    }

    @Override
    protected void applyRequest(EnvioOrdenRequestDto request, EnvioOrden entity) {
        entity.setOrden(reference(OrdenProduccion.class, request.ordenId()));
        entity.setFuente(reference(FuenteIntegracion.class, request.fuenteId()));
        entity.setCorrelacion(request.correlacion());
        entity.setContenido(request.contenido());
        entity.setEstado(request.estado());
        entity.setCreadoEn(request.creadoEn());
        entity.setEnviadoEn(request.enviadoEn());
        entity.setVenceEn(request.venceEn());
        entity.setRespuesta(request.respuesta());
        entity.setMotivo(request.motivo());
        entity.setRespondidoEn(request.respondidoEn());
    }

    @Override
    protected EnvioOrdenResponseDto toResponse(EnvioOrden entity) {
        return new EnvioOrdenResponseDto(
            entity.getId(),
            referenceId(entity.getOrden()),
            referenceId(entity.getFuente()),
            entity.getCorrelacion(),
            entity.getContenido(),
            entity.getEstado(),
            entity.getCreadoEn(),
            entity.getEnviadoEn(),
            entity.getVenceEn(),
            entity.getRespuesta(),
            entity.getMotivo(),
            entity.getRespondidoEn()
        );
    }
}
