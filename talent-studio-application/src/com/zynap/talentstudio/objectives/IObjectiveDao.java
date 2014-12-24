package com.zynap.talentstudio.objectives;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;

import java.util.Collection;
import java.util.List;

/**
 * User: amark
 * Date: 23-May-2006
 * Time: 17:01:29
 */
public interface IObjectiveDao extends IModifiable, IFinder {

    Objective findObjective(Long id) throws TalentStudioException;

    void updateObjective(Objective objective) throws TalentStudioException;

    void deleteObjective(Objective objective) throws TalentStudioException;

    List<ObjectiveSet> findAll(String objectiveType);

    /**
     * The list of the given subjects current, approved objectives.
     *
     * @param subjectId the id of the subject
     * @return the list of objectives.
     */
    List<Objective> findCurrentSubjectObjectives(Long subjectId);

    ObjectiveSet findCurrentObjectiveSet(Long subjectId);

    ObjectiveSetDto findCurrentObjectiveSetDto(Long subjectId);

    ObjectiveDefinition getPublishedDefinition();

    /**
     * Return the orgunits published objectives.
     *
     * @param orgUnitId the organisation unit id
     * @return the set of published objectives for the given orgunit
     */
    ObjectiveSet findOrgUnitObjectives(Long orgUnitId);

    Collection<ObjectiveSet> getPublishedCorporateObjectiveSets();

    List<ObjectiveSet> getArchivedObjectiveSets(Long subjectId);

    boolean hasArchivedObjectiveSets(Long subjectId);

    void createOrUpdateAssessors(Objective objective);

    void createOrUpdateAssessment(ObjectiveAssessment objectiveAssessment) throws TalentStudioException;

    /**
     * Retrieves all approved and not archived objectives the given user needs to assess.
     *
     * @param userId the assessor / logged in user
     * @return all objectives that are designated to assess
     */
    List<Objective> findAssessorsObjectives(Long userId) throws TalentStudioException;

    List<Long> findObjectiveSetOrgUnits(Long subjectId);

    boolean isHasApprovableAssessments(Long subjectId);

    void archiveObjectives(Long corporateObjectiveSetId);

}
