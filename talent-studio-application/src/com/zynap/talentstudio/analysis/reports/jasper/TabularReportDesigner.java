/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports.jasper;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignSubreport;
import net.sf.jasperreports.engine.design.JRDesignSubreportParameter;
import net.sf.jasperreports.engine.design.JRDesignTextField;

import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.IReportDao;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportConstants;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 18-Jan-2006 10:12:57
 */
public class TabularReportDesigner extends ReportDesigner {

    public JasperReportDesign getDesign(Report report, IReportDao reportDao) throws TalentStudioException {
        this.reportDao = reportDao;
        try {
            return getDesign(report, new HashMap(), null, null, null);
        } catch (TalentStudioException e) {
            logger.error("Unable to compile jasper design due to : " + e.getMessage(), e);
            throw new TalentStudioException(e);
        }
    }


    /**
     * Add link to legend on column header if appropriate.
     *
     * @param column
     * @param headerText
     */
    protected void createHeaderLinkIfRequired(Column column, JRDesignTextField headerText) {
        if (column.isColorDisplayable()) {

            // create detailed information onto the textFieldElement
            headerText.setHyperlinkType(JRHyperlink.HYPERLINK_TYPE_LOCAL_ANCHOR);
            JRDesignExpression expression = new JRDesignExpression();
            String legendLink = "\"javascript:submitLegendForm(" + column.getId() + ")\"";
            expression.setText(legendLink);
            expression.setValueClass(String.class);
            headerText.setAnchorNameExpression(expression);
        }
    }

    private TabularReportDesign getDesign(Report report, Map subReports, TabularReportDesign parent, List subReportColumns, String subReportPrefix) throws TalentStudioException {

        TabularReportDesign tabularReportDesign = new TabularReportDesign(parent, subReports);

        try {
            designTabularReport(tabularReportDesign, report, subReportColumns, subReports, subReportPrefix);
            tabularReportDesign.jasperReport = JasperCompileManager.compileReport(tabularReportDesign.jasperDesign);
        } catch (JRException e) {
            logger.error("Unable to compile jasper design due to : " + e.getMessage(), e);
            throw new TalentStudioException(e);
        }
        return tabularReportDesign;

    }

    private void designTabularReport(TabularReportDesign tabularReportDesign, Report report, List subReportColumns, Map subReportsDesign, String subReportPref) throws TalentStudioException, JRException {
        boolean isSubReport = subReportColumns != null;
        tabularReportDesign.subReportPrefix = (subReportPref == null) ? "" : subReportPref;
        loadDesign(tabularReportDesign, !isSubReport ? TABULAR_TEMPLATE : SUB_TABULAR_TEMPLATE);

        if (!isSubReport) {
            // add parameter for decimal places
            final JRDesignParameter parameter = createParameter(ReportConstants.DECIMAL_PLACES_PARAM, Integer.class.getName());
            tabularReportDesign.jasperDesign.addParameter(parameter);
        }

        Map stylesMap = tabularReportDesign.jasperDesign.getStylesMap();
        String currentPrefix = null;

        JRStyle headerStyle = (JRStyle) stylesMap.get(HEADER_STYLE);
        JRStyle detailsStyle = (JRStyle) stylesMap.get(DETAIL_STYLE);
        JRStyle groupStyle = (JRStyle) stylesMap.get(GROUP_STYLE);

        List columns = subReportColumns;

        if (columns == null) columns = report.getColumns();

        int x = 0;
        int headerY = 0;
        int detailsY = 20;
        if (isSubReport) {
            detailsY = 0;
        }

        JRDesignBand pageHeader = (JRDesignBand) tabularReportDesign.jasperDesign.getPageHeader();
        tabularReportDesign.detail = (JRDesignBand) tabularReportDesign.jasperDesign.getDetail();

        // set band height for the details
        tabularReportDesign.detail.setHeight(columns.size() > 1 ? columns.size() * 20 : 40);
        // jasperDesignObject needs to have the page height set dynamically as the currently defined height only works for 41 columns
        tabularReportDesign.jasperDesign.setPageHeight((columns.size() * 20) + 100);
        width = tabularReportDesign.jasperDesign.getColumnWidth();

        // set page width and column counts
        tabularReportDesign.jasperDesign.setPageWidth(width * report.getColumns().size());
        tabularReportDesign.jasperDesign.setColumnCount(columns.size());

        List subColumns = null;
        for (int i = 0; i < columns.size(); i++) {
            Column column = (Column) columns.get(i);
            if (!isSubReport) {
                // add column header
                addHeader(headerStyle, x, headerY, column, pageHeader);
            }

            // create a sub report for each associated collection being used
            String collectionPrefix = column.getCollectionPrefix();            
            if (collectionPrefix != null && !collectionPrefix.equals(currentPrefix)) {
                if (column.isGrouped()) {
                    addGroup(column, tabularReportDesign, (i == 0 ? groupStyle : detailsStyle), x, detailsY);
                } else {
                    currentPrefix = collectionPrefix;
                    subColumns = AnalysisAttributeHelper.findSubColumns(columns, collectionPrefix);
                    String subReportParam = "subReport" + tabularReportDesign.subReportPrefix + collectionPrefix;
                    addSubReport(tabularReportDesign, collectionPrefix, subReportParam, detailsStyle, x, detailsY);
                    TabularReportDesign subTabularReportDesign = getDesign(report, subReportsDesign, tabularReportDesign, subColumns, subReportParam);
                    subReportsDesign.put(subReportParam, subTabularReportDesign);
                }
            } else {

                // if there are no sub columns or the current column is not also a sub column
                if (subColumns == null || !subColumns.contains(column)) {
                    if (!isSubReport && column.isGrouped()) {
                        // only add grouping if not a sub report
                        addGroup(column, tabularReportDesign, (i == 0 ? groupStyle : detailsStyle), x, detailsY);
                    } else if (column.isFormula()) {
                        addFormula(column, tabularReportDesign, detailsStyle, x, detailsY);
                    } else {
                        addDetail(report, column, tabularReportDesign, detailsStyle, x, detailsY, isSubReport);
                    }
                }
            }
            x += width;
        }
    }

    /**
     * Adding design elements to the report design, creates field and the detail
     *
     * @param column
     * @param design
     * @param style
     * @param x
     * @param y
     * @throws net.sf.jasperreports.engine.JRException
     *
     */
    private void addDetail(Report report, Column column, JasperReportDesign design, JRStyle style, int x, int y, boolean isSubReport) throws JRException {
        JRDesignField field = createField(column, design.jasperDesign);

        JRDesignElement element;
        if (column.isColorDisplayable()) {
            element = createDetailImage(field, column.getPosition());
        } else {
            JRDesignTextField detailsText = createTextField(field);
            createDetailLinkIfRequired(report, column, design, detailsText, isSubReport);

            element = detailsText;
        }
        applyStyleElements(element, style, x, y, width, height);
        design.detail.addElement(element);
    }

    /**
     * Add hyper link on artefact if appropriate.
     *
     * @param column
     * @param jasperReportDesign
     * @param detailsText
     * @param isSubReport
     */
    private void createDetailLinkIfRequired(Report report, Column column, JasperReportDesign jasperReportDesign, JRDesignTextField detailsText, boolean isSubReport) {

        String attributeName = column.getAttributeName();
        final AnalysisParameter analysisParameter = column.getAnalysisParameter();
        boolean isQueAttribute = analysisParameter.isQuestionnaireAttribute() && analysisParameter.getRole() == null;
        // all progress report questions are questionnaire attributes except for the first one which is the row header and replesents the label  of the workflow
        boolean isProgressQueAttribute = report.isProgressReport() && !column.isProgressRowHeader();

        if (isQueAttribute && reportDao != null && !isSubReport) {            
            // do not provide links to appraisals
            isQueAttribute = !reportDao.isAppraisal(analysisParameter.getQuestionnaireWorkflowId());
        }

        // cannot provide links to performance reviews or must be by questionnaireId!        
        if (AnalysisAttributeHelper.supportsLink(attributeName) || isQueAttribute || isProgressQueAttribute) {

            String idFieldName = FIELD_ID_PARAM;
            String workflowIdFieldName = FIELD_QUE_WF_ID_PARAM;
            String hasAccessFieldName = FIELD_HASACCESS_PARAM;

            // if sub report truncate the attribute name - ie: remove the last bit so if you have "subject.coreDetail.name" you get "subject.id"
            if (isSubReport) {
                final int pos = attributeName.indexOf(AnalysisAttributeHelper.DELIMITER);
                if (pos != -1) {
                    String idPrefix = attributeName.substring(0, pos + 1);
                    idFieldName = idPrefix + FIELD_ID_PARAM;
                    hasAccessFieldName = idPrefix + hasAccessFieldName;
                }
                try {
                    addField(hasAccessFieldName, jasperReportDesign.jasperDesign, String.class.getName());
                } catch (JRException e) {
                    e.printStackTrace();
                }
            }

            try {
                addField(idFieldName, jasperReportDesign.jasperDesign, String.class.getName());
                if (isProgressQueAttribute) {
                    addField(workflowIdFieldName, jasperReportDesign.jasperDesign, String.class.getName());
                }
            } catch (JRException e) {
                e.printStackTrace();
            }


            detailsText.setHyperlinkType(JRHyperlink.HYPERLINK_TYPE_REFERENCE);
            JRDesignExpression expression = new JRDesignExpression();
            String expText = "\"" + urlParameter(column, isProgressQueAttribute)
                    + "&" + NODE_ID_PARAM + "=\"" + " + $F{" + idFieldName + "} ";
            if (isQueAttribute && !isSubReport) {
                expText = expText + " + \"&" + QUESTIONNAIRE_WF_ID_PARAM + "=" + analysisParameter.getQuestionnaireWorkflowId() + "\"";
            }

            if (isProgressQueAttribute && !isSubReport) {
                expText = expText + " + \"&" + QUESTIONNAIRE_WF_ID_PARAM + "=\"" + " +$F{" + workflowIdFieldName + "} ";
            }

            if (isSubReport)
                expText = expText + " + \"&" + FIELD_HASACCESS_PARAM + "=\"" + " + $F{" + hasAccessFieldName + "}";
            
            expression.setText(expText);
            expression.setValueClassName(String.class.getName());
            detailsText.setHyperlinkReferenceExpression(expression);
        }
    }

    private String urlParameter(Column column, boolean isQueUrl) {
        if (column.getAnalysisParameter().isQuestionnaireAttribute() || isQueUrl) {
            return _QUESTIONNAIRE_URL_PARAM_NAME;
        }
        return column.getAnalysisParameter().isPersonLinkAttribute() ? _SUBJECT_URL_PARAM_NAME : _POSITION_URL_PARAM_NAME;
    }

    /**
     * Add sub report.
     * <br/> Works by adding an element to the design that will create a AssociatedArtefactDataSource for the sub report.
     * <br/> It gets the list of associated nodes by adding a field with the collectionPrefix and a type of Collection.class
     * and then putting a reference to this field in the expression - see getSubReportDataSourceExpression().
     *
     * @param tabularReportDesign
     * @param collectionPrefix
     * @param paramName
     * @param detailsStyle
     * @param x
     * @param y
     * @throws JRException
     */
    private void addSubReport(TabularReportDesign tabularReportDesign, String collectionPrefix, String paramName, JRStyle detailsStyle, int x, int y) throws JRException {

        addField(collectionPrefix, tabularReportDesign.jasperDesign, Collection.class.getName());

        JRDesignSubreport jrDesignSubreport = new JRDesignSubreport(null);
        applyStyleElements(jrDesignSubreport, detailsStyle, x, y, width, height);

        // add sub report parameters to be passed through
        jrDesignSubreport.addParameter(createSubReportParameter(ReportConstants.REPORT_PARAM, Report.class));
        jrDesignSubreport.addParameter(createSubReportParameter(ReportConstants._DS_FACTORY_PARAM, JasperDataSourceFactory.class));
        jrDesignSubreport.addParameter(createSubReportParameter(ReportConstants._USER_PARAM, User.class));

        JRDesignExpression dataSourceExpression = new JRDesignExpression();
        dataSourceExpression.setText(getSubReportDataSourceExpression(collectionPrefix));
        dataSourceExpression.setValueClass(JRDataSource.class);
        jrDesignSubreport.setDataSourceExpression(dataSourceExpression);

        JRDesignExpression expression = new JRDesignExpression();
        expression.setValueClass(JasperReport.class);
        expression.setText("$P{" + paramName + "}");
        jrDesignSubreport.setExpression(expression);
        jrDesignSubreport.setKey(paramName);

        tabularReportDesign.detail.addElement(jrDesignSubreport);

        addSubReportParameter(tabularReportDesign, tabularReportDesign.subReportPrefix, paramName, true);
    }

    private String getSubReportDataSourceExpression(String collectionPrefix) {
        if(AnalysisAttributeHelper.isDocumentAttribute(collectionPrefix)) {
            return "($P{" + ReportConstants._DS_FACTORY_PARAM + "}).getDocumentDataSource($P{" + ReportConstants.REPORT_PARAM + "}, $F{" + collectionPrefix + "}, $P{" + ReportConstants._USER_PARAM + "})";
        }
        return "($P{" + ReportConstants._DS_FACTORY_PARAM + "}).getAssociatedArtefactDataSource($P{" + ReportConstants.REPORT_PARAM + "}, $F{" + collectionPrefix + "}, $P{" + ReportConstants._USER_PARAM + "})";
    }

    private void addSubReportParameter(TabularReportDesign tabularReportDesign, String key, String paramName, boolean first) throws JRException {

        JRDesignParameter subReportParameter = createParameter(paramName, JasperReport.class.getName());
        Map parametersMap = tabularReportDesign.jasperDesign.getParametersMap();
        if (!parametersMap.containsKey(subReportParameter.getName())) {
            tabularReportDesign.jasperDesign.addParameter(subReportParameter);
        }
        if (!first) {
            JRDesignSubreport jrDesignSubreport = (JRDesignSubreport) tabularReportDesign.detail.getElementByKey(key);
            parametersMap = jrDesignSubreport.getParametersMap();
            JRDesignSubreportParameter subreportParameter = createSubReportParameter(paramName, JasperReport.class);
            if (!parametersMap.containsKey(subreportParameter.getName())) {
                jrDesignSubreport.addParameter(subreportParameter);
            }
        }
        if (tabularReportDesign.parent != null) {
            addSubReportParameter(tabularReportDesign.parent, tabularReportDesign.subReportPrefix, paramName, false);
        }
    }

    private JRDesignSubreportParameter createSubReportParameter(final String reportParameterKey, final Class clazz) {
        // add the subreports parameters must be the url values and the report
        JRDesignSubreportParameter reportParameter = new JRDesignSubreportParameter();
        reportParameter.setName(reportParameterKey);
        JRDesignExpression parameterExpression = new JRDesignExpression();
        parameterExpression.setValueClass(clazz);
        parameterExpression.setText("$P{" + reportParameterKey + "}");
        reportParameter.setExpression(parameterExpression);
        return reportParameter;
    }

    /**
     * Paths to template files.
     */
    private static final String TABULAR_TEMPLATE = TEMPLATE_PATH + "tabular_template.jrxml";
    private static final String SUB_TABULAR_TEMPLATE = TEMPLATE_PATH + "sub_tabular_template.jrxml";

    private IReportDao reportDao;
}
