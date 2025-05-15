package persistence;

import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.apache.logging.log4j.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class UserDaoTest {

    private GenericDao<User> userDao;
    private static final Logger logger = LogManager.getLogger(UserDaoTest.class);

    @BeforeEach
    void setUp() {
        // Rebuild & seed schema (cleandb.sql already seeds two users: Alice(id=1), Bob(id=2))
        Database database = Database.getInstance();
        database.runSQL("cleandb.sql");
        SessionFactoryProvider.createSessionFactory();
        userDao = new GenericDao<>(User.class);
    }

    // —— Retrieval tests against the seeded rows ——

    @Test
    public void testGetAllUsesSeedData() {
        List<User> users = userDao.getAll();
        assertEquals(2, users.size(),
                "cleandb.sql should seed exactly 2 users");
    }

    @Test
    public void testGetByIdUsesSeedData() {
        User alice = userDao.getById(1);
        assertNotNull(alice, "User #1 should exist");
        assertEquals("Alice", alice.getFirstName());
        assertEquals("Wonder", alice.getLastName());
    }

    @Test
    public void testGetByIdNotFound() {
        assertNull(userDao.getById(999),
                "Unknown ID should return null");
    }

    @Test
    public void testCreate() {
        logger.debug("Testing save method...");
        User user = new User("John", "Doe", "john.doe" + System.currentTimeMillis() + "@example.com");
        int userId = userDao.insert(user);
        assertNotEquals(0, userId, "User ID should not be zero after insertion");

        assertEquals(3, userDao.getAll().size(),
                "After create, total users should go from 2 → 3");
    }

    @Test
    public void testUpdate() {
        logger.debug("Testing update method...");
        // modify seeded user #1
        User alice = userDao.getById(1);
        alice.setFirstName("AliceUpdated");
        userDao.update(alice);

        User updated = userDao.getById(1);
        assertEquals("AliceUpdated", updated.getFirstName(),
                "First name should reflect the update");
    }

    @Test
    public void testDelete() {
        logger.debug("Testing delete method...");
        // delete seeded user #2
        User bob = userDao.getById(2);
        assertNotNull(bob, "User #2 should exist before deletion");
        userDao.deleteEntity(bob);

        assertNull(userDao.getById(2),
                "User #2 should be null after deletion");
        assertEquals(1, userDao.getAll().size(),
                "After delete, total users should go from 2 → 1");
    }
}
