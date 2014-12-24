/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 25-Sep-2009 18:00:34
 * @version 0.1
 */

import com.zynap.talentstudio.ZynapDatabaseTestCase;
import com.zynap.talentstudio.analysis.reports.crosstab.Row;
import com.zynap.talentstudio.analysis.reports.crosstab.Cell;

import java.util.List;
import java.util.Map;

public class TestAppraisalReportBuilder extends ZynapDatabaseTestCase {

    protected String getDataSetFileName() {
        return "appraisal-test-data.xml";
    }

    protected void setUp() throws Exception {
        super.setUp();
        appraisalReportBuilder = (AppraisalReportBuilder) getBean("appraisalReportBuilder");
    }

    /* expected table of results

Question              |  Weighting  |  Internal Customer   |  Peer  |  Total Ave  |
-----------------------------------------------------------------------------------
Total                 |  18%        |  54.0                |  54.0  |  54.0       |
Pre Programme Total   |  12%        |  8.5                 |  9.0   |  8.75       |
Post Programme Total  |  25%        |  8.5                 |  7.5   |  8.0        |
Post Programme Total  |  35%        |  8.0                 |  7.5   |  7.75       |
Now Pre Total         |  10%        |  9.0                 |  9.0   |  9.0        |
Average               |  -          |  17.6                |  17.4  |  17.5       |
Weighted Average      |  -          |  16.564999999999998  |  16.2  |  16.3825    |

    * */
    public void testBuildResults() throws Exception {

        final Map<Report,List<Row>> map = appraisalReportBuilder.buildResults(TARGET_SUBJECT_ID);

        for (Map.Entry<Report, List<Row>> entry : map.entrySet()) {
            final List<Row> rows = entry.getValue();

            final Row weightedAverageRow = rows.get(7);
            final Cell internalCustomerCell = weightedAverageRow.getCell(2);
            assertEquals("16.56", internalCustomerCell.getDisplayValue());
            final Cell totalWeightedAv = weightedAverageRow.getCell(4);
            assertEquals("16.38", totalWeightedAv.getDisplayValue());
        }
    }

    public void testBuildResultsNoneCompleted() throws Exception {
        final Map<Report, List<Row>> results = appraisalReportBuilder.buildResults(INCOMPLETE_SUBJECT_ID);
        assertTrue(results.isEmpty());
    }

    private AppraisalReportBuilder appraisalReportBuilder;
    private static final Long TARGET_SUBJECT_ID = new Long(2950);
    private static final Long INCOMPLETE_SUBJECT_ID = new Long(3051);
}