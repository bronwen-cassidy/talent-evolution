/* Copyright: Copyright (c) 2004
 * Company:
 */
package com.zynap.talentstudio.web.display.tag;

import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.TabularReport;
import com.zynap.talentstudio.analysis.reports.crosstab.Cell;
import com.zynap.talentstudio.analysis.reports.crosstab.Row;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;

import com.zynap.talentstudio.web.utils.HtmlUtils;

import com.zynap.talentstudio.web.analysis.populations.PopulationUtils;
import com.zynap.talentstudio.web.organisation.associations.ArtefactAssociationWrapperBean;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.display.support.ArtefactViewModelBuilder;

import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Tag library that displays the contents of a tabular report.
 *
 * @author Angus Mark
 */
public final class AssociationViewTag extends AbstractViewTag {

    public void setAssociations(Object associations) throws JspException {
        List<ArtefactAssociationWrapperBean> list;
        if (ExpressionEvaluationUtils.isExpressionLanguage(associations.toString())) {
            list = (List<ArtefactAssociationWrapperBean>) ExpressionEvaluationUtils.evaluate(associations.toString(), associations.toString(), Object.class, pageContext);
        } else {
            list = (List<ArtefactAssociationWrapperBean>) associations;
        }

        if (list == null)
            this.associations = new ArrayList<ArtefactAssociationWrapperBean>();
        else {
            Collections.sort(list, new Comparator<ArtefactAssociationWrapperBean>() {
                public int compare(ArtefactAssociationWrapperBean o1, ArtefactAssociationWrapperBean o2) {
                    return o1.getQualifier().compareBySortOrder(o2.getQualifier());
                }
            });
            this.associations = list;
        }
    }

    public void setReport(Object report) throws JspException {
        if (ExpressionEvaluationUtils.isExpressionLanguage(report.toString())) {
            this.report = (Report) ExpressionEvaluationUtils.evaluate(report.toString(), report.toString(), Object.class, pageContext);
        } else {
            this.report = (Report) report;
        }
    }

    public void setInverse(String inverse) throws JspException {
        if (ExpressionEvaluationUtils.isExpressionLanguage(inverse)) {
            this.inverse = ExpressionEvaluationUtils.evaluateBoolean("inverse", inverse, pageContext);
        } else {
            this.inverse = Boolean.TRUE.toString().equals(inverse);
        }
    }

    protected int doStartTagInternal() throws Exception {

        // get answers
        Map answers = new HashMap();
        final List<AnalysisParameter> questionnaireAttributes = report.getQuestionnaireAttributes();
        if (!questionnaireAttributes.isEmpty()) {
            Long userId = ZynapWebUtils.getUserId((HttpServletRequest) pageContext.getRequest());
            answers = displayHelper.getAssociationQuestionnaireAnswers(questionnaireAttributes, associations, userId, report.getReportType());
        }

        // build rows and then build display
        final Collection rows = buildRows(report, answers, associations, inverse);
        final StringBuffer output = new StringBuffer();
        if (rows != null && !rows.isEmpty()) {
            output.append(displayColumnHeadings(report.getColumns()));
            output.append(displayRows(rows));
        }

        try {
            pageContext.getOut().println(output.toString());
        } catch (IOException e) {
            throw new JspException(e);
        }

        return EVAL_PAGE;
    }

    Collection buildRows(Report report, Map answers, List<ArtefactAssociationWrapperBean> associations, boolean inverse) {

        final List<Row> rows = new ArrayList<Row>();
        final List<Column> columns = report.getColumns();

        if (columns != null && !columns.isEmpty()) {

            // use copy of columns as we remove the association qualifier column
            final List<Column> copiedColumns = new ArrayList<Column>(columns);

            // find the association qualifier column if present and remove (if present value returned will be greater than -1)
            Map<Integer, Column> associationColumns = findAndRemoveAssociationQualifierColumn(copiedColumns);

            final boolean personPopulation = PopulationUtils.isPersonPopulation(report.getReportType());
            for (ArtefactAssociationWrapperBean artefactAssociation : associations) {

                final boolean subjectAssociation = artefactAssociation.isSubjectAssociation();
                final Node source = artefactAssociation.getSource();
                final Node target = artefactAssociation.getTarget();

                Node associatedNode;
                if (subjectAssociation) {

                    // if dealing with subject to position associations the target is the position and the source is the subject
                    // so if we are displaying people take the source (the subject), otherwise the target (the position)
                    if (personPopulation) {
                        associatedNode = source;
                    } else {
                        associatedNode = target;
                    }

                } else {

                    // if dealing with position to position associations we can choose either the target or the source
                    // the target is the parent and the source is the child
                    if (inverse) {
                        associatedNode = source;
                    } else {
                        associatedNode = target;
                    }
                }

                // only do the following if the node is active
                if (!associatedNode.isActive()) continue;

                // get cell for each report column
//                final Row row = new Row();
//                for (Column column : copiedColumns) {
//                    row.add(createCell(associatedNode, column, answers));
//                }
                final Report tempReport = new TabularReport();
                tempReport.setColumns(copiedColumns);
                final Row row = ArtefactViewModelBuilder.buildRow(tempReport, associatedNode, displayHelper, answers);

                // add a cell for the association attributes if present
                if (!associationColumns.isEmpty()) {
                    for (Map.Entry<Integer, Column> integerColumnEntry : associationColumns.entrySet()) {
                        final Column associationColumn = integerColumnEntry.getValue();
                        // get Postfix and use reflection
                        String associationAttribute = AnalysisAttributeHelper.removePrefix(associationColumn.getAttributeName());
                        String result = AnalysisAttributeHelper.evaluateProperty(associationAttribute, artefactAssociation);
                        final String label = associationColumn.getLabel();
                        DynamicAttribute mockAssociationAttribute = new DynamicAttribute(label, associationColumn.getColumnType());
                        Cell cell = Cell.create(mockAssociationAttribute, associatedNode, associatedNode, label, result, null);
                        row.add(integerColumnEntry.getKey(), cell);                        
                    }
                }
                rows.add(row);
            }
        }

        return rows;
    }

    /**
     * Find and remove association qualifier column if present.
     * <br/> Returns -1 if not found.
     *
     * @param columns
     * @return index in list of columns of association qualifier column.
     */
    private Map<Integer, Column> findAndRemoveAssociationQualifierColumn(final List<Column> columns) {

        //int position = -1;
        Map<Integer, Column> associationColumns = new HashMap<Integer, Column>();
        int i = 0;
        for (Iterator iterator = columns.iterator(); iterator.hasNext(); i++) {
            Column column = (Column) iterator.next();
            final String name = column.getAttributeName();
            if (AnalysisAttributeHelper.isQualifierAttribute(name)) {
                associationColumns.put(new Integer(i), column);
                iterator.remove();
            } else if (name.startsWith("subjectSecondaryAssociations") && (AnalysisAttributeHelper.splitAttributeNames(name).length < 3)) {
                associationColumns.put(new Integer(i), column);
                iterator.remove();
            }
        }

        return associationColumns;
    }

    private String displayColumnHeadings(List columns) {

        final StringBuffer output = new StringBuffer();
        output.append(displayPropertiesManager.getColumnsStart());

        final String columnFormat = displayPropertiesManager.getColumnFormat();
        for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
            Column column = (Column) iterator.next();
            final String label = HtmlUtils.htmlEscape(column.getLabel());
            output.append(MessageFormat.format(columnFormat, label));
        }

        output.append(displayPropertiesManager.getColumnsEnd());

        return output.toString();
    }

    private String displayRows(final Collection rows) {

        final StringBuffer output = new StringBuffer();
        output.append(displayPropertiesManager.getRowsStart());

        int pos = 1;
        for (Iterator iterator = rows.iterator(); iterator.hasNext(); pos++) {
            final Row row = (Row) iterator.next();
            output.append(displayRow(row, pos));
        }

        output.append(displayPropertiesManager.getRowsEnd());

        return output.toString();
    }

    private String displayRow(Row row, int pos) {

        final StringBuffer output = new StringBuffer();

        final String rowStart = (pos % 2 == 0) ? displayPropertiesManager.getEvenRowStart() : displayPropertiesManager.getOddRowStart();
        output.append(rowStart);

        final Collection cells = row.getCells();
        for (Iterator iterator = cells.iterator(); iterator.hasNext();) {
            Cell cell = (Cell) iterator.next();
            String cellHtml = MessageFormat.format(displayPropertiesManager.getCellStart(), getCellHtml(cell));
            output.append(cellHtml).append(displayPropertiesManager.getCellEnd());
        }

        output.append(displayPropertiesManager.getRowEnd());

        return output.toString();
    }

    private List<ArtefactAssociationWrapperBean> associations;
    private Report report;
    private boolean inverse;
}
