/**package persistence;

import entity.CostAnalysis;
import entity.Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CostAnalysisDaoTest {

    private GenericDao<CostAnalysis> costAnalysisDao;
    private GenericDao<Profile> userDao;

    @BeforeEach
    void setUp() {
        Database database = new Database();
        SessionFactoryProvider.createSessionFactory();
        database.runSQL("cleandb.sql");

        costAnalysisDao = new GenericDao<>(CostAnalysis.class);
        userDao = new GenericDao<>(Profile.class);

        costAnalysisDao.getAll().forEach(costAnalysisDao::deleteEntity);
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
                "Anna",
                "Taylor",
                "anna" + System.currentTimeMillis() + "@example.com"
        );
        userDao.insert(user);

        CostAnalysis analysis = new CostAnalysis();
        analysis.setUser(user);
        analysis.setCommuteType("Car");
        analysis.setOneYearCost(1200.0);
        analysis.setTwoYearCost(2300.0);
        analysis.setFiveYearCost(5000.0);
        analysis.setTotalCost(8500.0);

        int analysisId = costAnalysisDao.insert(analysis);
        assertNotEquals(0, analysisId);
    }

    @Test
    public void testGetById() {
        Profile user = buildValidUser(
                "Ben",
                "White",
                "ben" + System.currentTimeMillis() + "@example.com"
        );
        userDao.insert(user);

        CostAnalysis analysis = new CostAnalysis();
        analysis.setUser(user);
        analysis.setCommuteType("Bike");
        analysis.setOneYearCost(100.0);
        analysis.setTwoYearCost(180.0);
        analysis.setFiveYearCost(400.0);
        analysis.setTotalCost(680.0);

        int analysisId = costAnalysisDao.insert(analysis);
        CostAnalysis retrieved = costAnalysisDao.getById(analysisId);

        assertNotNull(retrieved);
        assertEquals("Bike", retrieved.getCommuteType());
    }

    @Test
    public void testUpdate() {
        Profile user = buildValidUser(
                "Chris",
                "Green",
                "chris" + System.currentTimeMillis() + "@example.com"
        );
        userDao.insert(user);

        CostAnalysis analysis = new CostAnalysis();
        analysis.setUser(user);
        analysis.setCommuteType("Bus");
        analysis.setOneYearCost(800.0);
        analysis.setTwoYearCost(1500.0);
        analysis.setFiveYearCost(3000.0);
        analysis.setTotalCost(5300.0);

        int analysisId = costAnalysisDao.insert(analysis);

        analysis.setCommuteType("Electric Scooter");
        costAnalysisDao.update(analysis);

        CostAnalysis updated = costAnalysisDao.getById(analysisId);
        assertEquals("Electric Scooter", updated.getCommuteType());
    }

    @Test
    public void testDelete() {
        Profile user = buildValidUser(
                "Dana",
                "Brown",
                "dana" + System.currentTimeMillis() + "@example.com"
        );
        userDao.insert(user);

        CostAnalysis analysis = new CostAnalysis();
        analysis.setUser(user);
        analysis.setCommuteType("Walk");
        analysis.setOneYearCost(0.0);
        analysis.setTwoYearCost(0.0);
        analysis.setFiveYearCost(0.0);
        analysis.setTotalCost(0.0);

        int analysisId = costAnalysisDao.insert(analysis);

        costAnalysisDao.deleteEntity(analysis);
        assertNull(costAnalysisDao.getById(analysisId));
    }

    @Test
    public void testGetAll() {
        Profile user = buildValidUser(
                "Ella",
                "Brown",
                "ella" + System.currentTimeMillis() + "@example.com"
        );
        userDao.insert(user);

        CostAnalysis analysis1 = new CostAnalysis();
        analysis1.setUser(user);
        analysis1.setCommuteType("Car");
        costAnalysisDao.insert(analysis1);

        CostAnalysis analysis2 = new CostAnalysis();
        analysis2.setUser(user);
        analysis2.setCommuteType("Bike");
        costAnalysisDao.insert(analysis2);

        List<CostAnalysis> analyses = costAnalysisDao.getAll();
        assertTrue(analyses.size() >= 2);
    }
}*/