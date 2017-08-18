package com.zynap.talentstudio.web.organisation.attributes;

import com.zynap.common.util.UploadedFile;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.calculations.Calculation;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.AttributeValueFile;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.util.CurrencyFormatter;
import com.zynap.talentstudio.util.FormatterFactory;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
* Class or Interface description.
*
* @author jsuiras
* @since 07-Feb-2005 15:38:37
* @version 0.1
*/
public class AttributeWrapperBean implements Serializable, Comparable, Cloneable, FormAttribute {

    public AttributeWrapperBean(String name, String id, DynamicAttribute attributeDefinition) {
        this(attributeDefinition);
        this.name = name;
        this.id = id;
    }

    public AttributeWrapperBean(DynamicAttribute attributeDefinition) {
        this(AttributeValue.create(attributeDefinition));
    }

    public AttributeWrapperBean(AttributeValue attributeValue) {
        this.attributeDefinition = attributeValue.getDynamicAttribute();
        setAttributeValue(attributeValue);
    }

    public AttributeWrapperBean(Node node, DynamicAttribute attributeDefinition) {
         this(AttributeValue.create(node, attributeDefinition));

    }

    public void setNodeLabel(String nodeLabel) {
        this.nodeLabel = nodeLabel;
        if(!StringUtils.hasText(name)) name = nodeLabel;
    }

    public final DynamicAttribute getAttributeDefinition() {
        return attributeDefinition;
    }

    public final void setAttributeDefinition(DynamicAttribute attributeDefinition) {
        this.attributeDefinition = attributeDefinition;
    }

    public final AttributeValue getAttributeValue() {
        return attributeValue;
    }

    public AttributeValue getModifiedAttributeValue() {
        attributeValue.initialiseNodeExtendedAttributes(getValue());
        return attributeValue;
    }

    public final String getMinSize() {
        return getAttributeDefinition().getMinSize();
    }

    public final String getMaxSize() {
        return getAttributeDefinition().getMaxSize();
    }

    public final boolean isMandatory() {
        return getAttributeDefinition().isMandatory();
    }

    public final void setAttributeValue(AttributeValue attributeValue) {
        this.attributeValue = attributeValue;
        initDateTimeFields(attributeValue);
    }

    public final UploadedFile getFile() {
        return null;
    }

    public final String getUploadedFileOriginalName() {
        return this.uploadedFile != null ? uploadedFile.getFileName() : null;
    }

    public final void setFile(UploadedFile file) {

        if (file != null) {

            if (file.getFileSize() > 0) {

                // set content on AttributeValue if file parameter has content
                AttributeValueFile attfile = (AttributeValueFile) attributeValue;
                attfile.setBlobValue(file.getBlobValue());
                attfile.setValue(file.getFileName());
                this.uploadedFile = file;
                // check dirtyFile again
                dirtyFile = file.isInvalid();

            } else if (!attributeValue.hasValue()) {
                this.uploadedFile = file;
                // check dirtyFile again
                dirtyFile = file.isInvalid();
            }

        } else {
            this.dirtyFile = true;
        }
    }

    public final boolean isDirtyFile() {
        return dirtyFile;
    }

    public final UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public final String getDateTime() {
        final String myTime = getTime();
        if (StringUtils.hasText(date) || StringUtils.hasText(myTime)) {
            return date + DynamicAttribute.DATE_TIME_DELIMITER + myTime;
        }

        return "";
    }

    public final String getTime() {
        if (StringUtils.hasText(hour) || StringUtils.hasText(minute)) {
            return hour + DynamicAttribute.TIME_DELIMITER + minute;
        }
        return "";
    }

    public final String getDisplayValue() {

        if (attributeDefinition.isCalculated()) {
            final Node node = attributeValue.getNode();
            Calculation calculation = attributeDefinition.getCalculation();
            return calculation.execute(node).getValue();
        } else {
            if (attributeDefinition.isDate()) {
                return FormatterFactory.getDateFormatter().formatDateAsString(getDate());
            } else if (attributeDefinition.isDateTimeType()) {
                return FormatterFactory.getDateFormatter().formatDateTimeAsString(getDateTime());
            } else if (attributeDefinition.isTime()) {
                return getTime();
            } else if (attributeDefinition.isCurrencyType()) {
	            return CurrencyFormatter.formatCurrency(attributeValue.getValue(), attributeValue.getCurrency());
            } else if (attributeDefinition.isNodeType() || attributeDefinition.isLastUpdatedByType()) {
                return this.nodeLabel;
            } else {
                return attributeValue.getDisplayValue();
            }
        }
    }

	public final String getDateTimeDateDisplayValue() {
        final String value = getValue();
        if (StringUtils.hasText(value)) {
            String[] dates = StringUtils.tokenizeToStringArray(value, DynamicAttribute.DATE_TIME_DELIMITER);
            return FormatterFactory.getDateFormatter().formatDateAsString(dates[0]);
        }
        return "";
    }

    public String getValue() {
        if (attributeDefinition.isDate()) {
            return getDate();
        } else if (attributeDefinition.isDateTimeType()) {
            return getDateTime();
        } else if (attributeDefinition.isTime()) {
            return getTime();
        } else {
            return attributeValue.getValue();
        }
    }

    public void setValue(String value) {
        attributeValue.setValue(value);
    }

    public final String getFieldName() {

        if (attributeDefinition.isDate()) {
            return ".date";
        } else if (attributeDefinition.isDateTime()) {
            return ".dateTime";
        } else if (attributeDefinition.isTime()) {
            return ".time";
        } else if (attributeDefinition.isImageType()) {
            return ".file";
        } else {
            return ".value";
        }
    }

    public final Collection getActiveLookupValues() {
        return attributeDefinition.getActiveLookupValues();
    }

    /**
     * This method is used by the jsp's
     *
     * @param label set the label for the attribute
     */
    public void setLabel(String label) {
        this.name = label;
        if(attributeDefinition.isNodeType() || attributeDefinition.isLastUpdatedByType()) {
            this.nodeLabel = label;    
        }
    }

    public final void clearValue() {
        attributeValue.clearValue();
        this.date = "";
        this.hour = "";
        this.minute = "";
    }

    public String getType() {
        return attributeDefinition.getType();
    }

    public String getLabel() {
        return StringUtils.hasText(name) ? name : attributeDefinition.getLabel();
    }

    public final String getId() {
        final Long attributeDefId = attributeDefinition.getId();
        return id != null ? id : (attributeDefId != null ? attributeDefId.toString() : null);
    }

    public NodeExtendedAttribute[] getMultiSelectValues() {
        if(attributeValue != null) {
            final List<NodeExtendedAttribute> attributeList = attributeValue.getNodeExtendedAttributes();
            return attributeList.toArray(new NodeExtendedAttribute[attributeList.size()]);
        }
        return new NodeExtendedAttribute[0];
    }

    //voiding getId as bronie suggest is not right
    public Long getAttributeId(){
        return attributeDefinition.getId();  //can return null
    }
    public final boolean isDerivedAttribute() {
        return (IDynamicAttributeService.PP_SUB_DERIVED_ATT_DEFINITION == getAttributeDefinition()
                || IDynamicAttributeService.PP_SUP_DERIVED_ATT_DEFINITION == getAttributeDefinition()
                || IDynamicAttributeService.SP_SUB_DERIVED_ATT_DEFINITION == getAttributeDefinition()
                || IDynamicAttributeService.SP_SUP_DERIVED_ATT_DEFINITION == getAttributeDefinition());
    }

    public final boolean supportsMultipleAnswers() {
        return attributeDefinition != null && attributeDefinition.supportsMultipleAnswers();
    }

    public final boolean isCoreAttribute() {
        return (id != null && !isDerivedAttribute());
    }

    public final boolean isSelectionType() {
        return attributeDefinition != null && attributeDefinition.isSelectionType();
    }

    public final boolean isMappingType() {
        return attributeDefinition != null && attributeDefinition.isMappingType();
    }

    public final boolean isOrganisationUnit() {
        return AnalysisAttributeHelper.isOrgUnitAttribute(id);
    }

    public final boolean isDynamic() {
        return attributeDefinition != null && attributeDefinition.isDynamic();
    }

    public final int compareTo(Object o) {
        AttributeWrapperBean wrapper = (AttributeWrapperBean) o;
        int dif = this.typeOrder() - wrapper.typeOrder();
        if (dif != 0)
            return dif;
        else
            return this.getLabel().compareTo(wrapper.getLabel());
    }

    public Object clone() throws CloneNotSupportedException {
        final AttributeWrapperBean bean = new AttributeWrapperBean(name, id, attributeDefinition);
        bean.setIsOrgunitBranch(isOrgunitBranch);
        return bean;
    }

    public String getTitle() {
        return attributeDefinition != null ? attributeDefinition.getDescription() : null;
    }

    public final void setDate(String date) {
        this.date = date;
    }

    public final void setHour(String hour) {
        this.hour = hour;
    }

    public final void setMinute(String minute) {
        this.minute = minute;
    }

    public final String getDate() {
        return date;
    }

    public final String getHour() {
        return hour;
    }

    public final String getMinute() {
        return minute;
    }

	public String getCurrency() {
		return attributeValue.getCurrency();
	}

	public void setCurrency(String currency) {
		attributeValue.setCurrency(currency);
	}

	public final String getRangeMessage() {
        return AttributeWrapperMessageResolver.getRangeMessage(attributeDefinition);
    }

    public final String[] getRangeParams() {
        return AttributeWrapperMessageResolver.getMessageParams(attributeDefinition);
    }

    public final boolean isEditable() {
        return true;
    }

    public final boolean isLineItem() {
        return false;
    }

    public boolean isHidden() {
        return false;
    }

    public int getMultiSelectSize() {
        int defaultSize = 10;
        final Collection multiSelectValues = getActiveLookupValues();
        if(multiSelectValues.size() > 0 && multiSelectValues.size() <= 10) {
            defaultSize = multiSelectValues.size(); 
        }
        return defaultSize;
    }

    public boolean isHasHelpText() {
        return attributeDefinition != null && attributeDefinition.isHasHelpText();
    }

    public String toString() {
        return id != null ? id : attributeDefinition != null ? attributeDefinition.getLabel() : super.toString();
    }

    private void initDateTimeFields(AttributeValue attributeValue) {

        if (attributeDefinition != null) {
            final String value = attributeValue.getValue();
            final boolean hasValue = StringUtils.hasText(value);
            if (hasValue) {
                if (attributeDefinition.isDate()) {
                    this.date = value;
                } else if (attributeDefinition.isTime()) {
                    String[] values = StringUtils.delimitedListToStringArray(value, DynamicAttribute.TIME_DELIMITER);
                    this.hour = values[0];
                    this.minute = values[1];
                } else if (attributeDefinition.isDateTimeType()) {
                    String[] values = StringUtils.delimitedListToStringArray(value, DynamicAttribute.DATE_TIME_DELIMITER);
                    String[] times = StringUtils.delimitedListToStringArray(values[1], DynamicAttribute.TIME_DELIMITER);
                    this.date = values[0];
                    this.hour = times[0];
                    this.minute = times[1];
                }
            }
        }
    }

    private int typeOrder() {
        if (isCoreAttribute())
            return 0;
        else if (!isDerivedAttribute())
            return 1;
        else
            return 2;
    }

    public void setIsOrgunitBranch(boolean orgunitBranch) {
        isOrgunitBranch = orgunitBranch;
    }

    public boolean isOrgunitBranch() {
        return isOrgunitBranch;
    }

    public Long getAttributeDefinitionId() {
        return attributeDefinition != null ? attributeDefinition.getId() : null;
    }

    private boolean dirtyFile;
    protected DynamicAttribute attributeDefinition;
    protected AttributeValue attributeValue;

    private String name;

    private String id;
    private String date = "";
    private String hour = "";
    private String minute = "";
    private UploadedFile uploadedFile;
    protected String nodeLabel;
    private boolean isOrgunitBranch;
}

