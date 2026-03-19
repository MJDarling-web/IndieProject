package persistence;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Generic DAO for CRUD and query operations.
 *
 * @param <T> the entity type
 */
public class GenericDao<T> {

    private final Class<T> type;
    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Instantiates a new GenericDao.
     *
     * @param type the entity class type
     */
    public GenericDao(Class<T> type) {
        this.type = type;
        logger.info("GenericDao initialized for {}", type.getSimpleName());
    }

    private Session getSession() {
        return SessionFactoryProvider.getSessionFactory().openSession();
    }

    /**
     * Gets an entity by its ID.
     * Works with Integer, UUID, etc.
     *
     * @param id the entity ID
     * @return the entity or null
     */
    public T getById(Object id) {
        try (Session session = getSession()) {
            return session.get(type, id);
        }
    }

    /**
     * Insert entity.
     *
     * @param entity the entity
     * @return the generated identifier as Object
     */
    public Object insert(T entity) {
        Transaction transaction = null;

        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            session.persist(entity);
            session.flush();
            transaction.commit();
            return session.getIdentifier(entity);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error inserting entity", e);
            throw e;
        }
    }

    /**
     * Update entity.
     *
     * @param entity the entity
     */
    public void update(T entity) {
        Transaction transaction = null;

        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            session.merge(entity);
            transaction.commit();
        } catch (Exception e) {
            logger.error("Error updating entity", e);
            throw e;
        }
    }

    /**
     * Delete entity.
     *
     * @param entity the entity
     */
    public void deleteEntity(T entity) {
        Transaction transaction = null;

        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            session.remove(session.contains(entity) ? entity : session.merge(entity));
            transaction.commit();
        } catch (Exception e) {
            logger.error("Error during delete operation", e);
            throw e;
        }
    }

    /**
     * Gets all entities.
     *
     * @return the list of all entities
     */
    public List<T> getAll() {
        try (Session session = getSession()) {
            return session.createQuery("FROM " + type.getSimpleName(), type).getResultList();
        }
    }

    /**
     * Gets entities by property like.
     *
     * @param propertyName the property name
     * @param value the value
     * @return matching entities
     */
    public List<T> getByPropertyLike(String propertyName, String value) {
        try (Session session = getSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> query = builder.createQuery(type);
            Root<T> root = query.from(type);
            Expression<String> property = root.get(propertyName);
            query.where(builder.like(property, "%" + value + "%"));
            return session.createQuery(query).getResultList();
        }
    }

    /**
     * Gets entities by property equal.
     *
     * @param propertyName the property name
     * @param value the value
     * @return matching entities
     */
    public List<T> getByPropertyEqual(String propertyName, Object value) {
        try (Session session = getSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> query = builder.createQuery(type);
            Root<T> root = query.from(type);
            query.where(builder.equal(root.get(propertyName), value));
            return session.createQuery(query).getResultList();
        }
    }

    /**
     * Gets a list of entities with a custom parameterized HQL query.
     *
     * Example:
     * getByCustomQuery("from TransportationProfile where user.id = :userId", "userId", userId)
     *
     * @param hql the HQL query
     * @param paramName the parameter name
     * @param value the parameter value
     * @return matching entities
     */
    public List<T> getByCustomQuery(String hql, String paramName, Object value) {
        try (Session session = getSession()) {
            return session.createQuery(hql, type)
                    .setParameter(paramName, value)
                    .getResultList();
        }
    }
}