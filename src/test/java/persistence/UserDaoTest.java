package persistence;

import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class UserDaoTest {

    private GenericDao<User> userDao;

    @BeforeEach
    void setUp() {
        Database database = new Database();
        SessionFactoryProvider.createSessionFactory();
        database.runSQL("cleandb.sql");
        userDao = new GenericDao<>(User.class);
    }

    @Test
    public void testCreate() {
        User user = new User("John", "Doe", "john.doe@example.com", "password123");
        int userId = userDao.insert(user);
        assertNotEquals(0, userId, "User ID should not be zero after insertion");
    }

    @Test
    public void testGetById() {
        User user = new User("Jane", "Doe", "jane.doe@example.com", "password123");
        int userId = userDao.insert(user);
        User retrievedUser = userDao.getById(userId);
        assertNotNull(retrievedUser, "User should be retrieved by ID");
        assertEquals("Jane", retrievedUser.getFirstName(), "First name should match");
    }

    @Test
    public void testUpdate() {
        User user = new User("John", "Smith", "john.smith@example.com", "password123");
        int userId = userDao.insert(user);

        // Update user information
        user.setFirstName("John Updated");
        userDao.update(user);

        User updatedUser = userDao.getById(userId);
        assertEquals("John Updated", updatedUser.getFirstName(), "Updated first name should match");
    }

    @Test
    public void testDelete() {
        User user = new User("Jo", "Do", "jo.d@example.com", "password123");
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
        User user1 = new User("Alice", "Wonderland", "alice@example.com", "password123");
        User user2 = new User("Bob", "Builder", "bob@example.com", "password123");
        userDao.insert(user1);
        userDao.insert(user2);

        List<User> users = userDao.getAll();
        assertTrue(users.size() >= 2, "There should be at least 2 users in the database");
    }
}
