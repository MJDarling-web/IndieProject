package persistence;

import entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;
import jakarta.persistence.*;


/**
 * Data Access Object (DAO) for the User entity.
 */
public class UserDao {
    private static final Logger logger = LogManager.getLogger(UserDao.class);
    private final SessionFactory sessionFactory;

    /**
     * Constructor - Uses the provided SessionFactory
     */
    public UserDao() {
        this.sessionFactory = SessionFactoryProvider.getSessionFactory();
    }

    /**
     * Get all users from the database
     * @return List of users
     */
    public List<User> getAllUsers() {
        List<User> usersList = null;
        Transaction transaction = null;
        logger.info("Get all users");
        logger.info("connecting");
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            // Corrected HQL query
            usersList = session.createQuery("FROM User", User.class).list();

            transaction.commit();
            logger.info("Retrieved {} users", usersList.size());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error fetching users: ", e);
        }
        return usersList;
    }
}
