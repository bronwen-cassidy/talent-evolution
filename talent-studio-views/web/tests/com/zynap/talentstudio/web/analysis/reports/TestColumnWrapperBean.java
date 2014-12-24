package com.zynap.talentstudio.web.analysis.reports;

/**
 * User: amark
 * Date: 03-Nov-2006
 * Time: 10:10:13
 */

import com.zynap.talentstudio.ZynapTestCase;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;

public class TestColumnWrapperBean extends ZynapTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        columnWrapperBean = new ColumnWrapperBean(new Column());
        assertNull(columnWrapperBean.getFunctionWrapperBean());
        assertTrue(columnWrapperBean.getColumnDisplayImages().isEmpty());
    }

    public void testAssociatedArtefactColumnIsOrderable() throws Exception {

        columnWrapperBean.setAttributeDefinition(new AttributeWrapperBean(new DynamicAttribute()));
        columnWrapperBean.setAttribute(ASSOCIATED_ARTEFACT_ATTR);
        assertFalse(columnWrapperBean.isOrderable());
    }

    public void testGroupedColumnIsOrderable() throws Exception {

        columnWrapperBean.setAttribute(EXTENDED_ATTR.toString());

        final DynamicAttribute dynamicAttribute = new DynamicAttribute();
        dynamicAttribute.setId(EXTENDED_ATTR);
        columnWrapperBean.setAttributeDefinition(new AttributeWrapperBean(dynamicAttribute));

        columnWrapperBean.setGrouped(true);
        assertFalse(columnWrapperBean.isOrderable());
    }

    public void testMultipleAnswerColumnIsOrderable() throws Exception {

        columnWrapperBean.setAttribute(EXTENDED_ATTR.toString());

        final DynamicAttribute dynamicAttribute = new DynamicAttribute();
        dynamicAttribute.setId(EXTENDED_ATTR);
        columnWrapperBean.setAttributeDefinition(new AttributeWrapperBean(dynamicAttribute));

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_NUMBER);
        dynamicAttribute.setDynamic(true);
        assertFalse(columnWrapperBean.isOrderable());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_MULTISELECT);
        dynamicAttribute.setDynamic(false);
        assertFalse(columnWrapperBean.isOrderable());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_BLOG_COMMENT);
        dynamicAttribute.setDynamic(false);
        assertFalse(columnWrapperBean.isOrderable());
    }

    public void testAssociatedArtefactColumnIsSupportsGroups() throws Exception {

        columnWrapperBean.setAttributeDefinition(new AttributeWrapperBean(new DynamicAttribute()));
        columnWrapperBean.setAttribute(ASSOCIATED_ARTEFACT_ATTR);
        assertTrue(columnWrapperBean.isSupportsGroups());
    }

    public void testDynamicLineItemColumnIsSupportsGroups() throws Exception {

        columnWrapperBean.setAttribute(EXTENDED_ATTR.toString());

        final DynamicAttribute dynamicAttribute = new DynamicAttribute();
        dynamicAttribute.setId(EXTENDED_ATTR);
        columnWrapperBean.setAttributeDefinition(new AttributeWrapperBean(dynamicAttribute));

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_NUMBER);
        dynamicAttribute.setDynamic(true);
        assertFalse(columnWrapperBean.isSupportsGroups());
    }

    public void testIsSupportGroups() throws Exception {

        columnWrapperBean.setAttribute(EXTENDED_ATTR.toString());

        final DynamicAttribute dynamicAttribute = new DynamicAttribute();
        dynamicAttribute.setId(EXTENDED_ATTR);
        dynamicAttribute.setDynamic(false);
        columnWrapperBean.setAttributeDefinition(new AttributeWrapperBean(dynamicAttribute));

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_BLOG_COMMENT);
        assertFalse(columnWrapperBean.isSupportsGroups());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_DATE);
        assertFalse(columnWrapperBean.isSupportsGroups());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_DATETIMESTAMP);
        assertFalse(columnWrapperBean.isSupportsGroups());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_ENUM_MAPPING);
        assertTrue(columnWrapperBean.isSupportsGroups());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_HTMLLINK);
        assertFalse(columnWrapperBean.isSupportsGroups());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_IMAGE);
        assertFalse(columnWrapperBean.isSupportsGroups());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_LAST_UPDATED);
        assertFalse(columnWrapperBean.isSupportsGroups());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_LAST_UPDATED_BY);
        assertFalse(columnWrapperBean.isSupportsGroups());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_MULTISELECT);
        assertFalse(columnWrapperBean.isSupportsGroups());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_NUMBER);
        assertFalse(columnWrapperBean.isSupportsGroups());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_POSITION);
        assertFalse(columnWrapperBean.isSupportsGroups());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_STRUCT);
        assertTrue(columnWrapperBean.isSupportsGroups());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_SUBJECT);
        assertFalse(columnWrapperBean.isSupportsGroups());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_SUM);
        assertFalse(columnWrapperBean.isSupportsGroups());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_TEXTAREA);
        assertFalse(columnWrapperBean.isSupportsGroups());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_TEXTFIELD);
        assertFalse(columnWrapperBean.isSupportsGroups());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_TIMESTAMP);
        assertFalse(columnWrapperBean.isSupportsGroups());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_OU);
        AttributeWrapperBean attributeWrapperBean = new AttributeWrapperBean("test", AnalysisAttributeHelper.ORG_UNIT_LABEL_ATTR, dynamicAttribute);
        columnWrapperBean.setAttribute(AnalysisAttributeHelper.ORG_UNIT_LABEL_ATTR);
        columnWrapperBean.setAttributeDefinition(attributeWrapperBean);
        assertTrue(columnWrapperBean.isSupportsGroups());
    }

    public void testIsSupportColours() throws Exception {

        columnWrapperBean.setAttribute(EXTENDED_ATTR.toString());

        final DynamicAttribute dynamicAttribute = new DynamicAttribute();
        dynamicAttribute.setId(EXTENDED_ATTR);
        columnWrapperBean.setAttributeDefinition(new AttributeWrapperBean(dynamicAttribute));

        dynamicAttribute.setDynamic(false);

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_BLOG_COMMENT);
        assertFalse(columnWrapperBean.isSupportColours());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_DATE);
        assertFalse(columnWrapperBean.isSupportColours());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_DATETIMESTAMP);
        assertFalse(columnWrapperBean.isSupportColours());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_ENUM_MAPPING);
        assertTrue(columnWrapperBean.isSupportColours());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_HTMLLINK);
        assertFalse(columnWrapperBean.isSupportColours());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_IMAGE);
        assertFalse(columnWrapperBean.isSupportColours());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_LAST_UPDATED);
        assertFalse(columnWrapperBean.isSupportColours());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_LAST_UPDATED_BY);
        assertFalse(columnWrapperBean.isSupportColours());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_MULTISELECT);
        assertFalse(columnWrapperBean.isSupportColours());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_NUMBER);
        assertFalse(columnWrapperBean.isSupportColours());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_OU);
        assertFalse(columnWrapperBean.isSupportColours());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_POSITION);
        assertFalse(columnWrapperBean.isSupportColours());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_STRUCT);
        assertTrue(columnWrapperBean.isSupportColours());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_SUBJECT);
        assertFalse(columnWrapperBean.isSupportColours());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_SUM);
        assertFalse(columnWrapperBean.isSupportColours());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_TEXTAREA);
        assertFalse(columnWrapperBean.isSupportColours());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_TEXTFIELD);
        assertFalse(columnWrapperBean.isSupportColours());

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_TIMESTAMP);
        assertFalse(columnWrapperBean.isSupportColours());
    }

    public void testDynamicLineItemColumnIsSupportsColours() throws Exception {

        columnWrapperBean.setAttribute(EXTENDED_ATTR.toString());

        final DynamicAttribute dynamicAttribute = new DynamicAttribute();
        dynamicAttribute.setId(EXTENDED_ATTR);
        columnWrapperBean.setAttributeDefinition(new AttributeWrapperBean(dynamicAttribute));

        dynamicAttribute.setType(DynamicAttribute.DA_TYPE_STRUCT);
        dynamicAttribute.setDynamic(true);
        assertFalse(columnWrapperBean.isSupportColours());
    }

    public void testMetricColumnWrapperBean() throws Exception {
        final Column column = new Column();
        column.setFormula(true);

        columnWrapperBean = new ColumnWrapperBean(column);
        assertTrue(columnWrapperBean.getColumnDisplayImages().isEmpty());
    }

    public void testColumnWrapperBean() throws Exception {
        final Column column = new Column();
        column.setFormula(true);

        columnWrapperBean = new ColumnWrapperBean(column);
        assertTrue(columnWrapperBean.getColumnDisplayImages().isEmpty());
    }

    private ColumnWrapperBean columnWrapperBean;

    private static final String ASSOCIATED_ARTEFACT_ATTR = "subjectPrimaryAssociations.position.organisationUnit.id";
    private static final Long EXTENDED_ATTR = new Long(39);
}