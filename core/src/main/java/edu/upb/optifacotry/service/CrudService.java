package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static edu.upb.optifacotry.service.AccionPermiso.CREAR;
import static edu.upb.optifacotry.service.AccionPermiso.ELIMINAR;
import static edu.upb.optifacotry.service.AccionPermiso.MODIFICAR;

public abstract class CrudService<E extends BaseEntity, Q, R> extends ReadOnlyCrudService<E, R> {

    protected CrudService(JpaRepository<E, UUID> repository) {
        super(repository);
    }

    @Transactional
    public R create(Q request) {
        PermisosService.Actor actor = actorActual();
        AlcancePermiso permiso = acceso(CREAR, actor);
        E entity = newEntity();
        prepararEscritura(request, entity, permiso, actor);
        E saved = repository.saveAndFlush(entity);
        entityManager.refresh(saved);
        return toResponse(saved);
    }

    @Transactional
    public R update(UUID id, Q request) {
        PermisosService.Actor actor = actorActual();
        AlcancePermiso permiso = acceso(MODIFICAR, actor);
        E entity = findEntityForUpdate(id);
        permiso.verificar(entity);
        prepararEscritura(request, entity, permiso, actor);
        repository.flush();
        entityManager.refresh(entity);
        return toResponse(entity);
    }

    @Transactional
    public void delete(UUID id) {
        AlcancePermiso permiso = acceso(ELIMINAR);
        E entity = findEntityForUpdate(id);
        permiso.verificar(entity);
        repository.delete(entity);
        repository.flush();
    }

    protected abstract E newEntity();

    protected abstract void applyRequest(Q request, E entity);

    // Las validaciones sobre otras entidades pueden ejecutarse una vez autorizado el alcance.
    protected void completarEscritura(Q request, E entity, PermisosService.Actor actor) {
    }

    private void prepararEscritura(Q request, E entity, AlcancePermiso permiso, PermisosService.Actor actor) {
        applyRequest(request, entity);
        permiso.verificar(entity);
        completarEscritura(request, entity, actor);
    }
}
