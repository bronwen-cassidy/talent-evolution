/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.reportingchart.settings;

import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.preferences.Preference;
import com.zynap.talentstudio.preferences.properties.AttributePreference;
import com.zynap.talentstudio.preferences.properties.ConditionalAttributePreference;
import com.zynap.talentstudio.preferences.properties.SwitchAttributePreference;
import com.zynap.talentstudio.web.preferences.AttributePreferenceAccessor;
import com.zynap.talentstudio.web.preferences.AttributePreferenceModifier;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class PositionSettingsWrapperBean extends ViewSettingsWrapperBean {

    public PositionSettingsWrapperBean(Preference preference) {
        super(preference);
    }

    public void setPrimaryAssociationColor(String value) {
        String[] colors = StringUtils.delimitedListToStringArray(value, ":");
        AttributePreferenceModifier.modifyAttributePreference(preferenceCollection, Position.class.getName(), POSITION_PRIMARY_ASSOCIATION_KEY, BG_COLOR_FORMAT_ATTR_KEY, colors[0]);
    }

    public void setSecondaryAssociationColor(String value) {
        String[] colors = StringUtils.delimitedListToStringArray(value, ":");
        AttributePreferenceModifier.modifyAttributePreference(preferenceCollection, Position.class.getName(), SECONDARY_ASSOCIATION_KEY, BG_COLOR_FORMAT_ATTR_KEY, colors[0]);
    }

    public void setSecondAssociationLineWidth(String value) {
        AttributePreferenceModifier.modifyAttributePreference(preferenceCollection, Position.class.getName(), SECONDARY_ASSOCIATION_KEY, LINE_HEIGHT, value);
    }

    public void setPrimaryAssociationLineWidth(String value) {
        AttributePreferenceModifier.modifyAttributePreference(preferenceCollection, Position.class.getName(), POSITION_PRIMARY_ASSOCIATION_KEY, LINE_HEIGHT, value);
    }

    public void setPrimaryAssociationShowable(boolean value) {
        AttributePreferenceModifier.modifyAttributePreference(preferenceCollection, Position.class.getName(), POSITION_PRIMARY_ASSOCIATION_KEY, null, value);
    }

    public void setSecondaryAssociationShowable(boolean value) {
        AttributePreferenceModifier.modifyAttributePreference(preferenceCollection, Position.class.getName(), SECONDARY_ASSOCIATION_KEY, null, value);
    }

    public Collection<ValueAttributeView> getAttributeViews() {
        return positionAttributeViews;
    }

    public Collection getSelectionAttributes() {
        return positionSelectionAttributes;
    }

    public String getSelectedSelectionAttributeName() {
        return selectedPositionSelectionAttribute;
    }

    public void assignSelectedAttributes(String[] values) {
        disableAllPreferences(getPositionPref().getAttributePrefs());
        for (int i = 0; i < values.length; i++) {
            String[] attributes = StringUtils.split(values[i], ":");
            AttributePreferenceModifier.modifyAttributePreference(preferenceCollection, Position.class.getName(), attributes[0], attributes[1], null, true);
        }
    }

    public void setExtendedAttributes(Collection<DynamicAttribute> extendedAttributes) {        
        positionSelectionAttributes = new LinkedHashSet<ValueAttributeView>();
        for (DynamicAttribute dynamicAttribute :extendedAttributes) {
            if (dynamicAttribute.isSelectionType()) {
                addSelectionAttribute(dynamicAttribute);
            }
            addNonSelectionAttribute(dynamicAttribute);
        }
    }

    public String getSelectedPositionSelectionAttributeLabel() {
        return selectedPositionSelectionAttributeLabel;
    }

    public void setSelectedSelectionAttribute(String selectedPositionSelectionAttribute) {
        if (!StringUtils.hasText(selectedPositionSelectionAttribute)) return;
        positionAttributeViews = new LinkedHashSet<ValueAttributeView>();
        String[] values = StringUtils.delimitedListToStringArray(selectedPositionSelectionAttribute, ":");
        this.selectedPositionSelectionAttribute = values[1];
        this.selectedPositionSelectionAttributeLabel = values[2];
        // contains the values for the given view
        SwitchAttributePreference positionTitlePreference = getPositionTitlePreference();
        if (DEFAULT_FORMAT_INFO.equals(this.selectedPositionSelectionAttribute)) {
            positionTitlePreference.setAttributeName(TITLE_KEY);
        } else {
            positionTitlePreference.setAttributeName(this.selectedPositionSelectionAttribute);
            positionTitlePreference.setDisplayName(selectedPositionSelectionAttributeLabel);
        }
        // get default formatting info
        createSelectedSelectionViews(positionTitlePreference, values[0]);
    }

    private void createSelectedSelectionViews(SwitchAttributePreference positionTitlePreference, String value) {
        // add an entry for each condition
        ValueAttributeView defaultAttributeView = getDefaultAttributeView();
        positionAttributeViews.add(defaultAttributeView);
        if (DEFAULT_FORMAT_INFO.equals(selectedPositionSelectionAttribute)) return;

        ValueAttributeView attributeView = getTargetAttribute(value);
        Map conditions = positionTitlePreference.getConditions();
        for (Iterator iterator = attributeView.getValues().iterator(); iterator.hasNext();) {
            LookupValue lookupValue = (LookupValue) iterator.next();
            String desc = lookupValue.getLabel();
            String attrName = lookupValue.getValueId();
            ValueAttributeView view;
            ConditionalAttributePreference cond = (ConditionalAttributePreference) conditions.get(attrName);
            if (cond == null) {
                view = new ValueAttributeView(attrName, defaultAttributeView.getFgColor(), defaultAttributeView.getBgColor(), desc, true);
            } else {
                view = new ValueAttributeView(attrName, getFgColorValue(cond), getBgColorValue(cond), desc, cond.isDisplayable());
            }
            positionAttributeViews.add(view);
        }
    }

    private ValueAttributeView getDefaultAttributeView() {
        AttributePreference defaultCondition = getPositionTitlePreference().getDefault();
        return new ValueAttributeView(DEFAULT_VIEW_ID, TITLE_KEY, getFgColorValue(defaultCondition), getBgColorValue(defaultCondition), DEFAULT_FORMAT_INFO, defaultCondition.isDisplayable());
    }

    private ValueAttributeView getTargetAttribute(final String valueId) {
        return (ValueAttributeView) CollectionUtils.find(positionSelectionAttributes, new Predicate() {
            public boolean evaluate(Object object) {
                return ((ValueAttributeView) object).getKey().equals(new Long(valueId));
            }
        });
    }

    /**
     * Adds a preference attribute to the list of displayable attributes in the PreferenceCollection.
     *
     * @param dynamicAttribute
     */
    private void addNonSelectionAttribute(DynamicAttribute dynamicAttribute) {
        String label = dynamicAttribute.getLabel();
        String attributeName = dynamicAttribute.getExternalRefLabel();
        AttributePreference pref = AttributePreferenceAccessor.get(preferenceCollection, Position.class, attributeName);
        if (pref == null) {
            AttributePreferenceModifier.modifyAttributePreference(preferenceCollection, Position.class.getName(), attributeName, label, null, false);
        }
    }

    /**
     * Adds a selection attribute as a view to select to the views.
     *
     * @param dynamicAttribute
     */
    private void addSelectionAttribute(DynamicAttribute dynamicAttribute) {
        AttributePreference pref = getPositionTitlePreference();
        String label = dynamicAttribute.getLabel();
        String attributeName = dynamicAttribute.getExternalRefLabel();
        boolean displayed = false;
        if (pref.getAttributeName().equals(attributeName)) {
            selectedPositionSelectionAttribute = attributeName;
            displayed = true;
        }
        ArrayList<LookupValue> attributeValues = new ArrayList<LookupValue>(dynamicAttribute.getActiveLookupValues());
        ValueAttributeView view = new ValueAttributeView(attributeName, label, displayed, dynamicAttribute.getId(), attributeValues);
        positionSelectionAttributes.add(view);
    }

    private Collection<ValueAttributeView> positionAttributeViews;
    private Collection<ValueAttributeView> positionSelectionAttributes;

    private String selectedPositionSelectionAttribute;
    protected static final Long DEFAULT_VIEW_ID = new Long(-22);
    private String selectedPositionSelectionAttributeLabel;
}
