package com.zynap.talentstudio.organisation.subjects;

import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.ArtefactAssociation;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.positions.Position;

public abstract class SubjectCommonAssociation extends ArtefactAssociation {

    /**
     * default constructor.
     */
    public SubjectCommonAssociation() {
        super();
    }

    /**
     * Convenience constructor.
     */
    public SubjectCommonAssociation(LookupValue qualifier, Subject subject, Position position) {
        this(null, qualifier, subject, position);
    }

    /**
     * full constructor.
     */
    public SubjectCommonAssociation(Long id, LookupValue qualifier, Subject subject, Position position) {
        super(id, qualifier);
        this.subject = subject;
        this.position = position;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Node getSource() {
        return subject;
    }

    public void setSource(Node source) {
        this.subject = (Subject) source;
    }

    public Position getTarget() {
        return position;
    }

    public void setTarget(Position target) {
        this.position = target;
    }

    public SubjectAssociation createAuditable() {

        final OrganisationUnit orgUnit = position.getOrganisationUnit();

        final Subject s = new Subject(subject.getId(), subject.getCoreDetail());
        final Position p = new Position(position.getId(), position.getTitle());

        if (orgUnit != null) {
            p.setOrganisationUnit(new OrganisationUnit(orgUnit.getId(), orgUnit.getLabel()));
        }
        return new SubjectAssociation(id, qualifier.createAuditable(), s, p);
    }

    private Subject subject;
    private Position position;
}
