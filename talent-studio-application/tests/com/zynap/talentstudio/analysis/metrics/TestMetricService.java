package com.zynap.talentstudio.analysis.metrics;

/**
 * User: amark
 * Date: 07-Jun-2005
 * Time: 17:43:20
 */

import com.zynap.domain.IDomainObject;
import com.zynap.domain.admin.User;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.analysis.IAnalysisService;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.MetricReport;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.common.AccessType;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.security.users.IUserService;

import java.util.Collection;
import java.util.List;

public class TestMetricService extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        metricService = (IMetricService) applicationContext.getBean("metricService");
        userService = (IUserService) applicationContext.getBean("userService");
        reportService = (IReportService) applicationContext.getBean("reportService");
        analysisService = (IAnalysisService) applicationContext.getBean("analysisService");
    }

    public void testCreate() throws Exception {

        Metric newMetric = createCountMetric();
        assertNull(newMetric.getAttributeName());

        final IDomainObject found = metricService.findById(newMetric.getId());
        assertEquals(newMetric, found);
    }

    public void testDelete() throws Exception {

        Metric newMetric = createCountMetric();
        final Long newMetricId = newMetric.getId();

        metricService.delete(newMetric);

        try {
            metricService.findById(newMetricId);
            fail("Incorrectly found deleted metric: " + newMetricId);
        } catch (DomainObjectNotFoundException e) {

        }
    }

    public void testFindAll() throws Exception {

        // random pretend user id
        Long userId = new Long(-90);

        // metric created by admin user
        Metric newMetric = createCountMetric();

        // metric is created by admin user - since we have no specified user principal yet we should not find the new metric
        Collection all = metricService.findAll(userId);
        assertFalse(all.contains(newMetric));

        // make public and check again - all public metrics are accessible by anyone
        newMetric.setAccessType(AccessType.PUBLIC_ACCESS.toString());
        metricService.update(newMetric);
        all = metricService.findAll(userId);
        assertTrue(all.contains(newMetric));

        // mark it as private again and find using id of user who created the metric - should find it
        newMetric.setAccessType(AccessType.PRIVATE_ACCESS.toString());
        metricService.update(newMetric);
        final Long creator = newMetric.getUserId();
        all = metricService.findAll(creator);
        assertTrue(all.contains(newMetric));
    }

    public void testFindAllByType() throws Exception {

        final User user = getAdminUser(userService);
        final Long userId = user.getId();

        Metric newMetric = createSumMetric();

        // metric is created by admin user - since we have no specified user principal yet we should not find the new metric
        Collection all = metricService.findAll(userId, newMetric.getArtefactType(), AccessType.PUBLIC_ACCESS.toString());
        assertFalse(all.contains(newMetric));

        all = metricService.findAll(userId, newMetric.getArtefactType(), AccessType.PRIVATE_ACCESS.toString());
        assertTrue(all.contains(newMetric));        

        // should be found as we are now looking as the same user
        all = metricService.findAll(userId);
        assertTrue(all.contains(newMetric));
    }

    public void testFindAllCountNotReturned() throws Exception {

        final User user = getAdminUser(userService);
        final Long userId = user.getId();

        List all = metricService.findAll(userId);
        assertEquals(0, all.size());
    }

    private Metric createSumMetric() throws TalentStudioException {

        Metric newMetric = new Metric("sum metric 1", IPopulationEngine.SUM, "targetDerivedAttributes[20]");
        newMetric.setArtefactType(Node.SUBJECT_UNIT_TYPE_);
        newMetric.setAccessType(AccessType.PRIVATE_ACCESS.toString());
        newMetric.setUserId(ADMINISTRATOR_USER_ID);

        metricService.create(newMetric);
        assertNotNull(newMetric.getId());

        return newMetric;
    }

    public void testMetricInPublicReport() throws Exception {

        final Metric privateMetric = createCountMetric();

        final Population population = (Population) analysisService.findById(new Long(-1));

        final Report report = new MetricReport("No of key position report title", "some description", AccessType.PUBLIC_ACCESS.toString());
        report.setDefaultPopulation(population);
        report.setPopulationType(population.getType());
        report.setUserId(ADMINISTRATOR_USER_ID);
        report.setReportType(Report.METRIC_REPORT);

        final Column column = new Column();
        column.setLabel(privateMetric.getLabel());
        column.setMetric(privateMetric);
        report.addColumn(column);

        // report is public
        reportService.create(report);
        assertTrue(metricService.metricInPublicReport(privateMetric.getId()));

        // set to private and try again
        report.setAccessType(AccessType.PRIVATE_ACCESS.toString());
        reportService.update(report);
        assertFalse(metricService.metricInPublicReport(privateMetric.getId()));
    }

    public void testFindAvailableMetrics() throws Exception {

        final User user = getAdminUser(userService);
        final Long userId = user.getId();

        // add 2 private metrics
        final Metric countMetric = createCountMetric();
        assertTrue(countMetric.isPrivate());
        final Metric sumMetric = createSumMetric();
        assertTrue(sumMetric.isPrivate());

        // list of available metrics should include both
        final Collection availableMetrics = metricService.findAvailableMetrics(userId, sumMetric.getArtefactType());
        assertTrue(availableMetrics.contains(sumMetric));
        assertTrue(availableMetrics.contains(countMetric));
    }

    private Metric createCountMetric() throws TalentStudioException {

        Metric newMetric = new Metric();
        newMetric.setArtefactType(Node.SUBJECT_UNIT_TYPE_);
        newMetric.setLabel("metric 1");
        newMetric.setAccessType(AccessType.PRIVATE_ACCESS.toString());
        newMetric.setOperator(IPopulationEngine.COUNT);
        newMetric.setUserId(ADMINISTRATOR_USER_ID);

        metricService.create(newMetric);
        assertNotNull(newMetric.getId());

        return newMetric;
    }

    private IMetricService metricService;
    private IUserService userService;
    private IReportService reportService;
    private IAnalysisService analysisService;
}
