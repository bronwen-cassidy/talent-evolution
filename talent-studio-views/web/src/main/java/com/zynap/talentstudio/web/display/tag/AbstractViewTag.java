/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.display.tag;

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.reports.crosstab.Cell;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.display.support.ArtefactViewQuestionnaireHelper;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.util.resource.PropertiesManager;
import com.zynap.web.utils.HtmlUtils;

import org.springframework.context.MessageSource;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;
import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tag library that displays the contents of a tabular report.
 *
 * @author Angus Mark
 */
public abstract class AbstractViewTag extends RequestContextAwareTag {

    public void setDisplayHelper(Object displayHelper) throws JspException {
        if (ExpressionEvaluationUtils.isExpressionLanguage(displayHelper.toString())) {
            this.displayHelper = (ArtefactViewQuestionnaireHelper) ExpressionEvaluationUtils.evaluate(displayHelper.toString(), displayHelper.toString(), Object.class, pageContext);
        } else {
            this.displayHelper = (ArtefactViewQuestionnaireHelper) displayHelper;
        }
    }

    public void release() {
        super.release();

        this.viewPositionUrl = null;
        this.viewSubjectUrl = null;
        this.checkNodeAccess = false;
    }

    public void setViewSubjectUrl(String viewSubjectUrl) throws JspException {
        this.viewSubjectUrl = ExpressionEvaluationUtils.evaluateString("viewSubjectUrl", viewSubjectUrl, pageContext);
    }

    public void setViewPositionUrl(String viewPositionUrl) throws JspException {
        this.viewPositionUrl = ExpressionEvaluationUtils.evaluateString("viewPositionUrl", viewPositionUrl, pageContext);
    }

    public void setCheckNodeAccess(String checkNodeAccess) throws JspException {
        this.checkNodeAccess = ExpressionEvaluationUtils.evaluateBoolean("checkNodeAccess", checkNodeAccess, pageContext);
    }

    public void setImageUrl(String imageUrl) throws JspException {
        if (ExpressionEvaluationUtils.isExpressionLanguage(imageUrl))
            this.imageUrl = (String) ExpressionEvaluationUtils.evaluate(imageUrl, imageUrl, Object.class, pageContext);
        else
            this.imageUrl = imageUrl;
    }

    protected String getImageCellHtml(Cell cell) {

        // get message
        final UserSession userSession = ZynapWebUtils.getUserSession((HttpServletRequest) pageContext.getRequest());
        final WebApplicationContext webApplicationContext = getRequestContext().getWebApplicationContext();
        MessageSource messageSource = (MessageSource) webApplicationContext.getBean(ArtefactViewTag.MESSAGES_BEAN);

        String output;
        if (cell.isValueSet()) {
            String format = displayPropertiesManager.getImageDisplayCellFormat();
            // build URL with node id for security check and item id for image

            final Node node = cell.getSourceNode();
            final Long nodeId = node.getId();
            final String baseURL = ZynapWebUtils.addContextPath((HttpServletRequest) pageContext.getRequest(), imageUrl);
            final Map<String, Object> params = new HashMap<String, Object>();
            params.put(ParameterConstants.NODE_ID_PARAM, nodeId);
            params.put(ParameterConstants.ITEM_ID, cell.getNodeExtendedAttributeId());
            final String url = ZynapWebUtils.buildURL(baseURL, params);
            String message = messageSource.getMessage(ArtefactViewTag.IMAGE_MESSAGE_KEY, null, "Image", userSession.getLocale());

            output = MessageFormat.format(format, nodeId, url, message);
        } else {
            final String format = displayPropertiesManager.getMissingImageFormat();
            String message = messageSource.getMessage(ArtefactViewTag.NO_IMAGE_MESSAGE_KEY, null, "No image provided", userSession.getLocale());
            output = MessageFormat.format(format, message);
        }

        return output;
    }

    protected String getExecImageCellHtml(Cell cell) {

        // get message
        final UserSession userSession = ZynapWebUtils.getUserSession((HttpServletRequest) pageContext.getRequest());
        final WebApplicationContext webApplicationContext = getRequestContext().getWebApplicationContext();
        MessageSource messageSource = (MessageSource) webApplicationContext.getBean(ArtefactViewTag.MESSAGES_BEAN);

        String output;
        if (cell.isValueSet()) {
            String format = displayPropertiesManager.getExecImageDisplayCellFormat();

            final Node node = cell.getSourceNode();
            final Long nodeId = node.getId();
            final String baseURL = ZynapWebUtils.addContextPath((HttpServletRequest) pageContext.getRequest(), imageUrl);
            final Map<String, Object> params = new HashMap<String, Object>();
            params.put(ParameterConstants.NODE_ID_PARAM, nodeId);
            params.put(ParameterConstants.ITEM_ID, cell.getNodeExtendedAttributeId());
            final String url = ZynapWebUtils.buildURL(baseURL, params);
            String message = messageSource.getMessage(ArtefactViewTag.IMAGE_MESSAGE_KEY, null, "Image", userSession.getLocale());

            output = MessageFormat.format(format, nodeId, url, message);
        } else {
            final String format = displayPropertiesManager.getMissingImageFormat();
            String message = messageSource.getMessage(ArtefactViewTag.NO_IMAGE_MESSAGE_KEY, null, "No image provided", userSession.getLocale());
            output = MessageFormat.format(format, message);
        }

        return output;
    }

    protected AbstractViewTag.DisplayPropertiesManager displayPropertiesManager = new DisplayPropertiesManager();

    protected String getCellHtml(Cell cell) {

        boolean isLinkAttribute = cell.getValue() != null && cell.getDynamicAttribute() != null && cell.getDynamicAttribute().isLinkType();
        String cellValue;
        if (cell.isList()) {
            cellValue = getListCellHtml(cell);
        } else if (cell.isImage()) {
            cellValue = getImageCellHtml(cell);
        } else if (isLinkAttribute) {
            cellValue = getLinkAttributeHtml(cell);
        } else {
            cellValue = getLinkHtml(getLink(cell), cell);
        }
        return cellValue;
    }

    /**
     * Check if attribute supports link.
     *
     * @param attributeName
     * @param cell
     * @return true or false
     */
    protected boolean supportsLink(final String attributeName, Cell cell) {
        return AnalysisAttributeHelper.supportsLink(attributeName);
    }


    private String getLinkAttributeHtml(Cell cell) {
        String value = cell.getDisplayValue();
        String labelValue = cell.getAttributeName();
        if (cell.isBlank()) {
            return value;
        }
        return MessageFormat.format(displayPropertiesManager.getAttributeLinkFormat(), value, labelValue);
    }

    private String getListCellHtml(Cell cell) {
        StringBuffer linkBuffer = new StringBuffer();

        List values = ((Cell.ListCell) cell).getValues();
        if (values.isEmpty()) {
            String displayValue = HtmlUtils.htmlEscape(cell.getDisplayValue());
            linkBuffer.append(MessageFormat.format(displayPropertiesManager.getMultiLineFormat(), displayValue));
        } else {
            for (int i = 0; i < values.size(); i++) {
                Cell nestedCell = (Cell) values.get(i);
                final String cellValue = getCellHtml(nestedCell);
                linkBuffer.append(MessageFormat.format(displayPropertiesManager.getMultiLineFormat(), cellValue));               
            }
        }

        return linkBuffer.toString();
    }

    private String getLinkHtml(String link, Cell cell) {

        final String displayValue = HtmlUtils.htmlEscape(cell.getDisplayValue());
        if (StringUtils.hasText(link)) {
            String cellFormat = displayPropertiesManager.getLinkCellFormat();
            return MessageFormat.format(cellFormat, link, displayValue);
        }

        return displayValue;
    }

    private String getLink(Cell cell) {

        String link = null;

        final String attributeName = cell.getAttributeName();

        if (supportsLink(attributeName, cell)) {

            // link only to be displayed if we are not checking access (as is the case for reports as the nodes returned have already been filtered by access)
            // or if we are checking node access and the node indicates we do have access
            final Node node = cell.getSourceNode();

            if (node != null && (!checkNodeAccess || checkAccess(node))) {
                if (node instanceof Position) {
                    link = ZynapWebUtils.buildURL(viewPositionUrl, ParameterConstants.NODE_ID_PARAM, node.getId());
                } else if (node instanceof Subject) {
                    link = ZynapWebUtils.buildURL(viewSubjectUrl, ParameterConstants.NODE_ID_PARAM, node.getId());
                }
            }
        }

        return link;
    }

    /**
     * checks access when required for arbitary nested nodes
     *
     * @param node
     * @return true if has access, false otherwise
     */
    protected boolean checkAccess(Node node) {
        boolean access = node.isHasAccess();
        if (!access) {
            access = displayHelper.checkNodeAccess(node);
        }
        return access;
    }

    final class DisplayPropertiesManager {

        private final PropertiesManager propertiesManager = PropertiesManager.getInstance(AbstractViewTag.class);

        String getColumnsStart() {
            return propertiesManager.getString("columns.start");
        }

        public String getMultiLineFormat() {
            return propertiesManager.getString("multicell.format");
        }

        String getColumnsEnd() {
            return propertiesManager.getString("columns.end");
        }

        String getColumnFormat() {
            return propertiesManager.getString("column");
        }

        String getRowsStart() {
            return propertiesManager.getString("rows.start");
        }

        String getRowsEnd() {
            return propertiesManager.getString("rows.end");
        }

        String getEvenRowStart() {
            return propertiesManager.getString("row.start.even");
        }

        String getOddRowStart() {
            return propertiesManager.getString("row.start.odd");
        }

        public String getRowStart() {
            return propertiesManager.getString("row.start");
        }

        public String getCellFormat() {
            return propertiesManager.getString("cell.format");
        }

        public String getLabelFormat() {
            return propertiesManager.getString("label.format");
        }

        public String getRowEnd() {
            return propertiesManager.getString("row.end");
        }

        public String getCellStart() {
            return propertiesManager.getString("cell.start");
        }

        public String getCellEnd() {
            return propertiesManager.getString("cell.end");
        }

        public String getLinkCellFormat() {
            return propertiesManager.getString("cell.link.format");
        }

        public String getAttributeLinkFormat() {
            return propertiesManager.getString("cell.link.attribute.format");
        }

        public String getImageDisplayCellFormat() {
            return propertiesManager.getString("cell.image.inline.format");
        }

        public String getExecImageDisplayCellFormat() {
            return propertiesManager.getString("cell.image.exec.format");
        }

        public String getMissingImageFormat() {
            return propertiesManager.getString("cell.image.blank.format");
        }
    }

    /**
     * Indicates whether or not to check node access when determining if a cell should have a link.
     * <br/> Defaults to false.
     */
    private boolean checkNodeAccess = false;
    private String viewSubjectUrl;
    private String viewPositionUrl;
    protected ArtefactViewQuestionnaireHelper displayHelper;
    private String imageUrl;
}
