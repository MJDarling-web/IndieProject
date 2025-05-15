package persistence;

import entity.TransportationProfile;
import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransportationProfileDaoTest {

    private GenericDao<TransportationProfile> profileDao;
    private GenericDao<User>                  userDao;

    @BeforeEach
    void setUp() {
        Database database = new Database();
        SessionFactoryProvider.createSessionFactory();
        database.runSQL("cleandb.sql");

        profileDao = new GenericDao<>(TransportationProfile.class);
        userDao    = new GenericDao<>(User.class);
    }

    @Test
    public void testGetAllUsesSeedData() {
        List<TransportationProfile> profiles = profileDao.getAll();
        assertEquals(2,
                profiles.size(),
                "cleandb.sql should seed exactly 2 transportation profiles");
    }

    @Test
    public void testGetByIdUsesSeedData() {
        TransportationProfile p1 = profileDao.getById(1);
        assertNotNull(p1, "Profile #1 should exist");
        assertEquals("Compact", p1.getVehicleType());
        assertEquals(1, p1.getUser().getId());
    }

    @Test
    public void testGetByIdNotFound() {
        assertNull(profileDao.getById(999),
                "Unknown ID should return null");
    }

    @Test
    public void testCreateProfile() {
        User u = new User("Mark", "Lee", "mark+" + System.currentTimeMillis() + "@example.com");
        userDao.insert(u);

        TransportationProfile newProfile = new TransportationProfile();
        newProfile.setUser(u);
        newProfile.setVehicleType("Sedan");
        newProfile.setMilesPerGallon(30.0);
        newProfile.setMonthlyPayment(300.0);
        newProfile.setInsuranceCost(100.0);
        newProfile.setMaintenanceCost(50.0);
        newProfile.setFuelCostPerGallon(3.8);
        newProfile.setLastUpdated(new Date());

        int newId = profileDao.insert(newProfile);
        assertNotEquals(0, newId);

        assertEquals(3,
                profileDao.getAll().size(),
                "After create, total profiles should go from 2 → 3");
    }

    @Test
    public void testUpdateProfile() {
        TransportationProfile p = profileDao.getById(1);
        p.setVehicleType("Convertible");
        profileDao.update(p);

        TransportationProfile updated = profileDao.getById(1);
        assertEquals("Convertible", updated.getVehicleType());
    }

    @Test
    public void testDeleteProfile() {
        TransportationProfile toDelete = profileDao.getById(2);
        profileDao.deleteEntity(toDelete);

        assertNull(profileDao.getById(2),
                "After delete, profile #2 should be gone");
        assertEquals(1,
                profileDao.getAll().size(),
                "After delete, total profiles should go from 2 → 1");
    }
}
