/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.display.support;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.crosstab.Cell;
import com.zynap.talentstudio.analysis.reports.crosstab.Row;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.web.utils.HtmlUtils;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 16-May-2007 16:22:38
 */
public class ArtefactViewBuilder extends AbstractDisplayViewBuilder {

    public ArtefactViewBuilder(String imageUrl, String imageMessage, String noImageMessage, boolean checkNodeAccess, String viewPositionUrl, String viewSubjectUrl, Node node) {
        super(imageUrl, imageMessage, noImageMessage, checkNodeAccess, viewPositionUrl, viewSubjectUrl);
        this.node = node;
    }

    public String buildOutput(Report report, Long userId, String viewType, ArtefactViewQuestionnaireHelper displayHelper,
                              int cellCount, String labelStyle, String fieldStyle) throws TalentStudioException {

        final StringBuffer output = new StringBuffer();
        
        Map answers = new HashMap();
        List<AnalysisParameter> questionnaireAttributes = report.getQuestionnaireAttributes();
        if (!questionnaireAttributes.isEmpty()) {
            if (PERSONAL_VIEW.equals(viewType)) {
                answers = displayHelper.getPersonalQuestionnaireAnswers(questionnaireAttributes, node, userId);
            } else {
                answers = displayHelper.getNodeQuestionnaireAnswers(node, questionnaireAttributes, userId);
            }
        }

        final Row row = buildRow(report, node, answers);
        output.append(propertiesManager.getString("row.start"));

        final Collection cells = row.getCells();
        int pos = 0;
        for (Iterator cellIterator = cells.iterator(); cellIterator.hasNext(); pos++) {
            if (pos != 0 && pos % cellCount == 0) {
                // close the row
                output.append(propertiesManager.getString("row.end"));
                // open a new row
                output.append(propertiesManager.getString("row.start"));
            }
            Cell cell = (Cell) cellIterator.next();
            // create the label table data with class and values
            output.append(MessageFormat.format(propertiesManager.getString("label.format"), labelStyle, HtmlUtils.htmlEscape(cell.getLabel())));
            if (cell.isImage()) {
                output.append(MessageFormat.format(propertiesManager.getString("cell.format"), fieldStyle, getImageCellHtml(cell)));
            } else {
                output.append(MessageFormat.format(propertiesManager.getString("cell.format"), fieldStyle, getCellHtml(cell)));
            }
        }

        while (pos % cellCount != 0) {
            output.append(MessageFormat.format(propertiesManager.getString("cell.format"), labelStyle, "&nbsp;"));
            output.append(MessageFormat.format(propertiesManager.getString("cell.format"), fieldStyle, "&nbsp;"));
            pos++;
        }

        output.append(propertiesManager.getString("row.end"));
        return output.toString();
    }

    /**
     * Must be overridden to prevent linking to self.
     *
     * @param attributeName
     * @param cell
     * @return true if super returns true and the attributeValues's node is not the current node.
     */
    protected boolean supportsLink(final String attributeName, Cell cell) {
        // notIsSelf needs to determine that the value is not the value of the current node being considered.
        return (super.supportsLink(attributeName, cell) && notIsSelf(cell.getTargetNode()));
    }

    /**
     * Do not provide a link to self
     *
     * @param otherNode
     * @return true if the attributesValue's node is not the same as the current node, false otherwise
     */
    private boolean notIsSelf(Node otherNode) {
        return (otherNode == null || (!this.node.getId().equals(otherNode.getId())));
    }

    /**
     * Build rows of cells (1 cell for each column in the report).
     * <br/> If the node has no value for the cell attribute, then will return a cell with a blank value.
     *
     * @param report
     * @param node
     * @param questionnaireAnswers
     * @return Row (will always return one row, never null)
     */
    Row buildRow(Report report, Node node, Map questionnaireAnswers) {
        final Row row = new Row();

        final List columns = report.getColumns();
        if (columns != null && !columns.isEmpty()) {


            for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
                final Column column = (Column) iterator.next();
                row.add(createCell(node, column, questionnaireAnswers));
            }
        }

        return row;
    }

    public static final String PERSONAL_VIEW = "personnal";
    private Node node;
}
