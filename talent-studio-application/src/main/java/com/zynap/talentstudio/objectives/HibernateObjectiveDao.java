package com.zynap.talentstudio.objectives;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.expression.Expression;
import net.sf.hibernate.expression.Order;
import net.sf.hibernate.transform.DistinctRootEntityResultTransformer;

import com.zynap.common.persistence.ZynapPersistenceSupport;
import com.zynap.exception.TalentStudioException;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: amark
 * Date: 23-May-2006
 * Time: 14:38:15
 */
public class HibernateObjectiveDao extends ZynapPersistenceSupport implements IObjectiveDao {

    public Class getDomainObjectClass() {
        return ObjectiveSet.class;
    }

    public Objective findObjective(Long id) throws TalentStudioException {
        return ((Objective) findById(Objective.class, id));
    }

    public void updateObjective(Objective objective) throws TalentStudioException {
        getHibernateTemplate().update(objective);
    }

    public void deleteObjective(Objective objective) throws TalentStudioException {
        getHibernateTemplate().delete(objective);
    }

    /**
     * The list of the given subjects current, approved objectives.
     *
     * @param subjectId the id of the subject
     * @return the list of objectives.
     */
    public List<Objective> findCurrentSubjectObjectives(Long subjectId) {
        try {
            Session session = getSession(false);
            Criteria criteria = session.createCriteria(Objective.class);
            criteria.add(Expression.not(Expression.eq("status", ObjectiveConstants.STATUS_ARCHIVED)));
            criteria.createAlias("objectiveSet", "objSet");
            criteria.add(Expression.eq("objSet.subject.id", subjectId));
            criteria.setResultTransformer(new DistinctRootEntityResultTransformer());

            //noinspection unchecked
            return criteria.list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return new ArrayList<Objective>();
    }

    public ObjectiveSet findCurrentObjectiveSet(Long subjectId) {

        try {
            Session session = getSession(false);
            Criteria criteria = session.createCriteria(ObjectiveSet.class);
            criteria.add(Expression.eq("subject.id", subjectId));
            criteria.add(Expression.not(Expression.eq("status", ObjectiveConstants.STATUS_ARCHIVED)));
            criteria.createCriteria("objectives");
            List objectiveSets = criteria.list();

            if (objectiveSets.isEmpty()) return null;
            return (ObjectiveSet) objectiveSets.iterator().next();

        } catch (HibernateException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    //String status, Date lastModifiedDate, String actionRequired, String actionGroup, String type, boolean approved, Long subjectId
    public ObjectiveSetDto findCurrentObjectiveSetDto(Long subjectId) {
        StringBuffer query = new StringBuffer("select new ");
        query.append(ObjectiveSetDto.class.getName()).append("(o.status, o.lastModifiedDate, o.actionRequired, o.actionGroup, o.type, o.approved, o.subject.id, o.id )")
                .append(" from ObjectiveSet o where o.subject.id=").append(subjectId)
                .append(" and o.status <> '").append(ObjectiveConstants.STATUS_ARCHIVED).append("'");
        final List<ObjectiveSetDto> objectiveSets = getHibernateTemplate().find(query.toString());
        return objectiveSets.isEmpty() ? null : objectiveSets.iterator().next();
    }

    public ObjectiveSet findOrgUnitObjectives(Long orgUnitId) {

        StringBuffer query = new StringBuffer("from ObjectiveSet objectiveSet where objectiveSet.organisationUnit.id=:orgUnitId");
        query.append(" and objectiveSet.objectiveDefinition.status ='PUBLISHED'");
        List result = getHibernateTemplate().findByNamedParam(query.toString(), "orgUnitId", orgUnitId);
        ObjectiveSet objectiveSet = null;
        if (!result.isEmpty()) objectiveSet = (ObjectiveSet) result.iterator().next();
        return objectiveSet;
    }

    public ObjectiveDefinition getPublishedDefinition() {
        String query = "from ObjectiveDefinition definition where definition.status='" + ObjectiveConstants.STATUS_PUBLISHED + "'";
        List definitions = getHibernateTemplate().find(query);
        if (definitions == null || definitions.isEmpty()) {
            return null;
        }
        return (ObjectiveDefinition) definitions.iterator().next();
    }

    public Collection<ObjectiveSet> getPublishedCorporateObjectiveSets() {
        StringBuffer query = new StringBuffer("from ObjectiveSet set where set.status = '");
        query.append(ObjectiveConstants.STATUS_PUBLISHED).append("' and set.type='");
        query.append(ObjectiveConstants.CORPORATE_TYPE).append("'");
        return getHibernateTemplate().find(query.toString());
    }

    public List<ObjectiveSet> getArchivedObjectiveSets(Long subjectId) {
        Session session = getSession();
        try {
            //noinspection unchecked
            return session.createCriteria(ObjectiveSet.class)
                    .add(Expression.eq("status", ObjectiveConstants.STATUS_ARCHIVED))
                    .addOrder(Order.desc("id"))
                    .createCriteria("subject")
                    .add(Expression.eq("id", subjectId))
                    .setResultTransformer(new DistinctRootEntityResultTransformer())
                    .list();
        } catch (HibernateException e) {
            throw new DataRetrievalFailureException(e.getMessage(), e);
        }
    }

    public boolean hasArchivedObjectiveSets(Long subjectId) {
        StringBuffer query = new StringBuffer("select count(*) from ObjectiveSet set where status = '");
        query.append(ObjectiveConstants.STATUS_ARCHIVED).append("' and set.subject.id = ").append(subjectId);
        Integer count = (Integer) getHibernateTemplate().find(query.toString()).get(0);
        return count > 0;
    }

    public List<ObjectiveSet> findAll(String objectiveType) {
        Session session = getSession(false);
        try {
            //noinspection unchecked
            return session.createCriteria(ObjectiveSet.class)
                    .add(Expression.eq("type", objectiveType))
                    .setResultTransformer(new DistinctRootEntityResultTransformer())
                    .list();
        } catch (HibernateException e) {
            throw new DataRetrievalFailureException(e.getMessage(), e);
        }
    }

    public void createOrUpdateAssessors(Objective objective) {
        getHibernateTemplate().saveOrUpdateCopy(objective);
    }

    public void createOrUpdateAssessment(ObjectiveAssessment objectiveAssessment) throws TalentStudioException {
        try {
            getHibernateTemplate().saveOrUpdate(objectiveAssessment);
        } catch (DataAccessException e) {
            throw new TalentStudioException(e.getMessage(), e);
        }
    }

    /**
     * Retrieves all approved and not archived objectives the given user needs to assess.
     *
     * @param userId the assessor / logged in user
     * @return all objectives that are designated to assess
     */
    public List<Objective> findAssessorsObjectives(Long userId) throws TalentStudioException {
        try {
            Session session = getSession(false);
            Criteria criteria = session.createCriteria(Objective.class);
            criteria.add(Expression.eq("status", ObjectiveConstants.STATUS_APPROVED))
                    .addOrder(Order.desc("label"))
                    .createCriteria("assessors")
                    .add(Expression.eq("id", userId))
                    .setResultTransformer(new DistinctRootEntityResultTransformer());

            //noinspection unchecked
            return criteria.list();
        } catch (HibernateException e) {
            throw new TalentStudioException(e.getMessage(), e);
        }
    }

    public List<Long> findObjectiveSetOrgUnits(Long subjectId) {
        StringBuffer query = new StringBuffer("select distinct ouSet.organisationUnit.id from ObjectiveSet ouSet,");
        query.append(" Objective ouObjective, Objective subjectObjective, ObjectiveSet subjectSet")
                .append(" where subjectSet.subject.id=").append(subjectId)
                .append(" and subjectSet.status <> '").append(ObjectiveConstants.STATUS_ARCHIVED).append("'")
                .append(" and subjectObjective.objectiveSet.id = subjectSet.id")
                .append(" and subjectObjective.parent.id=ouObjective.id")
                .append(" and ouObjective.objectiveSet.id=ouSet.id");
        return getHibernateTemplate().find(query.toString());
    }

    public boolean isHasApprovableAssessments(Long subjectId) {
        StringBuffer query = new StringBuffer("select count(ass.id) from objectives o, objective_assessments ass, objective_sets os");
        query.append(" where os.subject_id=")
                .append(subjectId)
                .append(" and os.status <> 'ARCHIVED'")
                .append(" and o.objective_set_id=os.id and ass.objective_id=o.node_id and o.status='")
                .append(ObjectiveConstants.STATUS_APPROVED).append("'")
                .append(" and ass.is_approved='F'");
        final int count = getJdbcTemplate().queryForInt(query.toString());
        return count > 0;
    }

    public void archiveObjectives(Long corporateObjectiveSetId) {
        new ArchiveObjectives(getJdbcTemplate()).execute(corporateObjectiveSetId);
    }

    private static class ArchiveObjectives extends StoredProcedure {

        public ArchiveObjectives(JdbcTemplate jdbcTemplate) {
            super(jdbcTemplate, "zynap_org_unit_sp.archive_objectives");
            declareParameter(new SqlParameter("objective_set_id_", Types.INTEGER));
        }

        public void execute(Long id) {
            Map<String, Long> in = new HashMap<String, Long>();
            in.put("objective_set_id_", id);
            execute(in);
        }
    }
}
