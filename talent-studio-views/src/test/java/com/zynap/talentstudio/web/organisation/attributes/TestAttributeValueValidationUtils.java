/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 01-Jul-2005 22:55:56
 * @version ${VERSION}
 */
package com.zynap.talentstudio.web.organisation.attributes;

import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.AbstractValidatorTestCase;
import com.zynap.talentstudio.web.common.validation.NodeValidator;
import com.zynap.talentstudio.web.organisation.attributes.validators.DateAttributeValueValidator;
import com.zynap.talentstudio.web.organisation.attributes.validators.DateTimeAttributeValueValidator;
import com.zynap.talentstudio.web.organisation.attributes.validators.ImageAttributeValueValidator;
import com.zynap.talentstudio.web.organisation.attributes.validators.IntegerAttributeValueValidator;
import com.zynap.talentstudio.web.organisation.attributes.validators.LinkAttributeValueValidator;
import com.zynap.talentstudio.web.organisation.attributes.validators.TextAttributeValueValidator;
import com.zynap.talentstudio.web.organisation.attributes.validators.TimeAttributeValueValidator;
import com.zynap.talentstudio.web.organisation.subjects.SubjectWrapperBean;
import com.zynap.util.spring.BindUtils;
import com.zynap.talentstudio.web.controller.ZynapDefaultFormController;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */

public class TestAttributeValueValidationUtils extends AbstractValidatorTestCase {

    protected void setUp() throws Exception {
        Map validationMappings = createFactoryMappings();
        factory = new AttributeValueValidationFactory();
        factory.setAttributeValueMappings(validationMappings);
        attributeWrapperBean = new AttributeWrapperBean("name", "id", new DynamicAttribute());
        subjectWrapperBean = new SubjectWrapperBean(new Subject());
        binder = new DataBinder(subjectWrapperBean, ZynapDefaultFormController.DEFAULT_COMMAND_NAME);
    }

    protected void tearDown() throws Exception {
    }

    public void testValidateAttributesFailsMandatory() throws Exception {
        DynamicAttribute textAttribute = new DynamicAttribute(new Long(2), "Test Text", TEXT_ATTR, "S", true, true, false);
        List attributes = setUpSubjectAttributes(textAttribute);

        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[] {ZERO_ATTRIBUTE_VALUE}, new Object[] {""});
        assertResult(pvs, attributes, "mandatory.has.no.value");
    }

    public void testValidateMandatoryNoValueNotMandatory() throws Exception {
        DynamicAttribute textAttribute = new DynamicAttribute(new Long(2), "Test Text", TEXT_ATTR, "S", false, true, false);
        List attributes = setUpSubjectAttributes(textAttribute);

        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[] {ZERO_ATTRIBUTE_VALUE}, new Object[] {""});
        assertResultNoErrors(pvs, attributes);
    }

    public void testValidateAttributesTextFailsMinLength() throws Exception {
        DynamicAttribute textAttribute = new DynamicAttribute(new Long(2), "Test Text", TEXT_ATTR, "S", true, true, false);
        textAttribute.setMinSize("10");
        List attributes = setUpSubjectAttributes(textAttribute);

        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[] {ZERO_ATTRIBUTE_VALUE}, new Object[] {"hello"});
        assertResult(pvs, attributes, "text.is.less.than.minlength");
    }

    public void testValidateAttributesTextFailsMaxLength() throws Exception {
        DynamicAttribute textAttribute = new DynamicAttribute(new Long(2), "Test Text", TEXT_ATTR, "S", true, true, false);
        textAttribute.setMaxSize("5");
        List attributes = setUpSubjectAttributes(textAttribute);

        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[] {ZERO_ATTRIBUTE_VALUE}, new Object[] {"hello_world"});
        assertResult(pvs, attributes, "text.is.greater.than.maxlength");        
    }

    public void testValidateAttributesTextSameMinMaxValueEqualSuccess() throws Exception {
        DynamicAttribute textAttribute = new DynamicAttribute(new Long(2), "Test Text", TEXT_ATTR, "S", true, true, false);
        textAttribute.setMaxSize("5");
        textAttribute.setMinSize("5");
        List attributes = setUpSubjectAttributes(textAttribute);

        // 5 letter word for the value
        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[] {ZERO_ATTRIBUTE_VALUE}, new Object[] {"hello"});
        assertResultNoErrors(pvs, attributes);
    }

    public void testValidateMandatoryNumberAttributesFailsMandatory() throws Exception {
        DynamicAttribute numAttribute = new DynamicAttribute(new Long(2), "Test Num", TEXT_ATTR, "S", true, true, false);
        List attributes = setUpSubjectAttributes(numAttribute);

        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[] {ZERO_ATTRIBUTE_VALUE}, new Object[] {""});
        assertResult(pvs, attributes, "mandatory.has.no.value");
    }

    public void testValidateMandatoryNumberAttributesLessThanMin() throws Exception {
        DynamicAttribute numAttribute = new DynamicAttribute(new Long(2), "Test Num", DynamicAttribute.DA_TYPE_NUMBER, "S", true, true, false);
        numAttribute.setMinSize("22");
        List attributes = setUpSubjectAttributes(numAttribute);

        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[] {ZERO_ATTRIBUTE_VALUE}, new Object[] {"20"});
        assertResult(pvs, attributes, "number.is.less.than.minsize");
    }

    public void testValidateMandatoryNumberAttributesGreaterThanMax() throws Exception {
        DynamicAttribute numAttribute = new DynamicAttribute(new Long(2), "Test Num", DynamicAttribute.DA_TYPE_NUMBER, "S", true, true, false);
        numAttribute.setMaxSize("18");
        List attributes = setUpSubjectAttributes(numAttribute);

        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[] {ZERO_ATTRIBUTE_VALUE}, new Object[] {"20"});
        assertResult(pvs, attributes, "number.is.greater.than.maxsize");
    }

    public void testValidateMandatoryNumberAttributeNotANumber() throws Exception {
        DynamicAttribute numAttribute = new DynamicAttribute(new Long(2), "Test Num", DynamicAttribute.DA_TYPE_NUMBER, "S", true, true, false);
        numAttribute.setMaxSize("18");
        List attributes = setUpSubjectAttributes(numAttribute);

        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[] {ZERO_ATTRIBUTE_VALUE}, new Object[] {"hello"});
        assertResult(pvs, attributes, "not.a.number");
    }

    public void testValidateMandatoryNumberAttributesSuccess() throws Exception {
        DynamicAttribute numAttribute = new DynamicAttribute(new Long(2), "Test Num", DynamicAttribute.DA_TYPE_NUMBER, "S", true, true, false);
        numAttribute.setMinSize("10");
        numAttribute.setMaxSize("18");
        List attributes = setUpSubjectAttributes(numAttribute);

        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[] {ZERO_ATTRIBUTE_VALUE}, new Object[] {"15"});
        assertResultNoErrors(pvs, attributes);
    }

    public void testValidateAttributeNumberSameAsRangeSuccess() throws Exception {
        DynamicAttribute numAttribute = new DynamicAttribute(new Long(2), "Test Num", DynamicAttribute.DA_TYPE_NUMBER, "S", true, true, false);
        numAttribute.setMaxSize("18");
        numAttribute.setMinSize("18");
        List attributes = setUpSubjectAttributes(numAttribute);

        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[] {ZERO_ATTRIBUTE_VALUE}, new Object[] {"18"});
        assertResultNoErrors(pvs, attributes);
    }

    public void testValidateAttributeNumberNoRangeProvidedSuccess() throws Exception {
        DynamicAttribute numAttribute = new DynamicAttribute(new Long(2), "Test Num", DynamicAttribute.DA_TYPE_NUMBER, "S", true, true, false);
        List attributes = setUpSubjectAttributes(numAttribute);

        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[] {ZERO_ATTRIBUTE_VALUE}, new Object[] {"18"});
        assertResultNoErrors(pvs, attributes);
    }

    public void testValidateAttributesSelectionValueNull() throws Exception {

        DynamicAttribute selectionTypeAttribute = new DynamicAttribute(new Long(2), "Test Struct", DynamicAttribute.DA_TYPE_STRUCT, "S", false, true, false);
        List attributes = setUpSubjectAttributes(selectionTypeAttribute);

        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[] {ZERO_ATTRIBUTE_VALUE}, new Object[] {null});
        assertResultNoErrors(pvs, attributes);
    }

    private void assertResultNoErrors(MutablePropertyValues pvs, List attributes) {
        Errors errors = validate(pvs, attributes);
        assertFalse(errors.hasErrors());
    }

    private void assertResult(MutablePropertyValues pvs, List attributes, String expectedCode) {
        Errors errors = validate(pvs, attributes);
        assertEquals(1, errors.getErrorCount());
        assertErrorCode(errors, expectedCode, ZERO_ATTRIBUTE_VALUE);
    }

    private Errors validate(MutablePropertyValues pvs, List attributes) {
        binder.bind(pvs);
        Errors errors = binder.getBindingResult();
        AttributeValueValidationUtils.validateAttributes(factory, attributes, errors, NodeValidator.ATTRIBUTE_WRAPPERS_PREFIX, null);
        return errors;
    }

    private List setUpSubjectAttributes(DynamicAttribute textAttribute) {
        attributeWrapperBean.setAttributeDefinition(textAttribute);
        AttributeValue attributeValue = AttributeValue.create(textAttribute);
        attributeWrapperBean.setAttributeValue(attributeValue);
        List attributes = new ArrayList();
        attributes.add(attributeWrapperBean);
        subjectWrapperBean.setWrappedDynamicAttributes(attributes);
        return attributes;
    }

    private Map createFactoryMappings() {
        Map mappings = new HashMap();
        mappings.put("TEXT", new TextAttributeValueValidator());
        mappings.put("TEXTAREA", new TextAttributeValueValidator());
        mappings.put("NUMBER", new IntegerAttributeValueValidator());
        mappings.put("DATE", new DateAttributeValueValidator());
        mappings.put("TIME", new TimeAttributeValueValidator());
        mappings.put("DATETIME", new DateTimeAttributeValueValidator());
        mappings.put("IMAGE", new ImageAttributeValueValidator());
        mappings.put("LINK", new LinkAttributeValueValidator());
        return mappings;
    }

    private DataBinder binder;
    private AttributeValueValidationFactory factory;
    private AttributeWrapperBean attributeWrapperBean;

    protected static final String ZERO_ATTRIBUTE_VALUE = NodeValidator.ATTRIBUTE_WRAPPERS_PREFIX + "[0].value";
    private SubjectWrapperBean subjectWrapperBean;
    protected static final String TEXT_ATTR = DynamicAttribute.DA_TYPE_TEXTFIELD;
}