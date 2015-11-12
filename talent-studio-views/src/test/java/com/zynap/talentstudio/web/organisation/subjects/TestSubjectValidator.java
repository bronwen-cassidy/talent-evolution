/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.subjects;

import com.zynap.common.util.UploadedFile;
import com.zynap.domain.IDomainObject;
import com.zynap.domain.admin.LoginInfo;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.CoreDetail;
import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.common.lookups.LookupType;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.organisation.subjects.SubjectAssociation;
import com.zynap.talentstudio.web.AbstractValidatorTestCase;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.validation.AssociationValidationHelper;
import com.zynap.talentstudio.web.organisation.associations.ArtefactAssociationWrapperBean;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;
import com.zynap.talentstudio.web.organisation.attributes.FormAttribute;
import com.zynap.util.spring.BindUtils;
import com.zynap.talentstudio.web.controller.ZynapDefaultFormController;

import org.apache.commons.lang.StringUtils;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TestSubjectValidator extends AbstractValidatorTestCase {

    public void setUp() throws Exception {
        subjectValidator = new SubjectValidator();
        Subject subject = new Subject();
        subjectWrapperBean = new SubjectWrapperBean(subject);
        binder = createDataBinder(subjectWrapperBean);
    }

    public void tearDown() throws Exception {
        subjectValidator = null;
        subjectWrapperBean = null;
    }

    public void testSupports() throws Exception {
        assertTrue(subjectValidator.supports(SubjectWrapperBean.class));
    }

    /**
     * Validates the dynamicAttributes.
     *
     * @throws Exception
     */
    public void testValidateDynamicAttributes() throws Exception {
        List<FormAttribute> wrappedAttributes = new ArrayList<FormAttribute>();
        AttributeWrapperBean attributeWrapper = new AttributeWrapperBean("one", new Long(2).toString(), createAttributeDefinition(new Long(3), "Yesterday"));
        wrappedAttributes.add(attributeWrapper);
        subjectWrapperBean.setWrappedDynamicAttributes(wrappedAttributes);
        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[]{"wrappedDynamicAttributes[0]." + attributeWrapper.getFieldName()}, new Object[]{"attribute value sunday"});
        binder.bind(pvs);
        Errors errors = binder.getBindingResult();
        subjectValidator.validateDynamicAttributes(subjectWrapperBean, errors);
        assertFalse(errors.hasErrors());
    }

    /**
     * Validates the required values of firstName, secondName etc.
     *
     * @throws Exception
     */
    public void testValidateRequiredValues() throws Exception {

        // set no values and check for errors
        Errors errors = binder.getBindingResult();
        subjectValidator.validateRequiredValues(subjectWrapperBean, errors);
        assertEquals(2, errors.getErrorCount());

        // set values - should now get no validation errors
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.addPropertyValue(SubjectValidator.FIRST_NAME_FIELD, "Mandy");
        propertyValues.addPropertyValue(SubjectValidator.SECOND_NAME_FIELD, "Sunday");
        propertyValues.addPropertyValue(SubjectValidator.PREF_NAME_FIELD, null);
        propertyValues.addPropertyValue(SubjectValidator.TITLE_FIELD, null);
        binder.bind(propertyValues);

        binder = new DataBinder(subjectWrapperBean, ControllerConstants.COMMAND_NAME);
        errors = binder.getBindingResult();
        subjectValidator.validateRequiredValues(subjectWrapperBean, errors);
        assertFalse(errors.hasErrors());
    }

    /**
     * Validates the required values of firstName, secondName etc.
     *
     * @throws Exception
     */
    public void testValidateRequiredValuesLengths() throws Exception {

        // set first name, last name and pref. given name to be too long
        String value = StringUtils.leftPad("*", 151);
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.addPropertyValue(SubjectValidator.FIRST_NAME_FIELD, value);
        propertyValues.addPropertyValue(SubjectValidator.SECOND_NAME_FIELD, value);
        propertyValues.addPropertyValue(SubjectValidator.PREF_NAME_FIELD, value);
        propertyValues.addPropertyValue(SubjectValidator.TITLE_FIELD, "Ms");
        binder.bind(propertyValues);

        binder = new DataBinder(subjectWrapperBean, ControllerConstants.COMMAND_NAME);
        Errors errors = binder.getBindingResult();
        subjectValidator.validateRequiredValues(subjectWrapperBean, errors);
        assertEquals(3, errors.getErrorCount());

        assertErrorCode(errors, "error.firstname.maxlength", SubjectValidator.FIRST_NAME_FIELD);
        assertErrorCode(errors, "error.lastname.maxlength", SubjectValidator.SECOND_NAME_FIELD);
        assertErrorCode(errors, "error.givenname.maxlength", SubjectValidator.PREF_NAME_FIELD);
    }

    /**
     * Validation of login info only gets validated if the login info has a username set.
     *
     * @throws Exception
     */
    public void testValidateLoginNoValues() throws Exception {

        // no validation as no login info supplied
        Errors errors = binder.getBindingResult();
        subjectValidator.validateLoginValues(subjectWrapperBean, errors);
        assertFalse(errors.hasErrors());
    }

    /**
     * Validation of login info only gets validated if the login info has a username set.
     *
     * @throws Exception
     */
    public void testValidateLoginValues() throws Exception {

        CoreDetail coreDetail = new CoreDetail("dr", "fred", "flintstone");
        coreDetail.setPrefGivenName("fred");
        Subject subject = new Subject(coreDetail);
        subject.setActive(true);
        subject.setDateOfBirth(new Date());

        LoginInfo loginInfo = new LoginInfo();
        final User user = new User(loginInfo, coreDetail);
        subject.setUser(user);
        subjectWrapperBean = new SubjectWrapperBean(subject);

        binder = new DataBinder(subjectWrapperBean, ZynapDefaultFormController.DEFAULT_COMMAND_NAME);
        MutablePropertyValues pvs = new MutablePropertyValues();
        pvs.addPropertyValue(SubjectValidator.USER_NAME_FIELD, "username");
        binder.bind(pvs);

        Errors errors = binder.getBindingResult();
        subjectValidator.validateLoginValues(subjectWrapperBean, errors);
        assertEquals(2, errors.getErrorCount());
        assertErrorCode(errors, "error.password.missing", SubjectValidator.PASSWORD_FIELD);
        assertErrorCode(errors, "error.repeat.password.missing", SubjectValidator.REPEATED_PASSWORD_FIELD);
    }

    /**
     * Check that a primary association requires a target and a qualifier.
     *
     * @throws Exception
     */
    public void testValidatePrimaryAssociations() throws Exception {

        // add association and set qualifier and target ids to null through binder
        Errors errors = binder.getBindingResult();
        subjectWrapperBean.addSubjectAssociation(createPrimaryAssociation(subjectWrapperBean));
        MutablePropertyValues pvs = BindUtils.createPropertyValues(new String[]{FIRST_PRIMARY_ASSOCIATION_QUALIFIER_FIELD, FIRST_PRIMARY_ASSOCIATION_TARGET_FIELD}, new Object[]{IDomainObject.UNASSIGNED_VALUE, IDomainObject.UNASSIGNED_VALUE});
        binder.bind(pvs);

        // check validation fails
        subjectValidator.validateSubjectPrimaryAssociations(subjectWrapperBean, errors);
        assertEquals(2, errors.getErrorCount());

        assertErrorCode(errors, "error.association.qualifier.required", FIRST_PRIMARY_ASSOCIATION_QUALIFIER_FIELD);
        assertErrorCode(errors, "error.association.target.required", FIRST_PRIMARY_ASSOCIATION_TARGET_FIELD);
    }

    /**
     * Primary associations are optional so validation only occurs if at least one is present.
     *
     * @throws Exception
     */
    public void testNoPrimaryAssociation() throws Exception {

        // primary associations not required so no validation expected
        Errors errors = binder.getBindingResult();
        subjectValidator.validateSubjectPrimaryAssociations(subjectWrapperBean, errors);
        assertFalse(errors.hasErrors());
    }

    /**
     * Check picture extensions are rejected if not appropriate.
     * 
     * @throws Exception
     */
    public void testPicture() throws Exception {

        final byte[] file = new byte[10];
        subjectWrapperBean.setFile(new UploadedFile("origfilename", new Long(file.length), file, "htm"));

        Errors errors = binder.getBindingResult();
        subjectValidator.validateCoreValues(subjectWrapperBean, errors);
        assertErrorCode(errors, "not.an.image", "file");
    }

    public void testValidatePrimaryAssociationNoQualifier() {

        final SubjectAssociation primaryAssociation = createPrimaryAssociation(subjectWrapperBean);
        primaryAssociation.setTarget(new Position(new Long(1)));
        primaryAssociation.getQualifier().setId(IDomainObject.UNASSIGNED_VALUE);
        subjectWrapperBean.addSubjectAssociation(primaryAssociation);

        Errors errors = binder.getBindingResult();
        subjectValidator.validateSubjectPrimaryAssociations(subjectWrapperBean, errors);

        assertEquals(1, errors.getErrorCount());
        assertErrorCode(errors, "error.association.qualifier.required", FIRST_PRIMARY_ASSOCIATION_QUALIFIER_FIELD);
    }

    public void testValidatePrimaryAssociationNoTarget() {

        final SubjectAssociation primaryAssociation = createPrimaryAssociation(subjectWrapperBean);
        PRIMARY_ASSOCIATION_QUALIFIER.setId(new Long(10));
        primaryAssociation.setQualifier(PRIMARY_ASSOCIATION_QUALIFIER);
        primaryAssociation.getTarget().setId(IDomainObject.UNASSIGNED_VALUE);
        subjectWrapperBean.addSubjectAssociation(primaryAssociation);

        Errors errors = binder.getBindingResult();
        subjectValidator.validateSubjectPrimaryAssociations(subjectWrapperBean, errors);

        assertEquals(1, errors.getErrorCount());
        assertErrorCode(errors, "error.association.target.required", FIRST_PRIMARY_ASSOCIATION_TARGET_FIELD);
    }

    public void testPrimaryAssociationsDuplicateQualifier() {

        final SubjectAssociation primaryAssociation = createPrimaryAssociation(subjectWrapperBean);
        subjectWrapperBean.addSubjectAssociation(primaryAssociation);
        subjectWrapperBean.addSubjectAssociation(primaryAssociation);
        assertEquals(2, subjectWrapperBean.getSubjectPrimaryAssociations().size());

        final Long qualifierId = new Long(10);
        final Long targetId = new Long(11);
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.addPropertyValue(FIRST_PRIMARY_ASSOCIATION_QUALIFIER_FIELD, qualifierId);
        propertyValues.addPropertyValue(FIRST_PRIMARY_ASSOCIATION_TARGET_FIELD, targetId);

        propertyValues.addPropertyValue(SECOND_PRIMARY_ASSOCIATION_QUALIFIER_FIELD, qualifierId);
        propertyValues.addPropertyValue(SECOND_PRIMARY_ASSOCIATION_TARGET_FIELD, targetId);
        binder.bind(propertyValues);

        // primary associations can share targets but not qualifiers
        // therefore there will be 1 validation error
        Errors errors = binder.getBindingResult();
        subjectValidator.validateSubjectPrimaryAssociations(subjectWrapperBean, errors);
        assertEquals(1, errors.getErrorCount());
        assertErrorCode(errors, "error.association.target.unique.required", FIRST_PRIMARY_ASSOCIATION_TARGET_FIELD);
    }

    public void testPrimaryAssociationNoOrphans() {

        Errors errors = binder.getBindingResult();
        subjectValidator.validateSubjectPrimaryAssociations(subjectWrapperBean, errors, false);
        assertEquals(1, errors.getGlobalErrorCount());
        assertEquals("error.association.primary.required", errors.getGlobalError().getCode());

        final SubjectAssociation primaryAssociation = createPrimaryAssociation(subjectWrapperBean);
        primaryAssociation.setTarget(new Position(new Long(1)));
        primaryAssociation.getQualifier().setId(new Long(2));
        subjectWrapperBean.addSubjectAssociation(primaryAssociation);

        binder = createDataBinder(subjectWrapperBean);        
        errors = binder.getBindingResult();
        subjectValidator.validateSubjectPrimaryAssociations(subjectWrapperBean, errors, false);
        assertEquals(0, errors.getGlobalErrorCount());
    }

    /**
     * Validates that an added secondary association which has no values entered fails validation.
     *
     * @throws Exception
     */
    public void testValidateSecondaryAssociations() throws Exception {

        // add invalid primary association to check that primary association is not validated
        subjectWrapperBean.addSubjectAssociation(createPrimaryAssociation(subjectWrapperBean));

        subjectWrapperBean.addNewSecondaryAssociation();
        subjectWrapperBean.addNewSecondaryAssociation();

        // should be 2 errors per association - one for target and one for qualifier
        Errors errors = binder.getBindingResult();
        subjectValidator.validateSubjectSecondaryAssociations(subjectWrapperBean, errors);
        assertEquals(subjectWrapperBean.getSubjectSecondaryAssociations().size() * 2, errors.getErrorCount());
    }

    /**
     * Validates that a secondary association with the same qualifier and target are considered duplicates.
     *
     * @throws Exception
     */
    public void testValidateSecondaryAssociationsIdentical() throws Exception {

        subjectWrapperBean.addSubjectAssociation(createPrimaryAssociation(subjectWrapperBean));

        subjectWrapperBean.addNewSecondaryAssociation();
        subjectWrapperBean.addNewSecondaryAssociation();

        List subjectSecondaryAssociations = subjectWrapperBean.getSubjectSecondaryAssociations();
        for (int i = 0; i < subjectSecondaryAssociations.size(); i++) {
            ArtefactAssociationWrapperBean artefactAssociationWrapperBean = (ArtefactAssociationWrapperBean) subjectSecondaryAssociations.get(i);
            artefactAssociationWrapperBean.setTargetId(new Long(22));
            artefactAssociationWrapperBean.setQualifierId(new Long(403));
        }

        Errors errors = binder.getBindingResult();
        subjectValidator.validateSubjectSecondaryAssociations(subjectWrapperBean, errors);
        assertEquals(1, errors.getErrorCount());
    }

    /**
     * Validates that an added secondary association which has no target, but has a qualifier, entered fails validation.
     *
     * @throws Exception
     */
    public void testValidateSecondaryAssociationsNoTarget() throws Exception {

        // set two secondary associations with no target
        subjectWrapperBean.addNewSecondaryAssociation();
        subjectWrapperBean.addNewSecondaryAssociation();
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.addPropertyValue(FIRST_SECONDARY_ASSOCIATION_QUALIFIER_FIELD, new Long(10));
        propertyValues.addPropertyValue(FIRST_SECONDARY_ASSOCIATION_TARGET_FIELD, IDomainObject.UNASSIGNED_VALUE);

        propertyValues.addPropertyValue(SECOND_SECONDARY_ASSOCIATION_QUALIFIER_FIELD, new Long(11));
        propertyValues.addPropertyValue(SECOND_SECONDARY_ASSOCIATION_TARGET_FIELD, IDomainObject.UNASSIGNED_VALUE);
        binder.bind(propertyValues);

        // should be 2 errors as neither association has a target
        Errors errors = binder.getBindingResult();
        subjectValidator.validateSubjectSecondaryAssociations(subjectWrapperBean, errors);
        assertEquals(subjectWrapperBean.getSubjectSecondaryAssociations().size(), errors.getErrorCount());
        assertErrorCode(errors, "error.association.target.required", FIRST_SECONDARY_ASSOCIATION_TARGET_FIELD);
        assertErrorCode(errors, "error.association.target.required", SECOND_SECONDARY_ASSOCIATION_TARGET_FIELD);
    }

    /**
     * Validates that an added secondary association which has no qualifier, but has a target, entered fails validation.
     *
     * @throws Exception
     */
    public void testValidateSecondaryAssociationsNoQualifier() throws Exception {

        // set two secondary associations with no qualifier
        subjectWrapperBean.addNewSecondaryAssociation();
        subjectWrapperBean.addNewSecondaryAssociation();
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.addPropertyValue(FIRST_SECONDARY_ASSOCIATION_QUALIFIER_FIELD, IDomainObject.UNASSIGNED_VALUE);
        propertyValues.addPropertyValue(FIRST_SECONDARY_ASSOCIATION_TARGET_FIELD, new Long(10));

        propertyValues.addPropertyValue(SECOND_SECONDARY_ASSOCIATION_QUALIFIER_FIELD, IDomainObject.UNASSIGNED_VALUE);
        propertyValues.addPropertyValue(SECOND_SECONDARY_ASSOCIATION_TARGET_FIELD, new Long(11));
        binder.bind(propertyValues);

        // should be 2 errors as neither association has a qualifier
        Errors errors = binder.getBindingResult();
        subjectValidator.validateSubjectSecondaryAssociations(subjectWrapperBean, errors);
        assertEquals(subjectWrapperBean.getSubjectSecondaryAssociations().size(), errors.getErrorCount());
        assertErrorCode(errors, "error.association.qualifier.required", FIRST_SECONDARY_ASSOCIATION_QUALIFIER_FIELD);
        assertErrorCode(errors, "error.association.qualifier.required", SECOND_SECONDARY_ASSOCIATION_QUALIFIER_FIELD);
    }

    /**
     * Validates that a secondary association with the same qualifier are considered duplicates.
     *
     * @throws Exception
     */
    public void testSecondaryAssociationsDuplicateQualifier() throws Exception {

        subjectWrapperBean.addNewSecondaryAssociation();
        subjectWrapperBean.addNewSecondaryAssociation();
        assertEquals(2, subjectWrapperBean.getSubjectSecondaryAssociations().size());

        final Long qualifierId = new Long(10);
        final Long targetId = new Long(11);
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.addPropertyValue(FIRST_SECONDARY_ASSOCIATION_QUALIFIER_FIELD, qualifierId);
        propertyValues.addPropertyValue(FIRST_SECONDARY_ASSOCIATION_TARGET_FIELD, targetId);

        propertyValues.addPropertyValue(SECOND_SECONDARY_ASSOCIATION_QUALIFIER_FIELD, qualifierId);
        propertyValues.addPropertyValue(SECOND_SECONDARY_ASSOCIATION_TARGET_FIELD, targetId);
        binder.bind(propertyValues);

        // primary associations can share targets and qualifiers
        // therefore there will be no validation errors
        Errors errors = binder.getBindingResult();
        subjectValidator.validateSubjectPrimaryAssociations(subjectWrapperBean, errors);
        assertFalse(errors.hasErrors());
    }

    private DynamicAttribute createAttributeDefinition(Long id, String label) {
        return new DynamicAttribute(id, label, DynamicAttribute.DA_TYPE_TEXTFIELD, Node.POSITION_UNIT_TYPE_, false, true, false);
    }

    private SubjectAssociation createPrimaryAssociation(SubjectWrapperBean subjectWrapperBean) {
        return new SubjectAssociation(new LookupValue(ILookupManager.LOOKUP_TYPE_PRIMARY_SUBJECT_ASSOC), subjectWrapperBean.getModifiedSubject(ROOT_USER), new Position());
    }

    private SubjectValidator subjectValidator;
    private SubjectWrapperBean subjectWrapperBean;
    private DataBinder binder;

    private static final LookupValue PRIMARY_ASSOCIATION_QUALIFIER = new LookupValue("ACTING", "Acting", "description", new LookupType(ILookupManager.LOOKUP_TYPE_PRIMARY_SUBJECT_ASSOC));

    private static final String FIRST_PRIMARY_ASSOCIATION_QUALIFIER_FIELD = "subjectPrimaryAssociations[0]" + AssociationValidationHelper.ASSOCIATION_QUALIFIER_FIELD;
    private static final String FIRST_PRIMARY_ASSOCIATION_TARGET_FIELD = "subjectPrimaryAssociations[0]" + AssociationValidationHelper.ASSOCIATION_TARGET_FIELD;

    private static final String SECOND_PRIMARY_ASSOCIATION_QUALIFIER_FIELD = "subjectPrimaryAssociations[1]" + AssociationValidationHelper.ASSOCIATION_QUALIFIER_FIELD;
    private static final String SECOND_PRIMARY_ASSOCIATION_TARGET_FIELD = "subjectPrimaryAssociations[1]" + AssociationValidationHelper.ASSOCIATION_TARGET_FIELD;

    private static final String FIRST_SECONDARY_ASSOCIATION_QUALIFIER_FIELD = "subjectSecondaryAssociations[0]" + AssociationValidationHelper.ASSOCIATION_QUALIFIER_FIELD;
    private static final String FIRST_SECONDARY_ASSOCIATION_TARGET_FIELD = "subjectSecondaryAssociations[0]" + AssociationValidationHelper.ASSOCIATION_TARGET_FIELD;

    private static final String SECOND_SECONDARY_ASSOCIATION_QUALIFIER_FIELD = "subjectSecondaryAssociations[1]" + AssociationValidationHelper.ASSOCIATION_QUALIFIER_FIELD;
    private static final String SECOND_SECONDARY_ASSOCIATION_TARGET_FIELD = "subjectSecondaryAssociations[1]" + AssociationValidationHelper.ASSOCIATION_TARGET_FIELD;
}