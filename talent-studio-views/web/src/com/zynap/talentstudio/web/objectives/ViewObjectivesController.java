/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.objectives;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.common.lookups.LookupType;
import com.zynap.talentstudio.objectives.Objective;
import com.zynap.talentstudio.objectives.ObjectiveAssessment;
import com.zynap.talentstudio.objectives.ObjectiveConstants;
import com.zynap.talentstudio.objectives.ObjectiveDefinition;
import com.zynap.talentstudio.objectives.ObjectiveSet;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.NodeInfo;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.domain.admin.User;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Controller that adds objectives to subjects.
 *
 * @author bcassidy
 * @version 0.1
 * @since 24-May-2006 13:14:20
 */
public class ViewObjectivesController extends DefaultObjectivesController {

    protected boolean isFormSubmission(HttpServletRequest request) {
        return super.isFormSubmission(request) || isDisplayTagSort(request);
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        setCancelView(HistoryHelper.getBackURL(request));
        setSuccessView(HistoryHelper.getBackURL(request));

        Subject subject = getTargetSubject(request);
        ObjectiveSetFormBean wrapperBean = new ObjectiveSetFormBean();

        NodeInfo nodeInfo = new NodeInfo(subject, subject.getPrimaryPositions(), subject.getPrimaryOrganisationUnits());
        wrapperBean.setNodeInfo(nodeInfo);
        wrapperBean.setUserId(ZynapWebUtils.getUserId(request));

        List<ObjectiveSet> objectiveSets = new ArrayList<ObjectiveSet>(subject.getObjectiveSets());

        // if it is null means there are no current objectives and no archived objectives
        if (!objectiveSets.isEmpty()) {
            ObjectiveSet objectiveSet = objectiveService.findCurrentObjectiveSet(subject.getId());
            // no current but we know there are archived sets
            if (objectiveSet == null) objectiveSet = objectiveSets.iterator().next();

            wrapperBean.setObjectiveSet(objectiveSet);
            wrapperBean.setArchivedObjectiveSets(objectiveSets);
            List<Objective> objectives = objectiveSet.getObjectives();
            List<ObjectiveWrapperBean> wrappedObjectives = wrapObjectives(objectiveSet.getObjectiveDefinition(), objectives, new ArrayList<Objective>());
            wrapperBean.setObjectives(wrappedObjectives);
            setObjectiveAssessments(wrapperBean, wrappedObjectives);
        }
        return wrapperBean;
    }

    // no validation required here just check approved info
    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

        ObjectiveSetFormBean wrapper = (ObjectiveSetFormBean) command;

        if (getTargetPage(request, page) == VIEW_ARCHIVED_IDX) {
            Long selectedObjectiveSetId = RequestUtils.getLongParameter(request, "objectiveSetId", null);
            if (selectedObjectiveSetId != null) {
                ObjectiveSet objectiveSet = (ObjectiveSet) objectiveService.findById(selectedObjectiveSetId);
                wrapper.setObjectiveSet(objectiveSet);
                List<Objective> objectives = objectiveSet.getObjectives();
                ObjectiveDefinition definition = objectiveSet.getObjectiveDefinition();

                List<ObjectiveWrapperBean> wrappedObjectives = wrapObjectives(definition, objectives, new ArrayList<Objective>());
                wrapper.setObjectives(wrappedObjectives);
                setObjectiveAssessments(wrapper, wrappedObjectives);
                if (objectiveSet.isArchived()) wrapper.setAssessmentsApproved(true);
            }
        } else {
            wrapper.setAssessmentsApproved(RequestUtils.getBooleanParameter(request, "assessmentsApproved", false));
            wrapper.setSendEmail(RequestUtils.getBooleanParameter(request, "sendEmail", false));
            String activeTab = request.getParameter("activeTab");
            if (StringUtils.hasText(activeTab)) wrapper.setActiveTab(activeTab);
        }
    }

    // only saves the objectiveAssessments
    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        ObjectiveSetFormBean formBean = (ObjectiveSetFormBean) command;
        List<ObjectiveWrapperBean> objectiveWrappers = formBean.getObjectives();
        ObjectiveSet objectiveSet = formBean.getObjectiveSet();
        boolean assessmentsApproved = formBean.isAssessmentsApproved();
        if (assessmentsApproved) objectiveSet.setStatus(ObjectiveConstants.STATUS_COMPLETE);

        for (ObjectiveWrapperBean wrapperBean : objectiveWrappers) {
            ObjectiveAssessment assessment = wrapperBean.getAssessment().getModifiedAssessment();
            Objective objective = wrapperBean.getObjective();
            if (assessmentsApproved) objective.setStatus(ObjectiveConstants.STATUS_COMPLETE);
            assessment.setApproved(assessmentsApproved);
        }

        formBean.setActiveTab(OBJECTIVES_TAB);
        objectiveService.update(objectiveSet);
        if (formBean.isSendEmail()) sendEmail(request, formBean);
        return showPage(request, errors, VIEW_OBJECTIVES_IDX);
    }

    public void setLookupManager(ILookupManager lookupManager) {
        this.lookupManager = lookupManager;
    }

    protected void setObjectiveAssessments(ObjectiveSetFormBean wrapperBean, List<ObjectiveWrapperBean> objectives) throws TalentStudioException {

        LookupType rating = lookupManager.findLookupType(ObjectiveConstants.RATING_LOOKUP_TYPE);
        ObjectiveSet objectiveSet = wrapperBean.getObjectiveSet();

        boolean assessmentsApproved = true;
        boolean archived = objectiveSet.isArchived();

        for (ObjectiveWrapperBean objectiveWrapperBean : objectives) {
            Objective objective = objectiveWrapperBean.getObjective();
            ObjectiveAssessment assessment = objective.getManagerAssessment();
            if (assessment == null) {
                assessment = new ObjectiveAssessment();
                objective.addAssessment(assessment);
            }
            Set<ObjectiveAssessment> objectiveAssessments = new HashSet<ObjectiveAssessment>(objective.getAssessments());
            objectiveAssessments.remove(assessment);
            for (ObjectiveAssessment objAssessment : objectiveAssessments) {
                objectiveWrapperBean.add(new AssessmentFormBean(objAssessment));
            }
            // if any one of these is false the overall is false
            if (archived) assessment.setApproved(true);
            if (assessmentsApproved) assessmentsApproved = assessment.isApproved();
            objectiveWrapperBean.setAssessment(new AssessmentFormBean(assessment, rating));
        }

        wrapperBean.setAssessmentsApproved(archived || assessmentsApproved);
    }

    void sendEmail(HttpServletRequest request, ObjectiveSetFormBean wrapper) {
        
        List<User> recipients = new ArrayList<User>();
        Subject subject;
        User from = ZynapWebUtils.getUser(request);
        
        if (personnalObjectives) {
            // recipients are the managers
            try {
                subject = subjectService.findByUserId(ZynapWebUtils.getUserId(request));
                recipients.addAll(subject.getManagers());
            } catch (TalentStudioException e) {
                logger.debug(e.getMessage(), e);
                return;
            }
        } else {
            // superior is browsing email recipient is the objective owner
            subject = wrapper.getNodeInfo().getSubject();
            User user = subject.getUser();
            if(user != null) recipients.add(user);
        }
        if (!recipients.isEmpty()) {
            mailNotification.send("", from, subject, recipients.toArray(new User[recipients.size()]));
        }
    }

    private ILookupManager lookupManager;

    private static final int VIEW_ARCHIVED_IDX = 4;
    static final int VIEW_OBJECTIVES_IDX = 0;
    static final String OBJECTIVES_TAB = "objectives";
}
