/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.organisation.attributes;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.common.lookups.LookupType;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.util.FormatterFactory;

import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.ArrayList;

/**
 * The class represents a cheaper dynamicAttribute with only the minimum of associations (just the lookupType)
 *
 * @author bcassidy
 * @version 0.1
 * @since 12-Jul-2007 10:18:26
 */
public class DynamicAttributeDTO extends ZynapDomainObject {


    public DynamicAttributeDTO(String type, String label, Long id, String artefactType, String description, String modifiedLabel, LookupType refersTo) {
        this.type = type;
        this.label = label;
        this.id = id;
        this.artefactType = artefactType;
        this.description = description;
        this.modifiedLabel = modifiedLabel;
        this.refersToType = refersTo;
    }

    public DynamicAttributeDTO(String type, String label, Long id, String artefactType, String description, String modifiedLabel, LookupType refersTo,
                               boolean calculated, boolean active, boolean mandatory, boolean searchable) {
        this(type, label, id, artefactType, description, modifiedLabel, refersTo);
        this.calculated = calculated;
        this.active = active;
        this.mandatory = mandatory;
        this.searchable = searchable;
    }

    public DynamicAttributeDTO(String type, String label, Long id, String artefactType, String description, String modifiedLabel, LookupType refersTo,
                               boolean mandatory) {
        this(type, label, id, artefactType, description, modifiedLabel, refersTo);
        this.mandatory = mandatory;
        this.active = true;
        this.searchable = true;
    }

    public DynamicAttributeDTO(String type, String label, Long id, String artefactType, String description, String modifiedLabel) {
        this(type, label, id, artefactType, description, modifiedLabel, null);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArtefactType() {
        return artefactType;
    }

    public void setArtefactType(String artefactType) {
        this.artefactType = artefactType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModifiedLabel() {
        return modifiedLabel;
    }

    public void setModifiedLabel(String modifiedLabel) {
        this.modifiedLabel = modifiedLabel;
    }

    public LookupType getRefersToType() {
        return refersToType;
    }

    public boolean isCalculated() {
        return calculated;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public boolean isSearchable() {
        return searchable;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getDisplayValue() {
        if (StringUtils.hasText(value)) {
            if (DynamicAttribute.DA_TYPE_DATE.equals(type)) {
                return FormatterFactory.getDateFormatter().formatDateAsString(value);
            } else if (isDateTime()) {
                return FormatterFactory.getDateFormatter().formatDateTimeAsString(value);
            } 
        }
        return value;
    }

    public final void setDate(String date) {
        this.value = date;
    }

    public final void setHour(String hour) {
        this.hour = hour;
    }

    public final void setMinute(String minute) {
        this.minute = minute;
    }

    public final String getDate() {
        return value;
    }

    public final String getHour() {
        return hour;
    }

    public final String getMinute() {
        return minute;
    }

    public final String getDateTime() {
        final String myTime = getTime();
        String date = getDate();
        if (StringUtils.hasText(date) || StringUtils.hasText(myTime)) {
            return date + DATE_TIME_DELIMITER + myTime;
        }

        return "";
    }

    public final String getTime() {
        if (StringUtils.hasText(hour) || StringUtils.hasText(minute)) {
            return hour + TIME_DELIMITER + minute;
        }
        return "";
    }

    public String getRangeMessage() {
        return "";
    }

    public boolean isDateTime() {
        return DynamicAttribute.DA_TYPE_DATETIMESTAMP.equals(type);
    }

    public Collection<LookupValue> getActiveLookupValues() {
        return (refersToType != null) ? refersToType.getActiveLookupValues() : new ArrayList<LookupValue>();
    }

    private String type;
    private String artefactType;
    private String description;
    private String modifiedLabel;
    private LookupType refersToType;
    private boolean calculated;
    private boolean mandatory;
    private boolean searchable;
    private String value;
    private String hour;
    private String minute;

    private final String TIME_DELIMITER = DynamicAttribute.TIME_DELIMITER;
    private final String DATE_TIME_DELIMITER = DynamicAttribute.DATE_TIME_DELIMITER;
}
