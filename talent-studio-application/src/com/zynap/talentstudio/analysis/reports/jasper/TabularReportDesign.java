package com.zynap.talentstudio.analysis.reports.jasper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 27-Jan-2006
 * Time: 09:30:21
 * To change this template use File | Settings | File Templates.
 */
public class TabularReportDesign extends JasperReportDesign {

    TabularReportDesign(TabularReportDesign parent, Map subReports) {
        this.parent = parent;
        this.subReportsDesign = subReports;
    }

    public Map<Object, Object> getParameters() {
        HashMap<Object, Object> parameters = new HashMap<Object, Object>();
        Set entries = subReportsDesign.entrySet();
        for (Iterator iterator = entries.iterator(); iterator.hasNext();) {
            Map.Entry ent = (Map.Entry) iterator.next();
            parameters.put(ent.getKey(), ((TabularReportDesign) ent.getValue()).getJasperReport());
        }
        return parameters;
    }


    /**
     * Contains the the jasperDesign and jasperReport objects.
     *
     * @serial
     */
    protected Map subReportsDesign;

    protected transient TabularReportDesign parent;

    protected transient String subReportPrefix;

    private static final long serialVersionUID = -12736935476657688L;
}
