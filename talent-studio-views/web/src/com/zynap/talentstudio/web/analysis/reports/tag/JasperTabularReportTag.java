/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.tag;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;

import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.web.analysis.reports.jasper.CrossTabHtmlExporter;
import com.zynap.talentstudio.web.analysis.reports.jasper.JasperHtmlExporter;
import com.zynap.web.tag.ZynapTagSupport;

import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.jsp.JspException;

import java.io.StringWriter;
import java.util.Collection;

/**
 * Tag library that displays the contents of a report as HTML using Jasper.
 *
 * @author Angus Mark
 */
public class JasperTabularReportTag extends ZynapTagSupport {


    public void release() {
        super.release();
        this.jasperPrint = null;
        this.viewPositionUrl = null;
        this.viewQuestionnaireUrl = null;
        this.viewSubjectUrl = null;
        this.drillDownUrl = null;
        this.drillDownAltText = null;
        this.report = null;
        this.rowHeadings = null;
        this.columnHeadings = null;
        this.forCrossTab = false;
    }

    public void setJasperPrint(Object jasperPrint) throws JspException {
        if (ExpressionEvaluationUtils.isExpressionLanguage(jasperPrint.toString())) {
            this.jasperPrint = (JasperPrint) ExpressionEvaluationUtils.evaluate(jasperPrint.toString(), jasperPrint.toString(), Object.class, pageContext);
        } else {
            this.jasperPrint = (JasperPrint) jasperPrint;
        }
    }

    public void setReport(Object report) throws JspException {
        if (ExpressionEvaluationUtils.isExpressionLanguage(report.toString())) {
            this.report = (Report) ExpressionEvaluationUtils.evaluate(report.toString(), report.toString(), Object.class, pageContext);
        } else {
            this.report = (Report) report;
        }
    }

    public void setColumnHeadings(Object columnHeadings) throws JspException {
        if (ExpressionEvaluationUtils.isExpressionLanguage(columnHeadings.toString())) {
            this.columnHeadings = (Collection) ExpressionEvaluationUtils.evaluate(columnHeadings.toString(), columnHeadings.toString(), Object.class, pageContext);
        } else {
            this.columnHeadings = (Collection) columnHeadings;
        }

    }

    public void setRowHeadings(Object rowHeadings) throws JspException {
        if (ExpressionEvaluationUtils.isExpressionLanguage(rowHeadings.toString())) {
            this.rowHeadings = (Collection) ExpressionEvaluationUtils.evaluate(rowHeadings.toString(), rowHeadings.toString(), Object.class, pageContext);
        } else {
            this.rowHeadings = (Collection) rowHeadings;
        }
    }

    public void setViewSubjectUrl(String viewSubjectUrl) throws JspException {
        if (ExpressionEvaluationUtils.isExpressionLanguage(viewSubjectUrl.toString()))
            this.viewSubjectUrl = ExpressionEvaluationUtils.evaluateString("viewSubjectUrl", viewSubjectUrl, pageContext);
        else
            this.viewSubjectUrl = viewSubjectUrl;
    }

    public void setViewPositionUrl(String viewPositionUrl) throws JspException {
        if (ExpressionEvaluationUtils.isExpressionLanguage(viewPositionUrl.toString()))
            this.viewPositionUrl = ExpressionEvaluationUtils.evaluateString("viewPositionUrl", viewPositionUrl, pageContext);
        else
            this.viewPositionUrl = viewPositionUrl;
    }

    public void setViewQuestionnaireUrl(String viewQuestionnaireUrl) throws JspException {
        if (ExpressionEvaluationUtils.isExpressionLanguage(viewQuestionnaireUrl.toString()))
            this.viewQuestionnaireUrl = ExpressionEvaluationUtils.evaluateString("viewQuestionnaireUrl", viewQuestionnaireUrl, pageContext);
        else
            this.viewQuestionnaireUrl = viewQuestionnaireUrl;
    }

    public void setDrillDownUrl(String drillDownUrl) throws JspException {
        if (ExpressionEvaluationUtils.isExpressionLanguage(drillDownUrl.toString()))
            this.drillDownUrl = ExpressionEvaluationUtils.evaluateString("drillDownUrl", drillDownUrl, pageContext);
        else
            this.drillDownUrl = drillDownUrl;
    }

    public void setDrillDownAltText(String drillDownAltText) throws JspException {
        if (ExpressionEvaluationUtils.isExpressionLanguage(drillDownAltText.toString()))
            this.drillDownAltText = ExpressionEvaluationUtils.evaluateString("drillDownAltText", drillDownAltText, pageContext);
        else
            this.drillDownAltText = drillDownAltText;
    }

    public void setForCrossTab(boolean forCrossTab) {
        this.forCrossTab = forCrossTab;
    }

    protected int doInternalStartTag() throws Exception {

        final StringWriter stringWriter = new StringWriter();

        final JRHtmlExporter exporter;
        if (forCrossTab) {
            exporter = new CrossTabHtmlExporter(viewSubjectUrl, viewPositionUrl, drillDownUrl, viewQuestionnaireUrl, drillDownAltText, report, columnHeadings, rowHeadings);
        } else {
            exporter = new JasperHtmlExporter(viewSubjectUrl, viewPositionUrl, drillDownUrl, viewQuestionnaireUrl, report);
        }
        if(report.isProgressReport()) {
            exporter.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);            
        }
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, stringWriter);
        exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "");
        exporter.exportReport();

        pageContext.getOut().print(stringWriter.toString());

        return EVAL_PAGE;
    }

    private JasperPrint jasperPrint;
    private String viewSubjectUrl;
    private String viewPositionUrl;
    private String viewQuestionnaireUrl;
    private String drillDownUrl;
    private String drillDownAltText;
    private Report report;
    private Collection columnHeadings;
    private Collection rowHeadings;
    private boolean forCrossTab = false;

}
