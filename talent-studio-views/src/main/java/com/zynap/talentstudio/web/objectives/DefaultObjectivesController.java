/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.objectives;

import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.mail.IMailNotification;
import com.zynap.talentstudio.objectives.IObjectiveService;
import com.zynap.talentstudio.objectives.Objective;
import com.zynap.talentstudio.objectives.ObjectiveConstants;
import com.zynap.talentstudio.objectives.ObjectiveDefinition;
import com.zynap.talentstudio.objectives.ObjectiveSet;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.NodeInfo;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.organisation.attributes.DynamicAttributesHelper;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

/**
 * Default controller used for add, edit objectives, default implements to edit.
 * This is the class that handles the managers view when dealing with his staffs
 * goals.
 *
 * @author bcassidy
 * @version 0.1
 * @since 24-May-2006 13:14:20
 */
public class DefaultObjectivesController extends DefaultWizardFormController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        setCancelView(HistoryHelper.getBackURL(request));
        setSuccessView(HistoryHelper.getBackURL(request));

        ObjectiveDefinition objectiveDefinition = objectiveService.getPublishedDefinition();
        ObjectiveSet corporateSet = objectiveService.getPublishedCorporateObjectiveSet();

        // lazy load the lookupValues of the dynamic attributes on the objectiveDefinition
        objectiveDefinition.initLazy();
        Subject subject = getTargetSubject(request);

        // subjects current goal set
        ObjectiveSet subjectsCurrentGoals = getSubjectObjectives(objectiveDefinition, corporateSet, subject);

        // set the objective set against the bean
        ObjectiveSetFormBean wrapperBean = new ObjectiveSetFormBean();
        wrapperBean.setObjectiveSet(subjectsCurrentGoals);
        wrapperBean.setUserId(ZynapWebUtils.getUserId(request));

        // assigns the users organisation unit objectives to the wrapper.
        List<OrganisationUnit> organisationUnits = assignOrganisationObjectives(subject, wrapperBean);

        // verify the subjects goals and the organisation units they are in are valid
        List<Objective> invalidObjectives = subjectsCurrentGoals.getInvalidObjectives(organisationUnits);
        boolean objectivesValid = invalidObjectives.isEmpty();
        if (!objectivesValid) {
            subjectsCurrentGoals.setStatus(ObjectiveConstants.STATUS_OPEN);
            subjectsCurrentGoals.setActionRequired(ObjectiveConstants.ACTION_REQUIRED_NOACTION);
            subjectsCurrentGoals.setActionGroup(ObjectiveConstants.ACTION_GROUP_UNASSIGNED);
            subjectsCurrentGoals.setApproved(false);
        }

        // get the node info for the subject
        NodeInfo nodeInfo = new NodeInfo(subject, subject.getPrimaryPositions(), organisationUnits);
        wrapperBean.setNodeInfo(nodeInfo);

        // wraps the objectives into the objective wrapper ObjectiveWrapperBean
        wrapObjectives(objectiveDefinition, subjectsCurrentGoals, wrapperBean, invalidObjectives);
        wrapperBean.setPersonnalObjectives(personnalObjectives);
        return wrapperBean;
    }

    private void wrapObjectives(ObjectiveDefinition objectiveDefinition, ObjectiveSet subjectsCurrentGoals, ObjectiveSetFormBean wrapperBean, List<Objective> invalidObjectives) {
        List<Objective> objectives = subjectsCurrentGoals.getObjectives();
        List<ObjectiveWrapperBean> wrappedObjectives = wrapObjectives(objectiveDefinition, objectives, invalidObjectives);
        wrapperBean.setObjectives(wrappedObjectives);
    }

    private List<OrganisationUnit> assignOrganisationObjectives(Subject subject, ObjectiveSetFormBean wrapperBean) {
        // organisation unit sets
        List<OrganisationUnit> organisationUnits = subject.getPrimaryOrganisationUnits();
        for (OrganisationUnit unit : organisationUnits) {
            if (unit.isActive()) {
                ObjectiveSet set = unit.getCurrentObjectiveSet();
                if (set == null) continue;
                set.initLazy();
                if (set.isApprovedOrComplete()) {
                    wrapperBean.addOrganisationObjectiveSet(set);
                }
            }
        }
        return organisationUnits;
    }

    /**
     * Get the existing current objectives, if there are none it creates a new Set.
     *
     * @param objectiveDefinition
     * @param corporateSet
     * @param subject
     * @return the current existing set of objectives or a new one
     */
    private ObjectiveSet getSubjectObjectives(ObjectiveDefinition objectiveDefinition, ObjectiveSet corporateSet, Subject subject) {
        // if subject has no current obj set create one cannot get to the add without a published definition
        ObjectiveSet subjectsCurrentGoals = objectiveService.findCurrentObjectiveSet(subject.getId());

        if (subjectsCurrentGoals == null) {
            // create it
            subjectsCurrentGoals = new ObjectiveSet();
            subjectsCurrentGoals.setSubject(subject);
            if (corporateSet != null) {
                subjectsCurrentGoals.setLabel(corporateSet.getLabel());
                subjectsCurrentGoals.setCorporateObjectiveSet(corporateSet);
            }
            subjectsCurrentGoals.setObjectiveDefinition(objectiveDefinition);
        }
        return subjectsCurrentGoals;
    }

    List<ObjectiveWrapperBean> wrapObjectives(ObjectiveDefinition objectiveDefinition, List<Objective> objectives, List<Objective> invalidObjectives) {
        List<ObjectiveWrapperBean> wrappedObjectives = new ArrayList<ObjectiveWrapperBean>();
        boolean invalid = !invalidObjectives.isEmpty();
        final List<DynamicAttribute> dynamicAttributes = objectiveDefinition.getDynamicAttributes();
        Collections.sort(dynamicAttributes);
        for (Objective objective : objectives) {
            if (invalidObjectives.contains(objective)) objective.setParent(null);
            if (invalid) objective.setStatus(ObjectiveConstants.STATUS_OPEN);
            ObjectiveWrapperBean wrapper = new ObjectiveWrapperBean(objective);
            wrapper.setWrappedDynamicAttributes(DynamicAttributesHelper.getAttributeWrapperBeans(dynamicAttributes, objective, dynamicAttributeService));
            wrapper.setInvalid(invalid);
            wrappedObjectives.add(wrapper);
        }
        return wrappedObjectives;
    }

    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

        ObjectiveSetFormBean wrapper = (ObjectiveSetFormBean) command;
        ObjectiveValidator validator = (ObjectiveValidator) getValidator();

        wrapper.setSendEmail(RequestUtils.getBooleanParameter(request, "sendEmail", false));
        wrapper.setApproveObjectives(RequestUtils.getBooleanParameter(request, "approveObjectives", false));


        if (isFinishRequest(request)) {
            validator.validateObjectiveSet(wrapper, errors);
        } else {
            switch (getTargetPage(request, page)) {

                case ADD_OBJECTIVE_IDX:
                    addObjective(wrapper);
                    break;
                case DELETE_OBJECTIVE_IDX:
                    int deleteIndex = RequestUtils.getIntParameter(request, DELETE_IDX, -1);
                    if (deleteIndex != -1) {
                        wrapper.removeObjective(deleteIndex);
                    }
                    break;
            }
        }
    }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        ObjectiveSetFormBean wrapper = (ObjectiveSetFormBean) command;
        ObjectiveSet modifiedObjectiveSet = wrapper.getModifiedObjectiveSet();
        modifiedObjectiveSet.setType(ObjectiveConstants.USER_TYPE);
        if (modifiedObjectiveSet.getId() == null) {
            modifiedObjectiveSet.setPublishedDate(new Date());
        }

        try {
            objectiveService.createOrUpdate(modifiedObjectiveSet);
        } catch (TalentStudioException e) {
            logger.error(e);
            return showPage(request, errors, 1);
        }
        if (wrapper.isSendEmail() && (wrapper.isSendReview() || wrapper.isApproveObjectives())) {
            sendEmail(request, wrapper);
        }
        return buildView(command);
    }

    void sendEmail(HttpServletRequest request, ObjectiveSetFormBean wrapper) {
        User[] users = getRecipients(request, wrapper);
        if (users != null) {
            User from = ZynapWebUtils.getUser(request);
            Subject subject = wrapper.getNodeInfo().getSubject();
            mailNotification.send("", from, subject, users);
        }
    }


    protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return buildView(command);
    }

    private ModelAndView buildView(Object command) {
        ObjectiveSetFormBean wrapper = (ObjectiveSetFormBean) command;
        Long id = wrapper.getNodeInfo().getSubject().getId();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(ParameterConstants.NODE_ID_PARAM, id);
        parameters.put("activeSearchTab", "browse");
        parameters.put("activeTab", "objectives");
        return new ModelAndView(new ZynapRedirectView(getSuccessView(), parameters));
    }

    /**
     * Adds an ObjectiveWrapperBean to the ObjectiveSetWrapperBean.
     *
     * @param wrapperBean the form bean wrapping the objectiveSet and it's objectives.
     */
    void addObjective(ObjectiveSetFormBean wrapperBean) {
        ObjectiveDefinition objectiveDefinition = wrapperBean.getObjectiveDefinition();
        ObjectiveWrapperBean objectiveWrapperBean = new ObjectiveWrapperBean(new Objective());
        objectiveWrapperBean.setWrappedDynamicAttributes(DynamicAttributesHelper.createAttributeWrappers(objectiveDefinition.getDynamicAttributes(), true));
        wrapperBean.addObjective(objectiveWrapperBean);
    }

    /**
     * Get the subject the objective will be added to.
     *
     * @param request the servlet request
     * @return Subject
     * @throws com.zynap.exception.TalentStudioException
     *          if the subject does not exist
     */
    Subject getTargetSubject(HttpServletRequest request) throws TalentStudioException {
        Long subjectId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.NODE_ID_PARAM);
        return subjectService.findById(subjectId);
    }

    /**
     * @param request              the servlet request
     * @param objectiveWrapperBean the form wrapper
     * @return the user of the person being viewed
     */
    User[] getRecipients(HttpServletRequest request, ObjectiveSetFormBean objectiveWrapperBean) {
        return new User[]{objectiveWrapperBean.getNodeInfo().getSubject().getUser()};
    }

    public final void setObjectiveService(IObjectiveService objectiveService) {
        this.objectiveService = objectiveService;
    }

    public final void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public void setPersonnalObjectives(boolean personnalObjectives) {
        this.personnalObjectives = personnalObjectives;
    }

    public void setDynamicAttributeService(IDynamicAttributeService dynamicAttributeService) {
        this.dynamicAttributeService = dynamicAttributeService;
    }

    public void setMailNotification(IMailNotification mailNotification) {
        this.mailNotification = mailNotification;
    }

    protected IObjectiveService objectiveService;
    protected ISubjectService subjectService;
    protected IDynamicAttributeService dynamicAttributeService;
    protected IMailNotification mailNotification;

    private static final int ADD_OBJECTIVE_IDX = 2;
    private static final int DELETE_OBJECTIVE_IDX = 3;

    protected boolean personnalObjectives;
    private static final String DELETE_IDX = "deleteIndex";
}
