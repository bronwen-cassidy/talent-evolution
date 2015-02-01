package com.zynap.talentstudio.web.common.validation;

import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.organisation.Node;

import com.zynap.talentstudio.web.organisation.NodeWrapperBean;
import com.zynap.talentstudio.web.organisation.associations.ArtefactAssociationWrapperBean;
import com.zynap.talentstudio.web.organisation.attributes.AttributeValueValidationFactory;
import com.zynap.talentstudio.web.organisation.attributes.AttributeValueValidationUtils;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Iterator;

/**
 * Common validator for Nodes - handles validation of dynamic attributes and of subject to position associations.
 * User: amark
 * Date: 09-Feb-2005
 * Time: 15:18:30
 */
public abstract class NodeValidator implements Validator {

    /**
     * Validate the dynamic attributes of the NodeWrapper.
     *
     * @param nodeWrapperBean The NodeWrapperBean
     * @param errors The Errors collection to add any validation errors to
     */
    public final void validateDynamicAttributes(NodeWrapperBean nodeWrapperBean, Errors errors) {
        AttributeValueValidationUtils.validateAttributes(factory, nodeWrapperBean.getWrappedDynamicAttributes(), errors, ATTRIBUTE_WRAPPERS_PREFIX, nodeWrapperBean.getId());
    }

    public final void setFactory(AttributeValueValidationFactory factory) {
        this.factory = factory;
    }

    /**
     * Validate subject to position secondary associations.
     * @param wrapper
     * @param errors
     */
    public final void validateSubjectSecondaryAssociations(NodeWrapperBean wrapper, Errors errors) {
        final Collection associations = wrapper.getSubjectSecondaryAssociations();
        validateAssociations(wrapper, errors, associations, IPopulationEngine.SUBJECT_SECONDARY_ASSOCIATIONS_ATTR);
    }

    /**
     * Validate subject to position primary associations.
     * @param wrapper
     * @param errors
     */
    public final void validateSubjectPrimaryAssociations(NodeWrapperBean wrapper, Errors errors) {
        Collection associations = wrapper.getSubjectPrimaryAssociations();
        validateAssociations(wrapper, errors, associations, IPopulationEngine.SUBJECT_PRIMARY_ASSOCIATIONS_ATTR);
    }

    /**
     * Overriden method that takes additional parameter to indicate if subject must have at least one primary association.
     *
     * @param wrapper
     * @param errors
     * @param allowOrphans
     */
    public final void validateSubjectPrimaryAssociations(NodeWrapperBean wrapper, Errors errors, boolean allowOrphans) {
        Collection associations = wrapper.getSubjectPrimaryAssociations();

        if (!allowOrphans && associations.isEmpty()) {
            errors.reject("error.association.primary.required", "You must specify at least one association.");    
        }

        validateSubjectPrimaryAssociations(wrapper, errors);
    }

    /**
     * Common method used to validate both subject to position primary and secondary associations.
     * <br/>  Checks that source and target and association qualifier have been set.
     * <br/>  Also checks for duplicates - for a secondary association a duplicate is an association that has the same qualifier and target.
     * <br/> For a primary association a duplicate is an association that has the same target regardless of the qualifier.
     *
     * @param wrapper
     * @param errors
     * @param associations
     * @param associationFieldName
     */
    private void validateAssociations(NodeWrapperBean wrapper, Errors errors, Collection associations, String associationFieldName) {
        int index = 0;
        boolean isSubjectNode = wrapper.getNode().getNodeType().equals(Node.SUBJECT_UNIT_TYPE_);
        for (Iterator iterator = associations.iterator(); iterator.hasNext(); index++) {
            ArtefactAssociationWrapperBean subjectAssociation = (ArtefactAssociationWrapperBean) iterator.next();

            final String key = associationFieldName + "[" + index + "]";

            final String comments = subjectAssociation.getComments();
            if(StringUtils.hasText(comments) && comments.length() > 3000) {
                errors.rejectValue(key + ".comments", "error.max.length.exceeded.3000");
            }

            AssociationValidationHelper.validateAssociation(wrapper, errors, key, isSubjectNode);

            if (errors.hasErrors()) {
                continue;
            }

            String associationTargetField = isSubjectNode ? AssociationValidationHelper.ASSOCIATION_TARGET_FIELD : AssociationValidationHelper.ASSOCIATION_SOURCE_FIELD;
            // only check for duplicate association targets if this is a primary association
            if (subjectAssociation.isPrimary() && AssociationValidationHelper.checkExists(associations, subjectAssociation, isSubjectNode)) {
                errors.rejectValue(key + associationTargetField, "error.association.target.unique.required", "The association target must be unique.");
            }

            if (subjectAssociation.isSecondary() && AssociationValidationHelper.checkDuplicateExists(associations, subjectAssociation, isSubjectNode)) {
                errors.rejectValue(key + associationTargetField, "error.association.unique.required", "The associations must be unique.");
            }
        }
    }

    protected AttributeValueValidationFactory factory;
    
    public static final String ATTRIBUTE_WRAPPERS_PREFIX = "wrappedDynamicAttributes";
}
