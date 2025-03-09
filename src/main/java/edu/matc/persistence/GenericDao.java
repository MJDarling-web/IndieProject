/**
 *
 package edu.matc.persistence;

 import org.apache.logging.log4j.LogManager;
 import org.apache.logging.log4j.Logger;
 import org.hibernate.Session;
 import org.hibernate.SessionFactory;
 import org.hibernate.Transaction;
 import org.hibernate.query.Query;
 import org.hibernate.query.criteria.HibernateCriteriaBuilder;
 import org.hibernate.query.criteria.JpaCriteriaQuery;
 import org.hibernate.query.criteria.JpaRoot;

 import javax.persistence.criteria.CriteriaBuilder;
 import javax.persistence.criteria.CriteriaQuery;
 import javax.persistence.criteria.Root;
 import java.util.List;

 /**
 * a generic DAO inspired by paulas youtube video
 *

public class GenericDao {
    private Clsss<T> type;
    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Instantiate a new Generic dao
     *
     * @param type the entity type for example, user.
     *
    public GenericDao(Class<T> type) {
        this.type = type;
    }

    /**
     * Get entity by id
     * @param id entity id to search by
     * @return a entity
     *
    public <T>T getById(int id) {
        Session session = getSession();
        T entity = (T)session.get(type.class, id);
        session.close();
        return entity;
    }

    /**
     * Delete a book
     * @param book Book to be deleted
     *
    public void delete(T entity) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        session.delete(entity);
        transaction.commit();
        session.close();
    }

    /** Return a list of all books
     *
     * @return All books
     *

    public List<T> getAll() {
        Session session = getSession();

        CriteriaQuery<T> builder = session.getCriteriaBuilder();

        CriteriaQuery<T> query = builder.createQuery(type);
        Root<T> root = query.from(type);
        List<T> list = session.createQuery( query ).getResultList();
        session.close();
        return list;
    }


    /**
     * Returns an open session from the SessionFactory
     * @return session
     *
    private Session getSession() {
        return SessionFactoryProvider.getSessionFactory().openSession();
    }
}

 *
 */