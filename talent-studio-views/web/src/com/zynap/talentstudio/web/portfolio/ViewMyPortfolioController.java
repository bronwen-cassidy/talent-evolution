/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.portfolio;

import com.zynap.domain.UserSession;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.display.IDisplayConfigService;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.questionnaires.IQuestionnaireService;
import com.zynap.talentstudio.questionnaires.QuestionnaireDTO;
import com.zynap.talentstudio.web.common.exceptions.InvalidSubmitException;
import com.zynap.talentstudio.web.display.support.ArtefactViewQuestionnaireHelper;
import com.zynap.talentstudio.web.organisation.BrowseNodeWrapper;
import com.zynap.talentstudio.web.organisation.NodeWrapperBean;
import com.zynap.talentstudio.web.organisation.SubjectBrowseNodeWrapper;
import com.zynap.talentstudio.web.organisation.subjects.SubjectWrapperBean;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.web.controller.ZynapDefaultFormController;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller is used to display the executive summary and the portfolio items and questionnaires/appraisals for the current person logged in.
 *
 *
 * @author taulant
 * @version 0.1
 * @since 25-Nov-2008 11:24:06
 */
public class ViewMyPortfolioController extends ZynapDefaultFormController {


    protected Object formBackingObject(HttpServletRequest request) throws Exception {


        BrowseNodeWrapper browseNodeWrapper = new SubjectBrowseNodeWrapper(null, null, null);
        final UserSession userSession = ZynapWebUtils.getUserSession(request);

        final Long userId = userSession.getId();
        Subject subject;
        try {
            subject = subjectService.findByUserId(userId);
        } catch (Exception e) {
            throw new InvalidSubmitException(request.getSession(), browseNodeWrapper, request.getRequestURI(), false, getClass().getName());
        }

        browseNodeWrapper.setNodeId(subject.getId());
        browseNodeWrapper.setNodeType(Node.SUBJECT_UNIT_TYPE_);
        MyPortfolioHelper.setDisplayInfo(displayConfigService, browseNodeWrapper, request);
        updateNodeInfo(subject, browseNodeWrapper, userSession);
        return browseNodeWrapper;
    }

    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return super.showForm(request, response, errors);
    }

    protected void updateNodeInfo(final Node currentNode, BrowseNodeWrapper wrapper, UserSession userSession) throws TalentStudioException {

        // given the fact that this node was returned in the list of artefacts we must have access so set flag to true
        currentNode.setHasAccess(true);
        final NodeWrapperBean nodeWrapperBean = createNodeWrapper(currentNode, wrapper);
        wrapper.setNodeWrapper(nodeWrapperBean);
        final Long subjectId = currentNode.getId();
        final Collection<QuestionnaireDTO> questionnaires = questionnaireService.getPersonalPortfolioQuestionnaires(subjectId, userSession.getId());
        SubjectWrapperBean subjectWrapper = (SubjectWrapperBean) wrapper.getNodeWrapper();
        MyPortfolioHelper.assignQuestionnaires(subjectWrapper, questionnaires);

        // browsing my details is a personal view
        subjectWrapper.setPersonalView(true);
    }


    /**
     * Provides the web with informaiton required by the tab library the builds the executive summary, details and associations views.
     * @param request the servet request
     * @param command the command object as returned from this classes formBackingObject method
     * @param errors the errors object which holds any binding errors
     * @return a map of key / value pairs which will be available to the recieving jsp pages.
     * @throws Exception database errors propogated.
     */
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

    protected final NodeWrapperBean createNodeWrapper(Node node, BrowseNodeWrapper wrapper) {

        Subject subject = (Subject) node;
        final SubjectWrapperBean subjectWrapperBean = new SubjectWrapperBean(subject);
        PortfolioItemHelper.filterPersonalPortfolioItems(subjectWrapperBean);
        wrapper.setNodeWrapper(subjectWrapperBean);
        return subjectWrapperBean;
    }

    public void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public void setDisplayConfigService(IDisplayConfigService displayConfigService) {
        this.displayConfigService = displayConfigService;
    }

    public void setQuestionnaireService(IQuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    public void setPositionService(IPositionService positionService) {
        this.positionService = positionService;
    }

    public void setDynamicAttributeService(IDynamicAttributeService dynamicAttributeService) {
        this.dynamicAttributeService = dynamicAttributeService;
    }

    public void setPopulationEngine(IPopulationEngine populationEngine) {
        this.populationEngine = populationEngine;
    }

    private ISubjectService subjectService;
    private IQuestionnaireService questionnaireService;
    private IDisplayConfigService displayConfigService;
    private IPositionService positionService;
    private IDynamicAttributeService dynamicAttributeService;
    private IPopulationEngine populationEngine;


}
