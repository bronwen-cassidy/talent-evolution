/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.jasper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;

import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.analysis.reports.jasper.ReportDesigner;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 17-May-2006 09:40:47
 */
public class DefaultJasperHtmlExporter extends JRHtmlExporter {

    public DefaultJasperHtmlExporter(String subjectUrl, String positionUrl, String drillDownUrl, String questionnaireUrl, Report report) {
        super();
        this.subjectUrl = subjectUrl;
        this.positionUrl = positionUrl;
        this.drillDownUrl = drillDownUrl;
        this.questionnaireUrl = questionnaireUrl;
        this.report = report;
        setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
        setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
        setParameter(JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, Boolean.FALSE);
    }

    protected void exportReportToWriter() throws JRException, IOException {
        for (reportIndex = 0; reportIndex < jasperPrintList.size(); reportIndex++) {
            jasperPrint = (JasperPrint) jasperPrintList.get(reportIndex);

            List pages = jasperPrint.getPages();
            if (pages != null && pages.size() > 0) {
                JRPrintPage page;
                for (pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++) {
                    page = (JRPrintPage) pages.get(pageIndex);
                    exportPage(page);
                }
            }
        }
        writer.flush();
    }

    protected String formatLink(JRPrintText hyperLinkText) {
        String href = null;

        final String text = hyperLinkText.getText();

        // ignore blank links
        if (StringUtils.isNotBlank(text)) {

            switch (hyperLinkText.getHyperlinkType()) {
                case JRHyperlink.HYPERLINK_TYPE_REFERENCE:
                    href = hyperLinkText.getHyperlinkReference();
                    break;

                case JRHyperlink.HYPERLINK_TYPE_LOCAL_ANCHOR:
                    href = "#" + hyperLinkText.getHyperlinkAnchor();
                    break;

                case JRHyperlink.HYPERLINK_TYPE_LOCAL_PAGE:
                    break;

                case JRHyperlink.HYPERLINK_TYPE_REMOTE_ANCHOR:
                    href = hyperLinkText.getHyperlinkReference() + "#" + hyperLinkText.getHyperlinkAnchor();
                    break;

                case JRHyperlink.HYPERLINK_TYPE_REMOTE_PAGE:
                    break;
                default :
                    break;
            }

            if(ReportConstants.NO_VALUE.equals(text)) return null;
            
            if((StringUtils.isNotBlank(href)) && (href.indexOf(ReportDesigner._QUESTIONNAIRE_URL_PARAM_NAME) != -1)) {
                return checkAccess(href);
            } else if (Report.hasValue(text)) {
                return checkAccess(href);
            }
        }

        return href;
    }

    protected final String checkAccess(String href) {

        if (StringUtils.isNotEmpty(href)) {
            if (href.indexOf("hasAccess=false") > -1) return null;

            if (StringUtils.isBlank(drillDownUrl) && href.indexOf(ReportDesigner._DRILL_DOWN_URL_PARAM_NAME) != -1) return null;
            if (StringUtils.isBlank(positionUrl) && href.indexOf(ReportDesigner._POSITION_URL_PARAM_NAME) != -1) return null;
            if (StringUtils.isBlank(subjectUrl) && href.indexOf(ReportDesigner._SUBJECT_URL_PARAM_NAME) != -1) return null;
            if (StringUtils.isBlank(questionnaireUrl) && href.indexOf(ReportDesigner._QUESTIONNAIRE_URL_PARAM_NAME) != -1) return null;

            if (StringUtils.isNotBlank(positionUrl)) href = StringUtils.replace(href, ReportDesigner._POSITION_URL_PARAM_NAME, positionUrl);
            if (StringUtils.isNotBlank(subjectUrl)) href = StringUtils.replace(href, ReportDesigner._SUBJECT_URL_PARAM_NAME, subjectUrl);
            if (StringUtils.isNotBlank(questionnaireUrl)) href = StringUtils.replace(href, ReportDesigner._QUESTIONNAIRE_URL_PARAM_NAME, questionnaireUrl);
            if (StringUtils.isNotBlank(drillDownUrl)) href = StringUtils.replace(href, ReportDesigner._DRILL_DOWN_URL_PARAM_NAME, drillDownUrl);
        }

        return href;
    }

    protected String subjectUrl;
    protected String positionUrl;
    protected String drillDownUrl;
    protected String questionnaireUrl;
    protected Report report;
}
