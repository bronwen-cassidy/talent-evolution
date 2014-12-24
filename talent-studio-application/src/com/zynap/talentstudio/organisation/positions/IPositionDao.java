/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.positions;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.IAssociation;
import com.zynap.talentstudio.organisation.subjects.SubjectAssociation;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IPositionDao extends IFinder, IModifiable {

    /**
     * Finds all the positions that have the specified position as their root.
     *
     * @param positionId The position id
     * @return List containing {@link com.zynap.talentstudio.organisation.positions.Position} objects
     * @throws com.zynap.exception.TalentStudioException
     *
     */
    List<Position> findDescendents(Long positionId) throws TalentStudioException;

    /**
     * Cheaper version of {@link #findDescendents(Long)} 
     *
     * @param id the id of the root position
     * @return only the ids of the positions who are decendants of the given position id
     * @throws TalentStudioException
     */
    List<Long> findDescendentIds(Long id) throws TalentStudioException;

    /**
     * Finds the reporting structure for the given position.
     *
     * @param positionId
     * @param principalId
     * @param viewPositionPermitId
     * @param viewOrgUnitPermitId
     * @param viewSubjectPermitId
     * @return Position
     */    
    Position findReportStructureFor(Long positionId, Long principalId, Long viewPositionPermitId, Long viewOrgUnitPermitId, Long viewSubjectPermitId) throws TalentStudioException;

    Position findUsersPosition(Long userId, Long orgUnitId);

    /**
     * Check access to the position and the positions and subjects associated to it.
     *
     * @param position             The position
     * @param userId               The user id
     * @param viewPositionPermitId The permit the user must have in order to have access to the positions
     * @param viewOrgUnitPermitId  The permit the user must have in order to have access to the org unit the position is associated with
     * @param viewSubjectPermitId  The permit the user must have in order to have access to the subject
     */
    void checkAccess(Position position, Long userId, Long viewPositionPermitId, Long viewOrgUnitPermitId, Long viewSubjectPermitId);

    /**
     * Find positions in specified org unit that can be parent of specified position.
     * <br/> If the positionId is not null it is used to remove children of the position in the org unit to avoid cyclic references.
     * <br/> Positions are also filtered by security.
     *
     *
     * @param orgUnitId
     * @param positionId (can be null.)
     * @param userId
     * @param permitId
     * @return List containing {@link com.zynap.talentstudio.organisation.positions.Position} objects
     */
    List<Position> findAvailableParentsForPosition(Long orgUnitId, Long positionId, Long userId, Long permitId);

    boolean checkNodeAccess(Node position, Long id, Long viewPositionPermitId);

    boolean checkNodeViewAccess(Node node, Long userId, Long viewPositionPermitId);

    void deleteAssociation(IAssociation association);

    void deletePositions(Long positionId) throws TalentStudioException;

    void refreshState(Position position) throws TalentStudioException;

    void updateCurrentHoldersInfo(Long positionId);
}
