/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.associations;

import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.ArtefactAssociation;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.organisation.subjects.SubjectCommonAssociation;
import com.zynap.talentstudio.util.IDomainUtils;

import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public final class ArtefactAssociationWrapperBean implements Serializable {

    public ArtefactAssociationWrapperBean(ArtefactAssociation association) {
        this.association = association;
        this.qualifierId = association.getQualifier().getId();
        this.targetId = association.getTarget().getId();
        this.sourceId = association.getSource() != null ? association.getSource().getId() : null;
    }

    public Long getId() {
        return association.getId();
    }

    public void resetId() {
        association.setId(null);
    }

    public Long getQualifierId() {
        return qualifierId;
    }

    public void setQualifierId(Long qualifierId) {
        this.qualifierId = qualifierId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public void setSourceLabel(String sourceLabel) {
        this.sourceLabel = sourceLabel;
    }

    public LookupValue getQualifier() {
        return association.getQualifier();
    }

    public Node getTarget() {
        return association.getTarget();
    }

    public Node getSource() {
        return association.getSource();
    }

    public boolean isPrimary() {
        return association.isPrimary();
    }

    public boolean isSecondary() {
        return association.isSecondary();
    }

    public boolean isQualifierSet() {
        return IDomainUtils.isValidId(qualifierId);
    }

    public boolean isTargetSet() {
        return IDomainUtils.isValidId(targetId);
    }

    public boolean isSourceSet() {
        return IDomainUtils.isValidId(sourceId);
    }

    public boolean isSubjectAssociation() {
        return association instanceof SubjectCommonAssociation;
    }

    public String getTargetLabel() {
        if (StringUtils.hasText(targetLabel)) return targetLabel;
        if (getTarget() != null) return getTarget().getLabel();
        return null;
    }

    public String getSourceLabel() {
        if (StringUtils.hasText(sourceLabel)) return sourceLabel;
        if (getSource() != null) return getSource().getLabel();
        return null;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setTargetLabel(String targetLabel) {
        this.targetLabel = targetLabel;
    }

    public void setComments(String comments) {
        association.setComments(comments);
    }

    public String getComments() {
        return association.getComments();
    }

    public ArtefactAssociation getModifiedAssociation() {

        final LookupValue qualifier = new LookupValue();
        qualifier.setId(qualifierId);
        qualifier.setTypeId(association.getQualifier().getTypeId());
        association.setQualifier(qualifier);

        Position target = new Position(targetId);
        if (isSubjectAssociation()) {
            Subject source = new Subject();
            source.setId(sourceId);
            association.setSource(source);
            association.setSourceId(sourceId);
        }
        association.setTarget(target);
        association.setTargetId(targetId);

        return association;
    }

    private Long sourceId;
    private String sourceLabel;

    /**
     * Qualifier id is copied from association in constructor so that it can be bound by Spring separately without altering the original object.
     */
    private Long qualifierId;

    /**
     * Target id is copied from association in constructor so that it can be bound by Spring separately without altering the original object.
     */
    private Long targetId;

    private String targetLabel;

    /**
     * The original association - intended to be readonly.
     */
    private final ArtefactAssociation association;
}
