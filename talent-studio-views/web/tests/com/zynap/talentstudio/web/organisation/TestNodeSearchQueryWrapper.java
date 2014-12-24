package com.zynap.talentstudio.web.organisation;

/**
 * User: amark
 * Date: 20-Apr-2005
 * Time: 08:33:01
 */

import junit.framework.TestCase;

import com.zynap.domain.QueryParameter;
import com.zynap.domain.orgbuilder.ISearchConstants;
import com.zynap.domain.orgbuilder.PositionSearchQuery;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.DynamicAttributeDTO;

import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;
import com.zynap.talentstudio.web.organisation.positions.PositionSearchQueryWrapper;
import com.zynap.talentstudio.common.lookups.LookupType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Iterator;

public class TestNodeSearchQueryWrapper extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();

        nodeSearchQueryWrapper = new PositionSearchQueryWrapper(new PositionSearchQuery());
    }

    public void testGetNodeSearchQuery() throws Exception {

        assertNotNull(nodeSearchQueryWrapper.getWrappedDynamicAttributes());
        assertTrue(nodeSearchQueryWrapper.getWrappedDynamicAttributes().isEmpty());

        // add two attributes - only has 1 has a value
        Collection assignedAttributes = new ArrayList();

        final DynamicAttributeDTO attributeWrapperBean1 = new DynamicAttributeDTO(DynamicAttribute.DA_TYPE_STRUCT, "da1", new Long(1), "S", "", "", new LookupType());
        final String expectedValue = "1";
        attributeWrapperBean1.setValue(expectedValue);
        assignedAttributes.add(attributeWrapperBean1);

        final DynamicAttributeDTO attributeWrapperBean2 = new DynamicAttributeDTO(DynamicAttribute.DA_TYPE_STRUCT, "da2", new Long(1), "S", "", "", new LookupType());
        assignedAttributes.add(attributeWrapperBean2);

        // check that search query only has one attribute value to be used in the query
        nodeSearchQueryWrapper.setWrappedDynamicAttributes(assignedAttributes);
        final Collection attributesValues = nodeSearchQueryWrapper.getNodeSearchQuery().getAttributesValues();
        assertEquals(1, attributesValues.size());

        // check fields set correctly
        for (Iterator iterator = attributesValues.iterator(); iterator.hasNext();) {
            AttributeValue attributeValue = (AttributeValue) iterator.next();
            assertEquals(attributeWrapperBean1.getId(), attributeValue.getDynamicAttribute().getId());
            assertEquals(expectedValue, attributeValue.getValue());
        }

        // set org unit id to null
        nodeSearchQueryWrapper.setOrgUnitId(null);

        // set label to blank
        nodeSearchQueryWrapper.setOuLabel("");

        // set title
        final String expectedTitle = "title";
        nodeSearchQueryWrapper.setTitle(expectedTitle);

        // check that only the title parameter is present
        Map queryParameters = nodeSearchQueryWrapper.getNodeSearchQuery().getMappedResults();
        assertEquals(1, queryParameters.size());
        final QueryParameter titleParam = (QueryParameter) queryParameters.get(ISearchConstants.TITLE);
        assertNotNull(titleParam);
        assertEquals(expectedTitle, titleParam.getValue());

        //set org unit id and check that it is now in the list of parameters
        nodeSearchQueryWrapper.setOrgUnitId(new Long(0));
        queryParameters = nodeSearchQueryWrapper.getNodeSearchQuery().getMappedResults();
        assertEquals(2, queryParameters.size());
        
        final QueryParameter orgUnitParameter = (QueryParameter) queryParameters.get(ISearchConstants.ORG_ID);
        assertNotNull(orgUnitParameter);
    }

    PositionSearchQueryWrapper nodeSearchQueryWrapper;
}