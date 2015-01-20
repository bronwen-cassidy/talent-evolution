package com.zynap.talentstudio.web.perfomance;

import com.zynap.talentstudio.performance.IPerformanceReviewService;
import com.zynap.talentstudio.performance.PerformanceReview;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.workflow.Notification;
import com.zynap.web.controller.ZynapDefaultFormController;

import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * Controller that displays performance reviews.
 * <p/>
 * User: amark
 * Date: 19-Oct-2006
 * Time: 14:25:18
 */
public class ViewPerformanceReviewController extends ZynapDefaultFormController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        final Long performanceReviewId = RequestUtils.getRequiredLongParameter(request, PerformanceReviewMultiController.REVIEW_ID);
        return performanceReviewService.findById(performanceReviewId);
    }


    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        Map model = new HashMap();
        //load all notifications
        PerformanceReview review = (PerformanceReview) command;

        List<Notification> notifications = performanceReviewService.getAppraisalReviewNotifications(review.getId());
        model.put("notifications", notifications);
        return model;
    }

    public void setPerformanceReviewService(IPerformanceReviewService performanceReviewService) {
        this.performanceReviewService = performanceReviewService;
    }

    private IPerformanceReviewService performanceReviewService;

}
