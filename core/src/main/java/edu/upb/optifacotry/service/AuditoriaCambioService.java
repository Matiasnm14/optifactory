package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.AuditoriaCambioRequestDto;
import edu.upb.optifacotry.repository.dto.response.AuditoriaCambioResponseDto;
import edu.upb.optifacotry.repository.entity.AuditoriaCambio;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.AuditoriaCambioRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaCambioService extends CrudService<AuditoriaCambio, AuditoriaCambioRequestDto, AuditoriaCambioResponseDto> {

    public AuditoriaCambioService(AuditoriaCambioRepository repository) {
        super(repository);
    }

    @Override
    protected AuditoriaCambio newEntity() {
        return new AuditoriaCambio();
    }

    @Override
    protected void applyRequest(AuditoriaCambioRequestDto request, AuditoriaCambio entity) {
        entity.setEntidad(request.entidad());
        entity.setRegistroId(request.registroId());
        entity.setAccion(request.accion());
        entity.setValoresAnteriores(request.valoresAnteriores());
        entity.setValoresNuevos(request.valoresNuevos());
        entity.setMotivo(request.motivo());
        entity.setUsuario(reference(Usuario.class, request.usuarioId()));
        entity.setOcurridoEn(request.ocurridoEn());
    }

    @Override
    protected AuditoriaCambioResponseDto toResponse(AuditoriaCambio entity) {
        return new AuditoriaCambioResponseDto(
            entity.getId(),
            entity.getEntidad(),
            entity.getRegistroId(),
            entity.getAccion(),
            entity.getValoresAnteriores(),
            entity.getValoresNuevos(),
            entity.getMotivo(),
            referenceId(entity.getUsuario()),
            entity.getOcurridoEn()
        );
    }
}
