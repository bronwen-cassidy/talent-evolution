/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.objectives;

import com.zynap.talentstudio.objectives.Objective;
import com.zynap.talentstudio.objectives.ObjectiveSet;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 08-Mar-2007 09:12:43
 */
public class EditCorporateObjectiveController extends BaseCorporateObjectiveController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        Long id = RequestUtils.getRequiredLongParameter(request, "id");
        ObjectiveSet objectiveSet = (ObjectiveSet) objectiveService.findById(id);

        List<ObjectiveWrapperBean> objectives = new ArrayList<ObjectiveWrapperBean>();
        Collection corporateObjectives = objectiveSet.getObjectives();

        for (Iterator iterator = corporateObjectives.iterator(); iterator.hasNext();) {
            Objective objective = (Objective) iterator.next();
            ObjectiveWrapperBean wrapper = new ObjectiveWrapperBean(objective);
            objectives.add(wrapper);
        }

        return new CorporateObjectivesFormBean(objectives, objectiveSet);
    }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        CorporateObjectivesFormBean bean = (CorporateObjectivesFormBean) command;
        ObjectiveSet objectiveSet = bean.getModifiedObjectiveSet();
        try {
            objectiveService.update(objectiveSet);
        } catch (DataAccessException e) {
            errors.rejectValue("objectiveSet.label", "error.duplicate.label");
            return showPage(request, errors, 1);
        }
        return new ModelAndView(new ZynapRedirectView(getSuccessView(), CORPORATE_ID_PARAM, objectiveSet.getId()));
    }

    protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return new ModelAndView(new ZynapRedirectView(getCancelView(), CORPORATE_ID_PARAM, ((CorporateObjectivesFormBean) command).getObjectiveSet().getId()));
    }
}
