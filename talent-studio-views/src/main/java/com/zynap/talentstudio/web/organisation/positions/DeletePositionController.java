/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.positions;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.organisation.DeleteNodeController;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class DeletePositionController extends DeleteNodeController {

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        Position position = (Position) command;
        Map<String, Object> refData = new HashMap<String, Object>();
        List reportingPositions = positionService.findDescendents(position.getId());
        refData.put(ControllerConstants.POSITIONS, reportingPositions);
        return refData;
    }

    /**
     * Get node.
     *
     * @param nodeId The node id
     * @return A Node
     * @throws com.zynap.exception.TalentStudioException
     */
    protected Node getNode(Long nodeId) throws TalentStudioException {
        return positionService.findByID(nodeId);
    }

    /**
     * Delete node.
     *
     * @param command The command object
     * @throws com.zynap.exception.TalentStudioException
     */
    protected void deleteNode(Object command) throws TalentStudioException {
        Position position = (Position) command;
        positionService.deletePosition(position.getId());
        positionService.refreshState(position);
    }

    public void setPositionService(IPositionService positionService) {
        this.positionService = positionService;
    }

    private IPositionService positionService;
}
