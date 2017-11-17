/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.dashboard;

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.dashboard.IDashboardService;
import com.zynap.talentstudio.display.IDisplayConfigService;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.common.exceptions.InvalidSubmitException;
import com.zynap.talentstudio.web.display.support.ArtefactViewQuestionnaireHelper;
import com.zynap.talentstudio.web.organisation.SubjectDashboardBuilder;
import com.zynap.talentstudio.web.organisation.SubjectDashboardWrapper;
import com.zynap.talentstudio.web.organisation.subjects.SubjectWrapperBean;
import com.zynap.talentstudio.web.portfolio.MyPortfolioHelper;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.controller.ZynapDefaultFormController;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 09-Jun-2010 15:00:42
 */
public class ViewMyDashboardController extends ZynapDefaultFormController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        final UserSession userSession = ZynapWebUtils.getUserSession(request);
        MyDashboardWrapper wrapper = new MyDashboardWrapper();
        final Long userId = userSession.getId();
        Subject subject;
        try {
            subject = subjectService.findByUserId(userId);
	        final Set<SubjectDashboardWrapper> subjectDashboardWrappers = dashboardBuilder.buildSubjectDashboards(subject, dashboardService, populationEngine);
	        if (!subjectDashboardWrappers.isEmpty()) {
		        wrapper.setDashboards(subjectDashboardWrappers);
	        }
        } catch (Exception e) {
            throw new InvalidSubmitException(request.getSession(), null, request.getRequestURI(), false, getClass().getName());
        }

        SubjectWrapperBean subjectWrapperBean = createNodeWrapper(subject);
        wrapper.setNodeWrapper(subjectWrapperBean);
        wrapper.setContentView(MyPortfolioHelper.getPersonalExecSummaryViews(displayConfigService, request, Node.SUBJECT_UNIT_TYPE_));

        return wrapper;
    }

	protected SubjectWrapperBean createNodeWrapper(Node node) {

        Subject subject = (Subject) node;
        subject.setHasAccess(true);
        final SubjectWrapperBean subjectWrapperBean = new SubjectWrapperBean(subject);
        subjectWrapperBean.setPersonalView(true);
        return subjectWrapperBean;

    }

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {

        Map<String, Object> refData = new HashMap<String, Object>();
        final Long userId = ZynapWebUtils.getUserId(request);

        ArtefactViewQuestionnaireHelper artefactViewQuestionnaireHelper = new ArtefactViewQuestionnaireHelper(populationEngine);
        artefactViewQuestionnaireHelper.setPositionService(positionService);
        artefactViewQuestionnaireHelper.setSubjectService(subjectService);
        artefactViewQuestionnaireHelper.setUserId(userId);
        artefactViewQuestionnaireHelper.setDynamicAttributeService(dynamicAttributeService);
        refData.put("displayHelper", artefactViewQuestionnaireHelper);
        return refData;

    }


    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return showForm(request, response, errors);
    }

    public void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public void setDashboardService(IDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    public void setDashboardBuilder(SubjectDashboardBuilder dashboardBuilder) {
        this.dashboardBuilder = dashboardBuilder;
    }

    public void setPopulationEngine(IPopulationEngine populationEngine) {
        this.populationEngine = populationEngine;
    }

    public void setDisplayConfigService(IDisplayConfigService displayConfigService) {
        this.displayConfigService = displayConfigService;
    }

    public void setPositionService(IPositionService positionService) {
        this.positionService = positionService;
    }

    public void setDynamicAttributeService(IDynamicAttributeService dynamicAttributeService) {
        this.dynamicAttributeService = dynamicAttributeService;
    }

    private ISubjectService subjectService;
    private IPositionService positionService;
    private IDynamicAttributeService dynamicAttributeService;
    private IDashboardService dashboardService;
    private IPopulationEngine populationEngine;
    private IDisplayConfigService displayConfigService;
    protected SubjectDashboardBuilder dashboardBuilder;
}
