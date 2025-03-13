package persistence;

import entity.CostProjection;
import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class CostProjectionDaoTest {

    private GenericDao<CostProjection> costProjectionDao;

    @BeforeEach
    void setUp() {
        Database database = new Database();
        SessionFactoryProvider.createSessionFactory();
        database.runSQL("cleandb.sql");  // Clears and sets up the database for tests
        costProjectionDao = new GenericDao<>(CostProjection.class);  // Initializes the DAO
    }

    @Test
    public void testCreate() {
        User user = new User("John", "Doe", "j@example.com", "password123");
        int userId = new GenericDao<>(User.class).insert(user);  // Insert a User to associate with the CostProjection

        CostProjection costProjection = new CostProjection();
        costProjection.setUser(user);
        costProjection.setTransportationMode("Car");
        costProjection.setDurationInMinutes(30);
        costProjection.setDistanceInMiles(10.0);
        costProjection.setDateAdded(new java.util.Date());

        int logId = costProjectionDao.insert(costProjection);
        assertNotEquals(0, logId, "Log ID should not be zero after insertion");
    }

    @Test
    public void testGetById() {
        // First, insert a user
        User user = new User("Jane", "Doe", "jane.doe@example.com", "password123");
        int userId = new GenericDao<>(User.class).insert(user);

        // Then insert a CostProjection
        CostProjection costProjection = new CostProjection();
        costProjection.setUser(user);
        costProjection.setTransportationMode("Bike");
        costProjection.setDurationInMinutes(45);
        costProjection.setDistanceInMiles(15.5);
        costProjection.setDateAdded(new java.util.Date());
        int logId = costProjectionDao.insert(costProjection);

        // Fetch the CostProjection by ID
        CostProjection retrievedCostProjection = costProjectionDao.getById(logId);
        assertNotNull(retrievedCostProjection, "CostProjection should be retrieved by ID");
        assertEquals("Bike", retrievedCostProjection.getTransportationMode(), "Transportation mode should match");
    }

    @Test
    public void testUpdate() {
        // Insert a user and cost projection
        User user = new User("John", "Smith", "john.smith@example.com", "password123");
        int userId = new GenericDao<>(User.class).insert(user);

        CostProjection costProjection = new CostProjection();
        costProjection.setUser(user);
        costProjection.setTransportationMode("Car");
        costProjection.setDurationInMinutes(30);
        costProjection.setDistanceInMiles(10.0);
        costProjection.setDateAdded(new java.util.Date());
        int logId = costProjectionDao.insert(costProjection);

        // Update the CostProjection
        costProjection.setTransportationMode("Bus");
        costProjection.setDurationInMinutes(60);
        costProjectionDao.update(costProjection);

        // Fetch the updated CostProjection
        CostProjection updatedCostProjection = costProjectionDao.getById(logId);
        assertEquals("Bus", updatedCostProjection.getTransportationMode(), "Updated transportation mode should match");
        assertEquals(60, updatedCostProjection.getDurationInMinutes(), "Updated duration should match");
    }

    @Test
    public void testDelete() {
        // Insert a user and cost projection
        User user = new User("Jo", "Do", "jo.d@example.com", "password123");
        int userId = new GenericDao<>(User.class).insert(user);

        CostProjection costProjection = new CostProjection();
        costProjection.setUser(user);
        costProjection.setTransportationMode("Walk");
        costProjection.setDurationInMinutes(10);
        costProjection.setDistanceInMiles(2.0);
        costProjection.setDateAdded(new java.util.Date());
        int logId = costProjectionDao.insert(costProjection);

        // Delete the CostProjection
        costProjectionDao.deleteEntity(costProjection);

        // Try to retrieve the deleted CostProjection
        CostProjection deletedCostProjection = costProjectionDao.getById(logId);
        assertNull(deletedCostProjection, "CostProjection should be null after deletion");
    }

    @Test
    public void testGetAll() {
        // Insert a couple of users and cost projections
        User user1 = new User("Alice", "Wonderland", "alice@example.com", "password123");
        int userId1 = new GenericDao<>(User.class).insert(user1);

        User user2 = new User("Bob", "Builder", "bob@example.com", "password123");
        int userId2 = new GenericDao<>(User.class).insert(user2);

        CostProjection costProjection1 = new CostProjection();
        costProjection1.setUser(user1);
        costProjection1.setTransportationMode("Car");
        costProjection1.setDurationInMinutes(30);
        costProjection1.setDistanceInMiles(10.0);
        costProjection1.setDateAdded(new java.util.Date());
        costProjectionDao.insert(costProjection1);

        CostProjection costProjection2 = new CostProjection();
        costProjection2.setUser(user2);
        costProjection2.setTransportationMode("Bike");
        costProjection2.setDurationInMinutes(45);
        costProjection2.setDistanceInMiles(15.5);
        costProjection2.setDateAdded(new java.util.Date());
        costProjectionDao.insert(costProjection2);

        List<CostProjection> costProjections = costProjectionDao.getAll();
        assertTrue(costProjections.size() >= 2, "There should be at least 2 cost projections in the database");
    }
}
