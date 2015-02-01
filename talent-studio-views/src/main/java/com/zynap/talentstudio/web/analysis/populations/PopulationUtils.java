package com.zynap.talentstudio.web.analysis.populations;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.IAnalysisService;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.populations.PopulationCriteria;
import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.analysis.reports.chart.ChartDataStructure;
import com.zynap.talentstudio.analysis.reports.chart.ChartSlice;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.questionnaires.Questionnaire;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.organisation.associations.ArtefactAssociationWrapperBean;
import com.zynap.talentstudio.web.utils.RequestUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * User: amark
 * Date: 08-Jun-2005
 * Time: 12:42:04
 */
public final class PopulationUtils {

    private PopulationUtils() {
    }

    /**
     * Does the population have people as potential artefacts.
     *
     * @param populationType
     * @return true or false
     */
    public static boolean isPersonPopulation(String populationType) {
        return IPopulationEngine.P_SUB_TYPE_.equals(populationType);
    }

    /**
     * Creates a population with the given list of associations.
     * <br/>
     *
     * @param engine       the populationEngine
     * @param associations a list of {@link ArtefactAssociationWrapperBean} objects
     * @param nodeType     Position node type of a person node type
     * @return a population with criteria which will search only for the given node type
     */
    public static Population createAssociationPopulation(IPopulationEngine engine, List associations, String nodeType) {
        final boolean personPopulation = PopulationUtils.isPersonPopulation(nodeType);
        Population population = personPopulation ? engine.getAllSubjectsPopulation() : engine.getAllPositionsPopulation();
        List nodes = getTargetNodes(associations, personPopulation);
        createCriteria(population, nodes);
        return population;
    }

    public static Population createPersonPopulation(IPopulationEngine engine, List people) {
        final Population population = engine.getAllSubjectsPopulation();
        createCriteria(population, people);
        return population;
    }

    public static Population createPersonPopulation(IPopulationEngine engine, String[] subjectIds) {
        final Population population = engine.getAllSubjectsPopulation();
        for (int i = 0; i < subjectIds.length; i++) {
            String subjectId = subjectIds[i];
            PopulationCriteria criteria = PopulationUtils.buildEqualToCriteria("id", subjectId, i == 0 ? null : IPopulationEngine.OR);
            population.addPopulationCriteria(criteria);
        }
        return population;
    }

    private static void createCriteria(Population population, List people) {
        for (int i = 0; i < people.size(); i++) {
            Node node = (Node) people.get(i);
            PopulationCriteria criteria = PopulationUtils.buildEqualToCriteria("id", node.getId().toString(), i == 0 ? null : IPopulationEngine.OR);
            population.addPopulationCriteria(criteria);
        }
    }

    public static PopulationCriteria buildEqualToCriteria(String key, String value, String operator) {
        PopulationCriteria criteria = new PopulationCriteria();
        criteria.setOperator(operator);
        criteria.setAttributeName(key);
        criteria.setRefValue(value.toString());
        criteria.setComparator(IPopulationEngine.EQ);
        return criteria;
    }

    public static List getTargetNodes(List associations, final boolean personPopulation) {
        return (List) CollectionUtils.collect(associations, new Transformer() {
            public Object transform(Object o) {
                ArtefactAssociationWrapperBean bean = (ArtefactAssociationWrapperBean) o;
                boolean subjectAssociation = bean.isSubjectAssociation();
                final Node source = bean.getSource();
                final Node target = bean.getTarget();
                return subjectAssociation ? (personPopulation ? source : target) : target;
            }
        });
    }

    public static List<Long> getTargetNodeIds(List associations, String nodeType) {
        final boolean personPopulation = isPersonPopulation(nodeType);
        return (List<Long>) CollectionUtils.collect(associations, new Transformer() {
            public Object transform(Object o) {
                ArtefactAssociationWrapperBean bean = (ArtefactAssociationWrapperBean) o;
                boolean subjectAssociation = bean.isSubjectAssociation();
                final Node source = bean.getSource();
                final Node target = bean.getTarget();
                return subjectAssociation ? (personPopulation ? source.getId() : target.getId()) : target.getId();
            }
        });
    }

    /**
     * Adds a criteria to the population when drilling down into another report.
     * <br/> Ignores total attributes.
     *
     * @param originalPopulation
     * @param attributeAsString
     * @param value
     * @param horizontalLabel
     */
    public static void addCriteria(Population originalPopulation, String attributeAsString, String value, String horizontalLabel) {

        // only add the criteria if we are not drilling down from a total cell or row
        if (horizontalLabel != null && horizontalLabel.indexOf(ReportConstants.TOTAL) != -1) {
            // nothing to do
            return;
        }
        if (horizontalLabel != null && horizontalLabel.indexOf(ReportConstants.NA) != -1) {
            // N/A is always an isnull criteria 
            String comparator = IPopulationEngine.ISNULL;
            value = null;
            addPopulationCriteria(originalPopulation, attributeAsString, value, comparator);
            return;
        }
        if (!IPopulationEngine._CROSS_TAB_TOTAL.equals(value)/* && !"null".equals(value)*/) {

            // determine correct operator - if attribute value not supplied use "is null"; otherwise use "equals"
            String comparator = IPopulationEngine.EQ;
            if (!StringUtils.hasText(value) || "null".equals(value)) {
                comparator = IPopulationEngine.ISNULL;

                // NOTE: set value to null so that population engine does not include it in the query
                value = null;
            }
            addPopulationCriteria(originalPopulation, attributeAsString, value, comparator);
        }
    }

    public static void addPopulationCriteria(Population originalPopulation, String attributeAsString, String value, String comparator) {
        // if population already has criterias need to include an AND operator
        final List populationCriterias = originalPopulation.getPopulationCriterias();
        String operator = (populationCriterias == null || populationCriterias.size() == 0) ? null : IPopulationEngine.AND;
        PopulationCriteria criteria = createPopulationCriteria(originalPopulation, attributeAsString, value, comparator, operator);
        originalPopulation.addPopulationCriteria(criteria);
    }

    public static PopulationCriteria createPopulationCriteria(Population originalPopulation, String attributeAsString, String value, String comparator, String operator) {
        // build attribute and add criteria
        final AnalysisParameter attribute = AnalysisAttributeHelper.getAttributeFromName(attributeAsString);
        return new PopulationCriteria(null, IPopulationEngine.OP_TYPE_, value, attribute, operator, comparator, originalPopulation);
    }

    public static Population buildPopulation(Long[] populationIds, Long defaultPopulationId, IAnalysisService analysisService) throws TalentStudioException {

        Population merged = new Population();
        StringBuffer label = new StringBuffer();
        if (populationIds.length > 1) {

            final List<PopulationCriteria> criterias = merged.getPopulationCriterias();
            int criteriaIndex = 0;

            for (int i = 0; i < populationIds.length; i++) {
                Population population = (Population) analysisService.findById(populationIds[i]);
                label.append(population.getLabel());
                if (i < populationIds.length - 1) label.append(" + ");
                if (i == 0) {
                    merged.setType(population.getType());
                    merged.setId(population.getId());
                    merged.setActiveCriteria(population.getActiveCriteria());
                }
                // get the criteria and add them all to merged we need to and the first one
                final List<PopulationCriteria> populationCriterias = population.getPopulationCriterias();
                for (int j = 0; j < populationCriterias.size(); j++) {
                    PopulationCriteria criteria = populationCriterias.get(j);
                    // the first of the next set of criterias we need to add an AND clause to the end of the last criteria
                    if (!criterias.isEmpty() && j == 0) {
                        criteria.setOperator(IPopulationEngine.AND);
                        criteria.setType(IPopulationEngine.OP_TYPE_);
                    }
                    criteria.setPosition(criteriaIndex);
                    merged.addPopulationCriteria(criteria);

                    criteriaIndex++;
                }
            }

            merged.setLabel(label.toString());

        } else {
            // resort to the default behaviour may not be necessary as we add the default population in regardless
            Long populationId = populationIds.length == 1 ? populationIds[0] : defaultPopulationId;
            if (populationId != null) {
                merged = (Population) analysisService.findById(populationId);
            } else return null;
        }

        return merged;
    }

    public static Population buildPopulation(List<Population> populations) throws TalentStudioException {

        Population merged = new Population();
        StringBuffer label = new StringBuffer();
        if (populations.size() > 1) {

            final List<PopulationCriteria> criterias = merged.getPopulationCriterias();
            int criteriaIndex = 0;
            int i = 0;
            for (Population population : populations) {

                label.append(population.getLabel());
                if (i < populations.size() - 1) label.append(" + ");
                if (i == 0) {
                    merged.setType(population.getType());
                    merged.setId(population.getId());
                    merged.setActiveCriteria(population.getActiveCriteria());
                }
                // get the criteria and add them all to merged we need to and the first one
                final List<PopulationCriteria> populationCriterias = population.getPopulationCriterias();
                for (int j = 0; j < populationCriterias.size(); j++) {
                    PopulationCriteria criteria = populationCriterias.get(j);
                    // the first of the next set of criterias we need to add an AND clause to the end of the last criteria
                    if (!criterias.isEmpty() && j == 0) {
                        criteria.setOperator(IPopulationEngine.AND);
                        criteria.setType(IPopulationEngine.OP_TYPE_);
                    }
                    criteria.setPosition(criteriaIndex);
                    merged.addPopulationCriteria(criteria);

                    criteriaIndex++;
                }
                i++;
            }

            merged.setLabel(label.toString());

        }
        return merged;
    }

    public static void addColumnPopulationCriteria(Population population, HttpServletRequest request) throws Exception {

        // horizontal attribute is required
        final String horizontalAttribute = RequestUtils.getRequiredStringParameter(request, ReportConstants.HORIZONTAL_ATTR);
        final String horizontalLabel = request.getParameter(ReportConstants.HORIZONTAL_ATTR_LABEL);

        // value will be blank if drilling down from N/A option
        final String horizontalValue = request.getParameter(ReportConstants.HORIZONTAL_ATTR_VALUE);
        addCriteria(population, horizontalAttribute, horizontalValue, horizontalLabel);

        // vertical is only supplied if drilling down from cross tab report
        final String verticalLabel = request.getParameter(ReportConstants.VERTICAL_ATTR_LABEL);
        if (StringUtils.hasText(verticalLabel)) {
            // must have attribute but value will be blank if drilling down from N/A option
            final String verticalAttribute = RequestUtils.getRequiredStringParameter(request, ReportConstants.VERTICAL_ATTR);
            final String verticalValue = request.getParameter(ReportConstants.VERTICAL_ATTR_VALUE);
            addCriteria(population, verticalAttribute, verticalValue, verticalLabel);
        }
    }

    public static Population buildChartDrillDown(HttpServletRequest request, IPopulationEngine populationEngine, Population originalPopulation) {
        String chartDataId = request.getParameter(ReportConstants.CHART_DATA_ID);
        String personPopulationId = request.getParameter(ParameterConstants.POPULATION_PERSON_ID);
        String[] subjectIdArray;
        if (personPopulationId != null) {
            subjectIdArray = new String[]{personPopulationId};

        } else if (chartDataId != null) {
            // get the chart from the session
            String key = request.getParameter(ReportConstants.CHART_ATTR_VALUE);
            HttpSession session = request.getSession();
            ChartDataStructure cds = (ChartDataStructure) session.getAttribute(chartDataId);

            ChartSlice chartSlice = cds.get(key);
            List<NodeExtendedAttribute> attributeList = chartSlice.getAttributes();
            Set<String> subjectIds = new LinkedHashSet<String>();
            for (NodeExtendedAttribute nodeExtendedAttribute : attributeList) {
                Questionnaire questionnaire = (Questionnaire) nodeExtendedAttribute.getNode();
                Long id = questionnaire.getSubjectId();
                if (id != null) {
                    subjectIds.add(id.toString());
                }
            }
            subjectIdArray = subjectIds.toArray(new String[subjectIds.size()]);

        } else {
            return originalPopulation;

        }

        return PopulationUtils.createPersonPopulation(populationEngine, subjectIdArray);

    }

}
