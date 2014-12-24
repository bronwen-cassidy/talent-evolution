package com.zynap.talentstudio.organisation.positions;

import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.ArtefactAssociation;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.OrganisationUnit;


public class PositionAssociation extends ArtefactAssociation {

    /**
     * default constructor.
     */
    public PositionAssociation() {
        super();
    }

    public PositionAssociation(LookupValue qualifier, Position target) {
        this(null, qualifier, null, target);
    }

    /**
     * full constructor.
     */
    public PositionAssociation(Long id, LookupValue qualifier, Position source, Position target) {
        super(id, qualifier);
        this.source = source;
        this.target = target;
        if(target != null) this.targetId = target.getId();
        if(source != null) this.sourceId = source.getId();
    }

    /**
     * Is this a secondary association.
     *
     * @return true if the association has a qualifier and the qualifier type is ILookupManager.LOOKUP_TYPE_SECONDARY_POSITION_ASSOC.
     */
    public boolean isSecondary() {
        return qualifier != null && qualifier.getTypeId().equals(ILookupManager.LOOKUP_TYPE_SECONDARY_POSITION_ASSOC);
    }

    /**
     * Is this a primary association.
     *
     * @return true if the association has a qualifier and the qualifier type is ILookupManager.LOOKUP_TYPE_PRIMARY_POSITION_ASSOC.
     */
    public boolean isPrimary() {
        return qualifier != null && qualifier.getTypeId().equals(ILookupManager.LOOKUP_TYPE_PRIMARY_POSITION_ASSOC);
    }

    public Node getSource() {
        return source;
    }

    public void setSource(Node source) {
        this.source = (Position) source;
    }

    public Position getTarget() {
        return target;
    }

    public void setTarget(Position target) {
        this.target = target;
    }

    public PositionAssociation createAuditable() {
        
        final OrganisationUnit sOrgUnit = source.getOrganisationUnit();
        OrganisationUnit sou = sOrgUnit != null ? new OrganisationUnit(sOrgUnit.getId(), sOrgUnit.getLabel()) : sOrgUnit;
        Position s = new Position(source.getId(), source.getTitle(), sou);

        final OrganisationUnit tou = target.getOrganisationUnit();
        OrganisationUnit tOrgunit = tou != null ? new OrganisationUnit(tou.getId(), tou.getLabel()) : tou;
        Position t = new Position(target.getId(), target.getTitle(), tOrgunit);

        return new PositionAssociation(id, qualifier.createAuditable(), s, t);
    }

    private Position source;
    private Position target;
}
