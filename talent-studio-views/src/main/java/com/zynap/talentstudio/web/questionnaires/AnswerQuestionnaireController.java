package com.zynap.talentstudio.web.questionnaires;

import com.zynap.domain.admin.User;
import com.zynap.exception.PessimisticLockingFailureException;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.mail.IMailNotification;
import com.zynap.talentstudio.messages.IMessageService;
import com.zynap.talentstudio.messages.MessageItem;
import com.zynap.talentstudio.organisation.IOrganisationUnitService;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.NoSubjectForUserException;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.questionnaires.IQueWorkflowService;
import com.zynap.talentstudio.questionnaires.IQuestionnaireService;
import com.zynap.talentstudio.questionnaires.Questionnaire;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;
import com.zynap.talentstudio.questionnaires.QuestionnaireUtils;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.common.UrlBeanPair;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.tree.TreeBuilderHelper;
import com.zynap.talentstudio.web.workflow.WorkflowConstants;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * User: amark
 * Date: 27-Sep-2006
 * Time: 10:04:11
 */
public class AnswerQuestionnaireController extends DefaultWizardFormController implements WorkflowConstants {

    /**
     * Load questionnaire from business layer.
     *
     * @param request the servlet request
     * @return QuestionnaireWrapper
     * @throws Exception
     */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        final Long questionnaireId = RequestUtils.getLongParameter(request, ParameterConstants.QUESTIONNAIRE_ID);
        final Long workflowId = RequestUtils.getLongParameter(request, WorkflowConstants.WORKFLOW_ID_PARAM_PREFIX);
        final Long subjectId = RequestUtils.getLongParameter(request, ParameterConstants.NODE_ID_PARAM);
        final User user = ZynapWebUtils.getUser(request);
        final boolean isMyPortfolio = RequestUtils.getBooleanParameter(request, MY_PORTFOLIO, false);
        try {
            QuestionnaireWrapper questionnaireWrapper = questionnaireHelper.getQuestionnaireWrapper(questionnaireId, workflowId, user, subjectId, true);
            Questionnaire questionnaire = questionnaireWrapper.getQuestionnaire();
            questionnaireWrapper.setManagerView(managerView || (questionnaire.isManager()));
            questionnaireWrapper.setHrView(user.getId().equals(questionnaireWrapper.getHrUserId()));
            questionnaireWrapper.setUserId(user.getId());

            if (managerView && questionnaire.getSubjectId() != null) {
                // check the user of the questionnaire can login to recieve notification!
                Subject subject = subjectService.findById(questionnaire.getSubjectId());
                questionnaireWrapper.setSubjectUser(subject.getUser());
            } else {
                try {
                    Subject subject = subjectService.findByUserId(user.getId());
                    if (subject != null) {
                        questionnaireWrapper.setUserManagers(subject.getManagers());
                    }
                } catch (NoSubjectForUserException nsue) {
                    questionnaireWrapper.setUserManagers(new LinkedList<User>());
                }
            }

            QuestionnaireUtils.initLazyCollections(questionnaire.getQuestionnaireWorkflow().getQuestionnaireDefinition());
            questionnaireWrapper.setMyPortfolio(isMyPortfolio);
            return questionnaireWrapper;
        } catch (PessimisticLockingFailureException e) {
            logger.error("PessimisticLockingFailureException seen: " + e.getMessage());
            final QuestionnaireWrapper questionnaireWrapper = new QuestionnaireWrapper();
            questionnaireWrapper.setQuestionnaire(new Questionnaire());
            questionnaireWrapper.setErrorKey(e.getKey());
            questionnaireWrapper.setMyPortfolio(isMyPortfolio);
            return questionnaireWrapper;
        }
    }

    /**
     * Load data for pickers.
     *
     * @param refData the map containing any data needed by the web page
     * @throws com.zynap.exception.TalentStudioException on error
     */
    protected void dataForPickers(Map<String, Object> refData) throws TalentStudioException {
        refData.put("orgUnitTree", TreeBuilderHelper.buildOrgUnitTree(organisationUnitService.findOrgUnitTree(OrganisationUnit.ROOT_ORG_UNIT_ID)));
    }

    /**
     * Load ref. data.
     * <br/> Adds error messages to display if the save failed.
     * <br/> Also loads org unit picker data.
     *
     * @param request the servlet request
     * @param command the command object {@link com.zynap.talentstudio.web.questionnaires.QuestionnaireWrapper}
     * @param errors  the web page errors
     * @param page    the current page
     * @return Map
     * @throws Exception
     */
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

        int targetPage = getTargetPage(request, page);
        final QuestionnaireWrapper wrapper = (QuestionnaireWrapper) command;
        if (targetPage == 6) {
            processInboxNotification(request, wrapper);
            refreshQuestionnaire(wrapper, (Questionnaire) questionnaireService.findById(wrapper.getQuestionnaireId()));
        }

        final Map<String, Object> refData = new HashMap<String, Object>();

        if (wrapper.isFatalErrors()) {
            refData.put(MESSAGE_KEY, "questionnaire.hasfatalerrors");
        }

        dataForPickers(refData);
        return refData;
    }

    private void processInboxNotification(HttpServletRequest request, QuestionnaireWrapper wrapper) throws TalentStudioException {

        final Questionnaire modifiedQuestionnaire = wrapper.getQuestionnaire();
        boolean sendToInbox = RequestUtils.getBooleanParameter(request, "sendToInbox", false);
        boolean sendToHr = RequestUtils.getBooleanParameter(request, "sendToHr", false);
        boolean sendEmail = RequestUtils.getBooleanParameter(request, "sendEmail", false);

        boolean process = sendEmail || sendToInbox || sendToHr;
        if (process) {
            Long subjectId = modifiedQuestionnaire.getSubjectId();
            List<User> participants = new ArrayList<User>();
            // we are going to the subordinate who is the subject of the questionnaire
            if (managerView) {
                User toUser = userService.findBySubjectId(subjectId);
                if (toUser != null) participants.add(toUser);
                if(wrapper.getHrUser() != null && sendToHr) participants.add(wrapper.getHrUser());
                
            } else {
                // find the manager
                Subject subject = subjectService.findById(subjectId);
                participants = subject.getManagers();
            }
            if (wrapper.isMyPortfolio()) {
                // only check for selected managers if there are more than 1
                if (participants.size() > 1) {
                    //if there is more then one manager then do the following filter
                    //filter managers to only one rather then all -i.e the manager selected
                    Iterator<User> participant = participants.iterator();
                    while (participant.hasNext()) {
                        User user = participant.next();
                        if (!wrapper.containsManagerSelection(user.getId())) {
                            participant.remove();
                        }
                    }
                }
            }
            if (!participants.isEmpty()) {
                wrapper.setSendSuccess(true);
                String viewType = managerView && !sendToHr ? MessageItem.INDIVIDUAL_VIEW : MessageItem.MANAGER_VIEW; 
                if (sendToInbox || sendToHr) messageService.create(modifiedQuestionnaire, viewType, ZynapWebUtils.getUser(request), participants);
                if (sendEmail) {
                    UrlBeanPair pair;
                    if (sendToInbox) pair = mailNotifications.get(INBOX_MAIL);
                    else {
                        if (managerView) pair = mailNotifications.get(NO_INBOX_MAIL_MANAGER);
                        else pair = mailNotifications.get(NO_INBOX_MAIL_INDIVIDUAL);
                    }
                    IMailNotification mailNotification = pair.getRef();
                    String url = pair.getUrl();
                    try {
                        mailNotification.send(url, ZynapWebUtils.getUser(request), modifiedQuestionnaire, participants.toArray(new User[participants.size()]));
                    } catch (Exception e) {
                        wrapper.setSendSuccess(false);
                        wrapper.setFatalErrors(true);
                        wrapper.setSendFail(true);
                        wrapper.setSendErrorMessage("send.fail");
                    }
                }
            }
        }
    }

    /**
     * Redirect to previous page.
     *
     * @param request  the servlet request
     * @param response the servlet response
     * @param command  the command object {@link com.zynap.talentstudio.web.questionnaires.QuestionnaireWrapper}
     * @param errors   the errors object containing binding errors
     * @return ModelAndView
     * @throws Exception
     */
    protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        final QuestionnaireWrapper wrapper = (QuestionnaireWrapper) command;
        return buildModelAndView(getCancelView(), wrapper);
    }


    /**
     * Build model and view for cancel or save.
     *
     * @param url     the redirect page url
     * @param wrapper the command object {@link com.zynap.talentstudio.web.questionnaires.QuestionnaireWrapper}
     * @return ModelAndView
     */
    private ModelAndView buildModelAndView(final String url, final QuestionnaireWrapper wrapper) {

        final RedirectView redirectView = HistoryHelper.buildRedirectView(url);
        redirectView.addStaticAttribute(ParameterConstants.QUESTIONNAIRE_ID, wrapper.getQuestionnaireId());
        redirectView.addStaticAttribute(WORKFLOW_ID_PARAM_PREFIX, wrapper.getWorkflowId());
        redirectView.addStaticAttribute(ParameterConstants.NODE_ID_PARAM, wrapper.getSubjectId());
        redirectView.addStaticAttribute(MY_PORTFOLIO, wrapper.isMyPortfolio());

        return new ModelAndView(redirectView);
    }

    /**
     * Set questionnaire data on wrapper.
     *
     * @param wrapper       QuestionnaireWrapper
     * @param questionnaire questionnaire to be refreshed
     */
    protected final void refreshQuestionnaire(QuestionnaireWrapper wrapper, Questionnaire questionnaire) {
        final QuestionnaireDefinition questionnaireDefinition = questionnaire.getQuestionnaireWorkflow().getQuestionnaireDefinition();
        questionnaireHelper.setQuestionnaireState(wrapper, questionnaireDefinition, questionnaire);
    }

    public void setQuestionnaireWorkflowService(IQueWorkflowService questionnaireWorkflowService) {
        this.questionnaireWorkflowService = questionnaireWorkflowService;
    }

    public void setQuestionnaireHelper(QuestionnaireHelper questionnaireHelper) {
        this.questionnaireHelper = questionnaireHelper;
    }

    public void setManagerView(boolean value) {
        this.managerView = value;
    }

    public void setMessageService(IMessageService messageService) {
        this.messageService = messageService;
    }

    public void setMailNotifications(Map<String, UrlBeanPair> mailNotification) {
        this.mailNotifications = mailNotification;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    public void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public final void setQuestionnaireService(IQuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    public final void setOrganisationUnitService(IOrganisationUnitService organisationUnitService) {
        this.organisationUnitService = organisationUnitService;
    }

    public final void setDynamicAttributeService(IDynamicAttributeService dynamicAttributeService) {
        this.dynamicAttributeService = dynamicAttributeService;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    protected String imageURL;

    protected IQuestionnaireService questionnaireService;
    protected IQueWorkflowService questionnaireWorkflowService;
    protected IDynamicAttributeService dynamicAttributeService;
    protected volatile QuestionnaireHelper questionnaireHelper;
    protected Map<String, UrlBeanPair> mailNotifications;
    private IOrganisationUnitService organisationUnitService;
    private IMessageService messageService;
    protected IUserService userService;
    protected ISubjectService subjectService;

    private boolean managerView;
    protected final String MY_PORTFOLIO = "myPortfolio";
    protected final String INBOX_MAIL = "INBOX";
    protected final String NO_INBOX_MAIL_MANAGER = "NO_INBOX_MANAGER";
    protected final String NO_INBOX_MAIL_INDIVIDUAL = "NO_INBOX_STAFF";

    /**
     * Constants for questionnaire targets.
     */
    public static final int DELETE_IMAGE_IDX = 10;
    public static final int ADD_DYNAMIC_LINE_ITEM = 11;
    public static final int DELETE_DYNAMIC_LINE_ITEM = 12;

    /**
     * Constants for ref data.
     */
    public static final String MESSAGE_KEY = "message";
}
