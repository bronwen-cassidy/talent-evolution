package com.zynap.talentstudio.web.analysis.populations;

/**
 * User: amark
 * Date: 11-Aug-2006
 * Time: 16:06:50
 */

import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.PopulationCriteria;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;

import java.util.Collection;

public class TestCriteriaWrapperBean extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        criteria = new PopulationCriteria();
        criteriaWrapperBean = new CriteriaWrapperBean(criteria);
    }

    public void testGetComparators() throws Exception {
        final Collection comparators = criteriaWrapperBean.getComparators();
        assertTrue(comparators.isEmpty());
    }

    public void testGetBlogCommentComparators() throws Exception {
        final String[] expectedValues = new String[]{IPopulationEngine.LIKE, IPopulationEngine.ISNULL};
        assertComparatorsPresent(DynamicAttribute.DA_TYPE_BLOG_COMMENT, expectedValues);
    }

    public void testGetTextComparators() throws Exception {
        final String[] expectedValues = new String[]{IPopulationEngine.EQ, IPopulationEngine.LIKE, IPopulationEngine.ISNULL};
        assertComparatorsPresent(DynamicAttribute.DA_TYPE_TEXTAREA, expectedValues);
        assertComparatorsPresent(DynamicAttribute.DA_TYPE_TEXTFIELD, expectedValues);
    }

    public void testGetNumericComparators() throws Exception {
        final String[] expectedValues = new String[]{IPopulationEngine.EQ, IPopulationEngine.LT, IPopulationEngine.ISNULL, IPopulationEngine.GT};
        assertComparatorsPresent(DynamicAttribute.DA_TYPE_NUMBER, expectedValues);
        assertComparatorsPresent(DynamicAttribute.DA_TYPE_SUM, expectedValues);
    }

    public void testGetDateComparators() throws Exception {
        final String[] expectedValues = new String[]{IPopulationEngine.EQ, IPopulationEngine.LT, IPopulationEngine.ISNULL, IPopulationEngine.GT};
        assertComparatorsPresent(DynamicAttribute.DA_TYPE_DATE, expectedValues);
        assertComparatorsPresent(DynamicAttribute.DA_TYPE_TIMESTAMP, expectedValues);
        assertComparatorsPresent(DynamicAttribute.DA_TYPE_DATETIMESTAMP, expectedValues);
        assertComparatorsPresent(DynamicAttribute.DA_TYPE_LAST_UPDATED, expectedValues);
    }

    public void testGetImageComparators() throws Exception {
        final String[] expectedValues = new String[]{IPopulationEngine.ISNULL};
        assertComparatorsPresent(DynamicAttribute.DA_TYPE_IMAGE, expectedValues);
    }

    public void testGetNodeComparators() throws Exception {
        final String[] expectedValues = new String[]{IPopulationEngine.EQ, IPopulationEngine.ISNULL};
        assertComparatorsPresent(DynamicAttribute.DA_TYPE_POSITION, expectedValues);
        assertComparatorsPresent(DynamicAttribute.DA_TYPE_SUBJECT, expectedValues);
        assertComparatorsPresent(DynamicAttribute.DA_TYPE_OU, expectedValues);
    }

    public void testGetUserComparators() throws Exception {
        final String[] expectedValues = new String[]{IPopulationEngine.EQ, IPopulationEngine.ISNULL};
        assertComparatorsPresent(DynamicAttribute.DA_TYPE_LAST_UPDATED_BY, expectedValues);
    }

    public void testGetSelectionTypeComparators() throws Exception {
        final String[] expectedValues = new String[]{IPopulationEngine.EQ, IPopulationEngine.ISNULL};
        assertComparatorsPresent(DynamicAttribute.DA_TYPE_STRUCT, expectedValues);
        assertComparatorsPresent(DynamicAttribute.DA_TYPE_MULTISELECT, expectedValues);
        assertComparatorsPresent(DynamicAttribute.DA_TYPE_ENUM_MAPPING, expectedValues);        
    }

    public void testGetDerivedAttributeComparators() throws Exception {
        final String[] expectedValues = new String[]{IPopulationEngine.EQ, IPopulationEngine.LT, IPopulationEngine.ISNULL, IPopulationEngine.GT};
        assertComparatorsPresent(IDynamicAttributeService.PP_SUB_DERIVED_ATT_DEFINITION, expectedValues);
        assertComparatorsPresent(IDynamicAttributeService.PP_SUP_DERIVED_ATT_DEFINITION, expectedValues);
        assertComparatorsPresent(IDynamicAttributeService.SP_SUB_DERIVED_ATT_DEFINITION, expectedValues);
        assertComparatorsPresent(IDynamicAttributeService.SP_SUP_DERIVED_ATT_DEFINITION, expectedValues);
    }

    public void testSetComparator() throws Exception {

        final String comparator = IPopulationEngine.ISNULL;
        criteriaWrapperBean.setComparator(comparator);

        final PopulationCriteria modifiedCriteria = getModifiedCriteria();
        assertEquals(comparator, modifiedCriteria.getComparator());
    }

    public void testGetComparator() throws Exception {

        final String comparator = IPopulationEngine.ISNULL;
        criteriaWrapperBean.setComparator(comparator);

        assertEquals(comparator, criteriaWrapperBean.getComparator());
    }

    public void testUpdateRefValue() throws Exception {

        final String value = AnalysisAttributeHelper.ORG_UNIT_ID_ATTR;
        criteriaWrapperBean.setAttributeDefinition(new AttributeWrapperBean(new DynamicAttribute()));
        criteriaWrapperBean.getAttributeDefinition().setValue(value);
        criteriaWrapperBean.updateRefValue();

        final PopulationCriteria modifiedCriteria = getModifiedCriteria();
        assertEquals(value, modifiedCriteria.getRefValue());
    }

    public void testGetRefValue() throws Exception {

        final String refValue = "refvalue";
        criteria.setRefValue(refValue);

        assertEquals(refValue, criteriaWrapperBean.getRefValue());
    }

    public void testSetRefValue() throws Exception {

        final String refValue = "refvalue";
        criteria.setRefValue(refValue);

        final String newRefValue = "newRefValue";
        criteriaWrapperBean.setRefValue(newRefValue);

        final PopulationCriteria modifiedCriteria = getModifiedCriteria();
        assertEquals(newRefValue, modifiedCriteria.getRefValue());
    }

    private void assertComparatorsPresent(final String type, final String[] expectedValues) {

        final DynamicAttribute dynamicAttribute = new DynamicAttribute("testDA", type);
        assertComparatorsPresent(dynamicAttribute, expectedValues);
    }

    private void assertComparatorsPresent(final DynamicAttribute dynamicAttribute, final String[] expectedValues) {

        criteriaWrapperBean.setAttributeDefinition(new AttributeWrapperBean(dynamicAttribute));
        final Collection comparators = criteriaWrapperBean.getComparators();

        assertEquals(expectedValues.length, comparators.size());
        for (int i = 0; i < expectedValues.length; i++) {
            String expectedValue = expectedValues[i];
            assertTrue("Expected value not found: " + expectedValue, comparators.contains(expectedValue));
        }
    }

    private PopulationCriteria getModifiedCriteria() {
        return (PopulationCriteria) criteriaWrapperBean.getAnalysisAttribute();
    }

    private CriteriaWrapperBean criteriaWrapperBean;
    private PopulationCriteria criteria;
}