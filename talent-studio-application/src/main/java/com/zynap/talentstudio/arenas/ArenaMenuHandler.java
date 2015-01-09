/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.arenas;

import com.zynap.domain.UserPrincipal;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.security.permits.IPermit;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.preferences.Preference;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.InitializingBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ArenaMenuHandler implements InitializingBean, Serializable, IArenaMenuHandler {

    /**
     * Invoked by a BeanFactory after it has set all bean properties supplied
     * (and satisfied BeanFactoryAware and ApplicationContextAware).
     * <p>This method allows the bean instance to perform initialization only
     * possible when all bean properties have been set and to throw an
     * exception in the event of misconfiguration.
     *
     * @throws TalentStudioException in the event of misconfiguration (such
     *                               as failure to set an essential property) or if initialization fails.
     */
    public void afterPropertiesSet() throws TalentStudioException {
        loadArenas();
    }

    public synchronized void reload() throws TalentStudioException {
        loadArenas();
    }

    /**
     * Gets the menus the logged in user is allowed to see.
     * <br/>
     * Menus are viewable according to their given permissions encapsulated as an {@link IPermit}
     *
     * @param userPrincipal
     * @return the collection of menus, items, and sections filtered to what the user is allowed access to.
     */
    public List<Arena> getSecuredArenas(UserPrincipal userPrincipal) {

        List<MenuItem> menuItems;
        try {
            menuItems = arenaManager.getActiveArenaPermits(userPrincipal.getUserId(), userPrincipal.getUserType());
        } catch (TalentStudioException e) {
            logger.error("unknown exception reason: " + e.getMessage(), e);
            return new ArrayList<Arena>();
        }
        List<Arena> secureArenas = new ArrayList<Arena>();

        for (MenuItem menuItem : menuItems) {

            if (menuItem.getUserType() != null && !menuItem.getUserType().equals(userPrincipal.getUserType())) continue;
            /* checking to see if the menu item is a report menu item, if so and the report has any groups we need to
            check that this users group is in the reports group before we can continue */
            if(menuItem.isReportMenuItem()) {
                Report report = menuItem.getReport();
                /* if the report has groups but this users group is not included do not add this menu item */
                if(report.isHasGroups() && !report.containsGroup(userPrincipal.getUserGroup())) continue;
            }

            if(menuItem.isReportingChartMenuItem()) {
                Preference preference = menuItem.getPreference();
                if(preference.isHasGroups() && !preference.containsGroup(userPrincipal.getUserGroup())) continue;
            }
            
            MenuSection menuSection = menuItem.getMenuSection();
            Arena arena = menuSection.getArena();
            if (arena == null) {
                logger.error(">>>>>>>>>>>>>>>>>>>>> " +
                        "arena for menuSection: " + menuSection.getLabel() + " and menu item: " + menuItem.getLabel() + " with the menuItem of "
                        + menuItem.getId() + " is null! The user logging in: " + userPrincipal.getUsername());
                continue;
            }
            if (secureArenas.contains(arena)) {
                arena = secureArenas.get(secureArenas.indexOf(arena));
                arena.addMenuItem(menuItem);

            } else {
                menuSection = menuSection.shallowCopy();
                menuSection.addMenuItem(menuItem);
                arena = arena.shallowCopy();
                arena.addMenuSection(menuSection);
                secureArenas.add(arena);
            }
        }
        return sortArenas(secureArenas);
    }

    /**
     * Post sorting to order arenas and sections by sortOrder.
     *
     * @param secureArenas arenas that have been filtered according to the logged in users credentials.
     * @return List sorted list of secure menus
     */
    private List<Arena> sortArenas(List<Arena> secureArenas) {

        Collections.sort(secureArenas);

        for (Arena arena : secureArenas) {
            List<MenuSection> menuSections = new ArrayList<MenuSection>(arena.getMenuSections());
            Collections.sort(menuSections);
            arena.setMenuSections(menuSections);
            sortSections(menuSections);
        }

        return secureArenas;
    }

    private void sortSections(List<MenuSection> menuSections) {

        for (MenuSection menuSection : menuSections) {
            List<MenuItem> menuItems = new ArrayList<MenuItem>(menuSection.getMenuItems());
            Collections.sort(menuItems);
            menuSection.setMenuItems(menuItems);
        }
    }

    private void loadArenas() throws TalentStudioException {

        final Collection<Arena> arenas = arenaManager.getSortedArenas();

        activeArenas.clear();
        inactiveArenas.clear();

        for (Arena arena : arenas) {            
            if (arena.isActive()) {
                activeArenas.add(arena);
            } else {
                inactiveArenas.add(arena);
            }
        }
    }

    public void setArenaManager(IArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

    public Collection<Arena> getActiveArenas() {
        return activeArenas;
    }

    public Collection<Arena> getInactiveArenas() {
        return inactiveArenas;
    }

    private IArenaManager arenaManager;


    private Collection<Arena> activeArenas = new ArrayList<Arena>();
    private Collection<Arena> inactiveArenas = new ArrayList<Arena>();

    protected final Log logger = LogFactory.getLog(getClass());
}
