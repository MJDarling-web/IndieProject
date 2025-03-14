package persistence;

import entity.CostAnalysis;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class CostAnalysisDaoTest {

    private GenericDao<CostAnalysis> costAnalysisDao;

    @BeforeEach
    void setUp() {
        Database database = new Database();
        SessionFactoryProvider.createSessionFactory();
        database.runSQL("cleandb.sql");
        costAnalysisDao = new GenericDao<>(CostAnalysis.class);
    }

    @Test
    public void testCreate() {
        CostAnalysis costAnalysis = new CostAnalysis();
        int analysisId = costAnalysisDao.insert(costAnalysis);
        assertNotEquals(0, analysisId, "Analysis ID should not be zero after insertion");
    }

    @Test
    public void testGetById() {
        CostAnalysis costAnalysis = new CostAnalysis();
        costAnalysis.setCommuteType("Work");
        int analysisId = costAnalysisDao.insert(costAnalysis);
        CostAnalysis retrievedAnalysis = costAnalysisDao.getById(analysisId);
        assertNotNull(retrievedAnalysis, "Cost analysis should be retrieved by ID");
        assertEquals("Work", retrievedAnalysis.getCommuteType(), "Commute type should match");
    }

    @Test
    public void testUpdate() {
        CostAnalysis costAnalysis = new CostAnalysis();
        int analysisId = costAnalysisDao.insert(costAnalysis);

        // Update cost analysis
        costAnalysis.setCommuteType("Work");
        costAnalysisDao.update(costAnalysis);

        CostAnalysis updatedAnalysis = costAnalysisDao.getById(analysisId);
        assertEquals("Work", updatedAnalysis.getCommuteType(), "Updated commute type should match");
    }

    @Test
    public void testDelete() {
        CostAnalysis costAnalysis = new CostAnalysis();
        int analysisId = costAnalysisDao.insert(costAnalysis);

        // Delete cost analysis
        costAnalysisDao.deleteEntity(costAnalysis);
        CostAnalysis deletedAnalysis = costAnalysisDao.getById(analysisId);
        assertNull(deletedAnalysis, "Cost analysis should be null after deletion");
    }

    @Test
    public void testGetAll() {
        CostAnalysis analysis1 = new CostAnalysis();
        CostAnalysis analysis2 = new CostAnalysis();
        costAnalysisDao.insert(analysis1);
        costAnalysisDao.insert(analysis2);

        List<CostAnalysis> analyses = costAnalysisDao.getAll();
        assertTrue(analyses.size() >= 2, "There should be at least 2 cost analyses in the database");
    }
}
