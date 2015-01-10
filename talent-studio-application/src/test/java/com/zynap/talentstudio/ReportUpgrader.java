package com.zynap.talentstudio;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.metrics.IMetricService;
import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.ColumnDisplayImage;
import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.TabularReport;
import com.zynap.talentstudio.arenas.MenuItem;
import com.zynap.talentstudio.arenas.MenuSection;
import com.zynap.talentstudio.arenas.MenuSectionPK;
import com.zynap.talentstudio.calculations.Calculation;
import com.zynap.talentstudio.calculations.Expression;
import com.zynap.talentstudio.calculations.MixedCalculation;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.security.permits.AccessPermit;
import com.zynap.talentstudio.security.permits.IPermit;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * User: amark
 * Date: 31-Mar-2006
 * Time: 14:40:19
 * sunserver properties
 * -Dtest.db.url=jdbc:oracle:thin:@ts-uk12:1521:oradb -Dtest.db.username=tsdev -Dtest.db.password=tsdev -Dtest.base.url=http://localhost:7001/talent-studio -Dautonomy.indexport=9001 -Dautonomy.aciport=9002   -Dautonomy.position.database=positiondata -Dautonomy.subject.database=subjectdata -Dautonomy.retries=3 -Dautonomy.connection.timeout=5000 -Dautonomy.searcher=mockAutonomySearcher -Dsender.email=bcassidy@zynap.com -Dtest.webservice.username=webserviceuser -Dtest.webservice.password=webserviceuser -Dsearch.engine=autonomy -Dserver.url= -Dsearch.gateway=autonomyGateway
 * <p/>
 * UK live server properties
 * -Dtest.db.url=jdbc:oracle:thin:@10.83.110.118:1521:ZYNTS01 -Dtest.db.username=TS5 -Dtest.db.password=TS5 -Dtest.base.url=http://localhost:7001/talent-studio -Dautonomy.indexport=9001 -Dautonomy.aciport=9002   -Dautonomy.position.database=positiondata -Dautonomy.subject.database=subjectdata -Dautonomy.retries=3 -Dautonomy.connection.timeout=5000 -Dautonomy.searcher=mockAutonomySearcher -Dsender.email=bcassidy@zynap.com -Dtest.webservice.username=webserviceuser -Dtest.webservice.password=webserviceuser -Dsearch.engine=autonomy -Dserver.url= -Dsearch.gateway=autonomyGateway
 * <p/>
 * US live server properties
 * -Dtest.db.url=jdbc:oracle:thin:@64.14.161.100:1521:talent9 -Dtest.db.username=TS7 -Dtest.db.password=TS7 -Dtest.base.url=http://localhost:7001/talent-studio -Dautonomy.indexport=9001 -Dautonomy.aciport=9002   -Dautonomy.position.database=positiondata -Dautonomy.subject.database=subjectdata -Dautonomy.retries=3 -Dautonomy.connection.timeout=5000 -Dautonomy.searcher=mockAutonomySearcher -Dsender.email=bcassidy@zynap.com -Dtest.webservice.username=webserviceuser -Dtest.webservice.password=webserviceuser -Dsearch.engine=autonomy -Dserver.url= -Dsearch.gateway=autonomyGateway
 * <p/>
 * AZ live database properties
 * -Dtest.db.url=jdbc:oracle:thin:@10.83.110.118:1521:ZYNTS02 -Dtest.db.username=TSAZ -Dtest.db.password=TSAZ -Dtest.base.url=http://localhost:7001/talent-studio -Dautonomy.indexport=9001 -Dautonomy.aciport=9002   -Dautonomy.position.database=positiondata -Dautonomy.subject.database=subjectdata -Dautonomy.retries=3 -Dautonomy.connection.timeout=5000 -Dautonomy.searcher=mockAutonomySearcher -Dsender.email=bcassidy@zynap.com -Dtest.webservice.username=webserviceuser -Dtest.webservice.password=webserviceuser -Dsearch.engine=autonomy -Dserver.url= -Dsearch.gateway=autonomyGateway
 */
public class ReportUpgrader extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        reportService = (IReportService) getBean("reportService");
        metricService = (IMetricService) getBean("metricService");

    }

    protected void tearDown() throws Exception {
        commit();
    }

    public void testCreateCalculations() throws Exception {
        final List<Report> reports = reportService.findAll(Report.TABULAR_REPORT);
        reports.addAll(reportService.findAll(Report.METRIC_REPORT));
        for (Report report : reports) {
            // get the columns as we need to split the formula up
            final List<Column> columns = report.getColumns();
            List<Column> invalidColumns = formatColumns(report, columns);
            // realign the column index number in case we have had to remove an invalid column
            if (!invalidColumns.isEmpty()) {
                columns.removeAll(invalidColumns);
                int index = 0;
                for (Column column : columns) {
                    column.setPosition(new Integer(index++));        
                }
                System.out.println("stop");
            }

            // we will save the report and hopefully also the calculations
            reportService.update(report);
        }
    }

    private List<Column> formatColumns(Report report, List<Column> columns) throws TalentStudioException {
        List<Column> columnsToRemove = new ArrayList<Column>();

        outer: for (Column column : columns) {
            if(column == null) {                
                continue;
            }
            if (column.isFormula()) {

                List<String> tokens = tokenizeFormula(column);
                Calculation calculation = new MixedCalculation();
                Expression expression = new Expression();

                for (Iterator<String> iterator = tokens.iterator(); iterator.hasNext();) {
                    String element = iterator.next();

                    if (AnalysisAttributeHelper.isRightBracket(element)) {
                        expression.setRightBracket(element);
                        if (StringUtils.hasText(expression.getOperator()) || !iterator.hasNext()) {
                            calculation.addExpression(expression);
                            expression = new Expression();
                        }

                    } else if (AnalysisAttributeHelper.isLeftBracket(element)) {
                        expression.setLeftBracket(element);

                    } else if (isQuestionnaireFormulaAttribute(element)) {
                        final AnalysisParameter analysisParameter = AnalysisAttributeHelper.splitQuestionCriteriaId(element);
                        expression.setAttribute(new DynamicAttribute(Long.valueOf(analysisParameter.getDynamicAttributeId())));
                        expression.setQuestionnaireWorkflowId(analysisParameter.getQuestionnaireWorkflowId());

                        if (StringUtils.hasText(expression.getOperator()) || !iterator.hasNext()) {
                            calculation.addExpression(expression);
                            // end of the line create a new expression
                            expression = new Expression();
                        }

                    } else if (AnalysisAttributeHelper.isMathSymbol(element)) {
                        expression.setOperator(element);
                        // end of the line
                        calculation.addExpression(expression);
                        // end of the line create a new expression
                        expression = new Expression();

                    } else {
                        // attribute or metric may be the end of the line ??
                        if (report.isMetricReport()) {
                            final Long metricId = Long.valueOf(element);
                            try {
                                Metric valid = (Metric) metricService.findById(metricId);
                                expression.setMetric(valid);
                            } catch (TalentStudioException e) {
                                columnsToRemove.add(column);
                                continue outer;
                            }
                        } else if (AnalysisAttributeHelper.isDerivedAttribute(element)) {
                            expression.setRefValue(element);
                        }
                        else {
                            expression.setAttribute(new DynamicAttribute(Long.valueOf(element)));
                        }

                        if (StringUtils.hasText(expression.getOperator()) || !iterator.hasNext()) {
                            calculation.addExpression(expression);
                            // end of the line create a new expression
                            expression = new Expression();
                        }
                    }
                }
                column.setCalculation(calculation);
            }
        }
        return columnsToRemove;
    }

    private boolean isQuestionnaireFormulaAttribute(String element) {
        return org.apache.commons.lang.StringUtils.contains(element, AnalysisAttributeHelper.QUESTION_CRITERIA_DELIMITER);
    }

    private List<String> tokenizeFormula(Column column) {
        List<String> tokens = new ArrayList<String>();

        String attributeName = column.getAttributeName();
        if(attributeName != null) {
            StringTokenizer tk = new StringTokenizer(attributeName, " ");

            while (tk.hasMoreElements()) {
                tokens.add((String) tk.nextElement());
            }
        }
        return tokens;
    }

    public void testX() throws Exception {
        execute();
    }

    public void execute() throws Exception {

        testCreateCalculations();
        // load reports
        //noinspection unchecked
        //final List<Report> reports = template.query("select * from reports where rep_type in ('" + Report.TABULAR_REPORT + "','" + Report.METRIC_REPORT + "')", new ReportMapper());
        // only for tabular in this case
        //final List<Report> reports = reportService.findAll(Report.TABULAR_REPORT);

        /*for (Report report : reports) {
            loadReportColumns(report);
            loadMenuItems(report);

            reportService.update(report);
        }*/
    }

//    public void testFind() throws Exception {
//        reportService.findAllStandardReports(getAdminUser(userService).getId());
//        reportService.findAllMetricReports(getAdminUser(userService).getId());
//    }

    private void loadReportColumns(Report report) {

        //noinspection unchecked
        final List<Column> columns = template.query("select * from report_columns where rep_id = ?", new Object[]{report.getId()}, new ReportColumnMapper());

        for (Column column : columns) {
            if (column.isColorDisplayable()) {
                loadColumnDisplayImages(column);
            }
            report.addColumn(column);
        }
    }

    private void loadMenuItems(Report report) {
        //noinspection unchecked
        final List<MenuItem> menuItems = template.query("select * from menu_items where report_id = ?", new Object[]{report.getId()}, new MenuItemMapper());
        for (MenuItem menuItem : menuItems) {
            report.addMenuItem(menuItem);
        }
    }

    private void loadColumnDisplayImages(Column column) {
        template.query("select * from COLUMN_DISPLAY_IMAGES where report_column_id = ?", new Object[]{column.getId()}, new ColumnDisplayImageRowMapper(column));
    }

    private class MenuItemMapper implements RowMapper {

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

            final Long id = rs.getLong("ID");

            final String label = rs.getString("LABEL");
            final int sortOrder = rs.getInt("SORT_ORDER");
            final String url = rs.getString("URL");
            final String desc = rs.getString("DESCRIPTION");
            final String userType = rs.getString("USER_TYPE");

            final Long permitId = rs.getLong("PERMIT_ID");
            IPermit permit = new AccessPermit(permitId, null, true);

            final String sectionId = rs.getString("SECTION_ID");
            final String moduleId = rs.getString("MODULE_ID");
            MenuSection menuSection = new MenuSection(new MenuSectionPK(sectionId, moduleId), null);

            return new MenuItem(id, label, sortOrder, url, desc, userType, permit, null, menuSection);
        }
    }

    private class ReportMapper implements RowMapper {

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

            final Long id = rs.getLong("ID");
            final String scope = rs.getString("ACCESS_TYPE");
            final String type = rs.getString("REP_TYPE");
            final String desc = rs.getString("DESCRIPTION");
            final String label = rs.getString("LABEL");
            final Long populationId = rs.getLong("DEFAULT_POPULATION_ID");
            final Long metricId = getOptionalIdField(rs, "METRIC_ID");
            final Long userId = rs.getLong("USER_ID");
            final String populationType = rs.getString("POPULATION_TYPE");
            final String operator = rs.getString("OPERATOR");
            final Long drillDownReportId = getOptionalIdField(rs, "DD_REPORT_ID");

            Report report = Report.create(id, type);
            //Report report = new Report(id, label, desc, scope);
            report.setLabel(label);
            report.setDescription(desc);
            report.setAccessType(scope);
            report.setReportType(type);
            report.setPopulationType(populationType);
            report.setOperator(operator);
            report.setUserId(userId);

            final Population defaultPopulation = new Population();
            defaultPopulation.setType(populationType);
            defaultPopulation.setId(populationId);
            report.setDefaultPopulation(defaultPopulation);

            if (metricId != null) report.setDefaultMetric(new Metric(metricId));
            if (drillDownReportId != null) report.setDrillDownReport(new TabularReport(drillDownReportId));

            return report;
        }
    }

    private class ColumnDisplayImageRowMapper implements ResultSetExtractor {

        public ColumnDisplayImageRowMapper(Column column) {
            this.column = column;
        }

        public Object extractData(ResultSet rs) throws SQLException, DataAccessException {

            while (rs.next()) {
                final Long id = rs.getLong("ID");
                final String displayImage = rs.getString("DISPLAY_IMAGE");

                final Long lookupValueId = rs.getLong("LOOKUP_VALUE_ID");
                final LookupValue lookupValue = new LookupValue();
                lookupValue.setId(lookupValueId);

                column.addColumnDisplayImage(new ColumnDisplayImage(id, lookupValue, displayImage));
            }

            return null;
        }

        private Column column;
    }

    private class ReportColumnMapper implements RowMapper {

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

            final Long questionnaireWorkflowId = getOptionalIdField(rs, "QUE_WF_ID");
            final Long metricId = getOptionalIdField(rs, "METRIC_ID");

            final String colSource = rs.getString("SOURCE");
            final Long id = rs.getLong("ID");
            final String refValue = rs.getString("REF_VALUE");
            final String label = rs.getString("LABEL");
            final Integer position = rs.getInt("POSITION");
            final String columnType = rs.getString("COLUMN_TYPE");
            final boolean mandatory = isTrue(rs, "MANDATORY");
            final boolean colorDisplayable = isTrue(rs, "IS_COLOR_DISPLAYABLE");
            final String role = rs.getString("ROLE");
            final boolean formula = isTrue(rs, "IS_FORMULA");
            final boolean grouped = isTrue(rs, "IS_GROUPED");

            final Column column = new Column();
            if (questionnaireWorkflowId != null) column.setQuestionnaireWorkflowId(questionnaireWorkflowId);
            if (metricId != null) column.setMetric(new Metric(metricId));
            if (StringUtils.hasText(columnType)) column.setColumnType(columnType);
            if (StringUtils.hasText(role)) column.setRole(role);
            column.setColumnSource(colSource);
            column.setAttributeName(refValue);
            column.setPosition(position);
            column.setMandatory(mandatory);
            column.setColorDisplayable(colorDisplayable);
            column.setFormula(formula);
            column.setId(id);
            column.setLabel(label);
            column.setGrouped(grouped);

            return column;
        }
    }

    private Long getOptionalIdField(ResultSet rs, String columnName) throws SQLException {
        final String temp = rs.getString(columnName);
        return StringUtils.hasText(temp) ? new Long(temp) : null;
    }

    private boolean isTrue(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName).equals("T");
    }

    public static void main(String[] args) throws Exception {
        String host = "ts-uk12";//args[0];
        String username = "tsdev";//args[1];
        String sid = "oradb";//args[2];

        System.out.println(">>>>>>>>>>>>>> host = " + host);
        System.out.println(">>>>>>>>>>>>>> username = " + username);
        System.out.println(">>>>>>>>>>>>>> sid = " + sid);

        System.setProperty("test.db.url", "jdbc:oracle:thin:@" + host + ":1521:" + sid);
        System.setProperty("test.db.username", username);
        System.setProperty("test.db.password", username);
        System.setProperty("test.base.url", "http://localhost:8888/talent-studio");
        System.setProperty("search.gateway", "oracleGateway");
        System.setProperty("server.url", "zynap89");
        System.setProperty("autonomy.indexport", "9001");
        System.setProperty("autonomy.aciport", "9002");
        System.setProperty("autonomy.position.database", "positiondata");
        System.setProperty("autonomy.subject.database", "subjectdata");
        System.setProperty("autonomy.retries", "3");
        System.setProperty("autonomy.connection.timeout", "5000");
        System.setProperty("autonomy.searcher", "mockAutonomySearcher");
        System.setProperty("sender.email", "bcassidy@zynap.com");
        System.setProperty("test.webservice.username", "webserviceuser");
        System.setProperty("test.webservice.password", "webserviceuser");
        System.setProperty("search.engine", "autonomy");

        ReportUpgrader upgrader = new ReportUpgrader();
        upgrader.setUp();
        upgrader.execute();
        upgrader.tearDown();
    }

    private IReportService reportService;
    private IMetricService metricService;
}
