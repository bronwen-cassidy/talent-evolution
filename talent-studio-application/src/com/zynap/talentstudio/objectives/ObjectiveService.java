package com.zynap.talentstudio.objectives;

import com.zynap.domain.IDomainObject;
import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.DefaultService;
import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.organisation.OrganisationUnit;

import java.util.*;

/**
 * User: amark
 * Date: 23-May-2006
 * Time: 17:08:31
 */
public class ObjectiveService extends DefaultService implements IObjectiveService {

    public void setObjectiveDao(IObjectiveDao objectiveDao) {
        this.objectiveDao = objectiveDao;
    }

    protected IFinder getFinderDao() {
        return objectiveDao;
    }

    protected IModifiable getModifierDao() {
        return objectiveDao;
    }

    public void createOrUpdate(ObjectiveSet objectiveSet) throws TalentStudioException {
        if (objectiveSet.getId() == null) {
            create(objectiveSet);
        } else {
            update(objectiveSet);
        }
    }

    public void createOrUpdateAssessors(Objective objective) throws TalentStudioException {
        objectiveDao.createOrUpdateAssessors(objective);
    }

    public void createOrUpdateAssessment(ObjectiveAssessment objectiveAssessment) throws TalentStudioException {
        objectiveDao.createOrUpdateAssessment(objectiveAssessment);
    }

    public void create(IDomainObject domainObject) throws TalentStudioException {

        final ObjectiveSet newObjectiveSet = (ObjectiveSet) domainObject;

        final Date currentDate = new Date();
        final Long userId = UserSessionFactory.getUserSession().getId();

        // set auditing details on objectives themselves
        final Collection newObjectives = newObjectiveSet.getObjectives();
        for (Object newObjective1 : newObjectives) {
            Objective newObjective = (Objective) newObjective1;
            setCreateAuditingDetails(newObjective, userId, currentDate);
        }

        super.create(newObjectiveSet);
    }

    public void update(IDomainObject domainObject) throws TalentStudioException {

        final ObjectiveSet objectiveSet = (ObjectiveSet) domainObject;

        final Date currentDate = new Date();
        final Long userId = UserSessionFactory.getUserSession().getId();

        // set auditing details on objectives themselves
        final Collection newObjectives = objectiveSet.getObjectives();

        for (Object newObjective : newObjectives) {
            Objective objective = (Objective) newObjective;
            if (objective.getId() == null) {
                setCreateAuditingDetails(objective, userId, currentDate);
            } else {
                setUpdateAuditingDetails(objective, userId, currentDate);
            }
        }

        super.update(domainObject);
    }

    public void approve(ObjectiveSet objectiveSet, User user) throws TalentStudioException {

        final String status = ObjectiveConstants.STATUS_APPROVED;
        final Date currentDate = new Date();

        // set auditing details for all child objectives and set as approved as well
        final Collection objectives = objectiveSet.getObjectives();
        for (Object objective1 : objectives) {
            Objective objective = (Objective) objective1;
            objective.setStatus(status);
            objective.setDateApproved(currentDate);
            setUpdateAuditingDetails(objective, user.getId(), currentDate);
        }

        objectiveSet.setStatus(status);
        objectiveSet.setApproved(true);
        update(objectiveSet);
    }

    public Objective findObjective(Long id) throws TalentStudioException {
        return objectiveDao.findObjective(id);
    }

    public List<Objective> findAssessorsObjectives(Long userId) throws TalentStudioException {
        return objectiveDao.findAssessorsObjectives(userId);
    }

    public void approveObjective(Objective objective, User user) throws TalentStudioException {

        objective.setStatus(ObjectiveConstants.STATUS_APPROVED);
        final Date currentDate = new Date();

        setUpdateAuditingDetails(objective, user.getId(), currentDate);
        updateObjective(objective);
    }

    public void updateObjective(Objective objective) throws TalentStudioException {

        final Date currentDate = new Date();
        final User user = UserSessionFactory.getUserSession().getUser();
        setUpdateAuditingDetails(objective, user.getId(), currentDate);

        objectiveDao.updateObjective(objective);
    }

    public void deleteObjective(Objective objective) throws TalentStudioException {
        objectiveDao.deleteObjective(objective);
    }

    public List<ObjectiveSet> findAll(String objectiveType) {
        return objectiveDao.findAll(objectiveType);
    }

    public List<Objective> findCurrentObjectives(Long subjectId) {
        return objectiveDao.findCurrentSubjectObjectives(subjectId);
    }

    public boolean isObjectivesValid(List<OrganisationUnit> primaryOrganisationUnits, Long subjectId) {
        List<Long> ouIds = objectiveDao.findObjectiveSetOrgUnits(subjectId);
        for (Long ouId : ouIds) {
            if(ouId != null && !primaryOrganisationUnits.contains(new OrganisationUnit(ouId))) return false;
        }        
        return true;
    }

    public boolean isHasApprovableAssessments(Long subjectId) {
        return objectiveDao.isHasApprovableAssessments(subjectId);
    }

    public Integer countNumAssessments(Long userId) throws TalentStudioException {
        return objectiveDao.findAssessorsObjectives(userId).size();
    }

    public ObjectiveSet findCurrentObjectiveSet(Long subjectId) {
        return objectiveDao.findCurrentObjectiveSet(subjectId);    
    }

    public ObjectiveSetDto findCurrentObjectiveSetDto(Long subjectId) {
        return objectiveDao.findCurrentObjectiveSetDto(subjectId);
    }

    public void createOrUpdateObjectives(Collection objectives) throws TalentStudioException {
        for (Object objective1 : objectives) {
            Objective objective = (Objective) objective1;
            updateObjectiveAssessment(objective);
        }
    }

    /**
     * Updates the objective by assigning the objective assessment details.
     *
     * @param objective the objective whose assessment we wish
     * @throws TalentStudioException
     */
    public void updateObjectiveAssessment(Objective objective) throws TalentStudioException {
        Set<ObjectiveAssessment> assessments = objective.getAssessments();
        for(ObjectiveAssessment objectiveAssessment : assessments) {
            if (objectiveAssessment.isApproved() && objectiveAssessment.getDateApproved() == null)
                objectiveAssessment.setDateApproved(new Date());
        }
        objectiveDao.updateObjective(objective);
    }

    public ObjectiveSet getPublishedCorporateObjectiveSet() {
        Collection<ObjectiveSet> publishedSets = objectiveDao.getPublishedCorporateObjectiveSets();
        if(publishedSets.isEmpty()) {
            return null;
        }
        return publishedSets.iterator().next();
    }

    public List getArchivedObjectiveSets(Long subjectId) {
        return objectiveDao.getArchivedObjectiveSets(subjectId);
    }

    public boolean hasArchivedObjectiveSets(Long subjectId) {
        return objectiveDao.hasArchivedObjectiveSets(subjectId);
    }

    public ObjectiveSet publishObjectives(Long id) throws TalentStudioException {

        ObjectiveSet objectiveSet = (ObjectiveSet) findById(id);
        objectiveSet.setStatus(STATUS_PUBLISHED);
        Date currentDate = new Date();
        objectiveSet.setPublishedDate(currentDate);
        objectiveDao.update(objectiveSet);
        return objectiveSet;
    }

    public ObjectiveDefinition getPublishedDefinition() {
        return objectiveDao.getPublishedDefinition();
    }

    public void expireCurrentObjectives() throws TalentStudioException {
        // get the active objective definition, there is only ever one active one at any given moment in time
        Collection<ObjectiveSet> corporateObjectiveSets = objectiveDao.getPublishedCorporateObjectiveSets();
        
        for (ObjectiveSet corporateObjectiveSet : corporateObjectiveSets) {
            final Date expiryDate = corporateObjectiveSet.getExpiryDate();
            if (expiryDate != null) {
                Date now = new Date();
                if (expiryDate.before(now) || expiryDate.equals(now)) {
                    expire(corporateObjectiveSet.getId());
                }
            }
        }
    }

    public void expire(Long objectiveSetId) throws TalentStudioException {
        objectiveDao.archiveObjectives(objectiveSetId);
    }

    private void setUpdateAuditingDetails(Objective objective, Long user, Date date) {
        objective.setDateUpdated(date);
        objective.setUpdatedById(user);
        final boolean shouldSetDateApproved = objective.isApproved() && objective.getDateApproved() == null;
        if (shouldSetDateApproved) {
            objective.setDateApproved(date);
        }
        if(objective.getManagerAssessment() != null) {
            ObjectiveAssessment assessment = objective.getManagerAssessment();
            if(assessment.isApproved() && assessment.getDateApproved() == null) assessment.setDateApproved(date);
        }
    }

    private void setCreateAuditingDetails(Objective objective, Long user, Date date) {

        objective.setDateCreated(date);
        objective.setCreatedById(user);
        setUpdateAuditingDetails(objective, user, date);
    }

    private IObjectiveDao objectiveDao;
}
