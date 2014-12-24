package com.zynap.talentstudio.web.organisation.subjects;

import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.organisation.EditNodeAttributesController;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.talentstudio.security.UserSessionFactory;

import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: bcassidy
 */
public final class EditSubjectAttributesController extends EditNodeAttributesController {

    public EditSubjectAttributesController() {
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        setCancelView(HistoryHelper.getBackURL(request));
        setSuccessView(HistoryHelper.getBackURL(request));

        //get the id from the request and get the subject
        final Long subjectId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.SUBJECT_ID_PARAM);
        Subject subject = subjectService.findById(subjectId);
        SubjectWrapperBean subjectWrapperBean = new SubjectWrapperBean(subject);
        subjectWrapperBean.setPersonalView(personalView);
        applyAttributes(request, subjectWrapperBean, subject);
        return subjectWrapperBean;
    }

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        
        Map refData = new HashMap();
        List titles = getLookupManager().findActiveLookupValues(ILookupManager.LOOKUP_TYPE_TITLE);
        refData.put(ControllerConstants.TITLES, titles);
        return refData;
    }

    public final ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {

        SubjectWrapperBean subjectWrapperBean = (SubjectWrapperBean) command;
        boolean imageAttributeDeleted = clearImageExtendedAttribute(request, subjectWrapperBean);
        if (imageAttributeDeleted) return showForm(request, response, errors);

        Subject subject = subjectWrapperBean.getModifiedSubject(UserSessionFactory.getUserSession().getUser());
//        subject.setActive(RequestUtils.getBooleanParameter(request, "active", false));
        subjectService.update(subject, subjectWrapperBean.getModifiedSubjectPicture());
        subjectService.updateCurrentJobInfo(subject.getId());
        
        RedirectView view = new ZynapRedirectView(getSuccessView());
        view.addStaticAttribute(ParameterConstants.SUBJECT_ID_PARAM, subject.getId());
        return new ModelAndView(view);
    }

    public void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public ISubjectService getSubjectService() {
        return subjectService;
    }

    public ILookupManager getLookupManager() {
        return lookupManager;
    }

    public void setLookupManager(ILookupManager lookupManager) {
        this.lookupManager = lookupManager;
    }

    public void setPersonalView(boolean personalView) {
        this.personalView = personalView;
    }

    private ILookupManager lookupManager;
    private ISubjectService subjectService;
    private boolean personalView;
}
