/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.objectives;

import com.zynap.talentstudio.objectives.IObjectiveService;
import com.zynap.talentstudio.objectives.ObjectiveConstants;
import com.zynap.talentstudio.web.controller.ZynapDefaultFormController;

import javax.servlet.http.HttpServletRequest;

/**
 * Simple class that just returns a list of objective definitions.
 *
 * @author bcassidy
 * @version 0.1
 * @since 07-Mar-2007 15:27:59
 */
public class ListCorporateObjectivesController extends ZynapDefaultFormController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        CorporateObjectivesFormBean bean = new CorporateObjectivesFormBean();
        bean.setObjectiveSets(objectiveService.findAll(ObjectiveConstants.CORPORATE_TYPE));
        return bean;
    }

    public void setObjectiveService(IObjectiveService objectiveService) {
        this.objectiveService = objectiveService;
    }

    private IObjectiveService objectiveService;
}
