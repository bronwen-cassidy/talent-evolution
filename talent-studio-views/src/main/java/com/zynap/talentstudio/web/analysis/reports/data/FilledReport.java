/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.data;

import net.sf.jasperreports.engine.JasperPrint;

import java.io.Serializable;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public abstract class FilledReport implements Serializable {

    protected FilledReport(JasperPrint jasperPrint) {
        this.jasperPrint = jasperPrint;
    }

    public JasperPrint getJasperPrint() {
        return jasperPrint;
    }

    public boolean isHasResults() {
        final List pages = jasperPrint.getPages();
        return pages != null && !pages.isEmpty();
    }

    public boolean isSpiderChart() {
        return false;
    }

    public boolean isPieChart() {
        return false;
    }
    
    public boolean isSeriesChart() {
    	return false;
    }

    public boolean isBarChart() {
        return false;
    }

    private final JasperPrint jasperPrint;
}
