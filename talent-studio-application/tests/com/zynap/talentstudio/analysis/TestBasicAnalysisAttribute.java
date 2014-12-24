package com.zynap.talentstudio.analysis;

/**
 * User: amark
 * Date: 15-Aug-2006
 * Time: 15:02:02
 */

import com.zynap.talentstudio.ZynapTestCase;
import com.zynap.talentstudio.analysis.reports.Column;

public class TestBasicAnalysisAttribute extends ZynapTestCase {

    public void testSetAnalysisParameter() throws Exception {

        BasicAnalysisAttribute analysisAttribute = new Column();
        AnalysisParameter analysisParameter = new AnalysisParameter(NAME, WF_ID, null);
        analysisAttribute.setAnalysisParameter(analysisParameter);
        assertEquals(NAME, analysisParameter.getName());
        assertEquals(WF_ID, analysisParameter.getQuestionnaireWorkflowId());
    }

    public void testGetNullAttribute() throws Exception {

        BasicAnalysisAttribute analysisAttribute = new Column();
        final AnalysisParameter analysisParameter = analysisAttribute.getAnalysisParameter();
        assertNull(analysisParameter);
    }

    public void testGetAnalysisParameter() throws Exception {

        BasicAnalysisAttribute analysisAttribute = new Column();
        analysisAttribute.setAttributeName(NAME);
        analysisAttribute.setQuestionnaireWorkflowId(WF_ID);
        final AnalysisParameter analysisParameter = analysisAttribute.getAnalysisParameter();
        assertNotNull(analysisParameter);
        assertEquals(NAME, analysisParameter.getName());
        assertEquals(WF_ID, analysisParameter.getQuestionnaireWorkflowId());
    }

    private static final String NAME = "test";
    private static final Long WF_ID = new Long(Long.MIN_VALUE);
}