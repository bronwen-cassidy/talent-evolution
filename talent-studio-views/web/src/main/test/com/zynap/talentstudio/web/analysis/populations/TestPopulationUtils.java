/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.populations;

import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.organisation.ArtefactAssociation;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.organisation.positions.Position;

import com.zynap.talentstudio.web.organisation.associations.ArtefactAssociationWrapperBean;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TestPopulationUtils extends ZynapMockControllerTest {

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testCreateAssociationPopulation() throws Exception {
        IPopulationEngine engine = (IPopulationEngine) applicationContext.getBean("populationEngine");
        IPositionService positionService = (IPositionService) applicationContext.getBean("positionService");
        IReportService reportService = (IReportService) applicationContext.getBean("reportService");
        Position position = positionService.findByID(new Long(1));
        Collection subjectPrimaryAssociations = position.getSubjectPrimaryAssociations();

        List<ArtefactAssociationWrapperBean> associations = new ArrayList<ArtefactAssociationWrapperBean>(wrapAssociations(subjectPrimaryAssociations));
        Report report = (Report) reportService.findById(new Long(-5));
        Population population = PopulationUtils.createAssociationPopulation(engine, associations, report.getReportType());
        List list = engine.find(population, new Long(0));
        assertNotNull(list);
    }

    public void testCreateAssociationPopulationEmptyAssociations() throws Exception {
        IPopulationEngine engine = (IPopulationEngine) applicationContext.getBean("populationEngine");
        IReportService reportService = (IReportService) applicationContext.getBean("reportService");

        List associations = new ArrayList();
        Report report = (Report) reportService.findById(new Long(-5));
        Population population = PopulationUtils.createAssociationPopulation(engine, associations, report.getReportType());
        List list = engine.find(population, new Long(0));
        assertNotNull(list);
    }

    private List<ArtefactAssociationWrapperBean> wrapAssociations(Collection subjectPrimaryAssociations) {
        List<ArtefactAssociationWrapperBean> result = new ArrayList<ArtefactAssociationWrapperBean>();
        for (Iterator iterator = subjectPrimaryAssociations.iterator(); iterator.hasNext();) {
            ArtefactAssociation artefactAssociation = (ArtefactAssociation) iterator.next();
            result.add(new ArtefactAssociationWrapperBean(artefactAssociation));
        }
        return result;
    }
}