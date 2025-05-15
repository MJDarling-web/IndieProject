package persistence;

import entity.CostAnalysis;
import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CostAnalysisDaoTest {

    private GenericDao<CostAnalysis> costAnalysisDao;
    private GenericDao<User>         userDao;

    @BeforeEach
    void setUp() {
        Database database = new Database();
        SessionFactoryProvider.createSessionFactory();
        database.runSQL("cleandb.sql");

        costAnalysisDao = new GenericDao<>(CostAnalysis.class);
        userDao         = new GenericDao<>(User.class);
    }

    @Test
    public void testGetAllUsesSeedData() {
        List<CostAnalysis> list = costAnalysisDao.getAll();
        assertEquals(3,
                list.size(),
                "cleandb.sql should seed exactly 3 cost analyses");
    }

    @Test
    public void testGetByIdUsesSeedData() {
        CostAnalysis ca = costAnalysisDao.getById(2);
        assertNotNull(ca, "Row #2 should exist");
        assertEquals("Bus", ca.getCommuteType());
        assertEquals(1, ca.getUser().getId());
    }

    @Test
    public void testGetByIdNotFound() {
        assertNull(costAnalysisDao.getById(999),
                "Nonexistent ID should return null");
    }

    @Test
    public void testCreateAnalysis() {
        User u = new User("Anna", "Taylor", "anna+" + System.currentTimeMillis() + "@example.com");
        userDao.insert(u);

        CostAnalysis newCa = new CostAnalysis();
        newCa.setUser(u);
        newCa.setCommuteType("Scooter");
        newCa.setOneYearCost(300.0);
        newCa.setTwoYearCost(550.0);
        newCa.setFiveYearCost(1200.0);
        newCa.setTotalCost(2050.0);

        int newId = costAnalysisDao.insert(newCa);
        assertNotEquals(0, newId);

        assertEquals(4, costAnalysisDao.getAll().size());
    }

    @Test
    public void testUpdateAnalysis() {
        CostAnalysis ca = costAnalysisDao.getById(1);
        ca.setCommuteType("Electric Car");
        costAnalysisDao.update(ca);

        CostAnalysis updated = costAnalysisDao.getById(1);
        assertEquals("Electric Car", updated.getCommuteType());
    }

    @Test
    public void testDeleteAnalysis() {
        CostAnalysis toDelete = costAnalysisDao.getById(3);
        costAnalysisDao.deleteEntity(toDelete);

        assertNull(costAnalysisDao.getById(3));
        assertEquals(2, costAnalysisDao.getAll().size());
    }
}
