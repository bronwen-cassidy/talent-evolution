/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.preferences;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.preferences.domain.DomainObjectPreferenceCollection;

import org.springframework.beans.factory.InitializingBean;

/**
 * Component that loads default reporting chart preferences into the database.
 * <br/> Will overwrite existing default preferences if required.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class PreferencesFactory implements InitializingBean {

    public void afterPropertiesSet() throws Exception {

        String hasDefaultSettings = checkDefaultPreferences(PreferenceConstants.MASTER_REPORTING_CHART_VIEW);
        if (hasDefaultSettings == null) {
            addDefaultPreferences(PreferenceConstants.INSTALLATION_PREFERENCE_TYPE, PreferenceConstants.MASTER_REPORTING_CHART_VIEW);
            addDefaultPreferences(PreferenceConstants.SYSTEM_PREFERENCE_TYPE, "Default Reporting Chart");
        } else {
            int hashcode = Integer.parseInt(hasDefaultSettings);
            if (hashcode != defaultPreference.hashCode()) {
                updateDefaultPreferences();
            }
        }
    }

    private void updateDefaultPreferences() throws TalentStudioException {
        preferencesManager.updateDefaultPreference(defaultPreference);
    }

    private void addDefaultPreferences(String type, String viewName) throws TalentStudioException {
        Preference preference = new Preference(viewName, type, defaultPreference, PreferenceConstants.SYSTEM_USER_ID);
        preferencesManager.createPreference(preference);
    }

    private String checkDefaultPreferences(String viewName) throws TalentStudioException {
        return preferencesManager.checkDefaultPreferences(viewName);
    }

    public void setPreferencesManager(IPreferencesManager preferencesManager) {
        this.preferencesManager = preferencesManager;
    }

    public void setDefaultPreference(DomainObjectPreferenceCollection defaultPreference) {
        this.defaultPreference = defaultPreference;
    }

    private DomainObjectPreferenceCollection defaultPreference;

    private IPreferencesManager preferencesManager;
}

