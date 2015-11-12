/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.objectives;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.common.lookups.LookupType;
import com.zynap.talentstudio.mail.IMailNotification;
import com.zynap.talentstudio.objectives.IObjectiveService;
import com.zynap.talentstudio.objectives.Objective;
import com.zynap.talentstudio.objectives.ObjectiveAssessment;
import com.zynap.talentstudio.objectives.ObjectiveConstants;
import com.zynap.talentstudio.objectives.ObjectiveSet;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.NodeInfo;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;
import com.zynap.talentstudio.web.organisation.attributes.DynamicAttributesHelper;
import com.zynap.talentstudio.web.organisation.attributes.FormAttribute;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.controller.ZynapDefaultFormController;

import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Controller that displays current objectives for logged in user.
 *
 * @author bcassidy
 * @version 0.1
 * @since 24-May-2006 10:56:17
 */
public class ViewObjectiveController extends ZynapDefaultFormController implements ObjectiveConstants {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        Long objectiveId = RequestUtils.getRequiredLongParameter(request, OBJECTIVE_ID);
        Objective objective = objectiveService.findObjective(objectiveId);
        List<FormAttribute> wrappedAttributes = DynamicAttributesHelper.wrapExtendedAttributes(objective);
        
        // sort for the views
        Collections.sort(wrappedAttributes, new Comparator<FormAttribute>() {
            public int compare(FormAttribute o1, FormAttribute o2) {
                AttributeWrapperBean attr1 = (AttributeWrapperBean) o1;
                AttributeWrapperBean attr2 = (AttributeWrapperBean) o2;
                return attr1.getAttributeDefinition().getId().compareTo(attr2.getAttributeDefinition().getId());
            }
        });
        
        ObjectiveWrapperBean wrapperBean = new ObjectiveWrapperBean(objective);
        wrapperBean.setWrappedDynamicAttributes(wrappedAttributes);

        ObjectiveAssessment assessment = objective.getManagerAssessment();
        if(assessment == null) {
            assessment = new ObjectiveAssessment();
            objective.addAssessment(assessment);
        }

        LookupType rating = lookupManager.findLookupType(RATING_LOOKUP_TYPE);
        AssessmentFormBean formBean = new AssessmentFormBean(assessment, rating);
        wrapperBean.setAssessment(formBean);

        Set<ObjectiveAssessment> objectiveAssessments = new HashSet<ObjectiveAssessment>(objective.getAssessments());
        objectiveAssessments.remove(assessment);
        for(ObjectiveAssessment objAssessment : objectiveAssessments) {
            wrapperBean.add(new AssessmentFormBean(objAssessment));        
        }

        wrapperBean.setUserId(ZynapWebUtils.getUserId(request));
        ObjectiveSet objectiveSet = objective.getObjectiveSet();
        Subject subject = objectiveSet.getSubject();

        if (!personnalObjectives && subject != null) {
            // viewing the staff members objective
            buidNodeInfo(subject, wrapperBean);
        }
        return wrapperBean;
    }

    private void buidNodeInfo(Subject subject, ObjectiveWrapperBean wrapperBean) {
        List<Position> primaryPositions = subject.getPrimaryPositions();
        List<OrganisationUnit> organisationUnits = new ArrayList<OrganisationUnit>();
        for (Position primaryPosition : primaryPositions) {
            organisationUnits.add(primaryPosition.getOrganisationUnit());
        }
        NodeInfo info = new NodeInfo(subject, primaryPositions, organisationUnits);
        wrapperBean.setNodeInfo(info);
    }


    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors) throws Exception {
        ObjectiveWrapperBean wrapper = (ObjectiveWrapperBean) command;
        wrapper.getAssessment().setApproved(RequestUtils.getBooleanParameter(request, "assessment.approved", false));
    }

    public ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse httpServletResponse, Object command, Errors errors) throws Exception {

        ObjectiveWrapperBean wrapperBean = (ObjectiveWrapperBean) command;
        Objective objective = wrapperBean.getModifiedObjectiveAssessment();
        objectiveService.updateObjectiveAssessment(objective);

        Subject subject;
        if (wrapperBean.isSendEmail()) {
            List<User> users = new ArrayList<User>();
            if(personnalObjectives) {

                subject = subjectService.findByUserId(ZynapWebUtils.getUserId(request));
                users.addAll(subject.getManagers());

            } else {
                // send email to the person whose objectives have been assessed
                final NodeInfo info = wrapperBean.getNodeInfo(); 
                subject = info.getSubject();
                final User o = subject.getUser();
                if(o != null) users.add(o);

            }
            if (!users.isEmpty()) {
                mailNotification.send("", ZynapWebUtils.getUser(request), subject, (User[]) users.toArray(new User[users.size()]));
            }
        }
        return showForm(request, errors, getFormView());
    }

    public void setObjectiveService(IObjectiveService objectiveService) {
        this.objectiveService = objectiveService;
    }

    public void setLookupManager(ILookupManager lookupManager) {
        this.lookupManager = lookupManager;
    }

    public void setPersonnalObjectives(boolean personnalObjectives) {
        this.personnalObjectives = personnalObjectives;
    }

    public void setMailNotification(IMailNotification mailNotification) {
        this.mailNotification = mailNotification;
    }

    public void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    private IObjectiveService objectiveService;
    protected ISubjectService subjectService;
    private boolean personnalObjectives;
    private ILookupManager lookupManager;
    private IMailNotification mailNotification;

}
