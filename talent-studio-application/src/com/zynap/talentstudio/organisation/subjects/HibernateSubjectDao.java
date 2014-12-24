/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.subjects;

import com.zynap.common.persistence.ZynapPersistenceSupport;
import com.zynap.domain.IDomainObject;
import com.zynap.domain.admin.User;
import com.zynap.domain.admin.UserType;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.CoreDetail;
import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.security.SecurityHelper;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * SubjectDao loads the subject object.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
@SuppressWarnings({"unchecked"})
public class HibernateSubjectDao extends ZynapPersistenceSupport implements ISubjectDao {

    public Class getDomainObjectClass() {
        return Subject.class;
    }

    /**
     * Check access to the subject and the positions associated to it.
     *
     * @param subject              The subject
     * @param userId               The user id
     * @param viewSubjectPermitId  The permit the user must have in order to have access to the subject
     * @param viewPositionPermitId The permit the user must have in order to have access to the positions
     */
    public void checkAccess(Subject subject, Long userId, Long viewSubjectPermitId, Long viewPositionPermitId) {
        SecurityHelper.checkSubjectAccess(subject, userId, viewSubjectPermitId, viewPositionPermitId, getHibernateTemplate());
    }

    public boolean checkNodeAccess(Node node, Long userId, Long viewSubjectPermitId) {
        SecurityHelper.checkNodeAccess(node, userId, viewSubjectPermitId, getHibernateTemplate());
        return node.isHasAccess();
    }

    public boolean checkNodeViewAccess(Node node, Long userId, Long viewSubjectPermitId) {
        SecurityHelper.checkNodeViewAccess(node, userId, viewSubjectPermitId, getHibernateTemplate());
        return node.isHasAccess();
    }

    /**
     * Delete the subject.
     * <br> Overriden to ensure that the core detail gets deleted.
     *
     * @param domainObject
     * @throws TalentStudioException
     */
    public void delete(IDomainObject domainObject) throws TalentStudioException {

        Subject subject = (Subject) domainObject;
        User user = subject.getUser();
        CoreDetail coreDetail = subject.getCoreDetail();
        super.delete(subject);
        // delete the associated objects not marked for cascade delete
        if (user == null) {
            getHibernateTemplate().delete(coreDetail);
            //getHibernateTemplate().delete("from CoreDetail coredetail where coredetail.id = " + subject.getCoreDetail().getId());
        } else {
            // delete all messages sent from this subjects user and to this subjects user
            getHibernateTemplate().delete("from MessageItem messages where messages.toUserId = " + user.getId() + " or messages.fromUser.id= " + user.getId());
            user.setUserType(UserType.USER);
            getHibernateTemplate().update(user);
        }
    }

    /**
     * Find subject by user id.
     * <br> Works for both active and inactive subjects.
     *
     * @param userId
     * @return IDomainObject
     * @throws TalentStudioException
     */
    public Subject findByUserId(Long userId) throws TalentStudioException {
        String queryString = "from Subject subject where subject.user.id=?";
        Collection<Subject> subjects = getHibernateTemplate().find(queryString, userId);
        if (subjects.isEmpty()) {
            throw new NoSubjectForUserException("No subject was found for the given userId: " + userId);
        }
        return subjects.iterator().next();
    }

    /**
     * Finds the people who report to the given user's position.
     *
     * @param userId the logged in user
     * @return all of the people reporting to the given user.
     * @throws com.zynap.exception.TalentStudioException
     *          select subject_id from SUBJECT_PRIMARY_ASSOCIATIONS where position_id in (
     *          select pa.source_id  from POSITION_ASSOCIATIONS pa, SUBJECT_PRIMARY_ASSOCIATIONS sa where pa.target_id = sa.position_id
     *          and sa.subject_id = 101)
     */
    public List<SubjectDTO> findTeam(Long userId) throws TalentStudioException {
        StringBuffer sql = new StringBuffer("select new ");
        sql.append(SubjectDTO.class.getName())
                .append(" (sp1.subject.id, sp1.subject.coreDetail.firstName, sp1.subject.coreDetail.secondName, sp1.subject.currentJobInfo, info.username")
                .append(")");
        sql.append(" from SubjectPrimaryAssociation sp1 left outer join sp1.subject.user.loginInfo info ");
        sql.append(" where exists");
        sql.append(" (select sa.source.id from PositionAssociation sa, SubjectPrimaryAssociation sp2, LookupValue lv");
        sql.append(" where sp2.position.id = sa.target.id");
        sql.append(" and sa.qualifier.id = lv.id");
        sql.append(" and lv.typeId = '").append(ILookupManager.LOOKUP_TYPE_PRIMARY_POSITION_ASSOC).append("'");
        sql.append(" and sp1.position.id=sa.source.id and sp1.position.active='T' and sp2.subject.user.id = ?)");
        final List<SubjectDTO> results = getHibernateTemplate().find(sql.toString(), new Object[]{userId});
        Set<SubjectDTO> uniqueResults = new HashSet<SubjectDTO>(results);
        return new ArrayList<SubjectDTO>(uniqueResults);
    }

    public Collection<SubjectDTO> findAllSubjectDTOs() {
        StringBuffer query = new StringBuffer("select new ");
        query.append(SubjectDTO.class.getName()).append(" ( subject.id, subject.coreDetail.firstName, subject.coreDetail.secondName, subject.currentJobInfo, info.username ) ");
        query.append(" from Subject subject left outer join subject.user.loginInfo info ");
        Set subjects = new HashSet<SubjectDTO>(getHibernateTemplate().find(query.toString()));
        return new ArrayList<SubjectDTO>(subjects);
    }

    public SubjectPicture findPicture(Long id) {
        return (SubjectPicture) getHibernateTemplate().load(SubjectPicture.class, id);
    }

    public void createPicture(SubjectPicture picture) {
        getHibernateTemplate().save(picture);
    }

    public void updatePicture(SubjectPicture picture) {
        getHibernateTemplate().update(picture);
    }

    public void deletePicture(SubjectPicture picture) {
        getHibernateTemplate().delete(picture);
    }

    public String getMyTeamViewAttributeLabel(Long userId) {
        final List list = getHibernateTemplate().find("select attr.label from DynamicAttribute attr, MyTeamView teamView where teamView.userId=" + userId
                + " and attr.id = teamView.dynamicAttributeId ");
        if (list.isEmpty()) return null;
        return (String) list.get(0);
    }

    public NodeExtendedAttribute getTeamViewAttribute(Long managerUserId, Long subjectId) {
        StringBuffer sql = new StringBuffer("from NodeExtendedAttribute attr where attr.node.id=");
        sql.append(subjectId).append(" and attr.dynamicAttribute.id=(select teamView.dynamicAttributeId from MyTeamView teamView where teamView.userId=")
                .append(managerUserId).append(")");
        final List results = getHibernateTemplate().find(sql.toString());
        return (NodeExtendedAttribute) (results.isEmpty() ? null : results.get(0));
    }

    public void updateCurrentJobInfo(Long subjectId) {
        new UpdateCurrentJobInfo(getJdbcTemplate()).execute(subjectId);
    }

    private class UpdateCurrentJobInfo extends StoredProcedure {

        public UpdateCurrentJobInfo(JdbcTemplate jdbcTemplate) {
            super(jdbcTemplate, "zynap_node_sp.update_current_job_info");
            declareParameter(new SqlParameter("subject_id_", Types.INTEGER));
        }

        public void execute(Long id) {
            Map<String, Long> in = new HashMap<String, Long>();
            in.put("subject_id_", id);
            execute(in);
        }
    }
}
