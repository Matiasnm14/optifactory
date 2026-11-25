package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.response.AuditoriaCambioResponseDto;
import edu.upb.optifacotry.repository.entity.AuditoriaCambio;
import edu.upb.optifacotry.repository.repos.AuditoriaCambioRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaCambioService extends ReadOnlyCrudService<AuditoriaCambio, AuditoriaCambioResponseDto> {

    public AuditoriaCambioService(AuditoriaCambioRepository repository) {
        super(repository);
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
