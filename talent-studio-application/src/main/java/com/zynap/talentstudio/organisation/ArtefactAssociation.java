/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation;

import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.positions.Position;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public abstract class ArtefactAssociation implements Serializable, IAssociation {

    /**
     * default constructor.
     */
    protected ArtefactAssociation() {
    }

    /**
     * Constructor.
     *
     * @param id
     * @param qualifier
     */
    protected ArtefactAssociation(Long id, LookupValue qualifier) {
        this.id = id;
        this.qualifier = qualifier;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LookupValue getQualifier() {
        return this.qualifier;
    }

    public void setQualifier(LookupValue qualifier) {
        this.qualifier = qualifier;
    }

    public abstract Node getSource();

    public abstract void setSource(Node source);

    public abstract Position getTarget();

    public abstract void setTarget(Position target);

    public boolean equals(Object o) {

        if (o.getClass() != this.getClass())
            return false;
        else {
            final ArtefactAssociation artefactAssociation = (ArtefactAssociation) o;
            if (this.getId() == null || artefactAssociation.getId() == null) return (this == artefactAssociation);
            return (this.getId().longValue() == artefactAssociation.getId().longValue());
        }
    }

    public int hashCode() {
        return (id != null ? id.hashCode() : super.hashCode());
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id: ", getId())
                .append("source: ", getSource())
                .append("target: ", getTarget())
                .toString();
    }

    public boolean hasPositionSource() {
        return getSource() instanceof Position;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public abstract boolean isPrimary();

    public abstract boolean isSecondary();

    public Long getSourceId() {
        // in the case of a new Node the source is the one being added hence it is null
        // as we store the object the id will get populated via pass by reference, hence this check
        if (sourceId == null) {
            sourceId = getSource() != null ? getSource().getId() : null;
        }
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    /**
     * identifier field.
     */
    protected Long id;
    /**
     * persistent field.
     */
    protected LookupValue qualifier;
    private String comments;
    protected Long sourceId;
    protected Long targetId;
}
