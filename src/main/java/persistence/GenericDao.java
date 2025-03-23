package persistence;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.*;

import java.util.List;

/**
 * The type Generic dao.
 *
 * @param <T> the type parameter
 */
public class GenericDao<T> {
    private Class<T> type;
    private final Logger logger = LogManager.getLogger(this.getClass());
    /**
     * Instantiates a new Generic dao.
     *
     * @param type the type
     */
    public GenericDao(Class<T> type) {
        this.type = type;
        logger.info("connect");
    }

    private Session getSession() {
        return SessionFactoryProvider.getSessionFactory().openSession();
    }

    /**
     * Gets by id.
     *
     * @param id the id
     * @return the by id
     */
    public T getById(int id) {
        Session session = getSession();
        T entity = session.get(type, id);
        session.close();
        return entity;
    }

    /**
     * Insert entity
     *
     * @param entity the entity
     * @return the int
     */
    public int insert(T entity) {
        int id = 0;
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(entity);  // Ensure this is a new entity and does not conflict with others.
            session.flush(); // Flush changes to the database.
            transaction.commit(); // Commit the transaction
            id = (int) session.getIdentifier(entity); // Retrieve the ID after commit
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();  // Rollback on error
            }
            throw e;  // Propagate the error
        } finally {
            session.close();  // Always close the session
        }
        return id;
    }


    /**
     * Update.
     *
     * @param entity the entity
     */
    public void update(T entity) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        session.update(entity);  // Merge is used for update and insert if the entity is detached
        transaction.commit();
        session.close();
    }

    /**
     * Delete entity.
     *
     * @param entity the entity
     */
    public void deleteEntity(T entity) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.remove(entity);
            session.flush();  // Ensures that the entity is immediately deleted from DB
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error during delete operation", e);
        } finally {
            session.close();
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
     * @param value        the value
     * @return the list of entities matching the property
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
     * Gets a list of entities with a custom query.
     *
     * @param hql the HQL query string
     * @return the list of entities matching the query
     */
    public List<T> getByCustomQuery(String hql) {
        try (Session session = getSession()) {
            return session.createQuery(hql, type).getResultList();
        }
    }
}
