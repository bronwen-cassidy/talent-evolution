/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.objectives;

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.objectives.ObjectiveConstants;
import com.zynap.talentstudio.objectives.ObjectiveSet;
import com.zynap.talentstudio.objectives.Objective;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.NodeInfo;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Controller that displays objectives (completed and archived objectives for a subject)
 *
 * @author bcassidy
 * @version 0.1
 * @since 28-June-2007 10:56:17
 */
public class ViewSubjectObjectivesController extends ViewObjectivesController implements ObjectiveConstants {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        ObjectiveSetFormBean wrapperBean = new ObjectiveSetFormBean();
        Long subjectId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.NODE_ID_PARAM);
        Subject subject = subjectService.findById(subjectId);
        ObjectiveSet objectiveSet = objectiveService.findCurrentObjectiveSet(subject.getId());

        Collection<ObjectiveSet> sets = subject.getObjectiveSets();
        if (objectiveSet != null && !objectiveSet.isComplete()) {
            sets.remove(objectiveSet);
        }

        // objectiveSet does not exist or it is not complete we remove it from the view, unless we are the administrator
        UserSession userSession = ZynapWebUtils.getUserSession(request);
        if(objectiveSet == null || (!userSession.isAdministrator() && !objectiveSet.isComplete())) {
            objectiveSet = sets.iterator().next();
        }
        
        wrapperBean.setObjectiveSet(objectiveSet);
        wrapperBean.setUserId(ZynapWebUtils.getUserId(request));
        wrapperBean.setArchivedObjectiveSets(new ArrayList<ObjectiveSet>(sets));

        List<ObjectiveWrapperBean> wrappedObjectives = wrapObjectives(objectiveSet.getObjectiveDefinition(), objectiveSet.getObjectives(), new ArrayList<Objective>());
        wrapperBean.setObjectives(wrappedObjectives);
        setObjectiveAssessments(wrapperBean, wrappedObjectives);

        // node info
        NodeInfo nodeInfo = new NodeInfo(subject, subject.getPrimaryPositions(), subject.getPrimaryOrganisationUnits());
        wrapperBean.setNodeInfo(nodeInfo);
        return wrapperBean;
    }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return showPage(request, errors, VIEW_OBJECTIVES_IDX);
    }
}
