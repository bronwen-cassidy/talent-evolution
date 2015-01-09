package com.zynap.talentstudio.questionnaires;

/**
 * Class or Interface description.
 *
 * @author syeoh
 * @since 26-Jun-2007 17:23:53
 * @version 0.1
 */


import java.util.List;

public class TestQuestionnaireDefinitionService extends TestAbstractQuestionnaireService {

    public void testUpdate() throws Exception {
        final QuestionnaireDefinition definition = getDefinition();
        definition.setLabel("Test 2");
        questionnaireDefinitionService.update(definition);
    }

    public void testFindAll() throws Exception {

        // create two extra definitions
        createDefinition("com/zynap/talentstudio/questionnaires/xml/Self Evaluation.xml");
        createDefinition("com/zynap/talentstudio/questionnaires/xml/CPWPerformancePotentialQuestionnaire.xml");

        // noinspection unchecked
        final List<QuestionnaireDefinition> allQuestionnaireDefinitions = questionnaireDefinitionService.findAll();

        assertNotNull(allQuestionnaireDefinitions);
        assertEquals(3, allQuestionnaireDefinitions.size());

        String[] labelNames = {"CPW Succession Performance / Potential Questionnaire", "Manager Evaluation2", "Self Evaluation"};

        // assert the order is correct
        int count = 0;
        for (QuestionnaireDefinition questionnaireDefinition : allQuestionnaireDefinitions) {
            assertEquals(labelNames[count], questionnaireDefinition.getLabel());
            count++;
        }
    }

    public void testCreate() throws Exception {

        final String testFile = "com/zynap/talentstudio/questionnaires/xml/Self Evaluation.xml";
        final QuestionnaireDefinition questionnaireDefinition = createDefinition(testFile);

        final QuestionnaireDefinition actual = questionnaireDefinitionService.findDefinition(questionnaireDefinition.getId());
        assertNotNull(actual);


        final String testFile2 = "com/zynap/talentstudio/questionnaires/xml/CPWPerformancePotentialQuestionnaire.xml";
        final QuestionnaireDefinition questionnaireDefinition2 = createDefinition(testFile2);

        final QuestionnaireDefinition actual2 = questionnaireDefinitionService.findDefinition(questionnaireDefinition2.getId());
        assertNotNull(actual2);
    }

    public void testFindDefinition() throws Exception {
        final QuestionnaireDefinition definition = questionnaireDefinitionService.findDefinition(QUE_DEFINITION_ID);
        assertNotNull(definition);
    }

}