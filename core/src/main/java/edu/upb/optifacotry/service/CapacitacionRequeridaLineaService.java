package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.CapacitacionRequeridaLineaRequestDto;
import edu.upb.optifacotry.repository.dto.response.CapacitacionRequeridaLineaResponseDto;
import edu.upb.optifacotry.repository.entity.Capacitacion;
import edu.upb.optifacotry.repository.entity.CapacitacionRequeridaLinea;
import edu.upb.optifacotry.repository.entity.Linea;
import edu.upb.optifacotry.repository.repos.CapacitacionRequeridaLineaRepository;
import org.springframework.stereotype.Service;

@Service
public class CapacitacionRequeridaLineaService extends CrudService<CapacitacionRequeridaLinea, CapacitacionRequeridaLineaRequestDto, CapacitacionRequeridaLineaResponseDto> {

    public CapacitacionRequeridaLineaService(CapacitacionRequeridaLineaRepository repository) {
        super(repository);
    }

    @Override
    protected CapacitacionRequeridaLinea newEntity() {
        return new CapacitacionRequeridaLinea();
    }

    @Override
    protected void applyRequest(CapacitacionRequeridaLineaRequestDto request, CapacitacionRequeridaLinea entity) {
        entity.setLinea(reference(Linea.class, request.lineaId()));
        entity.setCapacitacion(reference(Capacitacion.class, request.capacitacionId()));
        entity.setCantidadPersonas(request.cantidadPersonas());
    }

    @Override
    protected CapacitacionRequeridaLineaResponseDto toResponse(CapacitacionRequeridaLinea entity) {
        return new CapacitacionRequeridaLineaResponseDto(
            entity.getId(),
            referenceId(entity.getLinea()),
            referenceId(entity.getCapacitacion()),
            entity.getCantidadPersonas()
        );
    }
}
