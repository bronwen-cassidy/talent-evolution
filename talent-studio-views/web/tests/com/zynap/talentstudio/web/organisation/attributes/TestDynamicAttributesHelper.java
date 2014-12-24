package com.zynap.talentstudio.web.organisation.attributes;

/**
 * User: amark
 * Date: 23-Aug-2006
 * Time: 16:10:40
 */

import com.zynap.common.util.UploadedFile;
import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.common.lookups.LookupType;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.AttributeValueFile;
import com.zynap.talentstudio.organisation.attributes.AttributeValuesCollection;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttributeFile;
import com.zynap.talentstudio.organisation.attributes.DynamicAttributeDTO;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.organisation.positions.Position;

import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.organisation.NodeWrapperBean;
import com.zynap.talentstudio.web.organisation.positions.PositionWrapperBean;

import org.apache.commons.lang.ArrayUtils;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Collection;

public class TestDynamicAttributesHelper extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        positionService = (IPositionService) getBean("positionService");
        dynamicAttributeService = (IDynamicAttributeService) getBean("dynamicAttrService");

        lookupManager = (ILookupManager) applicationContext.getBean("lookupManager");
    }

    public void testAssignMultiSelectAndDynamicAttributeValuesToNode() throws Exception {

        final Position position = positionService.findByID(DEFAULT_POSITION_ID);

        final List dynamicAttributes = new ArrayList();
        final DynamicAttribute multiSelectDA = new DynamicAttribute(new Long(-2), "multiSelectDA", DynamicAttribute.DA_TYPE_MULTISELECT);
        final LookupType lookupType = lookupManager.findLookupType(ILookupManager.LOOKUP_TYPE_TITLE);
        multiSelectDA.setRefersToType(lookupType);
        dynamicAttributes.add(multiSelectDA);

        final List attributeWrapperBeans = DynamicAttributesHelper.getAttributeWrapperBeans(dynamicAttributes, position, dynamicAttributeService);
        final int expectedNumberOfDynamicAttributes = dynamicAttributes.size();
        assertEquals(expectedNumberOfDynamicAttributes, attributeWrapperBeans.size());

        final List activeLookupValues = new ArrayList(lookupType.getActiveLookupValues());
        final int max = activeLookupValues.size() - 1;
        String[] multiSelectValueAsArray = new String[max];
        for (int i = 0; i < max; i++) {
            LookupValue lookupValue = (LookupValue) activeLookupValues.get(i);
            multiSelectValueAsArray[i] = lookupValue.getId().toString();
        }

        final String multiSelectValue = StringUtils.arrayToCommaDelimitedString(multiSelectValueAsArray);
        final AttributeWrapperBean multiSelectWrapper = (AttributeWrapperBean) attributeWrapperBeans.get(0);
        multiSelectWrapper.setValue(multiSelectValue);

        // use helper to save values to node
        DynamicAttributesHelper.assignAttributeValuesToNode(attributeWrapperBeans, position);

        // check attributevalues
        AttributeValuesCollection dynamicAttributeValues = position.getDynamicAttributeValues();
        assertEquals(expectedNumberOfDynamicAttributes, dynamicAttributeValues.size());

        AttributeValue multiSelectAttributeValue = dynamicAttributeValues.get(multiSelectDA);
        assertAttributeValue(multiSelectAttributeValue, multiSelectValue, multiSelectDA, position);
        List nodeExtendedAttributes = multiSelectAttributeValue.getNodeExtendedAttributes();
        assertEquals(2, nodeExtendedAttributes.size());
        for (int i = 0; i < nodeExtendedAttributes.size(); i++) {
            final NodeExtendedAttribute nodeExtendedAttribute = (NodeExtendedAttribute) nodeExtendedAttributes.get(i);
            assertEquals(multiSelectValueAsArray[i], nodeExtendedAttribute.getValue());
        }

        // set new value
        final String newMultiSelectValue = ((LookupValue) activeLookupValues.get(0)).getId().toString();
        multiSelectWrapper.setValue(newMultiSelectValue);

        // use helper to save values to node
        DynamicAttributesHelper.assignAttributeValuesToNode(attributeWrapperBeans, position);

        // check attributevalues
        dynamicAttributeValues = position.getDynamicAttributeValues();
        assertEquals(expectedNumberOfDynamicAttributes, dynamicAttributeValues.size());

        multiSelectAttributeValue = dynamicAttributeValues.get(multiSelectDA);
        assertAttributeValue(multiSelectAttributeValue, newMultiSelectValue, multiSelectDA, position);
        nodeExtendedAttributes = multiSelectAttributeValue.getNodeExtendedAttributes();
        assertEquals(1, nodeExtendedAttributes.size());
        assertEquals(newMultiSelectValue, ((NodeExtendedAttribute) nodeExtendedAttributes.get(0)).getValue());
    }

    public void testAssignAttributeValuesToNode() throws Exception {

        final Position position = positionService.findByID(DEFAULT_POSITION_ID);

        final List dynamicAttributes = new ArrayList();
        final DynamicAttribute textDA = new DynamicAttribute(new Long(-1), "textDA", DynamicAttribute.DA_TYPE_TEXTFIELD);
        dynamicAttributes.add(textDA);

        final DynamicAttribute multiSelectDA = new DynamicAttribute(new Long(-2), "multiSelectDA", DynamicAttribute.DA_TYPE_MULTISELECT);
        final LookupType lookupType = lookupManager.findLookupType(ILookupManager.LOOKUP_TYPE_TITLE);
        multiSelectDA.setRefersToType(lookupType);
        dynamicAttributes.add(multiSelectDA);

        final DynamicAttribute dateDA = new DynamicAttribute(new Long(-3), "dateDA", DynamicAttribute.DA_TYPE_DATE);
        dynamicAttributes.add(dateDA);

        final DynamicAttribute imgDA = new DynamicAttribute(new Long(-4), "imgDA", DynamicAttribute.DA_TYPE_IMAGE);
        dynamicAttributes.add(imgDA);

        final List attributeWrapperBeans = DynamicAttributesHelper.getAttributeWrapperBeans(dynamicAttributes, position, dynamicAttributeService);
        final int expectedNumberOfDynamicAttributes = dynamicAttributes.size();
        assertEquals(expectedNumberOfDynamicAttributes, attributeWrapperBeans.size());

        // set values
        final AttributeWrapperBean textAttributeWrapper = (AttributeWrapperBean) attributeWrapperBeans.get(0);
        final AttributeWrapperBean multiSelectWrapper = (AttributeWrapperBean) attributeWrapperBeans.get(1);
        final AttributeWrapperBean dateAttributeWrapper = (AttributeWrapperBean) attributeWrapperBeans.get(2);
        final AttributeWrapperBean imgAttributeWrapper = (AttributeWrapperBean) attributeWrapperBeans.get(3);

        final String textValue = "hello";

        final List activeLookupValues = new ArrayList(lookupType.getActiveLookupValues());
        final int max = activeLookupValues.size() - 1;
        String[] multiSelectValueAsArray = new String[max];
        for (int i = 0; i < max; i++) {
            LookupValue lookupValue = (LookupValue) activeLookupValues.get(i);
            multiSelectValueAsArray[i] = lookupValue.getId().toString();
        }
        final int length = multiSelectValueAsArray.length;
        final String multiSelectValue = StringUtils.arrayToCommaDelimitedString(multiSelectValueAsArray);

        final String dateValue = "2006-08-31";

        multiSelectWrapper.setValue(multiSelectValue);
        textAttributeWrapper.setValue(textValue);
        dateAttributeWrapper.setDate(dateValue);

        final String fileName = "test.gif";
        final byte[] blobValue = "hello world".getBytes();
        UploadedFile file = new UploadedFile(fileName, new Long(blobValue.length), blobValue, "gif");
        imgAttributeWrapper.setFile(file);

        // use helper to save values to node
        DynamicAttributesHelper.assignAttributeValuesToNode(attributeWrapperBeans, position);

        // check attributevalues
        AttributeValuesCollection dynamicAttributeValues = position.getDynamicAttributeValues();
        assertEquals(expectedNumberOfDynamicAttributes, dynamicAttributeValues.size());

        AttributeValue textAttributeValue = dynamicAttributeValues.get(textDA);
        assertAttributeValue(textAttributeValue, textValue, textDA, position);

        AttributeValue multiSelectAttributeValue = dynamicAttributeValues.get(multiSelectDA);
        assertAttributeValue(multiSelectAttributeValue, multiSelectValue, multiSelectDA, position);

        AttributeValue dateAttributeValue = dynamicAttributeValues.get(dateDA);
        assertAttributeValue(dateAttributeValue, dateValue, dateDA, position);

        AttributeValue imgAttributeValue = dynamicAttributeValues.get(imgDA);
        assertAttributeValue(imgAttributeValue, fileName, blobValue, imgDA, position);

        // check nodeextendedattributes
        Set extendedAttributes = position.getExtendedAttributes();
        final int expectedSize = expectedNumberOfDynamicAttributes + length - 1;
        assertEquals(expectedSize, extendedAttributes.size());
        assertNodeExtendedAttributes(extendedAttributes, textValue, textDA, dateValue, dateDA, multiSelectValueAsArray, multiSelectDA, fileName, blobValue, imgDA, position);

        // clear value on text attribute
        textAttributeWrapper.clearValue();

        // use helper to save values to node
        DynamicAttributesHelper.assignAttributeValuesToNode(attributeWrapperBeans, position);

        // check nodeextendedattributes again
        extendedAttributes = position.getExtendedAttributes();
        assertEquals(expectedSize - 1, extendedAttributes.size());
        assertNodeExtendedAttributes(extendedAttributes, textValue, textDA, dateValue, dateDA, multiSelectValueAsArray, multiSelectDA, fileName, blobValue, imgDA, position);

        // check attributevalues again
        dynamicAttributeValues = position.getDynamicAttributeValues();
        assertEquals(expectedNumberOfDynamicAttributes - 1, dynamicAttributeValues.size());

        assertNull(dynamicAttributeValues.get(textDA));

        multiSelectAttributeValue = dynamicAttributeValues.get(multiSelectDA);
        assertAttributeValue(multiSelectAttributeValue, multiSelectValue, multiSelectDA, position);

        dateAttributeValue = dynamicAttributeValues.get(dateDA);
        assertAttributeValue(dateAttributeValue, dateValue, dateDA, position);

        imgAttributeValue = dynamicAttributeValues.get(imgDA);
        assertAttributeValue(imgAttributeValue, fileName, blobValue, imgDA, position);

        // set new value for text and date and image
        final String newTextValue = "newTextValue";
        final String newDateValue = "1996-08-31";
        textAttributeWrapper.setValue(newTextValue);
        dateAttributeWrapper.setDate(newDateValue);

        final String newFileName = "another.bmp";
        final byte[] newBlobValue = "foo bar bar".getBytes();
        file = new UploadedFile(newFileName, new Long(newBlobValue.length), newBlobValue, "gif");
        imgAttributeWrapper.setFile(file);

        DynamicAttributesHelper.assignAttributeValuesToNode(attributeWrapperBeans, position);

        dynamicAttributeValues = position.getDynamicAttributeValues();
        textAttributeValue = dynamicAttributeValues.get(textDA);
        assertAttributeValue(textAttributeValue, newTextValue, textDA, position);

        multiSelectAttributeValue = dynamicAttributeValues.get(multiSelectDA);
        assertAttributeValue(multiSelectAttributeValue, multiSelectValue, multiSelectDA, position);

        dateAttributeValue = dynamicAttributeValues.get(dateDA);
        assertAttributeValue(dateAttributeValue, newDateValue, dateDA, position);

        imgAttributeValue = dynamicAttributeValues.get(imgDA);
        assertAttributeValue(imgAttributeValue, newFileName, newBlobValue, imgDA, position);

        // check nodeextendedattributes again
        assertNodeExtendedAttributes(extendedAttributes, newTextValue, textDA, newDateValue, dateDA, multiSelectValueAsArray, multiSelectDA, newFileName, newBlobValue, imgDA, position);
    }

    public void testClearAttributeValue() throws Exception {

        final List dynamicAttributes = new ArrayList();

        final DynamicAttribute imgDA = new DynamicAttribute(new Long(-4), "imgDA", DynamicAttribute.DA_TYPE_IMAGE);
        dynamicAttributes.add(imgDA);

        final Position position = positionService.findByID(DEFAULT_POSITION_ID);
        final NodeWrapperBean nodeWrapperBean = new PositionWrapperBean(position);
        final List attributeWrapperBeans = DynamicAttributesHelper.getAttributeWrapperBeans(dynamicAttributes, position, dynamicAttributeService);
        assertEquals(dynamicAttributes.size(), attributeWrapperBeans.size());
        nodeWrapperBean.setWrappedDynamicAttributes(attributeWrapperBeans);

        // set value
        final String fileName = "test.gif";
        final byte[] blobValue = "hello world".getBytes();
        UploadedFile file = new UploadedFile(fileName, new Long(blobValue.length), blobValue, "gif");
        final AttributeWrapperBean imgAttributeWrapper = (AttributeWrapperBean) attributeWrapperBeans.get(0);
        imgAttributeWrapper.setFile(file);

        // use helper to save values to node
        DynamicAttributesHelper.assignAttributeValuesToNode(attributeWrapperBeans, position);

        // check value is correct
        AttributeValuesCollection dynamicAttributeValues = position.getDynamicAttributeValues();
        AttributeValue imgAttributeValue = dynamicAttributeValues.get(imgDA);
        assertAttributeValue(imgAttributeValue, fileName, blobValue, imgDA, position);

        // clear value and save again
        for (int i = 0; i < dynamicAttributes.size(); i++) {
            final Long index = new Long(i);
            nodeWrapperBean.setDeleteImageIndex(index);
            DynamicAttributesHelper.clearAttributeValue(nodeWrapperBean);
        }

        DynamicAttributesHelper.assignAttributeValuesToNode(attributeWrapperBeans, position);

        // check extended attribute is gone
        dynamicAttributeValues = position.getDynamicAttributeValues();
        imgAttributeValue = dynamicAttributeValues.get(imgDA);
        assertNull(imgAttributeValue);

        final Set extendedAttributes = position.getExtendedAttributes();
        assertTrue(extendedAttributes.isEmpty());
    }

    public void testIsClearAttributeValueRequest() throws Exception {

        final Position position = positionService.findByID(DEFAULT_POSITION_ID);
        final NodeWrapperBean nodeWrapperBean = new PositionWrapperBean(position);

        final MockHttpServletRequest request = new MockHttpServletRequest();
        assertFalse(DynamicAttributesHelper.isClearAttributeValueRequest(request, nodeWrapperBean));

        // parameter alone is not enough
        request.addParameter(ControllerConstants.DELETE_IMAGE_INDEX, "foo");
        assertFalse(DynamicAttributesHelper.isClearAttributeValueRequest(request, nodeWrapperBean));

        // index must be supplied
        final Long index = new Long(0);
        nodeWrapperBean.setDeleteImageIndex(index);
        assertTrue(DynamicAttributesHelper.isClearAttributeValueRequest(request, nodeWrapperBean));
    }

    public void testGetSearchableExtendedAttributes() throws Exception {
        final Collection<DynamicAttributeDTO> subjectDTOCollection = dynamicAttributeService.getSearchableAttributeDtos("S");
        final Collection<DynamicAttributeDTO> positionDTOCollection = dynamicAttributeService.getSearchableAttributeDtos("P");
        assertNotNull(subjectDTOCollection);
        assertNotNull(positionDTOCollection);
    }

    private void assertAttributeValue(AttributeValue attributeValue, String fileName, byte[] blobValue, DynamicAttribute da, Position position) {
        assertAttributeValue(attributeValue, fileName, da, position);
        assertEquals(blobValue, ((AttributeValueFile) attributeValue).getBlobValue());
        assertEquals(1, attributeValue.getNodeExtendedAttributes().size());
    }

    private void assertNodeExtendedAttributes(Set extendedAttributes,
                                              final String textValue, final DynamicAttribute textDA,
                                              final String dateValue, final DynamicAttribute dateDA,
                                              final String[] multiSelectValueAsArray, final DynamicAttribute multiSelectDA,
                                              String fileName, byte[] blobValue, DynamicAttribute imgDA, Position node) {

        for (Iterator iterator = extendedAttributes.iterator(); iterator.hasNext();) {
            NodeExtendedAttribute nodeExtendedAttribute = (NodeExtendedAttribute) iterator.next();
            final String value = nodeExtendedAttribute.getValue();
            final DynamicAttribute dynamicAttribute = nodeExtendedAttribute.getDynamicAttribute();

            assertEquals(node, nodeExtendedAttribute.getNode());

            if (dynamicAttribute.isImageType()) {
                assertEquals(fileName, value);
                assertEquals(blobValue, ((NodeExtendedAttributeFile) nodeExtendedAttribute).getBlobValue());
                assertEquals(imgDA, dynamicAttribute);
            } else if (dynamicAttribute.isTextAttribute()) {
                assertEquals(textValue, value);
                assertEquals(textDA, dynamicAttribute);
            } else if (dynamicAttribute.isDateAttribute()) {
                assertEquals(dateValue, value);
                assertEquals(dateDA, dynamicAttribute);
            } else {
                assertTrue(ArrayUtils.contains(multiSelectValueAsArray, value));
                assertEquals(multiSelectDA, dynamicAttribute);
            }
        }
    }

    private void assertAttributeValue(AttributeValue attributeValue, final String expectedValue, final DynamicAttribute expectedDA, final Position position) {
        assertEquals(expectedValue, attributeValue.getValue());
        assertEquals(expectedDA, attributeValue.getDynamicAttribute());
        assertEquals(position, attributeValue.getNode());

        if (attributeValue instanceof AttributeValueFile) {
            assertNotNull(((AttributeValueFile) attributeValue).getBlobValue());
        }
    }

    private ILookupManager lookupManager;
    private IPositionService positionService;
    private IDynamicAttributeService dynamicAttributeService;
}