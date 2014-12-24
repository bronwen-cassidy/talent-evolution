/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.views;

import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRPropertiesMap;

import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.BasicAnalysisAttribute;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 22-Mar-2011 07:59:54
 */
public class FieldValue implements JRField {

    public FieldValue(BasicAnalysisAttribute analysisAttribute) {
        this.analysisAttribute = analysisAttribute;
    }

    public String getName() {
        return AnalysisAttributeHelper.getName(analysisAttribute.getAnalysisParameter());
    }

    public String getDescription() {
        return null;
    }

    public void setDescription(String description) {

    }

    public Class getValueClass() {
        return Number.class;
    }

    public String getValueClassName() {
        return Number.class.getName();
    }

    public JRPropertiesMap getPropertiesMap() {
        return null;
    }

    private BasicAnalysisAttribute analysisAttribute;
}
