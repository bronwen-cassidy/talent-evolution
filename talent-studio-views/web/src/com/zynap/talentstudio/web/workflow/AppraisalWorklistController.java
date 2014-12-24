package com.zynap.talentstudio.web.workflow;

import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.display.DisplayConfig;
import com.zynap.talentstudio.display.IDisplayConfigService;
import com.zynap.talentstudio.mail.IMailNotification;
import com.zynap.talentstudio.objectives.IObjectiveService;
import com.zynap.talentstudio.objectives.Objective;
import com.zynap.talentstudio.objectives.ObjectiveAssessment;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.performance.IPerformanceReviewService;
import com.zynap.talentstudio.performance.PerformanceEvaluator;
import com.zynap.talentstudio.performance.PerformanceManager;
import com.zynap.talentstudio.performance.PerformanceReview;
import com.zynap.talentstudio.questionnaires.QuestionAttribute;
import com.zynap.talentstudio.questionnaires.Questionnaire;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinitionModel;
import com.zynap.talentstudio.web.NodeInfo;
import com.zynap.talentstudio.web.common.UrlBeanPair;
import com.zynap.talentstudio.web.objectives.AssessmentFormBean;
import com.zynap.talentstudio.web.objectives.ObjectiveWrapperBean;
import com.zynap.talentstudio.web.organisation.DisplayContentWrapper;
import com.zynap.talentstudio.web.organisation.NodeWrapper;
import com.zynap.talentstudio.web.organisation.subjects.SubjectWrapperBean;
import com.zynap.talentstudio.web.perfomance.PerformanceEvaluatorWrapper;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.workflow.performance.PerformanceReviewAnswer;
import com.zynap.talentstudio.web.workflow.performance.PerformanceReviewQuestion;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: amark
 * Date: 18-Aug-2006
 * Time: 16:09:31
 * <p/>
 * Controller that adds additional functionality to WorklistController to handle appraisals.
 * <br/> Handles manager answer review and display of objectives.
 */
public class AppraisalWorklistController extends WorklistController {

    public void setDisplayConfigService(IDisplayConfigService displayConfigService) {
        this.displayConfigService = displayConfigService;
    }

    public void setObjectiveService(IObjectiveService objectiveService) {
        this.objectiveService = objectiveService;
    }

    protected void bindAndValidateAdditionalTargets(HttpServletRequest request, WorklistWrapper wrapper, Errors errors, int targetPage) throws Exception {

        switch (targetPage) {
            case ASSIGN_ROLES:
                handleAssignRoles(wrapper, request);
                break;
            case SAVE_ROLES:
                handleSaveRoles(request, wrapper, errors);
                break;
            case ADD_ROLE:
                wrapper.addEvaluator(new PerformanceEvaluator(new LookupValue(), wrapper.getAppraisalReview(), wrapper.getSubject(), null));
                break;
            case DELETE_ROLE:
                final int intParameter = RequestUtils.getIntParameter(request, ROLE_DEL_INDX, -1);
                if (intParameter != -1) {
                    wrapper.removeEvaluator(intParameter);
                }
                break;
            case REVIEW_EVALUATOR_ANSWERS:
                handleReviewEvaluatorAnswers(wrapper);
                break;
            case VIEW_OBJECTIVE_IDX:
                Long objectiveId = RequestUtils.getRequiredLongParameter(request, IObjectiveService.OBJECTIVE_ID);
                Objective objective = objectiveService.findObjective(objectiveId);
                ObjectiveWrapperBean wrapperBean = new ObjectiveWrapperBean(objective);

                Set<ObjectiveAssessment> objectiveAssessments = objective.getAssessments();
                for (ObjectiveAssessment assessment : objectiveAssessments) {
                    wrapperBean.add(new AssessmentFormBean(assessment));
                }

                wrapperBean.setAssessment(new AssessmentFormBean(objective.getManagerAssessment()));
                wrapper.setObjective(wrapperBean);
                wrapper.setActiveTab(OBJECTIVEINFO_TAB);
                break;
        }
    }

    protected void referenceDataAdditionalTargets(WorklistWrapper wrapper, int targetPage, Map<String, Object> refData) {

        switch (targetPage) {
            case SAVE_ROLES:
                refData.put(MESSAGE_KEY, "appraisal.roles.saved");
                break;
        }

        loadPickerData(targetPage);
    }


    protected void respondAppraisalNotification(WorklistWrapper wrapper, HttpServletRequest request) throws Exception {

        String action = wrapper.getAction();
        if (!WorkflowConstants.START_ASSESMENTS.equals(action)) return;
        PerformanceReview current = (PerformanceReview) performanceReviewService.findById(wrapper.getPerformanceId());
        if (!current.isNotifiable()) return;
        Collection<User> participants = getParticipants(wrapper, current);
        UrlBeanPair pair = mailNotifications.get(APPRAISAL_MAIL);
        IMailNotification mailNotification = pair.getRef();
        mailNotification.send(pair.getUrl(), ZynapWebUtils.getUser(request), current.getEvaluatorWorkflow(), participants.toArray(new User[participants.size()]));
    }

    protected List<User> getParticipants(WorklistWrapper wrapper, PerformanceReview current) throws TalentStudioException {
        final List<PerformanceEvaluator> evaluators = performanceReviewService.getAssignedPerformanceEvaluators(current.getId(), wrapper.getSubjectId());
        List<User> users = new ArrayList<User>();
        for (PerformanceEvaluator evaluator : evaluators) {
            final User evaluatorUser = evaluator.getUser();
            if(!users.contains(evaluatorUser)) users.add(evaluatorUser);
        }
        return users;
    }

    protected boolean loadPickerData(int targetPage) {
        return super.loadPickerData(targetPage) || targetPage == REVIEW_EVALUATOR_ANSWERS;
    }

    /**
     * Get answers supplied by peer, internal customer, or evaluatee (if any) based on group selected by manager and set on wrapper.
     * <br/> Sets active tab to "review" tab.
     *
     * @param wrapper the worklist delegate
     * @throws TalentStudioException if finding a non-existant questionnaire
     */
    private void handleReviewEvaluatorAnswers(WorklistWrapper wrapper) throws TalentStudioException {

        final String selectedGroup = wrapper.getSelectedGroup();
        if (StringUtils.hasText(selectedGroup)) {
            final Collection<Questionnaire> evaluatorQuestionnaires = questionnaireService.findQuestionnaires(wrapper.getEvaluatorWorkflowId(), wrapper.getSubjectId());
            final Collection<PerformanceReviewQuestion> evaluatorQuestions = getEvaluationAnswers(wrapper.getEvaluatorDefinition(), evaluatorQuestionnaires, selectedGroup);
            wrapper.setEvaluatorQuestions(evaluatorQuestions);
        } else {
            wrapper.setEvaluatorQuestions(null);
        }

        wrapper.setActiveTab(REVIEW_TAB);
    }

    /**
     * Clear questionnaire data from wrapper, load appraisal roles and set on wrapper.
     * <br/> Sets active tab to "assign roles" tab.
     *
     * @param wrapper the worklist wrapper
     * @param request the servlet request
     * @throws TalentStudioException any errors
     */
    protected void handleAssignRoles(WorklistWrapper wrapper, HttpServletRequest request) throws TalentStudioException {

        clearInfo(wrapper, true);
        setWorkflowParameters(wrapper, request);

        // check if evaluatee can log in - important as if they cannot self-evaluation is not possible
        boolean evaluateeHasUser = false;
        final Long subjectId = wrapper.getSubjectId();

        try {
            userService.findBySubjectId(subjectId);
            evaluateeHasUser = true;
        } catch (TalentStudioException ex) {
            logger.debug("Subject with ID: " + subjectId + " does not have a user and cannot thus be a self evaluator", ex);
        }

        Subject subject = subjectService.findById(subjectId);

        // get the lookup values of roles we will create a wrappers for what
        List<LookupValue> roleValues = performanceReviewService.getRoles();
        LookupValue evaluateeRole = null;
        for (LookupValue roleValue : roleValues) {
            if (PerformanceEvaluator.SELF_EVALUATOR.equals(roleValue.getValueId())) {
                evaluateeRole = roleValue;
                break;
            }
        }
        // remove the self appraisal role
        roleValues.remove(evaluateeRole);
        wrapper.setRoles(roleValues);

        final PerformanceReview performanceReview = (PerformanceReview) performanceReviewService.findById(wrapper.getPerformanceId());
        wrapper.setAppraisalReview(performanceReview);
        wrapper.setSubject(subject);

        List<PerformanceEvaluator> evaluators = performanceReviewService.getAssignedPerformanceEvaluators(wrapper.getPerformanceId(), subjectId);
        // check for an evaluatee
        PerformanceEvaluator evaluatee = null;
        for (PerformanceEvaluator evaluator : evaluators) {
            if (evaluator.isSelfEvaluator()) {
                evaluatee = evaluator;
                break;
            }
        }

        // these are the assigned ones check if empty
        if (evaluators.isEmpty()) {
            evaluators.add(new PerformanceEvaluator(new LookupValue(), performanceReview, subject, null));
        }

        if (evaluatee == null) {
            evaluatee = new PerformanceEvaluator(evaluateeRole, performanceReview, subject, null);
            evaluators.add(evaluatee);
        }

        wrapper.setPerformanceRoles(evaluators, evaluateeHasUser);

        if (wrapper.getNotification().isUserManaged()) {

            List<Subject> managers = subject.getSubjectManagers();
            List<NodeWrapper> wrappedManagers = new ArrayList<NodeWrapper>();
            for (Subject manager : managers) {
                if (manager.getUser() != null) {
                    wrappedManagers.add(new NodeWrapper(manager));
                }
            }
            wrapper.setManagers(wrappedManagers);
            PerformanceManager performanceManager = performanceReviewService.findPerformanceManager(wrapper.getPerformanceId(), subjectId);
            wrapper.setPerformanceManager(performanceManager);
        }
        wrapper.setActiveTab(ASSIGN_ROLES_TAB);
    }

    /**
     * Save roles assigned by manager if any to db.
     * <br/> Sets active tab to "assign roles" tab.
     *
     * @param request the servlet request
     * @param wrapper the worklist wrapper
     * @param errors
     * @throws TalentStudioException any errors
     */
    protected void handleSaveRoles(HttpServletRequest request, WorklistWrapper wrapper, Errors errors) throws Exception {

        wrapper.setSelectedManagerId(RequestUtils.getLongParameter(request, "selectedManagerId", null));

        final boolean sendToEvaluatee = RequestUtils.getBooleanParameter(request, SELF_EVALUATION_PARAM, false);
        boolean isUserManaged = wrapper.getNotification().isUserManaged();
        final List<PerformanceEvaluator> performanceEvaluators = getModifiedEvaluatorRoles(wrapper, sendToEvaluatee);

        // validate that at least one role, or manager, or self has been selected before proceeding, only happens when the appraisal is user managed
        // when the appraisal is manager driven/managed the manager is always auto-selected
        if (isUserManaged) {
            WorklistValidator validator = (WorklistValidator) getValidator();
            validator.validatePerformanceRoles(wrapper, performanceEvaluators, errors);
        }
        if (!errors.hasErrors()) {
            PerformanceManager performanceManager = wrapper.getModifiedPerformanceManager();

            if (isUserManaged) {
                performanceReviewService.saveRoles(performanceEvaluators, performanceManager);
            } else {
                performanceReviewService.saveRoles(performanceEvaluators, wrapper.getSubjectId(), wrapper.getPerformanceId());
            }
            questionnaireWorkflowService.setNotificationActionable(wrapper.getNotificationId(), true, START_REVIEW);
            wrapper.setPerformanceRoles(performanceReviewService.getAssignedPerformanceEvaluators(wrapper.getPerformanceId(), wrapper.getSubjectId()), wrapper.isEvaluateeHasUser());
            wrapper.setAction(START_REVIEW);
            wrapper.setActiveTab(WORKLIST_TAB);
        }
    }

    /**
     * Get modified roles from wrapper.
     *
     * @param worklistWrapper the worklist wrapper
     * @param sendToEvaluatee is evaluatee to recieve review
     * @return List of {@link PerformanceEvaluator} objects
     * @throws TalentStudioException any errors
     */
    protected List<PerformanceEvaluator> getModifiedEvaluatorRoles(WorklistWrapper worklistWrapper, boolean sendToEvaluatee) throws TalentStudioException {

        final List<PerformanceEvaluator> modifiedRoles = new ArrayList<PerformanceEvaluator>();

        // add evaluators for assigned roles
        final List<PerformanceEvaluatorWrapper> performanceRoles = worklistWrapper.getPerformanceRoles();
        for (PerformanceEvaluatorWrapper performanceEvaluatorWrapper : performanceRoles) {
            final Long performerId = performanceEvaluatorWrapper.getPerformerId();
            final PerformanceEvaluator performanceEvaluator = performanceEvaluatorWrapper.getPerformanceEvaluator();

            if (performerId != null) {
                performanceEvaluator.setUser(userService.getUserById(performerId));
                performanceEvaluator.setUserId(performerId);
            } else {
                performanceEvaluator.setUser(null);
                performanceEvaluatorWrapper.setPerformerLabel(null);
            }

            modifiedRoles.add(performanceEvaluator);
        }

        // add self-evaluator if required
        final PerformanceEvaluator selfEvaluator = worklistWrapper.getEvaluateeRole().getPerformanceEvaluator();
        if (sendToEvaluatee) {
            final User user = userService.findBySubjectId(selfEvaluator.getSubject().getId());
            selfEvaluator.setUser(user);
            selfEvaluator.setUserId(user.getId());
        } else {
            selfEvaluator.setUser(null);
        }

        modifiedRoles.add(selfEvaluator);

        return modifiedRoles;
    }

    /**
     * Get list of answers from questionnaires for specified group.
     *
     * @param definition     the model of the questionnaire definition
     * @param questionnaires the list of questionnaires
     * @param groupName      the name of the group to find
     * @return Collection of {@link PerformanceReviewQuestion} objects.
     */
    private Collection<PerformanceReviewQuestion> getEvaluationAnswers(QuestionnaireDefinitionModel definition, Collection<Questionnaire> questionnaires, String groupName) {

        final List<QuestionAttribute> questions = questionnaireHelper.getQuestions(definition, groupName);
        final Collection<PerformanceReviewQuestion> performanceReviewQuestions = new ArrayList<PerformanceReviewQuestion>();

        for (QuestionAttribute question : questions) {
            final PerformanceReviewQuestion performanceReviewQuestion = new PerformanceReviewQuestion(question.getLabel());
            performanceReviewQuestions.add(performanceReviewQuestion);

            for (Questionnaire questionnaire : questionnaires) {
                final Collection<AttributeValue> values = questionnaire.getDynamicAttributeValues().getValues();

                for (AttributeValue attributeValue : values) {
                    final DynamicAttribute dynamicAttribute = attributeValue.getDynamicAttribute();
                    if (question.getDynamicAttribute().equals(dynamicAttribute)) {
                        // check if node label must be set (applies to attributes that point to other artefacts such as org units or positions)
                        attributeValue.setLabel(dynamicAttributeService.getDomainObjectLabel(attributeValue));
                        final PerformanceReviewAnswer answer = new PerformanceReviewAnswer(questionnaire.getUser(), questionnaire.getRole(), attributeValue);
                        performanceReviewQuestion.addAnswer(answer);
                    }
                }
            }
        }
        return performanceReviewQuestions;
    }

    /**
     * Get objectives for evaluatee.
     * <br/> Used when manager is reviewing answers.
     *
     * @param wrapper the worklist wrapper
     * @throws TalentStudioException any errors
     */
    protected void setSubjectData(WorklistWrapper wrapper) throws TalentStudioException {

        if (wrapper.isManagerEvaluation()) {
            // need the artefact for the executive summary, and the objectives
            final Long subjectId = wrapper.getSubjectId();
            final Subject subject = subjectService.findById(subjectId);
            wrapper.setArtefact(new SubjectWrapperBean(subject));
            wrapper.setNodeInfo(new NodeInfo(subject, subject.getPrimaryPositions(), subject.getPrimaryOrganisationUnits()));

            // load the objectives
            final Collection<Objective> objectives = objectiveService.findCurrentObjectives(subject.getId());
            wrapper.setObjectives(objectives);

            // set the display config information
            final Report execSummaryReport = displayConfigService.findDisplayConfigReport(Node.SUBJECT_UNIT_TYPE_, DisplayConfig.EXECUTIVE_SUMMARY_TYPE);
            DisplayContentWrapper displayContentWrapper = new DisplayContentWrapper();
            displayContentWrapper.setExecutiveSummaryReport(execSummaryReport);
            wrapper.setDisplayConfigView(displayContentWrapper);
        }
    }

    public void setPerformanceReviewService(IPerformanceReviewService performanceReviewService) {
        this.performanceReviewService = performanceReviewService;
    }

    /**
     * Constants for role-related targets.
     */
    private static final int ASSIGN_ROLES = 5;
    private static final int SAVE_ROLES = 10;
    private static final int ADD_ROLE = 14;
    private static final int DELETE_ROLE = 15;

    /**
     * Constants for review-related targets.
     */
    private static final int REVIEW_EVALUATOR_ANSWERS = 7;

    /**
     * Constants for objective-related targets.
     */
    private static final int VIEW_OBJECTIVE_IDX = 9;

    private static final String REVIEW_TAB = "review";
    private static final String ASSIGN_ROLES_TAB = "roles";
    private static final String OBJECTIVEINFO_TAB = "objectiveinfo";

    private final String APPRAISAL_MAIL = "APPRAISAL";

    private IDisplayConfigService displayConfigService;
    private IObjectiveService objectiveService;
    protected IPerformanceReviewService performanceReviewService;
    protected static final String START_REVIEW = "START";
    private static final String ROLE_DEL_INDX = "delIndx";
}
