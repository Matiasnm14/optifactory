package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.EstadoOperativoRequestDto;
import edu.upb.optifacotry.repository.dto.response.EstadoOperativoResponseDto;
import edu.upb.optifacotry.repository.entity.EstadoOperativo;
import edu.upb.optifacotry.repository.entity.Linea;
import edu.upb.optifacotry.repository.entity.Maquina;
import edu.upb.optifacotry.repository.entity.MensajeRecibido;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.EstadoOperativoRepository;
import org.springframework.stereotype.Service;

@Service
public class EstadoOperativoService extends CrudService<EstadoOperativo, EstadoOperativoRequestDto, EstadoOperativoResponseDto> {

    public EstadoOperativoService(EstadoOperativoRepository repository) {
        super(repository);
    }

    @Override
    protected EstadoOperativo newEntity() {
        return new EstadoOperativo();
    }

    @Override
    protected void applyRequest(EstadoOperativoRequestDto request, EstadoOperativo entity) {
        entity.setLinea(reference(Linea.class, request.lineaId()));
        entity.setMaquina(reference(Maquina.class, request.maquinaId()));
        entity.setEstado(request.estado());
        entity.setTipoFuente(request.tipoFuente());
        entity.setMensaje(reference(MensajeRecibido.class, request.mensajeId()));
        entity.setResponsable(reference(Usuario.class, request.responsableId()));
        entity.setOrigenEn(request.origenEn());
        entity.setRecibidoEn(request.recibidoEn());
    }

    @Override
    protected EstadoOperativoResponseDto toResponse(EstadoOperativo entity) {
        return new EstadoOperativoResponseDto(
            entity.getId(),
            referenceId(entity.getLinea()),
            referenceId(entity.getMaquina()),
            entity.getEstado(),
            entity.getTipoFuente(),
            referenceId(entity.getMensaje()),
            referenceId(entity.getResponsable()),
            entity.getOrigenEn(),
            entity.getRecibidoEn()
        );
    }
}
