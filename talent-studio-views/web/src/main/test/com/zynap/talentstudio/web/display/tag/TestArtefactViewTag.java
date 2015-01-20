/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.display.tag;

import com.zynap.domain.IDomainObject;
import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.crosstab.Cell;
import com.zynap.talentstudio.analysis.reports.crosstab.Row;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.util.FormatterFactory;

import com.zynap.talentstudio.web.utils.ZynapDBunitMockTagLibTest;
import com.zynap.talentstudio.web.display.support.ArtefactViewQuestionnaireHelper;

import javax.servlet.jsp.tagext.TagSupport;

import java.util.List;
import java.util.HashMap;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TestArtefactViewTag extends ZynapDBunitMockTagLibTest {

    protected void setUp() throws Exception {
        super.setUp();

        subjectService = (ISubjectService) getBean("subjectService");
        reportService = (IReportService) getBean("reportService");
        positionService = (IPositionService) getBean("positionService");
    }

    protected TagSupport getTabLibrary() throws Exception {
        artefactViewTag = new ArtefactViewTag();
        final ArtefactViewQuestionnaireHelper displayHelper = new ArtefactViewQuestionnaireHelper((IPopulationEngine) getBean("populationEngine"));
        displayHelper.setDynamicAttributeService((IDynamicAttributeService) getBean("dynamicAttrService"));
        artefactViewTag.setDisplayHelper(displayHelper);
        return artefactViewTag;
    }

    protected String getDataSetFileName() {
        return "test-data.xml";
    }

    public void testBuildRow() throws Exception {

        final Subject subject = subjectService.findById(SUBJECT_ID);
        final Report report = (Report) reportService.findById(REPORT_ID);

        final Row row = artefactViewTag.buildRow(report, subject, new HashMap());
        assertNotNull(row);

        final List columns = report.getColumns();
        final List cells = row.getCells();
        assertEquals(columns.size(), cells.size());

        int index = 0;
        final Cell titleCell = row.getCell(index);
        assertEquals(subject.getTitle(), titleCell, columns, index);
        assertEquals(subject, titleCell.getSourceNode());
        assertEquals(subject, titleCell.getTargetNode());
        assertNull(titleCell.getNodeExtendedAttributeId());

        index = 1;
        final Cell firstNameCell = row.getCell(index);
        assertTrue(firstNameCell instanceof Cell.DefaultCell);
        assertEquals(subject.getFirstName(), firstNameCell, columns, index);
        assertEquals(subject, firstNameCell.getSourceNode());
        assertNull(firstNameCell.getNodeExtendedAttributeId());
        assertEquals(subject, firstNameCell.getTargetNode());

        index = 2;
        final Cell secondNameCell = row.getCell(index);
        assertTrue(secondNameCell instanceof Cell.DefaultCell);
        assertEquals(subject.getSecondName(), secondNameCell, columns, index);
        assertEquals(subject, secondNameCell.getSourceNode());
        assertNull(secondNameCell.getNodeExtendedAttributeId());
        assertEquals(subject, secondNameCell.getTargetNode());

        index = 3;
        final Cell prefGivenNameCell = row.getCell(index);
        assertTrue(prefGivenNameCell instanceof Cell.DefaultCell);
        assertEquals(subject.getPrefGivenName(), prefGivenNameCell, columns, index);
        assertEquals(subject, prefGivenNameCell.getSourceNode());
        assertNull(prefGivenNameCell.getNodeExtendedAttributeId());
        assertEquals(subject, prefGivenNameCell.getTargetNode());

        index = 4;
        final Cell dobCell = row.getCell(index);
        assertTrue(dobCell instanceof Cell.DateCell);
        assertEquals(FormatterFactory.getDateFormatter().formatDateAsString(subject.getDateOfBirth()), dobCell, columns, index);
        assertEquals(subject, dobCell.getSourceNode());
        assertNull(dobCell.getNodeExtendedAttributeId());
        assertEquals(subject, dobCell.getTargetNode());

        index = 5;
        final Cell currentPositionCell = row.getCell(index);
        assertTrue(currentPositionCell.isList());
        final IDomainObject currentPosition = positionService.findById(DEFAULT_POSITION_ID);
        assertEquals(currentPosition.getLabel(), currentPositionCell, columns, index);
        assertNull(currentPositionCell.getSourceNode());
        assertNull(currentPositionCell.getTargetNode());
        assertNull(currentPositionCell.getNodeExtendedAttributeId());

        index = 6;
        final Cell pictureCell = row.getCell(index);
        assertTrue(pictureCell.isImage());
        assertEquals(ReportConstants.BLANK_VALUE, pictureCell, columns, index);
        assertTrue(pictureCell.isBlank());
        assertEquals(subject, pictureCell.getSourceNode());
        assertNull(pictureCell.getNodeExtendedAttributeId());
        assertEquals(subject, pictureCell.getTargetNode());

        index = 7;
        final Cell dateCell = row.getCell(index);
        assertTrue(dateCell instanceof Cell.DateCell);
        assertEquals("30 Aug 2006", dateCell, columns, index);
        assertEquals(subject, dateCell.getSourceNode());
        assertNotNull(dateCell.getNodeExtendedAttributeId());
        assertEquals(subject, dateCell.getTargetNode());

        index = 8;
        final Cell dateTimeCell = row.getCell(index);
        assertTrue(dateTimeCell instanceof Cell.DefaultCell);
        assertEquals("23 Aug 2006 00:00", dateTimeCell, columns, index);
        assertEquals(subject, dateTimeCell.getSourceNode());
        assertNotNull(dateTimeCell.getNodeExtendedAttributeId());
        assertEquals(subject, dateTimeCell.getTargetNode());

        index = 9;
        final Cell imageCell = row.getCell(index);
        assertTrue(imageCell.isImage());
        assertEquals("dontpanic.gif", imageCell, columns, index);
        assertEquals(subject, imageCell.getSourceNode());
        assertNotNull(imageCell.getNodeExtendedAttributeId());
        assertEquals(subject, imageCell.getTargetNode());

        index = 10;
        final Cell linkCell = row.getCell(index);
        assertTrue(linkCell instanceof Cell.DefaultCell);
        assertEquals("http://www.yahoo.co.uk", linkCell, columns, index);
        assertEquals(subject, linkCell.getSourceNode());
        assertNotNull(linkCell.getNodeExtendedAttributeId());
        assertEquals(subject, linkCell.getTargetNode());

        index = 11;
        final Cell numberCell = row.getCell(index);
        assertTrue(numberCell instanceof Cell.NumberCell);
        assertEquals("6", numberCell, columns, index);
        assertEquals(subject, numberCell.getSourceNode());
        assertNotNull(numberCell.getNodeExtendedAttributeId());
        assertEquals(subject, numberCell.getTargetNode());

        index = 12;
        final Cell selectionCell = row.getCell(index);
        assertTrue(selectionCell instanceof Cell.DefaultCell);
        assertEquals("Male", selectionCell, columns, index);
        assertEquals(subject, selectionCell.getSourceNode());
        assertNotNull(selectionCell.getNodeExtendedAttributeId());
        assertEquals(subject, selectionCell.getTargetNode());

        index = 13;
        final Cell textCell = row.getCell(index);
        assertTrue(textCell instanceof Cell.DefaultCell);
        assertEquals("textattr", textCell, columns, index);
        assertEquals(subject, textCell.getSourceNode());
        assertNotNull(textCell.getNodeExtendedAttributeId());
        assertEquals(subject, textCell.getTargetNode());

        index = 14;
        final Cell textAreaCell = row.getCell(index);
        assertTrue(textAreaCell instanceof Cell.DefaultCell);
        assertEquals("textblock", textAreaCell, columns, index);
        assertEquals(subject, textAreaCell.getSourceNode());
        assertNotNull(textAreaCell.getNodeExtendedAttributeId());
        assertEquals(subject, textAreaCell.getTargetNode());

        index = 15;
        final Cell timeCell = row.getCell(index);
        assertTrue(timeCell instanceof Cell.DefaultCell);
        assertEquals("02:00", timeCell, columns, index);
        assertEquals(subject, timeCell.getSourceNode());
        assertNotNull(timeCell.getNodeExtendedAttributeId());
        assertEquals(subject, timeCell.getTargetNode());
    }

    private void assertEquals(final String value, final Cell cell, final List columns, int index) {

        assertEquals(value, cell.getDisplayValue());

        final Column column = (Column) columns.get(index);
        assertEquals(cell.getLabel(), column.getLabel());

        if (cell.getDynamicAttribute() == null || cell.getDynamicAttribute().getId() == null) assertNull(cell.getNodeExtendedAttributeId());
    }

    private IReportService reportService;
    private ArtefactViewTag artefactViewTag;
    private ISubjectService subjectService;
    private IPositionService positionService;

    private static final Long REPORT_ID = new Long(-99);
    private static final Long SUBJECT_ID = new Long(141);
}