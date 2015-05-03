/* 
 * Copyright (c) TalentScope Ltd. 2008
 * All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfAction;

import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.analysis.reports.crosstab.Cell;
import com.zynap.talentstudio.analysis.reports.crosstab.Row;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.display.DisplayConfig;
import com.zynap.talentstudio.display.IDisplayConfigService;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.organisation.subjects.SubjectPicture;
import com.zynap.talentstudio.web.display.support.ArtefactViewModelBuilder;
import com.zynap.talentstudio.web.display.support.ArtefactViewQuestionnaireHelper;
import com.zynap.talentstudio.web.display.tag.ArtefactViewTag;
import com.zynap.talentstudio.web.organisation.attributes.FormAttribute;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.net.MalformedURLException;

/**
 * build pdf documents for the questionnaires
 *
 * @author taulant.bajraktari
 * @since 08-Sep-2008 13:02:00
 */
public class QuestionnairePdfMakerHelper {


    private Subject subject;

    /**
     * using the jsp, rendering system, the below code does the same as view questionnaire jsp
     * toe create the pdf document the questionnaire wrapper, is required with needed params see below
     *
     * @param user
     * @param questionnaireWrapper - the questionnaire wrapper to display
     * @param messageSource        - interact with messages.properties to get the data
     * @param request              - the request, for the locale
     * @param subjectService
     * @param displayConfigService
     * @param displayHelper        @return byte[] pdf documnent
     * @throws Exception
     */
    public byte[] createPdfDocument(User user, QuestionnaireWrapper questionnaireWrapper, ReloadableResourceBundleMessageSource messageSource, HttpServletRequest request, ISubjectService subjectService, IDisplayConfigService displayConfigService, ArtefactViewQuestionnaireHelper displayHelper, boolean isMyExecutiveSummary) throws Exception {
        this.messageSource = messageSource;
        this.request = request;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        writer.setViewerPreferences(PdfWriter.FitWindow);

        document.open();
        document.newPage();
        PdfDestination destination = new PdfDestination(PdfDestination.XYZ, 0, -1, 1);
        writer.setOpenAction(PdfAction.gotoLocalPage(1, destination, writer));

        PdfPTable layoutTableWithHeader = getTable(1);
        layoutTableWithHeader.getDefaultCell().setBorder(com.lowagie.text.Rectangle.NO_BORDER);

        addPageHeader(layoutTableWithHeader, request, questionnaireWrapper, document);

        PdfPTable layoutTable = getTable(1);
        layoutTable.getDefaultCell().setBorder(PdfPCell.BOX);

        processExecutiveSummary(layoutTable, user, document, request, questionnaireWrapper, subjectService, displayConfigService, displayHelper, isMyExecutiveSummary);

        //process performance review
        processPerformanceReview(layoutTable, questionnaireWrapper);
        processesInfoBox(layoutTable, questionnaireWrapper, subjectService);

        layoutTableWithHeader.addCell(layoutTable);
        document.add(layoutTableWithHeader);


        document.close();

        return outputStream.toByteArray();
    }

    private void addPageHeader(PdfPTable layoutTableWithHeader, HttpServletRequest request, QuestionnaireWrapper questionnaireWrapper, Document document) throws DocumentException, IOException {
        PdfPTable pageHeader = getTable(3);
        pageHeader.getDefaultCell().setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        pageHeader.setWidths(new int[]{60, 10, 30});

        com.lowagie.text.Image image = getPicture(request, ReportConstants.LOGO_FILE);
        image.scaleToFit(100, 100);
        PdfPCell logo = new PdfPCell(image);

        logo.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        logo.setVerticalAlignment(com.lowagie.text.Rectangle.ALIGN_LEFT);
        pageHeader.addCell(logo);


        final PdfPCell emptyCell = new PdfPCell();
        emptyCell.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        pageHeader.addCell(emptyCell);

        Paragraph title = getHeadingTitle(questionnaireWrapper.getQuestionnaireLabel());
        title.getFont().setSize(16);


        title.setAlignment(Paragraph.ALIGN_RIGHT);
        final PdfPCell titleCell = new PdfPCell(title);
        titleCell.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        titleCell.setVerticalAlignment(com.lowagie.text.Rectangle.ALIGN_RIGHT);
        titleCell.setHorizontalAlignment(com.lowagie.text.Rectangle.ALIGN_MIDDLE);
        pageHeader.addCell(titleCell);

        layoutTableWithHeader.addCell(pageHeader);
        layoutTableWithHeader.completeRow();
        layoutTableWithHeader.addCell(getNewLine());
        layoutTableWithHeader.completeRow();

    }

    public Map getAnswers(ArtefactViewQuestionnaireHelper displayHelper, User user, Subject subject, Report report, String viewType) throws TalentStudioException {
        Map answers = new HashMap();
        java.util.List<AnalysisParameter> questionnaireAttributes = report.getQuestionnaireAttributes();
        if (!questionnaireAttributes.isEmpty()) {
            if (ArtefactViewTag.PERSONAL_VIEW.equals(viewType)) {
                answers = displayHelper.getPersonalQuestionnaireAnswers(questionnaireAttributes, subject, user.getId());
            } else {
                answers = displayHelper.getNodeQuestionnaireAnswers(subject, questionnaireAttributes, user.getId());
            }
        }
        return answers;
    }

    private void processExecutiveSummary(PdfPTable layoutTable, User user, Document document, HttpServletRequest request, QuestionnaireWrapper questionnaireWrapper, ISubjectService subjectService, IDisplayConfigService displayConfigService, ArtefactViewQuestionnaireHelper displayHelper, boolean myExecutiveSummary) throws IOException, DocumentException, TalentStudioException {

        /**
         * include image together with executive summary
         */
        PdfPCell pdfPCell;
        //Subject subject;
        PdfPTable executiveSummary = getTable(2);

        Report executiveReport;
        if (myExecutiveSummary) {
            subject = subjectService.findByUserId(user.getId());
            executiveReport = displayConfigService.findDisplayConfigReport(Node.SUBJECT_UNIT_TYPE_, DisplayConfig.MY_EXEC_SUMMARY_TYPE);
        } else {
            subject = subjectService.findById(questionnaireWrapper.getSubjectId());
            executiveReport = displayConfigService.findDisplayConfigReport(Node.SUBJECT_UNIT_TYPE_, DisplayConfig.EXECUTIVE_SUMMARY_TYPE);
        }


        if (subject.isHasPicture()) {
            pdfPCell = getPicture(subjectService, subject);
        } else {
            pdfPCell = displayNoPicture(request);
        }
        executiveSummary.setWidths(new int[]{20, 80});
        executiveSummary.addCell(pdfPCell);


        PdfPTable executiveSummaryData = getTable(4);

        Map answers = getAnswers(displayHelper, user, subject, executiveReport, "");

        Row row = ArtefactViewModelBuilder.buildRow(executiveReport, subject, displayHelper, answers);

        java.util.List<Cell> cells = row.getCells();

        for (Cell cell : cells) {
            pdfPCell = new PdfPCell(getText(cell.getLabel()));
            pdfPCell.setBackgroundColor(getLineItemBackgroundColor());
            executiveSummaryData.addCell(pdfPCell);
            pdfPCell = new PdfPCell(getText(cell.getDisplayValue()));
            pdfPCell.setBackgroundColor(getLineItemBackgroundColor());
            executiveSummaryData.addCell(pdfPCell);
        }

        executiveSummary.addCell(executiveSummaryData);


        layoutTable.addCell(executiveSummary);

    }

    private PdfPCell displayNoPicture(HttpServletRequest request) throws IOException, BadElementException {
        com.lowagie.text.Image image = getPicture(request, "/images/nopicture.jpg");
        image.setBorder(com.lowagie.text.Image.BOX);
        image.setBorderColor(Color.BLACK);
        image.setAlignment(com.lowagie.text.Image.ALIGN_CENTER);
        image.scaleAbsoluteHeight(100);
        image.scaleAbsoluteWidth(100);
        PdfPCell pdfPCell = new PdfPCell(image);
        return pdfPCell;

    }

    public com.lowagie.text.Image getPicture(HttpServletRequest request, String filename) throws IOException, BadElementException {
        final ServletContext context = request.getSession().getServletContext();
        InputStream imageData = context.getResourceAsStream(filename);
        byte[] imageBytes = new byte[imageData.available()];
        imageData.read(imageBytes);
        final com.lowagie.text.Image image = com.lowagie.text.Image.getInstance(imageBytes);

        return image;
    }

    private PdfPCell getPicture(ISubjectService subjectService, Subject subject) throws IOException, BadElementException {
        SubjectPicture subjectPicture = subjectService.findPicture(subject.getId());

        byte[] imageBytes = subjectPicture.getPicture();
        final com.lowagie.text.Image image = com.lowagie.text.Image.getInstance(imageBytes);
        image.setBorder(com.lowagie.text.Image.BOX);
        image.setBorderColor(Color.BLACK);
        image.setAlignment(com.lowagie.text.Image.ALIGN_CENTER);
        image.scaleAbsoluteHeight(100);
        image.scaleAbsoluteWidth(100);
        PdfPCell pdfPCell = new PdfPCell(image);
        return pdfPCell;
    }

    private void processesInfoBox(PdfPTable layoutTable, QuestionnaireWrapper questionnaireWrapper, ISubjectService subjectService) throws DocumentException, TalentStudioException {


        Subject subject = subjectService.findById(questionnaireWrapper.getSubjectId());
        String subjectName = subject.getLabel() == null ? "" : subject.getLabel();
        String modifiedBy = getMessage("username.questionnaire") + " " + subjectName;

        layoutTable.addCell(getHeadingTitle(modifiedBy));


        java.util.List<QuestionGroupWrapper> queGroupWrapperList = questionnaireWrapper.getQuestionnaireGroups();

        PdfPTable questionGroupsTable = getTable(1);
        questionGroupsTable.getDefaultCell().setPadding(5f);
        for (int j = 0; j < queGroupWrapperList.size(); j++) {
            QuestionGroupWrapper queWraperGroup = queGroupWrapperList.get(j);

            final PdfPCell cellheader = new PdfPCell(getSubheadingTitle(queWraperGroup.getLabel()));
            cellheader.setBackgroundColor(getGroupQuestionHeaderBackgroundColor());
            questionGroupsTable.addCell(cellheader);
            questionGroupsTable.completeRow();

            java.util.List<FormAttribute> wrappedDynamicAtt = queWraperGroup.getWrappedDynamicAttributes();
            PdfPTable formAttributesTable = getTable(2);
            questionGroupsTable.addCell(formAttributesTable);

            for (int k = 0; k < wrappedDynamicAtt.size(); k++) {
                FormAttribute frmAttribute = wrappedDynamicAtt.get(k);
                // do not export hidden questions at all
                if (frmAttribute.isHidden()) continue;

                if (!frmAttribute.isEditable()) {
                    // this is a narrative italic coloured rubbish
                    final PdfPCell cell = new PdfPCell(getNarrativeText(frmAttribute.getLabel()));
                    cell.setBackgroundColor(getNarrativeHeaderBackgroundColor());

                    cell.setColspan(2);
                    formAttributesTable.addCell(cell);
                    formAttributesTable.completeRow();

                } else if (frmAttribute.isLineItem()) {
                    LineItemWrapper lineItem = (LineItemWrapper) frmAttribute;
                    final QuestionAttributeWrapperBean[][] attributeWrapperBeans = lineItem.getGrid();
                    PdfPTable lineItemTable = getTable(attributeWrapperBeans[0].length + 1);
                    // added + 1 to the number of columns to cater for the row label

                    addLineItemsTableHeader(lineItem, attributeWrapperBeans, lineItemTable);
                    addLineItemTableRows(formAttributesTable, lineItem, attributeWrapperBeans, lineItemTable);

                    formAttributesTable.getDefaultCell().setColspan(2);
                    formAttributesTable.addCell(lineItemTable);
//
                } else {

                    QuestionAttributeWrapperBean queAttWrapperBean = (QuestionAttributeWrapperBean) frmAttribute;

                    String setManadatoryQuestionString = queAttWrapperBean.getLabel() + " : ";
                    if (queAttWrapperBean.isMandatory()) {
                        setManadatoryQuestionString += "*";
                    }
                    PdfPCell cell = new PdfPCell(getSubheadingTitle(setManadatoryQuestionString));
                    cell.setBackgroundColor(getQuestionsHeaderBackgroundColor());
                    formAttributesTable.addCell(cell);

                    cell = new PdfPCell(getQuestionAnswer(queAttWrapperBean));
                    cell.setBackgroundColor(getQuestionsAnswerBackgroundColor());
                    formAttributesTable.addCell(cell);

                    formAttributesTable.completeRow();
                }
            }
        }
        layoutTable.addCell(questionGroupsTable);
    }


    private void addLineItemTableRows(PdfPTable formAttributesTable, LineItemWrapper lineItem, QuestionAttributeWrapperBean[][] attributeWrapperBeans, PdfPTable lineItemTable) throws DocumentException, TalentStudioException {
        lineItemTable.completeRow();
        for (final QuestionAttributeWrapperBean[] columns : attributeWrapperBeans) {
            processLineItemWrapper(lineItemTable, columns);
            formAttributesTable.completeRow();
        }

    }

    private void addLineItemsTableHeader(LineItemWrapper lineItem, QuestionAttributeWrapperBean[][] attributeWrapperBeans, PdfPTable lineItemTable) throws TalentStudioException {
        String label = "";
        if (lineItem.getLabel() != null) label = lineItem.getLabel();

        PdfPCell cell = new PdfPCell(getLineItemHeader(label));
        cell.setBackgroundColor(getLineItemHeaderBackgroundColor());
        lineItemTable.addCell(cell);

        //do the heading for the questionnaire
        for (QuestionAttributeWrapperBean queAttWrapperBeanHeader : attributeWrapperBeans[0]) {
            String requiredAnswerToQuestion = queAttWrapperBeanHeader.getLabel();
            if (queAttWrapperBeanHeader.isMandatory()) {
                requiredAnswerToQuestion += "*";
            }
            cell = new PdfPCell(getLineItemHeader(requiredAnswerToQuestion));
            cell.setBackgroundColor(getLineItemHeaderBackgroundColor());
            lineItemTable.addCell(cell);
        }
    }


    private PdfPCell getQuestionAnswer(QuestionAttributeWrapperBean queAttWrapperBean) throws DocumentException {
        final String displayValue = queAttWrapperBean.getDisplayValue();

        if (queAttWrapperBean.isMandatory() && !StringUtils.hasText(displayValue)) {
            return new PdfPCell(getText(getMessage("dynamicattribute.missing.value")));
        }
        return new PdfPCell(getText(displayValue));
    }


    private void processLineItemWrapper(PdfPTable lineItemTable, QuestionAttributeWrapperBean[] columns) throws DocumentException, TalentStudioException {

        QuestionAttributeWrapperBean rowQuestionHeader = columns[0];

        PdfPCell cell = new PdfPCell(getSubheadingTitle(rowQuestionHeader.getLineItemLabel()));
        cell.setBackgroundColor(getLineItemBackgroundColor());
        lineItemTable.addCell(cell);

        for (QuestionAttributeWrapperBean queWrapperBean : columns) {
            cell = new PdfPCell(getQuestionAnswer(queWrapperBean));
            cell.setBackgroundColor(getLineItemBackgroundColor());
            lineItemTable.addCell(cell);
        }
        lineItemTable.completeRow();
    }


    private void processPerformanceReview(PdfPTable layoutTable, QuestionnaireWrapper questionnaireWrapper) throws DocumentException, TalentStudioException {
        PdfPTable performanceTable = getTable(1);
        if (questionnaireWrapper.isPerformanceReview()) {
            String roleLabel = "";
            if (questionnaireWrapper.getQuestionnaire().getRole() != null) {
                roleLabel = questionnaireWrapper.getQuestionnaire().getRole().getLabel();
            }

            //do the setting for role label
            if (questionnaireWrapper.getQuestionnaire().isManager()) {
                roleLabel = getMessage("worklist.appraisal.role.manager");
            }
            if (roleLabel == null) {
                roleLabel = getMessage("appraisee");
            }
            String[] titleArgs = new String[3];
            titleArgs[0] = questionnaireWrapper.getQuestionnaireLabel();
            titleArgs[1] = questionnaireWrapper.getQuestionnaire().getSubject().getLabel();
            titleArgs[2] = roleLabel;

            String title = getMessage("questionnaire.detailed.label", titleArgs);

            PdfPCell cellTitle = new PdfPCell(getHeadingTitle(title));
            cellTitle.setBackgroundColor(getNarrativeHeaderBackgroundColor());
            performanceTable.addCell(cellTitle);
            performanceTable.completeRow();

            layoutTable.addCell(performanceTable);

        }
    }

    /**
     * get a table with 100 percentage and width ( number of columns)
     *
     * @param columns
     * @return
     */
    private PdfPTable getTable(int columns) {
        PdfPTable table = new PdfPTable(columns);
        table.setWidthPercentage(100);
        table.setSplitLate(false);
        return table;
    }

    /**
     * new line
     *
     * @return
     */
    private Paragraph getNewLine() {
        return new Paragraph("\n");
    }

    public Paragraph getLineItemHeader(String header) throws TalentStudioException {
        final Font textFont = FontFactory.getFont(getLineItemHeaderTextFontName(), getLineItemHeaderTextSize(), getLineItemHeaderTextSizeWeight(), getLineItemHeaderTextColor());
        return new Paragraph(header, textFont);
    }

    public Paragraph getSubheadingTitle(String header) {
        return new Paragraph(header, FontFactory.getFont(getSubheadingTitleTextFontName(), getSubheadingTitleTextSize(), getSubheadingTitleTextSizeWeight()));
    }


    public Paragraph getHeadingTitle(String header) {
        return new Paragraph(header, FontFactory.getFont(getHeadingTitleTextFontName(), getHeadingTitleTextSize(), getHeadingTitleTextSizeWeight()));
    }

    public Paragraph getText(String text) {
        return new Paragraph(text, FontFactory.getFont(getNormalTextFontName(), getNormalTextSize(), getNormalTextSizeWeight()));
    }

    public Paragraph getNarrativeText(String text) throws TalentStudioException {
        return new Paragraph(text, FontFactory.getFont(getNarrativeTextFontName(), getNarrativeTextSize(), getNarrativeTextSizeWeight(), getNarrativeTextColor()));
    }

    //get font name

    private String getNarrativeTextFontName() {
        return getMessage("narrative.text.font.name");
    }

    private String getNormalTextFontName() {
        return getMessage("normal.text.font.name");
    }

    private String getHeadingTitleTextFontName() {
        return getMessage("heading.title.text.font.name");
    }

    private String getSubheadingTitleTextFontName() {
        return getMessage("subheading.title.text.font.name");
    }

    private String getLineItemHeaderTextFontName() {
        return getMessage("line.item.heading.text.font.name");
    }

    //get text size weight

    private int getNarrativeTextSizeWeight() {
        return Integer.parseInt(getMessage("narrative.text.size.weight"));
    }

    private int getNormalTextSizeWeight() {
        return Integer.parseInt(getMessage("normal.text.size.weight"));
    }

    private int getHeadingTitleTextSizeWeight() {
        return Integer.parseInt(getMessage("heading.title.text.size.weight"));
    }

    private int getSubheadingTitleTextSizeWeight() {
        return Integer.parseInt(getMessage("subheading.title.text.size.weight"));
    }

    private int getLineItemHeaderTextSizeWeight() {
        return Integer.parseInt(getMessage("line.item.heading.text.size.weight"));
    }


    //getText size
    private int getNarrativeTextSize() {
        return Integer.parseInt(getMessage("narrative.text.size"));
    }

    private int getNormalTextSize() {
        return Integer.parseInt(getMessage("normal.text.size"));
    }

    private int getHeadingTitleTextSize() {
        return Integer.parseInt(getMessage("heading.title.text.size"));
    }

    private int getSubheadingTitleTextSize() {
        return Integer.parseInt(getMessage("subheading.title.text.size"));
    }

    private int getLineItemHeaderTextSize() {
        return Integer.parseInt(getMessage("line.item.heading.text.size"));
    }


    private Color getNarrativeHeaderBackgroundColor() throws TalentStudioException {
        return getRbgColor(getMessage("narrative.header.background.color"));
    }

    private Color getRbgColor(String rbg) throws TalentStudioException {
        String[] rbgtokens = rbg.split(",");
        if (rbgtokens.length < 2) {
            throw new TalentStudioException("PDF RBG color format not valid.");
        }
        return new Color(Integer.parseInt(rbgtokens[0]), Integer.parseInt(rbgtokens[1]), Integer.parseInt(rbgtokens[2]));
    }

    private Color getNarrativeTextColor() throws TalentStudioException {
        return getRbgColor(getMessage("narrative.text.color"));
    }

    private Color getLineItemHeaderTextColor() throws TalentStudioException {
        return getRbgColor(getMessage("line.item.header.text.color"));
    }

    private Color getLineItemHeaderBackgroundColor() throws TalentStudioException {
        return getRbgColor(getMessage("line.item.header.background.color"));
    }

    private Color getQuestionsAnswerBackgroundColor() throws TalentStudioException {
        return getRbgColor(getMessage("questions.answer.background.color"));
    }

    private Color getQuestionsHeaderBackgroundColor() throws TalentStudioException {
        return getRbgColor(getMessage("questions.header.background.color"));
    }

    private Color getLineItemBackgroundColor() throws TalentStudioException {
        return getRbgColor(getMessage("line.item.background.color"));
    }

    //get it for group question
    public Color getGroupQuestionHeaderBackgroundColor() throws TalentStudioException {
        return getRbgColor(getMessage("group.question.header.background.color"));
    }

    /**
     * get message from messages.properties
     *
     * @param key
     * @return
     */
    public String getMessage(String key) {

        try {   //only required for testing - remove after
            return messageSource.getMessage(key, null, request.getLocale()).trim();
        } catch (Exception e) {

        }
        return ""; //should never be here
    }

    public String getMessage(String key, String[] messageArguments) {

        try {   //only required for testing - remove after
            return messageSource.getMessage(key, messageArguments, request.getLocale()).trim();
        } catch (Exception e) {

        }
        return ""; //should never be here
    }

    public Subject getSubject() {
        return subject;
    }

    ReloadableResourceBundleMessageSource messageSource;
    HttpServletRequest request;

}
