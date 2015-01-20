/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.reportingchart.settings;

import com.zynap.talentstudio.arenas.MenuItem;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.organisation.subjects.SubjectAssociation;
import com.zynap.talentstudio.preferences.Preference;
import com.zynap.talentstudio.preferences.domain.DomainObjectPreference;
import com.zynap.talentstudio.preferences.domain.DomainObjectPreferenceCollection;
import com.zynap.talentstudio.preferences.properties.AttributePreference;
import com.zynap.talentstudio.preferences.properties.ConditionalAttributePreference;
import com.zynap.talentstudio.preferences.properties.NestedAttributePreference;
import com.zynap.talentstudio.preferences.properties.SwitchAttributePreference;
import com.zynap.talentstudio.web.arena.MenuItemHelper;
import com.zynap.talentstudio.web.preferences.AttributePreferenceAccessor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ViewSettingsWrapperBean implements Serializable {

    public ViewSettingsWrapperBean(Preference preference) {
        this.preference = preference;
        this.preferenceCollection = preference.getPreferenceCollection();
    }

    public ViewSettingsWrapperBean(Preference preference, String preferenceType) {
        this(preference);
        this.preferenceType = preferenceType;
    }

    public Long getId() {
        return preference.getId();
    }

    public String getViewName() {
        return preference.getViewName();
    }

    public String getDescription() {
        return preference.getDescription();
    }

    public String getType() {
        return preference.getType();
    }

    public String getOuColor() {
        AttributePreference orgUnitLabelPreference = getOrgUnitLabelPreference();
        return getBgColorValue(orgUnitLabelPreference) + ":" + getFgColorValue(orgUnitLabelPreference);
    }

    public String getOuFgColor() {
        return getFgColorValue(getOrgUnitLabelPreference());
    }

    public String getOuBgColor() {
        return getBgColorValue(getOrgUnitLabelPreference());
    }

    public boolean isOuShow() {
        return getOrgUnitLabelPreference().isDisplayable();
    }

    public String getPositionPrimaryAssociationColor() {
        return getBgColorValue(getPositionPrimaryAssociationPreference());
    }

    public String getPositionSecondaryAssociationColor() {
        return getBgColorValue(getPositionSecondaryAssociationPreference());
    }

    public String getPositionSecondaryAssociationLine() {
        return getPositionSecondaryAssociationPreference().getFormattingInfo().get(LINE_HEIGHT).getValue();
    }

    public String getPositionPrimaryAssociationLine() {
        return getPositionPrimaryAssociationPreference().getFormattingInfo().get(LINE_HEIGHT).getValue();
    }

    public boolean isPositionPrimaryAssociationShow() {
        return getPositionPrimaryAssociationPreference().isDisplayable();
    }

    public boolean isPositionSecondaryAssociationShow() {
        return getPositionSecondaryAssociationPreference().isDisplayable();
    }

    public String getSelectedPositionSelectionAttribute() {
        return getPositionTitlePreference().getAttributeName();
    }

    public String getSelectedPositionSelectionAttributeLabel() {
        return getPositionTitlePreference().getDisplayName();    
    }

    public List getPositionSelectedValues() {
        List<ValueAttributeView> result = new ArrayList<ValueAttributeView>();
        SwitchAttributePreference positionTitlePreference = getPositionTitlePreference();
        Map conditions = positionTitlePreference.getConditions();
        for (Iterator iterator = conditions.values().iterator(); iterator.hasNext();) {
            ConditionalAttributePreference pref = (ConditionalAttributePreference) iterator.next();
            result.add(new ValueAttributeView(pref.getExpectedValue(), getFgColorValue(pref), getBgColorValue(pref), pref.getDisplayName(), pref.isDisplayable()));
        }
        return result;
    }

    public List<ValueAttributeView> getCurrentDisplayPositionAttributes() {
        return new ArrayList<ValueAttributeView>(getAttributes(getPositionPref().getAttributePrefs()));
    }

    public boolean getSubjectPrimaryAssociationsShow() {
        return getSubjectPrimaryAssociationAttributePreference().isDisplayable();
    }

    public boolean getSubjectSecondaryAssociationsShow() {
        return getSubjectSecondaryAssociationPreference().isDisplayable();
    }

    public List getSubjectPrimaryAttributes() {
        return createAssociationViews(getSubjectPrimaryAssociationAttributePreference());
    }

    public List getSubjectSecondaryAttributes() {
        return createAssociationViews(getSubjectSecondaryAssociationPreference());
    }

    public Collection getCurrentDisplaySubjectAttributes() {
        return getAttributes(getSubjectPref().getAttributePrefs());
    }

    public String getPreferenceType() {
        return preferenceType;
    }

    public List<MenuItem> getAssignedArenas() {
        return MenuItemHelper.getAssignedArenas(preference.getMenuItems());
    }

    public Set<Group> getAssignedGroups() {
        return preference.getGroups();
    }

    protected String getBgColorValue(AttributePreference preference) {
        return preference.getFormattingInfo().get(BG_COLOR_FORMAT_ATTR_KEY).getValue();
    }

    protected String getFgColorValue(AttributePreference preference) {
        return preference.getFormattingInfo().get(FG_COLOR_FORMAT_ATTR_KEY).getValue();
    }

    protected AttributePreference getPositionPrimaryAssociationPreference() {
        return getPositionPref().get(POSITION_PRIMARY_ASSOCIATION_KEY);
    }

    protected AttributePreference getOrgUnitLabelPreference() {
        return getOrganisationUnitPref().get(LABEL_KEY);
    }

    protected AttributePreference getPositionSecondaryAssociationPreference() {
        return getPositionPref().get(SECONDARY_ASSOCIATION_KEY);
    }

    protected SwitchAttributePreference getPositionTitlePreference() {
        NestedAttributePreference nestedAttributePreference = (NestedAttributePreference) getPositionPref().get(TITLE_KEY);
        return (SwitchAttributePreference) nestedAttributePreference.getNestedAttributePreference();
    }

    protected SwitchAttributePreference getSubjectSecondaryAssociationPreference() {
        // get preference where primary is false
        return (SwitchAttributePreference) AttributePreferenceAccessor.getPreference(preferenceCollection, SubjectAssociation.class,
                SUBJECT_ASSOCIATION_TYPE_KEY, Boolean.FALSE.toString());
    }

    protected SwitchAttributePreference getSubjectPrimaryAssociationAttributePreference() {

        // get preference where primary is true
        return (SwitchAttributePreference) AttributePreferenceAccessor.getPreference(preferenceCollection, SubjectAssociation.class,
                SUBJECT_ASSOCIATION_TYPE_KEY, Boolean.TRUE.toString());
    }

    protected boolean isAttributePreference(AttributePreference preference) {
        if (POSITION_PRIMARY_ASSOCIATION_KEY.equals(preference.getAttributeName())) return false;
        if (SECONDARY_ASSOCIATION_KEY.equals(preference.getAttributeName())) return false;
        if (LABEL_KEY.equals(preference.getAttributeName())) return false;
        if (TITLE_KEY.equals(preference.getAttributeName())) return false;
        return true;
    }

    protected DomainObjectPreference getPositionPref() {
        return preferenceCollection.get(Position.class.getName());
    }

    protected DomainObjectPreference getSubjectPref() {
        return preferenceCollection.get(Subject.class.getName());
    }

    protected DomainObjectPreference getOrganisationUnitPref() {
        return preferenceCollection.get(OrganisationUnit.class.getName());
    }

    protected void disableAllPreferences(Map attributePrefs) {
        for (Iterator iterator = attributePrefs.values().iterator(); iterator.hasNext();) {
            AttributePreference attributePreference = (AttributePreference) iterator.next();
            if (isAttributePreference(attributePreference)) {
                attributePreference.setDisplayable(false);
            }
        }
    }

    private List<ValueAttributeView> getAttributes(Map selectedAttributes) {
        List<ValueAttributeView> result = new ArrayList<ValueAttributeView>();
        for (Iterator iterator = selectedAttributes.values().iterator(); iterator.hasNext();) {
            AttributePreference currentPreference = (AttributePreference) iterator.next();
            if (isAttributePreference(currentPreference) && currentPreference.isDisplayable()) {
                result.add(new ValueAttributeView(currentPreference.getAttributeName(), currentPreference.getDisplayName(), currentPreference.isDisplayable()));
            }
        }
        return result;
    }

    private List createAssociationViews(SwitchAttributePreference preference) {
        List<ValueAttributeView> result = new ArrayList<ValueAttributeView>();
        Collection conditions = preference.getConditions().values();
        for (Iterator iterator = conditions.iterator(); iterator.hasNext();) {
            ConditionalAttributePreference pref = (ConditionalAttributePreference) iterator.next();
            result.add(new ValueAttributeView(pref.getExpectedValue(), getFgColorValue(pref), getBgColorValue(pref), pref.getDisplayName(), pref.isDisplayable()));
        }
        return result;
    }

    public void removeNonExistentExtendedAttributes(Collection<DynamicAttribute> subjectAttributes, String name) {
        final DomainObjectPreference domainObjectPreference = preferenceCollection.get(name);
        final Map<String, AttributePreference> atttributePreferences = domainObjectPreference.getAttributePrefs();
        List<String> attributesToRemove = new ArrayList<String>();
        for (String key : atttributePreferences.keySet()) {
            boolean found = false;
            for (DynamicAttribute dynamicAttribute : subjectAttributes) {
                final String attributeName = dynamicAttribute.getExternalRefLabel();
                if(key.equals(attributeName)) {
                    found = true;
                }
            }
            if(!found) {
                attributesToRemove.add(key);
            }
        }
        for (String attributeKey : attributesToRemove) {
            atttributePreferences.remove(attributeKey);
        }
    }

    public boolean isSecure() {
        return preference.isSecure();
    }

    public void setSecure(boolean value) {
        preference.setSecure(value);
    }

    protected Preference preference;
    protected DomainObjectPreferenceCollection preferenceCollection;

    private String preferenceType;

    public static final String FG_COLOR_FORMAT_ATTR_KEY = "color";
    public static final String BG_COLOR_FORMAT_ATTR_KEY = "background-color";

    public static final String LABEL_KEY = "label";
    public static final String POSITION_PRIMARY_ASSOCIATION_KEY = "primaryAssociation";
    public static final String SECONDARY_ASSOCIATION_KEY = "secondaryAssociations";
    public static final String TITLE_KEY = "title";
    public static final String SUBJECT_ASSOCIATION_TYPE_KEY = "associationType";

    public static final String DEFAULT_FORMAT_INFO = "default";
    public static final String LINE_HEIGHT = "line-height";
}
