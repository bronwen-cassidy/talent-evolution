/**
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.questionnaires.support;

import com.zynap.common.util.StringUtil;
import com.zynap.talentstudio.common.exceptions.TalentStudioRuntimeException;
import com.zynap.talentstudio.common.lookups.LookupType;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.questionnaires.QuestionAttribute;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Rule factory to create the questionnaireDefinition
 *
 * @author bcassidy
 * @version 0.1
 * @since 16-Jul-2005 02:29:17
 */
public class Question extends BaseQuestion implements Cloneable {

    private static final long serialVersionUID = -602539711646947008L;

    public Question() {
        this.dynamicAttribute = new DynamicAttribute();
        this.dynamicAttribute.setArtefactType(Node.QUESTIONNAIRE_TYPE);
        this.dynamicAttribute.setMandatory(false);
    }

    public String getType() {
        return questionType;
    }

    public void setType(String type) {
        final String key = type.toUpperCase();
        this.questionType = key;
        String daType = dynamicAttributeTypeMappings.get(key);
        if (daType == null) daType = key;
        if (TYPE_POSITIVEINTEGER.equals(key)) dynamicAttribute.setMinSize("0");
        dynamicAttribute.setType(daType);
    }

    public String getLabel() {
        return this.label;
    }

    public void setMandatory(String value) {
        dynamicAttribute.setMandatory(!"N".equalsIgnoreCase(value));
    }

    public void setLabel(String label) {
        this.dynamicAttribute.setLabel(label);
        final Double uniqueNum = QuestionnaireDefinitionFactory.generateUniqueNumber();
        this.dynamicAttribute.setUniqueNumber(uniqueNum);
        this.dynamicAttribute.setExternalRefLabel(StringUtil.clean(dynamicAttribute.getLabel().trim().toLowerCase()) + uniqueNum);
        this.label = label;
    }

    public String getDescription() {
        return dynamicAttribute.getDescription();
    }

    public void setDescription(String description) {
        dynamicAttribute.setDescription(description);
    }

    public DynamicAttribute getDynamicAttribute() {        
        return dynamicAttribute;
    }

    final List<DynamicAttribute> getDynamicAttributes(QuestionnaireDefinition questionnaireDefinition) {
        List<DynamicAttribute> attributes = new ArrayList<DynamicAttribute>();
        dynamicAttribute.setQuestionnaireDefinition(questionnaireDefinition);
        attributes.add(dynamicAttribute);
        return attributes;
    }

    public void setDynamic(boolean dynamic) {
        this.dynamicAttribute.setDynamic(dynamic);
    }

    public boolean isDynamic() {
        return dynamicAttribute != null && dynamicAttribute.isDynamic();
    }


    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        dynamicAttribute.setDescription(title);
        this.title = title;
    }

    public String getTextId() {
        return textId;
    }

    /**
     * Sets the external ref label on the dynamic attribute used in the integration iterface.
     *
     * @param title some unique string identifier.
     */
    public void setTextId(String title) {
        textId = title;
    }

    public void setLineItem(QuestionLineItem questionLineItem) {
        this.questionLineItem = questionLineItem;
    }

    public QuestionLineItem getLineItem() {
        return questionLineItem;
    }

    /**
     * Add an operand.
     *
     * @param reference the question reference a wrapper for the dynamicAttributeReference
     */
    public void addOperand(QuestionReference reference) {
        reference.setOperandParent(this);
        this.operands.add(reference);
    }

    /**
     * Add a mapping.
     *
     * @param reference the question reference a wrapper for the dynamicAttributeReference
     * @throws IllegalQuestionMappingException if a mapping is not allowed for the question type
     */
    public void addMapping(QuestionReference reference) throws IllegalQuestionMappingException {
        reference.setMappingQuestionParent(this);
        if (!mappings.isEmpty()) {
            QuestionReference prev = mappings.get(mappings.size() - 1);
            if (!prev.getReferenceId().equals(reference.getReferenceId())) {
                throw new IllegalQuestionMappingException("Question with id: " + getTextId() + " has an invalid mapping source id", "error.invalid.mapping.source", new Object[]{label});
            }
        }
        // get all the created lookup values, create a lookup type and tie them together
        LookupType lookupType = createLookupType();
        final LookupValue referanceValue = reference.getLookupValue();
        if (referanceValue != null) lookupType.addLookupValue(referanceValue);

        List childMappings = reference.getChildren();
        for (int i = 0; i < childMappings.size(); i++) {
            QuestionReference questionReference = (QuestionReference) childMappings.get(i);
            LookupValue value = questionReference.getLookupValue();
            if (value != null) lookupType.addLookupValue(value);
        }
        mappings.add(reference);
    }

    public List<QuestionReference> getReferences() {
        List<QuestionReference> allReferences = new ArrayList<QuestionReference>();
        for (int i = 0; i < mappings.size(); i++) {
            QuestionReference questionReference = mappings.get(i);
            allReferences.add(questionReference);
            allReferences.addAll(questionReference.getChildren());
        }
        allReferences.addAll(operands);
        return allReferences;
    }

    public void addLookupValue(LookupValue value) {
        value.setDescription(value.getLabel());
        value.setSortOrder(SORT_ORDER++);
        createLookupType().addLookupValue(value);
    }

    public void setMultiQuestion(MultiQuestion multiQuestion) {
        this.multiQuestion = multiQuestion;
    }

    public MultiQuestion getMultiQuestion() {
        return this.multiQuestion;
    }

    public boolean belongsToMultiQuestion() {
        return this.multiQuestion != null;
    }

    public boolean isMultiQuestion() {
        return false;
    }

    public boolean isSum() {
        return DynamicAttribute.DA_TYPE_SUM.equals(dynamicAttribute.getType());
    }

    public boolean isEnumMapping() {
        return DynamicAttribute.DA_TYPE_ENUM_MAPPING.equals(dynamicAttribute.getType());
    }

    public boolean isDisplayableLabel() {
        return displayableLabel;
    }

    public void setDisplayableLabel(boolean displayableLabel) {
        this.displayableLabel = displayableLabel;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isManagerWrite() {
        return managerWrite;
    }

    public void setManagerWriteValue(String managerWrite) {
        this.managerWrite = "Y".equalsIgnoreCase(managerWrite);
    }

    public void setQuestionnaireGroup(QuestionnaireGroup questionnaireGroup) {
        this.questionnaireGroup = questionnaireGroup;
    }

    public QuestionnaireGroup getQuestionnaireGroup() {
        return questionnaireGroup != null ? questionnaireGroup : multiQuestion != null ? multiQuestion.getQuestionnaireGroup() : null;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    protected final Object clone() {
        try {
            Question clonedQuestion = (Question) super.clone();

            final DynamicAttribute cloned = (DynamicAttribute) dynamicAttribute.clone();

            // generate new unique number for dynamic attribute
            Double uniqueNumber = QuestionnaireDefinitionFactory.generateUniqueNumber();
            cloned.setUniqueNumber(uniqueNumber);
            clonedQuestion.dynamicAttribute = cloned;

            return clonedQuestion;

        } catch (CloneNotSupportedException e) {
            throw new TalentStudioRuntimeException("Unable to clone " + this.getClass(), e);
        }
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Question)) return false;

        final Question question = (Question) o;
        return new EqualsBuilder()
                .append(label, question.label)
                .append(questionType, question.questionType)
                .append(target, question.target)
                .append(length, question.length)
                .append(title, question.title)
                .append(displayableLabel, question.displayableLabel)
                .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(title)
                .append(target)
                .append(questionType)
                .append(label)
                .append(length)
                .append(displayableLabel)
                .toHashCode();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Question[");
        stringBuffer.append("\r\n label=").append(label);
        stringBuffer.append("\r\n title=").append(title);
        stringBuffer.append("\r\n target=").append(target);
        stringBuffer.append("]");

        return stringBuffer.toString();
    }

    private LookupType createLookupType() {
        if (dynamicAttribute.getRefersToType() == null) {
            final String generatedLabel = this.label + QuestionnaireDefinitionFactory.generateUniqueNumber();
            final String description = this.label + "-" + dynamicAttribute.getDescription();
            LookupType lookupType = LookupType.createQuestionnaireLookupType(generatedLabel, description);
            dynamicAttribute.setRefersToType(lookupType);
        }
        return dynamicAttribute.getRefersToType();
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getCellClass() {
        return cellClass;
    }

    public void setCellClass(String cellClass) {
        this.cellClass = cellClass;
    }

    private QuestionnaireGroup questionnaireGroup;
    private MultiQuestion multiQuestion;
    private DynamicAttribute dynamicAttribute;
    private QuestionLineItem questionLineItem;

    private String title;
    private String target;
    private String questionType;
    private String label;
    private String textId;
    private String width;
    private String cellClass;

    private List<QuestionReference> mappings = new ArrayList<QuestionReference>();
    private List<QuestionReference> operands = new ArrayList<QuestionReference>();

    /* should the name of this question be displayed on the questionnaire view */
    private boolean displayableLabel = true;
    /* determines if this question is only editable by a manager */
    private boolean managerWrite;

    /* determine if this question is a hidden one or not default is false*/
    private boolean hidden;
    private Integer sortOrder;

    /* says how long should text boxes and text areas may be */
    private int length;

    private int SORT_ORDER = 0;

    public static final String TYPE_STATUS = "STATUS";
    public static final String TYPE_POSITIVEINTEGER = "POSITIVEINTEGER";

    private static Map<String, String> dynamicAttributeTypeMappings = new HashMap<String, String>(14);
    static {
        dynamicAttributeTypeMappings.put(DynamicAttribute.DA_TYPE_SELECT, DynamicAttribute.DA_TYPE_STRUCT);
        dynamicAttributeTypeMappings.put("TEXTBOX", DynamicAttribute.DA_TYPE_TEXTAREA);
        dynamicAttributeTypeMappings.put(TYPE_STATUS, DynamicAttribute.DA_TYPE_STRUCT);
        dynamicAttributeTypeMappings.put("INTEGER", DynamicAttribute.DA_TYPE_NUMBER);
        dynamicAttributeTypeMappings.put("IMAGE", DynamicAttribute.DA_TYPE_IMAGE);
        dynamicAttributeTypeMappings.put("RADIO", DynamicAttribute.DA_TYPE_STRUCT);
        dynamicAttributeTypeMappings.put(QuestionAttribute.TYPE_CHECKBOX, DynamicAttribute.DA_TYPE_MULTISELECT);
        dynamicAttributeTypeMappings.put(TYPE_POSITIVEINTEGER, DynamicAttribute.DA_TYPE_NUMBER);
    }
}
