package com.zynap.talentstudio.analysis.populations;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.analysis.reports.GroupingAttribute;
import com.zynap.talentstudio.common.exceptions.TalentStudioRuntimeException;
import com.zynap.talentstudio.common.groups.Group;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;


public class Population extends ZynapDomainObject implements Cloneable {

    public Population(Long id) {
        super(id);
        populationCriterias = new ArrayList<PopulationCriteria>();
    }

    /**
     * full constructor
     *
     * @param id
     * @param type
     * @param name
     * @param scope
     * @param description
     * @param compiledSql
     * @param populationCriterias
     */
    public Population(Long id, String type, String name, String scope, String description, String compiledSql, List<PopulationCriteria> populationCriterias) {
        this.id = id;
        this.type = type;
        this.label = name;
        this.scope = scope;
        this.description = description;
        this.compiledSql = compiledSql;
        this.populationCriterias = new ArrayList<PopulationCriteria>();
        this.populationCriterias = populationCriterias;
    }

    /**
     * default constructor
     */
    public Population() {

        populationCriterias = new ArrayList<PopulationCriteria>();
    }


    public String getArtefactType() {
        return getType();
    }

    public boolean isForSearching() {
        return forSearching;
    }

    public void setForSearching(boolean forSearching) {
        this.forSearching = forSearching;
    }

    public String[] getOrderColumns() {
        return orderColumns;
    }

    /**
     * Attributes used to order the results
     * @return GroupingAttribute object
     */
    public List<GroupingAttribute> getOrderAttributes() {
        return orderAttributes;
    }

    public void setOrderColumns(String[] orderColumns) {
        this.orderColumns = orderColumns;
    }

    /**
     * @param orderColumns
     */
    public void setOrderAttributes(List<GroupingAttribute> orderColumns) {
        this.orderAttributes = orderColumns;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getScope() {
        return this.scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompiledSql() {
        return this.compiledSql;
    }

    public void setCompiledSql(String compiledSql) {
        this.compiledSql = compiledSql;
    }

    public List<PopulationCriteria> getPopulationCriterias() {
        return this.populationCriterias;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public void assignNewGroups(Set<Group> newGroups) {
        getGroups().clear();
        setGroups(newGroups);
    }

    /**
     * Gets the population criterias excluding the active=All criteria
     * @return all population criterias except the active=All criteria
     */
    public List<PopulationCriteria> getQueryablePopulationCriterias() {
        List<PopulationCriteria> result = new ArrayList<PopulationCriteria>();
        for (PopulationCriteria populationCriteria : populationCriterias) {
            if (!populationCriteria.isAllActive()) {
                result.add(populationCriteria);
            }
        }
        return result;
    }

    public void setPopulationCriterias(List<PopulationCriteria> populationCriterias) {
        this.populationCriterias = populationCriterias;
    }

    public void addPopulationCriteria(PopulationCriteria criteria) {
        populationCriterias.add(criteria);
        criteria.setPopulation(this);
        criteria.setPosition(populationCriterias.size() - 1);
    }

    public Object clone() throws CloneNotSupportedException {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new TalentStudioRuntimeException("Unable to clone " + this.getClass(), e);
        }
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("type", getType())
                .append("label", getLabel())
                .append("scope", getScope())
                .append("description", getDescription())
                .append("compiledSql", getCompiledSql())
                .toString();
    }

    public void setSortDirection(String orderDirection) {
        this.orderDirection = orderDirection;
    }

    public String getOrderDirection() {
        return orderDirection;
    }

    /**
     * Attributes used to do the grouping of results.
     * @param groupingAttributes
     */
    public void setGroupingAttributes(List<GroupingAttribute> groupingAttributes) {
        this.groupingAttributes = groupingAttributes;
    }

    public List<GroupingAttribute> getGroupingAttributes() {
        return groupingAttributes;
    }

    public void wrapCriteria() {

        final List<PopulationCriteria> criterias = getPopulationCriterias();
        int count = criterias == null ? -1 : criterias.size();

        if (count > 0 && criterias != null) {
            final PopulationCriteria populationCriteria = criterias.get(0);
            // if it has a bracket nothing to do
            final String operator = populationCriteria.getOperator();
            final String lastCriteriaType = criterias.get(criterias.size() - 1).getType();

            // if this lot is true we can wrap the criteria
            if (!IPopulationEngine.LEFT_BRCKT_.equals(operator) && !IPopulationEngine.RIGHT_BRCKT_.equals(lastCriteriaType)) {
                populationCriteria.setOperator(IPopulationEngine.LEFT_BRCKT_);
                // add at the end a closing bracket
                final PopulationCriteria criteria = new PopulationCriteria(IPopulationEngine.RIGHT_BRCKT_);
                addPopulationCriteria(criteria);
            }
        }
    }

    public Integer getActiveCriteria() {
        return activeCriteria;
    }

    public void setActiveCriteria(Integer activeCriteria) {
        this.activeCriteria = activeCriteria;
    }

    public boolean isActiveNodesOnly() {
        return activeCriteria.equals(ACTIVE_ONLY);
    }

    public boolean isInactiveNodesOnly() {
        return this.activeCriteria.equals(INACTIVE_ONLY);
    }

	public static final Long DUMMY_POPULATION_ID = -10L;
	private boolean forSearching;
    private String[] orderColumns;
    private List<GroupingAttribute> orderAttributes = new ArrayList<GroupingAttribute>();
    private List<GroupingAttribute> groupingAttributes = new ArrayList<GroupingAttribute>();

    /**
     * nullable persistent field
     */
    protected String type;

    /**
     * nullable persistent field
     */
    protected String scope;

    /**
     * nullable persistent field
     */
    protected String description;

    /**
     * nullable persistent field
     */
    private Long userId;

    /**
     * nullable persistent field
     */
    protected String compiledSql;

    /**
     * persistent field
     */
    protected List<PopulationCriteria> populationCriterias = new ArrayList<PopulationCriteria>();

    /* the groups this report is published for, may be empty, if this report is PRIVATE groups must be empty or null */
    private Set<Group> groups = new HashSet<Group>();

    private String orderDirection = "ASC";
    /* default criteria for populations is active nodes */
    private Integer activeCriteria = ACTIVE_ONLY;

    public static final String ORDER_DESCENDING = "DESC";
    public static final String ORDER_ASC = "ASC";

    /* only active nodes are considered in the search */
    public static final Integer ACTIVE_ONLY = 1;
    /* only inactive nodes are considered in the search */
    public static final Integer INACTIVE_ONLY = 2;
    /* Both active and inactive nodes are considered in the search */
    public static final Integer ALL_ACTIVE = 3;
}
