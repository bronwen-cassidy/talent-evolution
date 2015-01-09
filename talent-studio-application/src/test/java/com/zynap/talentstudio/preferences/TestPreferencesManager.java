/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.preferences;

import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.arenas.MenuItem;
import com.zynap.talentstudio.arenas.MenuSection;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.preferences.domain.DomainObjectPreference;
import com.zynap.talentstudio.preferences.domain.DomainObjectPreferenceCollection;
import com.zynap.talentstudio.preferences.format.FormattingAttribute;
import com.zynap.talentstudio.preferences.format.FormattingInfo;
import com.zynap.talentstudio.preferences.properties.AttributePreference;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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
public class TestPreferencesManager extends BasePreferenceTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        preferencesManager = (IPreferencesManager) getBean("prefManager");
        reportService = (IReportService) getBean("reportService");
    }

//    public void testGetPreferences() throws Exception {
//        Collection preferences = preferencesManager.getPreferences(new Long(21));
//        assertNotNull(preferences);
//    }

    public void testGetPreferenceByViewName() throws Exception {

        final Long userId = ADMINISTRATOR_USER_ID;
        final String viewName = "pref1";
        final String type = PreferenceConstants.USER_PREFERENCE_TYPE;

        final Preference newPreference = buildPreference(viewName, type, userId);
        preferencesManager.createPreference(newPreference);
        assertNotNull(newPreference.getId());

        final Preference preference = preferencesManager.getPreference(viewName);
        assertEquals(newPreference, preference);
        assertEquals(newPreference.getType(), preference.getType());
    }

    public void testCreatePreference() throws Exception {

        final Long userId = ADMINISTRATOR_USER_ID;
        final String viewName = "testPref";
        final String type = PreferenceConstants.USER_PREFERENCE_TYPE;

        final Preference newPreference = buildPreference(viewName, type, userId);
        final DomainObjectPreference ouPref = new DomainObjectPreference();
        final AttributePreference attributePreference = new AttributePreference("label", true);

        Map attributes = new HashMap();
        attributes.put("fgcolor", new FormattingAttribute("fgcolor", "green"));
        attributes.put("bgcolor", new FormattingAttribute("bgcolor", "brown"));
        attributePreference.setFormattingInfo(new FormattingInfo(attributes));

        ouPref.add(attributePreference);
        newPreference.getPreferenceCollection().add(OrganisationUnit.class.getName(), ouPref);

        preferencesManager.createPreference(newPreference);
        assertNotNull(newPreference.getId());

        final Collection userPreferences = preferencesManager.getPreferences(userId);
        Preference found = (Preference) ((List) userPreferences).get(userPreferences.size() - 1);
        assertEquals(viewName, found.getViewName());
        assertEquals(userId, found.getUserId());
        assertEquals(type, found.getType());
        assertNotNull(found.getId());
    }

    public void testUpdateDisplayPreference() throws Exception {

        final Long userId = ADMINISTRATOR_USER_ID;
        final String viewName = "testPref";
        final String type = PreferenceConstants.USER_PREFERENCE_TYPE;

        // create preference
        final Preference newPreference = buildPreference(viewName, type, userId);
        preferencesManager.createPreference(newPreference);

        // add preference data and update
        Map attributes = new HashMap();
        attributes.put(LINE_HEIGHT_ATTR_NAME, new FormattingAttribute(LINE_HEIGHT_ATTR_NAME, "10px"));
        attributes.put(COLOR_ATTR_NAME, new FormattingAttribute(COLOR_ATTR_NAME, "lavender"));

        AttributePreference attrPref = new AttributePreference(LABEL_PREF_NAME, true);
        attrPref.setFormattingInfo(new FormattingInfo(attributes));

        DomainObjectPreference ouPref = new DomainObjectPreference();
        ouPref.add(attrPref);

        newPreference.getPreferenceCollection().add(OrganisationUnit.class.getName(), ouPref);
        preferencesManager.updatePreference(newPreference);

        // check preference and attribute have both been updated
        Preference actual = preferencesManager.getPreference(userId, newPreference.getViewName());
        assertEquals(newPreference, actual);

        final DomainObjectPreference actualObjectPref = actual.getPreferenceCollection().get(OrganisationUnit.class.getName());
        assertEquals(actualObjectPref, ouPref);

        final AttributePreference attributePreference = actualObjectPref.get(LABEL_PREF_NAME);
        assertEquals(attrPref, attributePreference);
    }

    public void testUpdatePreference() throws Exception {

        final Long userId = ADMINISTRATOR_USER_ID;
        final String viewName = "testPref";
        final String type = PreferenceConstants.USER_PREFERENCE_TYPE;

        final Preference newPreference = buildPreference(viewName, type, userId);
        preferencesManager.createPreference(newPreference);

        // find it and update it
        final Preference found = preferencesManager.getPreference(userId, viewName);
        final String newViewName = "fred";
        found.setViewName(newViewName);
        preferencesManager.updatePreference(found);

        final Preference updated = preferencesManager.getPreference(found.getId());
        assertEquals(found, updated);
    }

    public void testGetPreferencesNotNull() throws Exception {
        final Collection userPreferences = preferencesManager.getPreferences(new Long(-10));
        assertNotNull(userPreferences);
    }

    /**
     * Check that install preferences are never returned.
     *
     * @throws Exception
     */
    public void testGetUserPreferences() throws Exception {

        final Long userId = ADMINISTRATOR_USER_ID;
        final String viewName = "testPref";
        final String type = PreferenceConstants.USER_PREFERENCE_TYPE;

        final Preference newPreference = buildPreference(viewName, type, userId);
        preferencesManager.createPreference(newPreference);

        final Collection userPreferences = preferencesManager.getPreferences(userId);
        for (Iterator iterator = userPreferences.iterator(); iterator.hasNext();) {
            Preference preference = (Preference) iterator.next();
            assertEquals(PreferenceConstants.USER_PREFERENCE_TYPE, preference.getType());
        }
    }

    public void testGetPreferenceById() throws Exception {

        final Long userId = ADMINISTRATOR_USER_ID;
        final String viewName = "testPref";
        final String type = PreferenceConstants.USER_PREFERENCE_TYPE;

        final Preference original = buildPreference(viewName, type, userId);
        preferencesManager.createPreference(original);

        // get new id and lookup the object
        final Long newId = preferencesManager.getPreference(userId, viewName).getId();
        final Preference found = preferencesManager.getPreference(newId);
        assertEquals(newId, found.getId());
        assertEquals(original, found);
    }

    public void testGetPreferenceInvalidId() throws Exception {
        try {
            preferencesManager.getPreference(new Long(-99));
            fail("Must fail as invalid id was supplied");
        } catch (DomainObjectNotFoundException expected) {
        }
    }

    public void testGetPreferenceByUserIdAndViewName() throws Exception {

        final Long userId = ADMINISTRATOR_USER_ID;
        final String viewName = "testPref";
        final String type = PreferenceConstants.USER_PREFERENCE_TYPE;

        final Preference newPreference = buildPreference(viewName, type, userId);
        preferencesManager.createPreference(newPreference);

        final Preference found = preferencesManager.getPreference(userId, viewName);
        assertNotNull(found.getId());
        assertEquals(newPreference, found);
    }

    public void testPublishPreference() throws Exception {

        final Long userId = ADMINISTRATOR_USER_ID;
        final String viewName = "testPref";
        final String type = PreferenceConstants.USER_PREFERENCE_TYPE;

        final Preference newPreference = buildPreference(viewName, type, userId);

        final List menuSections = (List) reportService.getMenuSections();
        final MenuSection menuSection = (MenuSection) menuSections.get(0);

        // add 1 menu item
        Set menuItems = new LinkedHashSet();
        final MenuItem firstMenuItem = new MenuItem("test item", menuSection, "test.htm");
        menuItems.add(firstMenuItem);
        newPreference.assignNewMenuItems(menuItems);
        preferencesManager.createPreference(newPreference);

        // check that it is present
        Collection reportingChartMenuItems = newPreference.getMenuItems();
        assertEquals(1, reportingChartMenuItems.size());
        assertEquals(reportingChartMenuItems.toArray()[0], firstMenuItem);

        // change menu item and update - old one should be removed and new one added
        Set newMenuItems = new LinkedHashSet();
        final MenuItem secondMenuItem = new MenuItem("test item", menuSection, "test.htm");
        newMenuItems.add(secondMenuItem);
        newPreference.assignNewMenuItems(newMenuItems);
        preferencesManager.updatePreference(newPreference);

        reportingChartMenuItems = newPreference.getMenuItems();
        assertEquals(1, reportingChartMenuItems.size());
        assertEquals(reportingChartMenuItems.toArray()[0], secondMenuItem);

        // check that ids have been set
        assertNotNull(firstMenuItem.getId());
        assertNotNull(secondMenuItem.getId());

        // check that the permits have been set
        assertNotNull(firstMenuItem.getPermit());
        assertNotNull(secondMenuItem.getPermit());

        // check that are reporting chart menu items
        assertTrue(firstMenuItem.isReportingChartMenuItem());
        assertTrue(secondMenuItem.isReportingChartMenuItem());
    }

    public void testDeletePreference() throws Exception {

        final Long userId = ADMINISTRATOR_USER_ID;
        final String viewName = "testPref";
        final String type = PreferenceConstants.USER_PREFERENCE_TYPE;

        final Preference newPreference = buildPreference(viewName, type, userId);
        preferencesManager.createPreference(newPreference);

        // find and delete
        final Preference found = preferencesManager.getPreference(userId, viewName);
        assertEquals(newPreference, found);
        preferencesManager.deletePreference(found);

        // check it is gone
        try {
            preferencesManager.getPreference(found.getId());
            fail("deleted preference incorrectly found");
        } catch (DomainObjectNotFoundException expected) {
        }
    }

    public void testGetSystemPreferences() throws Exception {

        // create two preferences - 1 system and 1 user
        final Preference newPreference = buildPreference("testPref1", PreferenceConstants.SYSTEM_PREFERENCE_TYPE, ADMINISTRATOR_USER_ID);
        preferencesManager.createPreference(newPreference);

        final Preference newPreference2 = buildPreference("testPref2", PreferenceConstants.USER_PREFERENCE_TYPE, ADMINISTRATOR_USER_ID);
        preferencesManager.createPreference(newPreference2);

        final Collection systemPreferences = preferencesManager.getSystemPreferences();
        assertNotNull(systemPreferences);
        assertFalse(systemPreferences.isEmpty());

        for (Iterator iterator = systemPreferences.iterator(); iterator.hasNext();) {
            Preference preference = (Preference) iterator.next();
            assertEquals(PreferenceConstants.SYSTEM_PREFERENCE_TYPE, preference.getType());
        }
    }

    private Preference buildPreference(final String viewName, final String type, Long userId) {
        final DomainObjectPreferenceCollection prefCollection = new DomainObjectPreferenceCollection();


        return new Preference(viewName, type, prefCollection, userId);
    }

    private void assertEquals(final Preference expected, final Preference found) {
        assertEquals(found.getType(), expected.getType());
        assertEquals(found.getViewName(), expected.getViewName());
        assertEquals(found.getUserId(), expected.getUserId());
        assertEquals(expected.getPreferenceCollection(), found.getPreferenceCollection());
    }

    private IReportService reportService;
    private IPreferencesManager preferencesManager;
}