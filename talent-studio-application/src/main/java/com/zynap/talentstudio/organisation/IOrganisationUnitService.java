package com.zynap.talentstudio.organisation;


import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.IZynapService;

import java.util.Collection;
import java.util.List;

/**
 * User: amark
 * Date: 13-Jul-2005
 * Time: 15:57:09
 */
public interface IOrganisationUnitService extends IZynapService {

    OrganisationUnit findById(Long orgUnitId) throws TalentStudioException;

    /**
     * Find descendents of specified org unit.
     * <br/> Not filtered by security.
     *
     *
     *
     *
     * @param organisationUnitId The org unit id
     * @return List of {@link com.zynap.talentstudio.organisation.OrganisationUnit} objects.
     * @throws TalentStudioException An exception
     */
    List<OrganisationUnit> findOrgUnitTree(final Long organisationUnitId) throws TalentStudioException;

    /**
     * Find complete org unit list.
     * <br/> The org units that the user has no access to will have the has access flag set to false.
     *
     *
     * @param userId The org unit id
     * @return List of {@link com.zynap.talentstudio.organisation.OrganisationUnit} objects.
     */
    List<OrganisationUnit> findSecureOrgUnitTree(Long userId);
    
    /**
     * Find complete org unit list with org units that user has no access to removed.
     *
     * @return List of {@link com.zynap.talentstudio.organisation.OrganisationUnit} objects.
     */
    List<OrganisationUnit> findAllSecure();

    /**
     * Find org units that can be parents of specified org unit - use the userId to filter the org units based on user security access.
     *
     * @param orgUnitId The org unit id
     * @param userId    The user id
     * @return List of {@link com.zynap.talentstudio.organisation.OrganisationUnit} objects.
     */
    List<OrganisationUnit> findValidParents(Long orgUnitId, Long userId);

    /**
     * Find org units - use the userId to filter the org units based on user security access.
     *
     *
     * @param userId The user id
     * @return List of {@link com.zynap.talentstudio.organisation.OrganisationUnit} objects.
     */
    List<OrganisationUnit> findValidParents(Long userId);

    Collection search(Long userId, OrgUnitSearchQuery query);

    void updateMerge(OrganisationUnit mergedOrganisationUnit, OrganisationUnit defunctOrganisationUnit) throws TalentStudioException;

    /**
     * Find org unit of specified position.
     *
     * @param positionId
     * @return OrganisationUnit
     * @throws TalentStudioException
     */
    OrganisationUnit findOrgUnitByPositionId(String positionId) throws TalentStudioException;

    /**
     * Find org unit of specified subject.
     * <br/> Finds org unit for subject's current position.
     *
     * @param subjectId
     * @return OrganisationUnit
     */
    OrganisationUnit findOrgUnitBySubjectId(String subjectId);

    /**
     * Find org unit of specified user.
     * <br/> Finds org unit for user's associated subject's current position.
     *
     * @param userId
     * @return OrganisationUnit
     */
    OrganisationUnit findOrgUnitBySubjectUserId(String userId);

    void deleteOrgUnitCascade(OrganisationUnit organisationUnit) throws TalentStudioException;

    OrganisationUnit findOrgUnitByUser(String userId);
}
