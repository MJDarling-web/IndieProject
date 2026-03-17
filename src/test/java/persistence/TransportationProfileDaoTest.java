/**package persistence;

import entity.Profile;
import entity.TransportationProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TransportationProfileDaoTest {

    private GenericDao<TransportationProfile> profileDao;
    private GenericDao<Profile> userDao;

    @BeforeEach
    void setUp() {
        Database database = new Database();
        SessionFactoryProvider.createSessionFactory();
        database.runSQL("cleandb.sql");

        profileDao = new GenericDao<>(TransportationProfile.class);
        userDao = new GenericDao<>(Profile.class);
        profileDao.getAll().forEach(profileDao::deleteEntity);
    }

    private TransportationProfile buildValidProfile(Profile user, String vehicleType) {
        TransportationProfile profile = new TransportationProfile();
        profile.setUser(user);
        profile.setVehicleType(vehicleType);
        profile.setMilesPerGallon(25.0);
        profile.setMonthlyPayment(0.0);
        profile.setInsuranceCost(0.0);
        profile.setMaintenanceCost(0.0);
        profile.setFuelCostPerGallon(3.5);
        profile.setLastUpdated(new Date());
        return profile;
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
                "John",
                "Doe",
                "john" + System.currentTimeMillis() + "@example.com"
        );
        userDao.insert(user);

        TransportationProfile profile = buildValidProfile(user, "Sedan");
        int profileId = profileDao.insert(profile);

        assertNotEquals(0, profileId);
    }

    @Test
    public void testGetById() {
        Profile user = buildValidUser(
                "Samantha",
                "Carter",
                "samantha" + System.currentTimeMillis() + "@example.com"
        );
        userDao.insert(user);

        TransportationProfile profile = buildValidProfile(user, "SUV");
        int profileId = profileDao.insert(profile);

        TransportationProfile retrieved = profileDao.getById(profileId);
        assertNotNull(retrieved);
        assertEquals("SUV", retrieved.getVehicleType());
    }

    @Test
    public void testUpdate() {
        Profile user = buildValidUser(
                "Michael",
                "Knight",
                "michael" + System.currentTimeMillis() + "@example.com"
        );
        userDao.insert(user);

        TransportationProfile profile = buildValidProfile(user, "Car");
        int profileId = profileDao.insert(profile);

        profile.setVehicleType("Convertible");
        profileDao.update(profile);

        TransportationProfile updated = profileDao.getById(profileId);
        assertEquals("Convertible", updated.getVehicleType());
    }

    @Test
    public void testDelete() {
        Profile user = buildValidUser(
                "Sarah",
                "Connor",
                "sarah" + System.currentTimeMillis() + "@example.com"
        );
        userDao.insert(user);

        TransportationProfile profile = buildValidProfile(user, "Motorcycle");
        int profileId = profileDao.insert(profile);

        profileDao.deleteEntity(profile);
        assertNull(profileDao.getById(profileId));
    }

    @Test
    public void testGetAll() {
        Profile user = buildValidUser(
                "Bruce",
                "Wayne",
                "bruce" + System.currentTimeMillis() + "@example.com"
        );
        userDao.insert(user);

        TransportationProfile profile1 = buildValidProfile(user, "Truck");
        TransportationProfile profile2 = buildValidProfile(user, "Car");
        profileDao.insert(profile1);
        profileDao.insert(profile2);

        List<TransportationProfile> profiles = profileDao.getAll();
        assertTrue(profiles.size() >= 2);
    }
}*/