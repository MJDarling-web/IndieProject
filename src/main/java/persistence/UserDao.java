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
    /**
     * insert a new user or update a user
     * @param user the User to save or update
     */
    public void insertUser(User user) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            session.merge(user);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                try {
                    transaction.rollback();
                } catch (Exception rollbackEx) {
                    logger.error("Rollback failed: ", rollbackEx);
                }
            }
            logger.error("Error inserting user: ", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }


    /* get user by email address
     * @param email
     */
    public User getByEmail(String email) {
        User user = null;
        try (Session session = sessionFactory.openSession()) {
            user = session.createQuery("FROM User WHERE email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();
        } catch (Exception e) {
            logger.error("Error getting user by email", e);
        }
        return user;
    }

}
