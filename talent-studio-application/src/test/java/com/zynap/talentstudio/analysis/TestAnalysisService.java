package com.zynap.talentstudio.analysis;

/**
 * User: amark
 * Date: 06-Feb-2006
 * Time: 13:24:50
 */

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.populations.PopulationCriteria;
import com.zynap.talentstudio.analysis.populations.PopulationDto;
import com.zynap.talentstudio.common.AccessType;

import java.util.Collection;

public class TestAnalysisService extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        analysisService = (IAnalysisService) getBean("analysisService");
    }

    /**
     * Test finding all public populations for positions.
     *
     * @throws Exception
     */
    public void testFindPopulationsPositionPublicOnly() throws Exception {

        final String scope = AccessType.PUBLIC_ACCESS.toString();
        final String type = IPopulationEngine.P_POS_TYPE_;

        // create population
        final Population newPopulation = createPopulation();
        newPopulation.setScope(scope);
        newPopulation.setType(type);
        analysisService.create(newPopulation, ROOT_USER_ID);
        PopulationDto expected = new PopulationDto(newPopulation.getId(), newPopulation.getLabel(), type, scope, "");
        final Collection<PopulationDto> populations = analysisService.findAll(type, ROOT_USER_ID, scope);

        // must be some populations returned
        assertFalse(populations.isEmpty());

        // new population must be present
        assertTrue(populations.contains(expected));

        // check scope and type of populations returned
        for (PopulationDto population : populations) {
            assertEquals(type, population.getType());
            assertEquals(scope, population.getScope());
        }
    }

    /**
     * Test finding all public populations regardless of type.
     *
     * @throws Exception
     */
    public void testFindPopulationsPublicOnly() throws Exception {

        final String scope = AccessType.PUBLIC_ACCESS.toString();

        // create new position population
        final Population newPositionPopulation = createPopulation();
        newPositionPopulation.setScope(scope);
        newPositionPopulation.setType(IPopulationEngine.P_POS_TYPE_);
        analysisService.create(newPositionPopulation, ROOT_USER_ID);

        // create new person population
        final Population newPersonPopulation = createPopulation();
        newPersonPopulation.setScope(scope);
        newPersonPopulation.setType(IPopulationEngine.P_POS_TYPE_);
        analysisService.create(newPersonPopulation, ROOT_USER_ID);

        PopulationDto expectedPosition = new PopulationDto(newPositionPopulation.getId(), newPositionPopulation.getLabel(), newPositionPopulation.getType(), scope, "");
        PopulationDto expectedPerson = new PopulationDto(newPersonPopulation.getId(), newPersonPopulation.getLabel(), newPersonPopulation.getType(), scope, "");

        final Collection<PopulationDto> populations = analysisService.findAll(null, ROOT_USER_ID, scope);

        // must be some populations returned
        assertFalse(populations.isEmpty());

        // new populations must be present
        assertTrue(populations.contains(expectedPosition));
        assertTrue(populations.contains(expectedPerson));

        // check scope of populations returned only
        for (PopulationDto population : populations) {
            assertEquals(scope, population.getScope());
        }
    }

    /**
     * Test finding all populations for positions (public and any private).
     *
     * @throws Exception
     */
    public void testFindPopulationsPosition() throws Exception {

        final String scope = AccessType.PRIVATE_ACCESS.toString();
        final String type = IPopulationEngine.P_POS_TYPE_;

        // create population
        final Population newPopulation = createPopulation();
        newPopulation.setScope(scope);
        newPopulation.setType(type);
        analysisService.create(newPopulation, ROOT_USER_ID);

        PopulationDto expected = new PopulationDto(newPopulation.getId(), newPopulation.getLabel(), type, scope, "");
        // if you do the find as the user who created the population you will get it back even though it is private
        final Collection<PopulationDto> populations = analysisService.findAll(type, ROOT_USER_ID, scope);

        // must be some populations returned
        assertFalse(populations.isEmpty());

        // new population must be present
        assertTrue(populations.contains(expected));

        // check type of populations returned only
        for (PopulationDto population : populations) {
            assertEquals(type, population.getType());
        }

        // if you do the find as a different user you will not be returned the new population
        final Collection<PopulationDto> found = analysisService.findAll(type, new Long(-9999), scope);
        assertFalse(found.contains(expected));
    }

    /**
     * Test finding all populations regardless of type and scope (public and any private).
     *
     * @throws Exception
     */
    public void testFindPopulations() throws Exception {

        final String scope = AccessType.PRIVATE_ACCESS.toString();

        // create private position population
        final Population newPositionPopulation = createPopulation();
        newPositionPopulation.setScope(scope);
        newPositionPopulation.setType(IPopulationEngine.P_POS_TYPE_);
        analysisService.create(newPositionPopulation, ROOT_USER_ID);

        // create public subject population
        final Population newPersonPopulation = createPopulation();
        newPersonPopulation.setScope(AccessType.PUBLIC_ACCESS.toString());
        newPersonPopulation.setType(IPopulationEngine.P_POS_TYPE_);
        analysisService.create(newPersonPopulation, ROOT_USER_ID);

        PopulationDto positionExpected = new PopulationDto(newPositionPopulation.getId(), newPositionPopulation.getLabel(), newPositionPopulation.getType(), scope, "");
        PopulationDto personExpected = new PopulationDto(newPersonPopulation.getId(), newPersonPopulation.getLabel(), newPersonPopulation.getType(), scope, "");

        // if you do the find as the user who created the population you will get it back even though it is private
        final Collection<PopulationDto> populations = analysisService.findAll(null, ROOT_USER_ID, scope);

        // must be some populations returned
        assertFalse(populations.isEmpty());

        // both new populations must be present
        assertTrue(populations.contains(positionExpected));
        assertTrue(populations.contains(personExpected));

        // if you do the find as a different user you will not be returned the new private population but you will get the new public one
        final Collection<PopulationDto> found = analysisService.findAll(null, new Long(-9999), scope);
        assertFalse(found.contains(positionExpected));
        assertTrue(populations.contains(personExpected));
    }

    public void testCreate() throws Exception {
        Population population = createPopulation();
        analysisService.create(population, ROOT_USER_ID);

        assertNotNull(population.getId());
    }

    public void testUpdatePopulation() throws Exception {
        Population population = createPopulation();
        analysisService.create(population, ROOT_USER_ID);

        // change label
        population.setLabel("New label");

        analysisService.update(population);
        Population confirm = (Population) analysisService.findById(population.getId());
        assertEquals(confirm.getLabel(), population.getLabel());
    }

    public void testDeletePopulation() throws Exception {
        Population population = createPopulation();
        analysisService.create(population, ROOT_USER_ID);

        analysisService.delete(population);

        try {
            analysisService.findById(population.getId());
            fail("Deleted population should not be found");
        } catch (TalentStudioException expected) {
        }
    }

    public void testPopulationInPublicReport() throws Exception {

        Population population = createPopulation();
        analysisService.create(population, ROOT_USER_ID);

        // should be false
        assertFalse(analysisService.populationInPublicReport(population.getId()));
    }

    public static Population createPopulation() {
        Population p = new Population();
        p.setLabel("Population Test");
        p.setType(IPopulationEngine.P_POS_TYPE_);
        p.setUserId(ROOT_USER_ID);
        p.setScope(AccessType.PRIVATE_ACCESS.toString());
        PopulationCriteria c = new PopulationCriteria();
        c.setAttributeName("title");
        c.setComparator(IPopulationEngine.LIKE);
        c.setRefValue("Child");
        p.addPopulationCriteria(c);
        return p;
    }

    public static Population createPopulationAllPositions() {
        Population p = new Population();
        p.setLabel("Population Test");
        p.setType(IPopulationEngine.P_POS_TYPE_);
        p.setUserId(ROOT_USER_ID);
        p.setScope(AccessType.PRIVATE_ACCESS.toString());
        return p;
    }

    public static Population createPopulationAllSubjects() {
        Population p = new Population();
        p.setLabel("Subject Test");
        p.setType(IPopulationEngine.P_SUB_TYPE_);
        p.setUserId(ROOT_USER_ID);
        p.setScope(AccessType.PRIVATE_ACCESS.toString());
        return p;
    }

    private IAnalysisService analysisService;
}