package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.IntervencionRequestDto;
import edu.upb.optifacotry.repository.dto.response.IntervencionResponseDto;
import edu.upb.optifacotry.repository.entity.Intervencion;
import edu.upb.optifacotry.repository.entity.Maquina;
import edu.upb.optifacotry.repository.entity.Persona;
import edu.upb.optifacotry.repository.entity.SolicitudMantenimiento;
import edu.upb.optifacotry.repository.repos.IntervencionRepository;
import org.springframework.stereotype.Service;

@Service
public class IntervencionService extends CrudService<Intervencion, IntervencionRequestDto, IntervencionResponseDto> {

    public IntervencionService(IntervencionRepository repository) {
        super(repository);
    }

    @Override
    protected Intervencion newEntity() {
        return new Intervencion();
    }

    @Override
    protected void applyRequest(IntervencionRequestDto request, Intervencion entity) {
        entity.setMaquina(reference(Maquina.class, request.maquinaId()));
        entity.setSolicitud(reference(SolicitudMantenimiento.class, request.solicitudId()));
        entity.setTecnico(reference(Persona.class, request.tecnicoId()));
        entity.setPrioridad(request.prioridad());
        entity.setFechaObjetivo(request.fechaObjetivo());
        entity.setDiagnostico(request.diagnostico());
        entity.setActividades(request.actividades());
        entity.setResultado(request.resultado());
        entity.setVerificacion(request.verificacion());
        entity.setEstado(request.estado());
        entity.setInicioIndisponibilidad(request.inicioIndisponibilidad());
        entity.setFinIndisponibilidad(request.finIndisponibilidad());
        entity.setVentanaConfirmada(request.ventanaConfirmada());
    }

    @Override
    protected IntervencionResponseDto toResponse(Intervencion entity) {
        return new IntervencionResponseDto(
            entity.getId(),
            referenceId(entity.getMaquina()),
            referenceId(entity.getSolicitud()),
            referenceId(entity.getTecnico()),
            entity.getPrioridad(),
            entity.getFechaObjetivo(),
            entity.getDiagnostico(),
            entity.getActividades(),
            entity.getResultado(),
            entity.getVerificacion(),
            entity.getEstado(),
            entity.getInicioIndisponibilidad(),
            entity.getFinIndisponibilidad(),
            entity.getVentanaConfirmada()
        );
    }
}
