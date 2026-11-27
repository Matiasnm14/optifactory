package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.entity.BaseEntity;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
class EntidadesJpa {

    @PersistenceContext
    private EntityManager entityManager;

    <E extends BaseEntity> E referencia(Class<E> tipo, UUID id) {
        return id == null ? null : entityManager.getReference(tipo, id);
    }

    UUID id(BaseEntity entidad) {
        return entidad == null ? null
                : (UUID) entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entidad);
    }

    <E extends BaseEntity> E bloquear(Class<E> tipo, UUID id) {
        E entidad = entityManager.find(tipo, id, LockModeType.PESSIMISTIC_WRITE);
        if (entidad == null) {
            throw new ResourceNotFoundException(id);
        }
        return entidad;
    }

    void refrescar(BaseEntity entidad) {
        entityManager.refresh(entidad);
    }
}
