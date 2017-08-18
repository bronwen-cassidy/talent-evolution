/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.display.tag;

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.crosstab.Cell;
import com.zynap.talentstudio.analysis.reports.crosstab.Row;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.web.display.support.ArtefactViewModelBuilder;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.HtmlUtils;

import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public final class ArtefactViewTag extends AbstractViewTag {

    public void setCellCount(int cellCount) {
        this.cellCount = cellCount;
    }

    public void setLabelStyle(String labelStyle) {
        this.labelStyle = labelStyle;
    }

    public void setFieldStyle(String fieldStyle) {
        this.fieldStyle = fieldStyle;
    }

    public void setViewType(String viewType) throws JspException {
        this.viewType = ExpressionEvaluationUtils.evaluateString("viewType", viewType, pageContext);
    }

    public void setExecutiveSummary(String executiveSummary) throws JspException {
        this.executiveSummary = ExpressionEvaluationUtils.evaluateBoolean("executiveSummary", executiveSummary, pageContext);
    }

    public void setReport(Object report) throws JspException {
        if (ExpressionEvaluationUtils.isExpressionLanguage(report.toString())) {
            this.report = (Report) ExpressionEvaluationUtils.evaluate(report.toString(), report.toString(), Object.class, pageContext);
        } else {
            this.report = (Report) report;
        }
    }

    public void setNode(Object node) throws JspException {
        if (ExpressionEvaluationUtils.isExpressionLanguage(node.toString())) {
            this.node = (Node) ExpressionEvaluationUtils.evaluate(node.toString(), node.toString(), Object.class, pageContext);
        } else {
            this.node = (Node) node;
        }
    }

    protected int doStartTagInternal() throws Exception {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        UserSession userSession = ZynapWebUtils.getUserSession((HttpServletRequest) pageContext.getRequest());
        Locale locale = request.getLocale();
        userSession.setLocale(locale);

        UserSessionFactory.setUserSession(userSession);

        final StringBuffer output = new StringBuffer();

        Map answers = new HashMap();
        List<AnalysisParameter> questionnaireAttributes = report.getQuestionnaireAttributes();
        if (!questionnaireAttributes.isEmpty()) {

            final Long userId = ZynapWebUtils.getUserId((HttpServletRequest) pageContext.getRequest());
            if (PERSONAL_VIEW.equals(viewType)) {
                answers = displayHelper.getPersonalQuestionnaireAnswers(questionnaireAttributes, node, userId);
            } else {
                answers = displayHelper.getNodeQuestionnaireAnswers(node, questionnaireAttributes, userId);
            }
        }

        final Row row = ArtefactViewModelBuilder.buildRow(report, node, displayHelper, answers);
        output.append(displayPropertiesManager.getRowStart());

        final Collection cells = row.getCells();
        int pos = 0;
        boolean odd = false;
        for (Iterator cellIterator = cells.iterator(); cellIterator.hasNext(); pos++) {
            if (pos != 0 && pos % cellCount == 0) {
                // close the row
                output.append(displayPropertiesManager.getRowEnd());
                // open a new row needs to be odd or even
                output.append(odd ? displayPropertiesManager.getOddRowStart() : displayPropertiesManager.getEvenRowStart());
                odd = !odd;
            }
            Cell cell = (Cell) cellIterator.next();
            // create the label table data with class and values
            if (cell.getDynamicAttribute() != null) {
                String desc = cell.getDynamicAttribute().getDescription();
                String attrFieldStyle = "attrLabel infodata detailCell";
                output.append(MessageFormat.format(displayPropertiesManager.getLabelFormat(), attrFieldStyle, HtmlUtils.htmlEscape(cell.getLabel()), desc));
            } else {
                output.append(MessageFormat.format(displayPropertiesManager.getLabelFormat(), labelStyle, HtmlUtils.htmlEscape(cell.getLabel()), ""));
            }
            if (cell.isImage()) {
                if (executiveSummary) {
                    output.append(MessageFormat.format(displayPropertiesManager.getCellFormat(), fieldStyle, getExecImageCellHtml(cell)));
                } else {
                    output.append(MessageFormat.format(displayPropertiesManager.getCellFormat(), fieldStyle, getImageCellHtml(cell)));
                }
            } else {
                output.append(MessageFormat.format(displayPropertiesManager.getCellFormat(), fieldStyle, getCellHtml(cell)));

            }
        }

        while (pos % cellCount != 0) {
            output.append(MessageFormat.format(displayPropertiesManager.getCellFormat(), labelStyle, "&nbsp;"));
            output.append(MessageFormat.format(displayPropertiesManager.getCellFormat(), fieldStyle, "&nbsp;"));
            pos++;
        }

        output.append(displayPropertiesManager.getRowEnd());


        try {
            pageContext.getOut().println(output.toString());
        } catch (IOException e) {
            UserSessionFactory.setUserSession(null);
            throw new JspException(e);
        }

        UserSessionFactory.setUserSession(null);
        return EVAL_PAGE;
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
        return (AnalysisAttributeHelper.supportsLink(attributeName) && notIsSelf(cell.getTargetNode()));
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
     * @return Row (will always return one row, never null)
     */
    Row buildRow(Report report, Node node, Map questionnaireAnswers) {
        return ArtefactViewModelBuilder.buildRow(report, node, displayHelper, questionnaireAnswers);
    }

    private int cellCount = 2;
    private String labelStyle = "infolabel detailCell";
    private String fieldStyle = "infodata detailCell";
    private String viewType = null;
    private Report report;
    private Node node;
    private boolean executiveSummary;

    public static final String IMAGE_MESSAGE_KEY = "image";
    public static final String NO_IMAGE_MESSAGE_KEY = "no.image.available";
    public static final String MESSAGES_BEAN = "messageSource";
    public static final String PERSONAL_VIEW = "personnal";
}
