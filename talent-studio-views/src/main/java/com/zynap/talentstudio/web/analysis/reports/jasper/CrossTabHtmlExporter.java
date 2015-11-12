/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.jasper;

import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.export.JRGridLayout;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;

import com.zynap.talentstudio.analysis.reports.CrossTabReport;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.analysis.reports.crosstab.Heading;
import com.zynap.talentstudio.analysis.reports.crosstab.Row;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.util.resource.PropertiesManager;
import com.zynap.talentstudio.web.utils.HtmlUtils;

import org.apache.commons.lang.StringUtils;

import org.springframework.web.util.JavaScriptUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
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
 * @since 05-Jan-2006 08:24:57
 */
public final class CrossTabHtmlExporter extends DefaultJasperHtmlExporter {

    public CrossTabHtmlExporter(String subjectUrl, String positionUrl, String drillDownUrl, String viewQuestionnaireUrl, String drillDownAltText, Report crosstabReport, Collection columnHeadings, Collection rowHeadings) {

        super(subjectUrl, positionUrl, drillDownUrl, viewQuestionnaireUrl, crosstabReport);
        this.columnHeadings = new ArrayList(columnHeadings);
        this.rowHeadings = new ArrayList(rowHeadings);
        this.drillDownAltText = drillDownAltText;
        setParameter(JRHtmlExporterParameter.HTML_HEADER, displayPropertiesManager.getHtmlHeader());
        setParameter(JRHtmlExporterParameter.HTML_FOOTER, displayPropertiesManager.getHtmlFooter());
    }

    protected void exportGrid(JRGridLayout gridLayout, boolean whitePageBackground) throws IOException {

        JRExporterGridCell[][] grid = gridLayout.getGrid();
        boolean[] isRowNotEmpty = gridLayout.getIsRowNotEmpty();
        StringBuffer headingBuffer = new StringBuffer();
        StringBuffer crosstabBuffer = new StringBuffer();
        List headings = new ArrayList();
        // main body
        crosstabBuffer.append(gridFormat(grid, isRowNotEmpty, headings));

        // heading infomation
        headingBuffer.append(startPageFormat(headings));
        headingBuffer.append(crosstabBuffer.toString());

        writer.write(headingBuffer.toString());
    }

    /**
     * Creates the page start, this builds the horizontal axis and the heading for the vertical axis.
     * <br/>
     * First the table tag the start currentRow the first empty td a td with a colspan of num headings and label of horizontal label
     *
     * @param columnHeadings
     * @return formatted string starting the crosstab
     */
    private String startPageFormat(Collection columnHeadings) {
        StringBuffer buffer = new StringBuffer();
        String columnLabel = ((CrossTabReport) report).getHorizontalColumn().getLabel();
        String rowLabel = ((CrossTabReport) report).getVerticalColumn().getLabel();
        buffer.append(MessageFormat.format(displayPropertiesManager.getReportStart(), columnLabel, rowLabel));
        // iterate the column headers adding the td's for each heading
        for (Iterator iterator = columnHeadings.iterator(); iterator.hasNext();) {
            String label = HtmlUtils.htmlEscape((String) iterator.next());
            buffer.append(MessageFormat.format(displayPropertiesManager.getCellHeadingFormat(), label));
        }
        buffer.append(displayPropertiesManager.getRowEnd());
        return buffer.toString();
    }

    private String gridFormat(JRExporterGridCell[][] grid, boolean[] rowNotEmpty, List headings) {
        StringBuffer gridBuffer = new StringBuffer();
        int colspan = 0;
        // which grid are we creating a drilldown for
        int rowIndex = 0;
        int colIndex = 0;
        int rowCount = 0;
        // do we write out the drilldown?
        boolean hasResults = false;
        JRPrintText currentCellElement = null;
        JRPrintText currentRowElement = null;

        for (int i = 2; i < rowNotEmpty.length; i++) {
            // do not handle an empty row
            if (!rowNotEmpty[i]) continue;

            JRExporterGridCell[] gridCells = grid[i];
            colspan = gridCells.length - 2;

            // y-axis start cell (the vertical axis value)
            JRPrintElement rowElement = gridCells[0].getElement();
            if (isPrintableElement(rowElement)) {

                // if we have previous row, close it
                if (currentRowElement != null) {

                    // add navigation for last cell of each row
                    addNavigationElements(gridBuffer, rowCount, hasResults, colspan, rowIndex, colIndex);

                    // end row as well
                    gridBuffer.append(displayPropertiesManager.getRowEnd());

                    // increment row index
                    rowIndex++;

                    // reset column index
                    colIndex = 0;
                }

                // start a new row
                String text = HtmlUtils.htmlEscape(((JRPrintText) rowElement).getText());
                gridBuffer.append(MessageFormat.format(displayPropertiesManager.getCrosstabRowStartFormat(), text));
                currentRowElement = (JRPrintText) rowElement;

                // current cell becomes null
                currentCellElement = null;

                // skip rest of processing
                continue;
            }

            JRPrintElement cellElement = gridCells[1].getElement();
            if (isPrintableElement(cellElement)) {

                // if we have a current cell this is the prev cell so close the entry
                if (currentCellElement != null) {

                    // add navigation
                    addNavigationElements(gridBuffer, rowCount, hasResults, colspan, rowIndex, colIndex);

                    // increment column index
                    colIndex++;
                }

                // start new table
                JRPrintText cellPrintText = (JRPrintText) cellElement;
                gridBuffer.append(displayPropertiesManager.getCellStart());
                String tableCellId = getTableId(rowIndex, colIndex);
                gridBuffer.append(MessageFormat.format(displayPropertiesManager.getDataTableStart(), tableCellId));

                // add value to list of headings
                String text = cellPrintText.getText();
                if (!headings.contains(text)) headings.add(text);

                // save as current cell
                currentCellElement = cellPrintText;

                // reset row count
                rowCount = 0;

                // skip rest of processing
                continue;
            }

            // handle the rest of the columns
            hasResults = appendData(gridCells, gridBuffer, rowCount);

            // increment row count
            rowCount++;
        }

        // add navigation for last cell of last row
        addNavigationElements(gridBuffer, rowCount, hasResults, colspan, rowIndex, colIndex);

        // end page
        gridBuffer.append(endPageFormat());

        return gridBuffer.toString();
    }

    private void addNavigationElements(StringBuffer gridBuffer, int rowCount, boolean hasResults, int colspan, int rowIndex, int colIndex) {

        // close table
        gridBuffer.append(displayPropertiesManager.getDataTableEnd());

        // add more and drill down links if appropriate
        if (hasResults) {

            gridBuffer.append(MessageFormat.format(displayPropertiesManager.getNavigationTableStart(), String.valueOf(colspan)));

            appendMoreLink(gridBuffer, rowCount, rowIndex, colIndex);
            appendDrillDownLink(gridBuffer, rowIndex, colIndex);

            gridBuffer.append(displayPropertiesManager.getNavigationTableEnd());
        }

        // close cell
        gridBuffer.append(displayPropertiesManager.getCellEnd());
    }

    /**
     * Appends more link if display limit has been set and row count is greater than display limit and has results is true.
     * @param gridBuffer
     * @param rowCount
     * @param rowIndex
     * @param colIndex
     */
    private void appendMoreLink(StringBuffer gridBuffer, int rowCount, int rowIndex, int colIndex) {

        // include more link if display limit has been set and row count greater than display limit
        Integer displayLimit = ((CrossTabReport) report).getDisplayLimit();
        if(displayLimit != null && rowCount > displayLimit.intValue()) {
            String tableId = getTableId(rowIndex, colIndex);
            gridBuffer.append(MessageFormat.format(displayPropertiesManager.getMoreLinkFormat(), tableId));
        }
    }

    private String getTableId(int rowIndex, int colIndex) {
        return JavaScriptUtils.javaScriptEscape("a" + getRowHeading(rowIndex).getId() + rowIndex + "_" + getColumnHeading(colIndex) + colIndex);
    }

    /**
     * Append link for drill down.
     * @param gridBuffer
     * @param rowIndex
     * @param colIndex
     */
    private void appendDrillDownLink(StringBuffer gridBuffer, int rowIndex, int colIndex) {

        if (StringUtils.isNotBlank(drillDownUrl)) {
            Heading rowHeading = getRowHeading(rowIndex);
            Heading columnHeading = getColumnHeading(colIndex);
            Map<String, Object> urlParams = new HashMap<String, Object>();

            urlParams.put(ReportConstants.HORIZONTAL_ATTR_VALUE, columnHeading.getId());
            urlParams.put(ReportConstants.HORIZONTAL_ATTR_LABEL, columnHeading.getLabel());

            urlParams.put(ReportConstants.VERTICAL_ATTR_VALUE, rowHeading.getId());
            urlParams.put(ReportConstants.VERTICAL_ATTR_LABEL, rowHeading.getLabel());

            String cellDrillDown = ZynapWebUtils.buildURL(drillDownUrl, urlParams, false);
            gridBuffer.append(MessageFormat.format(displayPropertiesManager.getDrillDownLinkFormat(), cellDrillDown, drillDownAltText));
        }
    }

    private Heading getColumnHeading(int colIndex) {
        return (Heading) columnHeadings.get(colIndex);
    }

    private Heading getRowHeading(int rowIndex) {
        return ((Row) rowHeadings.get(rowIndex)).getHeading();
    }

    /**
     * Starts an inner cell data row, td's the info, blank, text or an image and then closes the row.
     * <br/> This is because we do not display a drilldown url if there is no data to display
     *
     * @param gridCells
     * @param gridBuffer
     * @param rowCount
     *  @return true if the info within the table data cells is more then a "-", " " or null
     */
    private boolean appendData(JRExporterGridCell[] gridCells, StringBuffer gridBuffer, int rowCount) {

        // only display hideable row start if display limit is set and row count is greater than or equal to display limit
        Integer displayLimit = ((CrossTabReport) report).getDisplayLimit();
        if(displayLimit != null && rowCount >= displayLimit.intValue()) {
            gridBuffer.append(displayPropertiesManager.getHideableRowStart());
        } else {
            gridBuffer.append(MessageFormat.format(displayPropertiesManager.getRowStart(), rowCount % 2 == 0 ? "even" : "odd"));
        }

        boolean hasResults = false;
        for (int j = 2; j < gridCells.length; j++) {
            JRExporterGridCell gridCell = gridCells[j];
            JRPrintElement element = gridCell.getElement();

            int columnWidth = 100 / (report.getDisplayReport().getColumns().size() - 2);
            gridBuffer.append(MessageFormat.format(displayPropertiesManager.getCellDataStart(), String.valueOf(columnWidth)));
            if(isSpacingElement(element)) gridBuffer.append(EMPTY_VALUE);

            else if (isPrintElement(element)) {
                JRPrintText printText = ((JRPrintText) element);
                String text = printText.getText();
                if (!hasResults) hasResults = !isBlank(text);
                appendText(text, printText, gridBuffer);

            } else if(isImageElement(element)) {
                // handling an image
                JRPrintImage image = (JRPrintImage) element;
                JRImageRenderer renderer = (JRImageRenderer) image.getRenderer();
                if (renderer != null) {
                    if (!hasResults) hasResults = true;
                    appendImage(image, renderer, gridBuffer);
                }
            }
            gridBuffer.append(displayPropertiesManager.getCellEnd());

        }
        gridBuffer.append(displayPropertiesManager.getRowEnd());
        return hasResults;
    }

    private boolean isBlank(String text) {
        return !Report.hasValue(text) || StringUtils.isBlank(text);
    }

    private boolean isSpacingElement(JRPrintElement element) {
        return (element == null || element instanceof JRPrintRectangle);
    }

    private boolean isImageElement(JRPrintElement element) {
        return (element instanceof JRPrintImage);
    }

    private void appendImage(JRPrintImage image, JRImageRenderer renderer, StringBuffer gridBuffer) {
        String anchorName = image.getAnchorName();
        Object[] arguments = new Object[]{renderer.getImageLocation(), anchorName, anchorName};
        gridBuffer.append(MessageFormat.format(displayPropertiesManager.getCellImageFormat(), arguments));
    }

    private void appendText(String text, JRPrintText printText, StringBuffer gridBuffer) {
        String url = null;
        if (isBlank(text)) {
            text = EMPTY_VALUE;
        } else {
            url = formatLink(printText);
            text = HtmlUtils.htmlEscape(text);
        }
        if (url != null) gridBuffer.append(MessageFormat.format(displayPropertiesManager.getArtefactLinkFormat(), url, text));
        else gridBuffer.append(text);
    }

    private boolean isPrintableElement(JRPrintElement rowElement) {
        return isPrintElement(rowElement) && StringUtils.isNotBlank(((JRPrintText) rowElement).getText());
    }

    private boolean isPrintElement(JRPrintElement element) {
        return element instanceof JRPrintText;
    }

    private String endPageFormat() {
        return displayPropertiesManager.getPageEnd();
    }

    final class CrosstabDisplayPropertiesManager {

        private final PropertiesManager propertiesManager = PropertiesManager.getInstance(CrossTabHtmlExporter.class);

        String getNavigationTableStart() {
            return propertiesManager.getString("navigation.start");
        }

        String getNavigationTableEnd() {
            return propertiesManager.getString("navigation.end");
        }

        String getReportStart() {
            return propertiesManager.getString("report.start");
        }

        String getCellHeadingFormat() {
            return propertiesManager.getString("report.horizontal.row");
        }

        String getRowEnd() {
            return propertiesManager.getString("row.end");
        }

        String getCrosstabRowStartFormat() {
            return propertiesManager.getString("crosstab.row.start");
        }

        String getRowStart() {
            return propertiesManager.getString("row.start");
        }

        String getCellStart() {
            return propertiesManager.getString("cell.start");
        }

        String getCellEnd() {
            return propertiesManager.getString("cell.end");
        }

        String getCellDataStart() {
            return propertiesManager.getString("cell.data.start.cell");
        }

        String getDataTableEnd() {
            return propertiesManager.getString("cell.data.end");
        }

        String getDataTableStart() {
            return propertiesManager.getString("cell.data.start");
        }

        String getArtefactLinkFormat() {
            return propertiesManager.getString("cell.artefact.link.format");
        }

        String getPageEnd() {
            return propertiesManager.getString("page.end");
        }

        String getDrillDownLinkFormat() {
            return propertiesManager.getString("drilldown.link.format");
        }

        String getCellImageFormat() {
            return propertiesManager.getString("cell.image.format");
        }

        String getHtmlHeader() {
            return propertiesManager.getString("html.header");
        }

        String getHtmlFooter() {
            return propertiesManager.getString("html.footer");
        }

        String getHideableRowStart() {
            return propertiesManager.getString("hideable.row.start");
        }

        String getMoreLinkFormat() {
            return propertiesManager.getString("more.link.format");
        }
    }

    private final CrosstabDisplayPropertiesManager displayPropertiesManager = new CrosstabDisplayPropertiesManager();
    private final List columnHeadings;
    private final List rowHeadings;
    private final String drillDownAltText;

    private static final String EMPTY_VALUE = "&nbsp;";
}