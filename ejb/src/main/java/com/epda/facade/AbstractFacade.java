package com.epda.facade;

import com.epda.model.AuditLog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import java.util.List;
import java.util.Set;

public abstract class AbstractFacade<T> {

    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public <T> void create(T entity) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.flush(); // This forces the persist to occur and the ID to be generated.
            em.refresh(entity); // This refetches the entity from the database, ensuring you have the generated ID and all defaults set by the database.
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException(
                "Failed to persist entity: " + e.getMessage(),
                e
            );
        } finally {
            em.close();
        }
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
        logOperation("EDIT", entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
        logOperation("REMOVE", entity);
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        // CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        // CriteriaQuery<T> cq = cb.createQuery(entityClass);
        // Root<T> root = cq.from(entityClass);
        // cq.select(root);
        // return getEntityManager().createQuery(cq).getResultList();

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);

        // Attempt to sort by "updatedAt" if the attribute exists
        EntityType<T> entityType = getEntityManager()
            .getMetamodel()
            .entity(entityClass);
        Set<Attribute<? super T, ?>> attributes = entityType.getAttributes();
        boolean hasUpdatedAt = attributes
            .stream()
            .anyMatch(attr -> attr.getName().equals("updatedAt"));

        if (hasUpdatedAt) {
            cq.orderBy(cb.desc(root.get("updatedAt")));
        }

        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.select(root);
        TypedQuery<T> q = getEntityManager().createQuery(cq); // Use TypedQuery here
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList(); // This is now type-safe
    }

    public int count() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> root = cq.from(entityClass);
        cq.select(cb.count(root));
        jakarta.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    private void logOperation(String operation, T entity) {
        Long entityId = getId(entity);
        AuditLog log = new AuditLog(
            entityClass.getSimpleName(),
            entityId,
            operation
        );
        getEntityManager().persist(log);
    }

    // This is a simplistic way to extract the ID from an entity.
    // You'll need to adapt this method to your entities, possibly by using reflection or by requiring
    // entities to implement a common interface that provides access to the ID.
    private Long getId(T entity) {
        try {
            var method = entityClass.getMethod("getId");
            return (Long) method.invoke(entity);
        } catch (Exception e) {
            // Handle exception: log it, wrap it in a runtime exception, etc.
            return null;
        }
    }
}
