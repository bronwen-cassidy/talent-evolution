/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.attributes;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.common.exceptions.TalentStudioRuntimeException;
import com.zynap.talentstudio.common.lookups.LookupType;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.help.HelpTextItem;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;
import com.zynap.talentstudio.calculations.Calculation;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public final class DynamicAttribute extends ZynapDomainObject implements Comparable<DynamicAttribute>, Cloneable {


	private static final long serialVersionUID = -6134155447591180278L;

    /**
     * default constructor
     */
    public DynamicAttribute() {
    }

    public DynamicAttribute(Long id) {
        this.id = id;
    }

    public DynamicAttribute(String label, String type) {
        this.label = label;
        this.type = type;
    }

    public DynamicAttribute(Long id, String label, String type) {
        this(label, type);
        this.id = id;
    }

    public DynamicAttribute(Long id, String label, String type, String artefactType, boolean mandatory, boolean active, boolean searchable) {
        this();
        this.id = id;
        this.label = label;
        this.type = type;
        this.artefactType = artefactType;
        this.mandatory = mandatory;
        this.active = active;
        this.searchable = searchable;
    }

    public DynamicAttribute(String label, String type, String description) {
        this(label, type);
        this.description = description;
    }

    public String getExternalRefLabel() {
        return externalRefLabel;
    }

    public void setExternalRefLabel(String externalRefLabel) {
        this.externalRefLabel = externalRefLabel;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArtefactType() {
        return this.artefactType;
    }

    public void setArtefactType(String artefactType) {
        this.artefactType = artefactType;
    }

    public String getMaxSize() {
        return this.maxSize;
    }

    public void setMaxSize(String maxSize) {
        this.maxSize = maxSize;
    }

    public String getMinSize() {
        return this.minSize;
    }

    public void setMinSize(String minSize) {
        this.minSize = minSize;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public boolean isSearchable() {
        return searchable;
    }

    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getUniqueNumber() {
        return uniqueNumber;
    }

    public void setUniqueNumber(Double uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
    }

    public HelpTextItem getHelpTextItem() {
        return helpTextItem;
    }

    public void setHelpTextItem(HelpTextItem helpTextItem) {
        this.helpTextItem = helpTextItem;
    }

    public LookupType getRefersToType() {
        return refersToType;
    }

    public void setRefersToType(LookupType refersToType) {
        this.refersToType = refersToType;
    }

    public boolean isCurrentDate() {
        return isFunctionType() && CURRENT_DATE.equals(externalRefLabel);
    }

    public Calculation getCalculation() {
        return calculation;
    }

    public void setCalculation(Calculation calculation) {
        this.calculation = calculation;
    }

    public boolean isTextType() {
        return typeMatches(DA_TYPE_TEXTFIELD);
    }

    public boolean isSelectionType() {
        return typeMatches(DA_TYPE_STRUCT);
    }

    public boolean isMultiSelectionType() {
        return typeMatches(DA_TYPE_MULTISELECT);
    }

    public boolean isNumericType() {
        return typeMatches(DA_TYPE_NUMBER) || typeMatches(DA_TYPE_DECIMAL);
    }

    public boolean isSumType() {
        return typeMatches(DA_TYPE_SUM);
    }

    public boolean isLastUpdatedType() {
        return typeMatches(DA_TYPE_LAST_UPDATED);
    }

    public boolean isMappingType() {
        return typeMatches(DA_TYPE_ENUM_MAPPING);
    }

    public boolean isLinkType() {
        return typeMatches(DA_TYPE_HTMLLINK);
    }

    public boolean isImageType() {
        return typeMatches(DA_TYPE_IMAGE);
    }

    public boolean isLastUpdatedByType() {
        return typeMatches(DA_TYPE_LAST_UPDATED_BY);
    }

    public boolean isNodeType() {
        return isOrganisationUnitType() || isPositionType() || isSubjectType();
    }

    /**
     *
     * @return true if this extended attribute represents a date and time field or a last updated field, false otherwise
     */
    public boolean isDateTimeType() {
        return isDateTime() || isLastUpdatedType();
    }

    public boolean isSubjectType() {
        return typeMatches(DA_TYPE_SUBJECT);
    }

    public boolean isCurrencyType() {
        return typeMatches(DA_TYPE_CURRENCY);
    }

    public boolean isPositionType() {
        return typeMatches(DA_TYPE_POSITION);
    }

    public boolean isOrganisationUnitType() {
        return typeMatches(DA_TYPE_OU);
    }

    public boolean isDate() {
        return typeMatches(DA_TYPE_DATE);
    }

    public boolean isDateTime() {
        return typeMatches(DA_TYPE_DATETIMESTAMP) || typeMatches(DA_TYPE_LAST_UPDATED);
    }

    public boolean isTime() {
        return typeMatches(DA_TYPE_TIMESTAMP);
    }

    public boolean isTextAttribute() {
        return typeMatches(DA_TYPE_TEXTAREA) || isTextType();
    }

    public boolean isDateAttribute() {
        return isDate() || isDateTime() || isTime();
    }

    public boolean isBlogComment() {
        return typeMatches(DA_TYPE_BLOG_COMMENT);
    }

    /**
     *
     * @return true if this dynamic attribute represents a function, such as new Date() representation for the current date
     */
    public boolean isFunctionType() {
        return NODE_TYPE_FUNCTION.equals(artefactType); 
    }

    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    public boolean isDynamic() {
        return dynamic;
    }

    /**
     * Determines whether this dynamicAttribute is a calculated field or not
     * @return true if it is a calculated field, false otherwise.
     */
    public boolean isCalculated() {
        return calculated;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public boolean isHasHelpText() {
        return hasHelpText;
    }

    public void setHasHelpText(boolean hasHelpText) {
        this.hasHelpText = hasHelpText;
    }

    public void setCalculated(boolean calculated) {
        this.calculated = calculated;
    }

    public boolean supportsMultipleAnswers() {
        return isMultiSelectionType() || isDynamic() || isBlogComment();
    }

    public boolean typeMatches(final String expectedType) {
        return expectedType.equals(type);
    }

    public Collection<LookupValue> getActiveLookupValues() {
        return (refersToType != null) ? refersToType.getActiveLookupValues() : new ArrayList<LookupValue>();
    }

    public Long getQuestionnaireDefinitionId() {
        if (questionnaireDefinition != null) return questionnaireDefinition.getId();
        return questionnaireDefinitionId;
    }

    public void setQuestionnaireDefinitionId(Long questionnaireDefinitionId) {
        this.questionnaireDefinitionId = questionnaireDefinitionId;
    }

    public void setQuestionnaireDefinition(QuestionnaireDefinition questionnaireDefinition) {
        this.questionnaireDefinition = questionnaireDefinition;
    }

    public int compareTo(DynamicAttribute o) {
        return this.getLabel().compareTo(o.getLabel());
    }

    public List<DynamicAttributeReference> getReferences() {
        return references;
    }

    public void setReferences(List<DynamicAttributeReference> references) {
        this.references = references;
    }

    public void addReference(DynamicAttributeReference dynamicAttributeReference) {
        references.add(dynamicAttributeReference);
    }

    public void addChild(DynamicAttributeReference dynamicAttributeReference) {
        this.children.add(dynamicAttributeReference);
    }

    public List<DynamicAttributeReference> getChildren() {
        return children;
    }

    public void setChildren(List<DynamicAttributeReference> children) {
        this.children = children;
    }

    public List getFirstLevelChildren() {
        if(children.isEmpty()) return children;
        Collection result = CollectionUtils.select(children, new Predicate() {
            public boolean evaluate(Object object) {
                return ((DynamicAttributeReference) object).getParent() == null;
            }
        });
        return (List) result;
    }

    public void initLazy() {
        children.size();
        references.size();
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("label", getLabel())
                .append("type", getType())
                .append("artefactType", getArtefactType())
                .append("maxSize", getMaxSize())
                .append("minSize", getMinSize())
                .append("isMandatory", isMandatory())
                .append("isActive", isActive())
                .append("isSearchable", isSearchable())
                .append("description", getDescription())
                .append("lockId", getLockId())
                .toString();
    }

    public DynamicAttribute createAuditable() {
        DynamicAttribute attr = new DynamicAttribute(id, label, type);
        if(refersToType != null) {
            attr.setRefersToType(new LookupType(refersToType.getTypeId(), refersToType.getLabel(), ""));
        }
        return attr;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new TalentStudioRuntimeException("Unable to clone " + this.getClass(), e);
        }
    }

    public void setDecimalPlaces(Integer decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public Integer getDecimalPlaces() {
        return decimalPlaces;
    }

	/**
     * persistent field.
     */
    private String externalRefLabel;
    private String type;
    private String artefactType;
    private String maxSize;
    private String minSize;
    private boolean mandatory;
    private boolean searchable;
    private boolean dynamic;
    private boolean calculated;
    private String description;
    private Double uniqueNumber;
    private String mask;
    private boolean unique;
    private boolean hasHelpText;
    private Integer decimalPlaces;

    private LookupType refersToType;
    private Long questionnaireDefinitionId;
    private QuestionnaireDefinition questionnaireDefinition;
    private HelpTextItem helpTextItem;
    private Calculation calculation;
    private List<DynamicAttributeReference> references = new ArrayList<DynamicAttributeReference>();
    private List<DynamicAttributeReference> children = new ArrayList<DynamicAttributeReference>();

    public static final String DA_TYPE_NUMBER = "NUMBER";
    public static final String DA_TYPE_DECIMAL = "DECIMAL";
	public static final String DA_TYPE_DATE = "DATE";
    public static final String DA_TYPE_TEXTFIELD = "TEXT";
    public static final String DA_TYPE_TEXTAREA = "TEXTAREA";
    public static final String DA_TYPE_STRUCT = "STRUCT";
    public static final String DA_TYPE_IMAGE = "IMG";
    public static final String DA_TYPE_HTMLLINK = "LINK";
    public static final String DA_TYPE_DATETIMESTAMP = "DATETIME";
    public static final String DA_TYPE_TIMESTAMP = "TIME";

    /* question only attribute types*/
    public static final String DA_TYPE_MULTISELECT = "MULTISELECT";
    public static final String DA_TYPE_ENUM_MAPPING = "ENUMMAPPING";
    public static final String DA_TYPE_BLOG_COMMENT = "COMMENTS";
    public static final String DA_TYPE_SUM = "SUM";
    public static final String DA_TYPE_LAST_UPDATED_BY = "LASTUPDATEDBY";
    public static final String DA_TYPE_LAST_UPDATED = "LASTUPDATED";

    public static final String DA_TYPE_OU = "ORGANISATION";
    public static final String DA_TYPE_SUBJECT = "SUBJECT";
    public static final String DA_TYPE_CURRENCY = "CURRENCY";
    public static final String DA_TYPE_POSITION = "POSITION";

    public static final String NODE_TYPE_FUNCTION = "FUNC";
    public static final String CURRENT_DATE = "currentdate";

    public static final DynamicAttribute DA_TYPE_NUMBER_O = new DynamicAttribute("", DA_TYPE_NUMBER);
    public static final DynamicAttribute DA_TYPE_DATE_O = new DynamicAttribute("", DA_TYPE_DATE);
    public static final DynamicAttribute DA_TYPE_TEXTFIELD_O = new DynamicAttribute("", DA_TYPE_TEXTFIELD);
    public static final DynamicAttribute DA_TYPE_TEXTAREA_O = new DynamicAttribute("", DA_TYPE_TEXTAREA);
    public static final DynamicAttribute DA_TYPE_CURRENCY_O = new DynamicAttribute("", DA_TYPE_CURRENCY);
    public static final DynamicAttribute DA_TYPE_STRUCT_O = new DynamicAttribute("", DA_TYPE_STRUCT);
    public static final DynamicAttribute DA_TYPE_MULTISELECT_O = new DynamicAttribute("", DA_TYPE_MULTISELECT);

    public static final DynamicAttribute DA_TYPE_IMAGE_O = new DynamicAttribute("", DA_TYPE_IMAGE);
    public static final DynamicAttribute DA_TYPE_HTMLLINK_O = new DynamicAttribute("", DA_TYPE_HTMLLINK);
    public static final DynamicAttribute DA_TYPE_DATETIMESTAMP_O = new DynamicAttribute("", DA_TYPE_DATETIMESTAMP);
    public static final DynamicAttribute DA_TYPE_TIMESTAMP_O = new DynamicAttribute("", DA_TYPE_TIMESTAMP);
    public static final DynamicAttribute DA_TYPE_OU_0 = new DynamicAttribute("", DA_TYPE_OU);
    
    public static final DynamicAttributeDTO DATE_OF_BITH_ATTR = new DynamicAttributeDTO(DA_TYPE_DATE, "Date of Birth", new Long(-200), Node.SUBJECT_UNIT_TYPE_, "", "dateofbirth", null);
    public static final String DA_TYPE_SELECT = "SELECT";
    public static final String TIME_DELIMITER = ":";
    public static final String DATE_TIME_DELIMITER = " ";

}
