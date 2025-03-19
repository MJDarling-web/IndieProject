package persistence;

import entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Data Access Object (DAO) for the User entity.
 */
public class UserDao {

    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Gets a user by ID.
     *
     * @param id the ID of the user
     * @return the User object, or null if not found
     */
    public User getById(int id) {
        try (Session session = SessionFactoryProvider.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        }
    }

    /**
     * Inserts a new user into the database.
     *
     * @param user the User object to insert
     * @return the ID of the newly inserted user
     */
    public int insert(User user) {
        int id = 0;
        try (Session session = SessionFactoryProvider.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            id = (int) session.save(user);
            transaction.commit();
        } catch (Exception e) {
            logger.error("Error inserting user: " + e.getMessage(), e);
        }
        return id;
    }

    /**
     * Updates an existing user in the database.
     *
     * @param user the User object to update
     */
    public void update(User user) {
        try (Session session = SessionFactoryProvider.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
        } catch (Exception e) {
            logger.error("Error updating user: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a user from the database.
     *
     * @param user the User object to delete
     */
    public void delete(User user) {
        try (Session session = SessionFactoryProvider.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(user);
            transaction.commit();
        } catch (Exception e) {
            logger.error("Error deleting user: " + e.getMessage(), e);
        }
    }

    /**
     * Gets all users from the database.
     *
     * @return a list of all User objects
     */
    public List<User> getAll() {
        try (Session session = SessionFactoryProvider.getSessionFactory().openSession()) {
            List<User> users = session.createQuery("FROM User", User.class).getResultList();
            logger.info("Fetched " + users.size() + " users from the database.");
            for (User user : users) {
                logger.info("User: " + user.getFirstName() + " " + user.getLastName() + " - " + user.getEmail());
            }
            return users;
        } catch (Exception e) {
            logger.error("Error fetching users: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Gets users by a specific property (e.g., first name, last name, email) using a LIKE clause.
     *
     * @param propertyName the name of the property to search by
     * @param value        the value to search for
     * @return a list of User objects matching the search criteria
     */
    public List<User> getByPropertyLike(String propertyName, String value) {
        try (Session session = SessionFactoryProvider.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM User WHERE " + propertyName + " LIKE :value", User.class)
                    .setParameter("value", "%" + value + "%")
                    .getResultList();
        }
    }

    /**
     * Gets a user by email.
     *
     * @param email the email to search for
     * @return the User object, or null if not found
     */
    public User getByEmail(String email) {
        try (Session session = SessionFactoryProvider.getSessionFactory().openSession()) {
            return session.createQuery("FROM User WHERE email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();
        }
    }
}