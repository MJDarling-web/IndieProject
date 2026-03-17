/**package persistence;

import entity.CommutingLog;
import entity.Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CommutingLogDaoTest {

    private GenericDao<CommutingLog> commutingLogDao;
    private GenericDao<Profile> userDao;

    @BeforeEach
    void setUp() {
        Database database = new Database();
        SessionFactoryProvider.createSessionFactory();
        database.runSQL("cleandb.sql");
        commutingLogDao = new GenericDao<>(CommutingLog.class);
        userDao = new GenericDao<>(Profile.class);
        commutingLogDao.getAll().forEach(commutingLogDao::deleteEntity);
    }

    private Profile buildValidUser(String firstName, String lastName, String email) {
        Profile user = new Profile();
        user.setId(UUID.randomUUID().toString());
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        return user;
    }

    @Test
    public void testCreate() {
        Profile user = buildValidUser(
                "Jake",
                "Smith",
                "jake.smith" + System.currentTimeMillis() + "@example.com"
        );
        userDao.insert(user);

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
        Profile user = buildValidUser(
                "Jane",
                "Doe",
                "jane.doe" + System.currentTimeMillis() + "@example.com"
        );
        userDao.insert(user);

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
        Profile user = buildValidUser(
                "James",
                "Snow",
                "james.s" + System.currentTimeMillis() + "@example.com"
        );
        userDao.insert(user);

        CommutingLog commutingLog = new CommutingLog();
        commutingLog.setUser(user);
        commutingLog.setDate(new Date());
        commutingLog.setCommuteType("Car");
        commutingLog.setTimeSpent(50.0);
        commutingLog.setCost(30.0);

        int logId = commutingLogDao.insert(commutingLog);

        commutingLog.setCommuteType("Bicycle");
        commutingLogDao.update(commutingLog);

        CommutingLog updatedLog = commutingLogDao.getById(logId);
        assertEquals("Bicycle", updatedLog.getCommuteType(), "Updated commute type should match");
    }

    @Test
    public void testDelete() {
        Profile user = buildValidUser(
                "Jo",
                "Doe",
                "jo.doe" + System.currentTimeMillis() + "@example.com"
        );
        userDao.insert(user);

        CommutingLog commutingLog = new CommutingLog();
        commutingLog.setUser(user);
        commutingLog.setDate(new Date());
        commutingLog.setCommuteType("Walking");
        commutingLog.setTimeSpent(20.0);
        commutingLog.setCost(5.0);

        int logId = commutingLogDao.insert(commutingLog);

        commutingLogDao.deleteEntity(commutingLog);
        CommutingLog deletedLog = commutingLogDao.getById(logId);
        assertNull(deletedLog, "Commuting log should be null after deletion");
    }

    @Test
    public void testGetAll() {
        Profile user1 = buildValidUser(
                "Alice",
                "Wonderland",
                "alice" + System.currentTimeMillis() + "@example.com"
        );
        userDao.insert(user1);

        CommutingLog log1 = new CommutingLog();
        log1.setUser(user1);
        log1.setDate(new Date());
        log1.setCommuteType("Car");
        log1.setTimeSpent(30.0);
        log1.setCost(10.0);
        commutingLogDao.insert(log1);

        Profile user2 = buildValidUser(
                "Bob",
                "Builder",
                "bob" + System.currentTimeMillis() + "@example.com"
        );
        userDao.insert(user2);

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
}*/
