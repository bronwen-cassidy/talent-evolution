/* 
 * Copyright (C)  Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.integration.adapter;

import com.zynap.talentstudio.integration.BaseIntegrationDatabaseTest;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.questionnaires.Questionnaire;
import com.zynap.talentstudio.questionnaires.IQuestionnaireService;

import java.util.List;
import java.util.Iterator;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author syeoh
 * @version 0.1
 * @since 15-Mar-2007 11:17:50
 */

public class TestDatabaseXmlAdaptor extends BaseIntegrationDatabaseTest {

    protected void setUp() throws Exception {
        super.setUp();
        xmlAdapter = (IXmlAdapter) applicationContext.getBean("xmlAdapter");
    }


    public void testAnswerQuestionnaireWorkflow() throws Exception {

        String answerQuestionnaireXml = TestXmlAdaptor.getXmlString("com/zynap/talentstudio/integration/adapter/questionnaire-test-data.xml");

        // create a new set of answers according to questionnaire-test-data.xml
        xmlAdapter.execute(answerQuestionnaireXml, ATTACHMENTS, USER_NAME);

        Questionnaire selectedQuestionnaire = null;                                                                     
        IQuestionnaireService questService = (IQuestionnaireService) getBean("questionnaireService");
        final List<Questionnaire> questionnaires = questService.findAll();

        // find the questionnaire which we just added
        for (Questionnaire questionnaire : questionnaires) {
            Set extendedAttributes = questionnaire.getExtendedAttributes();
            for (Iterator attrIterator = extendedAttributes.iterator(); attrIterator.hasNext();) {
                NodeExtendedAttribute extAttribute = (NodeExtendedAttribute) attrIterator.next();
                if (extAttribute.getValue().equals(TEST_TITLE)) {
                    selectedQuestionnaire = questionnaire;
                    break;
                }
            }
        }


        Long workflowId = new Long(WORKFLOW_ID);
        Long subjectId = new Long(SUBJECT_ID);

        // assert questionnaire was added
        assertNotNull(selectedQuestionnaire);
        assertEquals(selectedQuestionnaire.getQuestionnaireWorkflowId(), workflowId);
        assertEquals(selectedQuestionnaire.getQuestionnaireWorkflow().getId(), workflowId);
        assertEquals(selectedQuestionnaire.getSubjectId(), subjectId);

    }

    protected String getDataSetFileName() {
        return "test-questionnaire-data.xml";
    }

    private IXmlAdapter xmlAdapter;

    private static final String SUBJECT_ID = "2";
    private static final String WORKFLOW_ID = "12";
    private static final String TEST_TITLE = "MyTestTitle";
    private static final String USER_NAME = "webserviceuser";
    private static final byte[][] ATTACHMENTS = new byte[][]{"Test of byte".getBytes(), "Hello World".getBytes()};
}
