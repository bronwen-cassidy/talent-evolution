/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.preferences;

import com.zynap.exception.TalentStudioException;

import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IPreferencesManagerDao {

    Collection getPreferences(Long userId) throws TalentStudioException;

    Collection getPreferences(String preferenceType) throws TalentStudioException;

    Preference getPreference(Long preferenceId) throws TalentStudioException;

    Preference getPreference(Long userId, String viewName) throws TalentStudioException;

    Preference getPreference(String viewName) throws TalentStudioException;

    void createPreference(Preference preference) throws TalentStudioException;

    void updatePreference(Preference preference) throws TalentStudioException;

    void deletePreference(Preference preference) throws TalentStudioException;

    String checkDefaultPreferences(String viewName) throws TalentStudioException;
}
