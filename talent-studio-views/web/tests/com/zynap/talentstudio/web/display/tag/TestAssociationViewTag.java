package com.zynap.talentstudio.web.display.tag;

/**
 * User: amark
 * Date: 15-Sep-2006
 * Time: 12:32:59
 */

import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.crosstab.Cell;
import com.zynap.talentstudio.analysis.reports.crosstab.Row;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;

import com.zynap.talentstudio.web.organisation.associations.ArtefactAssociationWrapperBean;
import com.zynap.talentstudio.web.organisation.positions.PositionWrapperBean;
import com.zynap.talentstudio.web.organisation.subjects.SubjectWrapperBean;
import com.zynap.talentstudio.web.utils.ZynapDBunitMockTagLibTest;
import com.zynap.talentstudio.web.display.support.ArtefactViewQuestionnaireHelper;

import javax.servlet.jsp.tagext.TagSupport;

import java.util.Collection;
import java.util.List;

public class TestAssociationViewTag extends ZynapDBunitMockTagLibTest {

    protected void setUp() throws Exception {
        super.setUp();

        subjectService = (ISubjectService) getBean("subjectService");
        reportService = (IReportService) getBean("reportService");
        positionService = (IPositionService) getBean("positionService");
    }

    protected TagSupport getTabLibrary() throws Exception {
        associationViewTag = new AssociationViewTag();
        final ArtefactViewQuestionnaireHelper displayHelper = new ArtefactViewQuestionnaireHelper((IPopulationEngine) getBean("populationEngine"));
        displayHelper.setDynamicAttributeService((IDynamicAttributeService) getBean("dynamicAttrService"));
        associationViewTag.setDisplayHelper(displayHelper);
        return associationViewTag;
    }

    protected String getDataSetFileName() {
        return "test-data.xml";
    }

    public void testBuildRows() throws Exception {

        final Report report = (Report) reportService.findById(CURRENT_JOBS_REPORT_ID);
        final List columns = report.getColumns();

        final Subject subject = subjectService.findById(SUBJECT_ID);
        final SubjectWrapperBean subjectWrapperBean = new SubjectWrapperBean(subject);

        final List<ArtefactAssociationWrapperBean> subjectPrimaryAssociations = subjectWrapperBean.getSubjectPrimaryAssociations();
        final Collection rows = associationViewTag.buildRows(report, null, subjectPrimaryAssociations, true);

        // only 1 row expected
        assertEquals(1, rows.size());
        final Row row = (Row) rows.iterator().next();

        int index = 0;
        final ArtefactAssociationWrapperBean artefactAssociationWrapper = subjectPrimaryAssociations.get(index);
        assertEquals(columns.size(), row.getCells().size());

        final Cell typeCell = row.getCell(index);
        final String value = artefactAssociationWrapper.getQualifier().getLabel();
        assertEquals(value, typeCell, columns, index);

        index = 1;
        final Cell currentJobCell = row.getCell(index);
        final Position target = (Position) artefactAssociationWrapper.getTarget();
        assertEquals(target.getLabel(), currentJobCell, columns, index);

        index = 2;
        final Cell orgUnitCell = row.getCell(index);
        assertEquals(target.getOrganisationUnit().getLabel(), orgUnitCell, columns, index);
    }

    public void testIncumbentsView() throws Exception {

        final Report report = (Report) reportService.findById(CURRENT_HOLDER_REPORT_ID);
        final Position position = positionService.findByID(DEFAULT_POSITION_ID);
        final PositionWrapperBean positionWrapperBean = new PositionWrapperBean(position);

        final List<ArtefactAssociationWrapperBean> subjectPrimaryAssociations = positionWrapperBean.getSubjectPrimaryAssociations();
        final Collection rows = associationViewTag.buildRows(report, null, subjectPrimaryAssociations, true);

        // only 1 row expected
        assertEquals(1, rows.size());
        final Row row = (Row) rows.iterator().next();
        final List cells = row.getCells();
        final List columns = report.getColumns();
        assertEquals(columns.size(), cells.size());

        final ArtefactAssociationWrapperBean artefactAssociationWrapper = subjectPrimaryAssociations.get(0);

        int index = 0;
        final Cell typeCell = row.getCell(index);
        assertEquals(artefactAssociationWrapper.getQualifier().getLabel(), typeCell, columns, index);
        assertNull(typeCell.getNodeExtendedAttributeId());

        index = 1;
        final Cell holderCell = row.getCell(index);
        final Subject subject = (Subject) artefactAssociationWrapper.getSource();
        assertEquals(subject.getLabel(), holderCell, columns, index);
        assertNull(holderCell.getNodeExtendedAttributeId());

        index = 2;
        final Cell dateCell = row.getCell(index);
        assertTrue(dateCell instanceof Cell.DateCell);
        assertEquals("30 Aug 2006", dateCell, columns, index);
        assertEquals(subject, dateCell.getSourceNode());
        assertNotNull(dateCell.getNodeExtendedAttributeId());
        assertEquals(subject, dateCell.getTargetNode());

        index = 3;
        final Cell dateTimeCell = row.getCell(index);
        assertTrue(dateTimeCell instanceof Cell.DefaultCell);
        assertEquals("23 Aug 2006 00:00", dateTimeCell, columns, index);
        assertEquals(subject, dateTimeCell.getSourceNode());
        assertNotNull(dateTimeCell.getNodeExtendedAttributeId());
        assertEquals(subject, dateTimeCell.getTargetNode());

        index = 4;
        final Cell imageCell = row.getCell(index);
        assertTrue(imageCell.isImage());
        assertEquals("dontpanic.gif", imageCell, columns, index);
        assertEquals(subject, imageCell.getSourceNode());
        assertNotNull(imageCell.getNodeExtendedAttributeId());
        assertEquals(subject, imageCell.getTargetNode());

        index = 5;
        final Cell linkCell = row.getCell(index);
        assertTrue(linkCell instanceof Cell.DefaultCell);
        assertEquals("http://www.yahoo.co.uk", linkCell, columns, index);
        assertEquals(subject, linkCell.getSourceNode());
        assertNotNull(linkCell.getNodeExtendedAttributeId());
        assertEquals(subject, linkCell.getTargetNode());

        index = 6;
        final Cell numberCell = row.getCell(index);
        assertTrue(numberCell instanceof Cell.NumberCell);
        assertEquals("6", numberCell, columns, index);
        assertEquals(subject, numberCell.getSourceNode());
        assertNotNull(numberCell.getNodeExtendedAttributeId());
        assertEquals(subject, numberCell.getTargetNode());

        index = 7;
        final Cell selectionCell = row.getCell(index);
        assertTrue(selectionCell instanceof Cell.DefaultCell);
        assertEquals("Male", selectionCell, columns, index);
        assertEquals(subject, selectionCell.getSourceNode());
        assertNotNull(selectionCell.getNodeExtendedAttributeId());
        assertEquals(subject, selectionCell.getTargetNode());

        index = 8;
        final Cell textCell = row.getCell(index);
        assertTrue(textCell instanceof Cell.DefaultCell);
        assertEquals("textattr", textCell, columns, index);
        assertEquals(subject, textCell.getSourceNode());
        assertNotNull(textCell.getNodeExtendedAttributeId());
        assertEquals(subject, textCell.getTargetNode());

        index = 9;
        final Cell textAreaCell = row.getCell(index);
        assertTrue(textAreaCell instanceof Cell.DefaultCell);
        assertEquals("textblock", textAreaCell, columns, index);
        assertEquals(subject, textAreaCell.getSourceNode());
        assertNotNull(textAreaCell.getNodeExtendedAttributeId());
        assertEquals(subject, textAreaCell.getTargetNode());

        index = 10;
        final Cell timeCell = row.getCell(index);
        assertTrue(timeCell instanceof Cell.DefaultCell);
        assertEquals("02:00", timeCell, columns, index);
        assertEquals(subject, timeCell.getSourceNode());
        assertNotNull(timeCell.getNodeExtendedAttributeId());
        assertEquals(subject, timeCell.getTargetNode());
    }

    public void testReportsToView() throws Exception {

        final Report report = (Report) reportService.findById(REPORTS_TO_REPORT_ID);
        final Position position = positionService.findByID(DEFAULT_POSITION_ID);
        final PositionWrapperBean positionWrapperBean = new PositionWrapperBean(position);

        final List<ArtefactAssociationWrapperBean> primaryTargetAssociations = positionWrapperBean.getPrimaryTargetAssociations();
        final Collection rows = associationViewTag.buildRows(report, null, primaryTargetAssociations, true);

        // only 1 row expected
        assertEquals(1, rows.size());
        final Row row = (Row) rows.iterator().next();
        final ArtefactAssociationWrapperBean artefactAssociationWrapper = primaryTargetAssociations.get(0);
        final List columns = report.getColumns();
        assertEquals(columns.size(), row.getCells().size());

        // check values
        int index = 0;
        final Cell typeCell = row.getCell(index);
        assertEquals(artefactAssociationWrapper.getQualifier().getLabel(), typeCell, columns, index);

        index = 1;
        final Cell currentJobCell = row.getCell(index);
        final Position source = (Position) artefactAssociationWrapper.getSource();
        assertEquals(source.getLabel(), currentJobCell, columns, index);
    }

    public void testSubordinatesView() throws Exception {

        final Report report = (Report) reportService.findById(REPORTS_TO_REPORT_ID);
        final Position position = positionService.findByID(DEFAULT_POSITION_ID);
        final PositionWrapperBean positionWrapperBean = new PositionWrapperBean(position);

        final List<ArtefactAssociationWrapperBean> primaryTargetAssociations = positionWrapperBean.getPrimaryTargetAssociations();
        final Collection rows = associationViewTag.buildRows(report, null, primaryTargetAssociations, false);

        // only 1 row expected
        assertEquals(1, rows.size());
        final Row row = (Row) rows.iterator().next();
        final ArtefactAssociationWrapperBean artefactAssociationWrapper = primaryTargetAssociations.get(0);
        final List columns = report.getColumns();
        assertEquals(columns.size(), row.getCells().size());

        // check values
        int index = 0;
        final Cell typeCell = row.getCell(index);
        assertEquals(artefactAssociationWrapper.getQualifier().getLabel(), typeCell, columns, index);

        index = 1;
        final Cell currentJobCell = row.getCell(index);
        final Position target = (Position) artefactAssociationWrapper.getTarget();
        assertEquals(target.getLabel(), currentJobCell, columns, index);
    }

    private void assertEquals(final String value, final Cell cell, final List columns, int index) {

        assertEquals(value, cell.getDisplayValue());

        final Column column = (Column) columns.get(index);
        assertEquals(cell.getLabel(), column.getLabel());

        if (cell.getDynamicAttribute() == null || cell.getDynamicAttribute().getId() == null) assertNull(cell.getNodeExtendedAttributeId());
    }

    private IReportService reportService;
    private ISubjectService subjectService;
    private IPositionService positionService;
    private AssociationViewTag associationViewTag;

    private static final Long CURRENT_JOBS_REPORT_ID = new Long(-100);
    private static final Long CURRENT_HOLDER_REPORT_ID = new Long(-105);
    private static final Long REPORTS_TO_REPORT_ID = new Long(-3);

    private static final Long SUBJECT_ID = new Long(141);
}