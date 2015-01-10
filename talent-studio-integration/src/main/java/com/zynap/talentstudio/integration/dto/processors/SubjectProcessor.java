package com.zynap.talentstudio.integration.dto.processors;

import com.zynap.domain.IDomainObject;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.organisation.subjects.SubjectAssociation;

import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 26-Oct-2005
 * Time: 15:44:08
 * To change this template use File | Settings | File Templates.
 */
public class SubjectProcessor extends NodeProccesor implements IPostProcessor {

    public void process(IDomainObject domainObject, User user) {
        super.process(domainObject, user);
        Subject subject = (Subject) domainObject;
        for (Iterator iterator = subject.getSubjectAssociations().iterator(); iterator.hasNext();) {
            SubjectAssociation assoc = (SubjectAssociation) iterator.next();
            assoc.setSource(subject);
            assoc.setTarget(new Position(assoc.getTarget().getId()));
        }
    }
}
