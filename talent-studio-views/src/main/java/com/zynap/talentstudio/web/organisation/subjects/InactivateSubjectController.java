/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.subjects;

import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.web.controller.ZynapDefaultFormController;

import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 16-Nov-2009 11:29:22
 */
public class InactivateSubjectController extends ZynapDefaultFormController {

    public final Object formBackingObject(HttpServletRequest request) throws Exception {
        
        Long nodeId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.NODE_ID_PARAM);
        setCancelView(HistoryHelper.getBackURL(request));
        setSuccessView(HistoryHelper.getBackURL(request));
        final Subject subject = subjectService.findById(nodeId);
        // load the associations to prevent lazy load issues
        subject.getSubjectAssociations().size();
        subject.getSubjectPrimaryAssociations().size();
        subject.getSubjectSecondaryAssociations().size();

        return subject;
    }

    public final ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {

        Subject subject = (Subject) command;

        subject.getSubjectAssociations().clear();
        subject.getSubjectPrimaryAssociations().clear();
        subject.getSubjectSecondaryAssociations().clear();
        subject.setActive(false);

        subjectService.update(subject);

        RedirectView view = new ZynapRedirectView(getSuccessView());
        view.addStaticAttribute(ParameterConstants.SUBJECT_ID_PARAM, subject.getId());
        return new ModelAndView(view);
    }

    public void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    private ISubjectService subjectService;
}
