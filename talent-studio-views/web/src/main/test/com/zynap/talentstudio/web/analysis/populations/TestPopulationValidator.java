/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.populations;

import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.populations.PopulationCriteria;
import com.zynap.talentstudio.common.AccessType;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;
import com.zynap.util.spring.BindUtils;
import com.zynap.web.controller.ZynapDefaultFormController;

import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TestPopulationValidator extends ZynapMockControllerTest {

    public void setUp() throws Exception {

        super.setUp();

        populationValidator = (PopulationValidator) getBean("populationValidator");

        populationWrapperBean = new PopulationWrapperBean(new Population());
        binder = BindUtils.createBinder(populationWrapperBean, ZynapDefaultFormController.DEFAULT_COMMAND_NAME);
    }

    public void testValidateRequiredValues() throws Exception {

        final String[] propertyNames = new String[]{"population.label", "population.type", "population.scope"};
        final Object[] propertyValues = new Object[]{"testOne", IPopulationEngine.P_SUB_TYPE_, AccessType.PUBLIC_ACCESS.toString()};
        binder.bind(BindUtils.createPropertyValues(propertyNames, propertyValues));

        Errors errors = binder.getBindingResult();
        populationValidator.validateRequiredValues(populationWrapperBean, errors);
        assertFalse(errors.hasErrors());
    }

    public void testSupports() throws Exception {
        assertTrue(populationValidator.supports(PopulationWrapperBean.class));
    }

    public void testValidateAttributeRequired() throws Exception {

        CriteriaWrapperBean criteriaWrapperBean = new CriteriaWrapperBean(new PopulationCriteria());
        criteriaWrapperBean.setComparator(IPopulationEngine.EQ);
        populationWrapperBean.addPopulationCriteria(criteriaWrapperBean);

        Errors errors = binder.getBindingResult();
        populationValidator.validateCriteria(populationWrapperBean, errors);

        assertEquals(1, errors.getErrorCount());
        assertEquals("criteria.attribute.required", errors.getFieldError("populationCriterias[0].attribute").getCode());
    }

    public void testValidateComparatorRequired() throws Exception {

        CriteriaWrapperBean criteriaWrapperBean = new CriteriaWrapperBean(new PopulationCriteria());
        criteriaWrapperBean.setAttribute(AnalysisAttributeHelper.FIRST_NAME_ATTR);
        populationWrapperBean.addPopulationCriteria(criteriaWrapperBean);

        Errors errors = binder.getBindingResult();
        populationValidator.validateCriteria(populationWrapperBean, errors);

        assertEquals(1, errors.getErrorCount());
        assertEquals("criteria.comparator.required", errors.getFieldError("populationCriterias[0].comparator").getCode());
    }

    public void testValidateOperatorRequired() throws Exception {

        CriteriaWrapperBean coreDetailCriteriaWrapper = new CriteriaWrapperBean(new PopulationCriteria());
        coreDetailCriteriaWrapper.setAttribute(AnalysisAttributeHelper.FIRST_NAME_ATTR);
        coreDetailCriteriaWrapper.setComparator(IPopulationEngine.EQ);
        populationWrapperBean.addPopulationCriteria(coreDetailCriteriaWrapper);

        CriteriaWrapperBean emailCriteriaWrapper = new CriteriaWrapperBean(new PopulationCriteria());
        emailCriteriaWrapper.setComparator(IPopulationEngine.EQ);
        emailCriteriaWrapper.setAttribute(AnalysisAttributeHelper.EMAIL_ATTR);
        populationWrapperBean.addPopulationCriteria(emailCriteriaWrapper);

        Errors errors = binder.getBindingResult();
        populationValidator.validateCriteria(populationWrapperBean, errors);

        assertEquals(1, errors.getErrorCount());
        assertEquals("criteria.operator.required", errors.getFieldError("populationCriterias[0].operator").getCode());
    }

    public void testCriteriaWithNullOperator () throws Exception {

        final String name = AnalysisAttributeHelper.FIRST_NAME_ATTR;

        CriteriaWrapperBean criteriaWrapperBean = new CriteriaWrapperBean(new PopulationCriteria());
        criteriaWrapperBean.setAttribute(name);
        criteriaWrapperBean.setAttributeDefinition(new AttributeWrapperBean(name, null, new DynamicAttribute()));
        criteriaWrapperBean.setComparator(IPopulationEngine.ISNULL);
        criteriaWrapperBean.getAttributeDefinition().setValue("test");
        populationWrapperBean.addPopulationCriteria(criteriaWrapperBean);

        Errors errors = binder.getBindingResult();
        populationValidator.validateCriteria(populationWrapperBean, errors);

        // check that value comes back null
        assertNull(criteriaWrapperBean.getAttributeDefinition().getValue());
    }

    public void testValidateCriteriaNullValue() {
        final String name = AnalysisAttributeHelper.FIRST_NAME_ATTR;

        CriteriaWrapperBean criteriaWrapperBean = new CriteriaWrapperBean(new PopulationCriteria());
        criteriaWrapperBean.setAttribute(name);
        criteriaWrapperBean.setAttributeDefinition(new AttributeWrapperBean(name, null, new DynamicAttribute()));
        criteriaWrapperBean.setComparator(IPopulationEngine.LIKE);

        populationWrapperBean.addPopulationCriteria(criteriaWrapperBean);
        Errors errors = binder.getBindingResult();
        populationValidator.validateCriteria(populationWrapperBean, errors);
        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getErrorCount());
        assertNotNull(errors.getFieldError("populationCriterias[0].attributeDefinition.value"));
    }

    public void testValidateCriteriaNodeTypeNullValue() {
        final String name = AnalysisAttributeHelper.FIRST_NAME_ATTR;

        CriteriaWrapperBean criteriaWrapperBean = new CriteriaWrapperBean(new PopulationCriteria());
        criteriaWrapperBean.setAttribute(name);

        criteriaWrapperBean.setAttributeDefinition(new AttributeWrapperBean(name, null, DynamicAttribute.DA_TYPE_OU_0));
        criteriaWrapperBean.setComparator(IPopulationEngine.EQ);
        populationWrapperBean.addPopulationCriteria(criteriaWrapperBean);

        Errors errors = binder.getBindingResult();
        populationValidator.validateCriteria(populationWrapperBean, errors);
        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getErrorCount());
        assertNotNull(errors.getFieldError("populationCriterias[0].attributeDefinition.value"));
    }

    public void testValidateCriteriaDerivedNullValue() {
        final String name = AnalysisAttributeHelper.FIRST_NAME_ATTR;

        CriteriaWrapperBean criteriaWrapperBean = new CriteriaWrapperBean(new PopulationCriteria());
        criteriaWrapperBean.setAttribute(name);

        criteriaWrapperBean.setAttributeDefinition(new AttributeWrapperBean(name, null, IDynamicAttributeService.PP_SUB_DERIVED_ATT_DEFINITION));
        criteriaWrapperBean.setComparator(IPopulationEngine.EQ);
        criteriaWrapperBean.getAttributeDefinition().setValue("");

        populationWrapperBean.addPopulationCriteria(criteriaWrapperBean);

        Errors errors = binder.getBindingResult();
        populationValidator.validateCriteria(populationWrapperBean, errors);
        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getErrorCount());
        assertNotNull(errors.getFieldError("populationCriterias[0].attributeDefinition.value"));
    }

    public void testValidateCriteriaDerivedNanValue() {
        final String name = AnalysisAttributeHelper.FIRST_NAME_ATTR;

        CriteriaWrapperBean criteriaWrapperBean = new CriteriaWrapperBean(new PopulationCriteria());
        criteriaWrapperBean.setAttribute(name);

        criteriaWrapperBean.setAttributeDefinition(new AttributeWrapperBean(name, null, IDynamicAttributeService.PP_SUB_DERIVED_ATT_DEFINITION));
        criteriaWrapperBean.setComparator(IPopulationEngine.EQ);
        criteriaWrapperBean.getAttributeDefinition().setValue("abc");

        populationWrapperBean.addPopulationCriteria(criteriaWrapperBean);

        Errors errors = binder.getBindingResult();
        populationValidator.validateCriteria(populationWrapperBean, errors);
        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getErrorCount());
        assertNotNull(errors.getFieldError("populationCriterias[0].attributeDefinition.value"));
    }

    public void testValidRefValue() throws Exception {

        final DynamicAttribute dateAttribute = DynamicAttribute.DA_TYPE_DATE_O;
        final String dateAttributeName = AnalysisAttributeHelper.DOB_ATTR;
        final String dateAttributeValue = "fred";
        buildCriteria(dateAttribute, dateAttributeName, dateAttributeValue);

        final DynamicAttribute numberAttribute = DynamicAttribute.DA_TYPE_NUMBER_O;
        final String numberAttributeName = AnalysisAttributeHelper.DOB_ATTR;
        final String numberAttributeValue = "fred";
        buildCriteria(numberAttribute, numberAttributeName, numberAttributeValue);

        final DynamicAttribute derivedAttribute = IDynamicAttributeService.PP_SUB_DERIVED_ATT_DEFINITION;
        final String derivedAttributeName = AnalysisAttributeHelper.DOB_ATTR;
        final String derivedAttributeValue = "fred";
        buildCriteria(derivedAttribute, derivedAttributeName, derivedAttributeValue);

        final DynamicAttribute orgUnitAttribute = DynamicAttribute.DA_TYPE_OU_0;
        final String orgUnitAttributeName = AnalysisAttributeHelper.ORG_UNIT_ID_ATTR;
        final String orgUnitAttributeValue = null;
        buildCriteria(orgUnitAttribute, orgUnitAttributeName, orgUnitAttributeValue);

        Errors errors = binder.getBindingResult();
        populationValidator.validateCriteria(populationWrapperBean, errors);
        final FieldError dateError = errors.getFieldError("populationCriterias[0].attributeDefinition.date");
        assertNotNull(dateError);
        assertEquals(dateAttributeValue, dateError.getRejectedValue());
        assertEquals("not.a.date", dateError.getCode());

        final FieldError numberError = errors.getFieldError("populationCriterias[1].attributeDefinition.value");
        assertNotNull(numberError);
        assertEquals(numberAttributeValue, numberError.getRejectedValue());
        assertEquals("not.a.number", numberError.getCode());

        final FieldError derivedError = errors.getFieldError("populationCriterias[2].attributeDefinition.value");
        assertNotNull(derivedError);
        assertEquals(numberAttributeValue, derivedError.getRejectedValue());
        assertEquals("criteria.number.required", derivedError.getCode());

        final FieldError orgUnitError = errors.getFieldError("populationCriterias[3].attributeDefinition.value");
        assertNotNull(orgUnitError);
        assertNull(orgUnitError.getRejectedValue());
        assertEquals("mandatory.has.no.value", orgUnitError.getCode());
    }

    private void buildCriteria(final DynamicAttribute dynamicAttribute, final String name, final String invalidValue) {

        CriteriaWrapperBean newCriteriaWrapperBean = new CriteriaWrapperBean(new PopulationCriteria());
        newCriteriaWrapperBean.setAttributeDefinition(new AttributeWrapperBean(dynamicAttribute));
        newCriteriaWrapperBean.setAttribute(name);
        newCriteriaWrapperBean.setComparator(IPopulationEngine.EQ);

        newCriteriaWrapperBean.getAttributeDefinition().setAttributeValue(AttributeValue.create(invalidValue, dynamicAttribute));
        populationWrapperBean.addPopulationCriteria(newCriteriaWrapperBean);
    }

    public void testInvalidBrackets() throws Exception {

        CriteriaWrapperBean coreDetailCriteriaWrapper = new CriteriaWrapperBean(new PopulationCriteria());
        coreDetailCriteriaWrapper.setAttribute(AnalysisAttributeHelper.FIRST_NAME_ATTR);
        coreDetailCriteriaWrapper.setComparator(IPopulationEngine.EQ);
        coreDetailCriteriaWrapper.setLeftBracket(IPopulationEngine.LEFT_BRCKT_);
        coreDetailCriteriaWrapper.setOperator(IPopulationEngine.OR);

        populationWrapperBean.addPopulationCriteria(coreDetailCriteriaWrapper);

        CriteriaWrapperBean emailCriteriaWrapper = new CriteriaWrapperBean(new PopulationCriteria());
        emailCriteriaWrapper.setComparator(IPopulationEngine.EQ);
        emailCriteriaWrapper.setAttribute(AnalysisAttributeHelper.EMAIL_ATTR);
        emailCriteriaWrapper.setLeftBracket(IPopulationEngine.LEFT_BRCKT_);

        populationWrapperBean.addPopulationCriteria(emailCriteriaWrapper);

        Errors errors = binder.getBindingResult();
        populationValidator.validateBrackets(populationWrapperBean, errors);
        assertEquals(2, errors.getErrorCount());
        assertEquals("criteria.brackets.notmatched", errors.getFieldError("populationCriterias[0].leftBracket").getCode());
        assertEquals("criteria.brackets.notmatched", errors.getFieldError("populationCriterias[1].leftBracket").getCode());
    }

    public void testValidateBrackets() throws Exception {

        CriteriaWrapperBean coreDetailCriteriaWrapper = new CriteriaWrapperBean(new PopulationCriteria());
        coreDetailCriteriaWrapper.setAttribute(AnalysisAttributeHelper.FIRST_NAME_ATTR);
        coreDetailCriteriaWrapper.setComparator(IPopulationEngine.EQ);
        coreDetailCriteriaWrapper.setLeftBracket(IPopulationEngine.LEFT_BRCKT_);
        coreDetailCriteriaWrapper.setOperator(IPopulationEngine.OR);

        populationWrapperBean.addPopulationCriteria(coreDetailCriteriaWrapper);

        CriteriaWrapperBean emailCriteriaWrapper = new CriteriaWrapperBean(new PopulationCriteria());
        emailCriteriaWrapper.setComparator(IPopulationEngine.EQ);
        emailCriteriaWrapper.setAttribute(AnalysisAttributeHelper.EMAIL_ATTR);
        emailCriteriaWrapper.setRightBracket(IPopulationEngine.RIGHT_BRCKT_);

        populationWrapperBean.addPopulationCriteria(emailCriteriaWrapper);

        Errors errors = binder.getBindingResult();
        populationValidator.validateBrackets(populationWrapperBean, errors);
        assertEquals(0, errors.getErrorCount());
    }

    private PopulationValidator populationValidator;
    private PopulationWrapperBean populationWrapperBean;
    private DataBinder binder;
    protected static final String LABEL_INVERSE = "labelInverse";
}