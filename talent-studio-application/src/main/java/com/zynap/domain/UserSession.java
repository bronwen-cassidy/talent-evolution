package com.zynap.domain;

import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.arenas.*;
import com.zynap.talentstudio.navigation.ZynapNavigator;
import com.zynap.talentstudio.security.homepages.HomePage;
import com.zynap.talentstudio.security.roles.Role;
import com.zynap.talentstudio.common.groups.Group;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.io.Serializable;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: aandersson
 * Date: 02-Feb-2004
 * Time: 16:55:14
 * To change this template use Options | File Templates.
 */
public class UserSession implements Serializable {

    
    public UserSession(UserPrincipal principal, IArenaMenuHandler menuHandler) {
        this(principal);
        this.menuHandler = menuHandler;
        loadMenus();

    }
    public UserSession(UserPrincipal userPrincipal) {
        this.userPrincipal = userPrincipal;
        
    }
    
    public UserPrincipal getUserPrincipal() {
        return userPrincipal;
    }

    public User getUser() {
        return getUserPrincipal().getUser();
    }

    public String getUserName() {
        return userPrincipal.getUsername();
    }

    public Long getId() {
        return userPrincipal.getUserId();
    }

    public Long getSubjectId() {
        return userPrincipal.getSubjectId();
    }

    public void reloadMenus() throws TalentStudioException {
        getMenuHandler().reload();
        loadMenus();
    }

    public Collection<Arena> getArenas() {
        return secureArenas;
    }

    public String getCurrentArenaId() {
        return currentArenaId;
    }

    public void setCurrentArenaId(String currentArenaId) {
        this.currentArenaId = currentArenaId;
    }

    public String getCurrentMenuSectionId() {
        return currentMenuSectionId;
    }

    public void setCurrentMenuSectionId(String currentMenuSectionId) {
        this.currentMenuSectionId = currentMenuSectionId;
    }

    public boolean hasAgreedToPolicies() {
        return agreedToPolicies;
    }

    public void setAgreedToPolicies(boolean agreedToPolicies) {
        this.agreedToPolicies = agreedToPolicies;
    }

    public HomePage getHomePage() {
        return userPrincipal.getHomePage(currentArenaId);
    }

    public Group getUserGroup() {
        return userPrincipal.getUserGroup();        
    }

    /**
     * Get menu sections for current arena.
     *
     * @return Collection of {@link com.zynap.talentstudio.arenas.MenuSection} objects, or an empty LinkedHashSet if the arena cannot be found.
     */
    public Collection<MenuSection> getMenuSections() {
        return getCurrentArenaMenuSections();
    }

    /**
     * Gets the menu items for the current menu section.
     *
     * @return Collection of {@link com.zynap.talentstudio.arenas.MenuItem} objects, or an empty LinkedHashSet if the menu section cannot be found.
     */
    public Collection<MenuItem> getMenuItems() {
        final MenuSection currentMenuSection = getCurrentMenuSection();
        return currentMenuSection != null ? currentMenuSection.getSortedMenuItems() : new LinkedHashSet<MenuItem>();
    }

    public IArenaMenuHandler getMenuHandler() {
        return menuHandler;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public ZynapNavigator getNavigator() {
        return navigator;
    }

    public void setNavigator(ZynapNavigator navigator) {
        this.navigator = navigator;
    }

    public Long getOrganisationUnitId() {
        return navigator != null ? navigator.getOrganisationUnitId() : new Long(0);
    }

    public Long getUserOrgUnitId() {
        return userPrincipal.getUserOrganisationId();
    }

    private void loadMenus() {
        secureArenas = menuHandler.getSecuredArenas(userPrincipal);
        if(secureArenas.isEmpty()) {
            try {
                menuHandler.reload();
                secureArenas = menuHandler.getSecuredArenas(userPrincipal);
            } catch (TalentStudioException e) {
                //
            }
        }
    }

    private Collection<MenuSection> getCurrentArenaMenuSections() {

        Collection<MenuSection> currentMenuSections = new LinkedHashSet<MenuSection>();

        final Arena currentArena = getCurrentArena();
        if (currentArena != null) {
            currentMenuSections = currentArena.getMenuSections();

            // if home arena filter the menu sections to remove the report menu sections that the user has no access to, or whose related arenas are inactive
            if (IArenaManager.MYZYNAP_MODULE.equals(currentArena.getArenaId())) {
                currentMenuSections = CollectionUtils.select(currentMenuSections, new HomePageReportMenuSectionPredicate(menuHandler.getInactiveArenas()));
            }
        }

        return currentMenuSections;
    }

    private Arena getCurrentArena() {
        return (Arena) CollectionUtils.find(secureArenas, new ArenaPredicate());
    }

    private MenuSection getCurrentMenuSection() {
        final Collection menuSections = getCurrentArenaMenuSections();
        return (MenuSection) CollectionUtils.find(menuSections, new MenuSectionPredicate());
    }

    public boolean isAdministrator() {
        return userPrincipal.isAdministrator();
    }

    public boolean isRoot() {
        return userPrincipal.isRoot();
    }

    public Long getUserSessionId() {
        return userPrincipal.getSessionLog().getId();
    }

    public Collection<Role> getUserRoles() {
        return userPrincipal.getUserRoles();
    }

    public boolean hasSuperUserPriviledges() {
        return userPrincipal.isSuperUser();
    }

    public void setPermitsLoaded(boolean permitsLoaded) {
        this.permitsLoaded = permitsLoaded;
    }

    public boolean isPermitsLoaded() {
        return permitsLoaded;
    }

    public boolean isMultiTenant() {
        return multiTenant;
    }

    public void setMultiTenant(boolean multiTenant) {
        this.multiTenant = multiTenant;
    }

    public Long getUserOrgUnitRootId() {
        return userPrincipal.getUserOrganisationRootId();
    }

    private UserPrincipal userPrincipal;
    private Locale locale;
    private String currentArenaId;
    private String currentMenuSectionId;
    private boolean agreedToPolicies = false;
    private ZynapNavigator navigator;
    private IArenaMenuHandler menuHandler;
    private Collection<Arena> secureArenas = new ArrayList<Arena>();

    private class HomePageReportMenuSectionPredicate implements Predicate {

        private Collection inactiveArenas;

        public HomePageReportMenuSectionPredicate(Collection inactiveArenas) {
            this.inactiveArenas = inactiveArenas;
        }

        /**
         * Returns true if menu section is in one of the inactive arenas or if user does not have access to associated arena.
         *
         * @param object
         * @return true or false
         */
        public boolean evaluate(Object object) {
            MenuSection menuSection = (MenuSection) object;

            // if home page report menu section check if associated arena is active and user has access to the arena
            if (menuSection.isHomeArenaReportMenuSection()) {
                final String sectionId = menuSection.getPrimaryKey().getId();

                // section id must correspond to the id of one of the arenas in the list of secured arenas
                for (Iterator iterator = secureArenas.iterator(); iterator.hasNext();) {
                    Arena secureArena = (Arena) iterator.next();
                    if (sectionId.equals(secureArena.getArenaId())) {
                        // now check arena is not in list of inactive
                        return !inactiveArenas.contains(secureArena);
                    }
                }

                // if not in the collection of secured arenas
                return false;
            }

            return true;
        }
    }

    private class MenuSectionPredicate implements Predicate {

        public boolean evaluate(Object o) {
            MenuSection menuSection = (MenuSection) o;
            return menuSection.getPrimaryKey().getId().equals(currentMenuSectionId);
        }
    }

    private class ArenaPredicate implements Predicate {

        public boolean evaluate(Object o) {
            Arena arena = (Arena) o;
            return arena.getArenaId().equals(currentArenaId);
        }
    }

    private boolean permitsLoaded;
    private boolean multiTenant;
}
