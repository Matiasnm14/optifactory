package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.response.AdjuntoResponseDto;
import edu.upb.optifacotry.repository.entity.Adjunto;
import edu.upb.optifacotry.repository.repos.AdjuntoRepository;
import edu.upb.optifacotry.repository.repos.ReporteSeguridadRepository;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AdjuntoService extends ReadOnlyCrudService<Adjunto, AdjuntoResponseDto> {

    private final AdjuntoRepository adjuntos;
    private final ReporteSeguridadRepository reportes;

    public AdjuntoService(AdjuntoRepository adjuntos, ReporteSeguridadRepository reportes) {
        super(adjuntos);
        this.adjuntos = adjuntos;
        this.reportes = reportes;
    }

    public Page<AdjuntoResponseDto> consultarReporte(UUID reporteId, Pageable pageable) {
        if (!reportes.existsById(reporteId)) {
            throw new ResourceNotFoundException(reporteId);
        }
        return adjuntos.findByReporteSeguridadId(reporteId, pageable).map(this::toResponse);
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
