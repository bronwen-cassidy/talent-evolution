package com.zynap.talentstudio.organisation.subjects;

import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.positions.Position;

public class SubjectPrimaryAssociation extends SubjectCommonAssociation {

    /**
     * default constructor.
     */
    public SubjectPrimaryAssociation() {
        super();
    }

    /**
     * Convenience constructor.
     */
    public SubjectPrimaryAssociation(LookupValue qualifier, Subject subject, Position position) {
        super(null, qualifier, subject, position);
    }

    /**
     * full constructor.
     */
    public SubjectPrimaryAssociation(Long id, LookupValue qualifier, Subject subject, Position position) {
        super(id, qualifier, subject, position);
    }


    /**
     * Is this a secondary association.
     *
     * @return false always.
     */
    public boolean isSecondary() {
        return false;
    }

    /**
     * Is this a primary association.
     *
     * @return true always.
     */
    public boolean isPrimary() {
        return true;
    }

    public String getRowid() {
        return rowid;
    }

    public void setRowid(String rowid) {
        this.rowid = rowid;
    }

    private String rowid;
}
