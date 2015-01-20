package com.zynap.talentstudio.web.analysis.populations;

/**
 * User: amark
 * Date: 14-Aug-2006
 * Time: 16:38:50
 */

import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.populations.PopulationCriteria;
import com.zynap.talentstudio.web.analysis.picker.AnalysisAttributeCollection;
import com.zynap.talentstudio.web.analysis.picker.PopulationCriteriaBuilder;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;

import java.util.List;

public class TestPopulationWrapperBean extends ZynapMockControllerTest {

    protected void setUp() throws Exception {
        super.setUp();

        populationEngine = (IPopulationEngine) getBean("populationEngine");
        populationCriteriaBuilder = (PopulationCriteriaBuilder) getBean("populationCriteriaBuilder");
    }

    public void testPopulationWrapperBean() throws Exception {

        final Population population = populationEngine.getAllSubjectsPopulation();
        populationWrapperBean = new PopulationWrapperBean(population);

        assertTrue(populationWrapperBean.isScopeChangeable());

        assertEquals(0, populationWrapperBean.getTarget());
        assertEquals(population.getType(), populationWrapperBean.getType());

        assertNull(populationWrapperBean.getIndex());
        assertNull(populationWrapperBean.getResultsetPreview());
        assertNull(populationWrapperBean.getPopulationCriterias());
        assertNull(populationWrapperBean.getAttributeCollection());
    }

    public void testPopulationWrapperBeanWithCriteria() throws Exception {

        final Population population = populationEngine.getAllSubjectsPopulation();
        final AnalysisAttributeCollection analysisAttributeCollection = populationCriteriaBuilder.buildCollection();

        // todo test with selection type, number, date time, time, derived attribute and brackets
        PopulationCriteria criteria1 = buildCriteria(AnalysisAttributeHelper.FIRST_NAME_ATTR, "fred");
        population.addPopulationCriteria(criteria1);

        PopulationCriteria criteria2 = buildCriteria(AnalysisAttributeHelper.DOB_ATTR, "2006-08-31");
        population.addPopulationCriteria(criteria2);

        PopulationCriteria criteria3 = buildCriteria(AnalysisAttributeHelper.ORG_UNIT_ID_ATTR, DEFAULT_ORG_UNIT_ID.toString());
        population.addPopulationCriteria(criteria3);

        PopulationCriteria invalidCriteria = buildCriteria("foo.bar", "fred");
        population.addPopulationCriteria(invalidCriteria);

        populationWrapperBean = new PopulationWrapperBean(population, analysisAttributeCollection);

        List populationCriterias = populationWrapperBean.getPopulationCriterias();
        assertNotNull(populationCriterias);

        final String orgUnitLabel = DEFAULT_ORG_UNIT.getLabel();
        CriteriaWrapperBean orgUnitCriteriaWrapper = (CriteriaWrapperBean) populationCriterias.get(2);
        orgUnitCriteriaWrapper.setNodeLabel(orgUnitLabel);

        assertEquals(population.getPopulationCriterias().size(), populationCriterias.size());

        assertTrue(((CriteriaWrapperBean) populationCriterias.get(3)).isInvalid());

        final CriteriaWrapperBean firstNameCriteriaWrapper = (CriteriaWrapperBean) populationCriterias.get(0);
        compareCriteria(criteria1, firstNameCriteriaWrapper, criteria1.getRefValue());

        final CriteriaWrapperBean dobCriteriaWrapper = (CriteriaWrapperBean) populationCriterias.get(1);
        assertNotNull(dobCriteriaWrapper.getAttributeDefinition().getDate());
        compareCriteria(criteria2, dobCriteriaWrapper, "31 Aug 2006");

        compareCriteria(criteria3, orgUnitCriteriaWrapper, orgUnitLabel);
    }

    public void testRemoveInvalidCriterias() throws Exception {

        final Population population = populationEngine.getAllSubjectsPopulation();
        final AnalysisAttributeCollection analysisAttributeCollection = populationCriteriaBuilder.buildCollection();

        PopulationCriteria criteria1 = buildCriteria(AnalysisAttributeHelper.FIRST_NAME_ATTR, "fred");
        population.addPopulationCriteria(criteria1);

        PopulationCriteria criteria2 = buildCriteria(AnalysisAttributeHelper.DOB_ATTR, "2006-08-31");
        population.addPopulationCriteria(criteria2);

        PopulationCriteria criteria3 = buildCriteria(AnalysisAttributeHelper.ORG_UNIT_ID_ATTR, DEFAULT_ORG_UNIT_ID.toString());
        population.addPopulationCriteria(criteria3);

        PopulationCriteria invalidCriteria = buildCriteria("foo.bar", "fred");
        population.addPopulationCriteria(invalidCriteria);

        populationWrapperBean = new PopulationWrapperBean(population, analysisAttributeCollection);

        populationWrapperBean.removeInvalidCriterias();
        final Population modifiedPopulation = populationWrapperBean.getModifiedPopulation();
        assertEquals(3, modifiedPopulation.getPopulationCriterias().size());
    }

    public void testCheckCriteriaAttribute() throws Exception {

        final Population population = populationEngine.getAllPositionsPopulation();
        populationWrapperBean = new PopulationWrapperBean(population);

        final AnalysisAttributeCollection analysisAttributeCollection = populationCriteriaBuilder.buildCollection();
        populationWrapperBean.setAttributeCollection(analysisAttributeCollection);

        final PopulationCriteria criteria = buildCriteria(AnalysisAttributeHelper.FIRST_NAME_ATTR, "fred");
        final CriteriaWrapperBean criteriaWrapperBean = new CriteriaWrapperBean(criteria);
        criteriaWrapperBean.setNodeLabel("nodeLabel");
        populationWrapperBean.addPopulationCriteria(criteriaWrapperBean);

        assertFalse(criteriaWrapperBean.isInvalid());
        assertTrue(criteriaWrapperBean.hasIncorrectAttributeDefinition());
        assertNotNull(criteriaWrapperBean.getNodeLabel());

        // after calling check attribute the attribute definition will be set on the criteria wrapper bean and the node label set to null
        populationWrapperBean.checkCriteriaAttribute(new Long(0));
        assertFalse(criteriaWrapperBean.isInvalid());
        assertFalse(criteriaWrapperBean.hasIncorrectAttributeDefinition());
        assertNull(criteriaWrapperBean.getNodeLabel());
    }

    public void testCheckCriteriaAttributes() throws Exception {

        final Population population = populationEngine.getAllPositionsPopulation();
        populationWrapperBean = new PopulationWrapperBean(population);

        final AnalysisAttributeCollection analysisAttributeCollection = populationCriteriaBuilder.buildCollection();
        populationWrapperBean.setAttributeCollection(analysisAttributeCollection);

        final String value = "refValue";
        final PopulationCriteria criteria1 = buildCriteria(AnalysisAttributeHelper.FIRST_NAME_ATTR, value);
        final CriteriaWrapperBean criteriaWrapperBean = new CriteriaWrapperBean(criteria1);
        populationWrapperBean.addPopulationCriteria(criteriaWrapperBean);

        final PopulationCriteria criteria2 = buildCriteria(AnalysisAttributeHelper.TELEPHONE_ATTR, "07984 938 607");
        final CriteriaWrapperBean criteriaWrapperBean2 = new CriteriaWrapperBean(criteria2);
        populationWrapperBean.addPopulationCriteria(criteriaWrapperBean2);

        final PopulationCriteria criteria3 = buildCriteria(AnalysisAttributeHelper.ORG_UNIT_ID_ATTR, DEFAULT_ORG_UNIT_ID.toString());
        final CriteriaWrapperBean criteriaWrapperBean3 = new CriteriaWrapperBean(criteria3);
        populationWrapperBean.addPopulationCriteria(criteriaWrapperBean3);

        // set and verify initial state
        populationWrapperBean.checkCriteriaAttributes();
        assertNotNull(criteriaWrapperBean.getAttribute());
        assertNotNull(criteriaWrapperBean.getAttributeDefinition());

        assertNotNull(criteriaWrapperBean2.getAttribute());
        assertNotNull(criteriaWrapperBean2.getAttributeDefinition());

        assertNotNull(criteriaWrapperBean3.getAttribute());
        assertNotNull(criteriaWrapperBean3.getAttributeDefinition());

        // set ref value but clear attribute on criteria 1 - ref value, attribute, and attr definition will be cleared
        criteriaWrapperBean.setRefValue(value);
        criteriaWrapperBean.setAttribute(null);

        // change attribute on criteria 2 - ref value will be set to null but attribute def will be new one as will attribute
        final String newAttributeName = AnalysisAttributeHelper.EMAIL_ATTR;
        criteriaWrapperBean2.setAttribute(newAttributeName);

        // change attribute definition value for 3 - should be copied to ref value
        final String newValue = "new value";
        criteriaWrapperBean3.getAttributeDefinition().setValue(newValue);

        populationWrapperBean.checkCriteriaAttributes();
        assertNull(criteriaWrapperBean.getAttribute());
        assertNull(criteriaWrapperBean.getAttributeDefinition());
        assertNull(criteriaWrapperBean.getRefValue());

        assertEquals(newAttributeName, criteriaWrapperBean2.getAttribute());
        assertEquals(newAttributeName, criteriaWrapperBean2.getAttributeDefinition().getId());
        assertNull(criteriaWrapperBean2.getRefValue());

        assertNotNull(criteriaWrapperBean3.getAttribute());
        assertNotNull(criteriaWrapperBean3.getAttributeDefinition());
        assertEquals(newValue, criteriaWrapperBean3.getRefValue());
    }

    private void compareCriteria(PopulationCriteria criteria, Object obj, String expectedValue) {

        CriteriaWrapperBean criteriaWrapperBean = (CriteriaWrapperBean) obj;
        assertEquals(criteria.getId(), criteriaWrapperBean.getId());
        assertEquals(criteria.getOperator(), criteriaWrapperBean.getOperator());
        assertEquals(criteria.getComparator(), criteriaWrapperBean.getComparator());
        assertEquals(expectedValue, criteriaWrapperBean.getDisplayValue());
    }

    private PopulationCriteria buildCriteria(String attributeName, final String value) {

        PopulationCriteria newCriteria = new PopulationCriteria();
        newCriteria.setAttributeName(attributeName);
        newCriteria.setComparator(IPopulationEngine.EQ);
        newCriteria.setRefValue(value);
        newCriteria.setOperator(IPopulationEngine.AND);

        return newCriteria;
    }

    private PopulationWrapperBean populationWrapperBean;
    private PopulationCriteriaBuilder populationCriteriaBuilder;
    private IPopulationEngine populationEngine;
}