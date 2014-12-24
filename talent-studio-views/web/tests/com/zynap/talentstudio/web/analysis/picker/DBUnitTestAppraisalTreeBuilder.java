/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.picker;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 05-Oct-2009 09:47:57
 * @version 0.1
 */

import com.zynap.talentstudio.ZynapDatabaseTestCase;
import com.zynap.talentstudio.questionnaires.IQueDefinitionService;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;

import java.util.Arrays;

public class DBUnitTestAppraisalTreeBuilder extends ZynapDatabaseTestCase {

    protected String getDataSetFileName() {
        return "test-data.xml";
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testBuildQuestionnaireBranch() throws Exception {
        IQueDefinitionService definitionService = (IQueDefinitionService) getBean("questionnaireDefinitionService");
        QuestionnaireDefinition definition = definitionService.findDefinition(new Long(1));
        AnalysisAttributeBranch root = new AnalysisAttributeBranch("2", "Q1 2006", "");
        QueDefinitionTreeBuilder builder = new QueDefinitionTreeBuilder();
        builder.setAttributeTypes(Arrays.asList(DynamicAttribute.DA_TYPE_NUMBER, DynamicAttribute.DA_TYPE_SUM));
        builder.buildQuestionnaireBranch(root, definition, null);
        assertEquals(2, root.getLeaves().size());

    }
}