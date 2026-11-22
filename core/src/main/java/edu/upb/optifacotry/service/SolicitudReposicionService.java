package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.SolicitudReposicionRequestDto;
import edu.upb.optifacotry.repository.dto.response.SolicitudReposicionResponseDto;
import edu.upb.optifacotry.repository.entity.Articulo;
import edu.upb.optifacotry.repository.entity.OrdenProduccion;
import edu.upb.optifacotry.repository.entity.SolicitudReposicion;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.SolicitudReposicionRepository;
import org.springframework.stereotype.Service;

@Service
public class SolicitudReposicionService extends CrudService<SolicitudReposicion, SolicitudReposicionRequestDto, SolicitudReposicionResponseDto> {

    public SolicitudReposicionService(SolicitudReposicionRepository repository) {
        super(repository);
    }

    @Override
    protected SolicitudReposicion newEntity() {
        return new SolicitudReposicion();
    }

    @Override
    protected void applyRequest(SolicitudReposicionRequestDto request, SolicitudReposicion entity) {
        entity.setArticulo(reference(Articulo.class, request.articuloId()));
        entity.setCantidad(request.cantidad());
        entity.setMotivo(request.motivo());
        entity.setOrden(reference(OrdenProduccion.class, request.ordenId()));
        entity.setSolicitante(reference(Usuario.class, request.solicitanteId()));
        entity.setCreadaEn(request.creadaEn());
        entity.setEstado(request.estado());
        entity.setDecision(request.decision());
        entity.setJustificacion(request.justificacion());
        entity.setDecididoPor(reference(Usuario.class, request.decididoPor()));
        entity.setDecididoEn(request.decididoEn());
    }

    @Override
    protected SolicitudReposicionResponseDto toResponse(SolicitudReposicion entity) {
        return new SolicitudReposicionResponseDto(
            entity.getId(),
            referenceId(entity.getArticulo()),
            entity.getCantidad(),
            entity.getMotivo(),
            referenceId(entity.getOrden()),
            referenceId(entity.getSolicitante()),
            entity.getCreadaEn(),
            entity.getEstado(),
            entity.getDecision(),
            entity.getJustificacion(),
            referenceId(entity.getDecididoPor()),
            entity.getDecididoEn()
        );
    }
}
