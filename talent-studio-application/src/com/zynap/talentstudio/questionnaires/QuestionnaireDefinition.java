package com.zynap.talentstudio.questionnaires;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


/**
 * @author Hibernate CodeGenerator
 */
public class QuestionnaireDefinition extends ZynapDomainObject {

    private static final long serialVersionUID = 6583957152226721208L;

    /**
     * default constructor
     */
    public QuestionnaireDefinition() {
    }

    /**
     * minimal constructor
     * @param id unique identifier for questionnaire definition
     * @param label label for definition
     */
    public QuestionnaireDefinition(Long id, String label) {
        super(id, label);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<QuestionnaireWorkflow> getQuestionnaireWorkflows() {
        return this.questionnaireWorkflows;
    }

    public void setQuestionnaireWorkflows(Set<QuestionnaireWorkflow> questionnaireWorkflows) {
        this.questionnaireWorkflows = questionnaireWorkflows;
    }

    public void addQuestionnaireWorkflow(QuestionnaireWorkflow questionnaireWorkflow) {
        questionnaireWorkflow.setQuestionnaireDefinition(this);
        questionnaireWorkflows.add(questionnaireWorkflow);
    }

    public void removeQuestionnaireWorkflow(QuestionnaireWorkflow questionnaireWorkflow) {
        questionnaireWorkflows.remove(questionnaireWorkflow);        
    }

    public List<DynamicAttribute> getDynamicAttributes() {
        return this.dynamicAttributes;
    }

    public void setDynamicAttributes(List<DynamicAttribute> dynamicAttributes) {
        this.dynamicAttributes = dynamicAttributes;
    }

    public QuestionnaireDefinitionModel getQuestionnaireDefinitionModel() {
        return questionnaireDefinitionModel;
    }

    public void setQuestionnaireDefinitionModel(QuestionnaireDefinitionModel questionnaireDefinitionModel) {
        this.questionnaireDefinitionModel = questionnaireDefinitionModel;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("label", getLabel())
                .append("title", getTitle())
                .append("description", getDescription())
                .toString();
    }

    /**
     * persistent field
     */
    private Set<QuestionnaireWorkflow> questionnaireWorkflows = new LinkedHashSet<QuestionnaireWorkflow>();

    /**
     * persistent field
     */
    private List<DynamicAttribute> dynamicAttributes = new ArrayList<DynamicAttribute>();

    /* persistant field */
    private String title;

    /* description persistant field */
    private String description;

    private QuestionnaireDefinitionModel questionnaireDefinitionModel;
}
