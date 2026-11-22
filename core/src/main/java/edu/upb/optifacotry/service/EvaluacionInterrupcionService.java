package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.EvaluacionInterrupcionRequestDto;
import edu.upb.optifacotry.repository.dto.response.EvaluacionInterrupcionResponseDto;
import edu.upb.optifacotry.repository.entity.Alternativa;
import edu.upb.optifacotry.repository.entity.EvaluacionInterrupcion;
import edu.upb.optifacotry.repository.entity.OrdenProduccion;
import edu.upb.optifacotry.repository.entity.Parada;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.EvaluacionInterrupcionRepository;
import org.springframework.stereotype.Service;

@Service
public class EvaluacionInterrupcionService extends CrudService<EvaluacionInterrupcion, EvaluacionInterrupcionRequestDto, EvaluacionInterrupcionResponseDto> {

    public EvaluacionInterrupcionService(EvaluacionInterrupcionRepository repository) {
        super(repository);
    }

    @Override
    protected EvaluacionInterrupcion newEntity() {
        return new EvaluacionInterrupcion();
    }

    @Override
    protected void applyRequest(EvaluacionInterrupcionRequestDto request, EvaluacionInterrupcion entity) {
        entity.setOrden(reference(OrdenProduccion.class, request.ordenId()));
        entity.setParada(reference(Parada.class, request.paradaId()));
        entity.setOrigen(request.origen());
        entity.setCantidadPendiente(request.cantidadPendiente());
        entity.setEvaluadaEn(request.evaluadaEn());
        entity.setAutor(reference(Usuario.class, request.autorId()));
        entity.setAlternativaElegida(reference(Alternativa.class, request.alternativaElegidaId()));
        entity.setJustificacion(request.justificacion());
        entity.setAprobadoPor(reference(Usuario.class, request.aprobadoPor()));
        entity.setAprobadoEn(request.aprobadoEn());
    }

    @Override
    protected EvaluacionInterrupcionResponseDto toResponse(EvaluacionInterrupcion entity) {
        return new EvaluacionInterrupcionResponseDto(
            entity.getId(),
            referenceId(entity.getOrden()),
            referenceId(entity.getParada()),
            entity.getOrigen(),
            entity.getCantidadPendiente(),
            entity.getEvaluadaEn(),
            referenceId(entity.getAutor()),
            referenceId(entity.getAlternativaElegida()),
            entity.getJustificacion(),
            referenceId(entity.getAprobadoPor()),
            entity.getAprobadoEn()
        );
    }
}
