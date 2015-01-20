package com.zynap.talentstudio.web.analysis;

/**
 * User: amark
 * Date: 04-Apr-2006
 * Time: 11:45:11
 */

import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.populations.PopulationCriteria;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.web.analysis.picker.AnalysisAttributeCollection;
import com.zynap.talentstudio.web.analysis.picker.PopulationCriteriaBuilder;
import com.zynap.talentstudio.web.analysis.populations.CriteriaWrapperBean;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;

public class TestAnalysisAttributeWrapperBean extends ZynapMockControllerTest {

    protected void setUp() throws Exception {
        super.setUp();

        analysisAttributeWrapperBean = new CriteriaWrapperBean(new PopulationCriteria());
        PopulationCriteriaBuilder populationCriteriaBuilder = (PopulationCriteriaBuilder) getBean("populationCriteriaBuilder");
        analysisAttributeCollection = populationCriteriaBuilder.buildCollection();
    }

    public void testIsInvalid() throws Exception {

        // test with no attribute - this is not invalid
        analysisAttributeWrapperBean.setAttributeDefinition(null);
        assertFalse(analysisAttributeWrapperBean.isInvalid());

        // test with non-existent attribute
        setAttribute(analysisAttributeWrapperBean, "foo.bar");
        assertTrue(analysisAttributeWrapperBean.isInvalid());
        assertEquals(IDynamicAttributeService.INVALID_ATT, analysisAttributeWrapperBean.getAttributeDefinition().getAttributeDefinition());

        // test with valid attribute
        setAttribute(analysisAttributeWrapperBean, AnalysisAttributeHelper.POSITION_TITLE_ATTR);
        assertFalse(analysisAttributeWrapperBean.isInvalid());
    }

    public void testIsAttributeSet() throws Exception {

        // test with null
        assertFalse(analysisAttributeWrapperBean.isAttributeSet());

        // test with attribute
        setAttribute(analysisAttributeWrapperBean, AnalysisAttributeHelper.POSITION_TITLE_ATTR);
        assertTrue(analysisAttributeWrapperBean.isAttributeSet());
    }

    public void testIsNode() throws Exception {
        assertFalse(analysisAttributeWrapperBean.isNode());

        setAttribute(analysisAttributeWrapperBean, AnalysisAttributeHelper.ORG_UNIT_ID_ATTR);
        assertTrue(analysisAttributeWrapperBean.isNode());

        analysisAttributeWrapperBean.setAttributeDefinition(new AttributeWrapperBean(new DynamicAttribute(null, DynamicAttribute.DA_TYPE_POSITION)));
        assertTrue(analysisAttributeWrapperBean.isNode());

        analysisAttributeWrapperBean.setAttributeDefinition(new AttributeWrapperBean(new DynamicAttribute(null, DynamicAttribute.DA_TYPE_SUBJECT)));
        assertTrue(analysisAttributeWrapperBean.isNode());
    }

    public void testIsPosition() throws Exception {
        assertFalse(analysisAttributeWrapperBean.isPosition());

        analysisAttributeWrapperBean.setAttributeDefinition(new AttributeWrapperBean(new DynamicAttribute(null, DynamicAttribute.DA_TYPE_POSITION)));
        assertTrue(analysisAttributeWrapperBean.isPosition());
    }

    public void testHasIncorrectAttributeDefinition() throws Exception {

        assertTrue(analysisAttributeWrapperBean.hasIncorrectAttributeDefinition());

        setAttribute(analysisAttributeWrapperBean, AnalysisAttributeHelper.POSITION_TITLE_ATTR);
        analysisAttributeWrapperBean.setAttribute("foo.bar");
        assertTrue(analysisAttributeWrapperBean.hasIncorrectAttributeDefinition());

        setAttribute(analysisAttributeWrapperBean, AnalysisAttributeHelper.ORG_UNIT_ID_ATTR);
        assertFalse(analysisAttributeWrapperBean.hasIncorrectAttributeDefinition());
    }

    public void testIsQuestion() throws Exception {
        assertFalse(analysisAttributeWrapperBean.isQuestion());

        setAttribute(analysisAttributeWrapperBean, "1_2");
        assertTrue(analysisAttributeWrapperBean.isQuestion());
    }

    public void testHasAttributeName() throws Exception {
        assertFalse(analysisAttributeWrapperBean.hasAttributeName());

        setAttribute(analysisAttributeWrapperBean, AnalysisAttributeHelper.ORG_UNIT_ID_ATTR);
        assertTrue(analysisAttributeWrapperBean.hasAttributeName());
    }

    public void testIsOrganisationUnit() throws Exception {
        assertFalse(analysisAttributeWrapperBean.isOrganisationUnit());

        setAttribute(analysisAttributeWrapperBean, AnalysisAttributeHelper.ORG_UNIT_ID_ATTR);
        assertTrue(analysisAttributeWrapperBean.isOrganisationUnit());
    }

    public void testIsDerivedAttribute() throws Exception {
        assertFalse(analysisAttributeWrapperBean.isDerivedAttribute());

        analysisAttributeWrapperBean.setAttributeDefinition(new AttributeWrapperBean(IDynamicAttributeService.PP_SUB_DERIVED_ATT_DEFINITION));
        assertTrue(analysisAttributeWrapperBean.isDerivedAttribute());
    }

    public void testIsSubject() throws Exception {
        assertFalse(analysisAttributeWrapperBean.isSubject());

        analysisAttributeWrapperBean.setAttributeDefinition(new AttributeWrapperBean(new DynamicAttribute(null, DynamicAttribute.DA_TYPE_SUBJECT)));
        assertTrue(analysisAttributeWrapperBean.isSubject());
    }

    public void testGetDynamicAttributeId() throws Exception {
        assertNull(analysisAttributeWrapperBean.getDynamicAttributeId());

        final String attributeId = "38";
        setAttribute(analysisAttributeWrapperBean, attributeId);
        assertEquals(attributeId, analysisAttributeWrapperBean.getDynamicAttributeId().toString());
    }

    public void testGetAttribute() throws Exception {
        assertNull(analysisAttributeWrapperBean.getAttribute());

        setAttribute(analysisAttributeWrapperBean, AnalysisAttributeHelper.ORG_UNIT_ID_ATTR);
        assertNotNull(analysisAttributeWrapperBean.getAttribute());
        assertEquals(AnalysisAttributeHelper.ORG_UNIT_ID_ATTR, analysisAttributeWrapperBean.getAttribute());
    }

    public void testGetQuestionAttribute() throws Exception {
        assertNull(analysisAttributeWrapperBean.getQuestionAttribute());

        setAttribute(analysisAttributeWrapperBean, "1_2_peer");
        assertNotNull(analysisAttributeWrapperBean.getQuestionAttribute());

        setAttribute(analysisAttributeWrapperBean, AnalysisAttributeHelper.POSITION_TITLE_ATTR);
        assertNull(analysisAttributeWrapperBean.getQuestionAttribute());
    }

    private void setAttribute(AnalysisAttributeWrapperBean analysisAttributeWrapperBean, String value) {
        analysisAttributeWrapperBean.setAttribute(value);
        analysisAttributeCollection.setAttributeDefinition(analysisAttributeWrapperBean);
    }

    AnalysisAttributeWrapperBean analysisAttributeWrapperBean;
    private AnalysisAttributeCollection analysisAttributeCollection;
}