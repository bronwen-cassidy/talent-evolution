/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.attributes;

import com.zynap.talentstudio.arenas.IArenaManager;
import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.organisation.attributes.DaDefinitionValidationFactory;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.validators.definition.DateRangeValidator;
import com.zynap.talentstudio.organisation.attributes.validators.definition.DateTimeRangeValidator;
import com.zynap.talentstudio.organisation.attributes.validators.definition.IntegerRangeValidator;
import com.zynap.talentstudio.organisation.attributes.validators.definition.StructValidator;
import com.zynap.talentstudio.organisation.attributes.validators.definition.TextRangeValidator;
import com.zynap.talentstudio.organisation.attributes.validators.definition.TimeRangeValidator;
import com.zynap.talentstudio.web.AbstractValidatorTestCase;
import com.zynap.util.spring.BindUtils;
import com.zynap.web.controller.ZynapDefaultFormController;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;

import java.util.HashMap;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TestDynamicAttributeValidator extends AbstractValidatorTestCase {

    protected void setUp() throws Exception {
        dynamicAttributeValidator = new DynamicAttributeValidator();
        DaDefinitionValidationFactory factory = new DaDefinitionValidationFactory();
        factory.setSpecificationMappings(createFactoryMappings());
        dynamicAttributeValidator.setFactory(factory);
        dynamicAttributeDefinition = new DynamicAttribute();
        dynamicAttributeWrapper = new DynamicAttributeWrapper(dynamicAttributeDefinition);
        binder = new DataBinder(dynamicAttributeWrapper, ZynapDefaultFormController.DEFAULT_COMMAND_NAME);
    }

    protected void tearDown() throws Exception {
        dynamicAttributeValidator = null;
        dynamicAttributeDefinition = null;
        dynamicAttributeWrapper = null;
    }

    public void testSupports() throws Exception {
        assertTrue(dynamicAttributeValidator.supports(DynamicAttributeWrapper.class));
    }

    public void testValidateLabel() throws Exception {
        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[]{"label", "description", "assignedArenas"},
                new Object[]{"", "description", CHOSEN_ASSIGNED_ARENAS});
        final Errors errors = bindAndValidate(pvs);
        assertErrorCount(1, errors);
        assertErrorCode(errors, "error.dynamicattribute.label.required", "label");
    }

    public void testValidateInvalidEmtpyLookupType() throws Exception {
        dynamicAttributeWrapper.setType(DynamicAttribute.DA_TYPE_STRUCT);
        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[]{"label", "description", "assignedArenas", "refersTo"},
                new Object[]{"test struct", "test description", CHOSEN_ASSIGNED_ARENAS, ""});
        Errors errors = bindAndValidate(pvs);
        assertErrorCount(1, errors);
        assertErrorCode(errors, "invalid.selection.value", "refersTo");
    }

    public void testValidateLookupTypeSuccess() throws Exception {
        dynamicAttributeWrapper.setType(DynamicAttribute.DA_TYPE_STRUCT);
        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[]{"label", "description", "assignedArenas", "refersTo"},
                new Object[]{"test struct", "test description", CHOSEN_ASSIGNED_ARENAS, ILookupManager.LOOKUP_TYPE_CLASSIFICATION});
        Errors errors = bindAndValidate(pvs);
        assertNoErrors(errors);
    }

    public void testValidateTextRangeEqualNoErrors() throws Exception {
        dynamicAttributeWrapper.setType(DynamicAttribute.DA_TYPE_TEXTFIELD);
        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[]{"label", "description", "assignedArenas", "minSize", "maxSize"},
                new Object[]{"test struct", "test description", CHOSEN_ASSIGNED_ARENAS, "12", "12"});
        Errors errors = bindAndValidate(pvs);
        assertNoErrors(errors);
    }

    public void testValidateTextRangeMinGreaterThanMax() throws Exception {
        dynamicAttributeWrapper.setType(DynamicAttribute.DA_TYPE_TEXTFIELD);
        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[]{"label", "description", "assignedArenas", "minSize", "maxSize"},
                new Object[]{"test struct", "test description", CHOSEN_ASSIGNED_ARENAS, "15", "12"});
        Errors errors = bindAndValidate(pvs);
        assertErrorCount(1, errors);
        assertErrorCode(errors, "invalid.range", "maxSize");
    }

    public void testValidateTextMinMaxLengthCannotBeZero() throws Exception {
        dynamicAttributeWrapper.setType(DynamicAttribute.DA_TYPE_TEXTFIELD);
        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[]{"label", "description", "assignedArenas", "minSize", "maxSize"},
                new Object[]{"test struct", "test description", CHOSEN_ASSIGNED_ARENAS, "0", "0"});
        Errors errors = bindAndValidate(pvs);
        assertErrorCount(1, errors);
        assertErrorCode(errors, "not.a.number", "minSize");
    }

    public void testValidateTextRangeNotANumber() throws Exception {
        dynamicAttributeWrapper.setType(DynamicAttribute.DA_TYPE_TEXTFIELD);
        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[]{"label", "description", "assignedArenas", "minSize", "maxSize"},
                new Object[]{"test struct", "test description", CHOSEN_ASSIGNED_ARENAS, "yes", "12"});
        Errors errors = bindAndValidate(pvs);
        assertErrorCount(1, errors);
        assertErrorCode(errors, "not.a.number", "minSize");
    }

    public void testValidateNumberRangeEqualNoErrors() throws Exception {
        dynamicAttributeWrapper.setType(DynamicAttribute.DA_TYPE_NUMBER);
        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[]{"label", "description", "assignedArenas", "minSize", "maxSize"},
                new Object[]{"test struct", "test description", CHOSEN_ASSIGNED_ARENAS, "12", "12"});
        Errors errors = bindAndValidate(pvs);
        assertNoErrors(errors);
    }

    public void testValidateNumberRangeMinGreaterThanMax() throws Exception {
        dynamicAttributeWrapper.setType(DynamicAttribute.DA_TYPE_NUMBER);
        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[]{"label", "description", "assignedArenas", "minSize", "maxSize"},
                new Object[]{"test struct", "test description", CHOSEN_ASSIGNED_ARENAS, "15", "12"});
        Errors errors = bindAndValidate(pvs);
        assertErrorCount(1, errors);
        assertErrorCode(errors, "invalid.range", "maxSize");
    }

    public void testValidateNumberRangeNotANumber() throws Exception {
        dynamicAttributeWrapper.setType(DynamicAttribute.DA_TYPE_NUMBER);
        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[]{"label", "description", "assignedArenas", "minSize", "maxSize"},
                new Object[]{"test struct", "test description", CHOSEN_ASSIGNED_ARENAS, "yes", "12"});
        Errors errors = bindAndValidate(pvs);
        assertErrorCount(1, errors);
        assertErrorCode(errors, "not.a.number", "minSize");
    }

    public void testValidateRangesDateTimeMinSizeDateOnly() throws Exception {
        dynamicAttributeWrapper.setType(DynamicAttribute.DA_TYPE_DATETIMESTAMP);
        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[]{"label", "description", "assignedArenas", "minDate", "minHour", "minMinute"},
                new Object[]{"test struct", "test description", CHOSEN_ASSIGNED_ARENAS, "15-02-2004", "", ""});
        Errors errors = bindAndValidate(pvs);
        assertErrorCount(1, errors);
        assertErrorCode(errors, "error.no.values.for.time", "minSize");
    }

    public void testValidateImageAttributeNoErrors() throws Exception {
        dynamicAttributeWrapper.setType(DynamicAttribute.DA_TYPE_IMAGE);
        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[]{"label", "description", "assignedArenas"},
                new Object[]{"test struct", "test description", CHOSEN_ASSIGNED_ARENAS});
        Errors errors = bindAndValidate(pvs);
        assertNoErrors(errors);
    }

    public void testValidateLinkAttributeNoErrors() throws Exception {
        dynamicAttributeWrapper.setType(DynamicAttribute.DA_TYPE_HTMLLINK);
        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[]{"label", "description", "assignedArenas"},
                new Object[]{"test struct", "test description", CHOSEN_ASSIGNED_ARENAS});
        Errors errors = bindAndValidate(pvs);
        assertNoErrors(errors);
    }

    private Errors bindAndValidate(MutablePropertyValues pvs) {
        binder.bind(pvs);
        Errors errors = getErrors(binder);
        dynamicAttributeValidator.validate(dynamicAttributeWrapper, errors);
        return errors;
    }

    public static Map createFactoryMappings() {
        Map<String, Object> mappings = new HashMap<String, Object>();
        // all the mappings required for the dynamicAttribute
        mappings.put("NUMBER", new IntegerRangeValidator());
        mappings.put("TEXT", new TextRangeValidator());
        mappings.put("TEXTAREA", new TextRangeValidator());
        mappings.put("STRUCT", new StructValidator());
        mappings.put("DATE", new DateRangeValidator());
        mappings.put("TIME", new TimeRangeValidator());
        mappings.put("DATETIME", new DateTimeRangeValidator());
        return mappings;
    }

    private DataBinder binder;
    private DynamicAttributeValidator dynamicAttributeValidator;
    private DynamicAttribute dynamicAttributeDefinition;
    private DynamicAttributeWrapper dynamicAttributeWrapper;

    private static final String[] CHOSEN_ASSIGNED_ARENAS = new String[]{IArenaManager.ORGANISATION_MODULE, IArenaManager.SUCCESSION_MODULE};
}