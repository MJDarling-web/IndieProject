package persistence;

import entity.TransportationProfile;
import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;

public class TransportationProfileDaoTest {

    private GenericDao<TransportationProfile> profileDao;
    private GenericDao<User> userDao;

    @BeforeEach
    void setUp() {
        Database database = new Database();
        SessionFactoryProvider.createSessionFactory();
        database.runSQL("cleandb.sql");

        profileDao = new GenericDao<>(TransportationProfile.class);
        userDao = new GenericDao<>(User.class);

        profileDao.getAll().forEach(profileDao::deleteEntity);
    }

    @Test
    public void testCreate() {
        User user = new User("Mark", "Lee", "mark" + System.currentTimeMillis() + "@example.com", "password123");
        userDao.insert(user);

        TransportationProfile profile = new TransportationProfile();
        profile.setUser(user);
        profile.setVehicleType("Sedan");
        profile.setMilesPerGallon(30.0);
        profile.setMonthlyPayment(300.0);
        profile.setInsuranceCost(100.0);
        profile.setMaintenanceCost(50.0);
        profile.setFuelCostPerGallon(3.8);
        profile.setLastUpdated(new Date());

        int profileId = profileDao.insert(profile);
        assertNotEquals(0, profileId);
    }

    @Test
    public void testGetById() {
        User user = new User("Samantha", "Carter", "samantha" + System.currentTimeMillis() + "@example.com", "password123");
        userDao.insert(user);

        TransportationProfile profile = new TransportationProfile();
        profile.setUser(user);
        profile.setVehicleType("SUV");

        int profileId = profileDao.insert(profile);
        TransportationProfile retrieved = profileDao.getById(profileId);

        assertNotNull(retrieved);
        assertEquals("SUV", retrieved.getVehicleType());
    }

    @Test
    public void testUpdate() {
        User user = new User("Tom", "Harris", "tom" + System.currentTimeMillis() + "@example.com", "password123");
        userDao.insert(user);

        TransportationProfile profile = new TransportationProfile();
        profile.setUser(user);
        profile.setVehicleType("Truck");

        int profileId = profileDao.insert(profile);

        profile.setVehicleType("Convertible");
        profileDao.update(profile);

        TransportationProfile updated = profileDao.getById(profileId);
        assertEquals("Convertible", updated.getVehicleType());
    }

    @Test
    public void testDelete() {
        User user = new User("Natalie", "Jones", "natalie" + System.currentTimeMillis() + "@example.com", "password123");
        userDao.insert(user);

        TransportationProfile profile = new TransportationProfile();
        profile.setUser(user);
        profile.setVehicleType("Hybrid");

        int profileId = profileDao.insert(profile);

        profileDao.deleteEntity(profile);
        assertNull(profileDao.getById(profileId));
    }

    @Test
    public void testGetAll() {
        User user = new User("Olivia", "Stone", "olivia" + System.currentTimeMillis() + "@example.com", "password123");
        userDao.insert(user);

        TransportationProfile profile1 = new TransportationProfile();
        profile1.setUser(user);
        profile1.setVehicleType("Compact");
        profileDao.insert(profile1);

        TransportationProfile profile2 = new TransportationProfile();
        profile2.setUser(user);
        profile2.setVehicleType("Motorcycle");
        profileDao.insert(profile2);

        List<TransportationProfile> profiles = profileDao.getAll();
        assertTrue(profiles.size() >= 2);
    }
}
