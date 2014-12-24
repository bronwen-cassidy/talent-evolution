package com.zynap.talentstudio.analysis.populations;

import net.sf.hibernate.SessionFactory;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.Page;
import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.analysis.reports.GroupingAttribute;
import com.zynap.talentstudio.analysis.reports.crosstab.CrossTableKey;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.security.permits.IPermit;

import org.springframework.orm.hibernate.support.HibernateDaoSupport;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 10-Jun-2005
 * Time: 12:16:53
 */
public abstract class HibernateAbstractResolver extends HibernateDaoSupport {

    protected HibernateAbstractResolver(IPermit positionViewPermit, IPermit subjectViewPermit, SessionFactory sessionFactory) {
        this.positionViewPermit = positionViewPermit;
        this.subjectViewPermit = subjectViewPermit;
        this.hibernateCriteriaResolver = new HibernateCriteriaResolver();
        setSessionFactory(sessionFactory);
    }

    public abstract List findQuestionnaireAnswers(List<AnalysisParameter> attributes, Population population, Long userId) throws TalentStudioException;

    public abstract List findQuestionnaireAnswers(List attributes, List ids, Long userId) throws TalentStudioException;

    public abstract List findPersonalQuestionnaireAnswers(List<AnalysisParameter> attributes, Long nodeId, Long userId) throws TalentStudioException;

    protected abstract List find(Population population, Long userId) throws TalentStudioException;

    protected abstract Page find(Population population, Long userId, int start, int end, int numRecords) throws TalentStudioException;

    protected abstract Map findCrossTab(Population population, Metric metric, AnalysisParameter rowAtt, AnalysisParameter columnAtt, Long userId) throws TalentStudioException;

    protected abstract Map findChart(ChartReport report, Long userId) throws TalentStudioException;

    protected abstract Map findMetric(Population population, Collection<Metric> metrics, AnalysisParameter groupingAttribute, Long userId) throws TalentStudioException;

    final void appendColumnsForGrouping(AnalysisParameter attrs[], StringBuffer sql, String nodePrefix) {
        for (int i = 0; i < attrs.length; i++) {
            sql.append(getHibernateCriteriaResolver().getAttrForSelectClause(attrs[i], nodePrefix, "attr" + i));
            if (i < attrs.length - 1) sql.append(",");
        }
    }

    final void appendOrderByClause(Population population, StringBuffer sql, String nodePrefix) {
        String attrs[] = population.getOrderColumns();
        String orderDirection = population.getOrderDirection();

        if (attrs != null && attrs.length > 0) {
            sql.append(" ORDER BY ");

            // attribute ordering
            for (int i = 0; i < attrs.length; i++) {
                sql.append(nodePrefix).append(".").append(attrs[i]);
                if (i < attrs.length - 1) sql.append(",");
            }
            sql.append(" ").append(orderDirection);
        }
    }

    final void addOrderByClause(Population population, StringBuffer sql, String nodePrefix) {
        final List<GroupingAttribute> orderAttributes = population.getOrderAttributes();
        final List<GroupingAttribute> groups = population.getGroupingAttributes();


        if (!groups.isEmpty() || !orderAttributes.isEmpty()) {
            sql.append(" ORDER BY ");
        }

        int index = 0;
        // group ordering
        for (GroupingAttribute group : groups) {
            String orderDirection = (group.getSortOrder() == -1) ? Population.ORDER_DESCENDING : Population.ORDER_ASC;
            AnalysisParameter analysisParameter = group.getAnalysisParameter();

            if (analysisParameter.isQuestionnaireAttribute()) {
                // all questionnaire grouped attributes have to be a selection type
                addStructQuestionnaireOrderBy(sql, nodePrefix, analysisParameter);
            } else if (analysisParameter.isAssociatedArtefactAttribute()) {
                // only one associatedArtefact comes through here and that is the subjectPrimaryAssociaitons.position.organisationUnit.label!
                addAssociatedArtefactOrderBy(sql, nodePrefix);
            } else if (analysisParameter.isDynamicAttribute()) {

                // all grouping dynamic attributes have to be a struct / selection type
                addStructOrderBy(sql, nodePrefix, analysisParameter);
            } else {
                sql.append(nodePrefix).append(".").append(analysisParameter.getName());
            }
            // order direction asc or desc
            sql.append(" ").append(orderDirection).append(" ");
            if (orderAttributes.size() > 0 || index < (groups.size() - 1)) sql.append(",");
            index++;
        }

        // attribute ordering
        int i = 0;
        String orderDirection = population.getOrderDirection();
        for (GroupingAttribute attribute : orderAttributes) {
            AnalysisParameter analysisParameter = attribute.getAnalysisParameter();

            if (analysisParameter.isQuestionnaireAttribute()) {
                if (attribute.isStruct()) {
                    addStructQuestionnaireOrderBy(sql, nodePrefix, analysisParameter);
                } else {
                    addQuestionnaireOrderBy(sql, nodePrefix, analysisParameter, attribute);
                }
                //} else if(analysisParameter.isDerivedAttribute()) {
                // todo add a select clause from the appropriate view see: SUB_SOURCE_DERIVED_ATTR_BASE and SUB_TARGET_DERIVED_ATTR_BASE views
                // todo - (if a need arises to be able to sort on derived attributes.) (needs to be native sql here too, as the others are)
            } else if (analysisParameter.isDynamicAttribute()) {
                if (attribute.isStruct()) {
                    addStructOrderBy(sql, nodePrefix, analysisParameter);
                } else {
                    addDynamicAttributeOrderBy(sql, nodePrefix, analysisParameter, attribute);
                }
            } else {
                sql.append(nodePrefix).append(".").append(analysisParameter.getName());
            }
            // order direction asc or desc
            sql.append(" ").append(orderDirection).append(" ");
            if (i < orderAttributes.size() - 1) sql.append(", ");
            i++;
        }
    }

    private void addQuestionnaireOrderBy(StringBuffer sql, String nodePrefix, AnalysisParameter analysisParameter, GroupingAttribute attribute) {
        String formattedNodeDasValue = formatSortNodeDasValue(attribute);
        sql.append("( select ").append(formattedNodeDasValue).append(" from dynamic_attributes da, node_das nd, Questionnaires q ")
                .append("where nd.da_id=da.id and da.id=")
                .append(analysisParameter.getName()).append(" and nd.node_id=")
                .append("q.node_id and q.subject_id=").append(nodePrefix).append(".id ")
                .append("and q.qwf_id=").append(analysisParameter.getQuestionnaireWorkflowId()).append(" ) ");
    }

    private void addDynamicAttributeOrderBy(StringBuffer sql, String nodePrefix, AnalysisParameter analysisParameter, GroupingAttribute attribute) {
        String formattedNodeDasValue = formatSortNodeDasValue(attribute);
        sql.append("( select ").append(formattedNodeDasValue).append(" from dynamic_attributes da, node_das nd where nd.da_id=da.id and da.id=")
                .append(analysisParameter.getName()).append(" and nd.node_id=")
                .append(nodePrefix).append(".id ) ");
    }

    private String formatSortNodeDasValue(GroupingAttribute attribute) {
        String oracleTypeConvert = "nd.value";
        if (attribute.isNumber()) {
            oracleTypeConvert = "to_number(nd.value)";
        } else if (attribute.isDate()) {
            oracleTypeConvert = "to_date(nd.value, 'yyyy-MM-dd')";
        }
        return oracleTypeConvert;
    }

    private void addAssociatedArtefactOrderBy(StringBuffer sql, String nodePrefix) {
        sql.append("( select ou.label from ORGANIZATION_UNITS ou, SUBJECT_PRIMARY_ASSOCIATIONS sa, position_associations pa, positions p ")
                .append("where sa.subject_id=")
                .append(nodePrefix).append(".id ")
                .append("and sa.position_id=pa.source_id ")
                .append("and pa.source_id=p.node_id ")
                .append("and p.org_unit_id = ou.node_id ")
                .append("and rownum=1 ) ");
    }

    private void addStructOrderBy(StringBuffer sql, String nodePrefix, AnalysisParameter analysisParameter) {
        sql.append("( select lv.short_desc from lookup_values lv, dynamic_attributes da, node_das nd where lv.id=nd.value and nd.da_id=da.id and da.id=")
                .append(analysisParameter.getName()).append(" and nd.node_id=")
                .append(nodePrefix).append(".id ) ");
    }

    private void addStructQuestionnaireOrderBy(StringBuffer sql, String nodePrefix, AnalysisParameter analysisParameter) {
        sql.append("( select lv.short_desc from lookup_values lv, dynamic_attributes da, node_das nd, Questionnaires q ")
                .append("where lv.id=nd.value and nd.da_id=da.id and da.id=")
                .append(analysisParameter.getName()).append(" and nd.node_id=")
                .append("q.node_id and q.subject_id=").append(nodePrefix).append(".id ")
                .append("and q.qwf_id=").append(analysisParameter.getQuestionnaireWorkflowId()).append(" ) ");
    }


    final StringBuffer getQueryForSimpleGroupingEvalMetric(Class populationClass, Metric metric, AnalysisParameter[] attrsToGroupBy, Population population) throws TalentStudioException {

        final boolean hasAttrs = (attrsToGroupBy != null && attrsToGroupBy.length > 0);

        final String nodeAlias = "n";
        final String permitAlias = "dp";
        final String attributePrefix = "attVal0";

        StringBuffer sql = new StringBuffer("select ");

        // add grouping columns and put comma at end before adding metric columns below
        if (hasAttrs) {
            appendColumnsForGrouping(attrsToGroupBy, sql, nodeAlias);
            sql.append(", ");
        }

        // add column for metric (something like "sum(attVal0.value)"
        String sqlAtt = hibernateCriteriaResolver.getAttrForSelectClause(metric.getAnalysisParameter(), nodeAlias, attributePrefix);
        sql.append(metric.getOperator()).append("(").append(sqlAtt).append(") ");

        // from clause
        sql.append("from ");
        sql.append(populationClass.getName()).append(" ").append(nodeAlias);

        // add attributes to from clause - add dynamic first then add questionnaire - otherwise query will not be correct
        if (hasAttrs) {
            for (int i = 0; i < attrsToGroupBy.length; i++) {
                final AnalysisParameter attribute = attrsToGroupBy[i];
                if (attribute.isDynamicAttribute() && !attribute.isQuestionnaireAttribute()) {
                    hibernateCriteriaResolver.addMetricAttributeToFromClause(attribute, sql, nodeAlias, "attr" + i);
                }
            }
            // add metrics attribute to from clause
            hibernateCriteriaResolver.addMetricAttributeToFromClause(metric.getAnalysisParameter(), sql, nodeAlias, attributePrefix);

            for (int i = 0; i < attrsToGroupBy.length; i++) {
                final AnalysisParameter attribute = attrsToGroupBy[i];
                if (attribute.isDynamicAttribute() && attribute.isQuestionnaireAttribute()) {
                    hibernateCriteriaResolver.addMetricAttributeToFromClause(attribute, sql, nodeAlias, "attr" + i);
                }
            }
        } else {
            // no attrs still have to add the metrics attribute from clause
            hibernateCriteriaResolver.addMetricAttributeToFromClause(metric.getAnalysisParameter(), sql, nodeAlias, attributePrefix);
        }

        // add comma and then add permit criteria join to from clause
        sql.append(", ");
        addPermitCriteria(sql, nodeAlias, permitAlias);

        // add metric attribute criteria
        if(metric.getOperator().equals(IPopulationEngine.COUNT)) {
            if(metric.getValue() != null) {
                hibernateCriteriaResolver.addCriteriaValueForMetricAttribute(metric.getAnalysisParameter(), attributePrefix, metric.getValue(), sql, nodeAlias);
            }
        } else {
            hibernateCriteriaResolver.addCriteriaForMetricAttribute(metric.getAnalysisParameter(), sql, nodeAlias, attributePrefix);
        }

        // add grouping attribute criteria
        if (hasAttrs) {
            for (int i = 0; i < attrsToGroupBy.length; i++) {
                final AnalysisParameter attribute = attrsToGroupBy[i];
                hibernateCriteriaResolver.addCriteriaForMetricAttribute(attribute, sql, nodeAlias, "attr" + i);
            }
        }

        // add population criteria
        appendPopulationCriteriaQuery(population, sql, nodeAlias);

        // add grouping clause
        if (hasAttrs) {
            addGrouping(sql, nodeAlias, attrsToGroupBy);
        }

        return sql;
    }

    final void appendPopulationCriteriaQuery(Population population, StringBuffer sql, String nodePrefix) throws TalentStudioException {       
        if (!population.isForSearching()) addActiveCriteria(population, sql, nodePrefix);
        getHibernateCriteriaResolver().compile(sql, population.getPopulationCriterias(), nodePrefix);
    }

    final void addPermitCriteria(StringBuffer sql, String nodePrefix, String permitPrefix) {
        sql.append(" UserDomainPermit ").append(permitPrefix);
        sql.append(" where \n");
        sql.append(" ").append(nodePrefix).append(".id = ").append(permitPrefix).append(".nodeId and ");
        sql.append(permitPrefix).append(".userId = :userId and ").append(permitPrefix).append(".permitId = :permitId \n");
    }

    final Class getPopulationClass(Population population) {
        if (population.getType().equals(IPopulationEngine.P_POS_TYPE_))
            return Position.class;
        if (population.getType().equals(IPopulationEngine.P_SUB_TYPE_))
            return Subject.class;
        return null;
    }

    final Long getViewPermitForPopulation(Population pop) {

        final String type = pop.getType();
        return getViewPermitForPopulation(type);
    }

    final Long getViewPermitForPopulation(final String type) {
        if (type.equals(IPopulationEngine.P_POS_TYPE_)) {
            return getPositionViewPermit().getId();
        } else if (type.equals(IPopulationEngine.P_SUB_TYPE_)) {
            return getSubjectViewPermit().getId();
        } else
            return null;
    }

    final void addOrderingForQuestionnairesAnswers(StringBuffer sql) {
        sql.append(" ORDER BY av.node, av.dynamicAttribute, av.dynamicPosition");
    }

    /**
     * Make sure that value is always a Number.
     * <br/> Required because Hibernate will return Strings for metrics using dynamic attributes.
     *
     * @param value
     * @return Number or null
     */
    final Number convertValueToNumber(Object value) {

        if (value != null && value instanceof String) {
            value = Double.valueOf((String) value);
        }

        return (Number) value;
    }

    private IPermit getSubjectViewPermit() {
        return subjectViewPermit;
    }

    private IPermit getPositionViewPermit() {
        return positionViewPermit;
    }

    private void addActiveCriteria(Population population, StringBuffer sql, String nodePrefix) {
        if (population.isActiveNodesOnly() || population.isInactiveNodesOnly()) {
            String activeString = population.isActiveNodesOnly() ? ".active = 'T' " : ".active = 'F' ";
            sql.append(AND).append(nodePrefix).append(activeString);
        }
    }

    private void addGrouping(StringBuffer sql, String nodePrefix, AnalysisParameter[] attrs) {
        sql.append(" GROUP BY ");
        appendColumnsForGrouping(attrs, sql, nodePrefix);
    }

    private HibernateCriteriaResolver getHibernateCriteriaResolver() {
        return hibernateCriteriaResolver;
    }

    /**
     * Inner class that builds Map using results.
     */
    final class CrossTabResultsProcessor {

        Map processCrossTableResults(List results, List rowNaResults, List colNaResults, List bothNaResults, List totalRowResults, List totalColResults, List totalNaRowResults, List totalNaColumnResults, List grandTotalResults) {

            Map<CrossTableKey, Number> map = new HashMap<CrossTableKey, Number>();

            int rowIndex = 0;
            int colIndex = 1;
            addResults(results, map, rowIndex, colIndex);

            rowIndex = NA_INDEX;
            colIndex = 0;
            addResults(rowNaResults, map, rowIndex, colIndex);

            rowIndex = 0;
            colIndex = NA_INDEX;
            addResults(colNaResults, map, rowIndex, colIndex);

            rowIndex = TOTAL_INDEX;
            colIndex = 0;
            addResults(totalRowResults, map, rowIndex, colIndex);

            rowIndex = 0;
            colIndex = TOTAL_INDEX;
            addResults(totalColResults, map, rowIndex, colIndex);

            addResults(bothNaResults, map, null, null);
            addResults(totalNaRowResults, map, null, IPopulationEngine._CROSS_TAB_TOTAL);
            addResults(totalNaColumnResults, map, IPopulationEngine._CROSS_TAB_TOTAL, null);
            addResults(grandTotalResults, map, IPopulationEngine._CROSS_TAB_TOTAL, IPopulationEngine._CROSS_TAB_TOTAL);

            return map;
        }

        /**
         * Add values from Map to correct places in grid.
         *
         * @param results
         * @param map
         * @param rowIndex
         * @param colIndex
         */
        private void addResults(List results, Map<CrossTableKey, Number> map, int rowIndex, int colIndex) {
            if (results != null) {
                for (Iterator iterator = results.iterator(); iterator.hasNext();) {
                    Object[] row = (Object[]) iterator.next();

                    Object value = row[row.length - 1];
                    map.put(new CrossTableKey(getIndex(rowIndex, row), getIndex(colIndex, row)), convertValueToNumber(value));
                }
            }
        }

        /**
         * Add total / na, etc results to Map.
         *
         * @param results
         * @param map
         * @param rowValue
         * @param colValue
         */
        private void addResults(List results, Map<CrossTableKey, Number> map, String rowValue, String colValue) {

            Object value = null;
            if (results != null && !results.isEmpty()) {
                value = results.get(0);
            }

            map.put(new CrossTableKey(rowValue, colValue), convertValueToNumber(value));
        }

        /**
         * Get index for Map.
         * <br/> Will return either null, {@link IPopulationEngine#_CROSS_TAB_TOTAL} or the value in the array.
         *
         * @param rowIndex
         * @param values
         * @return String
         */
        private String getIndex(int rowIndex, Object[] values) {
            if (rowIndex == NA_INDEX) return null;
            if (rowIndex == TOTAL_INDEX)
                return IPopulationEngine._CROSS_TAB_TOTAL;
            else
                return toString(values[rowIndex]);
        }

        private String toString(Object object) {
            return object != null ? object.toString() : null;
        }

        private static final int NA_INDEX = -1;
        private static final int TOTAL_INDEX = -2;
    }

    private final IPermit positionViewPermit;
    private final IPermit subjectViewPermit;
    private final HibernateCriteriaResolver hibernateCriteriaResolver;
    protected static final String AND = IPopulationEngine.AND;
    protected static final String WHERE = " WHERE ";
}
