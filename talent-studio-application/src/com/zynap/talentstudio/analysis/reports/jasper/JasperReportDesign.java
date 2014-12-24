package com.zynap.talentstudio.analysis.reports.jasper;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JasperDesign;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 27-Jan-2006
 * Time: 12:09:33
 * To change this template use File | Settings | File Templates.
 */
public abstract class JasperReportDesign implements Serializable {

    public JasperReport getJasperReport() {
        return jasperReport;
    }

    public abstract Map getParameters();

    protected JasperReport jasperReport;
    protected transient JasperDesign jasperDesign;
    protected transient JRDesignBand detail;

    private static final long serialVersionUID = -3486037943426788091L;    
}
