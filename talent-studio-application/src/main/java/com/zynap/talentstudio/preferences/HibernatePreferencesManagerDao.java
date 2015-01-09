package com.zynap.talentstudio.preferences;

import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.arenas.MenuItem;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * User: amark
 * Date: 21-Dec-2005
 * Time: 12:11:02
 */
public class HibernatePreferencesManagerDao extends HibernateDaoSupport implements IPreferencesManagerDao {

    public Collection getPreferences(Long userId) throws TalentStudioException {
        return getHibernateTemplate().find("from com.zynap.talentstudio.preferences.Preference pref where pref.type = ? and pref.userId =? order by upper(pref.viewName)", new Object[]{PreferenceConstants.USER_PREFERENCE_TYPE, userId});
    }

    public Collection getPreferences(String preferenceType) throws TalentStudioException {
        return getHibernateTemplate().find("from com.zynap.talentstudio.preferences.Preference pref where pref.type =? order by upper(pref.viewName)", preferenceType);
    }

    public Preference getPreference(Long preferenceId) throws TalentStudioException {
        try {
            return (Preference) getHibernateTemplate().load(Preference.class, preferenceId);
        } catch (DataAccessException e) {
            throw new DomainObjectNotFoundException(Preference.class, preferenceId);
        }
    }

    public Preference getPreference(Long userId, String viewName) throws TalentStudioException {
        final List list = getHibernateTemplate().find("from com.zynap.talentstudio.preferences.Preference pref where pref.userId =? and pref.viewName =?", new Object[]{userId, viewName});

        if (!list.isEmpty()) {
            return (Preference) list.get(0);
        }

        throw new DomainObjectNotFoundException(Preference.class, "viewName", viewName);

    }

    public Preference getPreference(String viewName) throws TalentStudioException {
        final List list = getHibernateTemplate().find("from com.zynap.talentstudio.preferences.Preference pref where pref.viewName =?", viewName);

        if (!list.isEmpty()) {
            return (Preference) list.get(0);
        }

        throw new DomainObjectNotFoundException(Preference.class, "viewName", viewName);
    }

    public void createPreference(Preference preference) throws TalentStudioException {
        getHibernateTemplate().save(preference);
    }

    public void updatePreference(Preference preference) throws TalentStudioException {
        getHibernateTemplate().update(preference);
    }

    public void deletePreference(Preference preference) throws TalentStudioException {

        // afm 2005 - DO NOT REMOVE - required to make sure that menu items get reloaded properly by the ArenaMenuHandler 
        final Set menuItems = preference.getMenuItems();
        for (Iterator iterator = menuItems.iterator(); iterator.hasNext();) {
            MenuItem menuItem = (MenuItem) iterator.next();
            menuItem.getMenuSection().getMenuItems().remove(menuItem);
        }

        getHibernateTemplate().delete(preference);
    }

    public String checkDefaultPreferences(String viewName) throws TalentStudioException {
        final List list = getHibernateTemplate().find("from com.zynap.talentstudio.preferences.Preference pref where pref.type =? and pref.viewName =?", new Object[]{PreferenceConstants.INSTALLATION_PREFERENCE_TYPE, viewName});

        Preference preference = null;

        if (!list.isEmpty()) {
            preference = (Preference) list.get(0);
        }

        return preference != null ? preference.getHashcode() : null;
    }
}
