/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.jasper;

import net.sf.jasperreports.engine.export.JRCsvExporter;

import org.springframework.util.StringUtils;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 16-May-2008 14:00:45
 */
public class JasperCsvExporter extends JRCsvExporter {

    protected String prepareText(String source) {

        if(StringUtils.hasText(source) && source.startsWith("-")) {
            source = " " + source;
        }
        
        return super.prepareText(source);
    }
}
