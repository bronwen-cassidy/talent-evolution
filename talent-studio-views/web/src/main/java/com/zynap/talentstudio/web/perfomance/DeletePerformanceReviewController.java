package com.zynap.talentstudio.web.perfomance;

import com.zynap.talentstudio.performance.PerformanceReview;
import com.zynap.talentstudio.performance.IPerformanceReviewService;
import com.zynap.talentstudio.questionnaires.IQuestionnaireService;

import com.zynap.web.controller.ZynapDefaultFormController;

import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: amark
 * Date: 22-Nov-2005
 * Time: 14:34:18
 */
public class DeletePerformanceReviewController extends ZynapDefaultFormController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        final Long performanceReviewId = RequestUtils.getRequiredLongParameter(request, PerformanceReviewMultiController.REVIEW_ID);
        return performanceReviewService.findById(performanceReviewId);
    }

    protected ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {

        PerformanceReview performanceReview = (PerformanceReview) command;

        try {
            performanceReviewService.deleteReview(performanceReview.getId());
        } catch (DataIntegrityViolationException e) {
            errors.reject("error.delete.appraisal.dependencies", "Cannot delete the appraisal as it is in use in the system");
            return showForm(request, response, errors);
        }

        return new ModelAndView(new ZynapRedirectView(getSuccessView()));
    }

    protected ModelAndView onCancelInternal(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {

        PerformanceReview performanceReview = (PerformanceReview) command;
        return new ModelAndView(new ZynapRedirectView(getCancelView(), PerformanceReviewMultiController.REVIEW_ID, performanceReview.getId()));
    }

    public void setPerformanceReviewService(IPerformanceReviewService performanceReviewService) {
        this.performanceReviewService = performanceReviewService;
    }

    private IPerformanceReviewService performanceReviewService;
}
