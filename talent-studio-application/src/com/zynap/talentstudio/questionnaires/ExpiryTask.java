package com.zynap.talentstudio.questionnaires;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

/**
 * User: amark
 * Date: 01-Nov-2006
 * Time: 12:08:55
 *
 * Facade for use by scheduling frameworks to handle tidy up of expired questionnaires and appraisals.
 */
public final class ExpiryTask {

    /**
     * The main method.
     * <br/> Uses the loaded instance of {@link IQuestionnaireService} to handle expired questionnaires and appraisals.
     * @throws Exception any error occurring while expiring the quetsionnaires
     */
    public void run() throws Exception {

        logger.debug(getClass() + " started at: " + new Date());

        try {
            questionnaireWorkflowService.handleExpiredWorkflowsAndAppraisals();
            logger.debug(getClass() + " completed at: " + new Date());

        } catch (Exception e) {
            logger.debug("Failed to run " + getClass() + " because of: ", e);
            throw e;
        }
    }

    public void setQuestionnaireWorkflowService(IQueWorkflowService questionnaireWorkflowService) {
        this.questionnaireWorkflowService = questionnaireWorkflowService;
    }

    private IQueWorkflowService questionnaireWorkflowService;

    /**
     * Logger.
     */
    private static final Log logger = LogFactory.getLog(ExpiryTask.class);
}
