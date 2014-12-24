package com.zynap.talentstudio.web.analysis.reports.data;

import java.util.Collection;

/**
 * User: amark
 * Date: 15-May-2006
 * Time: 14:58:28
 */
public class CrossTabFilledReport extends FilledReport {

    public CrossTabFilledReport(Collection discreetRows, Collection percentRows) {
        super(null);
        this.discreetRows = discreetRows;
        this.percentRows = percentRows;
    }

    public Collection getRows(boolean percent) {
        return (percent) ? percentRows : discreetRows;
    }

    public Collection getDiscreetRows() {
        return discreetRows;
    }

    /**
     * The cells with the discreet values.
     */
    private final Collection discreetRows;

    /**
     * The cells with the percent values.
     */
    private final Collection percentRows;
}
