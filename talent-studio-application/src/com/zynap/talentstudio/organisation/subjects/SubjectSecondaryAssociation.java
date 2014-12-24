package com.zynap.talentstudio.organisation.subjects;

import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.positions.Position;

public class SubjectSecondaryAssociation extends SubjectCommonAssociation {

    /**
     * default constructor.
     */
    public SubjectSecondaryAssociation() {
        super();
    }

    /**
     * Convenience constructor.
     */
    public SubjectSecondaryAssociation(LookupValue qualifier, Subject subject, Position position) {
        super(null, qualifier, subject, position);
    }

    /**
     * full constructor.
     */
    public SubjectSecondaryAssociation(Long id, LookupValue qualifier, Subject subject, Position position) {
        super(id, qualifier, subject, position);
    }


    /**
     * Is this a secondary association.
     *
     * @return true always.
     */
    public boolean isSecondary() {
        return true;
    }

    /**
     * Is this a primary association.
     *
     * @return false always.
     */
    public boolean isPrimary() {
        return false;
    }
}
