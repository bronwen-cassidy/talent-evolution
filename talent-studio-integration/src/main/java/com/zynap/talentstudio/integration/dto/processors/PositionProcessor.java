package com.zynap.talentstudio.integration.dto.processors;

import com.zynap.domain.IDomainObject;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.positions.PositionAssociation;

import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 26-Oct-2005
 * Time: 15:44:08
 * 
 */
public class PositionProcessor extends NodeProccesor implements IPostProcessor {

    public void process(IDomainObject domainObject, User user) {
        super.process(domainObject, user);
        Position position = (Position) domainObject;
        for (Iterator iterator = position.getSourceAssociations().iterator(); iterator.hasNext();) {
            PositionAssociation assoc = (PositionAssociation) iterator.next();
            assoc.setSource(position);
            assoc.setSourceId(position.getId());
            final Position target = assoc.getTarget();
            assoc.setTarget(new Position(target.getId()));
            assoc.setTargetId(target.getId());
            if (assoc.isPrimary()) {
                position.setParent(target);
                position.setParentId(target.getId());
            }
        }
    }
}
