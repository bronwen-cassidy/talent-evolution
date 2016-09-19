package com.zynap.talentstudio.web.workflow;

import com.zynap.domain.UserPrincipal;
import com.zynap.domain.admin.User;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.questionnaires.Questionnaire;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.questionnaires.AnswerQuestionnaireController;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.workflow.IWorkflowAdapter;
import com.zynap.talentstudio.workflow.Notification;
import com.zynap.talentstudio.workflow.WorkflowException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 04-May-2005
 * Time: 09:51:01
 *
 */
public class WorklistController extends AnswerQuestionnaireController {

    public final void setWorkflowAdapter(IWorkflowAdapter workflowAdapter) {
        this.workflowAdapter = workflowAdapter;
    }

    public final void setPerformanceReview(boolean performanceReview) {
        this.performanceReview = performanceReview;
    }

    /**
     * Overridden to check for a {@link com.zynap.talentstudio.web.common.ControllerConstants#FORM_SUBMISSION} in a request, if this exists then this is a form submission.
     *
     * @param request the HttpServletRequest
     * @return true if _formSubmission parameter is not null otherwise let the superclass check
     */
    protected final boolean isFormSubmission(HttpServletRequest request) {
        return super.isFormSubmission(request) || request.getParameter(ControllerConstants.FORM_SUBMISSION) != null || isDisplayTagSort(request);
    }

    /**
     * Overridden to read a {@link ControllerConstants#PAGE_NUM}  parameter in the request if it exists, otherwise let super handle.
     *
     * @param request     the HttpServletRequest
     * @param currentPage the current page we are displaying
     * @return the target page.
     */
    protected final int getTargetPage(HttpServletRequest request, int currentPage) {
        final String parameter = request.getParameter(ControllerConstants.PAGE_NUM);
        if (ZynapWebUtils.isGetRequest(request) && parameter != null) {
            return new Integer(parameter);
        }

        return super.getTargetPage(request, currentPage);
    }

    /**
     * Get form backing object.
     *
     * @param request current HTTP request
     * @return instance of {@link WorklistWrapper}
     * @throws Exception - any DomainObjectNotFoundException or TalentStudioException
     */
    protected final Object formBackingObject(HttpServletRequest request) throws Exception {
        WorklistWrapper wrapper = (WorklistWrapper) HistoryHelper.recoverCommand(request, WorklistWrapper.class);
        if (wrapper == null) {
            Long userId = ZynapWebUtils.getUserId(request);
            String workflowType = request.getParameter(WORKFLOW_TYPE_PARAM);
            String notificationStatus = RequestUtils.getStringParameter(request, NOTIFICATION_STATUS_PARAM, NOTIFICATION_STATUS_OPEN);
            List<Notification> notifications = workflowAdapter.getNotifications(userId, performanceReview);

            WorklistWrapper worklistWrapper = new WorklistWrapper(notifications, workflowType, notificationStatus, performanceReview);
            worklistWrapper.setCurrentUserId(userId);

            return worklistWrapper;
        }
        return wrapper;
    }

    /**
     * Implementation that returns null always - no finish for this wizard.
     *
     * @param request  the HttpServletRequest
     * @param response the HttpServletReponse
     * @param command  the WorklistWrapper
     * @param errors   the object containing binding errors
     * @return null
     * @throws Exception - is not implemented
     */
    protected final ModelAndView processFinishInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {
        return null;
    }

    /**
     * Set notification id, workflow id subject id and role on wrapper from request and return workflowid.
     *
     * @param wrapper the WorklistWrapper
     * @param request the HttpServletRequest
     * @return workflowId
     */
    protected final Long setWorkflowParameters(WorklistWrapper wrapper, HttpServletRequest request) {

        final Long notificationId = RequestUtils.getLongParameter(request, NOTIFICATION_ID_PARAM);
        wrapper.setNotificationId(notificationId);

        final Long performanceId = RequestUtils.getLongParameter(request, APPRAISAL_ID + notificationId);
        wrapper.setPerformanceId(performanceId);

        Long workflowId = RequestUtils.getLongParameter(request, WORKFLOW_ID_PARAM_PREFIX + notificationId);
        wrapper.setWorkflowId(workflowId);

        wrapper.setSubjectId(RequestUtils.getLongParameter(request, SUBJECT_ID_PARAM_PREFIX + notificationId));
        wrapper.setRole(RequestUtils.getLongParameter(request, ROLE_PARAM_PREFIX + notificationId));
        wrapper.setAction(wrapper.getNotification() != null ? wrapper.getNotification().getAction() : null);

        return workflowId;
    }

    /**
     * Clear data from wrapper.
     *
     * @param wrapper   the WorklistWrapper
     * @param isClosing true if the tab being closed
     */
    protected final void clearInfo(WorklistWrapper wrapper, boolean isClosing) {
        Questionnaire questionnaire = wrapper.getQuestionnaire();
        if (isClosing || questionnaire == null || questionnaire.getNotificationId().equals(wrapper.getNotificationId())) {
            wrapper.clearQuestionnaireState();
        }

        wrapper.setAction(null);

        wrapper.clearEvaluatorQuestions();
        wrapper.clearObjectives();
        wrapper.clearPerformanceRoles();
        wrapper.clearWorkflowParameters();
    }

    /**
     * Load ref data for different targets.
     *
     * @param request the HttpServletRequest
     * @param command the WorklistWrapper
     * @param errors  the object containing binding errors
     * @param page    the page data is required for
     * @return Map
     * @throws Exception - TalentStudioException
     */
    protected final Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        WorklistWrapper wrapper = (WorklistWrapper) command;
        Map<String, Object> refData = new HashMap<>();
        int targetPage = getTargetPage(request, page);

        switch (targetPage) {
            case TODO_LIST:
                wrapper.setActiveTab(WORKLIST_TAB);
                break;
            case RESPOND_NOTIFICATION:
                refData.put(MESSAGE_KEY, "questionnaire.completed");
                wrapper.setActiveTab(WORKLIST_TAB);
                break;
        }

        referenceDataAdditionalTargets(wrapper, targetPage, refData);

        if (wrapper.getQuestionnaire() != null) {
            final Long subjectId = wrapper.getQuestionnaire().getSubjectId();
            String completeURL = ZynapWebUtils.buildURL(imageURL, ParameterConstants.NODE_ID_PARAM, subjectId);
            refData.put("imageUrl", completeURL);
        }
        if (loadPickerData(targetPage)) {
            dataForPickers(refData);
        }
        return refData;
    }

    /**
     * Callback for custom post-processing in terms of binding and validation - does processing for specific targets.
     *
     * @param request the HttpServletRequest
     * @param command the WorklistWrapper
     * @param errors  the object containing binding errors
     * @param page    the page we are binging on and validating
     * @throws Exception
     */
    protected final void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

        WorklistWrapper wrapper = (WorklistWrapper) command;

        int targetPage = getTargetPage(request, page);
        final UserPrincipal userPrincipal = UserSessionFactory.getUserSession().getUserPrincipal();
        final Long userId = userPrincipal.getUserId();
        final Long subjectId = wrapper.getSubjectId();
        Notification notification;

        try {
            switch (targetPage) {
                case RESPOND_NOTIFICATION:

                    // starts the process
                    if (isRepondNotificationRequest(request)) {
                        setWorkflowParameters(wrapper, request);
                        final Long roleId = wrapper.getRole();
                        Notification wrapperNotification = wrapper.getNotification();
                        workflowAdapter.processNotification(wrapperNotification, wrapper.getAction(), userId, subjectId, roleId);
                        if (wrapper.isPerformanceReview()) respondAppraisalNotification(wrapper, request);
                        wrapper.setAction(Notification.getNextAction(wrapper.getAction(), wrapperNotification.getSubType()));
                        clearInfo(wrapper, false);
                        wrapper.setActiveTab(WORKLIST_TAB);
                    }
                    break;
                case OPEN_QUESTIONNAIRE:
                    wrapper.setSaved(false);
                    handleOpenQuestionaire(wrapper, request);
                    break;
                case SAVE_QUESTIONNAIRE:
                    Long questionnaireId = wrapper.getQuestionnaireId();
                    if (questionnaireId == null) break;

                    Questionnaire questionnaire = questionnaireService.findById(questionnaireId);
                    notification = wrapper.getNotification();
                    boolean shouldSetActionable = notification.getManagersManagerId() == null && notification.getHrId() == null;
                    if (wrapper.getSelectedManagerIds() != null && wrapper.getSelectedManagerIds().length > 0) {

                        // update the current notification to indicate it is awaiting verification
                        questionnaireWorkflowService.setNotificationActionable(wrapper.getNotificationId(), false, Notification.AWAITING_APPROVAL);
                        for (int i = 0; i < wrapper.getSelectedManagerIds().length; i++) {
                            workflowAdapter.processApprovalNotification(wrapper.getNotification(), Notification.VERIFY, userId, subjectId, wrapper.getSelectedManagerIds()[i]);
                        }
                        // create a new notification
                        wrapper.setAction(Notification.AWAITING_APPROVAL);
                        wrapper.setNotificationList(workflowAdapter.getNotifications(userId, performanceReview));
                        shouldSetActionable = false;
                    }

                    // manager's veiw send to hr has been selected
                    if (wrapper.getHrUserId() != null) {

                        // updates the current notification to indicate awaiting approval
                        questionnaireWorkflowService.setNotificationActionable(wrapper.getNotificationId(), false, Notification.AWAITING_APPROVAL);
                        workflowAdapter.processApprovalNotification(wrapper.getNotification(), Notification.APPROVE, userId, subjectId, wrapper.getHrUserId());
                        wrapper.setNotificationList(workflowAdapter.getNotifications(userId, performanceReview));
                        wrapper.setAction(Notification.AWAITING_APPROVAL);
                        shouldSetActionable = false;
                    }
                    // todo complete can only happen if
                    if (shouldSetActionable || notification.isApproved()) {
                        questionnaireWorkflowService.setNotificationActionable(wrapper.getNotificationId(), true, COMPLETE_WORKFLOW);
                    }

                    refreshQuestionnaire(wrapper, questionnaire);
                    wrapper.setSaved(true);
                    wrapper.setActiveTab(QUESTIONNAIRE_TAB);
                    break;
                case CLOSE_FORM_TAB:
                    clearInfo(wrapper, true);
                    wrapper.setActiveTab(WORKLIST_TAB);
                    break;
                case VIEW_QUESTIONNAIRE:
                    //todo handleOpenQuestionaire will be better in another method that just does view stuff
                    handleViewQuestionaire(wrapper, request);
                    wrapper.setActiveTab(QUESTIONNAIRE_TAB);
                    break;
                case APPROVE_QUESTIONNAIRE:
                    // this notification needs to be removed for this person, all instances with the same subject_id and hr_id
                    // and performance_id need to be marked as approved
                    setWorkflowParameters(wrapper, request);
                    notification = wrapper.getNotification();
                    workflowAdapter.approveNotification(notification.getSubjectId(), notification.getHrId(), notification.getPerformanceReviewId(), notification.getId());

                    clearInfo(wrapper, true);
                    wrapper.setNotificationList(workflowAdapter.getNotifications(userId, performanceReview));
                    wrapper.setActiveTab(WORKLIST_TAB);
                    wrapper.setAction(Notification.APPROVED);
                    break;
                case VERIFY_QUESTIONNAIRE:

                    // this notification needs to be removed for this person, all instances with the same subject_id and
                    // managers_manager_id and performance_id need to be marked as verified
                    setWorkflowParameters(wrapper, request);
                    notification = wrapper.getNotification();
                    workflowAdapter.verifyNotification(notification.getSubjectId(), notification.getManagersManagerId(), notification.getPerformanceReviewId());
                    clearInfo(wrapper, true);
                    wrapper.setNotificationList(workflowAdapter.getNotifications(userId, performanceReview));
                    wrapper.setActiveTab(WORKLIST_TAB);
                    wrapper.setAction(Notification.VERIFIED);
                    break;
            }

            bindAndValidateAdditionalTargets(request, wrapper, errors, targetPage);

        } catch (WorkflowException e) {
            // generic way of handling workflow errors show the worklist
            wrapper.setActiveTab(WORKLIST_TAB);
        } catch (DomainObjectNotFoundException e) {
            // indicates that user selected a task that has been removed because for example the administrator has deleted the questionnaire instance
            // so the notifiication no longer exists
            errors.reject("error.task.removed", "The requested task has been removed");
        }

        wrapper.setNotificationList(workflowAdapter.getNotifications(userId, wrapper.isPerformanceReview()));
    }

    /**
     * Only called for the HR and manager's manager views
     * @param wrapper the form backing object
     * @param request the request from which the workflow paramaters are read
     * @throws Exception any TalentStudioException errors
     */
    private void handleViewQuestionaire(WorklistWrapper wrapper, HttpServletRequest request) throws Exception {
        clearInfo(wrapper, true);
        final Long workflowId = setWorkflowParameters(wrapper, request);
        if (workflowId != null) {

            final Long userId = ZynapWebUtils.getUserId(request);
            User user = userService.findById(userId);

            setSubject(user, wrapper);
            wrapper.setUserId(userId);
            Notification notification = wrapper.getNotification();

            if(notification != null) {
                Long workflowUserId = notification.getManagerId();
                final Long role = wrapper.getRole();
                Questionnaire questionnaire = questionnaireService.findQuestionnaireByWorkflow(workflowId, workflowUserId, wrapper.getSubjectId(), role);

                // set questionnaire on wrapper and set active tab
                refreshQuestionnaire(wrapper, questionnaire);
                wrapper.setActiveTab(QUESTIONNAIRE_TAB);

                if (performanceReview) {
                    wrapper.setManagersManagerView(Objects.equals(userId, notification.getManagersManagerId()));
                    wrapper.setHrView(Objects.equals(userId, notification.getHrId()));
                }
                // set additional subject data
                setSubjectData(wrapper);
            }
        }
    }

    private boolean isRepondNotificationRequest(HttpServletRequest request) {
        return RequestUtils.getLongParameter(request, NOTIFICATION_ID_PARAM) != null;
    }

    protected void respondAppraisalNotification(WorklistWrapper wrapper, HttpServletRequest request) throws Exception {

    }

    /**
     * Default do-nothing implementation.
     * <br/> Meant to be overriden to support extra functionality.
     *
     * @param wrapper    the WorklistWrapper
     * @param targetPage the page being displayed next
     * @param refData    the map for additional data
     */
    protected void referenceDataAdditionalTargets(WorklistWrapper wrapper, int targetPage, Map<String, Object> refData) {
    }

    /**
     * Default do-nothing implementation.
     * <br/> Meant to be overriden to support extra functionality.
     *
     * @param request    the HttpServletRequest
     * @param wrapper    the WorklistWrapper
     * @param errors     the object containing binding errors
     * @param targetPage the page to be displayed
     * @throws com.zynap.exception.TalentStudioException any errors
     */
    protected void bindAndValidateAdditionalTargets(HttpServletRequest request, WorklistWrapper wrapper, Errors errors, int targetPage) throws Exception {
    }

    /**
     * Do we need data for pickers.
     *
     * @param targetPage the page to be displayed
     * @return true or false
     */
    protected boolean loadPickerData(int targetPage) {
        return (targetPage == SAVE_QUESTIONNAIRE || targetPage == OPEN_QUESTIONNAIRE || targetPage == DELETE_IMAGE_IDX || targetPage == ADD_DYNAMIC_LINE_ITEM || targetPage == DELETE_DYNAMIC_LINE_ITEM);
    }

    /**
     * Set subject data for display.
     * Default do-nothing implementation.
     * <br/> Meant to be overriden to support extra functionality.
     *
     * @param wrapper the WorklistWrapper
     * @throws TalentStudioException for any errors
     */
    protected void setSubjectData(WorklistWrapper wrapper) throws TalentStudioException {
    }

    /**
     * Load questionnaire.
     * <br/> First clears existing data, then sets subject and questionnaire data on wrapper.
     * <br/> Sets active tab to "questionnaire" tab.
     *
     * @param wrapper the WorklistWrapper
     * @param request the HttpServletRequest
     * @throws TalentStudioException if any errors
     */
    private void handleOpenQuestionaire(WorklistWrapper wrapper, HttpServletRequest request) throws TalentStudioException {
        clearInfo(wrapper, true);

        final Long workflowId = setWorkflowParameters(wrapper, request);
        if (workflowId != null) {

            final Long userId = ZynapWebUtils.getUserId(request);
            User user = userService.findById(userId);
            Subject currentUsersSubject = subjectService.findByUserId(userId);
            if (currentUsersSubject != null) {
                wrapper.setUserManagers(currentUsersSubject.getManagers());
            }
            setSubject(user, wrapper);
            wrapper.setUserId(userId);
            // get questionnaire - if null, means there are no answers yet
            final Long role = wrapper.getRole();
            Notification notification = wrapper.getNotification();
            Questionnaire questionnaire = questionnaireService.findQuestionnaireByWorkflow(workflowId, userId, wrapper.getSubjectId(), role);
            final QuestionnaireWorkflow questionnaireWorkflow = questionnaireWorkflowService.findById(workflowId);

            final boolean create = (questionnaire == null);
            if (create) {

                questionnaire = new Questionnaire(null, questionnaireWorkflow, user);
                questionnaire.setNotificationId(wrapper.getNotificationId());
                questionnaire.setSubjectId(wrapper.getSubjectId());
                final boolean isPerformanceReview = (role != null);
                if (isPerformanceReview) {
                    questionnaire.setRole(new LookupValue(role));
                }
                questionnaireService.createOrUpdateQuestionnaire(questionnaire);

                if (performanceReview) {
                    // mark the appraisal as having moved to the next phase
                    if (notification != null) {
                        final Long subjectId = wrapper.getSubjectId();
                        workflowAdapter.processNotification(notification, wrapper.getAction(), userId, subjectId, role);
                    }
                }
            }

            // set questionnaire on wrapper and set active tab
            refreshQuestionnaire(wrapper, questionnaire);
            wrapper.setActiveTab(QUESTIONNAIRE_TAB);

            if (performanceReview && notification != null) {
                wrapper.setManagersManagerView(Objects.equals(userId, notification.getManagersManagerId()));
                wrapper.setHrView(Objects.equals(userId, notification.getHrId()));
            }
            // set additional subject data
            setSubjectData(wrapper);
        }
    }

    /**
     * Find subject - can be evaluatee or self-service.
     *
     * @param user    the logged in user
     * @param wrapper the WorklistWrapper
     * @throws TalentStudioException any data access errors
     */
    private void setSubject(User user, WorklistWrapper wrapper) throws TalentStudioException {
        if (wrapper.getSubjectId() != null) {
            final Subject subject = subjectService.findById(wrapper.getSubjectId());
            wrapper.setSubjectName(subject.getLabel());
        }

        if (wrapper.getSubjectId() == null) wrapper.setSubjectId(user.getSubject().getId());
    }


    private IWorkflowAdapter workflowAdapter;
    private boolean performanceReview;

    /**
     * Constants for notification-related targets.
     */
    static final int TODO_LIST = 0;
    public static final int RESPOND_NOTIFICATION = 1;
    static final int OPEN_QUESTIONNAIRE = 2;
    static final int SAVE_QUESTIONNAIRE = 3;
    static final int CLOSE_FORM_TAB = 4;
    private static final int VIEW_QUESTIONNAIRE = 6;
    private static final int APPROVE_QUESTIONNAIRE = 13;
    private static final int VERIFY_QUESTIONNAIRE = 16;

    /**
     * Constants for tabs.
     */
    static final String WORKLIST_TAB = "worklist";
    static final String QUESTIONNAIRE_TAB = "questionnaire";

    /**
     * Parameter for selected group for evaluator answers.
     */
    public static final String SELECTED_GROUP_PARAM = "selectedGroup";
    private static final String COMPLETE_WORKFLOW = "COMPLETE";

}
