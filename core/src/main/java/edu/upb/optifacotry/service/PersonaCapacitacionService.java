package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.PersonaCapacitacionRequestDto;
import edu.upb.optifacotry.repository.dto.response.PersonaCapacitacionResponseDto;
import edu.upb.optifacotry.repository.entity.Capacitacion;
import edu.upb.optifacotry.repository.entity.Persona;
import edu.upb.optifacotry.repository.entity.PersonaCapacitacion;
import edu.upb.optifacotry.repository.repos.PersonaCapacitacionRepository;
import edu.upb.optifacotry.service.exception.InvalidRequestException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class PersonaCapacitacionService {

    private final PersonaCapacitacionRepository repository;
    private final PermisosService permisos;
    private final EntidadesJpa entidades;

    public PersonaCapacitacionService(PersonaCapacitacionRepository repository,
                                      PermisosService permisos,
                                      EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL', 'SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public Page<PersonaCapacitacionResponseDto> findAll(Pageable pageable) {
        return repository.findAll(
                PersonaCapacitacionRepository.visibles(permisos.actorActual().areas()), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL', 'SUPERVISION_PRODUCCION')")
    @Transactional(readOnly = true)
    public PersonaCapacitacionResponseDto findById(UUID id) {
        var filtro = PersonaCapacitacionRepository.visibles(permisos.actorActual().areas());
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL')")
    @Transactional
    public PersonaCapacitacionResponseDto create(PersonaCapacitacionRequestDto request) {
        var actor = permisos.actorActual();
        PersonaCapacitacion entidad = new PersonaCapacitacion();
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        var guardada = repository.saveAndFlush(entidad);
        entidades.refrescar(guardada);
        return toResponse(guardada);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL')")
    @Transactional
    public PersonaCapacitacionResponseDto update(UUID id, PersonaCapacitacionRequestDto request) {
        var actor = permisos.actorActual();
        PersonaCapacitacion entidad = entidades.bloquear(PersonaCapacitacion.class, id);
        verificarAreas(entidad, actor.areas());
        applyRequest(request, entidad);
        verificarAreas(entidad, actor.areas());
        repository.flush();
        entidades.refrescar(entidad);
        return toResponse(entidad);
    }

    @PreAuthorize("@permisosService.tieneAlgunRol('PERSONAL')")
    @Transactional
    public void delete(UUID id) {
        var actor = permisos.actorActual();
        PersonaCapacitacion entidad = entidades.bloquear(PersonaCapacitacion.class, id);
        verificarAreas(entidad, actor.areas());
        repository.delete(entidad);
        repository.flush();
    }

    private void verificarAreas(PersonaCapacitacion entidad, Set<UUID> areas) {
        AmbitoAreas.verificar(areas, () -> AmbitoAreas.dePersona(entidad.getPersona()));
    }

    private void applyRequest(PersonaCapacitacionRequestDto request, PersonaCapacitacion entity) {
        if (request.venceEn() != null && request.venceEn().isBefore(request.obtenidaEn())) {
            throw new InvalidRequestException("El vencimiento no puede ser anterior a la fecha de obtención.");
        }

        entity.setPersona(entidades.referencia(Persona.class, request.personaId()));
        entity.setCapacitacion(entidades.referencia(Capacitacion.class, request.capacitacionId()));
        entity.setObtenidaEn(request.obtenidaEn());
        entity.setVenceEn(request.venceEn());
    }

    private PersonaCapacitacionResponseDto toResponse(PersonaCapacitacion entity) {
        return new PersonaCapacitacionResponseDto(
            entity.getId(),
            entidades.id(entity.getPersona()),
            entidades.id(entity.getCapacitacion()),
            entity.getObtenidaEn(),
            entity.getVenceEn()
        );
    }
}
