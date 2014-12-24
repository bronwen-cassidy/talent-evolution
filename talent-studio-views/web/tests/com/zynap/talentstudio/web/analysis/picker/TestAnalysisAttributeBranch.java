package com.zynap.talentstudio.web.analysis.picker;

import com.zynap.talentstudio.ZynapTestCase;
import com.zynap.talentstudio.organisation.Node;

/**
 * User: amark
 * Date: 05-May-2006
 * Time: 14:17:53
 */
public class TestAnalysisAttributeBranch extends ZynapTestCase {

    public void testGetPrefix() throws Exception {

        AnalysisAttributeBranch analysisAttributeBranch = new AnalysisAttributeBranch("id", "label", Node.POSITION_UNIT_TYPE_, false, false, "id");
        assertEquals(analysisAttributeBranch.getId(), analysisAttributeBranch.getPrefix());
    }

    public void testGetRootPrefix() throws Exception {

        AnalysisAttributeBranch analysisAttributeBranch = new AnalysisAttributeBranch("id", "label", Node.POSITION_UNIT_TYPE_, true, false, "");
        assertTrue(analysisAttributeBranch.getPrefix().length() == 0);
    }


}


