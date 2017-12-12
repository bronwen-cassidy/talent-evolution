package com.zynap.talentstudio.analysis.populations;

import net.sf.hibernate.SessionFactory;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.Page;
import com.zynap.talentstudio.analysis.QuestionAttributeValuesCollection;
import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.common.SecurityConstants;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.organisation.positions.PositionDto;
import com.zynap.talentstudio.organisation.subjects.SubjectDTO;
import com.zynap.talentstudio.questionnaires.IQueWorkflowService;
import com.zynap.talentstudio.questionnaires.Questionnaire;
import com.zynap.talentstudio.security.permits.IPermit;
import com.zynap.talentstudio.security.permits.IPermitManagerDao;
import com.zynap.talentstudio.util.collections.CollectionUtils;

import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * User: jsueiras
 * Date: 23-Feb-2005
 * Time: 14:10:33
 * <p/>
 * Component that runs population queries.
 */
public class HibernatePopulationEngine implements IPopulationEngine, InitializingBean {

    public void afterPropertiesSet() throws Exception {
        populationResolver = new HibernateSimplePopulationResolver(getPositionViewPermit(), getSubjectViewPermit(), sessionFactory);
    }

    public List<? extends Node> find(Population population, Long userId) throws TalentStudioException {
        return populationResolver.find(population, userId);
    }

    public List<SubjectDTO> findSubjects(Population population, Long userId) throws TalentStudioException {
        return populationResolver.findSubjects(population, userId);
    }

    public List<PositionDto> findPositions(Population population, Long userId) throws TalentStudioException {
        return populationResolver.findPositions(population, userId);
    }

    public Page find(Population population, Long userId, int start, int end, int numRecords) throws TalentStudioException {
        return populationResolver.find(population, userId, start, end, numRecords);
    }

    public Map findCrossTab(Population population, Metric metric, AnalysisParameter rowAtt, AnalysisParameter columnAtt, Long userId) throws TalentStudioException {
        return populationResolver.findCrossTab(population, metric, rowAtt, columnAtt, userId);
    }

    public List<NodeExtendedAttribute> findQuestionnaireAnswers(Population population, Long userId, List<AnalysisParameter> attributes) throws TalentStudioException {
        return (List<NodeExtendedAttribute>) CollectionUtils.removeDuplicates(populationResolver.findQuestionnaireAnswers(attributes, population, userId));
    }

    public Map findMetrics(Population population, Collection metrics, AnalysisParameter groupingAttribute, Long userId) throws TalentStudioException {
        return populationResolver.findMetric(population, metrics, groupingAttribute, userId);
    }

    public Map<Long, QuestionAttributeValuesCollection> findQuestionnaireAnswers(List<AnalysisParameter> attributes, Population population, Long userId) throws TalentStudioException {
        final List<NodeExtendedAttribute> answers = findQuestionnaireAnswers(population, userId, attributes);
        return buildQuestionAttributeValueCollections(answers);
    }

    public Map<Long, QuestionAttributeValuesCollection> findQuestionnaireAnswers(List<AnalysisParameter> attributes, List<Long> nodeIds, Long userId) throws TalentStudioException {
        final List<NodeExtendedAttribute> answers = CollectionUtils.removeDuplicates(populationResolver.findQuestionnaireAnswers(attributes, nodeIds, userId));
        return buildQuestionAttributeValueCollections(answers, attributes, nodeIds);
    }

    public Map<Long, QuestionAttributeValuesCollection> findPersonalQuestionnaireAnswers(List<AnalysisParameter> attributes, Node node, Long userId) throws TalentStudioException {
        final List<NodeExtendedAttribute> answers = findPersonalQuestionnaireAttributeAnswers(attributes, node, userId);
        return buildQuestionAttributeValueCollections(answers, attributes, node);
    }

    public List<NodeExtendedAttribute> findPersonalQuestionnaireAttributeAnswers(List<AnalysisParameter> attributes, Node node, Long userId) throws TalentStudioException {
        return CollectionUtils.removeDuplicates(populationResolver.findPersonalQuestionnaireAnswers(attributes, node.getId(), userId));
    }

	public List<NodeExtendedAttribute> findPersonalQuestionnaireAttributeAnswers(List<AnalysisParameter> attributes, List<Long> workflowIds, Node node) throws TalentStudioException {
		return CollectionUtils.removeDuplicates(populationResolver.findPersonalQuestionnaireAnswers(attributes, workflowIds, node.getId()));
	}

    public Population getAllPositionsPopulation() {
        Population p = new Population();
        p.setLabel("All Positions");
        p.setType(IPopulationEngine.P_POS_TYPE_);
        return p;
    }

    public Population getAllSubjectsPopulation() {
        Population p = new Population();
        p.setLabel("All Subjects");
        p.setType(IPopulationEngine.P_SUB_TYPE_);
        return p;
    }

    public void setPermitManagerDao(IPermitManagerDao permitManagerDao) {
        this.permitManagerDao = permitManagerDao;
    }

    public void setDynamicAttributeService(IDynamicAttributeService dynamicAttributeService) {
        this.dynamicAttributeService = dynamicAttributeService;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Map<Long, QuestionAttributeValuesCollection> buildQuestionAttributeValueCollections(List<NodeExtendedAttribute> nodeExtendedAttributes,
                                                                                                List<AnalysisParameter> attributes, List<Long> nodeIds) {
        Map<Long, QuestionAttributeValuesCollection> values = buildQuestionAttributeValueCollections(nodeExtendedAttributes);

        for (Long subjectId : nodeIds) {
            QuestionAttributeValuesCollection value = values.get(subjectId);
            if(value == null) {
                value = new QuestionAttributeValuesCollection();
                values.put(subjectId, value);
            }

            for (AnalysisParameter parameter : attributes) {
                Long workflowId = parameter.getQuestionnaireWorkflowId();
                if (workflowId != null && !value.containsWorflow(workflowId)) {
                    boolean isParticipant = questionnaireWorkflowService.isParticipant(subjectId, workflowId);
                    value.addWorkflowValue(workflowId, isParticipant);
                }
            }

        }
        return values;
    }

    private Map<Long, QuestionAttributeValuesCollection> buildQuestionAttributeValueCollections(List<NodeExtendedAttribute> nodeExtendedAttributes,
                                                                                                List<AnalysisParameter> attributes, Node node) {
        List<Long> nodeIds = new ArrayList<Long>();
        nodeIds.add(node.getId());
        return buildQuestionAttributeValueCollections(nodeExtendedAttributes, attributes, nodeIds);
    }

    /**
     * Build Map based on nodeExtendedAttributes.
     *
     * @param nodeExtendedAttributes the extended (dynamic) attribute answers associated with a node
     * @return Map of QuestionAttributeValuesCollection keyed on subject id.
     */
    private Map<Long, QuestionAttributeValuesCollection> buildQuestionAttributeValueCollections(final List nodeExtendedAttributes) {
        final Map<Long, QuestionAttributeValuesCollection> values = new HashMap<Long, QuestionAttributeValuesCollection>();

        for (int i = 0; i < nodeExtendedAttributes.size(); i++) {
            final NodeExtendedAttribute nodeExtendedAttribute = (NodeExtendedAttribute) nodeExtendedAttributes.get(i);

            final Questionnaire questionnaire = (Questionnaire) nodeExtendedAttribute.getNode();
            final Long subjectId = questionnaire.getSubjectId();
            QuestionAttributeValuesCollection collection = values.get(subjectId);
            if (collection == null) {
                collection = new QuestionAttributeValuesCollection();
                values.put(subjectId, collection);
            }

            // make sure that label is set for node and last updated by question answers
            final DynamicAttribute dynamicAttribute = nodeExtendedAttribute.getDynamicAttribute();
            String nodeLabel = dynamicAttributeService.getDomainObjectLabel(nodeExtendedAttribute.getValue(), dynamicAttribute);

            final String key = AnalysisAttributeHelper.buildQuestionCriteriaId(dynamicAttribute, questionnaire);
            collection.addValue(key, nodeExtendedAttribute, nodeLabel);
        }

        return values;
    }

    private IPermit getPositionViewPermit() {
        if (viewPositionPermit == null) {
            viewPositionPermit = permitManagerDao.getPermit(SecurityConstants.POSITION_CONTENT, SecurityConstants.VIEW_ACTION);
        }
        return viewPositionPermit;
    }

    private IPermit getSubjectViewPermit() {
        if (viewSubjectPermit == null) {
            viewSubjectPermit = permitManagerDao.getPermit(SecurityConstants.SUBJECT_CONTENT, SecurityConstants.VIEW_ACTION);
        }
        return viewSubjectPermit;
    }

    public void setQuestionnaireWorkflowService(IQueWorkflowService questionnaireWorkflowService) {
        this.questionnaireWorkflowService = questionnaireWorkflowService;
    }

    private SessionFactory sessionFactory;

    HibernateSimplePopulationResolver populationResolver;

    private IPermitManagerDao permitManagerDao;
    private IQueWorkflowService questionnaireWorkflowService;
    private IDynamicAttributeService dynamicAttributeService;

    private static IPermit viewPositionPermit;

    private static IPermit viewSubjectPermit;

}
