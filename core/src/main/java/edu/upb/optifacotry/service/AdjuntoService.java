package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.response.AdjuntoResponseDto;
import edu.upb.optifacotry.repository.entity.Adjunto;
import edu.upb.optifacotry.repository.repos.AdjuntoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AdjuntoService extends ReadOnlyCrudService<Adjunto, AdjuntoResponseDto> {

    public AdjuntoService(AdjuntoRepository adjuntos) {
        super(adjuntos);
    }

    public Page<AdjuntoResponseDto> consultarReporte(UUID reporteId, Pageable pageable) {
        return listar((raiz, consulta, criterios) -> criterios.equal(raiz.get("reporteSeguridad").get("id"), reporteId),
                pageable);
    }

    @Override
    protected AdjuntoResponseDto toResponse(Adjunto entity) {
        return new AdjuntoResponseDto(
            entity.getId(),
            referenceId(entity.getReporteSeguridad()),
            entity.getNombre(),
            entity.getArchivo(),
            referenceId(entity.getAutor()),
            entity.getCreadoEn()
        );
    }
}
