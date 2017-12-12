package com.zynap.talentstudio.analysis.populations;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.Page;
import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.analysis.reports.ChartReportAttribute;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.organisation.positions.PositionDto;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.organisation.subjects.SubjectDTO;
import com.zynap.talentstudio.security.permits.IPermit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate.HibernateCallback;
import org.springframework.orm.hibernate.HibernateTemplate;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: jsueiras
 * Date: 10-Jun-2005
 * Time: 12:23:40
 */
public final class HibernateSimplePopulationResolver extends HibernateAbstractResolver {

    public HibernateSimplePopulationResolver(IPermit positionViewPermit, IPermit subjectViewPermit, SessionFactory sessionFactory) {
        super(positionViewPermit, subjectViewPermit, sessionFactory);
    }

    /**
     * Get questionnaire answers for specified attributes for all members of a population.
     *
     * @param attributes
     * @param population
     * @param userId
     * @return List of NodeExtendedAttributes
     * @throws TalentStudioException
     */
    public List findQuestionnaireAnswers(List<AnalysisParameter> attributes, Population population, Long userId) throws TalentStudioException {

        final String nodeAlias = "n";
        final String queAlias = "q";
        final String davAlias = "def";
        final String qwfAlias = "qwf";
        final Class populationClass = Subject.class;


        // build root of query
        StringBuffer sql = getQuestionnaireQueryRoot(populationClass, nodeAlias);

        // add join to domain permits for security
        addPermitCriteria(sql, nodeAlias, "dp");

        // add "and"
        sql.append(AND);

        // add restriction to ensure that only the expected attributes for the expected workflows are included
        addQuestionnaireQueryRestrictions(sql, attributes, queAlias, nodeAlias, davAlias, qwfAlias);

        // add rest of population criteria
        appendPopulationCriteriaQuery(population, sql, nodeAlias);

        // add grouping
        addOrderingForQuestionnairesAnswers(sql);

        String query = sql.toString();
        return execute(query, userId, population);
    }

    /**
     * Get questionnaire answers for specified attributes for all the specified nodes.
     *
     * @param attributes
     * @param nodeIds
     * @param userId
     * @return List of NodeExtendedAttributes
     * @throws TalentStudioException
     */
    public List findQuestionnaireAnswers(List attributes, List nodeIds, Long userId) throws TalentStudioException {

        final String nodeAlias = "n";
        final String queAlias = "q";
        final String davAlias = "def";
        final String qwfAlias = "qwf";
        final Class populationClass = Subject.class;

        // build root of query
        StringBuffer sql = getQuestionnaireQueryRoot(populationClass, nodeAlias);

        // add join to domain permits for security
        addPermitCriteria(sql, nodeAlias, "dp");

        // add "and"
        sql.append(AND);

        // add restriction to ensure that only the expected attributes for the expected workflows are included
        addQuestionnaireQueryRestrictions(sql, attributes, queAlias, nodeAlias, davAlias, qwfAlias);

        // add restriction for nodes
        sql.append(AND).append(nodeAlias).append(".id IN(").append(StringUtils.collectionToCommaDelimitedString(nodeIds)).append(")");

        // add grouping
        addOrderingForQuestionnairesAnswers(sql);

        String query = sql.toString();
        return execute(query, userId, IPopulationEngine.P_SUB_TYPE_);
    }

    /**
     * Different method that bypasses security to get answers for personal details.
     *
     * @param attributes
     * @param nodeId
     * @param userId
     * @return List of NodeExtendedAttributes
     */
    public List findPersonalQuestionnaireAnswers(List<AnalysisParameter> attributes, Long nodeId, Long userId) {

        final String nodeAlias = "n";
        final String queAlias = "q";
        final String davAlias = "def";
        final String qwfAlias = "qwf";

        final Class populationClass = Subject.class;

        // buidl query root
        StringBuffer sql = getQuestionnaireQueryRoot(populationClass, nodeAlias);

        // add a "where" clause
        sql.append(WHERE);

        // add restriction to ensure that only the expected attributes for the expected workflows are included
        addQuestionnaireQueryRestrictions(sql, attributes, queAlias, nodeAlias, davAlias, qwfAlias);

        // add restriction to only get the questionnaires for the specified nodeId
        sql.append(AND).append(nodeAlias).append(".id = ").append(nodeId);

        // add grouping
        addOrderingForQuestionnairesAnswers(sql);

        return getHibernateTemplate().find(sql.toString());
    }

	public List findPersonalQuestionnaireAnswers(List<AnalysisParameter> attributes, List<Long> workflowIds, Long nodeId) {

		final String nodeAlias = "n";
		final String queAlias = "q";
		final String davAlias = "def";
		final String qwfAlias = "qwf";

		final Class populationClass = Subject.class;

		// buidl query root
		StringBuffer sql = getQuestionnaireQueryRoot(populationClass, nodeAlias);

		// add a "where" clause
		sql.append(WHERE);
		
		List<String> attributeIds = new ArrayList<>();
		for (AnalysisParameter attribute : attributes) {
			attributeIds.add(attribute.getName());
		}
		
		addQuestionnaireQueryRestrictions(sql, queAlias, nodeAlias, davAlias, qwfAlias, workflowIds, attributeIds);

		// add restriction to only get the questionnaires for the specified nodeId
		sql.append(AND).append(nodeAlias).append(".id = ").append(nodeId);

		// add grouping
		addOrderingForQuestionnairesAnswers(sql);

		return getHibernateTemplate().find(sql.toString());
	}

    /**
     * Run a query to get data for a tabular report.
     * <br/> Returns the nodes - presentation layer components then get the appropriate values for the specified columns.
     *
     * @param population
     * @param userId
     * @return List of Nodes (subjects or positions)
     * @throws TalentStudioException
     */
    protected List find(Population population, Long userId) throws TalentStudioException {
        String query = buildFindQuery(population);
        return execute(query, userId, population);
    }

    /**
     * Searches for subjects.
     *
     * @param population
     * @param userId
     * @return List of {@link com.zynap.talentstudio.organisation.subjects.SubjectDTO} objects
     * @throws TalentStudioException
     */
    protected List<SubjectDTO> findSubjects(Population population, Long userId) throws TalentStudioException {

        StringBuffer query = new StringBuffer("select new ");
        query.append(SubjectDTO.class.getName()).append("(n.id, n.coreDetail.firstName, n.coreDetail.secondName, n.currentJobInfo, info.username) from ")
                .append(getPopulationClass(population).getName()).append(" n left outer join n.user.loginInfo info, ");
        addPermitCriteria(query, "n", "dp");
        appendPopulationCriteriaQuery(population, query, "n");
        appendOrderByClause(population, query, "n");

        return execute(query.toString(), userId, population);
    }

    protected List<PositionDto> findPositions(Population population, Long userId) throws TalentStudioException {

        StringBuffer query = new StringBuffer("select new ");
        query.append(PositionDto.class.getName()).append("(n.id, n.title, n.active, n.organisationUnit.label, n.currentHolderInfo) from ")
                .append(getPopulationClass(population).getName()).append(" n, ");
        addPermitCriteria(query, "n", "dp");
        appendPopulationCriteriaQuery(population, query, "n");
        appendOrderByClause(population, query, "n");
        return execute(query.toString(), userId, population);
    }

    protected Page find(Population population, Long userId, int start, int end, int numRecords) throws TalentStudioException {

        String query = buildFindPageQuery(population);
        return execute(query, userId, population, start, end, numRecords);
    }

    private String buildFindQuery(Population population) throws TalentStudioException {
        String query = population.getCompiledSql();
        if (!(StringUtils.hasText(query))) {
            final Class populationClass = getPopulationClass(population);
            final StringBuffer sql = getQueryRoot(populationClass);
            appendPopulationCriteriaQuery(population, sql, "n");
            appendOrderByClause(population, sql, "n");
            query = sql.toString();
            population.setCompiledSql(query);
        }

        return query;
    }

    private String buildFindPageQuery(Population population) throws TalentStudioException {
        String query = population.getCompiledSql();
        if (!(StringUtils.hasText(query))) {
            final Class populationClass = getPopulationClass(population);
            final StringBuffer sql = getQueryRoot(populationClass);
            appendPopulationCriteriaQuery(population, sql, "n");
            query = sql.toString();
            population.setCompiledSql(query);
        }

        return query;
    }

    /**
     * Run a cross tab report.
     * <br/> Returns a Map of values with CrossTableKey instances as keys.
     * <br/> The values are always Numbers.
     * <br/> Each CrossTableKey object holds a unique combination of a vertical grouping column value and a horizontal grouping column value.
     * <br/> It does not return entries where there are no values - this is handled by the presentation layer components that get all the possible values
     * for the vertical and horizontal grouping column values and build the full grid inserting the ones returned by this method.
     *
     * @param population
     * @param metric
     * @param rowAtt     The horizontal grouping column attribute
     * @param columnAtt  The vertical grouping column attribute
     * @param userId
     * @return Map
     * @throws TalentStudioException
     */
    protected Map findCrossTab(Population population, Metric metric, AnalysisParameter rowAtt, AnalysisParameter columnAtt, Long userId) throws TalentStudioException {

        Class populationClass = getPopulationClass(population);

        // wrap the population criterias in brackets to group them as a unit
        population.wrapCriteria();

        final List results = executeMetric(populationClass, metric, new AnalysisParameter[]{rowAtt, columnAtt}, population, userId);

        // build criterias for null
        boolean firstCriteria = (population.getQueryablePopulationCriterias() == null || population.getQueryablePopulationCriterias().size() == 0);
        PopulationCriteria criteriaRowAttNull = new PopulationCriteria(null, IPopulationEngine.OP_TYPE_, null, rowAtt, firstCriteria ? null : IPopulationEngine.AND, IPopulationEngine.ISNULL, null);
        PopulationCriteria criteriaColAttNull = new PopulationCriteria(null, IPopulationEngine.OP_TYPE_, null, columnAtt, firstCriteria ? null : IPopulationEngine.AND, IPopulationEngine.ISNULL, null);

        // get query for values where row attribute is null
        population.addPopulationCriteria(criteriaRowAttNull);
        final List rowNaResults = executeMetric(populationClass, metric, new AnalysisParameter[]{columnAtt}, population, userId);

        // remove row null criteria, add column null criteria and get query for values where column attribute is null
        population.getPopulationCriterias().remove(criteriaRowAttNull);
        population.addPopulationCriteria(criteriaColAttNull);
        final List colNaResults = executeMetric(populationClass, metric, new AnalysisParameter[]{rowAtt}, population, userId);

        // add row null criteria again to get query from values where column and row are both null
        criteriaRowAttNull.setOperator(IPopulationEngine.AND);
        population.addPopulationCriteria(criteriaRowAttNull);
        final List bothNaResults = executeMetric(populationClass, metric, new AnalysisParameter[]{}, population, userId);

        // remove the row null and column null criterias and get queries for row and column totals
        population.getPopulationCriterias().remove(criteriaRowAttNull);
        population.getPopulationCriterias().remove(criteriaColAttNull);
        final List totalRowResults = executeMetric(populationClass, metric, new AnalysisParameter[]{columnAtt}, population, userId);
        final List totalColResults = executeMetric(populationClass, metric, new AnalysisParameter[]{rowAtt}, population, userId);

        // get query for NA values for rows
        population.addPopulationCriteria(criteriaRowAttNull);
        criteriaRowAttNull.setOperator(firstCriteria ? null : IPopulationEngine.AND);
        final List totalNaRowResults = executeMetric(populationClass, metric, new AnalysisParameter[]{}, population, userId);

        // get query for NA values for columns
        population.getPopulationCriterias().remove(criteriaRowAttNull);
        population.addPopulationCriteria(criteriaColAttNull);
        criteriaColAttNull.setOperator(firstCriteria ? null : IPopulationEngine.AND);
        final List totalNaColumnResults = executeMetric(populationClass, metric, new AnalysisParameter[]{}, population, userId);

        // get query for grand total
        population.getPopulationCriterias().remove(criteriaColAttNull);
        final List grandTotalResults = executeMetric(populationClass, metric, new AnalysisParameter[]{}, population, userId);

        return new CrossTabResultsProcessor().processCrossTableResults(results, rowNaResults, colNaResults, bothNaResults, totalRowResults, totalColResults, totalNaRowResults, totalNaColumnResults, grandTotalResults);
    }

    @Override
    protected Map findChart(ChartReport report, Long userId) throws TalentStudioException {

        Population defaultPopulation = report.getDefaultPopulation();
        final String nodeAlias = "n";        
        final Class populationClass = Subject.class;

        List<ChartReportAttribute> attributeSet = report.getChartReportAttributes();
        Set<Long> workflowIds = new HashSet<Long>();
        Set<String> attributeIds = new HashSet<String>();
        for (ChartReportAttribute chartReportAttribute : attributeSet) {
            workflowIds.add(chartReportAttribute.getQuestionnaireWorkflowId());
            attributeIds.add(String.valueOf(chartReportAttribute.getDynamicAttributeId()));
        }

        StringBuffer sql = new StringBuffer("select count(lv.label) ");
        sql.append("from NodeExtendedAttribute av, DynamicAttribute def, LookupValue lv ");
        sql.append(", Questionnaire q, QuestionnaireWorkflow qwf, ");
        sql.append(populationClass.getName()).append(" ").append(nodeAlias).append(", ");

        // add join to domain permits for security
        addPermitCriteria(sql, nodeAlias, "dp");

        // add restriction to ensure that only the expected attributes for the expected workflows are included
        sql.append(AND).append("q.questionnaireWorkflowId = qwf.id");
        sql.append(AND).append("q.subjectId = n.id ");
        sql.append(AND).append("q.id = av.node.id");
        sql.append(AND).append("q.questionnaireWorkflowId  in (").append(StringUtils.collectionToCommaDelimitedString(workflowIds)).append(") ");
        sql.append(AND).append("def.id  in (").append(StringUtils.collectionToCommaDelimitedString(attributeIds)).append(") ");
        sql.append(AND).append("av.dynamicAttribute.id=def.id");
        sql.append(AND).append("lv.id=av.value");

        Map<Column, Integer> pies = new HashMap<Column, Integer>();
        // now for each column execute the result and get the answers
        for (Column column : report.getColumns()) {
            StringBuffer result = new StringBuffer(sql);
            result.append(AND);
            result.append("lv.label='").append(column.getValue()).append("' ");
            // add rest of population criteria
            appendPopulationCriteriaQuery(defaultPopulation, result, nodeAlias);
            String query = result.toString();
            List results = execute(query, userId, defaultPopulation);
            pies.put(column, (Integer)results.get(0));
        }
        
        return pies;
    }

    /**
     * Run all metrics specified.
     * <br/> Returns a Map of Maps - each Map contains a value for each metric keyed on the metric id.
     * <br/> Also contains the totals if doing grouping - if not doing grouping then this is the only Map in the List.
     *
     * @param population
     * @param metrics
     * @param groupingAttribute
     * @param userId
     * @return Map
     * @throws TalentStudioException
     */
    protected Map findMetric(Population population, Collection<Metric> metrics, AnalysisParameter groupingAttribute, Long userId) throws TalentStudioException {

        final Map<Object, Map<String, Object>> results = new LinkedHashMap<Object, Map<String, Object>>();

        final boolean hasGroupingAttribute = (groupingAttribute != null);
        final String groupingAttributeName = hasGroupingAttribute ? AnalysisAttributeHelper.getName(groupingAttribute) : null;

        final Class populationClass = getPopulationClass(population);

        AnalysisParameter[] attributesArray = new AnalysisParameter[0];
        if (hasGroupingAttribute) {
            attributesArray = new AnalysisParameter[1];
            attributesArray[0] = groupingAttribute;
        }

        // build metrics array
        final int numOfMetrics = metrics.size();
        final Metric[] metricsArray = metrics.toArray(new Metric[numOfMetrics]);

        // map that will hold totals if any present - add fake grouping value that indicates total
        final Map<String, Object> totalsMap = new HashMap<String, Object>();
        if (hasGroupingAttribute) totalsMap.put(groupingAttributeName, ReportConstants.TOTAL);

        // map that will hold NAs if any present - add fake grouping value that indicates NA
        final Map<String, Object> naMap = new HashMap<String, Object>();
        if (hasGroupingAttribute) naMap.put(groupingAttributeName, ReportConstants.NA);

        // run each metric individually
        for (int i = 0; i < numOfMetrics; i++) {
            Metric metric = metricsArray[i];

            // get key for adding elements to Map
            final Long id = metric.getId();
            String key = ReportConstants.METRIC_ATTR_PREFIX + id.toString();

            final List values = executeMetric(populationClass, metric, attributesArray, population, userId);
            for (int j = 0; j < values.size(); j++) {

                // get value
                Object value = values.get(j);

                // if grouping the value will be an array the first value is the grouping value, the 2nd is the metric value
                Object groupingAtributeValue = null;
                if (hasGroupingAttribute) {
                    Object[] valuesArray = (Object[]) value;
                    groupingAtributeValue = valuesArray[0];
                    value = valuesArray[1];
                }

                // find Map for results based on value of grouping attribute
                Map<String, Object> rowResults = results.get(groupingAtributeValue);
                if (rowResults == null) {
                    rowResults = new HashMap<String, Object>();
                    rowResults.put(groupingAttributeName, groupingAtributeValue);
                    results.put(groupingAtributeValue, rowResults);
                }

                rowResults.put(key, convertValueToNumber(value));
            }

            if (hasGroupingAttribute) {

                // build criteria to be added to population if we are doing grouping and therefore need to get NA values
                boolean firstCriteria = (population.getQueryablePopulationCriterias() == null || population.getQueryablePopulationCriterias().size() == 0);
                final PopulationCriteria nullGroupingCriteria = new PopulationCriteria(null, IPopulationEngine.OP_TYPE_, null, groupingAttribute, firstCriteria ? null : IPopulationEngine.AND, IPopulationEngine.ISNULL, null);

                // get totals for each metric and add values to totals Map
                getValues(populationClass, metric, population, userId, totalsMap, key);

                // before adding an extra criteria to the population let us wrap this in brackets if there is not one at the beginning
                population.wrapCriteria();

                // get NA values for each metric and add to NA Map
                population.addPopulationCriteria(nullGroupingCriteria);
                getValues(populationClass, metric, population, userId, naMap, key);

                // IMPORTANT: make sure that the null criteria is removed - otherwise subsequent queries will be wrong!!
                population.getPopulationCriterias().remove(nullGroupingCriteria);
            }
        }

        // append totals and NAs
        if (hasGroupingAttribute) {
            results.put(ReportConstants.NA, naMap);
            results.put(ReportConstants.TOTAL, totalsMap);
        }

        return results;
    }

    /**
     * Execute metric and copy values into Map provided.
     *
     * @param populationClass
     * @param metric
     * @param population
     * @param userId
     * @param map
     * @param key
     * @throws TalentStudioException
     */
    private void getValues(final Class populationClass, Metric metric, Population population, Long userId, final Map<String, Object> map, String key) throws TalentStudioException {

        final List values = executeMetric(populationClass, metric, null, population, userId);
        for (int j = 0; j < values.size(); j++) {
            Object value = values.get(j);
            map.put(key, convertValueToNumber(value));
        }
    }

    /**
     * Execute metric.
     *
     * @param populationClass
     * @param metric
     * @param attributesArray
     * @param population
     * @param userId
     * @return List
     * @throws TalentStudioException
     */
    private List executeMetric(Class populationClass, Metric metric, AnalysisParameter[] attributesArray, Population population, Long userId) throws TalentStudioException {

        final AnalysisParameter attribute = metric.getAnalysisParameter();
        if (metric.isAverage() && attribute.isDynamicAttribute()) {
            return executeExtendedAttributeAverageMetric(attribute, populationClass, attributesArray, population, userId);
        } else {
            final StringBuffer query = getQueryForSimpleGroupingEvalMetric(populationClass, metric, attributesArray, population);
            return execute(query.toString(), userId, population);
        }
    }

    /**
     * Execute average metric for extended attribute.
     * <br/> Does sum(extended attribute value) / count(nodes) as avg(extended attribute value) will not work because of missing values.
     *
     * @param attribute
     * @param populationClass
     * @param attributesArray
     * @param population
     * @param userId
     * @return List
     * @throws TalentStudioException
     */
    private List executeExtendedAttributeAverageMetric(final AnalysisParameter attribute, Class populationClass, final AnalysisParameter[] attributesArray, Population population, Long userId) throws TalentStudioException {

        final List results = new ArrayList();
        final boolean hasGroupingAttrs = (attributesArray != null && attributesArray.length > 0);
        final Metric sumMetric = new Metric();
        sumMetric.setAnalysisParameter(attribute);
        sumMetric.setOperator(IPopulationEngine.SUM);

        final List sumResults = executeMetric(populationClass, sumMetric, attributesArray, population, userId);
        if (!sumResults.isEmpty()) {

            final List countResults = executeMetric(populationClass, IPopulationEngine.COUNT_METRIC, attributesArray, population, userId);

            if (hasGroupingAttrs) {

                final int valueIndex = attributesArray.length;

                // iterate sum results, find matching element based on grouping attribute value in count results and calculate the new value
                for (int i = 0; i < sumResults.size(); i++) {
                    final Object[] sumValues = (Object[]) sumResults.get(i);

                    Object[] countValues = (Object[]) CollectionUtils.find(countResults, new Predicate() {

                        public boolean evaluate(Object object) {
                            Object[] countValues = (Object[]) object;
                            for (int j = 0; j < attributesArray.length; j++) {
                                Number countValue = convertValueToNumber(countValues[j]);
                                Number sumValue = convertValueToNumber(sumValues[j]);
                                if (!sumValue.equals(countValue)) {
                                    return false;
                                }
                            }
                            return true;
                        }
                    });

                    Number sumValue = convertValueToNumber(sumValues[valueIndex]);
                    final Number countValue = convertValueToNumber(countValues[valueIndex]);
                    final Number avgValue = calculateAverage(sumValue, countValue);

                    // build array of values to put in instead of original values - last element is average value - everything else is copied
                    Object[] newValues = new Object[sumValues.length];
                    System.arraycopy(sumValues, 0, newValues, 0, sumValues.length);
                    newValues[valueIndex] = avgValue;
                    results.add(newValues);
                }

            } else {

                // if not grouped just use first value only
                final Number countValue = convertValueToNumber(countResults.get(0));
                final Number sumValue = convertValueToNumber(sumResults.get(0));
                results.add(calculateAverage(sumValue, countValue));
            }
        }

        return results;
    }

    /**
     * Calculate average by dividing sum.doubleValue() by countValue.doubleValue().
     * <br/> Will return null if either (or both) is null.
     *
     * @param sumValue
     * @param countValue
     * @return Number or null
     */
    private Number calculateAverage(final Number sumValue, final Number countValue) {

        Number newValue = null;

        if (sumValue != null && countValue != null) {
            newValue = new Double(sumValue.doubleValue() / countValue.doubleValue());
        }

        return newValue;
    }

    private StringBuffer getQueryRoot(Class populationClass) {
        StringBuffer sql = new StringBuffer("select n from ");
        sql.append(populationClass.getName()).append(" n, ");
        addPermitCriteria(sql, "n", "dp");
        return sql;
    }

    /**
     * Build root of query for questionnaire answers.
     * <br/> Builds joins to lookup types and lookup values.
     *
     * @param populationClass
     * @param nodeAlias
     * @return StringBuffer
     */
    private StringBuffer getQuestionnaireQueryRoot(final Class populationClass, final String nodeAlias) {

        StringBuffer sql = new StringBuffer("select av ");
        sql.append("from NodeExtendedAttribute av join fetch av.dynamicAttribute def left join fetch def.refersToType lookupType ");
        sql.append("left join fetch lookupType.lookupValues");
        sql.append(", Questionnaire q, QuestionnaireWorkflow qwf, ");
        sql.append(populationClass.getName()).append(" ").append(nodeAlias).append(", ");

        return sql;
    }

    /**
     * Add criteria to query to get selected workflows and attributes.
     * <br/> Also restricts workflows to completed ones only.
     *
     * @param sql
     * @param attributes
     * @param queAlias
     * @param nodeAlias
     * @param davAlias
     * @param qwfAlias
     */
    private void addQuestionnaireQueryRestrictions(StringBuffer sql, List<AnalysisParameter> attributes, final String queAlias, final String nodeAlias, final String davAlias, String qwfAlias) {

        final Collection<Long> workflowIds = new HashSet<Long>();
        final Collection<String> attributeIds = new HashSet<String>();
        for (AnalysisParameter attribute : attributes) {

            workflowIds.add(attribute.getQuestionnaireWorkflowId());
            attributeIds.add(attribute.getName());
        }
        addQuestionnaireQueryRestrictions(sql, queAlias, nodeAlias, davAlias, qwfAlias, workflowIds, attributeIds);
    }

    private void addQuestionnaireQueryRestrictions(StringBuffer sql, String queAlias, String nodeAlias, String davAlias, String qwfAlias, Collection<Long> workflowIds, Collection<String> attributeIds) {
        // completed questionnaires only or info forms
        sql.append(queAlias).append(".questionnaireWorkflowId = ").append(qwfAlias).append(".id");
        sql.append(AND).append("( ");
        sql.append(queAlias).append(".completed = 'T' or ").append(qwfAlias).append(".workflowType = 'INFO_FORM')");
        sql.append(AND).append(queAlias).append(".subjectId = ").append(nodeAlias).append(".id");
        sql.append(AND).append(queAlias).append(".id = ").append("av").append(".node.id");
        sql.append(AND).append(queAlias).append(".questionnaireWorkflowId  in (").append(StringUtils.collectionToCommaDelimitedString(workflowIds)).append(") ");
        sql.append(AND).append(davAlias).append(".id  in (").append(StringUtils.collectionToCommaDelimitedString(attributeIds)).append(") ");
    }

    private List execute(String query, Long userId, Population population) throws TalentStudioException {
        return execute(query, userId, population.getType());
    }

    private List execute(final String query, final Long userId, String populationType) throws TalentStudioException {
        try {
            logger.info("Query executed: " + query);
            return getHibernateTemplate().findByNamedParam(query, new String[]{"userId", "permitId"}, new Object[]{userId, getViewPermitForPopulation(populationType)});
        } catch (DataAccessException e) {
            logger.error(e.getMessage() + " query executed: " + query, e);
            throw new TalentStudioException(e);
        }
    }

    private Page execute(final String query, final Long userId, Population population, final int start, final int end, int numRecords) throws TalentStudioException {
        try {
            logger.info("Query executed: " + query);
            Page page = new Page();
            final Long viewPermit = getViewPermitForPopulation(population.getType());
            final HibernateTemplate template = getHibernateTemplate();

            if (numRecords == -1) {

                final String countQuery = "select count(*) " + query.substring("select n".length());
                // execute the count
                numRecords = (Integer) getHibernateTemplate().execute(new HibernateCallback() {
                    public Object doInHibernate(Session session) throws HibernateException {
                        Query queryObject = session.createQuery(countQuery);
                        queryObject.setParameter("userId", userId);
                        queryObject.setParameter("permitId", viewPermit);
                        return queryObject.uniqueResult();
                    }
                }, true);
            }

            page.setNumRecords(numRecords);
            // if no count just return the page
            if (numRecords == 0) return page;

            // add the order by
            final StringBuffer orderQuery = new StringBuffer(query);
            addOrderByClause(population, orderQuery, "n");

            List results = (List) template.execute(new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException {
                    Query queryObject = session.createQuery(orderQuery.toString());
                    queryObject.setFirstResult(start);
                    queryObject.setMaxResults(end - start);
                    queryObject.setParameter("userId", userId);
                    queryObject.setParameter("permitId", viewPermit);
                    return queryObject.list();
                }
            }, true);

            page.setResults(results);

            page.setStart(start);
            if (numRecords <= end) {
                page.setNext(numRecords);
            } else {
                page.setNext(end);
            }
            return page;

        } catch (DataAccessException e) {
            logger.error(e.getMessage() + " query executed: " + query, e);
            throw new TalentStudioException(e);
        }
    }
}
