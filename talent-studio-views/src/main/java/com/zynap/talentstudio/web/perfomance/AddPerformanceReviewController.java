package com.zynap.talentstudio.web.perfomance;

import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.IAnalysisService;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.mail.IMailNotification;
import com.zynap.talentstudio.performance.IPerformanceReviewService;
import com.zynap.talentstudio.performance.PerformanceReview;
import com.zynap.talentstudio.questionnaires.DefinitionDTO;
import com.zynap.talentstudio.questionnaires.IQueDefinitionService;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.talentstudio.web.controller.ZynapDefaultFormController;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * User: amark
 * Date: 24-Nov-2005
 * Time: 09:45:14
 */
public class AddPerformanceReviewController extends ZynapDefaultFormController {

    public Object formBackingObject(HttpServletRequest request) throws Exception {

        final Collection populations = analysisService.findAll(IPopulationEngine.P_SUB_TYPE_, ZynapWebUtils.getUserId(request), null);
        final Collection<DefinitionDTO> definitions = questionnaireDefinitionService.listDefinitions();
        return new PerformanceReviewWrapper(populations, definitions);
    }

    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);

        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }

    protected ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {
        PerformanceReviewWrapper wrapper = (PerformanceReviewWrapper) command;
        final PerformanceReview performanceReview = wrapper.getModifiedPerformanceReview();

        try {
            Long userId = ZynapWebUtils.getUserId(request);
            User user = userService.getUserById(userId);

            // NOTE: these methods are called separately so that the stored procedures that are used when starting the performance review
            // can see the results of the creation - if they were in the same method the transactional behaviour would interfere !!
            performanceReviewService.createReview(performanceReview, user, wrapper.getManagerQuestionnaireDefinitionId(), wrapper.getGeneralQuestionnaireDefinitionId(), wrapper.getPopulationId(), wrapper.getExpiryDate());

            // if the entire process is to be managed by the logged in user we need a separate service to handle this use case
            boolean userManagedReview = wrapper.isUserManagedReview();
            performanceReviewService.startReview(performanceReview, user, userManagedReview);

            if (performanceReview.isNotifiable() && !userManagedReview) {
                Collection<User> managers = performanceReviewService.getManagers(performanceReview);
                getMailNotification().send(null, ZynapWebUtils.getUser(request), performanceReview.getManagerWorkflow(), (User[]) managers.toArray(new User[managers.size()]));
            }

            return new ModelAndView(new ZynapRedirectView(getSuccessView()));

        } catch (DataIntegrityViolationException e) {
            errors.rejectValue("label", "error.appraisal.duplicate.viewname", "Please select another name - the one you have chosen is in use.");
            return showForm(request, response, errors);
        } catch (TalentStudioException e) {
            errors.reject("error.appraisal.general", "There has been a problem with the appraisal.");
            return showForm(request, response, errors);
        }
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    public void setAnalysisService(IAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    public void setMailNotification(IMailNotification mailNotification) {
        this.mailNotification = mailNotification;
    }

    public IMailNotification getMailNotification() {
        return mailNotification;
    }

    public IQueDefinitionService getQuestionnaireDefinitionService() {
        return questionnaireDefinitionService;
    }

    public void setQuestionnaireDefinitionService(IQueDefinitionService questionnaireDefinitionService) {
        this.questionnaireDefinitionService = questionnaireDefinitionService;
    }

    public void setPerformanceReviewService(IPerformanceReviewService performanceReviewService) {
        this.performanceReviewService = performanceReviewService;
    }

    private IQueDefinitionService questionnaireDefinitionService;
    private IPerformanceReviewService performanceReviewService;
    private IUserService userService;
    private IAnalysisService analysisService;
    private IMailNotification mailNotification;
}
