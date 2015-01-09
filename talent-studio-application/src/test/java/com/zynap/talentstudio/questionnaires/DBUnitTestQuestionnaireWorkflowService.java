package com.zynap.talentstudio.questionnaires;

/**
 * Class or Interface description.
 *
 * @author syeoh
 * @since 20-Jun-2007 09:59:48
 * @version 0.1
 */

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.questionnaires.support.RepublishResults;

import java.util.Collection;
import java.util.Date;

public class DBUnitTestQuestionnaireWorkflowService extends TestAbstractQuestionnaireService {

    protected String getDataSetFileName() {
        return "test-questionnaireworkflow-data.xml";
    }

    public void testFindAll() throws Exception {
        final Collection allWorkflows = questionnaireWorkflowService.findAll();
        assertEquals(allWorkflows.size(), 3);
    }

    public void testUpdate() throws Exception {
        final QuestionnaireWorkflow workflow = (QuestionnaireWorkflow) questionnaireWorkflowService.findById(QUE_WORKFLOW_ID);
        workflow.setLabel("Test 2");
        questionnaireWorkflowService.update(workflow);
    }


    // todo: review this method
    public void testCreate() throws Exception {

        // create and test a questionnaire
        final User user = getAdminUser(userService);
        QuestionnaireWorkflow questionnaireWorkflow = buildWorkflow(user);
        questionnaireWorkflow.setWorkflowType(QuestionnaireWorkflow.TYPE_QUESTIONNAIRE);
        questionnaireWorkflowService.create(questionnaireWorkflow);
        assertEquals(QuestionnaireWorkflow.STATUS_NEW, questionnaireWorkflow.getStatus());
        assertEquals(QuestionnaireWorkflow.TYPE_QUESTIONNAIRE, questionnaireWorkflow.getWorkflowType());

        // create and test an infoform
        final User user2 = getAdminUser(userService);
        QuestionnaireWorkflow questionnaireWorkflowInfoForm = buildWorkflow(user2);
        questionnaireWorkflowInfoForm.setWorkflowType(QuestionnaireWorkflow.TYPE_INFO_FORM);
        questionnaireWorkflowService.create(questionnaireWorkflowInfoForm);
        assertEquals(QuestionnaireWorkflow.STATUS_NEW, questionnaireWorkflowInfoForm.getStatus());
        assertEquals(QuestionnaireWorkflow.TYPE_INFO_FORM, questionnaireWorkflowInfoForm.getWorkflowType());
    }

     

    protected QuestionnaireWorkflow buildWorkflow(final User user) throws Exception {

        QuestionnaireDefinition definition = getDefinition();
        QuestionnaireWorkflow questionnaireWorkflow = new QuestionnaireWorkflow();
        questionnaireWorkflow.setLabel(definition.getLabel());
        questionnaireWorkflow.setUserId(user.getId());
        questionnaireWorkflow.setQuestionnaireDefinition(definition);

        final Population allSubjectsPopulation = (Population) analysisService.findById(IPopulationEngine.ALL_PEOPLE_POPULATION_ID);
        questionnaireWorkflow.setPopulation(allSubjectsPopulation);

        return questionnaireWorkflow;
    }

    // TODO: seperate this into logical methods
    public void testRepublishWorkflow() throws Exception {

        // all people should only be part of 3 users
        assertTrue(subjectHasInfoForm(SUBJECT_ID_1, ALL_PEOPLE_QUE_ID));
        assertTrue(subjectHasInfoForm(SUBJECT_ID_2, ALL_PEOPLE_QUE_ID));
        assertTrue(subjectHasInfoForm(SUBJECT_ID_3, ALL_PEOPLE_QUE_ID));
        assertFalse(subjectHasInfoForm(SUBJECT_ID_4, ALL_PEOPLE_QUE_ID));
        assertFalse(subjectHasInfoForm(SUBJECT_ID_5, ALL_PEOPLE_QUE_ID));

        // republish workflow   (original xml only maps 3 out of the 5 users)
        RepublishResults results = questionnaireWorkflowService.republishWorkflow(ALL_PEOPLE_QUE_ID, ADMIN_ID);
        final Date date = results.getCompletedDate();

        // retrieve the workflow and assert it has the correct republish date
        final QuestionnaireWorkflow workflow = questionnaireWorkflowService.findWorkflowById(ALL_PEOPLE_QUE_ID);
        // assert republish date is as expected    by checking completed date against last republished date
        //in this test scenario both should be equal
        assertEquals(date, workflow.getLastRepublishedDate());
        
        assertEquals(results.getUsersAdded().length, 2);
        assertEquals(results.getUsersRemoved().length, 0);

        // assert item is turning up in portfolio for users 4 and 5
        assertTrue(subjectHasInfoForm(SUBJECT_ID_4, ALL_PEOPLE_QUE_ID));
        assertTrue(subjectHasInfoForm(SUBJECT_ID_5, ALL_PEOPLE_QUE_ID));

        // now let's check that all the Sarah's have the Sarah infoform
        assertTrue(subjectHasInfoForm(SUBJECT_ID_1, SARAH_QUE_ID));
        assertTrue(subjectHasInfoForm(SUBJECT_ID_2, SARAH_QUE_ID));
        assertTrue(subjectHasInfoForm(SUBJECT_ID_3, SARAH_QUE_ID));
        assertFalse(subjectHasInfoForm(SUBJECT_ID_4, SARAH_QUE_ID));
        assertFalse(subjectHasInfoForm(SUBJECT_ID_4, SARAH_QUE_ID));

        // now let's check that all the Fred's have the Fred infoform
        assertFalse(subjectHasInfoForm(SUBJECT_ID_1, FRED_QUE_ID));
        assertFalse(subjectHasInfoForm(SUBJECT_ID_2, FRED_QUE_ID));
        assertFalse(subjectHasInfoForm(SUBJECT_ID_3, FRED_QUE_ID));
        assertTrue(subjectHasInfoForm(SUBJECT_ID_4, FRED_QUE_ID));
        assertTrue(subjectHasInfoForm(SUBJECT_ID_5, FRED_QUE_ID));

        // all is good. let's change some people's names then republish and check the new changes

        // change subject 2 and 3 to Fred
        Subject subject2 = subjectService.findByUserId(SUBJECT_ID_2);
        subject2.setFirstName(FRED_NAME);
        subjectService.update(subject2);

        Subject subject3 = subjectService.findByUserId(SUBJECT_ID_3);
        subject3.setFirstName(FRED_NAME);
        subjectService.update(subject3);

        // change subject 4 to Sarah
        Subject subject4 = subjectService.findByUserId(SUBJECT_ID_4);
        subject4.setFirstName(SARAH_NAME);

        subjectService.update(subject4);

        // republish Sarah workflow
        RepublishResults sarahRepublishResults = questionnaireWorkflowService.republishWorkflow(SARAH_QUE_ID, ADMIN_ID);

        // check that there were one added and two removed
        assertEquals(sarahRepublishResults.getUsersAdded().length, 1);
        assertEquals(sarahRepublishResults.getUsersRemoved().length, 2);

        // republish Fred workflow
        RepublishResults fredRepublishResults = questionnaireWorkflowService.republishWorkflow(FRED_QUE_ID, ADMIN_ID);

        // check that there was two added and one removed
        assertEquals(fredRepublishResults.getUsersAdded().length, 2);
        assertEquals(fredRepublishResults.getUsersRemoved().length, 1);

        // check again that the correct info_form for each user is turning up in the correct profile.

        // for Fred it should now be subjects 2, 3, 5
        assertFalse(subjectHasInfoForm(SUBJECT_ID_1, FRED_QUE_ID));
        assertTrue(subjectHasInfoForm(SUBJECT_ID_2, FRED_QUE_ID));
        assertTrue(subjectHasInfoForm(SUBJECT_ID_3, FRED_QUE_ID));
        assertFalse(subjectHasInfoForm(SUBJECT_ID_4, FRED_QUE_ID));
        assertTrue(subjectHasInfoForm(SUBJECT_ID_5, FRED_QUE_ID));

        // for Sarah it should now be subjects 1 and 4
        assertTrue(subjectHasInfoForm(SUBJECT_ID_1, SARAH_QUE_ID));
        assertFalse(subjectHasInfoForm(SUBJECT_ID_2, SARAH_QUE_ID));
        assertFalse(subjectHasInfoForm(SUBJECT_ID_3, SARAH_QUE_ID));
        assertTrue(subjectHasInfoForm(SUBJECT_ID_4, SARAH_QUE_ID));
        assertFalse(subjectHasInfoForm(SUBJECT_ID_5, SARAH_QUE_ID));

    }

    public void testFindById() throws Exception {
        QuestionnaireWorkflow queWorkflow = (QuestionnaireWorkflow) questionnaireWorkflowService.findById(QUE_WORKFLOW_ID);
        assertNotNull(queWorkflow);
    }

    public void testFindWorkflow() throws Exception {
        final QuestionnaireWorkflow workflow = (QuestionnaireWorkflow) questionnaireWorkflowService.findById(QUE_WORKFLOW_ID);
        assertNotNull(workflow);
    }

    protected QuestionnaireDefinition getDefinition() throws Exception {
        return questionnaireDefinitionService.findDefinition(QUE_DEF_ID);
    }

    public void testCloseInfoForm() throws Exception {

       final User user = getAdminUser(userService);
       QuestionnaireWorkflow questionnaireWorkflow = buildWorkflow(user);

       questionnaireWorkflow.setWorkflowType(QuestionnaireWorkflow.TYPE_INFO_FORM);
       questionnaireWorkflowService.create(questionnaireWorkflow);
       assertEquals(QuestionnaireWorkflow.STATUS_NEW, questionnaireWorkflow.getStatus());

       questionnaireWorkflowService.startWorkflow(questionnaireWorkflow);
       assertFalse(questionnaireWorkflow.hasProcess());
       assertEquals(QuestionnaireWorkflow.STATUS_PUBLISHED, questionnaireWorkflow.getStatus());

       questionnaireWorkflowService.closeWorkflow(questionnaireWorkflow);
       assertFalse(questionnaireWorkflow.hasProcess());
       assertEquals(QuestionnaireWorkflow.STATUS_COMPLETED, questionnaireWorkflow.getStatus());
   }
    
    // workflow id's
    private final Long ALL_PEOPLE_QUE_ID = (long) 16;
    private final Long SARAH_QUE_ID = (long) 15;
    private final Long FRED_QUE_ID = (long) 17;

    // subject id's
    private final Long SUBJECT_ID_1 = (long) -44;
    private final Long SUBJECT_ID_2 = (long) -34;
    private final Long SUBJECT_ID_3 = (long) -321;
    private final Long SUBJECT_ID_4 = (long) -322;
    private final Long SUBJECT_ID_5 = (long) -323;

    // admin id
    private final Long ADMIN_ID = (long) 1;

    // first names for testing purposes
    private final String SARAH_NAME = "Sarah";
    private final String FRED_NAME = "Fred";

    public static final Long QUE_WORKFLOW_ID = (long) 15;
    protected static final Long QUE_DEF_ID = (long) -11;

}
