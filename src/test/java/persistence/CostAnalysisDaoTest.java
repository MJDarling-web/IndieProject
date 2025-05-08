package persistence;

import entity.CostAnalysis;
import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class CostAnalysisDaoTest {

    private GenericDao<CostAnalysis> costAnalysisDao;
    private GenericDao<User> userDao;

    @BeforeEach
    void setUp() {
        Database database = new Database();
        SessionFactoryProvider.createSessionFactory();
        database.runSQL("cleandb.sql");

        costAnalysisDao = new GenericDao<>(CostAnalysis.class);
        userDao = new GenericDao<>(User.class);

        costAnalysisDao.getAll().forEach(costAnalysisDao::deleteEntity);
    }

    @Test
    public void testCreate() {
        User user = new User("Anna", "Taylor", "anna" + System.currentTimeMillis() + "@example.com", "password123");
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
        User user = new User("Ben", "White", "ben" + System.currentTimeMillis() + "@example.com", "password123");
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
        User user = new User("Chris", "Green", "chris" + System.currentTimeMillis() + "@example.com", "password123");
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
        User user = new User("Dana", "Brown", "dana" + System.currentTimeMillis() + "@example.com", "password123");
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
        User user = new User("Ella", "Brown", "ella" + System.currentTimeMillis() + "@example.com", "password123");
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
}
