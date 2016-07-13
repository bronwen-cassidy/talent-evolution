/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */

package com.zynap.talentstudio.questionnaires;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.LockMode;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.expression.Expression;
import net.sf.hibernate.expression.Order;
import net.sf.hibernate.transform.DistinctRootEntityResultTransformer;

import com.zynap.common.persistence.ZynapPersistenceSupport;
import com.zynap.common.util.StringUtil;
import com.zynap.domain.admin.User;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.PessimisticLockingFailureException;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.util.collections.CollectionUtils;
import com.zynap.util.ArrayUtils;

import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate.HibernateCallback;
import org.springframework.orm.hibernate.HibernateTemplate;

import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */

public class HibernateQuestionnaireDao extends ZynapPersistenceSupport implements IQuestionnaireDao {

    public Class getDomainObjectClass() {
        return QuestionnaireWorkflow.class;
    }

    public void createDefinition(QuestionnaireDefinition questionnaireDefinition) throws TalentStudioException {
        getHibernateTemplate().save(questionnaireDefinition);
    }

    public QuestionnaireDefinition findDefinition(Long questionnaireDefinitionId) {
        return (QuestionnaireDefinition) getHibernateTemplate().load(QuestionnaireDefinition.class, questionnaireDefinitionId);
    }

    public void createQuestionnaireModel(QuestionnaireDefinitionModel questionnaireDefinitionModel) throws TalentStudioException {
        getHibernateTemplate().save(questionnaireDefinitionModel);
    }

    public void createQuestionnaireWorkflow(QuestionnaireWorkflow questionnaireWorkflow) throws TalentStudioException {
        getHibernateTemplate().save(questionnaireWorkflow);
    }

    public void createQuestionnaire(Questionnaire questionnaire) throws TalentStudioException {
        getHibernateTemplate().save(questionnaire);
    }

    public Collection<QuestionnaireDefinition> findAllDefinitions() throws Exception {
        Session session = getSession();
        Criteria criteria = session.createCriteria(QuestionnaireDefinition.class);
        criteria.addOrder(Order.asc("label"));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public List<DefinitionDTO> listDefinitions() {
        StringBuffer query = new StringBuffer("select new " + DefinitionDTO.class.getName() + "(");
        query.append("definition.label, definition.id, definition.description, definition.title )")
                .append(" from QuestionnaireDefinition definition order by definition.label");
        return getHibernateTemplate().find(query.toString());
    }

    public QuestionnaireXmlData findQuestionnaireXmlData(Long questionnaireDefinitionId) {
        return (QuestionnaireXmlData) getHibernateTemplate().load(QuestionnaireXmlData.class, questionnaireDefinitionId);
    }

    public void closeQuestionnaires(QuestionnaireWorkflow questionnaireWorkflow) {
        Object[] params = new Object[]{StringUtil.TRUE, new Date(), questionnaireWorkflow.getId()};
        getJdbcTemplate().update("update QUESTIONNAIRES set IS_COMPLETED = ?, COMPLETED_DATE = ? where QWF_ID = ?", params);
    }

    public Collection<Questionnaire> findAllQuestionnaires() throws Exception {
        Session session = getSession();
        Criteria criteria = session.createCriteria(Questionnaire.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public void updateDefinition(QuestionnaireDefinition questionnaireDefinition) throws TalentStudioException {
        getHibernateTemplate().update(questionnaireDefinition);
    }


    public void updateQuestionnaireModel(QuestionnaireDefinitionModel questionnaireDefinitionModel) throws TalentStudioException {
        getHibernateTemplate().update(questionnaireDefinitionModel);
    }

    public List<QuestionnaireDefinition> findPublishedQuestionnaireDefinitions(String[] questionTypes) throws TalentStudioException {

        final boolean questionTypeSupplied = !ArrayUtils.isEmpty(questionTypes);

        final List list;
        final StringBuffer query = new StringBuffer();
        query.append("select definition from QuestionnaireDefinition definition");
        query.append(" join fetch definition.questionnaireDefinitionModel");
        query.append(" join fetch definition.questionnaireWorkflows wf");

        if (questionTypeSupplied) {

            query.append(" where definition.id in ( select da.questionnaireDefinitionId from DynamicAttribute da");
            query.append(" where da.artefactType = '").append(Node.QUESTIONNAIRE_TYPE).append("' and ");
            query.append(" da.type in (").append(ArrayUtils.arrayToString(questionTypes, ",", "\'"));

            query.append(" )) ");
            query.append(" order by upper(definition.label)");

            list = getHibernateTemplate().find(query.toString());

        } else {

            query.append(" order by upper(definition.label)");
            list = getHibernateTemplate().find(query.toString());
        }

        return CollectionUtils.removeDuplicates(list);
    }

    public void createOrUpdate(Questionnaire questionnaire) throws TalentStudioException {
        questionnaire.setLocked(false);
        getHibernateTemplate().saveOrUpdateCopy(questionnaire);
    }

    /**
     * Locking method, locks the questionnaire for editing
     *
     * @param questionnaireId
     * @param loggedInUserId
     * @return
     * @throws TalentStudioException
     */
    public Questionnaire loadQuestionnaireForUpdate(Long questionnaireId, Long loggedInUserId) throws TalentStudioException {
        try {
            final HibernateTemplate template = getHibernateTemplate();
            Questionnaire questionnaire = (Questionnaire) template.load(Questionnaire.class, questionnaireId, LockMode.UPGRADE_NOWAIT);
            if (questionnaire.isLocked()) throw new PessimisticLockingFailureException("error.questionnaire.locked");
            questionnaire.setLocked(true);
            questionnaire.setLockedBy(loggedInUserId);
            template.update(questionnaire);
            assignQuestionnaireWorkflow(questionnaire);
            return questionnaire;
        } catch (CannotAcquireLockException e) {
            throw new PessimisticLockingFailureException(e.getMessage(), "error.questionnaire.locked");
        } catch (ConcurrencyFailureException e) {
            throw new PessimisticLockingFailureException(e.getMessage(), "error.questionnaire.locked");
        } catch (ObjectRetrievalFailureException e) {
            throw new DomainObjectNotFoundException(Questionnaire.class, questionnaireId, e);
        } catch (DataIntegrityViolationException e) {
            throw new PessimisticLockingFailureException(e.getMessage(), "error.questionnaire.locked");
        } catch (DataAccessException e) {
            throw new PessimisticLockingFailureException(e.getMessage(), "error.questionnaire.locked");
        }
    }

    public Questionnaire findOrCreateQuestionnaire(Long workflowId, Long userId, Long subjectId) throws TalentStudioException {

        Questionnaire questionnaire = findQuestionnaireByWorkflow(workflowId, null, subjectId, null);
        if (questionnaire == null) {
            questionnaire = new Questionnaire(null, getWorkflow(workflowId), new User(userId));
        }
        if (questionnaire.isLocked()) throw new PessimisticLockingFailureException("error.questionnaire.locked");
        questionnaire.setLocked(true);
        questionnaire.setLockedBy(userId);
        questionnaire.setSubjectId(subjectId);
        getHibernateTemplate().saveOrUpdate(questionnaire);
        return questionnaire;
    }

    public void removeNotifications(Long performanceId) {
        getHibernateTemplate().delete("from Notification n where n.performanceReviewId=" + performanceId);
    }

    public void createAttribute(NodeExtendedAttribute newAttribute) {
        getHibernateTemplate().save(newAttribute);
    }

    public void createOrUpdateAttribute(NodeExtendedAttribute newAttribute) {
        getHibernateTemplate().saveOrUpdate(newAttribute);
    }

    public List<NodeExtendedAttribute> findAttributes(Long dynamicAttributeId, Long questionnaireId) {
        return getHibernateTemplate().find("from NodeExtendedAttribute nea where nea.dynamicAttribute.id = " + dynamicAttributeId + " and nea.node.id = " + questionnaireId);
    }

    public List<DynamicAttribute> findAttributesLastMofidied(Long queDefId) {
        return getHibernateTemplate().find("from DynamicAttribute da where da.questionnaireDefinitionId = " + queDefId + " and da.type in ('LASTUPDATED', 'LASTUPDATEDBY')");
    }

    public void createData(QuestionnaireXmlData data) {
        getHibernateTemplate().save(data);
    }

    public NodeExtendedAttribute findAttribute(Long attributeId) {
        return (NodeExtendedAttribute) getHibernateTemplate().load(NodeExtendedAttribute.class, attributeId);
    }

    public void deleteQuestionnaireAttributes(Long queId, Long daId) {
        getHibernateTemplate().delete("from NodeExtendedAttribute attr where attr.node.id=" + queId + " and attr.dynamicAttribute.id=" + daId);
    }

    public void deleteAttribute(Long attributeId) {
        getHibernateTemplate().delete("from NodeExtendedAttribute attr where attr.id=" + attributeId);
    }

    public List<NodeExtendedAttribute> findNodeAttributes(Long daId, Integer dynamicPosition, Long questionnaireId) {
        final StringBuffer query = new StringBuffer();
        query.append("select attr from NodeExtendedAttribute attr");
        query.append(" where attr.dynamicAttribute.id=").append(daId);
        query.append(" and attr.dynamicPosition=").append(dynamicPosition);
        query.append(" and attr.node.id=").append(questionnaireId);
        return (List<NodeExtendedAttribute>) getHibernateTemplate().find(query.toString());
    }

    public List<QuestionAttribute> findLineItemQuestions(Long lineItemId) {
        final StringBuffer query = new StringBuffer();
        query.append("select q from QuestionAttribute q");
        query.append(" where q.lineItem.id=").append(lineItemId);
        return (List<QuestionAttribute>) getHibernateTemplate().find(query.toString());
    }


    /**
     * Gets completed questionnaires for subject.
     * <br/> Returns questionnaires and info form stubs.
     *
     * @param subjectId the subject whose questionnaires we are finding
     * @return Collection of {@link QuestionnaireDTO} objects.
     * @throws TalentStudioException
     */
    public Collection<QuestionnaireDTO> getPortfolioQuestionnaires(Long subjectId) throws TalentStudioException {

        final StringBuffer stringBuffer = getCompletedQuestionnaireQueryRoot();
        stringBuffer.append("and (workflow.managerRead = 'T' or (workflow.performanceReview is not null and questionnaire.completed = 'T') ")
                .append("or (workflow.workflowType='QUESTIONNARE_GENERAL' and questionnaire.completed='T')) ");
        addOrderByClause(stringBuffer);
        final String query = stringBuffer.toString();
        final Set<QuestionnaireDTO> results = new LinkedHashSet<QuestionnaireDTO>(getHibernateTemplate().findByNamedParam(query, "subjectId", subjectId));

        return getInfoForms(results, subjectId, true);
    }

    /**
     * Gets completed questionnaires for subject.
     * <br/> Differs from overloaded method as it only returns completed notification-based questionnaires that were answered by the specified user.
     * <br/> Returns questionnaires and info form stubs.
     *
     * @param subjectId subjectId for the personal portfolio questionnaires to be returned
     * @param userId    userId for the personal portfolio questionnaires to be returned
     * @return Collection of {@link QuestionnaireDTO} objects.
     * @throws TalentStudioException
     */
    public Collection<QuestionnaireDTO> getPersonalPortfolioQuestionnaires(Long subjectId, Long userId) throws TalentStudioException {

        final StringBuffer stringBuffer = getCompletedQuestionnaireQueryRoot();
        stringBuffer.append(" and (workflow.individualRead = 'T' or (workflow.performanceReview is not null and questionnaire.completed = 'T')")
                .append(" or (workflow.workflowType='QUESTIONNARE_GENERAL' and questionnaire.completed='T')) ");
        addOrderByClause(stringBuffer);

        final String query = stringBuffer.toString();

        final String[] paramNames = {"subjectId"};
        final Object[] paramValues = {subjectId};
        final Set<QuestionnaireDTO> results = new LinkedHashSet<QuestionnaireDTO>(getHibernateTemplate().findByNamedParam(query, paramNames, paramValues));

        return getInfoForms(results, subjectId, false);
    }

    public Questionnaire findQuestionnaireByWorkflow(Long questionnaireWorkflowId, Long userId, Long subjectId, Long roleId) throws TalentStudioException {

        final boolean hasRole = (roleId != null);
        final List<Long> parameterValues = new ArrayList<Long>();
        if (hasRole && userId == null) {
            throw new TalentStudioException("If you are trying to load an appraisal (performance review) you must provide a userId! ");
        }
        StringBuilder stringBuilder = new StringBuilder("from Questionnaire questionnaire where questionnaire.questionnaireWorkflowId= ?");
        stringBuilder.append(" and questionnaire.subjectId = ?");
        parameterValues.add(questionnaireWorkflowId);
        parameterValues.add(subjectId);

        if (userId != null) {
            stringBuilder.append(" and questionnaire.user.id = ?");
            parameterValues.add(userId);
        }

        if (hasRole) {
            stringBuilder.append(" and questionnaire.role.id = ?");
            parameterValues.add(roleId);
        }

        List questionnaires = getHibernateTemplate().find(stringBuilder.toString(), parameterValues.toArray());
        // todo if more than one questionnaire comes back in this list we have an error condition, i.e no role (would be a manager)
        Questionnaire questionnaire = (Questionnaire) (questionnaires.isEmpty() ? null : questionnaires.get(0));
        if (questionnaire != null) {
            assignQuestionnaireWorkflow(questionnaire);
            // check this is not a performance review userId is important for reviews as it represents the person doing the reviews.
            final QuestionnaireWorkflow workflow = questionnaire.getQuestionnaireWorkflow();
            if (workflow.getPerformanceReview() != null && userId == null) {
                throw new TalentStudioException("If you are trying to load an appraisal (performance review) you must provide a userId! ");
            }
        }
        return questionnaire;
    }

    public Collection<Questionnaire> findQuestionnaires(Long workflowId, Long subjectId) throws TalentStudioException {

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("from Questionnaire questionnaire ").append("where questionnaire.questionnaireWorkflowId= :workflowId ");
        stringBuilder.append("and questionnaire.subjectId = :subjectId");
        String query = stringBuilder.toString();

        return getHibernateTemplate().findByNamedParam(query, new String[]{"workflowId", "subjectId"}, new Object[]{workflowId, subjectId});
    }

    public List<Questionnaire> findCompletedQuestionnaires(Long workflowId, Long subjectId) throws TalentStudioException {
        final Collection<Questionnaire> questionnaireCollection = findQuestionnaires(workflowId, subjectId);
        // make sure all are completed or return none
        for (Questionnaire questionnaire : questionnaireCollection) {
            if(!questionnaire.isCompleted()) return new ArrayList<Questionnaire>();
        }
        return (List<Questionnaire>) questionnaireCollection;
    }

    public void reload(QuestionnaireWorkflow questionnaireWorkflow) {
        try {
            getSession().refresh(questionnaireWorkflow);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    public Long getLineItemId(Long dynamicAttributeId) {
        final StringBuffer query = new StringBuffer();
        query.append("select q.lineItem.id from QuestionAttribute q");
        query.append(" where q.dynamicAttribute.id=").append(dynamicAttributeId);
        List<Long> results = getHibernateTemplate().find(query.toString());
        return results.isEmpty() ? null : results.get(0);
    }

    public Integer countNumAppraisals(final Long userId) throws TalentStudioException {

        final StringBuffer query = new StringBuffer("select count(*) from Notification n where n.recipientId = :userId ");
        query.append( " and n.status <> 'COMPLETED'");
        query.append(" and " + " exists (select pr.id from PerformanceReview pr,").append(
                "QuestionnaireWorkflow qw where ").append(
                "n.workflowId = pr.id or n.workflowId = qw.id and qw.performanceReview.id = pr.id  )");

        Integer numRecords = (Integer) getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Query queryObject = session.createQuery(query.toString());
                queryObject.setParameter("userId", userId);
                return queryObject.uniqueResult();
            }
        }, true);
        return numRecords;
    }

    public Integer countNumQuestionnaires(final Long userId) throws TalentStudioException {

        final StringBuffer query = new StringBuffer("select count(*) from Notification n where n.recipientId = :userId");
        query.append( " and n.status <> 'COMPLETED'");
        query.append(" and " + "not exists (select pr.id from PerformanceReview pr,").append(
                "QuestionnaireWorkflow qw where ").append(
                "n.workflowId = pr.id or n.workflowId = qw.id and qw.performanceReview.id = pr.id  )");

        Integer numRecords = (Integer) getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Query queryObject = session.createQuery(query.toString());
                queryObject.setParameter("userId", userId);
                return queryObject.uniqueResult();
            }
        }, true);
        return numRecords;
    }

    public void addParticipant(QuestionnaireWorkflow questionnaireWorkflow, Subject subject) {
        Long id = questionnaireWorkflow.getId();
        getHibernateTemplate().save(new WorkflowParticipant(subject.getId(), id));
    }

    public boolean isParticipant(Long subjectId, Long questionnaireWorkflowId) {
        String query = "select count(*) from WorkflowParticipant p where p.primaryKey.subjectId="+subjectId + " and p.primaryKey.workflowId=" + questionnaireWorkflowId;
        return ((Integer) getHibernateTemplate().find(query).get(0)).intValue() > 0;
    }

    public void addParticipants(QuestionnaireWorkflow questionnaireWorkflow, Collection<Subject> subjects) {
        for (Subject subject : subjects) {
            if (questionnaireWorkflow.isNotificationBased() && subject.getUser() == null) continue;
            this.addParticipant(questionnaireWorkflow, subject);
        }
    }

    public void removeParticipant(QuestionnaireWorkflow questionnaireWorkflow, Subject subject) {
        Long id = questionnaireWorkflow.getId();
        getHibernateTemplate().delete(new WorkflowParticipant(subject.getId(), id));
    }

    public void removeParticipants(QuestionnaireWorkflow questionnaireWorkflow, Collection<Subject> subjects) {
        for (Subject subject : subjects) {
            removeParticipant(questionnaireWorkflow, subject);
        }
    }


    public Collection<User> getParticipants(QuestionnaireWorkflow questionnaireWorkflow) {
        StringBuilder query = new StringBuilder("select user from User user, WorkflowParticipant wp, Subject s");
        query.append(" where wp.primaryKey.workflowId = ").append(questionnaireWorkflow.getId())
                .append(" and wp.primaryKey.subjectId = s.id")
                .append(" and s.user.id = user.id ");

        //noinspection unchecked
        return getHibernateTemplate().find(query.toString());
    }

    public Collection getSubjectParticipants(QuestionnaireWorkflow questionnaireWorkflow) {
        StringBuilder query = new StringBuilder("select s from Subject s, WorkflowParticipant wp");
        query.append(" where wp.primaryKey.workflowId = ").append(questionnaireWorkflow.getId())
                .append(" and wp.primaryKey.subjectId = s.id");

        return getHibernateTemplate().find(query.toString());
    }

    public void removeAllParticipants(QuestionnaireWorkflow questionnaireWorkflow) {
        getHibernateTemplate().delete(" from WorkflowParticipant wp where wp.primaryKey.workflowId = " + questionnaireWorkflow.getId());
    }

    /**
     * Find workflows that have expired and are not already marked as complete.
     * <br/> Obviously excludes workflows that do not have an expiry date.
     *
     * @return Collection of {@link QuestionnaireWorkflow} objects.
     */
    public Collection<QuestionnaireWorkflow> findExpiredWorkflows() {

        final StringBuilder query = new StringBuilder("from QuestionnaireWorkflow workflow");
        query.append(" where workflow.expiryDate is not null");
        query.append(" and workflow.status <> '").append(QuestionnaireWorkflow.STATUS_COMPLETED).append("'");
        query.append(" and workflow.expiryDate");
        query.append(" < to_date('");

        // format current date to sensible pattern and then use as the arg in the to_date call
        DateFormat dateFormat = new SimpleDateFormat(IQuestionnaireService.EXPIRY_DATE_PATTERN);
        final String formattedDate = dateFormat.format(new Date());

        query.append(formattedDate);
        query.append("', '").append(IQuestionnaireService.EXPIRY_DATE_PATTERN).append("')");

        final String queryString = query.toString();

        //noinspection unchecked
        return getHibernateTemplate().find(queryString);
    }

    public Collection findWorkflowsForPopulation(Long populationId) {

        StringBuilder query = new StringBuilder("from QuestionnaireWorkflow workflow where workflow.population.id = :populationId");
        query.append(" and workflow.workflowType = '").append(QuestionnaireWorkflow.TYPE_INFO_FORM).append("'");
        query.append(" and workflow.status <> '").append(QuestionnaireWorkflow.STATUS_COMPLETED).append("'");
        query.append(" order by workflow.label");
        return getHibernateTemplate().findByNamedParam(query.toString(), new String[]{"populationId"}, new Object[]{populationId});
    }

    public Collection findAllWorkflows() {

        StringBuilder query = new StringBuilder("from QuestionnaireWorkflow workflow where ");
        query.append(" workflow.workflowType = '").append(QuestionnaireWorkflow.TYPE_INFO_FORM).append("'");
        query.append(" and workflow.status <> '").append(QuestionnaireWorkflow.STATUS_COMPLETED).append("'");
        query.append(" order by workflow.label");
        return getHibernateTemplate().find(query.toString());
    }

    public List<Questionnaire> findQuestionnairesByWorkflow(Long questionnaireWorkflowId) {
        StringBuffer query = new StringBuffer("from Questionnaire questionnaire where questionnaire.questionnaireWorkflow.id=");
        query.append(questionnaireWorkflowId);
        return getHibernateTemplate().find(query.toString());
    }

    /**
     * Finds the workflows that are manager write, not completed and is an info form
     *
     * @return questionnaires that are searchable and editable
     */
    public List<QuestionnaireDTO> findSearchableWorkflows() {
        StringBuilder query = new StringBuilder("select new " + QuestionnaireDTO.class.getName() + "(workflow.id, workflow.label, workflow.workflowType, workflow.status, workflow.expiryDate, workflow.closedDate, workflow.individualWrite, workflow.managerWrite)");
        query.append(" from QuestionnaireWorkflow workflow ");
        query.append(" where workflow.workflowType = '").append(QuestionnaireWorkflow.TYPE_INFO_FORM).append("'");
        query.append(" and workflow.status <> '").append(QuestionnaireWorkflow.STATUS_COMPLETED).append("'");
        query.append(" and workflow.managerWrite = 'T'");
        query.append(" order by workflow.label");
        return getHibernateTemplate().find(query.toString());
    }

    public Collection<QuestionnaireWorkflowDTO> findAllQuestionnaireWorkflowDTOs() {
        StringBuffer query = new StringBuffer("select new ");
        query.append(QuestionnaireWorkflowDTO.class.getName()).append(" ( workflow.id, workflow.label, workflow.workflowType, workflow.group.label,")
                .append(" workflow.startDate, workflow.lastRepublishedDate, workflow.expiryDate,")
                .append(" workflow.status, workflow.questionnaireDefinition.label, workflow.questionnaireDefinition.id, workflow.population.label )")
                .append(" from QuestionnaireWorkflow workflow left join fetch workflow.group")
                .append(" where workflow.workflowType in ('INFO_FORM', 'QUESTIONNARE_GENERAL') ");
        Set<QuestionnaireWorkflowDTO> workflows = new HashSet<QuestionnaireWorkflowDTO>(getHibernateTemplate().find(query.toString()));
        return new ArrayList<QuestionnaireWorkflowDTO>(workflows);
    }

    public Collection<QuestionnaireWorkflowDTO> findAllWorkflowDTOs() {
        StringBuffer query = new StringBuffer("select new ");
        query.append(QuestionnaireWorkflowDTO.class.getName()).append(" ( workflow.id, workflow.label, workflow.workflowType, workflow.group.label,")
                .append(" workflow.startDate, workflow.lastRepublishedDate, workflow.expiryDate,")
                .append(" workflow.status, workflow.questionnaireDefinition.label, workflow.questionnaireDefinition.id, workflow.population.label )")
                .append(" from QuestionnaireWorkflow workflow left join fetch workflow.group")
                .append(" order by workflow.label ");
        Set<QuestionnaireWorkflowDTO> workflows = new HashSet<QuestionnaireWorkflowDTO>(getHibernateTemplate().find(query.toString()));
        return new ArrayList<QuestionnaireWorkflowDTO>(workflows);
    }

    /**
     * When a timeout occurs unlocks any locked questionnaires.
     *
     * @param loggedInUserId
     */
    public void unlockQuestionnaires(Long loggedInUserId) {
        try {
            Session session = getSession(false);
            Criteria criteria = session.createCriteria(Questionnaire.class);
            criteria.add(Expression.eq("lockedBy", loggedInUserId));
            criteria.add(Expression.eq("locked", Boolean.TRUE));
            criteria.setResultTransformer(new DistinctRootEntityResultTransformer());
            final List questionnaires = criteria.list();
            for (Iterator iterator = questionnaires.iterator(); iterator.hasNext();) {
                Questionnaire questionnaire = (Questionnaire) iterator.next();
                questionnaire.setLocked(false);
                session.update(questionnaire);
            }
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    public void unlockQuestionnaire(Long questionnaireId, Long userId) {
        final HibernateTemplate template = getHibernateTemplate();
        String sql = "from Questionnaire que where que.id=" + questionnaireId + " and que.lockedBy = " + userId;
        List results = getHibernateTemplate().find(sql);
        if (!results.isEmpty()) {
            Questionnaire questionnaire = (Questionnaire) results.get(0);
            questionnaire.setLocked(false);
            template.update(questionnaire);
        }
    }

    public void deleteLineItemAnswers(Long questionnaireId, Integer dynamicPosition, Long lineItemId) {
        new DeleteLineItemAnswers(getJdbcTemplate()).execute(questionnaireId, dynamicPosition, lineItemId);
    }


    /**
     * Cascade deletion of a questionnaire definition and all of its dependancies.
     *
     * @param questionnaireDefinition
     * @throws TalentStudioException
     */
    public void deleteDefinition(QuestionnaireDefinition questionnaireDefinition) throws TalentStudioException {
        new DeleteDefinition(getJdbcTemplate()).execute(questionnaireDefinition.getId());
    }

    private static class DeleteDefinition extends StoredProcedure {

        public DeleteDefinition(JdbcTemplate jdbcTemplate) {
            super(jdbcTemplate, "zynap_node_sp.delete_que_definition");
            declareParameter(new SqlParameter("que_def_id_", Types.INTEGER));
        }

        public void execute(Long queDefId) {
            Map<String, Long> in = new HashMap<String, Long>();
            in.put("que_def_id_", queDefId);
            execute(in);
        }
    }

    private static class DeleteLineItemAnswers extends StoredProcedure {

        public DeleteLineItemAnswers(JdbcTemplate jdbcTemplate) {
            super(jdbcTemplate, "zynap_node_sp.delete_lineitem_row");
            declareParameter(new SqlParameter("dynamic_position_", Types.INTEGER));
            declareParameter(new SqlParameter("line_item_id_", Types.INTEGER));
            declareParameter(new SqlParameter("q_id_", Types.INTEGER));
        }

        public void execute(Long queDefId, Integer dynamicPosition, Long lineItemId) {
            Map<String, Number> in = new HashMap<String, Number>();
            in.put("dynamic_position_", dynamicPosition);
            in.put("line_item_id_", lineItemId);
            in.put("q_id_", queDefId);
            execute(in);
        }
    }

    /**
     * Add info forms to Set of completedQuestionnaires (uses a Set to remove duplicates.)
     * <br/> Then creates the new List and orders the whole List by workflow id.
     *
     * @param completedQuestionnaires this is both the completed questionnaires and the created info_forms (infoForms only
     *                                exist as notifications until they are selected for editing).
     * @param subjectId               the subject the infoForms, questionnaires belong to (i.e. the target audience person for the questionnaires)
     * @param isManager               - are we looking for the info_forms for a managers view (i.e. a manager viewing a subordinates portfolio)
     * @return List of sorted, guarenteed no-duplicates list
     */
    private List<QuestionnaireDTO> getInfoForms(Set<QuestionnaireDTO> completedQuestionnaires, Long subjectId, boolean isManager) {

        StringBuilder queryBuilder = new StringBuilder(" select new " + QuestionnaireDTO.class.getName() + "(")
                .append(" workflow.id, workflow.label, workflow.workflowType, workflow.status, workflow.expiryDate,")
                .append(" workflow.closedDate, workflow.individualWrite, workflow.managerWrite, workflow.group.label)")
                .append(" from WorkflowParticipant participant, QuestionnaireWorkflow workflow left join fetch workflow.group")
                .append(" where participant.primaryKey.subjectId = :subjectId")
                .append(" and participant.primaryKey.workflowId = workflow.id");
        if (isManager) {
            queryBuilder.append(" and workflow.managerRead = 'T'");
        } else {
            queryBuilder.append(" and workflow.individualRead = 'T'");
        }

        queryBuilder.append(" order by workflow.group.label, workflow.id");

        final String query = queryBuilder.toString();
        final List<QuestionnaireDTO> infoForms = getHibernateTemplate().findByNamedParam(query, "subjectId", subjectId);
        completedQuestionnaires.addAll(infoForms);

        // two separate lists are added together so we need to sort the lot, even though each list is individually sorted.
        final List<QuestionnaireDTO> results = new ArrayList<QuestionnaireDTO>(completedQuestionnaires);
        Collections.sort(results, new Comparator<QuestionnaireDTO>() {
            public int compare(QuestionnaireDTO o1, QuestionnaireDTO o2) {
                String label1 = o1.getGroupLabel();
                String label2 = o2.getGroupLabel();
                if (label1 == null) label1 = "";
                if (label2 == null) label2 = "";
                return label1.compareTo(label2);
            }
        });

        return results;
    }

    private StringBuffer getCompletedQuestionnaireQueryRoot() {

        final StringBuffer stringBuffer = new StringBuffer(" select new " + QuestionnaireDTO.class.getName() + "(");
        stringBuffer.append("questionnaire.completed, questionnaire.id, workflow.id, workflow.label, workflow.workflowType, workflow.status, workflow.expiryDate, ");
        stringBuffer.append("workflow.individualWrite, workflow.managerWrite, questionnaire.completedDate, questionnaire.role, workflow.group.label) ");
        stringBuffer.append("from Questionnaire questionnaire left join fetch questionnaire.role, ");
        stringBuffer.append("QuestionnaireWorkflow workflow left join fetch workflow.group where questionnaire.subjectId = :subjectId ");
        stringBuffer.append("and questionnaire.questionnaireWorkflowId = workflow.id and workflow.status <> 'ARCHIVED' ");
        return stringBuffer;
    }

    private void addOrderByClause(final StringBuffer stringBuffer) {
        stringBuffer.append(" order by questionnaire.questionnaireWorkflowId, questionnaire.role.sortOrder");
    }

    private void assignQuestionnaireWorkflow(Questionnaire questionnaire) {
        questionnaire.setQuestionnaireWorkflow(getWorkflow(questionnaire.getQuestionnaireWorkflowId()));
    }

    private QuestionnaireWorkflow getWorkflow(Long workflowId) {
        String query = "select workflow from QuestionnaireWorkflow workflow join fetch workflow.questionnaireDefinition where workflow.id = :workflowId";
        final List results = getHibernateTemplate().findByNamedParam(query, "workflowId", workflowId);
        return (QuestionnaireWorkflow) results.get(0);
    }
}
