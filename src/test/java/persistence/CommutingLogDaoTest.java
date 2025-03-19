package persistence;

import entity.CommutingLog;
import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;

public class CommutingLogDaoTest {

    private GenericDao<CommutingLog> commutingLogDao;
    private GenericDao<User> userDao;

    @BeforeEach
    void setUp() {
        Database database = new Database();
        SessionFactoryProvider.createSessionFactory();
        database.runSQL("cleandb.sql");
        commutingLogDao = new GenericDao<>(CommutingLog.class);
        userDao = new GenericDao<>(User.class);
    }

    @Test
    public void testCreate() {
        User user = new User("Jake", "Smith", "Jake.smith" + + System.currentTimeMillis() + "@example.com", "password123");
        int userId = userDao.insert(user);
        CommutingLog commutingLog = new CommutingLog();
        commutingLog.setUser(user);
        commutingLog.setDate(new Date());
        commutingLog.setCommuteType("Car");
        commutingLog.setTimeSpent(45.5);
        commutingLog.setCost(20.0);

        int logId = commutingLogDao.insert(commutingLog);
        assertNotEquals(0, logId, "Log ID should not be zero after insertion");
    }

    @Test
    public void testGetById() {
        User user = new User("Jane", "Doe", "jane.doe" + + System.currentTimeMillis() + "@example.com", "password123");
        int userId = userDao.insert(user);
        CommutingLog commutingLog = new CommutingLog();
        commutingLog.setUser(user);
        commutingLog.setDate(new Date());
        commutingLog.setCommuteType("Bus");
        commutingLog.setTimeSpent(30.0);
        commutingLog.setCost(15.0);

        int logId = commutingLogDao.insert(commutingLog);
        CommutingLog retrievedLog = commutingLogDao.getById(logId);

        assertNotNull(retrievedLog, "Commuting log should be retrieved by ID");
        assertEquals("Bus", retrievedLog.getCommuteType(), "Commute type should match");
    }

    @Test
    public void testUpdate() {
        User user = new User("James", "Snow", "james.s" + + System.currentTimeMillis() +" @example.com", "password123");
        int userId = userDao.insert(user);
        CommutingLog commutingLog = new CommutingLog();
        commutingLog.setUser(user);
        commutingLog.setDate(new Date());
        commutingLog.setCommuteType("Car");
        commutingLog.setTimeSpent(50.0);
        commutingLog.setCost(30.0);

        int logId = commutingLogDao.insert(commutingLog);

        // Update commuting log
        commutingLog.setCommuteType("Bicycle");
        commutingLogDao.update(commutingLog);

        CommutingLog updatedLog = commutingLogDao.getById(logId);
        assertEquals("Bicycle", updatedLog.getCommuteType(), "Updated commute type should match");
    }

    @Test
    public void testDelete() {
        User user = new User("Jo", "Doe", "jo.doe" + + System.currentTimeMillis() +" @example.com", "password123");
        int userId = userDao.insert(user);
        CommutingLog commutingLog = new CommutingLog();
        commutingLog.setUser(user);
        commutingLog.setDate(new Date());
        commutingLog.setCommuteType("Walking");
        commutingLog.setTimeSpent(20.0);
        commutingLog.setCost(5.0);

        int logId = commutingLogDao.insert(commutingLog);

        // Delete commuting log
        commutingLogDao.deleteEntity(commutingLog);
        CommutingLog deletedLog = commutingLogDao.getById(logId);
        assertNull(deletedLog, "Commuting log should be null after deletion");
    }

    @Test
    public void testGetAll() {
        User user1 = new User("Alice", "Wonderland", "alice" + + System.currentTimeMillis() +" @example.com", "password123");
        int userId1 = userDao.insert(user1);
        CommutingLog log1 = new CommutingLog();
        log1.setUser(user1);
        log1.setDate(new Date());
        log1.setCommuteType("Car");
        log1.setTimeSpent(30.0);
        log1.setCost(10.0);
        commutingLogDao.insert(log1);

        User user2 = new User("Bob", "Builder", "bob" + + System.currentTimeMillis() +"@example.com", "password123");
        int userId2 = userDao.insert(user2);
        CommutingLog log2 = new CommutingLog();
        log2.setUser(user2);
        log2.setDate(new Date());
        log2.setCommuteType("Bus");
        log2.setTimeSpent(60.0);
        log2.setCost(15.0);
        commutingLogDao.insert(log2);

        List<CommutingLog> logs = commutingLogDao.getAll();
        assertTrue(logs.size() >= 2, "There should be at least 2 commuting logs in the database");
    }
}
