package persistence;

import entity.CommutingLog;
import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class CommutingLogDaoTest {

    private GenericDao<CommutingLog> commutingLogDao;
    private GenericDao<User>         userDao;

    @BeforeEach
    void setUp() {
        // Rebuild schema & seed via cleandb.sql
        Database database = new Database();
        SessionFactoryProvider.createSessionFactory();
        database.runSQL("cleandb.sql");

        commutingLogDao = new GenericDao<>(CommutingLog.class);
        userDao         = new GenericDao<>(User.class);
    }

    // —— Retrieval tests against the seeded rows ——

    @Test
    public void testGetAllUsesSeedData() {
        List<CommutingLog> logs = commutingLogDao.getAll();
        // cleandb.sql seeds exactly 3 rows
        assertEquals(3, logs.size(),
                "cleandb.sql should produce exactly 3 commuting logs");
    }

    @Test
    public void testGetByIdUsesSeedData() {
        // Seed created ID=2 → Bus for user 1
        CommutingLog log = commutingLogDao.getById(2);
        assertNotNull(log, "Log #2 should exist");
        assertEquals("Bus", log.getCommuteType());
        assertEquals(1, log.getUser().getId());
    }

    @Test
    public void testGetByIdNotFound() {
        // No row at ID=999
        assertNull(commutingLogDao.getById(999),
                "Unknown ID should return null");
    }

    // —— CRUD tests (adjusted to account for initial seed of 3 rows) ——

    @Test
    public void testCreateLog() {
        User u = new User("Charlie", "Day", "charlie.day@example.com");
        userDao.insert(u);

        CommutingLog newLog = new CommutingLog();
        newLog.setUser(u);
        newLog.setDate(new Date());
        newLog.setCommuteType("Car");
        newLog.setTimeSpent(20.0);
        newLog.setCost(7.5);
        int newId = commutingLogDao.insert(newLog);

        assertNotEquals(0, newId);
        // 3 seeded + 1 new → 4 total
        assertEquals(4, commutingLogDao.getAll().size(),
                "After create, total rows should go from 3 → 4");
    }

    @Test
    public void testUpdateLog() {
        // change the seeded log #1
        CommutingLog log = commutingLogDao.getById(1);
        log.setCommuteType("Train");
        commutingLogDao.update(log);

        CommutingLog updated = commutingLogDao.getById(1);
        assertEquals("Train", updated.getCommuteType());
    }

    @Test
    public void testDeleteLog() {
        // delete seeded log #3
        CommutingLog toDelete = commutingLogDao.getById(3);
        commutingLogDao.deleteEntity(toDelete);

        assertNull(commutingLogDao.getById(3),
                "After delete, ID=3 should no longer exist");
        // 3 seeded → 2 remain
        assertEquals(2, commutingLogDao.getAll().size(),
                "Total rows should go from 3 → 2 after delete");
    }
}
