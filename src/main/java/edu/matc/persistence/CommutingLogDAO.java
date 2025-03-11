package edu.matc.persistence;

import edu.matc.entity.CommutingLog;  // Ensure this import is correct
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class CommutingLogDAO implements GenericDAO<CommutingLog> {

    private final Logger logger = LogManager.getLogger(this.getClass());
    SessionFactory sessionFactory = SessionFactoryProvider.getSessionFactory();

    /**
     * Get CommutingLog by id
     */
    @Override
    public CommutingLog getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(CommutingLog.class, id);
        }
    }

    /**
     * Insert a new CommutingLog
     */
    @Override
    public int insert(CommutingLog commutingLog) {
        int id = 0;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(commutingLog);
            transaction.commit();
            id = commutingLog.getId();
        }
        return id;
    }

    /**
     * Update an existing CommutingLog
     */
    @Override
    public void update(CommutingLog commutingLog) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(commutingLog);
            transaction.commit();
        }
    }

    /**
     * Delete a CommutingLog
     */
    @Override
    public void delete(CommutingLog commutingLog) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            if (!session.contains(commutingLog)) {
                commutingLog = session.merge(commutingLog);
            }
            session.delete(commutingLog);
            transaction.commit();
        }
    }

    /**
     * Return a list of all CommutingLogs
     *
     * @return List of all CommutingLogs
     */
    @Override
    public List<CommutingLog> getAll() {
        try (Session session = sessionFactory.openSession()) {
            // This line mimics the query syntax from your AuthorDAO
            List<CommutingLog> commutingLogs = session.createQuery("from CommutingLog", CommutingLog.class).getResultList();
            logger.debug("The list of commuting logs: " + commutingLogs);
            return commutingLogs;}
    }
}
