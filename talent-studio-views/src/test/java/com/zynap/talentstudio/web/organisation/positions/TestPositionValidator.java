package com.zynap.talentstudio.web.organisation.positions;


import junit.framework.TestCase;

import com.zynap.domain.IDomainObject;
import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.positions.PositionAssociation;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.validation.AssociationValidationHelper;
import com.zynap.talentstudio.web.organisation.associations.ArtefactAssociationWrapperBean;
import com.zynap.talentstudio.web.controller.ZynapDefaultFormController;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.List;

/**
* Class or Interface description.
*
* @author bcassidy
* @since 02-Sep-2004 17:15:37
* @version 0.1
*/
public class TestPositionValidator extends TestCase {

    protected void setUp() throws Exception {

        positionValidator = new PositionValidator();
        Position p = new Position();
        p.setParent(new Position(new Long(1)));
        positionWrapperBean = new PositionWrapperBean(p);
        PositionAssociation positionAssociation = new PositionAssociation(PRIMARY_LOOKUP_VALUE, new Position(new Long(-1)));
        positionWrapperBean.setPrimaryAssociation(positionAssociation);
    }

    public void testValidatePrimaryAssociationNoTarget() {

        DataBinder binder = new DataBinder(positionWrapperBean, ZynapDefaultFormController.DEFAULT_COMMAND_NAME);
        MutablePropertyValues pvs = new MutablePropertyValues();
        pvs.addPropertyValue(new PropertyValue(PRIMARY_ASSOCIATION_QUALIFIER_FIELD, new Long(10)));
        pvs.addPropertyValue(new PropertyValue(PRIMARY_ASSOCIATION_TARGET_FIELD, IDomainObject.UNASSIGNED_VALUE));
        binder.bind(pvs);
        Errors errors = binder.getBindingResult();
        positionValidator.validateAssociations(positionWrapperBean, errors);
        try {
            binder.close();
            fail("Should have thrown BindException");
        } catch (BindException ex) {
            FieldError labelError = ex.getFieldError(PRIMARY_ASSOCIATION_TARGET_FIELD);
            assertEquals(IDomainObject.UNASSIGNED_VALUE, labelError.getRejectedValue());
            assertEquals("error.association.target.required", labelError.getCode());
        }
    }

    public void testValidatePrimaryAssociationNoQualifier() {

        DataBinder binder = new DataBinder(positionWrapperBean, ZynapDefaultFormController.DEFAULT_COMMAND_NAME);
        MutablePropertyValues pvs = new MutablePropertyValues();
        pvs.addPropertyValue(new PropertyValue(PRIMARY_ASSOCIATION_TARGET_FIELD, new Long(10)));
        pvs.addPropertyValue(new PropertyValue(PRIMARY_ASSOCIATION_QUALIFIER_FIELD, IDomainObject.UNASSIGNED_VALUE));
        binder.bind(pvs);
        Errors errors = binder.getBindingResult();
        positionValidator.validateAssociations(positionWrapperBean, errors);
        try {
            binder.close();
            fail("Should have thrown BindException");
        } catch (BindException ex) {
            FieldError labelError = ex.getFieldError(PRIMARY_ASSOCIATION_QUALIFIER_FIELD);
            assertEquals(IDomainObject.UNASSIGNED_VALUE, labelError.getRejectedValue());
            assertEquals("error.association.qualifier.required", labelError.getCode());
        }
    }

    /**
     * Subject association added to the position binds to source id
     * @throws Exception
     */
    public void testValidateSubjectPrimaryAssociationsDuplicate() throws Exception {
        positionWrapperBean.addNewSubjectPrimaryAssociation();
        positionWrapperBean.addNewSubjectPrimaryAssociation();
        List subjectPrimaryAssociations = positionWrapperBean.getSubjectPrimaryAssociations();
        for (int i = 0; i < subjectPrimaryAssociations.size(); i++) {
            ArtefactAssociationWrapperBean artefactAssociationWrapperBean = (ArtefactAssociationWrapperBean) subjectPrimaryAssociations.get(i);
            artefactAssociationWrapperBean.setSourceId(new Long(12));
            artefactAssociationWrapperBean.setQualifierId(new Long(399));
        }
        // should produce a duplicate assocition error
        DataBinder binder = new DataBinder(positionWrapperBean, ZynapDefaultFormController.DEFAULT_COMMAND_NAME);
        Errors errors = binder.getBindingResult();
        positionValidator.validateSubjectPrimaryAssociations(positionWrapperBean, errors);
        assertEquals(1, errors.getErrorCount());
        FieldError error = errors.getFieldError(FIRST_PRIMARY_ASSOCIATION_SOURCE_FIELD);
        assertEquals("error.association.target.unique.required", error.getCode());
    }

    public void testValidateSubjectPrimaryAssociationsTargetsSame() throws Exception {
        positionWrapperBean.addNewSubjectPrimaryAssociation();
        positionWrapperBean.addNewSubjectPrimaryAssociation();

        List subjectPrimaryAssociations = positionWrapperBean.getSubjectPrimaryAssociations();
        ArtefactAssociationWrapperBean artefactAssociationWrapperBean1 = (ArtefactAssociationWrapperBean) subjectPrimaryAssociations.get(0);
        artefactAssociationWrapperBean1.setQualifierId(new Long(399));
        artefactAssociationWrapperBean1.setSourceId(new Long(12));
        ArtefactAssociationWrapperBean artefactAssociationWrapperBean2 = (ArtefactAssociationWrapperBean) subjectPrimaryAssociations.get(1);
        artefactAssociationWrapperBean2.setQualifierId(new Long(304));
        artefactAssociationWrapperBean2.setSourceId(new Long(12));

        // should produce a duplicate assocition error
        DataBinder binder = new DataBinder(positionWrapperBean, ZynapDefaultFormController.DEFAULT_COMMAND_NAME);
        Errors errors = binder.getBindingResult();
        positionValidator.validateSubjectPrimaryAssociations(positionWrapperBean, errors);
        assertEquals(1, errors.getErrorCount());
        FieldError error = errors.getFieldError(FIRST_PRIMARY_ASSOCIATION_SOURCE_FIELD);
        assertEquals("error.association.target.unique.required", error.getCode());
    }

    public void testValidateSubjectSecondaryAssociationsSame() throws Exception {
        positionWrapperBean.addNewSubjectSecondaryAssociation();
        positionWrapperBean.addNewSubjectSecondaryAssociation();
        List subjectPrimaryAssociations = positionWrapperBean.getSubjectSecondaryAssociations();
        for (int i = 0; i < subjectPrimaryAssociations.size(); i++) {
            ArtefactAssociationWrapperBean artefactAssociationWrapperBean = (ArtefactAssociationWrapperBean) subjectPrimaryAssociations.get(i);
            artefactAssociationWrapperBean.setSourceId(new Long(12));
            artefactAssociationWrapperBean.setQualifierId(new Long(399));
        }
        // should produce a duplicate assocition error
        DataBinder binder = new DataBinder(positionWrapperBean, ZynapDefaultFormController.DEFAULT_COMMAND_NAME);
        Errors errors = binder.getBindingResult();
        positionValidator.validateSubjectSecondaryAssociations(positionWrapperBean, errors);
        assertEquals(1, errors.getErrorCount());
        FieldError error = errors.getFieldError(FIRST_SECONDARY_ASSOCIATION_SOURCE_FIELD);
        assertEquals("error.association.unique.required", error.getCode());
    }

    public void testValidateSubjectSecondaryAssociationsNoErrors() throws Exception {
        positionWrapperBean.addNewSubjectSecondaryAssociation();
        positionWrapperBean.addNewSubjectSecondaryAssociation();
        List subjectSecondaryAssociations = positionWrapperBean.getSubjectSecondaryAssociations();

        // same source different qualifiers should be fine
        ArtefactAssociationWrapperBean artefactAssociationWrapperBean1 = (ArtefactAssociationWrapperBean) subjectSecondaryAssociations.get(0);
        artefactAssociationWrapperBean1.setQualifierId(new Long(399));
        artefactAssociationWrapperBean1.setSourceId(new Long(12));
        ArtefactAssociationWrapperBean artefactAssociationWrapperBean2 = (ArtefactAssociationWrapperBean) subjectSecondaryAssociations.get(1);
        artefactAssociationWrapperBean2.setQualifierId(new Long(304));
        artefactAssociationWrapperBean2.setSourceId(new Long(12));

        // should produce a duplicate assocition error
        DataBinder binder = new DataBinder(positionWrapperBean, ZynapDefaultFormController.DEFAULT_COMMAND_NAME);
        Errors errors = binder.getBindingResult();
        positionValidator.validateSubjectSecondaryAssociations(positionWrapperBean, errors);
        assertFalse("should not have had any errors", errors.hasErrors());
    }

    public void testNoPrimaryAssociation() {

        DataBinder binder = new DataBinder(positionWrapperBean, ZynapDefaultFormController.DEFAULT_COMMAND_NAME);
        MutablePropertyValues pvs = new MutablePropertyValues();
        pvs.addPropertyValue(new PropertyValue(PRIMARY_ASSOCIATION_QUALIFIER_FIELD, IDomainObject.UNASSIGNED_VALUE));
        pvs.addPropertyValue(new PropertyValue(PRIMARY_ASSOCIATION_TARGET_FIELD, IDomainObject.UNASSIGNED_VALUE));
        binder.bind(pvs);

        positionValidator.validateAssociations(positionWrapperBean, binder.getBindingResult());
        Errors errors = binder.getBindingResult();
        assertEquals(2, errors.getErrorCount());

        assertEquals("error.association.qualifier.required", errors.getFieldError(PRIMARY_ASSOCIATION_QUALIFIER_FIELD).getCode());
        assertEquals("error.association.target.required", errors.getFieldError(PRIMARY_ASSOCIATION_TARGET_FIELD).getCode());
    }

    public void testUnSelectedSecondaryAssociations() throws Exception {

        positionWrapperBean.addNewSecondaryAssociation();
        positionWrapperBean.addNewSecondaryAssociation();
        positionWrapperBean.getPosition().setId(new Long(20));
        List secondaryAssociations = positionWrapperBean.getSecondaryAssociations();
        for (int i = 0; i < secondaryAssociations.size(); i++) {
            ArtefactAssociationWrapperBean artefactAssociationWrapperBean = (ArtefactAssociationWrapperBean) secondaryAssociations.get(i);
            artefactAssociationWrapperBean.setSourceId(new Long(20));
        }
        positionWrapperBean.getPrimaryAssociation().setSourceId(new Long(20));

        // set primary association as we are testing secondary associations
        DataBinder binder = new DataBinder(positionWrapperBean, ZynapDefaultFormController.DEFAULT_COMMAND_NAME);
        MutablePropertyValues pvs = new MutablePropertyValues();
        setPrimaryAssociation(pvs);

        // should now have errors for unselected secondary associations - two errors expected per association
        binder.bind(pvs);
        Errors errors = binder.getBindingResult();
        positionValidator.validateAssociations(positionWrapperBean, errors);
        assertEquals((positionWrapperBean.getSecondaryAssociations().size() * 2), errors.getErrorCount());
    }

    public void testRecursiveSecondaryAssociation() {

        positionWrapperBean.getPosition().setId(new Long(20));
        positionWrapperBean.getPrimaryAssociation().setSourceId(new Long(20));
        positionWrapperBean.addNewSecondaryAssociation();

        DataBinder binder = new DataBinder(positionWrapperBean, ZynapDefaultFormController.DEFAULT_COMMAND_NAME);
        MutablePropertyValues pvs = new MutablePropertyValues();
        setPrimaryAssociation(pvs);

        // add a secondary association to self - not allowed
        pvs.addPropertyValue(new PropertyValue(FIRST_SECONDARY_ASSOCIATION_QUALIFIER_FIELD, new Long(10)));
        pvs.addPropertyValue(new PropertyValue(FIRST_SECONDARY_ASSOCIATION_TARGET_FIELD, positionWrapperBean.getPosition().getId()));

        binder.bind(pvs);
        Errors errors = binder.getBindingResult();
        positionValidator.validateAssociations(positionWrapperBean, errors);
        try {
            binder.close();
            fail("Should have thrown BindException");
        } catch (BindException ex) {
            FieldError labelError = ex.getFieldError(FIRST_SECONDARY_ASSOCIATION_TARGET_FIELD);
            assertEquals("error.association.recursive", labelError.getCode());
        }
    }

    public void testValidateSecondaryAssociationsDuplicateTargetAndQualifier() {

        // check that duplicate targets and duplicate qualifiers are allowed - 2ndary associations can be of duplicate type ("Emergency")
        positionWrapperBean.addNewSecondaryAssociation();
        positionWrapperBean.addNewSecondaryAssociation();

        DataBinder binder = new DataBinder(positionWrapperBean, ZynapDefaultFormController.DEFAULT_COMMAND_NAME);
        MutablePropertyValues pvs = new MutablePropertyValues();
        setPrimaryAssociation(pvs);
        pvs.addPropertyValue(new PropertyValue(FIRST_SECONDARY_ASSOCIATION_TARGET_FIELD, new Long(33)));
        pvs.addPropertyValue(new PropertyValue(FIRST_SECONDARY_ASSOCIATION_QUALIFIER_FIELD, new Long(33)));
        pvs.addPropertyValue(new PropertyValue(SECOND_SECONDARY_ASSOCIATION_TARGET_FIELD, new Long(33)));
        pvs.addPropertyValue(new PropertyValue(SECOND_SECONDARY_ASSOCIATION_QUALIFIER_FIELD, new Long(33)));
        binder.bind(pvs);

        Errors errors = binder.getBindingResult();
        assertFalse(errors.hasErrors());
    }

    public void testValidateSecondaryAssociationsInvalidQualifier() {

        positionWrapperBean.addNewSecondaryAssociation();
        positionWrapperBean.addNewSecondaryAssociation();

        DataBinder binder = new DataBinder(positionWrapperBean, ZynapDefaultFormController.DEFAULT_COMMAND_NAME);
        MutablePropertyValues pvs = new MutablePropertyValues();

        pvs.addPropertyValue(new PropertyValue(FIRST_SECONDARY_ASSOCIATION_QUALIFIER_FIELD, new Long(33)));
        pvs.addPropertyValue(new PropertyValue(FIRST_SECONDARY_ASSOCIATION_TARGET_FIELD, new Long(33)));

        pvs.addPropertyValue(new PropertyValue(SECOND_SECONDARY_ASSOCIATION_QUALIFIER_FIELD, IDomainObject.UNASSIGNED_VALUE));
        pvs.addPropertyValue(new PropertyValue(SECOND_SECONDARY_ASSOCIATION_TARGET_FIELD, new Long(33)));

        setPrimaryAssociation(pvs);
        binder.bind(pvs);

        Errors errors = binder.getBindingResult();
        positionValidator.validateAssociations(positionWrapperBean, errors);
        try {
            binder.close();
            fail("Should have thrown BindException");
        } catch (BindException ex) {
            // check that null target on secondary association 2 was rejected
            assertEquals(1, ex.getErrorCount());
            FieldError labelError = ex.getFieldError(SECOND_SECONDARY_ASSOCIATION_QUALIFIER_FIELD);
            assertEquals(IDomainObject.UNASSIGNED_VALUE, labelError.getRejectedValue());
            assertEquals("error.association.qualifier.required", labelError.getCode());
        }
    }

    public void testValidateAssociationsDefaultPosition() {

        positionWrapperBean = new PositionWrapperBean(new Position(new Long(10)));
        assertTrue(positionWrapperBean.isDefault());

        // no validation of associations for default position
        BindException errors = new BindException(positionWrapperBean, ControllerConstants.COMMAND_NAME);
        positionValidator.validateAssociations(positionWrapperBean, errors);
        assertFalse(errors.hasErrors());
    }

    public void testValidateSecondaryAssociationsInvalidTarget() {

        positionWrapperBean.addNewSecondaryAssociation();
        positionWrapperBean.addNewSecondaryAssociation();
        positionWrapperBean.addNewSecondaryAssociation();

        DataBinder binder = new DataBinder(positionWrapperBean, ZynapDefaultFormController.DEFAULT_COMMAND_NAME);
        MutablePropertyValues pvs = new MutablePropertyValues();

        // add the required primary association for the position
        setPrimaryAssociation(pvs);

        pvs.addPropertyValue(new PropertyValue(FIRST_SECONDARY_ASSOCIATION_TARGET_FIELD, new Long(33)));
        pvs.addPropertyValue(new PropertyValue(FIRST_SECONDARY_ASSOCIATION_QUALIFIER_FIELD, new Long(33)));
        pvs.addPropertyValue(new PropertyValue(SECOND_SECONDARY_ASSOCIATION_TARGET_FIELD, new Long(22)));
        pvs.addPropertyValue(new PropertyValue(SECOND_SECONDARY_ASSOCIATION_QUALIFIER_FIELD, new Long(22)));
        pvs.addPropertyValue(new PropertyValue(THIRD_SECONDARY_ASSOCIATION_TARGET_FIELD, IDomainObject.UNASSIGNED_VALUE));
        pvs.addPropertyValue(new PropertyValue(THIRD_SECONDARY_ASSOCIATION_QUALIFIER_FIELD, new Long(22)));
        binder.bind(pvs);

        Errors errors = binder.getBindingResult();
        positionValidator.validateAssociations(positionWrapperBean, errors);
        try {
            binder.close();
            fail("Should have thrown BindException");
        } catch (BindException ex) {
            // check that null target on secondary association 2 was rejected
            assertEquals(1, ex.getErrorCount());
            FieldError labelError = ex.getFieldError(THIRD_SECONDARY_ASSOCIATION_TARGET_FIELD);
            assertEquals(IDomainObject.UNASSIGNED_VALUE, labelError.getRejectedValue());
            assertEquals("error.association.target.required", labelError.getCode());
        }
    }

    /**
     * Binds on the target!
     * @throws Exception
     */
    public void testValidatePositionSecondaryAssociations() throws Exception {
        positionWrapperBean.addNewSecondaryAssociation();
        positionWrapperBean.addNewSecondaryAssociation();
        DataBinder binder = new DataBinder(positionWrapperBean, ZynapDefaultFormController.DEFAULT_COMMAND_NAME);
        MutablePropertyValues pvs = new MutablePropertyValues();

        // add the required primary association for the position
        setPrimaryAssociation(pvs);

        pvs.addPropertyValue(new PropertyValue(FIRST_SECONDARY_ASSOCIATION_TARGET_FIELD, new Long(33)));
        pvs.addPropertyValue(new PropertyValue(FIRST_SECONDARY_ASSOCIATION_QUALIFIER_FIELD, new Long(33)));

        pvs.addPropertyValue(new PropertyValue(SECOND_SECONDARY_ASSOCIATION_TARGET_FIELD, new Long(22)));
        pvs.addPropertyValue(new PropertyValue(SECOND_SECONDARY_ASSOCIATION_QUALIFIER_FIELD, new Long(22)));
        binder.bind(pvs);

        Errors errors = binder.getBindingResult();
        positionValidator.validateAssociations(positionWrapperBean, errors);
        try {
            binder.close();
        } catch (BindException ex) {
            ex.printStackTrace();
            fail("Should not have thrown BindException");
        }
        assertFalse(errors.hasErrors());
    }

    public void testValidate() throws Exception {

        positionWrapperBean.addNewSecondaryAssociation();

        DataBinder binder = new DataBinder(positionWrapperBean, ZynapDefaultFormController.DEFAULT_COMMAND_NAME);

        Errors errors = binder.getBindingResult();
        positionValidator.validateDynamicAttributes(positionWrapperBean, errors);

        // check that there are no errors as default validation only checks dynamic attributes, not association or core details
        assertFalse(errors.hasErrors());
    }

    public void testSupports() throws Exception {
        positionValidator = new PositionValidator();
        assertTrue(positionValidator.supports(PositionWrapperBean.class));
    }

    public void testValidateRequiredValues() throws Exception {

        final String idField = "position.organisationUnit.id";
        final String bindField = "position.organisationUnit.label";
        final String positionField = "position.title";

        Position position = new Position();
        position.setOrganisationUnit(new OrganisationUnit());
        positionWrapperBean = new PositionWrapperBean(position);

        DataBinder binder = new DataBinder(positionWrapperBean, ZynapDefaultFormController.DEFAULT_COMMAND_NAME);
        MutablePropertyValues mpv = new MutablePropertyValues();
        mpv.addPropertyValue(idField, null);
        mpv.addPropertyValue(positionField, null);
        binder.bind(mpv);

        Errors errors = binder.getBindingResult();
        positionValidator.validateRequiredValues(positionWrapperBean, errors);
        try {
            binder.close();
            fail("Should have thrown BindException");
        } catch (BindException ex) {

            assertEquals(2, ex.getErrorCount());

            List idErrors = ex.getFieldErrors(bindField);
            assertEquals(1, idErrors.size());

            List positionErrors = ex.getFieldErrors(positionField);
            assertEquals(1, positionErrors.size());
        }
    }

    private void setPrimaryAssociation(MutablePropertyValues pvs) {
        pvs.addPropertyValue(new PropertyValue(PRIMARY_ASSOCIATION_TARGET_FIELD, new Long(10)));
        pvs.addPropertyValue(new PropertyValue(PRIMARY_ASSOCIATION_QUALIFIER_FIELD, new Long(10)));
    }

    private PositionValidator positionValidator;
    private PositionWrapperBean positionWrapperBean;

    private static final LookupValue PRIMARY_LOOKUP_VALUE = new LookupValue("DIRECT", ILookupManager.LOOKUP_TYPE_PRIMARY_POSITION_ASSOC, "Direct", "description");

    private static final String PRIMARY_ASSOCIATION_QUALIFIER_FIELD = "primaryAssociation" + AssociationValidationHelper.ASSOCIATION_QUALIFIER_FIELD;
    private static final String FIRST_SECONDARY_ASSOCIATION_QUALIFIER_FIELD = "secondaryAssociations[0]" + AssociationValidationHelper.ASSOCIATION_QUALIFIER_FIELD;
    private static final String SECOND_SECONDARY_ASSOCIATION_QUALIFIER_FIELD = "secondaryAssociations[1]" + AssociationValidationHelper.ASSOCIATION_QUALIFIER_FIELD;
    private static final String THIRD_SECONDARY_ASSOCIATION_QUALIFIER_FIELD = "secondaryAssociations[2]" + AssociationValidationHelper.ASSOCIATION_QUALIFIER_FIELD;

    private static final String PRIMARY_ASSOCIATION_TARGET_FIELD = "primaryAssociation" + AssociationValidationHelper.ASSOCIATION_TARGET_FIELD;
    private static final String FIRST_PRIMARY_ASSOCIATION_SOURCE_FIELD = "subjectPrimaryAssociations[0]" + AssociationValidationHelper.ASSOCIATION_SOURCE_FIELD;
    private static final String FIRST_SECONDARY_ASSOCIATION_TARGET_FIELD = "secondaryAssociations[0]" + AssociationValidationHelper.ASSOCIATION_TARGET_FIELD;
    private static final String FIRST_SECONDARY_ASSOCIATION_SOURCE_FIELD = "subjectSecondaryAssociations[0]" + AssociationValidationHelper.ASSOCIATION_SOURCE_FIELD;
    private static final String SECOND_SECONDARY_ASSOCIATION_TARGET_FIELD = "secondaryAssociations[1]" + AssociationValidationHelper.ASSOCIATION_TARGET_FIELD;
    private static final String THIRD_SECONDARY_ASSOCIATION_TARGET_FIELD = "secondaryAssociations[2]" + AssociationValidationHelper.ASSOCIATION_TARGET_FIELD;
}
