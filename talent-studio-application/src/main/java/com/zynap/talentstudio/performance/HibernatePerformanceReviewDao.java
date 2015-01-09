/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.performance;

import net.sf.hibernate.Session;
import net.sf.hibernate.Criteria;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.expression.Expression;

import com.zynap.domain.IDomainObject;
import com.zynap.domain.admin.User;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.HibernateCrudAdaptor;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;
import com.zynap.talentstudio.workflow.Notification;

import org.springframework.dao.DataAccessException;

import java.util.Collection;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class HibernatePerformanceReviewDao extends HibernateCrudAdaptor implements IPerformanceReviewDao {

    public Class getDomainObjectClass() {
        return PerformanceReview.class;
    }

    public List findAll(String reportType, String artefactType, Long userId, boolean publicOnly) throws TalentStudioException {
        return getHibernateTemplate().find("from PerformanceReview review order by upper(review.label)");
    }

    public List<PerformanceEvaluator> getRoles(Long performanceId, Long subjectId) {
        return (List<PerformanceEvaluator>) getHibernateTemplate().find("from PerformanceEvaluator pe where pe.performanceReview.id = ? and pe.subject.id = ? order by upper(pe.role.sortOrder)", new Object[]{performanceId, subjectId});
    }

    public PerformanceManager findPerformanceManager(Long performanceId, Long subjectId) throws TooManyManagersFoundException {
        try {
            Session session = getSession(false);
            Criteria criteria = session.createCriteria(PerformanceManager.class);
            criteria.add(Expression.eq("subjectId", subjectId));
            criteria.add(Expression.eq("performanceId", performanceId));

            return (PerformanceManager) criteria.uniqueResult();
        } catch (HibernateException e) {
            throw new TooManyManagersFoundException(e);
        }
    }

    public void deletePerformanceManager(PerformanceManager performanceManager) throws TalentStudioException {
        getHibernateTemplate().delete(performanceManager);
    }

    public void saveOrUpdatePerformanceManager(PerformanceManager performanceManager) throws TalentStudioException {
        getHibernateTemplate().saveOrUpdate(performanceManager);
    }

    public List<PerformanceReview> getAllPerformanceReviews() {
        return (List<PerformanceReview>) getHibernateTemplate().find("from PerformanceReview r where r.appraisalType = '" + PerformanceReview.PERFORMANCE_TYPE + "'");
    }

    public QuestionnaireDefinition findPerformanceDefinition(Long appraisalId) {
        StringBuffer query = new StringBuffer("select workflow.questionnaireDefinition from QuestionnaireWorkflow workflow");
        query.append(" where workflow.performanceReview.id=").append(appraisalId);
        query.append(" and workflow.workflowType <> '").append(QuestionnaireWorkflow.TYPE_MANAGER_APPRAISAL).append("'");
        final List results = getHibernateTemplate().find(query.toString());
        return results.isEmpty() ? null : (QuestionnaireDefinition) results.get(0);
    }

    public List<Notification> getAppraisalReviewNotifications(Long performanceReviewId) {
        return (List<Notification>) getHibernateTemplate().find("from Notification n where n.performanceReviewId=?", new Object[]{performanceReviewId});
    }

    public Collection<User> getManagers(QuestionnaireWorkflow managerWorkflow) {

        // remove duplicates
        StringBuffer query = new StringBuffer("select distinct boss.user from User user, SubjectPrimaryAssociation subAssociation,");
        query.append(" SubjectPrimaryAssociation bossAssociation, Subject sub, Subject boss, Position p, WorkflowParticipant qwp");
        query.append(" where qwp.primaryKey.workflowId = ").append(managerWorkflow.getId());
        query.append(" and qwp.primaryKey.subjectId = subAssociation.subject.id");
        query.append(" and subAssociation.position.id = p.id")
                .append(" and subAssociation.qualifier.id=402")
                .append(" and p.parent.id = bossAssociation.position.id")
                .append(" and subAssociation.subject.id=sub.id")
                .append(" and bossAssociation.subject.id=boss.id")
                .append(" and boss.user.id=user.id");
        //noinspection unchecked
        return getHibernateTemplate().find(query.toString());

    }

    public void saveOrUpdateEvaluator(PerformanceEvaluator performanceEvaluator) throws TalentStudioException {
        getHibernateTemplate().saveOrUpdate(performanceEvaluator);
    }

    public void deleteEvaluator(PerformanceEvaluator performanceEvaluator) throws TalentStudioException {
        getHibernateTemplate().delete(performanceEvaluator);
    }

    public void deleteEvaluators(Long evaluateeSubjectId, Long reviewId) {
        getHibernateTemplate().delete("from PerformanceEvaluator e where e.subject.id=" + evaluateeSubjectId + " and e.performanceReview.id=" + reviewId);
    }

    public void delete(IDomainObject performanceReview) throws TalentStudioException {
        try {
            getHibernateTemplate().delete("from Notification n where n.performanceReviewId = " + performanceReview.getId());
            getHibernateTemplate().delete("from PerformanceEvaluator e where e.performanceId = " + performanceReview.getId());
            try {
                if (((PerformanceReview) performanceReview).isUserManaged()) {
                    getHibernateTemplate().delete("from PerformanceManager man where man.performanceId = " + performanceReview.getId());
                }
            } catch (DataAccessException e) {
                // do nothing means no manager was selected, an expected state
            }
            super.delete(performanceReview);
        } catch (DataAccessException e) {
            throw new DomainObjectNotFoundException(PerformanceReview.class, e);
        }
    }
}
