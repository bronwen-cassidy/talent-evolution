/*
 * Copyright (c) 2004 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.common.lookups;

import com.zynap.common.util.StringUtil;
import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.util.collections.DomainObjectCollectionHelper;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


/**
 * Value object for lookup types and their values.
 *
 * @author Andreas Andersson
 * @version $Revision: $
 *          $Id: $
 * @since 02/04/2004
 */
public class LookupType extends ZynapDomainObject {

    private static final long serialVersionUID = 4055263227629105288L;

    public LookupType() {
    }

    public LookupType(LookupType other) {
        this.typeId = other.typeId;
        this.type = other.type;
        this.uneditable = other.isUneditable();
        this.lookupValues = new LinkedList<LookupValue>(other.getLookupValues());
        this.label = other.label;
        this.description = other.description;
    }

    public LookupType(String typeId) {
        this.typeId = typeId;
    }

    public LookupType(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public LookupType(String typeId, String label, String description) {
        this(label, description);
        this.typeId = typeId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return (long) this.typeId.hashCode();
    }

    public List<LookupValue> getLookupValues() {
        if (lookupValues == null) lookupValues = new LinkedList<>();
        return lookupValues;
    }

    public void setLookupValues(List<LookupValue> lookupValues) {
        this.lookupValues = lookupValues;
    }

    public void addLookupValue(LookupValue value) {
	    value.setLookupType(this);
        if (value.isBlank()) {
            value.setSortOrder(0);
            getLookupValues().add(0, value);
        } else {
            getLookupValues().add(value);
        }
    }

    /**
     * Get the active lookup values only.
     * @return Collection containing LookupValue objects
     */
    public Collection<LookupValue> getActiveLookupValues() {
        //noinspection unchecked
        return CollectionUtils.select(getLookupValues(), new Predicate() {
            public boolean evaluate(Object object) {
                LookupValue lookupValue = (LookupValue) object;
                return lookupValue.isActive();
            }
        });
    }

	/**
	 * Get the active lookup values only.
	 * @return Collection containing LookupValue objects
	 */
	public List<String> getConcatenatedActiveLookupValues() {
		final List<LookupValue> activeLookupValues = getLookupValues();
		Collections.sort(activeLookupValues, new Comparator<LookupValue>() {
			@Override
			public int compare(LookupValue o1, LookupValue o2) {
				return new Integer(o1.getSortOrder()).compareTo(o2.getSortOrder());
			}
		});
		List<String> result = new ArrayList<>();
		for(LookupValue lv : activeLookupValues) {
			if(!result.contains(lv.getLabel())) result.add(lv.getLabel());
		}
		return result;
	}

    public boolean getUneditable() {
        return uneditable;
    }

    public boolean isUneditable() {
        return uneditable;
    }

    public void setUneditable(boolean uneditable) {
        this.uneditable = uneditable;
    }

    /**
     * Check if user defined lookup type.
     *
     * @return True if type equals USER_TYPE
     */
    public boolean isUserDefined() {
        return USER_TYPE.equals(getType());
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("typeId", getTypeId())
                .append("description", getDescription())
                .append("type", getType())
                .append("active", isActive())
                .append("uneditable", isUneditable())
                .append("label", getLabel())
                .toString();
    }

    /**
     * Get lookup value by id.
     *
     * @param id The id
     * @return LookupValue or null
     */
    public LookupValue getLookupValue(final Long id) {
        LookupValue lookupValue = null;
        final Collection currentLookupValues = getLookupValues();
        if (!currentLookupValues.isEmpty()) {
            return (LookupValue) DomainObjectCollectionHelper.findById(currentLookupValues, id);
        }

        return lookupValue;
    }

    public LookupValue getLookupValue(String value) {
        LookupValue lookupValue = null;
        final Collection currentLookupValues = getLookupValues();
        if (!currentLookupValues.isEmpty()) {
            return (LookupValue) DomainObjectCollectionHelper.findByField(currentLookupValues, "valueId",value);
        }

        return lookupValue;
    }

    public boolean hasBlankValue() {
        for (LookupValue lookupValue : lookupValues) {
            if(lookupValue.isActive() && lookupValue.isBlank()) return true;
        }
        return false;
    }


    public static LookupType createQuestionnaireLookupType(String label, String description) {
		LookupType lookupType = new LookupType(label, description);
		lookupType.setActive(true);
		lookupType.setUneditable(true);
		lookupType.setType(QUESTIONNAIRE_TYPE);
		return lookupType;
	}

	private String typeId;

    private String description;

    private List<LookupValue> lookupValues;

    /**
     * Indicates whether the lookup type was created by a user or was preloaded.
     * <br> Defaults to user type.
     */
    private String type = USER_TYPE;

    /**
     * Indicates that lookup type cannot be modified - maps to IS_SYSTEM column in db for now - todo change column name.
     */
    private boolean uneditable;

    public static final String USER_TYPE = "USER";
	public static final String QUESTIONNAIRE_TYPE = "QUESTIONNAIRE";
}
