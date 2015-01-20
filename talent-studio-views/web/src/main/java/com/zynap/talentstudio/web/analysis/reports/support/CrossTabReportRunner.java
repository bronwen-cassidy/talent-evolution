package com.zynap.talentstudio.web.analysis.reports.support;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.BasicAnalysisAttribute;
import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.CrossTabReport;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportUtils;
import com.zynap.talentstudio.analysis.reports.TabularReport;
import com.zynap.talentstudio.analysis.reports.crosstab.CrossTableKey;
import com.zynap.talentstudio.analysis.reports.crosstab.Heading;
import com.zynap.talentstudio.analysis.reports.crosstab.Row;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.IOrganisationUnitService;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.web.analysis.reports.data.FilledReport;
import com.zynap.talentstudio.web.analysis.reports.managers.CrossTabReportFiller;
import com.zynap.talentstudio.web.analysis.reports.views.RunCrossTabReportWrapper;
import com.zynap.talentstudio.web.analysis.reports.views.RunReportWrapperBean;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * User: amark
 * Date: 01-Mar-2006
 * Time: 10:21:00
 */
public final class CrossTabReportRunner extends ReportRunner {

    public void run(RunReportWrapperBean wrapper, Long userId) throws TalentStudioException {

        RunCrossTabReportWrapper runCrossTabWrapper = (RunCrossTabReportWrapper) wrapper;

        final CrossTabReport report = (CrossTabReport) runCrossTabWrapper.getReport();

        Collection columnHeaders = getColumnHeaders(report, dynamicAttributeService, organisationUnitService);
        Collection rowHeaders = getRowHeaders(report, dynamicAttributeService, organisationUnitService);

        // these need to be passed to the exporter so that it can set the drilldown info
        runCrossTabWrapper.setHorizontalHeadings(columnHeaders);
        runCrossTabWrapper.setRowHeadings(rowHeaders);

        // get population from wrapper (not from report) as user can change population at run-time
        final Population population = runCrossTabWrapper.getPopulation();

        // get metric and decimal places from wrapper (not from report) as user can change metric at run-time
        final Metric metric = runCrossTabWrapper.getMetric();
        final Column verticalColumn = report.getVerticalColumn();
        final Column horizontalColumn = report.getHorizontalColumn();

        final AnalysisParameter rowAttribute = verticalColumn.getAnalysisParameter();
        final AnalysisParameter columnAttribute = horizontalColumn.getAnalysisParameter();
        final Map results = populationEngine.findCrossTab(population, metric, rowAttribute, columnAttribute, userId);

        // default total is zero
        Number totalValue = new Double(0.0);
        // get total
        Object totalResult = CrossTabResultSetFormatter.findValue(results, IPopulationEngine._CROSS_TAB_TOTAL, IPopulationEngine._CROSS_TAB_TOTAL);
        Number total = CrossTabResultSetFormatter.evaluateValue(totalResult);
        if (total != null) totalValue = total;

        // populate the wrapper with the information needed to display the summary table, this includes the total population,
        // the missing vertical and the missing horizontal counts.
        ((RunCrossTabReportWrapper) wrapper).setTotal(totalValue);
        ((RunCrossTabReportWrapper) wrapper).setColumnNaTotal(results.get(new CrossTableKey(IPopulationEngine._CROSS_TAB_TOTAL, null)));
        ((RunCrossTabReportWrapper) wrapper).setRowNaTotal(results.get(new CrossTableKey(null, IPopulationEngine._CROSS_TAB_TOTAL)));

        FilledReport crossTabFilledReport;
        if (runCrossTabWrapper.isArtefactView()) {

            // get display report from wrapper
            final TabularReport displayReport = (TabularReport) runCrossTabWrapper.getDisplayReport();

            // IMPORTANT: use grouping columns taken from display report 
            final Column displayVerticalColumn = displayReport.getVerticalColumn();
            final Column displayHorizontalColumn = displayReport.getHorizontalColumn();

            crossTabFilledReport = runCrossTabViewReport(population, displayVerticalColumn, displayHorizontalColumn, rowHeaders, columnHeaders, displayReport, userId);

        } else {

            final int decimalPlaces = runCrossTabWrapper.getDecimalPlaces();
            crossTabFilledReport = runCrossTabReport(rowHeaders, columnHeaders, decimalPlaces, runCrossTabWrapper, results);
        }

        runCrossTabWrapper.setFilledReport(crossTabFilledReport);
    }

    public void setDynamicAttributeService(IDynamicAttributeService dynamicAttributeService) {
        this.dynamicAttributeService = dynamicAttributeService;
    }

    public void setOrganisationUnitService(IOrganisationUnitService organisationUnitService) {
        this.organisationUnitService = organisationUnitService;
    }

    public void setReportFiller(CrossTabReportFiller reportFiller) {
        this.reportFiller = reportFiller;
    }

    public static boolean checkNumberOfResults(BasicAnalysisAttribute horizontalAttribute, BasicAnalysisAttribute verticalAttribute,
                                               int maxCells, IDynamicAttributeService dynamicAttributeService, IOrganisationUnitService organisationUnitService) throws TalentStudioException {

        Collection columnHeaders = getColumnHeaders(horizontalAttribute, dynamicAttributeService, organisationUnitService);
        Collection rowHeaders = getRowHeaders(verticalAttribute, dynamicAttributeService, organisationUnitService);

        return (columnHeaders.size() * rowHeaders.size() <= maxCells);
    }

    public static Collection getRowHeaders(Report report, IDynamicAttributeService dynamicAttributeService, IOrganisationUnitService organisationUnitService) throws TalentStudioException {
        return getRowHeaders(((CrossTabReport) report).getVerticalColumn(), dynamicAttributeService, organisationUnitService);
    }

    public static Collection getRowHeaders(final BasicAnalysisAttribute verticalAnalysisAttribute, IDynamicAttributeService dynamicAttributeService, IOrganisationUnitService organisationUnitService) throws TalentStudioException {
        CrossTabRowBuilder rowBuilder = new CrossTabRowBuilder();
        return rowBuilder.buildHeadings(verticalAnalysisAttribute, dynamicAttributeService, organisationUnitService);
    }

    public static Collection getColumnHeaders(Report report, IDynamicAttributeService dynamicAttributeService, IOrganisationUnitService organisationUnitService) throws TalentStudioException {
        return getColumnHeaders(((CrossTabReport) report).getHorizontalColumn(), dynamicAttributeService, organisationUnitService);
    }

    public static Collection getColumnHeaders(BasicAnalysisAttribute horizontalAnalysisAttribute, IDynamicAttributeService dynamicAttributeService, IOrganisationUnitService organisationUnitService) throws TalentStudioException {
        CrossTabColumnBuilder columnbuilder = new CrossTabColumnBuilder();
        return columnbuilder.buildHeadings(horizontalAnalysisAttribute, dynamicAttributeService, organisationUnitService);
    }

    private FilledReport runCrossTabViewReport(Population population, Column verticalColumn, Column horizontalColumn,
                                               Collection rowHeaders, Collection columnHeaders, Report displayReport, Long userId) throws TalentStudioException {

        // run report
        final List nodes = populationEngine.find(population, userId);

        // get questionnaire answers if required
        //final List questionnaireAttributes = displayReport.getQuestionnaireAttributes();
        final List<AnalysisParameter> questionnaireAttributes = ReportUtils.getNonAssociatedQuestionnaireAttributes(displayReport);

        Map answers = null;
        if (!questionnaireAttributes.isEmpty() && population.getType().equals(IPopulationEngine.P_SUB_TYPE_)) {
            answers = populationEngine.findQuestionnaireAnswers(questionnaireAttributes, population, userId);
        }

        final AnalysisParameter verticalColumnAttribute = verticalColumn.getAnalysisParameter();
        final AnalysisParameter horizontalColumnAttribute = horizontalColumn.getAnalysisParameter();

        String[] groupedAttributeNames = new String[]{AnalysisAttributeHelper.getName(verticalColumnAttribute), AnalysisAttributeHelper.getName(horizontalColumnAttribute)};

        // fill report
        return reportFiller.fillReport(displayReport, nodes, answers, groupedAttributeNames, rowHeaders, columnHeaders);
    }

    private FilledReport runCrossTabReport(Collection<Row> rowHeaders, Collection<Heading> columnHeaders, int decimalPlaces,
                                           RunCrossTabReportWrapper wrapper, Map crossTabResults) throws TalentStudioException {

        CrossTabResultSetFormatter formatter = new CrossTabResultSetFormatter();
        return formatter.formatResults(crossTabResults, columnHeaders, rowHeaders, decimalPlaces, wrapper);
    }

    private IDynamicAttributeService dynamicAttributeService;
    private IOrganisationUnitService organisationUnitService;
    private CrossTabReportFiller reportFiller;

    private static abstract class DefaultCrossTabBuilder {

        public Collection<Object> buildHeadings(BasicAnalysisAttribute analysisAttribute, IDynamicAttributeService service, IOrganisationUnitService organisationService) throws TalentStudioException {
            Collection<Object> horizontalHeaders;

            final AnalysisParameter analysisParameter = analysisAttribute.getAnalysisParameter();
            if (analysisParameter.isDynamicAttribute()) {
                final Long id = Long.valueOf(analysisParameter.getDynamicAttributeId());
                DynamicAttribute target = service.findById(id);
                horizontalHeaders = wrapValues(target);
            } else {
                Collection orgUnits = organisationService.findAllSecure();
                horizontalHeaders = wrapObjects(orgUnits);
            }

            //horizontalHeaders.add(getValue(null, ReportConstants.NA));

            return horizontalHeaders;
        }

        protected Collection<Object> wrapObjects(Collection units) {
            Collection<Object> result = new LinkedList<Object>();
            for (Iterator iterator = units.iterator(); iterator.hasNext();) {
                OrganisationUnit orgunit = (OrganisationUnit) iterator.next();
                String id = orgunit.getId().toString();
                String label = orgunit.getLabel();
                result.add(getValue(id, label));
            }
            return result;
        }

        protected Collection<Object> wrapValues(DynamicAttribute dynamicAttribute) {
            Collection<LookupValue> values = dynamicAttribute.getActiveLookupValues();
            Collection<Object> result = new LinkedList<Object>();
            for (Iterator iterator = values.iterator(); iterator.hasNext();) {
                LookupValue lookupValue = (LookupValue) iterator.next();
                result.add(getValue(lookupValue.getId().toString(), lookupValue.getLabel()));
            }
            return result;
        }

        protected abstract Object getValue(String id, String label);
    }

    private static class CrossTabColumnBuilder extends DefaultCrossTabBuilder {

        protected Object getValue(String id, String label) {
            return new Heading(id, label);
        }
    }

    private static class CrossTabRowBuilder extends DefaultCrossTabBuilder {

        protected Object getValue(String id, String label) {
            return new Row(new Heading(id, label));
        }
    }
}
