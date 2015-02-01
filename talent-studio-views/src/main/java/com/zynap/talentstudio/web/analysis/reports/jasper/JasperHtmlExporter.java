/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.jasper;

import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.export.ElementWrapper;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.export.JRGridLayout;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.util.JRStyledText;

import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.analysis.reports.jasper.MetricReportDesigner;
import com.zynap.talentstudio.analysis.reports.jasper.ReportDesigner;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.util.resource.PropertiesManager;
import com.zynap.web.utils.HtmlUtils;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 05-Jan-2006 08:24:57
 */
public class JasperHtmlExporter extends DefaultJasperHtmlExporter {

    public JasperHtmlExporter(String subjectUrl, String positionUrl, String drillDownUrl, String viewQuestionnaireUrl, Report report) {
        super(subjectUrl, positionUrl, drillDownUrl, viewQuestionnaireUrl, report);
        setParameter(JRHtmlExporterParameter.HTML_HEADER, displayPropertiesManager.getHtmlHeader());
        setParameter(JRHtmlExporterParameter.HTML_FOOTER, displayPropertiesManager.getHtmlFooter());
    }

    protected void exportGrid(JRGridLayout gridLayout, boolean whitePageBackground) throws IOException, JRException {


        JRExporterGridCell[][] grid = gridLayout.getGrid();
        boolean[] isRowNotEmpty = gridLayout.getIsRowNotEmpty();               

        startHtmlTable();

        for (int y = 0; y < grid.length; y++) {
            
            if (evaluateRow(grid[y], isRowNotEmpty[y])) {

                JRPrintElement nextElement = grid[y][0].getElement();
                rowCellClass = ODD_STYLE;

                if (nextElement != null && (!(grid[y][0].getElement() instanceof JRPrintRectangle))) {

                    String style = nextElement.getStyle() != null ? nextElement.getStyle().getName() : "";
                    if (ReportDesigner.GROUP_STYLE.equals(style)) {

                        if (nextElement instanceof JRPrintImage || (nextElement instanceof JRPrintText && (!StringUtils.isBlank(((JRPrintText) nextElement).getText())))) {
                            rowCellClass = DEFAULT_GROUP_STYLE;
                        }
                    }
                }

                startRow(rowCellClass);

                // write the table datas
                int emptyCellColSpan = writeTableCells(grid, y);

                endRow(emptyCellColSpan);
                // close the table head and open the body if y == 0
                if (y == 0) {
                    writer.write(displayPropertiesManager.getTableHeadEnd());
                }
            }
        }
        writer.write(displayPropertiesManager.getReportEnd());

    }

    private boolean evaluateRow(JRExporterGridCell[] gridCells, boolean isRowNotEmpty) {
        boolean evaluateRow;
        if(report.isProgressReport()) {
            evaluateRow = (isRowNotEmpty && rowHasValues(gridCells));
        } else {
            evaluateRow = (isRowNotEmpty || !isRemoveEmptySpace);
        }
        return evaluateRow;
    }

    private boolean rowHasValues(JRExporterGridCell[] gridCells) {
        
        if(!report.isProgressReport()) return true;

        boolean hasValues = false;
        for (JRExporterGridCell gridCell : gridCells) {
            JRPrintElement element = gridCell.getElement();
            JRBox box = gridCell.getBox();
            ElementWrapper wrapper = gridCell.getWrapper();
            if(element != null && box != null && wrapper != null) {
                hasValues = true;
                break;
            }
        }
        return report.isProgressReport() && hasValues;
    }

    private int writeTableCells(JRExporterGridCell[][] grid, int y) throws IOException, JRException {
         
        int emptyCellColSpan = 0;

        for (int x = 0; x < grid[y].length; x++) {
            // non null element
            if (grid[y][x].getElement() != null) {

                emptyCellColSpan = checkEmptyColSpan(emptyCellColSpan);
                JRPrintElement element = grid[y][x].getElement();
                formatElement(element, grid, y, x);
                x += grid[y][x].getColSpan() - 1;

            } else {
                emptyCellColSpan++;
            }
        }
        return emptyCellColSpan;
    }

    private void startRow(String rowClass) throws IOException {
        writer.write(MessageFormat.format(displayPropertiesManager.getRowFormat(), rowClass));
    }

    private void endRow(int emptyCellColSpan) throws IOException {
        writeEmptyCellSpan(emptyCellColSpan);
        writer.write(displayPropertiesManager.getRowEnd());
    }

    private void startHtmlTable() throws IOException {
        String tableId = "abcxr" + String.valueOf(report.getId());
        String tableClass = "pager";
        if (report.isMetricReport()) tableId = "mtrcRp" + String.valueOf(report.getId());
        if (report.isProgressReport()) tableClass = "tablesorter";
        writer.write(MessageFormat.format(displayPropertiesManager.getReportStart(), tableClass, tableId));
    }

    /**
     * Writes the rectangle of empty cell elements.
     *
     * @param element
     * @param gridCell
     * @throws IOException
     */
    protected void exportRectangle(JRPrintElement element, JRExporterGridCell gridCell) throws IOException {
        StringBuffer rectangleBuffer = formatCellStart(gridCell, getDefaultStyle());
        rectangleBuffer.append(EMPTY_CELL_CHARACTER);
        rectangleBuffer.append(displayPropertiesManager.getCellEnd());
        writer.write(rectangleBuffer.toString());
    }

    protected void exportImage(JRPrintImage image, JRExporterGridCell gridCell) throws IOException {
        StringBuffer imageBuffer = formatCellStart(gridCell, getStyle(image));
        JRImageRenderer renderer = (JRImageRenderer) image.getRenderer();

        if (renderer != null) {
            String imageValue = MessageFormat.format(displayPropertiesManager.getCellImageFormat(), renderer.getImageLocation(), image.getAnchorName(), image.getAnchorName());

            String href = formatLink(image);
            if (href != null) {
                imageBuffer.append(MessageFormat.format(displayPropertiesManager.getLinkFormat(), href, imageValue));
            } else {
                imageBuffer.append(imageValue);
            }
        } else {
            // no image export a dash
            imageBuffer.append(ReportConstants.BLANK_VALUE);
        }
        imageBuffer.append(displayPropertiesManager.getCellEnd());
        writer.write(imageBuffer.toString());
    }

    /**
     * Exports a line to the page, currently uses a black image, but this can be changed to fill the td with a foreground, background color.
     *
     * @param line
     * @param gridCell
     * @throws IOException
     */
    protected void exportLine(JRPrintLine line, JRExporterGridCell gridCell) throws IOException {

        StringBuffer lineBuffer = formatCellStart(gridCell, getDefaultStyle());
        lineBuffer.append(displayPropertiesManager.getLineFormat());
        lineBuffer.append(displayPropertiesManager.getCellEnd());
        writer.write(lineBuffer.toString());

    }

    private void formatElement(JRPrintElement element, JRExporterGridCell[][] grid, int y, int x) throws IOException, JRException {
        if (element instanceof JRPrintLine) {

            exportLine((JRPrintLine) element, grid[y][x]);
        } else if (element instanceof JRPrintRectangle) {

            // not handling a rectangle leave it to super for now
            exportRectangle(element, grid[y][x]);
        } else if (element instanceof JRPrintEllipse) {

            // not handling an elipse leave to super
            exportRectangle(element, grid[y][x]);
        } else if (element instanceof JRPrintImage) {

            exportImage((JRPrintImage) element, grid[y][x]);
        } else if (element instanceof JRPrintText) {

            exportText((JRPrintText) element, grid[y][x]);
        } else if (element instanceof JRPrintFrame) {

            // not handled let the super deal with it
            exportFrame((JRPrintFrame) element, grid[y][x]);
        }
    }

    private void writeEmptyCellSpan(int emptyCellColSpan) throws IOException {
        if (emptyCellColSpan > 0) {
            writer.write(formatEmptyCells(emptyCellColSpan));
        }
    }

    private int checkEmptyColSpan(int emptyCellColSpan) throws IOException {

        if (emptyCellColSpan > 0) {
            String colspanedTd = formatEmptyCells(emptyCellColSpan);
            writer.write(colspanedTd);
            emptyCellColSpan = 0;
        }
        return emptyCellColSpan;
    }

    protected void exportText(JRPrintText printText, JRExporterGridCell gridCell) throws IOException {

        JRStyledText styledText = getStyledText(printText);
        String formattedText = formatText(styledText);

        String cellStyleClass = getStyle(printText);
        StringBuffer textBuffer = formatCellStart(gridCell, cellStyleClass);

        final String anchorName = printText.getAnchorName();
        // the anchor name is used for the color legend of the tabular reports or the metric report chart links
        if (anchorName != null) {
            // metric column header link
            String id = anchorName.substring(anchorName.indexOf("(") + 1, anchorName.indexOf(")"));
            String linkId = "agr_" + id;
            String formattedId = ZynapWebUtils.javaScriptEscape(id);
            if (anchorName.indexOf(MetricReportDesigner.METRIC_COLUMN_HEADER_KEY) != -1) {
                String hiddenFieldId = ZynapWebUtils.javaScriptEscape(ReportConstants.METRIC_COL_ID);
                textBuffer.append(MessageFormat.format(displayPropertiesManager.getMetricColumnLink(), linkId, hiddenFieldId, formattedId, formattedText));
            } else if (anchorName.indexOf(MetricReportDesigner.METRIC_BAR_COLUMN_HEADER_KEY) != -1) {
                formattedId = "bar" + linkId;
                textBuffer.append(MessageFormat.format(displayPropertiesManager.getBarChartOptionsLink(), formattedId, formattedText));
            } else {
                String prefix = ZynapWebUtils.javaScriptEscape("agr_");
                textBuffer.append(MessageFormat.format(displayPropertiesManager.getJavascriptAnchorFormat(), linkId, prefix, formattedId, formattedText));
            }
        } else {
            String href = formatLink(printText);
            if (href != null) {
                textBuffer.append(MessageFormat.format(displayPropertiesManager.getLinkFormat(), href, formattedText));
            } else {
                textBuffer.append(formattedText);
            }
        }

        if (COLUMN_HEADER.equals(cellStyleClass)) {
            textBuffer.append(displayPropertiesManager.getCellHeaderEnd());
        } else {
            textBuffer.append(displayPropertiesManager.getCellEnd());
        }

        writer.write(textBuffer.toString());
    }

    private String formatText(JRStyledText styledText) {

        String output = ReportConstants.BLANK_VALUE;

        if (styledText != null) {
            String text = styledText.getText();
            if (StringUtils.isNotEmpty(text)) output = HtmlUtils.htmlEscape(text);
        }

        return output;
    }

    private StringBuffer formatCellStart(JRExporterGridCell gridCell, String styleClass) {

        StringBuffer lineBuffer = new StringBuffer();

        boolean isColSpanned = gridCell.getColSpan() > 1 && !report.isProgressReport();
        boolean isRowSpanned = gridCell.getRowSpan() > 1 && !report.isProgressReport();

        if (isColSpanned && !isRowSpanned) {
            lineBuffer.append(MessageFormat.format(displayPropertiesManager.getColSpannedCellStart(), String.valueOf(gridCell.getColSpan()), styleClass));

        } else if (isRowSpanned && !isColSpanned) {
            lineBuffer.append(MessageFormat.format(displayPropertiesManager.getRowSpannedCellStart(), String.valueOf(gridCell.getRowSpan()), styleClass));

        } else if (isColSpanned && isRowSpanned) {
            lineBuffer.append(MessageFormat.format(displayPropertiesManager.getRowColSpannedCellStart(), String.valueOf(gridCell.getColSpan()), String.valueOf(gridCell.getRowSpan()), styleClass));

        } else {

            if (COLUMN_HEADER.equals(styleClass)) {
                lineBuffer.append(displayPropertiesManager.getCellHeaderStart());
            } else {
                lineBuffer.append(MessageFormat.format(displayPropertiesManager.getCellStart(), styleClass));
            }
        }

        return lineBuffer;
    }

    private String formatEmptyCells(int emptyCellColSpan) {

        final String style = getDefaultStyle();

        StringBuffer colspanedTd = new StringBuffer();
        for (int i = emptyCellColSpan; i > 0; i--) {
            colspanedTd.append(MessageFormat.format(displayPropertiesManager.getEmptyCell(), style));
        }
        return colspanedTd.toString();
    }

    private String formatLink(JRPrintHyperlink hyperLinkText) {
        String href = null;
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
            default:
                break;
        }

        return checkAccess(href);
    }

    private String getStyle(JRPrintElement printElement) {
        final JRStyle style = printElement.getStyle();

        if (style != null) {
            return styles.get(style.getName());
        } else if (rowCellClass != null && rowCellClass.equals(styles.get(ReportDesigner.GROUP_STYLE))) {
            return styles.get(ReportDesigner.GROUP_STYLE);
        } else {
            return getDefaultStyle();
        }
    }

    private String getDefaultStyle() {
        return styles.get(ReportDesigner.DETAIL_STYLE);
    }

    private static final String EMPTY_CELL_CHARACTER = "&nbsp;";

    private static final String ODD_STYLE = "odd";

    private JasperDisplayPropertiesManager displayPropertiesManager = new JasperDisplayPropertiesManager();

    private String rowCellClass = ODD_STYLE;

    private final static Map<String, String> styles = new HashMap<String, String>();

    private static final String COLUMN_HEADER = "columnHeader";

    static {
        styles.put(ReportDesigner.HEADER_STYLE, COLUMN_HEADER);
        styles.put(ReportDesigner.DETAIL_STYLE, "pager dataCell");
        styles.put(ReportDesigner.GROUP_STYLE, "pager dataCell");
    }

    final class JasperDisplayPropertiesManager {

        public String getHtmlFooter() {
            return propertiesManager.getString("html.footer");
        }

        public String getHtmlHeader() {
            return propertiesManager.getString("html.header");
        }

        public String getReportStart() {
            return propertiesManager.getString("report.start");
        }

        public String getRowEnd() {
            return propertiesManager.getString("report.row.end");
        }

        public String getEmptyCell() {
            return propertiesManager.getString("empty.cell");
        }

        public String getColSpannedCellStart() {
            return propertiesManager.getString("colspan.cell.start");
        }

        public String getRowSpannedCellStart() {
            return propertiesManager.getString("rowspan.cell.start");
        }

        public String getRowColSpannedCellStart() {
            return propertiesManager.getString("colspan.rowspan.cell.start");
        }

        public String getLineFormat() {
            return propertiesManager.getString("line.format");
        }

        public String getCellStart() {
            return propertiesManager.getString("cell.start");
        }

        public String getCellEnd() {
            return propertiesManager.getString("cell.end");
        }

        public String getJavascriptAnchorFormat() {
            return propertiesManager.getString("column.header.link");
        }

        public String getMetricColumnLink() {
            return propertiesManager.getString("metric.column.header.link");
        }

        public String getLinkFormat() {
            return propertiesManager.getString("cell.link.format");
        }

        public String getReportEnd() {
            return propertiesManager.getString("report.end");
        }

        public String getCellHeaderStart() {
            return propertiesManager.getString("cell.header.start");
        }

        public String getCellImageFormat() {
            return propertiesManager.getString("cell.colour.format");
        }

        public String getRowFormat() {
            return propertiesManager.getString("report.row.start.format");
        }

        public String getBarChartOptionsLink() {
            return propertiesManager.getString("metric.bar.chart.options");
        }

        public String getCellHeaderEnd() {
            return propertiesManager.getString("cell.header.end");
        }

        public String getTableHeadEnd() {
            return propertiesManager.getString("table.head.end");
        }


        private final PropertiesManager propertiesManager = PropertiesManager.getInstance(JasperHtmlExporter.class);
    }

    private static final String DEFAULT_GROUP_STYLE = "group";
}