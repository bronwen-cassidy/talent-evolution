/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.positions;

import com.zynap.domain.orgbuilder.PositionSearchQuery;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.IZynapService;
import com.zynap.talentstudio.organisation.Node;

import java.util.List;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IPositionService extends IZynapService {

    /**
     * Id of default position.
     */
    final int POSITION_DEFAULT = 1;

    /**
     * Check access to the position and its associated artefacts.
     *
     * @param position The position
     */
    void checkAccess(Position position);

    boolean checkNodeAccess(Node position, Long userId);

    boolean checkNodeViewAccess(Node node, Long userId);

    /**
     * Finds the position given the id.
     * <p/>
     * The method returns a completed valid object with both dynamic attributePreferences and associations.
     *
     * @param id
     * @return Position
     * @throws TalentStudioException
     */
    Position findByID(Long id) throws TalentStudioException;

    /**
     * Search for positions - no restrictions on whether they are active or not.
     *
     * @param principalId The id of the user searching for subjects
     * @param searchQuery The SubjectSearchQuery containing all the parameters
     * @return Collection of Subjects
     * @throws TalentStudioException
     */
    List search(Long principalId, PositionSearchQuery searchQuery) throws TalentStudioException;

    List<PositionDto> searchPositions(Long userId, PositionSearchQuery searchQuery) throws TalentStudioException;

    /**
     * Finds children and grandchildren (etc) for specified position.
     *
     * @param positionId
     * @return List containing {@link com.zynap.talentstudio.organisation.positions.Position} objects
     * @throws com.zynap.exception.TalentStudioException
     *
     */
    List<Position> findDescendents(Long positionId) throws TalentStudioException;

    List<Long> findDescendentIds(Long positionId) throws TalentStudioException;

    /**
     * Finds the reporting structure for the given position.
     *
     * @param positionId
     * @param principalId
     * @return Position
     */    
    Position findReportStructureFor(Long positionId, Long principalId) throws TalentStudioException;

    Position findUsersPosition(Long userId, Long orgUnitId);

    /**
     * Find positions in specified org unit that can be parent of specified position.
     * <br/> If the positionId is not null it is used to remove children of the position in the org unit to avoid cyclic references.
     * <br/> Positions are also filtered by security.
     *
     *
     * @param orgUnitId
     * @param positionId (can be null.)
     * @param userId
     * @return List containing {@link com.zynap.talentstudio.organisation.positions.Position} objects
     */
    List<Position> findAvailableParentsForPosition(Long orgUnitId, Long positionId, Long userId);

    void deletePosition(Long positionId) throws TalentStudioException;

    void deleteAll(List<Position> positions) throws TalentStudioException;

    void refreshState(Position position) throws TalentStudioException;

    void updateCurrentHoldersInfo(Long id);

}
