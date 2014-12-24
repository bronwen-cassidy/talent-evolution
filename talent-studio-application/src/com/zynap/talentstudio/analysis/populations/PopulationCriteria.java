package com.zynap.talentstudio.analysis.populations;

import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.BasicAnalysisAttribute;
import com.zynap.talentstudio.common.exceptions.TalentStudioRuntimeException;
import com.zynap.domain.orgbuilder.ISearchConstants;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * @hibernate.class table="POPULATION_CRITERIAS"
 * dynamic-update="true"
 * dynamic-insert="true"
 */
public class PopulationCriteria extends BasicAnalysisAttribute implements Cloneable {

    /**
     * default constructor.
     */
    public PopulationCriteria() {
        this(IPopulationEngine.OP_TYPE_);
    }

    /**
     * minimal constructor.
     */
    public PopulationCriteria(String type) {
        this.type = type;
    }

    /**
     * minimal constructor.
     */
    public PopulationCriteria(Long id, Population population) {
        super(id);
        this.population = population;
    }

    /**
     * Convenience constructor.
     */
    public PopulationCriteria(Long id, String type, String refValue, AnalysisParameter attribute, String operator, String comparator, Population population) {
        this(id, type, refValue, attribute.getName(), attribute.getQuestionnaireWorkflowId(), attribute.getRole(), operator, comparator, population);
    }

    /**
     * Min constructor
     */
    public PopulationCriteria(Long id, String type, String refValue, String attributeName, String operator, String comparator, Population population) {
        this(id, population);
        this.type = type;
        this.refValue = refValue;
        this.attributeName = attributeName;
        this.operator = operator;
        this.comparator = comparator;
    }

    /**
     * full constructor
     */
    public PopulationCriteria(Long id, String type, String refValue, String attributeName, Long questionnaireWorkflowId, String role, String operator, String comparator, Population population) {
        this(id, population);
        this.type = type;
        this.refValue = refValue;
        this.attributeName = attributeName;
        this.questionnaireWorkflowId = questionnaireWorkflowId;
        this.role = role;
        this.operator = operator;
        this.comparator = comparator;
        this.population = population;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRefValue() {
        return this.refValue;
    }

    public void setRefValue(String refValue) {
        this.refValue = refValue;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getComparator() {
        return this.comparator;
    }

    public void setComparator(String comparator) {
        this.comparator = comparator;
    }

    public Population getPopulation() {
        return this.population;
    }

    public void setPopulation(Population population) {
        this.population = population;
    }

    public String getModifiedAlias() {
        return modifiedAlias;
    }

    public void setModifiedAlias(String modifiedAlias) {
        this.modifiedAlias = modifiedAlias;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("type", getType())
                .append("refValue", getRefValue())
                .append("attributeName", getAttributeName())
                .append("operator", getOperator())
                .append("comparator", getComparator())
                .append("questionnaireWorkflowId").append(questionnaireWorkflowId)
                .toString();
    }

    public Object clone() {
        try {
            final PopulationCriteria populationCriteria = (PopulationCriteria) super.clone();
            populationCriteria.setPopulation((Population) (population != null ? population.clone() : null));
            return populationCriteria;
        } catch (CloneNotSupportedException e) {
            throw new TalentStudioRuntimeException("Unable to clone " + this.getClass(), e);
        }
    }

    public boolean isAllActive() {
        return (ISearchConstants.ACTIVE.equals(attributeName) && ISearchConstants.ALL.equals(refValue));
    }

    public PopulationCriteria deepCopy() {
        PopulationCriteria result = new PopulationCriteria(id, type, refValue, attributeName, questionnaireWorkflowId, role, operator, comparator, null);
        result.setPosition(position);
        result.setModifiedAlias(modifiedAlias);
        return result;
    }

    /**
     * nullable persistent field
     */
    private String type;

    /**
     * nullable persistent field
     */
    private String refValue;

    /**
     * nullable persistent field
     */
    private String operator;

    /**
     * nullable persistent field
     */
    private String comparator;

    /**
     * nullable persistent field
     */
    private Integer position;

    /**
     * persistent field
     */
    private Population population;

    /**
     * Transient field.
     */
    private transient String modifiedAlias;
}
