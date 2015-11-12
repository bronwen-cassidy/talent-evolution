/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.common.validation;

import com.zynap.talentstudio.web.organisation.NodeWrapperBean;
import com.zynap.talentstudio.web.organisation.associations.ArtefactAssociationWrapperBean;
import com.zynap.talentstudio.web.utils.ZynapValidationUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.validation.Errors;

import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public final class AssociationValidationHelper {

    /**
     * Is this a complete association.
     * <br> An association is considered complete if it has a qualifier and a target assigned.
     * <br> NOTE: We do not check if the source is assigned as a new association has a source which is unsaved and therefore
     * is not "real" yet.
     *
     * @param artefactAssociation
     * @param isTargetNode
     * @return true if the association is complete, otherwise false.
     */
    public static boolean isComplete(ArtefactAssociationWrapperBean artefactAssociation, boolean isTargetNode) {
        return artefactAssociation.isQualifierSet() && (isTargetNode ? artefactAssociation.isTargetSet() : artefactAssociation.isSourceSet());
    }

    /**
     * Check that source and target are not the same.
     *
     * @param artefactAssociation
     * @return true if the source and target are the same, otherwise false.
     */
    public static boolean isRecursive(ArtefactAssociationWrapperBean artefactAssociation) {

        if (artefactAssociation.isSourceSet() && artefactAssociation.isTargetSet()) {
            final Long sourceId = artefactAssociation.getSource().getId();
            final Long targetId = artefactAssociation.getTargetId();

            return (sourceId.equals(targetId));
        }

        return false;
    }

    public static boolean checkExists(Collection associations, final ArtefactAssociationWrapperBean associationToFind, final boolean isTargetNode) {
        int numberOfMatches = CollectionUtils.countMatches(associations, new Predicate() {
            public boolean evaluate(Object object) {
                ArtefactAssociationWrapperBean association = (ArtefactAssociationWrapperBean) object;

                // if we are validating the subject node we are looking at the position's id or the targetId
                final Long targetId = isTargetNode ? association.getTargetId() : association.getSourceId();
                final Long targetIdToFind = isTargetNode ? associationToFind.getTargetId() : associationToFind.getSourceId();

                // only considered as duplicate if complete and if targets match
                return (isComplete(association, isTargetNode) && isComplete(associationToFind, isTargetNode) && targetId.equals(targetIdToFind));
            }
        });

        return (numberOfMatches > 1);
    }

    public static void validateAssociation(NodeWrapperBean wrapper, Errors errors, final String key, boolean isTargetValidation) {
        ZynapValidationUtils.rejectIfUnassigned(errors, key + ASSOCIATION_QUALIFIER_FIELD, "error.association.qualifier.required", "Association qualifier is required", wrapper);
        if (isTargetValidation) {
            ZynapValidationUtils.rejectIfUnassigned(errors, key + ASSOCIATION_TARGET_FIELD, "error.association.target.required", "Association target is required", wrapper);
        } else {
            ZynapValidationUtils.rejectIfUnassigned(errors, key + ASSOCIATION_SOURCE_FIELD, "error.association.target.required", "Association target is required", wrapper);
        }
        // only validate the source association if the wrappers node has an id i.e only in an edit flow
        if (wrapper.getNode().getId() != null) {
            ZynapValidationUtils.rejectIfUnassigned(errors, key + ASSOCIATION_SOURCE_FIELD, "error.association.target.required", "Association target is required", wrapper);
        }
    }

    public static boolean checkDuplicateExists(Collection associations, final ArtefactAssociationWrapperBean associationToFind, final boolean isSubjectNode) {
        int numberOfMatches = CollectionUtils.countMatches(associations, new Predicate() {
            public boolean evaluate(Object object) {
                ArtefactAssociationWrapperBean association = (ArtefactAssociationWrapperBean) object;

                final Long qualifierId = association.getQualifierId();
                final Long qualifierIdToFind = associationToFind.getQualifierId();
                final Long targetId = isSubjectNode ? association.getTargetId() : association.getSourceId();
                final Long targetIdToFind = isSubjectNode ? associationToFind.getTargetId() : associationToFind.getSourceId();

                // only considered as duplicate if complete, if targets match and qualifiers match
                return (isComplete(association, isSubjectNode) && isComplete(associationToFind, isSubjectNode) && targetId.equals(targetIdToFind) && qualifierId.equals(qualifierIdToFind));
            }
        });

        return (numberOfMatches > 1);
    }

    public static final String ASSOCIATION_QUALIFIER_FIELD = ".qualifierId";
    public static final String ASSOCIATION_TARGET_FIELD = ".targetId";
    public static final String ASSOCIATION_SOURCE_FIELD = ".sourceId";
}
