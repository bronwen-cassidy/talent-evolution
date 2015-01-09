package com.zynap.talentstudio.security;

import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.positions.PositionAssociation;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.organisation.subjects.SubjectAssociation;

import org.springframework.orm.hibernate.HibernateTemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: amark
 * Date: 20-May-2005
 * Time: 16:55:04
 */
public final class SecurityHelper {

    /**
     * Check access to any node given a Node, a user and a permit.
     * <br> Sets the "hasAccess" attribute of the Node to true if the user has access.
     *
     * @param node
     * @param principalId
     * @param permitId
     * @param hibernateTemplate
     */
    public static void checkNodeAccess(Node node, Long principalId, Long permitId, HibernateTemplate hibernateTemplate) {
        if (!node.isHasAccess()) {
            final List results = hibernateTemplate.findByNamedQuery("findDomainObjectPermitForUserAndNode", new Object[]{permitId, principalId, node.getId()});
            node.setHasAccess(!results.isEmpty());
        }
    }

    /**
     * Check access to subject.
     * <br> Checks access to the subject itself, the position's org unit and the positions associated with the subject.
     *
     * @param subject              The subject
     * @param principalId
     * @param viewSubjectPermitId
     * @param viewPositionPermitId
     * @param hibernateTemplate
     */
    public static void checkSubjectAccess(Subject subject, Long principalId, Long viewSubjectPermitId, Long viewPositionPermitId,
                                          HibernateTemplate hibernateTemplate) {

        // check access to position
        checkNodeAccess(subject, principalId, viewSubjectPermitId, hibernateTemplate);

        // check access for associations
        checkSubjectAssociationTargetAccess(subject, principalId, viewPositionPermitId, hibernateTemplate);
    }

    /**
     * Check access to position.
     * <br> Checks access to the position itself, the position's org unit and the source and target associations for the position.
     *
     * @param position             The position
     * @param principalId
     * @param viewPositionPermitId
     * @param viewOrgUnitPermitId
     * @param viewSubjectPermitId
     * @param hibernateTemplate
     */
    public static void checkPositionAccess(Position position, Long principalId, Long viewPositionPermitId,
                                           Long viewOrgUnitPermitId, Long viewSubjectPermitId, HibernateTemplate hibernateTemplate) {

        // check access to position itself
        checkNodeAccess(position, principalId, viewPositionPermitId, hibernateTemplate);

        // check access to its subjects and its orgunit
        checkPositionAssociatedArtefacts(position, principalId, viewOrgUnitPermitId, viewSubjectPermitId, hibernateTemplate);

        // check access for position to position associations
        checkPositionAssociationTargetAccess(position, principalId, viewPositionPermitId, viewOrgUnitPermitId, viewSubjectPermitId, hibernateTemplate);
    }

    /**
     * Check access to the position's associated subjects and the org unit it belongs to.
     *
     * @param position
     * @param principalId
     * @param viewOrgUnitPermitId
     * @param viewSubjectPermitId
     */
    private static void checkPositionAssociatedArtefacts(Position position, Long principalId,
                                                         Long viewOrgUnitPermitId, Long viewSubjectPermitId, HibernateTemplate hibernateTemplate) {

        // check access for position org unit
        checkNodeAccess(position.getOrganisationUnit(), principalId, viewOrgUnitPermitId, hibernateTemplate);

        // check access for subject to position associations
        checkSubjectAssociationSourceAccess(position, principalId, viewSubjectPermitId, hibernateTemplate);
    }

    /**
     * Check access to position's associated positions.
     * <br> Sets the "hasAccess" attribute of the associated positions to true if the user has access.
     *
     * @param position
     * @param principalId
     * @param viewPositionPermitId
     * @param viewOrgUnitPermitId
     * @param viewSubjectPermitId
     */
    private static void checkPositionAssociationTargetAccess(Position position, Long principalId, Long viewPositionPermitId,
                                                             Long viewOrgUnitPermitId, Long viewSubjectPermitId, HibernateTemplate hibernateTemplate) {

        final Long positionId = position.getId();

        // process target associations
        final String positionAssociationTargetQuery = "select pa from PositionAssociation pa , UserDomainPermit dp " +
                " where pa.target.id = :positionId and pa.source.id = dp.nodeId and dp.userId = :userId and dp.permitId = :permitId";

        final List accessibleTargetAssociations = hibernateTemplate.findByNamedParam(positionAssociationTargetQuery,
                new String[]{"positionId", "userId", "permitId"}, new Long[]{positionId, principalId, viewPositionPermitId});

        final List targetAssociations = new ArrayList();
        targetAssociations.addAll(position.getTargetAssociations());
        targetAssociations.retainAll(accessibleTargetAssociations);

        Iterator it = targetAssociations.iterator();
        while (it.hasNext()) {
            PositionAssociation pa = (PositionAssociation) it.next();
            final Position source = (Position) pa.getSource();
            source.setHasAccess(true);
            checkPositionAssociatedArtefacts(source, principalId, viewOrgUnitPermitId, viewSubjectPermitId, hibernateTemplate);
        }

        // process source associations
        final String positionAssociationSourceQuery = "select pa from PositionAssociation pa , UserDomainPermit dp " +
                " where pa.source.id = :positionId and pa.target.id = dp.nodeId and dp.userId = :userId and dp.permitId = :permitId";

        final List accessibleSourceAssociations = hibernateTemplate.findByNamedParam(positionAssociationSourceQuery,
                new String[]{"positionId", "userId", "permitId"}, new Long[]{positionId, principalId, viewPositionPermitId});

        final List srcAssociations = new ArrayList();
        srcAssociations.addAll(position.getSourceAssociations());
        srcAssociations.retainAll(accessibleSourceAssociations);

        Iterator it1 = srcAssociations.iterator();
        while (it1.hasNext()) {
            PositionAssociation pa = (PositionAssociation) it1.next();
            final Position target = pa.getTarget();
            target.setHasAccess(true);
            checkPositionAssociatedArtefacts(target, principalId, viewOrgUnitPermitId, viewSubjectPermitId, hibernateTemplate);
        }
    }

    /**
     * Check access to subject's associated positions.
     * <br> Sets the "hasAccess" attribute of the associated positions to true if the user has access.
     *
     * @param subject
     * @param principalId
     * @param viewSubjectPermitId
     */
    private static void checkSubjectAssociationTargetAccess(Subject subject, Long principalId, Long viewSubjectPermitId, HibernateTemplate hibernateTemplate) {

        final String subjectAssociationTargetQuery = "select sa from SubjectAssociation sa , UserDomainPermit dp " +
                " where sa.subject.id = :subjectId and sa.position.id = dp.nodeId and dp.userId = :userId and dp.permitId = :permitId";

        final List accessibleTargetAssociations = hibernateTemplate.findByNamedParam(subjectAssociationTargetQuery,
                new String[]{"subjectId", "userId", "permitId"}, new Long[]{subject.getId(), principalId, viewSubjectPermitId});

        final List copy = new ArrayList();
        copy.addAll(subject.getSubjectAssociations());
        copy.retainAll(accessibleTargetAssociations);

        Iterator it = copy.iterator();
        while (it.hasNext()) {
            SubjectAssociation sa = (SubjectAssociation) it.next();
            sa.getTarget().setHasAccess(true);
        }
    }

    /**
     * Check access to position's associated subjects.
     * <br> Sets the "hasAccess" attribute of the associated subjects to true if the user has access.
     *
     * @param position
     * @param principalId
     * @param viewSubjectPermitId
     */
    private static void checkSubjectAssociationSourceAccess(Position position, Long principalId, Long viewSubjectPermitId, HibernateTemplate hibernateTemplate) {

        final String subjectAssociationSourceQuery = "select sa from SubjectAssociation sa , UserDomainPermit dp " +
                " where sa.position.id = :positionId and sa.subject.id = dp.nodeId and dp.userId = :userId and dp.permitId = :permitId";

        final List accessibleTargetAssociations = hibernateTemplate.findByNamedParam(subjectAssociationSourceQuery,
                new String[]{"positionId", "userId", "permitId"}, new Long[]{position.getId(), principalId, viewSubjectPermitId});

        final List copy = new ArrayList();
        copy.addAll(position.getSubjectAssociations());
        copy.retainAll(accessibleTargetAssociations);

        Iterator it = copy.iterator();
        while (it.hasNext()) {
            SubjectAssociation sa = (SubjectAssociation) it.next();
            sa.getSource().setHasAccess(true);
        }
    }

    public static void checkNodeViewAccess(Node node, Long userId, Long permitId, HibernateTemplate hibernateTemplate) {
        final List results = hibernateTemplate.find("from UserDomainPermit zudp where zudp.permitId = ? and zudp.userId = ? and zudp.nodeId = ?", new Object[]{permitId, userId, node.getId()});
        node.setHasAccess(!results.isEmpty());
    }
}
