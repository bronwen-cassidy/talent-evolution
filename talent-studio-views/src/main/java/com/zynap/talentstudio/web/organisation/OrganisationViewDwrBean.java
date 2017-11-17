/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.organisation;

import com.zynap.talentstudio.web.display.support.ArtefactViewBuilder;
import com.zynap.talentstudio.web.display.support.ArtefactViewQuestionnaireHelper;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.exception.TalentStudioException;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 16-May-2007 16:34:02
 */
public class OrganisationViewDwrBean {

    public String getExecutiveSummary(Long nodeId, Long reportId, String imageUrl, boolean checkNodeAccess, String viewPositionUrl,
                                      String viewSubjectUrl, int cellCount, String viewType, Long userId) {
        try {
            Node node = subjectService.findNodeById(nodeId);
            Report report = reportService.findById(reportId);
            ArtefactViewQuestionnaireHelper helper = new ArtefactViewQuestionnaireHelper(populationEngine);
            ArtefactViewBuilder viewBuilder = new ArtefactViewBuilder(imageUrl, imageMessage, noImageMessage, checkNodeAccess, viewPositionUrl, viewSubjectUrl, node);
            return viewBuilder.buildOutput(report, userId, viewType, helper, cellCount, labelStyle, fieldStyle);
            
        } catch (TalentStudioException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public void setReportService(IReportService reportService) {
        this.reportService = reportService;
    }

    public void setPopulationEngine(IPopulationEngine populationEngine) {
        this.populationEngine = populationEngine;
    }

    private ISubjectService subjectService;
    private IReportService reportService;
    private IPopulationEngine populationEngine;

    private final String imageMessage = "Image";
    private final String noImageMessage = "No image provided";

    private String labelStyle = "infolabel";
    private String fieldStyle = "infodata";
}
