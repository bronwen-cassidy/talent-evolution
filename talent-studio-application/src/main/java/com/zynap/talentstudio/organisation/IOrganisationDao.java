/**
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 1.0
 * @since 08-Jul-2005 16:29:57
 */
public interface IOrganisationDao extends IModifiable, IFinder {

    OrganisationUnit findByID(Long organisationUnitId) throws TalentStudioException;

    List<OrganisationUnit> findOrgUnitTree(Long organisationUnitId) throws TalentStudioException;

    List<OrganisationUnit> findSecureOrgUnitTree(Long userId, Long permitId);

    List findOrgUnitsBySubjectId(Long subjectId);

    List<OrganisationUnit> findValidParents(Long orgUnitId, Long userId, Long permitId);

    List<OrganisationUnit> findAllSecure(Long id, Long id1);

    List search(OrgUnitSearchQuery query, Long userId, Long permitId);

    OrganisationUnit findOrgUnitBySubjectUserId(Long userId);

    OrganisationUnit findOrgUnitByUserId(Long userId, Long permitId);

    OrganisationUnit findOrgUnitByPositionId(Long positionId);

    List<OrganisationUnit> findDecendants(Long organisationUnitId);

    void deleteOrganisationCascade(OrganisationUnit domainObject) throws TalentStudioException;
}
