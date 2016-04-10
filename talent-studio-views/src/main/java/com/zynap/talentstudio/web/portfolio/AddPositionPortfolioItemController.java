package com.zynap.talentstudio.web.portfolio;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.positions.IPositionService;

/**
* Class or Interface description.
*
* @author aandersson
* @since 28-Apr-2004 17:39:59
* @version 0.1
*/

public class AddPositionPortfolioItemController extends BaseAddPortfolioItemController {

    public AddPositionPortfolioItemController() {
        super();
    }

    protected Node getNode(Long nodeId) throws TalentStudioException {
        return positionService.findById(nodeId);
    }

    public void setPositionService(IPositionService positionService) {
        this.positionService = positionService;
    }

    private IPositionService positionService;
}
