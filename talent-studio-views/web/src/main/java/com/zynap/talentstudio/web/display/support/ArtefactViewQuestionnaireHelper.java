/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.display.support;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.analysis.populations.PopulationUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Facade used by AbstractViewTag to access question answers.
 *
 * @author bcassidy
 */
public class ArtefactViewQuestionnaireHelper implements Serializable {

    public ArtefactViewQuestionnaireHelper(IPopulationEngine populationEngine) {
        this.populationEngine = populationEngine;
    }

    public Map getNodeQuestionnaireAnswers(Node node, List<AnalysisParameter> questionnaireAttributes, Long userId) throws TalentStudioException {
        List<Long> nodeIds = new ArrayList<Long>();
        nodeIds.add(node.getId());
        //Population personPopulation = PopulationUtils.createPersonPopulation(populationEngine, nodes);
        return getQuestionnaireAnswers(questionnaireAttributes, nodeIds, userId);
    }

    public Map getAssociationQuestionnaireAnswers(List<AnalysisParameter> questionnaireAttributes, List associations, Long userId, String reportType) throws TalentStudioException {
        List<Long> nodeIds = PopulationUtils.getTargetNodeIds(associations, reportType);
        //Population population = PopulationUtils.createAssociationPopulation(populationEngine, associations, reportType);
        return getQuestionnaireAnswers(questionnaireAttributes, nodeIds, userId);
    }

    public Map getPersonalQuestionnaireAnswers(List<AnalysisParameter> attributes, Node node, Long userId) throws TalentStudioException {
        return populationEngine.findPersonalQuestionnaireAnswers(attributes, node, userId);
    }

    public boolean checkNodeAccess(Node node) {
        if (node instanceof Position) {
            return positionService.checkNodeViewAccess(node, userId);
        } else if (node instanceof Subject) {
            return subjectService.checkNodeViewAccess(node, userId);
        }
        return false;
    }

    private Map getQuestionnaireAnswers(List<AnalysisParameter> questionnaireAttributes, List<Long> nodeIds, Long userId) throws TalentStudioException {
        return populationEngine.findQuestionnaireAnswers(questionnaireAttributes, nodeIds, userId);
    }


    public void setPositionService(IPositionService positionService) {
        this.positionService = positionService;
    }

    public void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public DynamicAttribute getDynamicAttribute(String nodeType, AnalysisParameter analysisParameter) {

        Collection<DynamicAttribute> attributes = null;
        if(Node.POSITION_UNIT_TYPE_.equals(nodeType)) {
            if(positionDynamicAttributes == null) {
                positionDynamicAttributes = dynamicAttributeService.getAllActiveAttributes(nodeType, true);
            }
            attributes = positionDynamicAttributes;
        } else if(Node.SUBJECT_UNIT_TYPE_.equals(nodeType)) {
            if(subjectDynamicAttributes == null) {
                subjectDynamicAttributes = dynamicAttributeService.getAllActiveAttributes(nodeType, true);
            }
            attributes = subjectDynamicAttributes;
        }
        return AnalysisAttributeHelper.findDynamicAttribute(analysisParameter, attributes);
    }

    public void setDynamicAttributeService(IDynamicAttributeService dynamicAttributeService) {
        this.dynamicAttributeService = dynamicAttributeService;
    }

    private IPopulationEngine populationEngine;
    private IPositionService positionService;
    private ISubjectService subjectService;
    private Collection<DynamicAttribute> positionDynamicAttributes;
    private Collection<DynamicAttribute> subjectDynamicAttributes;
    private Long userId;
    private IDynamicAttributeService dynamicAttributeService;
}
