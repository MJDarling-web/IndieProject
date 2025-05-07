package persistence;

import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.apache.logging.log4j.*;
import static org.junit.jupiter.api.Assertions.*;
import jakarta.persistence.*;

import java.util.List;

public class UserDaoTest {

    private GenericDao<User> userDao;

    // Declare the logger at the class level
    private static final Logger logger = LogManager.getLogger(UserDaoTest.class);

    @BeforeEach
    void setUp() {
        Database database = Database.getInstance();
        database.runSQL("cleandb.sql");

        SessionFactoryProvider.createSessionFactory();

        userDao = new GenericDao<>(User.class);
        userDao.getAll().forEach(userDao::deleteEntity);


    }

    @Test
    public void testCreate() {
        logger.debug("Testing save method...");
        User user = new User("John", "Doe", "john.doe" + System.currentTimeMillis() + "@example.com", "password123");
        int userId = userDao.insert(user);
        assertNotEquals(0, userId, "User ID should not be zero after insertion");
    }

    @Test
    public void testGetById() {
        logger.debug("Testing retrieval by ID...");
        User user = new User("Jane", "Doe", "jane" + System.currentTimeMillis() + "@example.com", "password123");
        int userId = userDao.insert(user);
        User retrievedUser = userDao.getById(userId);
        assertNotNull(retrievedUser, "User should be retrieved by ID");
        assertEquals("Jane", retrievedUser.getFirstName(), "First name should match");
    }

    @Test
    public void testUpdate() {
        logger.debug("Testing update method...");
        User user = new User("John", "Smith", "john.smith" + System.currentTimeMillis() + "@example.com", "password123");
        int userId = userDao.insert(user);

        // Update user information
        user.setFirstName("John Updated");
        userDao.update(user);

        User updatedUser = userDao.getById(userId);
        assertEquals("John Updated", updatedUser.getFirstName(), "Updated first name should match");
    }

    @Test
    public void testDelete() {
        logger.debug("Testing delete method...");
        User user = new User("Jo", "Do", "jo.d" + System.currentTimeMillis() + "@example.com", "password123");
        int userId = userDao.insert(user);

        // Delete user
        User retrievedUser = userDao.getById(userId);
        assertNotNull(retrievedUser);

        userDao.deleteEntity(user);
        User deletedUser = userDao.getById(userId);
        assertNull(deletedUser, "User should be null after deletion");
    }

    @Test
    public void testGetAll() {
        logger.debug("Testing retrieval of all users...");
        User user1 = new User("Alice", "Wonderland", "alice" + System.currentTimeMillis() + "@example.com", "password123");
        User user2 = new User("Bob", "Builder", "bob" + System.currentTimeMillis() + "@example.com", "password123");
        userDao.insert(user1);
        userDao.insert(user2);

        List<User> users = userDao.getAll();
        assertTrue(users.size() >= 2, "There should be at least 2 users in the database");
    }

    @Test
    void restoreDatebase() {
        setUp();
    }
}
