package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.PeriodoPersonalRequestDto;
import edu.upb.optifacotry.repository.dto.response.PeriodoPersonalResponseDto;
import edu.upb.optifacotry.repository.entity.PeriodoPersonal;
import edu.upb.optifacotry.repository.entity.Persona;
import edu.upb.optifacotry.repository.entity.Turno;
import edu.upb.optifacotry.repository.repos.PeriodoPersonalRepository;
import edu.upb.optifacotry.service.exception.InvalidRequestException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class PeriodoPersonalService {

    private static final Set<String> TIPOS = Set.of("TURNO", "DISPONIBILIDAD", "AUSENCIA");

    private final PersonaService personas;
    private final PeriodoPersonalRepository periodos;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public PeriodoPersonalService(PeriodoPersonalRepository periodos,
                                  PersonaService personas,
                                  PermisosService permisos,
                                  EntidadesJpa entidades) {
        this.periodos = periodos;
        this.personas = personas;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL', 'SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public Page<PeriodoPersonalResponseDto> findAll(Pageable pageable) {
        return periodos.findAll(
                PeriodoPersonalRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL', 'SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public PeriodoPersonalResponseDto findById(UUID id) {
        var filtro = PeriodoPersonalRepository.visibles(permisos.actorActual().areas());
        return periodos.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL')")
    @Transactional
    public PeriodoPersonalResponseDto create(PeriodoPersonalRequestDto request) {
        var actor = permisos.actorActual();
        PeriodoPersonal entidad = new PeriodoPersonal();
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        var guardada = periodos.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL')")
    @Transactional
    public PeriodoPersonalResponseDto update(UUID id, PeriodoPersonalRequestDto request) {
        var actor = permisos.actorActual();
        PeriodoPersonal entidad = entidades.bloquear(PeriodoPersonal.class, id);
        verificarAreas(entidad, actor.areas());
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        periodos.flush();
        entidades.refrescar(entidad);
        return toResponse(entidad);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL')")
    @Transactional
    public void delete(UUID id) {
        var actor = permisos.actorActual();
        PeriodoPersonal entidad = entidades.bloquear(PeriodoPersonal.class, id);
        verificarAreas(entidad, actor.areas());
        periodos.delete(entidad);
        periodos.flush();
    }

    private void verificarAreas(PeriodoPersonal entidad, Set<UUID> areas) {
        AmbitoAreas.verificar(areas, () -> AmbitoAreas.dePersona(entidad.getPersona()));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL', 'SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public Page<PeriodoPersonalResponseDto> consultar(UUID personaId, Instant desde, Instant hasta, Pageable pageable) {
        requireIntervalo(desde, hasta);
        personas.findById(personaId);
        // Conserva las comparaciones estrictas con instantes almacenados en microsegundos.
        Instant limiteInferior = desde.truncatedTo(ChronoUnit.MICROS);
        Instant truncado = hasta.truncatedTo(ChronoUnit.MICROS);
        Instant limiteSuperior = truncado.isBefore(hasta) ? truncado.plus(1, ChronoUnit.MICROS) : truncado;
        var filtro = PeriodoPersonalRepository.visibles(permisos.actorActual().areas()).and((raiz, consulta, criterios) -> criterios.and(
                criterios.equal(raiz.get("persona").get("id"), personaId),
                criterios.lessThan(raiz.get("inicio"), limiteSuperior),
                criterios.greaterThan(raiz.get("fin"), limiteInferior)));
        return periodos.findAll(filtro, pageable).map(this::toResponse);
    }


    private void applyRequest(PeriodoPersonalRequestDto request, PeriodoPersonal entity) {
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

        entity.setPersona(entidades.referencia(Persona.class, request.personaId()));
        entity.setTipo(tipo);
        entity.setTurno(entidades.referencia(Turno.class, request.turnoId()));
        entity.setInicio(inicio);
        entity.setFin(fin);
        entity.setMotivo(request.motivo());
    }

    private static void requireIntervalo(Instant inicio, Instant fin) {
        if (!fin.isAfter(inicio)) {
            throw new InvalidRequestException("El fin del intervalo debe ser posterior al inicio.");
        }
    }

    private PeriodoPersonalResponseDto toResponse(PeriodoPersonal entity) {
        return new PeriodoPersonalResponseDto(
            entity.getId(),
            entidades.id(entity.getPersona()),
            entity.getTipo(),
            entidades.id(entity.getTurno()),
            entity.getInicio(),
            entity.getFin(),
            entity.getMotivo()
        );
    }
}
