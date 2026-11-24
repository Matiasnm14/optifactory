package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.entity.BaseEntity;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional(readOnly = true)
public abstract class ReadOnlyCrudService<E extends BaseEntity, R> {

    protected final JpaRepository<E, UUID> repository;

    @PersistenceContext
    protected EntityManager entityManager;

    protected ReadOnlyCrudService(JpaRepository<E, UUID> repository) {
        this.repository = repository;
    }

    public Page<R> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    public R findById(UUID id) {
        return toResponse(findEntity(id));
    }

    protected <T extends BaseEntity> T reference(Class<T> type, UUID id) {
        return id == null ? null : entityManager.getReference(type, id);
    }

    protected UUID referenceId(BaseEntity entity) {
        return entity == null ? null
                : (UUID) entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
    }

    protected abstract R toResponse(E entity);

    protected E findEntity(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }
}
