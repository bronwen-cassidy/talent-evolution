/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.objectives;

import com.zynap.talentstudio.objectives.Objective;
import com.zynap.talentstudio.objectives.ObjectiveDefinition;
import com.zynap.talentstudio.objectives.ObjectiveSet;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 08-Mar-2007 09:12:43
 */
public class AddCorporateObjectiveController extends BaseCorporateObjectiveController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        ObjectiveDefinition objectiveDefinition = objectiveService.getPublishedDefinition();

        final ObjectiveSet objectiveSet = new ObjectiveSet();
        objectiveSet.setType(CORPORATE_TYPE);

        CorporateObjectivesFormBean bean = new CorporateObjectivesFormBean();
        objectiveDefinition.addObjectiveSet(objectiveSet);
        bean.setObjectiveSet(objectiveSet);

        ObjectiveWrapperBean wrapperBean = new ObjectiveWrapperBean();
        wrapperBean.setObjective(new Objective());
        bean.addObjective(wrapperBean);
        return bean;
    }

    protected ModelAndView processFinishInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {
        CorporateObjectivesFormBean bean = (CorporateObjectivesFormBean) command;
        ObjectiveSet createdSet = bean.getModifiedObjectiveSet();
        createdSet.setType(CORPORATE_TYPE);
        try {
            objectiveService.create(createdSet);
            return new ModelAndView(new ZynapRedirectView(getSuccessView()));
        } catch (DataAccessException e) {
            e.printStackTrace();
            errors.rejectValue("objectiveSet.label", "error.duplicate.label");
            return showPage(request, (BindException) errors, 1);
        }
    }
}
