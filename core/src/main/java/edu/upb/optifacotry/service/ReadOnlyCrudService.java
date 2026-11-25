package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.entity.BaseEntity;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

import static edu.upb.optifacotry.service.AccionPermiso.CONSULTAR;

@Transactional(readOnly = true)
public abstract class ReadOnlyCrudService<E extends BaseEntity, R> {

    protected final JpaRepository<E, UUID> repository;
    private final Class<E> tipo;

    @Autowired
    private PermisosService permisos;

    @PersistenceContext
    protected EntityManager entityManager;

    protected ReadOnlyCrudService(JpaRepository<E, UUID> repository) {
        this.repository = repository;
        this.tipo = resolverTipo();
    }

    public Page<R> findAll(Pageable pageable) {
        return listar(Specification.unrestricted(), pageable);
    }

    public R findById(UUID id) {
        Specification<E> filtro = acceso(CONSULTAR).filtro();
        return consultas().findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    protected Page<R> listar(Specification<E> filtro, Pageable pageable) {
        Specification<E> autorizados = acceso(CONSULTAR).filtro();
        return consultas().findAll(autorizados.and(filtro), pageable).map(this::toResponse);
    }

    protected AlcancePermiso acceso(AccionPermiso accion) {
        return acceso(accion, actorActual());
    }

    protected AlcancePermiso acceso(AccionPermiso accion, PermisosService.Actor actor) {
        return PoliticaPermisos.exigir(tipo, accion, actor);
    }

    protected PermisosService.Actor actorActual() {
        return permisos.actorActual();
    }

    private SimpleJpaRepository<E, UUID> consultas() {
        return new SimpleJpaRepository<>(tipo, entityManager);
    }

    @SuppressWarnings("unchecked")
    private Class<E> resolverTipo() {
        return (Class<E>) Objects.requireNonNull(
                ResolvableType.forClass(getClass()).as(ReadOnlyCrudService.class).resolveGeneric(0),
                "El servicio debe indicar el tipo de entidad.");
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

    protected E findEntityForUpdate(UUID id) {
        E entidad = entityManager.find(tipo, id, LockModeType.PESSIMISTIC_WRITE);
        if (entidad == null) {
            throw new ResourceNotFoundException(id);
        }
        return entidad;
    }
}
