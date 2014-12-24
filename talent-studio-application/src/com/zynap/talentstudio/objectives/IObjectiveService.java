package com.zynap.talentstudio.objectives;

import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.IZynapService;
import com.zynap.talentstudio.organisation.OrganisationUnit;

import java.util.Collection;
import java.util.List;


/**
 * User: amark
 * Date: 23-May-2006
 * Time: 17:06:02
 */
public interface IObjectiveService extends IZynapService, ObjectiveConstants {

    void createOrUpdate(ObjectiveSet objectiveSet) throws TalentStudioException;

    void createOrUpdateAssessors(Objective objective) throws TalentStudioException;

    void createOrUpdateAssessment(ObjectiveAssessment objectiveAssessment) throws TalentStudioException;

    void approve(ObjectiveSet objectiveSet, User user) throws TalentStudioException;

    Objective findObjective(Long id) throws TalentStudioException;

    /**
     * Finds the objectives that the given user needs to assess.
     *
     * @param userId the logged in user
     * @return the list of approved and not archived objectives this user needs to assess
     */
    List<Objective> findAssessorsObjectives(Long userId) throws TalentStudioException;

    void approveObjective(Objective objective, User user) throws TalentStudioException;

    void updateObjective(Objective objective) throws TalentStudioException;

    void deleteObjective(Objective objective) throws TalentStudioException;

    /**
     *
     * @param objectiveType the type of objective
     *
     * @return a list all all objectiveSets determined by the type field, which may
     * be one of {@link ObjectiveConstants#CORPORATE_TYPE},
     * {@link com.zynap.talentstudio.objectives.ObjectiveConstants#ORG_UNIT_TYPE} or
     * {@link com.zynap.talentstudio.objectives.ObjectiveConstants#USER_TYPE}
     */
    List<ObjectiveSet> findAll(String objectiveType);

    ObjectiveSet findCurrentObjectiveSet(Long subjectId);

    ObjectiveSetDto findCurrentObjectiveSetDto(Long subjectId);

    ObjectiveSet getPublishedCorporateObjectiveSet();

    List getArchivedObjectiveSets(Long subjectId);

    boolean hasArchivedObjectiveSets(Long subjectId);

    ObjectiveSet publishObjectives(Long id) throws TalentStudioException;

    ObjectiveDefinition getPublishedDefinition();

    void createOrUpdateObjectives(Collection assessments) throws TalentStudioException;

    void updateObjectiveAssessment(Objective objectiveAssessment) throws TalentStudioException;

    void expireCurrentObjectives() throws TalentStudioException;

    void expire(Long objectiveSetId) throws TalentStudioException;

    /**
     *
     * @param subjectId the subject id whose (current) objectives we are interested in
     * @return list of the objectives for the given subject.
     */
    List<Objective> findCurrentObjectives(Long subjectId);

    boolean isObjectivesValid(List<OrganisationUnit> primaryOrganisationUnits, Long subjectId);

    boolean isHasApprovableAssessments(Long subjectId);

    Integer countNumAssessments(Long userId) throws TalentStudioException;
}
