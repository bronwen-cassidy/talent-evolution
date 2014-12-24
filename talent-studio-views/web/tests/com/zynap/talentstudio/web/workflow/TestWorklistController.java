package com.zynap.talentstudio.web.workflow;

/**
 * User: amark
 * Date: 21-Aug-2006
 * Time: 10:55:47
 */

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.questionnaires.IQueDefinitionService;
import com.zynap.talentstudio.questionnaires.IQueWorkflowService;
import com.zynap.talentstudio.questionnaires.Questionnaire;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.utils.ZynapDbUnitMockControllerTestCase;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.validation.Errors;

import java.util.List;
import java.util.Map;

public class TestWorklistController extends ZynapDbUnitMockControllerTestCase implements WorkflowConstants {

    protected void setUp() throws Exception {
        super.setUp();

        worklistController = (WorklistController) getBean("worklistQuestionnaireController");
        questionnaireWorkflowService = (IQueWorkflowService) getBean("queWorkflowService");
        questionnaireDefinitionService = (IQueDefinitionService) getBean("questionnaireDefinitionService");
    }

    protected String getDataSetFileName() throws Exception {
        return "test-data.xml";
    }

    public void testIsFormSubmission() throws Exception {

        assertFalse(worklistController.isFormSubmission(mockRequest));

        mockRequest.addParameter(ControllerConstants.FORM_SUBMISSION, Boolean.TRUE.toString());
        assertTrue(worklistController.isFormSubmission(mockRequest));
    }

    public void testIsFormSubmissionDisplayTagSort() throws Exception {

        assertFalse(worklistController.isFormSubmission(mockRequest));

        mockRequest.addParameter(ControllerConstants.DISPLAY_TAG_PREFIX, "123");
        assertTrue(worklistController.isFormSubmission(mockRequest));
    }

    public void testGetTargetPage() throws Exception {

        // default behaviour
        int currentPage = 5;
        int targetPage = worklistController.getTargetPage(mockRequest, currentPage);
        assertEquals(currentPage, targetPage);

        // test with POST - will not work
        final int newPage = 25;
        mockRequest.addParameter(ControllerConstants.PAGE_NUM, Integer.toString(newPage));
        targetPage = worklistController.getTargetPage(mockRequest, currentPage);
        assertEquals(currentPage, targetPage);

        // test with GET - will work
        mockRequest.setMethod(ZynapWebUtils.GET_METHOD);
        targetPage = worklistController.getTargetPage(mockRequest, currentPage);
        assertEquals(newPage, targetPage);
    }

    public void testFormBackingObject() throws Exception {

        final String workflowType = "questionnaire";
        final String notificationStatus = "closed";

        final WorklistWrapper formBackingObject = getFormBackingObject(workflowType, notificationStatus);

        assertFalse(formBackingObject.isFatalErrors());
        assertFalse(formBackingObject.isEvaluateeHasUser());
        assertFalse(formBackingObject.isPerformanceReview());

        assertEquals(workflowType, formBackingObject.getWorkflowType());
        assertEquals(notificationStatus, formBackingObject.getNotificationStatus());
        assertEquals(WorklistController.WORKLIST_TAB, formBackingObject.getActiveTab());
        assertNotNull(formBackingObject.getNotificationList());

        assertNotificationPropertiesNotSet(formBackingObject);
        assertQuestionnairePropertiesNotSet(formBackingObject);
        assertRolePropertiesNotSet(formBackingObject);
        assertReviewPropertiesNotSet(formBackingObject);
        assertObjectiveListPropertiesNotSet(formBackingObject);
        assertObjectivePropertiesNotSet(formBackingObject);
    }

    public void testProcessFinish() throws Exception {

        final String workflowType = "questionnaire";
        final String notificationStatus = "closed";
        final WorklistWrapper formBackingObject = getFormBackingObject(workflowType, notificationStatus);

        assertNull(worklistController.processFinishInternal(mockRequest, mockResponse, formBackingObject, getErrors(formBackingObject)));
    }

    public void testLoadPickerData() throws Exception {

        assertFalse(worklistController.loadPickerData(WorklistController.TODO_LIST));
        assertTrue(worklistController.loadPickerData(WorklistController.OPEN_QUESTIONNAIRE));
        assertTrue(worklistController.loadPickerData(WorklistController.SAVE_QUESTIONNAIRE));
        assertTrue(worklistController.loadPickerData(WorklistController.DELETE_IMAGE_IDX));
        assertTrue(worklistController.loadPickerData(WorklistController.ADD_DYNAMIC_LINE_ITEM));
        assertTrue(worklistController.loadPickerData(WorklistController.DELETE_DYNAMIC_LINE_ITEM));
    }

    public void testSetWorkflowParameters() throws Exception {

        final String workflowType = "questionnaire";
        final String notificationStatus = "closed";
        final WorklistWrapper formBackingObject = getFormBackingObject(workflowType, notificationStatus);

        final Long workflowId = WORKFLOW_ID;
        final Long notificationId = NOTIFICATION_ID;
        final Long subjectId = SUBJECT_ID;
        setWorkflowParameters(notificationId, workflowId, subjectId, null);

        final Long id = worklistController.setWorkflowParameters(formBackingObject, mockRequest);
        assertEquals(workflowId, id);
        assertEquals(notificationId, formBackingObject.getNotificationId());
        assertEquals(subjectId, formBackingObject.getSubjectId());
        assertEquals(workflowId, formBackingObject.getWorkflowId());
        assertNull(formBackingObject.getRole());

        // set role on request and do it again
        mockRequest.addParameter(ROLE_PARAM_PREFIX + notificationId, ROLE_ID);
        worklistController.setWorkflowParameters(formBackingObject, mockRequest);
        assertEquals(ROLE_ID, formBackingObject.getRole().toString());
    }

    public void testOnBindAndValidate() throws Exception {

        final String workflowType = "questionnaire";
        final String notificationStatus = "closed";
        final WorklistWrapper formBackingObject = getFormBackingObject(workflowType, notificationStatus);

        final Errors errors = getErrors(formBackingObject);
        worklistController.onBindAndValidateInternal(mockRequest, formBackingObject, errors, WorklistController.TODO_LIST);

        assertNotificationPropertiesNotSet(formBackingObject);
        assertQuestionnairePropertiesNotSet(formBackingObject);
        assertRolePropertiesNotSet(formBackingObject);
        assertReviewPropertiesNotSet(formBackingObject);
        assertObjectiveListPropertiesNotSet(formBackingObject);
        assertObjectivePropertiesNotSet(formBackingObject);
    }

    public void testReferenceData() throws Exception {

        final String workflowType = "questionnaire";
        final String notificationStatus = "closed";
        final WorklistWrapper formBackingObject = getFormBackingObject(workflowType, notificationStatus);

        final Errors errors = getErrors(formBackingObject);
        Map refData = worklistController.referenceData(mockRequest, formBackingObject, errors, WorklistController.TODO_LIST);
        assertEquals(0, refData.size());
        assertEquals(WorklistController.WORKLIST_TAB, formBackingObject.getActiveTab());

        assertNotificationPropertiesNotSet(formBackingObject);
        assertQuestionnairePropertiesNotSet(formBackingObject);
        assertRolePropertiesNotSet(formBackingObject);
        assertReviewPropertiesNotSet(formBackingObject);
        assertObjectiveListPropertiesNotSet(formBackingObject);
        assertObjectivePropertiesNotSet(formBackingObject);
    }

    public void testOpenQuestionnaire() throws Exception {

        final String workflowType = "questionnaire";
        final String notificationStatus = "closed";
        final WorklistWrapper formBackingObject = getFormBackingObject(workflowType, notificationStatus);

        // open questionnaire
        final int currentPage = WorklistController.TODO_LIST;
        final Errors errors = openQuestionnaire(formBackingObject, currentPage);

        // check questionnaire state set on wrapper
        assertQuestionnaireDetails(formBackingObject, errors, currentPage);
    }

    public void testCloseForm() throws Exception {

        final String workflowType = "questionnaire";
        final String notificationStatus = "closed";
        final WorklistWrapper formBackingObject = getFormBackingObject(workflowType, notificationStatus);

        // open questionnaire
        int currentPage = WorklistController.TODO_LIST;
        final Errors errors = openQuestionnaire(formBackingObject, currentPage);

        // check questionnaire state set on wrapper
        assertQuestionnaireDetails(formBackingObject, errors, currentPage);

        // close
        currentPage = WorklistController.OPEN_QUESTIONNAIRE;
        setTargetPage(WorklistController.CLOSE_FORM_TAB);
        worklistController.onBindAndValidateInternal(mockRequest, formBackingObject, errors, currentPage);

        // check tab
        assertEquals(WorklistController.WORKLIST_TAB, formBackingObject.getActiveTab());

        // check notification list properties
        assertNotNull(formBackingObject.getNotificationList());
        assertEquals(workflowType, formBackingObject.getWorkflowType());
        assertEquals(notificationStatus, formBackingObject.getNotificationStatus());

        // check other properties
        assertQuestionnairePropertiesNotSet(formBackingObject);
        assertRolePropertiesNotSet(formBackingObject);
        assertReviewPropertiesNotSet(formBackingObject);
        assertObjectiveListPropertiesNotSet(formBackingObject);
        assertObjectivePropertiesNotSet(formBackingObject);
        assertNotificationPropertiesNotSet(formBackingObject);
    }


    private void setWorkflowParameters(final Long notificationId, final Long workflowId, final Long subjectId, String role) {
        mockRequest.addParameter(NOTIFICATION_ID_PARAM, notificationId.toString());
        mockRequest.addParameter(WORKFLOW_ID_PARAM_PREFIX + notificationId, workflowId.toString());
        mockRequest.addParameter(SUBJECT_ID_PARAM_PREFIX + notificationId, subjectId.toString());
        if (role != null) mockRequest.addParameter(ROLE_PARAM_PREFIX + notificationId, role);
    }

    private WorklistWrapper getFormBackingObject(final String workflowType, final String notificationStatus) throws Exception {

        setUserSession(new UserSession(getAdminUserPrincipal(), getArenaMenuHandler()), mockRequest);
        mockRequest.addParameter(WORKFLOW_TYPE_PARAM, workflowType);
        if (notificationStatus != null) mockRequest.addParameter(NOTIFICATION_STATUS_PARAM, notificationStatus);

        return (WorklistWrapper) worklistController.formBackingObject(mockRequest);
    }

    private void assertObjectivePropertiesNotSet(final WorklistWrapper formBackingObject) {
        assertNull(formBackingObject.getObjective());
    }

    private void assertObjectiveListPropertiesNotSet(final WorklistWrapper formBackingObject) {
        assertNull(formBackingObject.getArtefact());
        assertNull(formBackingObject.getObjectives());
        assertNull(formBackingObject.getDisplayConfigView());
    }

    private void assertReviewPropertiesNotSet(final WorklistWrapper formBackingObject) {
        assertNull(formBackingObject.getSelectedGroup());
        assertNull(formBackingObject.getEvaluatorWorkflowId());
        assertNull(formBackingObject.getEvaluatorDefinition());
        assertNull(formBackingObject.getEvaluatorQuestions());
    }

    private void assertRolePropertiesNotSet(final WorklistWrapper formBackingObject) {
        assertNull(formBackingObject.getPerformanceRoles());
        assertNull(formBackingObject.getEvaluateeRole());
        assertFalse(formBackingObject.isEvaluateeHasUser());
    }

    private void assertQuestionnairePropertiesNotSet(final WorklistWrapper formBackingObject) {
        assertNull(formBackingObject.getNode());
        assertNull(formBackingObject.getSubjectId());
        assertNull(formBackingObject.getQuestionnaire());
        assertNull(formBackingObject.getQuestionnaireDefinition());

        final List questionnaireGroups = formBackingObject.getQuestionnaireGroups();
        assertTrue(questionnaireGroups == null || questionnaireGroups.isEmpty());

        final List wrappedDynamicAttributes = formBackingObject.getWrappedDynamicAttributes();
        assertTrue(wrappedDynamicAttributes == null || wrappedDynamicAttributes.isEmpty());

        assertEquals("", formBackingObject.getQuestionnaireLabel());
    }

    private void assertNotificationPropertiesNotSet(final WorklistWrapper formBackingObject) {
        assertNull(formBackingObject.getNotification());
        assertNull(formBackingObject.getAction());
        assertNull(formBackingObject.getWorkflowId());
        assertNull(formBackingObject.getSubjectId());
        assertNull(formBackingObject.getRole());
        assertNull(formBackingObject.getNotificationId());
    }

    private void assertQuestionnaireDetails(final WorklistWrapper formBackingObject, final Errors errors, final int currentPage) throws Exception {

        assertFalse(errors.hasErrors());
        assertFalse(errors.hasGlobalErrors());

        assertEquals(WorklistController.QUESTIONNAIRE_TAB, formBackingObject.getActiveTab());
        assertEquals(SUBJECT_ID, formBackingObject.getSubjectId());

        final Questionnaire questionnaire = formBackingObject.getQuestionnaire();
        assertNotNull(questionnaire);
        assertNotNull(questionnaire.getId());
        assertEquals(questionnaire, formBackingObject.getNode());

        final QuestionnaireDefinition definition = questionnaireDefinitionService.findDefinition(QUE_DEFINITION_ID);
        final QuestionnaireWorkflow workflow = (QuestionnaireWorkflow) questionnaireWorkflowService.findById(WORKFLOW_ID);

        assertEquals(definition, formBackingObject.getQuestionnaireDefinition());
        assertNotNull(formBackingObject.getQuestionnaireGroups());
        assertNotNull(formBackingObject.getWrappedDynamicAttributes());
        assertEquals(workflow.getLabel(), formBackingObject.getQuestionnaireLabel());

        final Map refData = worklistController.referenceData(mockRequest, formBackingObject, errors, currentPage);
        // 2 refdata, as the questionnaire is not null, we need the image url as well as the tree
        assertEquals(2, refData.size());
        assertNotNull(refData.get("orgUnitTree"));
        assertNotNull(refData.get("imageUrl"));

        assertRolePropertiesNotSet(formBackingObject);
        assertReviewPropertiesNotSet(formBackingObject);
        assertObjectiveListPropertiesNotSet(formBackingObject);
        assertObjectivePropertiesNotSet(formBackingObject);
    }

    private Errors openQuestionnaire(final WorklistWrapper formBackingObject, final int currentPage) throws Exception {

        setTargetPage(WorklistController.OPEN_QUESTIONNAIRE);
        setWorkflowParameters(NOTIFICATION_ID, WORKFLOW_ID, SUBJECT_ID, ROLE_ID);
        final Errors errors = getErrors(formBackingObject);
        worklistController.onBindAndValidateInternal(mockRequest, formBackingObject, errors, currentPage);

        return errors;
    }

    private WorklistController worklistController;
    private IQueWorkflowService questionnaireWorkflowService;
    private IQueDefinitionService questionnaireDefinitionService;


    private static final Long SUBJECT_ID = new Long(23);
    private static final Long WORKFLOW_ID = new Long(1);
    private static final Long NOTIFICATION_ID = new Long(-9);
    private static final Long QUE_DEFINITION_ID = new Long(1);

    private static final String ROLE_ID = "12";
}
