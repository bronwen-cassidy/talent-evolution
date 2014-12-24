package com.zynap.talentstudio.web.organisation.positions;


import com.zynap.talentstudio.web.common.validation.AssociationValidationHelper;
import com.zynap.talentstudio.web.common.validation.NodeValidator;
import com.zynap.talentstudio.web.organisation.associations.ArtefactAssociationWrapperBean;
import com.zynap.web.utils.ZynapValidationUtils;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Collection;
import java.util.Iterator;

public class PositionValidator extends NodeValidator implements Validator {

    public boolean supports(Class aClass) {
        return PositionWrapperBean.class.isAssignableFrom(aClass);
    }

    public void validate(Object o, Errors errors) {
        validateRequiredValues((PositionWrapperBean) o, errors);
        validateAssociations((PositionWrapperBean) o, errors);
        validateSubjectPrimaryAssociations((PositionWrapperBean) o, errors);
        validateSubjectSecondaryAssociations((PositionWrapperBean) o, errors);
    }

    public void validateAssociations(PositionWrapperBean positionWrapperBean, Errors errors) {

        if (positionWrapperBean.isDefault()) return;

        // check primary association is set
        final String primaryAssociationKey = "primaryAssociation";
        AssociationValidationHelper.validateAssociation(positionWrapperBean, errors, primaryAssociationKey, true);

        // check primary association is not recursive
        final ArtefactAssociationWrapperBean primaryAssociation = positionWrapperBean.getPrimaryAssociation();
        if (AssociationValidationHelper.isRecursive(primaryAssociation)) {
            errors.rejectValue(primaryAssociationKey + AssociationValidationHelper.ASSOCIATION_TARGET_FIELD, "error.association.recursive", "An association between a position and itself is not allowed.");
        }

        // validate secondary position to position associations
        Collection secondary = positionWrapperBean.getSecondaryAssociations();
        int index = 0;
        for (Iterator iterator = secondary.iterator(); iterator.hasNext(); index++) {
            ArtefactAssociationWrapperBean positionAssociation = (ArtefactAssociationWrapperBean) iterator.next();

            final String key = "secondaryAssociations" + "[" + index + "]";
            AssociationValidationHelper.validateAssociation(positionWrapperBean, errors, key, true);

            if (errors.hasErrors()) {
                continue;
            }

            // check for duplicates of secondary associations (hence looking at the target nodes for duplicates)
            if (AssociationValidationHelper.checkExists(secondary, positionAssociation, true)) {
                errors.rejectValue(key + AssociationValidationHelper.ASSOCIATION_TARGET_FIELD, "error.association.target.unique.required", "The association target must be unique.");
            }

            // check is not association to self (recursive)
            if (AssociationValidationHelper.isRecursive(positionAssociation)) {
                errors.rejectValue(key + AssociationValidationHelper.ASSOCIATION_TARGET_FIELD, "error.association.recursive", "An association between a position and itself is not allowed.");
            }
        }
    }

    /**
     * Validation of field lengths are reflected with the column lengths defined in the database.
     * <p/>
     * A mechanism may be required where these lengths are validated dynamically in case of database changes
     * At present, Oracle does not have a method of determining which column has had its length exceeded
     *
     * @param position
     * @param errors
     */
    public void validateRequiredValues(PositionWrapperBean position, Errors errors) {
        if(position.getPosition().getOrganisationUnit().getId() == null) {
            // spring binds on an attribute in the column which holds the editable field, unfortunately this cannot be the id field so we need
            // to manually check and reject using the field spring is aware of (the label)
            errors.rejectValue("position.organisationUnit.label", "error.parent.orgunit.required", "You must select a parent organisation unit.");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "position.title", "error.title.required", "Title is a required field.");
        ZynapValidationUtils.rejectGreater200(errors, "position.title", "error.max.length.exceeded.200", "Title must be less than 200 characters.");
    }

    public void validateNonRecursiveReportingAssociation(PositionWrapperBean positionWrapperBean, Errors errors) {
        // check this is not the default position before getting any target associations
        if (positionWrapperBean.isDefault()) return;
        
        ArtefactAssociationWrapperBean primaryPosition=positionWrapperBean.getPrimaryAssociation();

        Long directingToId = primaryPosition.getTargetId();
        final Long positionid = positionWrapperBean.getPosition().getId();

        if(positionid.equals(directingToId)){
            //person can not report to itself primaryAssociation.targetId
            errors.rejectValue("primaryAssociation.targetId", "position.reports.toiteself");
            return;
        }
        for (Long descendant : positionWrapperBean.getDecendantIds()) {
            if(descendant.equals(directingToId)){
                // manager can not report to employee
                errors.rejectValue("primaryAssociation.targetId","position.reports.subpositions");
                return;
            }
        }
    }
}
