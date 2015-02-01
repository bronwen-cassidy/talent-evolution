/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.jasper;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPRow;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.base.JRBasePrintElement;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.export.JRGridLayout;
import net.sf.jasperreports.engine.export.JRHtmlExporterNature;
import net.sf.jasperreports.engine.fill.JRTemplatePrintRectangle;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.reports.ColumnDisplayImage;
import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.web.analysis.reports.ColumnWrapperBean;
import com.zynap.talentstudio.web.analysis.reports.views.RunReportWrapperBean;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author taulant
 * @version 0.1
 * @since 20-Oct-2008 14:39:24
 */
public class JasperPdfExporter extends JRAbstractExporter {


    public JasperPdfExporter(String header, String headerLabelKey, RunReportWrapperBean reportWrapperBean, ReloadableResourceBundleMessageSource messageSource, HttpServletRequest request) {
        this.jasperPrint = reportWrapperBean.getFilledReport().getJasperPrint();
        this.messageSource = messageSource;
        this.request = request;
        this.reportWrapperBean = reportWrapperBean;
        this.header = header;
        this.headerLabelKey = headerLabelKey;
    }

    /**
     * run export and retrive the bytes for the pdf
     *
     * @return
     * @throws DocumentException
     * @throws IOException
     * @throws JRException
     * @throws TalentStudioException
     */
    public byte[] runPdfExport() throws DocumentException, IOException, JRException, TalentStudioException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        if (isResultSetEmpty(jasperPrint.getPages())) {
            document = new Document(PageSize.A4);
            writer = PdfWriter.getInstance(document, outputStream);
            document.open();
            final Paragraph noResult = new Paragraph(getHeading(getMessage("no.results")));
            noResult.setAlignment(Rectangle.ALIGN_CENTER);
            document.add(noResult);
            document.add(new Paragraph("\n"));

        } else {
            createDocument(jasperPrint.getPages());
            writer = PdfWriter.getInstance(document, outputStream);
            document.open();
            //below three lines force to be zoom at 100 and focus page 1
            document.newPage();
            PdfDestination destination = new PdfDestination(PdfDestination.XYZ, 0, -1, 1);
            writer.setOpenAction(PdfAction.gotoLocalPage(1, destination, writer));


            List<JRPrintPage> pages = jasperPrint.getPages();


            addPageHeader(document);

            for (JRPrintPage page : pages) {

                JRExporterGridCell[][] grid = getGrid(page);

                PdfPTable table = getTable(1);
                table.getDefaultCell().setBorder(Rectangle.BOX);
                int emptyRowCount = 0;
                for (int i = 0; i < grid.length; i++) {
                    JRExporterGridCell[] jrExporterGridCells = grid[i];
                    if (!isNullRow(jrExporterGridCells)) {


                        int oddEvenNo = i - emptyRowCount;
                        Color rowColor = null;
                        if (oddEvenNo > 0) {

                            if (oddEvenNo % 2 == 0) {
                                rowColor = getDataBackgroundEvenColor();
                            } else {
                                rowColor = getDataBackgroundOddColor();
                            }

                        }

                        PdfPTable subTable = getTable(jrExporterGridCells.length);
                        subTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                        subTable.getDefaultCell().setPadding(0);

                        for (int j = 0; j < jrExporterGridCells.length; j++) {
                            JRExporterGridCell jrExporterGridCell = jrExporterGridCells[j];

                            final JRPrintElement jrPrintElement = jrExporterGridCell.getElement();


                            if (jrPrintElement instanceof JRPrintText) {
                                final JRPrintText element = (JRPrintText) jrExporterGridCell.getElement();

                                String style = element.getStyle() != null ? element.getStyle().getName() : "";

                                final String text = element.getText();

                                if (TEMPLATE_HEADER.equalsIgnoreCase(style)) {
                                    final Paragraph paragraph = getHeading(text);
                                    final PdfPCell pCell = new PdfPCell(paragraph);
                                    pCell.setBackgroundColor(getHeaderBackgroundColor());
                                    subTable.addCell(pCell);
                                } else if (TEMPLATE_GROUP.equalsIgnoreCase(style)) {
                                    if (StringUtils.hasText(text)) {
                                        Paragraph paragraph = getGroupHeading(text);
                                        PdfPCell groupHeader = new PdfPCell(paragraph);
                                        groupHeader.setColspan(jrExporterGridCells.length);
                                        groupHeader.setBackgroundColor(getHeaderGroupBackgroundColor());

                                        subTable.addCell(groupHeader);
                                    } else {
                                        //empty header
                                        final PdfPCell pCell = new PdfPCell();
                                        pCell.setBackgroundColor(rowColor);
                                        subTable.addCell(pCell);
                                    }

                                } else {

                                    if (!isAddImageColumn(j, text, subTable, rowColor)) {
                                        final Paragraph paragraph = getText(text);
                                        final PdfPCell pdfPCell = new PdfPCell(paragraph);
                                        pdfPCell.setBackgroundColor(rowColor);
                                        subTable.addCell(pdfPCell);
                                    }
                                }


                            } else if (jrPrintElement instanceof JRTemplatePrintRectangle) {
                                final PdfPCell pCell = new PdfPCell();
                                pCell.setBackgroundColor(rowColor);
                                subTable.addCell(pCell);

                            } else if (jrPrintElement instanceof JRBasePrintElement) {

                            } else {


                                final PdfPCell pCell = new PdfPCell();
                                pCell.setBackgroundColor(rowColor);
                                subTable.addCell(pCell);
                            }
                        }

                        table.getDefaultCell().setPadding(0);
                        subTable.completeRow();

                        clearEmptyRows(subTable);

                        table.addCell(subTable);

                        synchronizeTableMemory(table, i);

                    } else {
                        emptyRowCount++;
                    }
                }
                document.add(table);
            }
        }
        document.close();

        return outputStream.toByteArray();
    }

    private boolean isGroupRow(JRExporterGridCell[] jrelement) {
        for (JRExporterGridCell jrExporterGridCell : jrelement) {
            final JRPrintElement jrPrintElement = jrExporterGridCell.getElement();
            if (jrPrintElement instanceof JRPrintText) {
                JRPrintText element = (JRPrintText) jrPrintElement;
                String style = element.getStyle() != null ? element.getStyle().getName() : "";

                if (TEMPLATE_GROUP.equalsIgnoreCase(style)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isResultSetEmpty(List<JRPrintPage> pages) {
        if (pages.isEmpty()) {
            return true;
        }
        JRPrintPage page = pages.get(0);
        JRExporterGridCell[][] grid = getGrid(page);
        if (grid.length == 0) return true;
        return false;
    }

    private void addPageHeader(Document document) throws DocumentException, IOException, TalentStudioException {

        PdfPTable pageHeader = getTable(3);
        pageHeader.getDefaultCell().setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        pageHeader.setWidths(new float[]{50, 10, 40});

        com.lowagie.text.Image image = getPicture(request, ReportConstants.LOGO_FILE);
        image.scaleToFit(100, 100);
        image.setAlignment(Paragraph.ALIGN_LEFT);
        PdfPCell logo = new PdfPCell(image);

        logo.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        logo.setHorizontalAlignment(com.lowagie.text.Rectangle.ALIGN_MIDDLE);
        pageHeader.addCell(logo);

        PdfPCell emptyCell = new PdfPCell();
        emptyCell.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        pageHeader.addCell(emptyCell);

        Paragraph title = getHeading(reportWrapperBean.getReport().getLabel());
        title.getFont().setSize(16);
        title.setAlignment(Paragraph.ALIGN_RIGHT);

        final PdfPCell titleCell = new PdfPCell(title);
        titleCell.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        titleCell.setHorizontalAlignment(com.lowagie.text.Rectangle.ALIGN_RIGHT);
        titleCell.setHorizontalAlignment(com.lowagie.text.Rectangle.ALIGN_MIDDLE);

        pageHeader.addCell(titleCell);


        emptyCell = new PdfPCell();
        emptyCell.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        pageHeader.addCell(emptyCell);
        pageHeader.completeRow();

        final Paragraph population = getText(new StringBuilder().append(getMessage(headerLabelKey)).append(" ").append(header).toString());
        population.setAlignment(Paragraph.ALIGN_LEFT);
        final PdfPCell populationCell = new PdfPCell(population);
        populationCell.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        populationCell.setColspan(3);
        pageHeader.addCell(populationCell);
        pageHeader.completeRow();

        emptyCell = new PdfPCell();
        emptyCell.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        pageHeader.addCell(emptyCell);
        pageHeader.completeRow();
        pageHeader.completeRow();
        document.add(pageHeader);
        document.add(getNewLine());

    }


    private void synchronizeTableMemory(PdfPTable table, int i) throws DocumentException {
        if (i % 50 == 0) {

            document.add(table);
            table.deleteBodyRows();
            //ensures out of memory does not occur with large tables

        }
    }

    public com.lowagie.text.Image getPicture(HttpServletRequest request, String filename) throws IOException, BadElementException {
        final ServletContext context = request.getSession().getServletContext();
        InputStream imageData = context.getResourceAsStream(filename);
        byte[] imageBytes = new byte[imageData.available()];
        imageData.read(imageBytes);
        final com.lowagie.text.Image image = com.lowagie.text.Image.getInstance(imageBytes);

        return image;
    }

    public boolean isAddImageColumn(int j, String text, PdfPTable subTable, Color rowColor) throws IOException, DocumentException, TalentStudioException {
        final List<ColumnWrapperBean> columnWrapperList = reportWrapperBean.getColumns();
        ColumnWrapperBean columnWrapperBean = columnWrapperList.get(j);

        if (columnWrapperBean.isColorDisplayable()) {
            List<ColumnDisplayImage> images = columnWrapperBean.getColumnDisplayImages();
            for (ColumnDisplayImage image : images) {
                if (image.getLookupValue().getLabel().equalsIgnoreCase(text)) {
                    PdfPTable imageTable = loadColorImage(image.getDisplayImage() + ".gif", rowColor);
                    subTable.getDefaultCell().setBorder(Rectangle.TABLE);
                    subTable.addCell(imageTable);
                    return true;
                }
            }

        }


        return false;
    }


    private void clearEmptyRows(PdfPTable subTable) {
        List<PdfPRow> rows = subTable.getRows();

        for (Iterator i = rows.iterator(); i.hasNext();) {
            PdfPRow row = (PdfPRow) i.next();
            PdfPCell[] cells = row.getCells();
            boolean rowEmpty = true;
            for (PdfPCell cell : cells) {
                if (!isCellEmpty(cell)) {
                    rowEmpty = false;
                }
            }
            if (rowEmpty) {
                i.remove();

            }
        }
    }

    private boolean isCellEmpty(PdfPCell cell) {
        boolean empty = true;
        if (cell != null) {   //avoid null
            if (cell.getTable() != null) {
                empty = false;
            }
            if (cell.getPhrase() != null) {
                empty = false;
            }
            if (!(cell.getChunks() == null || cell.getChunks().size() == 0)) {
                empty = false;
            }
            if (cell.getImage() != null) {
                empty = false;
            }

        }
        return empty;
    }

    private boolean isNullRow(JRExporterGridCell[] jrExporterGridCells) {
        for (JRExporterGridCell jrExporterGridCell : jrExporterGridCells) {

            if (jrExporterGridCell.getElement() instanceof JRPrintText) {
                return false;
            }
        }
        return true;
    }

    private PdfPTable loadColorImage(String colorName, Color rowColor) throws IOException, DocumentException, TalentStudioException {

        final ServletContext context = request.getSession().getServletContext();
        InputStream imageData = context.getResourceAsStream("/images/report/tabular/" + colorName);
        byte[] imageBytes = new byte[imageData.available()];
        imageData.read(imageBytes);

        PdfPTable table = getTable(2);
        final int[] widths = {1, 99};
        table.setWidths(widths);
        final PdfPCell emptyCell = new PdfPCell();
        emptyCell.setPadding(0);
        emptyCell.setBackgroundColor(rowColor);
        emptyCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(emptyCell);

        if (imageBytes.length > 0) {
            final com.lowagie.text.Image image = com.lowagie.text.Image.getInstance(imageBytes);
            image.scaleToFit(10, 10);
            image.setBorder(Rectangle.NO_BORDER);

            final PdfPCell imageCell = new PdfPCell(image);
            imageCell.setBorder(Rectangle.NO_BORDER);
            imageCell.setPadding(0);
            imageCell.setBackgroundColor(rowColor);
            imageCell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            imageCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(imageCell);
        } else {
            final PdfPCell noImageCell = new PdfPCell();
            noImageCell.setBorder(Rectangle.NO_BORDER);
            noImageCell.setPadding(0);
            noImageCell.setBackgroundColor(rowColor);
            noImageCell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            noImageCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(noImageCell);

        }

        return table;
    }

    private void createDocument(List<JRPrintPage> pages) {
        JRPrintPage page = pages.get(0);
        JRExporterGridCell[][] grid = getGrid(page);
        JRExporterGridCell[] jrExporterGridCells = grid[0];
        int colCount = jrExporterGridCells.length;

        if (colCount <= 5) {
            //default portrait
            document = new Document(PageSize.A4);
        } else if (colCount > 5 && colCount < 10) {
            document = new Document(PageSize.A4.rotate());
        } else if (colCount > 10 && colCount < 15) {
            document = new Document(PageSize.A3);
        } else if (colCount > 15 && colCount < 20) {
            document = new Document(PageSize.A2);
        } else {
            document = new Document(PageSize.A0);
        }
    }

    private PdfPTable getTable(int columns) {
        PdfPTable table = new PdfPTable(columns);
        table.setWidthPercentage(100);
        table.setSplitLate(false);

        return table;
    }

    public void exportReport() throws JRException {
        try {
            runPdfExport();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TalentStudioException e) {
            e.printStackTrace();
        }
    }


    public JRExporterGridCell[][] getGrid(JRPrintPage page) {

//        JRHtmlExporterNature nature = new JRHtmlExporterNature(filter, true, true);
        JRHtmlExporterNature nature = JRHtmlExporterNature.getInstance(true);
        JRGridLayout layout =
                new JRGridLayout(
                        nature,
                        page.getElements(),
                        jasperPrint.getPageWidth(),
                        jasperPrint.getPageHeight(),
                        globalOffsetX,
                        globalOffsetY,
                        null //address
                );
        return layout.getGrid();
    }

    private Paragraph getNewLine() {
        return new Paragraph("\n");
    }

    public Paragraph getGroupHeading(String header) {
        return new Paragraph(header, FontFactory.getFont(getHeadingGroupTextFontName(), getHeadingGroupTextSize(), getHeadingGroupTextSizeWeight()));
    }

    public Paragraph getHeading(String header) throws TalentStudioException {
        return new Paragraph(header, FontFactory.getFont(getHeadingTextFontName(), getHeadingTextSize(), getHeadingTextSizeWeight(), getHeaderTextColor()));
    }

    public Paragraph getText(String text) {
        return new Paragraph(text, FontFactory.getFont(getNormalTextFontName(), getNormalTextSize(), getNormalTextSizeWeight()));
    }

    //get font name


    private String getNormalTextFontName() {
        return getMessage("report.tabular.normal.text.font.name");
    }

    private String getHeadingTextFontName() {
        return getMessage("report.tabular.heading.text.font.name");
    }

    private String getHeadingGroupTextFontName() {
        return getMessage("report.tabular.heading.group.text.font.name");
    }

    //get text size weight


    private int getNormalTextSizeWeight() {
        return Integer.parseInt(getMessage("report.tabular.normal.text.size.weight"));
    }

    private int getHeadingTextSizeWeight() {
        return Integer.parseInt(getMessage("report.tabular.heading.text.size.weight"));
    }

    private int getHeadingGroupTextSizeWeight() {
        return Integer.parseInt(getMessage("report.tabular.heading.group.text.size.weight"));
    }


    private int getNormalTextSize() {
        return Integer.parseInt(getMessage("report.tabular.normal.text.size"));
    }

    private int getHeadingTextSize() {
        return Integer.parseInt(getMessage("report.tabular.heading.text.size"));
    }

    private int getHeadingGroupTextSize() {
        return Integer.parseInt(getMessage("report.tabular.heading.group.text.size"));
    }


    private Color getRbgColor(String rbg) throws TalentStudioException {
        String[] rbgtokens = rbg.split(",");
        if (rbgtokens.length < 2) {
            throw new TalentStudioException("PDF RBG color format not valid.");
        }
        return new Color(Integer.parseInt(rbgtokens[0]), Integer.parseInt(rbgtokens[1]), Integer.parseInt(rbgtokens[2]));
    }


    private Color getHeaderGroupBackgroundColor() throws TalentStudioException {
        return getRbgColor(getMessage("report.tabular.header.group.background.color"));
    }

    private Color getHeaderBackgroundColor() throws TalentStudioException {
        return getRbgColor(getMessage("report.tabular.header.background.color"));
    }

    private Color getHeaderTextColor() throws TalentStudioException {
        return getRbgColor(getMessage("report.tabular.header.text.color"));
    }

    private Color getDataBackgroundColor() throws TalentStudioException {
        return getRbgColor(getMessage("report.tabular.data.background.color"));
    }

    private Color getDataBackgroundOddColor() throws TalentStudioException {
        return getRbgColor(getMessage("report.tabular.data.background.color.odd"));
    }

    private Color getDataBackgroundEvenColor() throws TalentStudioException {
        return getRbgColor(getMessage("report.tabular.data.background.color.even"));
    }

    public String getMessage(String key) {

        try {
            return messageSource.getMessage(key, null, request.getLocale()).trim();
        } catch (Exception e) {

        }
        return ""; //should never be here
    }

    public String getMessage(String key, String[] messageArguments) {

        try {
            return messageSource.getMessage(key, messageArguments, request.getLocale()).trim();
        } catch (Exception e) {

        }
        return ""; //should never be here
    }


    private JasperPrint jasperPrint;
    private final String TEMPLATE_HEADER = "templateHeaderStyle";
    private final String TEMPLATE_GROUP = "templateGroupStyle";
    protected static PdfWriter writer = null;
    protected Document document = null;
    ReloadableResourceBundleMessageSource messageSource;
    HttpServletRequest request;
    private RunReportWrapperBean reportWrapperBean;
    private String header;
    private String headerLabelKey;
}
