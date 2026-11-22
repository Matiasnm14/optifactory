package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.AdjuntoRequestDto;
import edu.upb.optifacotry.repository.dto.response.AdjuntoResponseDto;
import edu.upb.optifacotry.repository.entity.Adjunto;
import edu.upb.optifacotry.repository.entity.ReporteSeguridad;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.AdjuntoRepository;
import org.springframework.stereotype.Service;

@Service
public class AdjuntoService extends CrudService<Adjunto, AdjuntoRequestDto, AdjuntoResponseDto> {

    public AdjuntoService(AdjuntoRepository repository) {
        super(repository);
    }

    @Override
    protected Adjunto newEntity() {
        return new Adjunto();
    }

    @Override
    protected void applyRequest(AdjuntoRequestDto request, Adjunto entity) {
        entity.setReporteSeguridad(reference(ReporteSeguridad.class, request.reporteSeguridadId()));
        entity.setNombre(request.nombre());
        entity.setArchivo(request.archivo());
        entity.setAutor(reference(Usuario.class, request.autorId()));
        entity.setCreadoEn(request.creadoEn());
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
