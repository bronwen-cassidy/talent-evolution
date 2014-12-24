/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.reportingchart.settings;

import com.zynap.talentstudio.arenas.MenuItem;
import com.zynap.talentstudio.arenas.MenuSection;
import com.zynap.talentstudio.common.lookups.LookupType;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.common.SelectionNode;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.preferences.Preference;
import com.zynap.talentstudio.preferences.format.FormattingAttribute;
import com.zynap.talentstudio.preferences.format.FormattingInfo;
import com.zynap.talentstudio.preferences.properties.AttributePreference;
import com.zynap.talentstudio.preferences.properties.ConditionalAttributePreference;
import com.zynap.talentstudio.preferences.properties.SwitchAttributePreference;
import com.zynap.talentstudio.web.arena.IMenuItemContainer;
import com.zynap.talentstudio.web.arena.MenuItemHelper;
import com.zynap.talentstudio.web.arena.MenuItemWrapper;
import com.zynap.talentstudio.web.preferences.AttributePreferenceAccessor;
import com.zynap.talentstudio.web.preferences.AttributePreferenceModifier;
import com.zynap.talentstudio.web.utils.SelectionNodeHelper;
import com.zynap.talentstudio.analysis.reports.Report;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
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
public class SettingsWrapperFormBean extends ViewSettingsWrapperBean implements IMenuItemContainer {

    public SettingsWrapperFormBean(Preference preference, Collection<MenuSection> menuSections, Collection<MenuSection> homePageMenuSections, String url) {

        super(preference, preference.getType());

        this.url = url;
        this.menuItemWrappers = MenuItemHelper.buildMenuItemWrappers(preference.getMenuItems(), menuSections, homePageMenuSections);

        positionSettingsWrapperBean = new PositionSettingsWrapperBean(preference);
    }

    public void setOuColor(String label) {
        String[] colors = StringUtils.delimitedListToStringArray(label, ":");
        String[] keys = {BG_COLOR_FORMAT_ATTR_KEY, FG_COLOR_FORMAT_ATTR_KEY};
        AttributePreferenceModifier.modifyAttributePreference(preferenceCollection, OrganisationUnit.class.getName(), LABEL_KEY, keys, colors);
    }

    public void setViewName(String viewName) {
        preference.setViewName(viewName);
    }

    public void setDescription(String description) {
        preference.setDescription(description);
    }

    public void setOuShow(boolean value) {
        AttributePreferenceModifier.modifyAttributePreference(preferenceCollection, OrganisationUnit.class.getName(), LABEL_KEY, LABEL_KEY, value);
    }

    public void setPositionPrimaryAssociationColor(String value) {
        positionSettingsWrapperBean.setPrimaryAssociationColor(value);
    }

    public void setPositionSecondaryAssociationColor(String value) {
        positionSettingsWrapperBean.setSecondaryAssociationColor(value);
    }

    public void setPositionSecondaryAssociationLine(String value) {
        positionSettingsWrapperBean.setSecondAssociationLineWidth(value);
    }

    public void setPositionPrimaryAssociationLine(String value) {
        positionSettingsWrapperBean.setPrimaryAssociationLineWidth(value);
    }

    public void setPositionPrimaryAssociationShow(boolean value) {
        positionSettingsWrapperBean.setPrimaryAssociationShowable(value);
    }

    public void setPositionSecondaryAssociationShow(boolean value) {
        positionSettingsWrapperBean.setSecondaryAssociationShowable(value);
    }

    public void setSelectedPositionSelectionAttribute(String selectedPositionSelectionAttribute) {
        positionSettingsWrapperBean.setSelectedSelectionAttribute(selectedPositionSelectionAttribute);
    }

    public Collection<ValueAttributeView> getPositionAttributeViews() {
        return positionSettingsWrapperBean.getAttributeViews();
    }

    public void setPrimarySubjectAssociationLookupType(LookupType primaryAssociation) {
        primarySubjectAssociationLookupType = primaryAssociation;
    }

    public void setSecondarySubjectAssociationLookupType(LookupType secondaryAssociation) {
        secondarySubjectAssociationLookupType = secondaryAssociation;
    }

    public void setPositionExtendedAttributes(Collection<DynamicAttribute> extendedAttributes) {
        positionSettingsWrapperBean.setExtendedAttributes(extendedAttributes);
    }

    public Collection getPositionSelectionAttributes() {
        return positionSettingsWrapperBean.getSelectionAttributes();
    }

    public Collection getDisplayPositionAttributes() {
        return new LinkedHashSet<AttributePreference>(preferenceCollection.get(Position.class.getName()).getAttributePrefs().values());
    }

    public void setSelectedPositionAttributes(String[] values) {
        positionSettingsWrapperBean.assignSelectedAttributes(values);
    }

    public void setSubjectExtendedAttributes(Collection<DynamicAttribute> subjectAttributes) {
        removeNonExistentExtendedAttributes(subjectAttributes, Subject.class.getName());
        for (Iterator iterator = subjectAttributes.iterator(); iterator.hasNext();) {
            DynamicAttribute dynamicAttribute = (DynamicAttribute) iterator.next();
            String label = dynamicAttribute.getLabel();
            String attributeName = dynamicAttribute.getExternalRefLabel();
            AttributePreference pref = AttributePreferenceAccessor.get(preferenceCollection, Subject.class, attributeName);
            if (pref == null) {
                AttributePreferenceModifier.modifyAttributePreference(preferenceCollection, Subject.class.getName(), attributeName, label, null, false);
            }
        }
    }

    public void setSelectedSubjectAttributes(String[] attributes) {
        disableAllPreferences(getSubjectPref().getAttributePrefs());
        for (int i = 0; i < attributes.length; i++) {
            String[] values = StringUtils.split(attributes[i], ":");
            AttributePreferenceModifier.modifyAttributePreference(preferenceCollection, Subject.class.getName(), values[0], values[1], null, true);
        }
    }

    /**
     * Subject primary associations have been selected, deselected, all the conditions are set to value and the preferences is set to value.
     *
     * @param value true if the subjects primary association is to be shown, false otherwise
     */
    public void setSubjectPrimaryAssociationsShow(boolean value) {
        SwitchAttributePreference subjectPrimaryAssociationPreference = getSubjectPrimaryAssociationAttributePreference();
        subjectPrimaryAssociationPreference.setDisplayable(value);
        if (!value) disableAllPreferences(subjectPrimaryAssociationPreference.getConditions());
    }

    public Collection getSubjectPrimaryValues() {
        return getAssociationTypes(primarySubjectAssociationLookupType, getSubjectPrimaryAssociationAttributePreference());
    }

    public Collection<ValueAttributeView> getSubjectPrimaryAttributeViews() {
        return subjectPrimaryAssociationViews;
    }

    public String[] getSelectedSubjectPrimaryValues() {
        return new String[0];
    }

    public void setSelectedSubjectPrimaryValues(String[] values) {
        subjectPrimaryAssociationViews = setSelectedAssociationValues(values, getSubjectPrimaryAssociationAttributePreference());
    }

    public void setSubjectSecondaryAssociationsShow(boolean value) {
        SwitchAttributePreference subjectSecondaryAssociationPreference = getSubjectSecondaryAssociationPreference();
        subjectSecondaryAssociationPreference.setDisplayable(value);
        if (!value) disableAllPreferences(subjectSecondaryAssociationPreference.getConditions());
    }

    public Collection getSubjectSecondaryValues() {
        return getAssociationTypes(secondarySubjectAssociationLookupType, getSubjectSecondaryAssociationPreference());
    }

    public Collection getSubjectSecondaryAttributeViews() {
        return subjectSecondaryAssociationViews;
    }

    public String[] getSelectedSubjectSecondaryValues() {
        return new String[0];
    }

    /**
     * Sets the subjects selected secondary association attribute preferences, such as colours.
     *
     * @param values a string array of values according to the labels of the secondary associations
     */
    public void setSelectedSubjectSecondaryValues(String[] values) {
        subjectSecondaryAssociationViews = setSelectedAssociationValues(values, getSubjectSecondaryAssociationPreference());
    }

    public String getSelectedPositionSelectionAttribute() {
        return positionSettingsWrapperBean.getSelectedSelectionAttributeName();
    }

    public String getSelectedPositionSelectionAttributeLabel() {
        return positionSettingsWrapperBean.getSelectedPositionSelectionAttributeLabel();
    }

    public Collection getDisplaySubjectAttributes() {
        return getAttributes(getSubjectPref().getAttributePrefs());
    }

    public Preference getModifiedPreference() {
        SwitchAttributePreference positionTitlePreference = getPositionTitlePreference();
        positionTitlePreference.getConditions().clear();
        updatePreferences(getPositionAttributeViews(), positionTitlePreference);

        updatePreferences(subjectPrimaryAssociationViews, getSubjectPrimaryAssociationAttributePreference());
        updatePreferences(subjectSecondaryAssociationViews, getSubjectSecondaryAssociationPreference());

        preference.setPreferenceCollection(preferenceCollection);

        // assign menu items
        preference.assignNewMenuItems(MenuItemHelper.getAssignedMenuItems(menuItemWrappers, preference.getViewName(), url));
        assignNewGroups(preference);

        return preference;
    }

    private void assignNewGroups(Preference preference) {
        Set<Group> newGroups = new LinkedHashSet<Group>();
        for (SelectionNode selectionNode : groups) {
            if (selectionNode.isSelected()) {
                newGroups.add((Group) selectionNode.getValue());
            }
        }
        preference.assignNewGroups(newGroups);
    }

    public void setGroupIds(Long[] groupIds) {
        SelectionNodeHelper.enableDomainObjectSelections(groups, groupIds);
    }

    public Long[] getGroupIds() {
        return new Long[0];
    }

    public void setGroups(Collection<SelectionNode> groups) {
        this.groups = groups;
    }

    public Collection<SelectionNode> getGroups() {
        return groups;
    }

    public int getGroupsSize() {
        return groups.size();
    }

    public boolean hasAssignedGroups() {
        return SelectionNodeHelper.hasSelectedItems(groups);
    }

    private Collection<ValueAttributeView> setSelectedAssociationValues(String[] values, SwitchAttributePreference associationAttributePreference) {

        Collection<ValueAttributeView> associationViews = new LinkedHashSet<ValueAttributeView>();

        Map<String, ConditionalAttributePreference> conditions = associationAttributePreference.getConditions();
        disableAllPreferences(conditions);

        for (int i = 0; i < values.length; i++) {
            String[] results = StringUtils.split(values[i], ":");
            String attributeName = results[0];
            String displayValue = results[1];

            ConditionalAttributePreference ca = conditions.get(attributeName);
            if (ca == null) {
                ca = new ConditionalAttributePreference(attributeName, true);
                ca.setDisplayName(displayValue);
                ca.setAttributeName(attributeName);


                ca.setFormattingInfo(createFormattingInfo(conditions.get(DEFAULT_FORMAT_INFO)));
                conditions.put(attributeName, ca);
            } else {
                ca.setDisplayable(true);
            }
            ValueAttributeView view = new ValueAttributeView(attributeName, getFgColorValue(ca), getBgColorValue(ca), displayValue, ca.isDisplayable());
            associationViews.add(view);
        }

        return associationViews;
    }

    private Collection getAssociationTypes(final LookupType lookupType, SwitchAttributePreference associationPreference) {

        Map<String, ConditionalAttributePreference> conditions = associationPreference.getConditions();
        if (conditions == null) conditions = new LinkedHashMap<String, ConditionalAttributePreference>();
        Collection<ValueAttributeView> values = new ArrayList<ValueAttributeView>();

        for (Iterator iterator = lookupType.getActiveLookupValues().iterator(); iterator.hasNext();) {
            LookupValue lookupValue = (LookupValue) iterator.next();
            final String label = lookupValue.getLabel();
            final String valueId = lookupValue.getValueId();
            ValueAttributeView view = new ValueAttributeView(valueId, label);
            checkConditions(conditions, view);
            values.add(view);

        }

        return values;
    }

    private void checkConditions(Map<String, ConditionalAttributePreference> conditions, ValueAttributeView attributeView) {
        final String label = attributeView.getDisplayValue();
        final String expectedValue = attributeView.getExpectedValue();
        ConditionalAttributePreference conditionPreference = conditions.get(expectedValue);
        if (conditionPreference == null) {
            ConditionalAttributePreference defaultPref = conditions.get(DEFAULT_FORMAT_INFO);
            conditionPreference = new ConditionalAttributePreference(expectedValue, true);
            conditionPreference.setAttributeName(attributeView.getAttributeName());
            conditionPreference.setDisplayName(label);
            conditionPreference.setFormattingInfo(createFormattingInfo(defaultPref));
            conditions.put(expectedValue, conditionPreference);
        }
        attributeView.setDisplayable(conditionPreference.isDisplayable());
    }

    private FormattingInfo createFormattingInfo(ConditionalAttributePreference defaultPref) {
        final Map<String, FormattingAttribute> current = defaultPref.getFormattingInfo().getFormattingAttributes();

        Map<String, FormattingAttribute> attributes = new LinkedHashMap<String, FormattingAttribute>();

        for (Map.Entry<String, FormattingAttribute> attributeEntry : current.entrySet()) {
            final FormattingAttribute currentAttribute = attributeEntry.getValue();
            attributes.put(attributeEntry.getKey(), new FormattingAttribute(currentAttribute.getName(), currentAttribute.getValue()));
        }

        return new FormattingInfo(attributes);
    }

    private List getAttributes(Map selectedAttributes) {
        List<ValueAttributeView> result = new ArrayList<ValueAttributeView>();
        for (Iterator iterator = selectedAttributes.values().iterator(); iterator.hasNext();) {
            AttributePreference pref = (AttributePreference) iterator.next();
            if (isAttributePreference(pref)) {
                result.add(new ValueAttributeView(pref.getAttributeName(), pref.getDisplayName(), pref.isDisplayable()));
            }
        }
        return result;
    }

    private void updatePreferences(Collection<ValueAttributeView> views, SwitchAttributePreference switchPreference) {
        for (Iterator iterator = views.iterator(); iterator.hasNext();) {
            ValueAttributeView view = (ValueAttributeView) iterator.next();
            switchPreference.addOrUpdateConditionalPropertyPref(view.getExpectedValue(), view.getDisplayValue(), view.isDisplayable());
            switchPreference.addOrUpdateFormattingInfo(view.getExpectedValue(), FG_COLOR_FORMAT_ATTR_KEY, view.getFgColor());
            switchPreference.addOrUpdateFormattingInfo(view.getExpectedValue(), BG_COLOR_FORMAT_ATTR_KEY, view.getBgColor());
        }
    }


    /**
     * Method for spring binding.
     *
     * @param positions
     */
    public void setActiveMenuItems(String[] positions) {
        MenuItemHelper.setSelected(menuItemWrappers, positions);
    }

    /**
     * Method for spring binding.
     *
     * @return String[]
     */
    public String[] getActiveMenuItems() {
        return new String[0];
    }

    /**
     * Method for spring binding.
     *
     * @param positions
     */
    public void setHomePageMenuItems(String[] positions) {
        MenuItemHelper.setHomePage(menuItemWrappers, positions);
    }

    /**
     * Method for spring binding.
     *
     * @return String[]
     */
    public String[] getHomePageMenuItems() {
        return new String[0];
    }

    /**
     * Get collection of menu item wrappers available for selection.
     *
     * @return Collection of {@link com.zynap.talentstudio.web.arena.MenuItemWrapper} objects
     */
    public List getMenuItemWrappers() {
        return menuItemWrappers;
    }

    /**
     * Check if has assigned menu items.
     *
     * @return true or false
     */
    public boolean hasAssignedMenuItems() {
        return MenuItemHelper.hasSelectedItems(menuItemWrappers);
    }

    /**
     * Get assigned menu items.
     *
     * @return Set
     */
    public Set getAssignedMenuItems() {
        return MenuItemHelper.getAssignedMenuItems(menuItemWrappers, getViewName(), url);
    }

    public String getUrl() {
        return url;
    }

    private Collection<ValueAttributeView> subjectPrimaryAssociationViews = new LinkedHashSet<ValueAttributeView>();
    private Collection<ValueAttributeView> subjectSecondaryAssociationViews = new LinkedHashSet<ValueAttributeView>();

    private LookupType primarySubjectAssociationLookupType;
    private LookupType secondarySubjectAssociationLookupType;

    private PositionSettingsWrapperBean positionSettingsWrapperBean;


    /**
     * The list of MenuItemWrappers.
     */
    private List<MenuItemWrapper> menuItemWrappers = new ArrayList<MenuItemWrapper>();
    private Collection<SelectionNode> groups = new ArrayList<SelectionNode>();

    /**
     * The url for the menu items.
     */
    private String url;
}