package com.zynap.talentstudio.web.perfomance;

import com.zynap.talentstudio.performance.IPerformanceReviewService;
import com.zynap.talentstudio.performance.PerformanceReview;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.utils.controller.ZynapMultiActionController;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.talentstudio.workflow.IWorkflowAdapter;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * User: amark
 * Date: 23-Nov-2005
 * Time: 12:56:59
 */
public class PerformanceReviewMultiController extends ZynapMultiActionController {

    public ModelAndView listReviewsHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info(request.toString() + response.toString());
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(REVIEWS, performanceReviewService.findAllPerformanceReviews());

        return new ModelAndView(LIST_REVIEWS_VIEW, ControllerConstants.MODEL_NAME, model);
    }

    public ModelAndView completeReviewHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info(request.toString() + response.toString());
        Long reviewId = RequestUtils.getRequiredLongParameter(request, REVIEW_ID);
        final PerformanceReview performanceReview = (PerformanceReview) performanceReviewService.findById(reviewId);
        performanceReviewService.closeReview(performanceReview);
        return new ModelAndView(new ZynapRedirectView("viewperformancereview.htm", REVIEW_ID, reviewId));
    }

    public ModelAndView reopenNotificationHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info(request.toString() + response.toString());
        Long reviewId = RequestUtils.getRequiredLongParameter(request, REVIEW_ID);
        Long notificationId = RequestUtils.getRequiredLongParameter(request, NOTIF_ID);
        workflowAdapter.reopenNotification(notificationId);
        return new ModelAndView(new ZynapRedirectView("viewperformancereview.htm", REVIEW_ID, reviewId));
    }

    public ModelAndView completeNotificationHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info(request.toString() + response.toString());
        Long reviewId = RequestUtils.getRequiredLongParameter(request, REVIEW_ID);
        Long notificationId = RequestUtils.getRequiredLongParameter(request, NOTIF_ID);
        workflowAdapter.completeNotification(notificationId);
        return new ModelAndView(new ZynapRedirectView("viewperformancereview.htm", REVIEW_ID, reviewId));
    }

    public void setPerformanceReviewService(IPerformanceReviewService performanceReviewService) {
        this.performanceReviewService = performanceReviewService;
    }

    public void setWorkflowAdapter(IWorkflowAdapter workflowAdapter) {
        this.workflowAdapter = workflowAdapter;
    }

    private IPerformanceReviewService performanceReviewService;
    private IWorkflowAdapter workflowAdapter;

    static final String REVIEWS = "reviews";
    static final String LIST_REVIEWS_VIEW = "listperformancereviews";

    public static final String REVIEW_ID = "reviewId";
    public static final String NOTIF_ID = "notifId";
}
