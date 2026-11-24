package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public abstract class CrudService<E extends BaseEntity, Q, R> extends ReadOnlyCrudService<E, R> {

    protected CrudService(JpaRepository<E, UUID> repository) {
        super(repository);
    }

    @Transactional
    public R create(Q request) {
        E entity = newEntity();
        applyRequest(request, entity);
        E saved = repository.saveAndFlush(entity);
        entityManager.refresh(saved);
        return toResponse(saved);
    }

    @Transactional
    public R update(UUID id, Q request) {
        E entity = findEntity(id);
        applyRequest(request, entity);
        repository.flush();
        entityManager.refresh(entity);
        return toResponse(entity);
    }

    @Transactional
    public void delete(UUID id) {
        repository.delete(findEntity(id));
        repository.flush();
    }

    protected abstract E newEntity();

    protected abstract void applyRequest(Q request, E entity);
}
