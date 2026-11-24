package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.PeriodoPersonalRequestDto;
import edu.upb.optifacotry.repository.dto.response.PeriodoPersonalResponseDto;
import edu.upb.optifacotry.repository.entity.PeriodoPersonal;
import edu.upb.optifacotry.repository.entity.Persona;
import edu.upb.optifacotry.repository.entity.Turno;
import edu.upb.optifacotry.repository.repos.PeriodoPersonalRepository;
import edu.upb.optifacotry.repository.repos.PersonaRepository;
import edu.upb.optifacotry.service.exception.InvalidRequestException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class PeriodoPersonalService extends CrudService<PeriodoPersonal, PeriodoPersonalRequestDto, PeriodoPersonalResponseDto> {

    private static final Set<String> TIPOS = Set.of("TURNO", "DISPONIBILIDAD", "AUSENCIA");

    private final PeriodoPersonalRepository periodos;
    private final PersonaRepository personas;

    public PeriodoPersonalService(PeriodoPersonalRepository periodos, PersonaRepository personas) {
        super(periodos);
        this.periodos = periodos;
        this.personas = personas;
    }

    public Page<PeriodoPersonalResponseDto> consultar(UUID personaId, Instant desde, Instant hasta, Pageable pageable) {
        requireIntervalo(desde, hasta);
        if (!personas.existsById(personaId)) {
            throw new ResourceNotFoundException(personaId);
        }
        // Conserva las comparaciones estrictas con instantes almacenados en microsegundos.
        Instant limiteInferior = desde.truncatedTo(ChronoUnit.MICROS);
        Instant limiteSuperior = hasta.truncatedTo(ChronoUnit.MICROS);
        if (limiteSuperior.isBefore(hasta)) {
            limiteSuperior = limiteSuperior.plus(1, ChronoUnit.MICROS);
        }
        return periodos.findByPersonaIdAndInicioLessThanAndFinGreaterThan(personaId, limiteSuperior, limiteInferior, pageable)
                .map(this::toResponse);
    }

    @Override
    protected PeriodoPersonal newEntity() {
        return new PeriodoPersonal();
    }

    @Override
    protected void applyRequest(PeriodoPersonalRequestDto request, PeriodoPersonal entity) {
        Instant inicio = request.inicio().truncatedTo(ChronoUnit.MICROS);
        Instant fin = request.fin().truncatedTo(ChronoUnit.MICROS);
        requireIntervalo(inicio, fin);
        String tipo = request.tipo().strip().toUpperCase(Locale.ROOT);
        if (!TIPOS.contains(tipo)) {
            throw new InvalidRequestException("El tipo de período debe ser TURNO, DISPONIBILIDAD o AUSENCIA.");
        }
        if ("TURNO".equals(tipo) && request.turnoId() == null) {
            throw new InvalidRequestException("Un período de tipo TURNO requiere turnoId.");
        }

        entity.setPersona(reference(Persona.class, request.personaId()));
        entity.setTipo(tipo);
        entity.setTurno(reference(Turno.class, request.turnoId()));
        entity.setInicio(inicio);
        entity.setFin(fin);
        entity.setMotivo(request.motivo());
    }

    private static void requireIntervalo(Instant inicio, Instant fin) {
        if (!fin.isAfter(inicio)) {
            throw new InvalidRequestException("El fin del intervalo debe ser posterior al inicio.");
        }
    }

    @Override
    protected PeriodoPersonalResponseDto toResponse(PeriodoPersonal entity) {
        return new PeriodoPersonalResponseDto(
            entity.getId(),
            referenceId(entity.getPersona()),
            entity.getTipo(),
            referenceId(entity.getTurno()),
            entity.getInicio(),
            entity.getFin(),
            entity.getMotivo()
        );
    }
}
