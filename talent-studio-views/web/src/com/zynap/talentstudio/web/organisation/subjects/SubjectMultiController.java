/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.subjects;

import com.zynap.domain.IDomainObject;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.AnalysisService;
import com.zynap.talentstudio.analysis.IAnalysisService;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.common.AccessType;
import com.zynap.talentstudio.integration.adapter.AdaptorResults;
import com.zynap.talentstudio.integration.adapter.IAdapter;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.controller.ZynapMultiActionController;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 16-Nov-2009 11:29:22
 */
public class SubjectMultiController extends ZynapMultiActionController {

    public ModelAndView activateSubject(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Long nodeId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.NODE_ID_PARAM);
        final String url = HistoryHelper.getBackURL(request);
        final Subject subject = subjectService.findById(nodeId);
        subject.setActive(true);
        subjectService.update(subject);

        RedirectView view = new ZynapRedirectView(url);
        view.addStaticAttribute(ParameterConstants.SUBJECT_ID_PARAM, subject.getId());
        return new ModelAndView(view);
    }

    public ModelAndView updateSubjectCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long nodeId = RequestUtils.getLongParameter(request, ParameterConstants.NODE_ID_PARAM);
        try {
            if (nodeId != null) {
                adapter.update(nodeId);
            }
        } catch (TalentStudioException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public ModelAndView synchroniseSubjects(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();

        try {
            List<Node> result = adapter.synchroniseSystems();
            model.put("subjects", result);
        } catch (TalentStudioException e) {
            logger.error("failed to synchronise the systems. ERROR: " + e.getMessage());
        }
        return new ModelAndView("viewsyncresults", "model", model);
    }

    public ModelAndView importSubjects(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        Long populationId = ServletRequestUtils.getLongParameter(request, "populationId");
        Population population = (Population) analysisService.findById(populationId);
        try {
            AdaptorResults adaptorResults = adapter.importPopulation(population);
            model.put("noResults", new Boolean(adaptorResults.getAddedResults().isEmpty() && adaptorResults.getModifiedResults().isEmpty()));
            model.put("added", adaptorResults.getAddedResults());
            model.put("modified", adaptorResults.getModifiedResults());
            model.put("pending", adaptorResults.getPendingResults());
        } catch (TalentStudioException e) {
            model.put("noResults", Boolean.TRUE);
            model.put("errorMsg", e.getMessage());
        }
        return new ModelAndView("viewimportresults", "model", model);
    }

    public ModelAndView exportSubjects(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        Long populationId = ServletRequestUtils.getLongParameter(request, "populationId");
        Population population = (Population) analysisService.findById(populationId);
        try {
            AdaptorResults adaptorResults = adapter.exportPopulation(population);
            model.put("noResults", new Boolean(adaptorResults.getAddedResults().isEmpty() && adaptorResults.getModifiedResults().isEmpty()));
            model.put("added", adaptorResults.getAddedResults());
            model.put("modified", adaptorResults.getModifiedResults());
            model.put("pending", adaptorResults.getPendingResults());
            model.put("errored", adaptorResults.getErroredResults());
        } catch (TalentStudioException e) {
            model.put("noResults", Boolean.TRUE);
            model.put("errorMsg", e.getMessage());
        }
        return new ModelAndView("viewexportresults", "model", model);
    }

    public ModelAndView viewThirdPartyAdaptors(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("populations", analysisService.findAll(Node.SUBJECT_UNIT_TYPE_, ZynapWebUtils.getUserId(request), AccessType.PUBLIC));
        return new ModelAndView("listadaptors", "model", model);
    }

    public void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public void setAdapter(IAdapter adapter) {
        this.adapter = adapter;
    }

    public void setAnalysisService(IAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    private ISubjectService subjectService;
    private IAnalysisService analysisService;
    private IAdapter adapter;
}