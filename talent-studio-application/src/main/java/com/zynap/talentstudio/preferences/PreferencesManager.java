/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.preferences;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.SecurityConstants;
import com.zynap.talentstudio.preferences.domain.DomainObjectPreferenceCollection;
import com.zynap.talentstudio.security.permits.IPermitManagerDao;
import com.zynap.talentstudio.security.permits.PermitHelper;

import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class PreferencesManager implements IPreferencesManager {

    public void setPreferencesManagerDao(IPreferencesManagerDao preferencesManagerDao) {
        this.preferencesManagerDao = preferencesManagerDao;
    }

    public Collection getSystemPreferences() throws TalentStudioException {
        return preferencesManagerDao.getPreferences(PreferenceConstants.SYSTEM_PREFERENCE_TYPE);
    }

    public Collection getPreferences(Long userId) throws TalentStudioException {
        return preferencesManagerDao.getPreferences(userId);
    }

    public void setPermitManagerDao(IPermitManagerDao permitManagerDao) {
        this.permitManagerDao = permitManagerDao;
    }

    public Preference getPreference(Long userId, String viewName) throws TalentStudioException {
        return preferencesManagerDao.getPreference(userId, viewName);
    }

    public Preference getPreference(Long preferenceId) throws TalentStudioException {
        return preferencesManagerDao.getPreference(preferenceId);
    }

    public void createPreference(Preference preference) throws TalentStudioException {

        checkMenuItemPermits(preference);
        preferencesManagerDao.createPreference(preference);
    }

    public void updatePreference(Preference preference) throws TalentStudioException {

        checkMenuItemPermits(preference);
        preferencesManagerDao.updatePreference(preference);
    }

    public void deletePreference(Preference preference) throws TalentStudioException {
        preferencesManagerDao.deletePreference(preference);
    }

    public void resetPreference(Long preferenceId) throws TalentStudioException {
        final Preference preference = getPreference(preferenceId);
        final Preference defaultPreference = preferencesManagerDao.getPreference(PreferenceConstants.MASTER_REPORTING_CHART_VIEW);
        preference.setPreferenceCollection(defaultPreference.getPreferenceCollection());

        updatePreference(preference);
    }

    public Preference getPreference(String viewName) throws TalentStudioException {
        return preferencesManagerDao.getPreference(viewName);
    }

    public void updateDefaultPreference(DomainObjectPreferenceCollection preferenceCollection) throws TalentStudioException {
        final Preference defaultPreference = getPreference(PreferenceConstants.MASTER_REPORTING_CHART_VIEW);
        defaultPreference.setPreferenceCollection(preferenceCollection);

        updatePreference(defaultPreference);
    }

    public String checkDefaultPreferences(String viewName) throws TalentStudioException {
        return preferencesManagerDao.checkDefaultPreferences(viewName);
    }

    private void checkMenuItemPermits(Preference preference) throws TalentStudioException {
        PermitHelper.checkMenuItemPermits(preference.getMenuItems(), permitManagerDao, SecurityConstants.BROWSE_ACTION, SecurityConstants.REPORTING_STRUCTURE_CONTENT);
    }

    private IPreferencesManagerDao preferencesManagerDao;
    private IPermitManagerDao permitManagerDao;
}
