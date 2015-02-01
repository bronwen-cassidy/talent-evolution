/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 09-Jul-2008 10:48:42
 * @version 0.1
 */

import com.zynap.talentstudio.ZynapDatabaseTestCase;

public class DBUnitTestQuestionnaireDwrBean extends ZynapDatabaseTestCase {

    protected String getDataSetFileName() {
        return "test-some-file.xml";
    }

    protected void setUp() throws Exception {
        super.setUp();
        questionnaireDwrBean = (QuestionnaireDwrBean) getBean("questionnaireDwrBean");
    }

    public void testGetRepublishResults() throws Exception {
        fail("Test is not implemented");
    }

    private QuestionnaireDwrBean questionnaireDwrBean;
}