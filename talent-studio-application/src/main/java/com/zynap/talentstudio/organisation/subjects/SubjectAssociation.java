package com.zynap.talentstudio.organisation.subjects;

import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.positions.Position;

public class SubjectAssociation extends SubjectCommonAssociation {

    /**
     * default constructor.
     */
    public SubjectAssociation() {
        super();
    }

    /**
     * Convenience constructor.
     */
    public SubjectAssociation(LookupValue qualifier, Subject subject, Position position) {
        super(null, qualifier, subject, position);
    }

    /**
     * full constructor.
     */
    public SubjectAssociation(Long id, LookupValue qualifier, Subject subject, Position position) {
        super(id, qualifier, subject, position);
    }

    /**
     * Is this a secondary association.
     *
     * @return true if the association has a qualifier and the qualifier type is ILookupManager.LOOKUP_TYPE_SECONDARY_SUBJECT_ASSOC.
     */
    public boolean isSecondary() {
        return qualifier != null && qualifier.getTypeId().equals(ILookupManager.LOOKUP_TYPE_SECONDARY_SUBJECT_ASSOC);
    }

    /**
     * Is this a primary association.
     *
     * @return true if the association has a qualifier and the qualifier type is ILookupManager.LOOKUP_TYPE_PRIMARY_SUBJECT_ASSOC.
     */
    public boolean isPrimary() {
        return qualifier != null && qualifier.getTypeId().equals(ILookupManager.LOOKUP_TYPE_PRIMARY_SUBJECT_ASSOC);
    }
}
