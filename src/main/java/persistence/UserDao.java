package persistence;

import entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.util.List;

/**
 * Data Access Object (DAO) for the User entity.
 */
public class UserDao {

    private static final SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

    /**
     * Get all users from the database.
     */
    public List<User> getAllUsers() {
        try (Session session = factory.openSession()) {
            return session.createQuery("FROM User", User.class).list();
        }
    }
}
