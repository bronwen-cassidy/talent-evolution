package com.zynap.talentstudio.questionnaires;

/**
 * User: amark
 * Date: 29-Aug-2006
 * Time: 16:56:55
 */

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.ZynapDatabaseTestCase;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.attributes.DynamicAttributeReference;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.exception.TalentStudioException;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TestQuestionCalculator extends ZynapDatabaseTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        userService = (IUserService) getBean("userService");
    }

    protected String getDataSetFileName() {
        return "question-calculator-test-data.xml";
    }

    public void testCalculate() throws Exception {
    }

    public void testCalculateSum() throws Exception {
        // a sum dependant dynamicAttribute id = 250
        // an enum_mapping dependant dynamicAttribute id = 257, 251
        User user = getAdminUser(userService);
        IQuestionnaireService questionnaireService = (IQuestionnaireService) getBean("questionnaireService");
        Questionnaire questionnaire = questionnaireService.findOrCreateQuestionnaire(WORKFLOW_ID, user.getId(), (long) -32);


        IDynamicAttributeService attributeService = (IDynamicAttributeService) getBean("dynamicAttrService");

        DynamicAttribute dynamicAttribute = attributeService.findById((long) 252);
        List<String> results = new ArrayList<String>();
        testResult(questionnaireService, questionnaire, dynamicAttribute, results, user);
        assertEquals(2, results.size());
        
        assertSame(results.get(0),results.get(0));
        assertEquals(results.get(1),"Low");
    }

    private void testResult(IQuestionnaireService questionnaireService, Questionnaire questionnaire, DynamicAttribute dynamicAttribute, List<String> results, User user) throws TalentStudioException {
        if (dynamicAttribute.getReferences().size() > 0) {

            // we have a sum or an enum mapping
            final DynamicAttributeReference dynamicAttributeReference = dynamicAttribute.getReferences().get(0);
            DynamicAttribute root = dynamicAttributeReference.getParentDa();
            final Long queId = questionnaire.getId();
            String answer = QuestionCalculator.calculate(root, questionnaireService, queId, user);
            if (answer != null && StringUtils.hasText(answer)) {
                results.add(answer);
            }
            testResult(questionnaireService, questionnaire, root, results, user);
        }
    }

    private IUserService userService;
    private static final Long WORKFLOW_ID = new Long(1);
}
