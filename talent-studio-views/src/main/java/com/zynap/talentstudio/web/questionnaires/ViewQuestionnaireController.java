package com.zynap.talentstudio.web.questionnaires;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.workflow.WorkflowConstants;
import com.zynap.talentstudio.web.controller.ZynapDefaultFormController;

import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller that loads a questionnaire.
 * <p/>
 * User: amark
 * Date: 15-Aug-2005
 * Time: 15:44:04
 */
public final class ViewQuestionnaireController extends ZynapDefaultFormController{

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        final Long questionnaireId = RequestUtils.getLongParameter(request, ParameterConstants.QUESTIONNAIRE_ID);
        final Long workflowId = RequestUtils.getLongParameter(request, WorkflowConstants.WORKFLOW_ID_PARAM_PREFIX);
        final Long subjectId = RequestUtils.getLongParameter(request, ParameterConstants.NODE_ID_PARAM);
        final User user = ZynapWebUtils.getUser(request);
        final boolean isMyPortfolio = RequestUtils.getBooleanParameter(request, "myPortfolio", false);
        QuestionnaireWrapper questionnaireWrapper = questionnaireHelper.getQuestionnaireWrapper(questionnaireId, workflowId, user, subjectId, false);
        questionnaireWrapper.setMyPortfolio(isMyPortfolio);
        return questionnaireWrapper;
    }

    /**
     * Sets image URL for use in JSPs.
     *
     * @param request
     * @param command
     * @param errors
     * @return Map
     * @throws Exception
     */
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {

        final Map<String, Object> refData = new HashMap<String, Object>();

        QuestionnaireWrapper wrapper = (QuestionnaireWrapper) command;
        final Long subjectId = wrapper.getQuestionnaire().getSubjectId();
       
        String completeURL = ZynapWebUtils.buildURL(imageURL, ParameterConstants.NODE_ID_PARAM, subjectId);
        refData.put("imageUrl", completeURL);

        refData.put(ControllerConstants.EDIT_VIEW, editURL);

        return refData;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setEditURL(String editURL) {
        this.editURL = editURL;
    }

    public void setQuestionnaireHelper(QuestionnaireHelper questionnaireHelper) {
        this.questionnaireHelper = questionnaireHelper;
    }

    private QuestionnaireHelper questionnaireHelper;
    private String imageURL;
    private String editURL;
}
