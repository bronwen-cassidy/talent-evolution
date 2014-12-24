package com.zynap.talentstudio.web.arena;

import com.zynap.talentstudio.arenas.Arena;
import com.zynap.talentstudio.arenas.MenuItem;
import com.zynap.talentstudio.arenas.MenuSection;
import com.zynap.util.ArrayUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * User: amark
 * Date: 29-Nov-2005
 * Time: 09:54:59
 */
public final class MenuItemHelper {

    public static List<MenuItemWrapper> buildMenuItemWrappers(Collection<MenuItem> menuItems, Collection<MenuSection> menuSections, Collection<MenuSection> homePageMenuSections) {

        List<MenuItemWrapper> temp = new ArrayList<MenuItemWrapper>();

        if (menuSections != null) {
            for (Iterator iterator = menuSections.iterator(); iterator.hasNext();) {
                MenuSection menuSection = (MenuSection) iterator.next();

                // check if menu section is selected
                boolean selected = CollectionUtils.exists(menuItems, new MenuItemPredicate(menuSection));

                // check if home page is selected - if there is home page section then it isn't, otherwise check in list of report menu items
                MenuSection homePageMenuSection = (MenuSection) CollectionUtils.find(homePageMenuSections, new HomePageMenuSectionPredicate(menuSection));
                boolean homePage = homePageMenuSection != null && CollectionUtils.exists(menuItems, new MenuItemPredicate(homePageMenuSection));

                MenuItemWrapper newMenuItemWrapper = new MenuItemWrapper(menuSection.getArena().getLabel(), menuSection, homePageMenuSection, selected, homePage);
                temp.add(newMenuItemWrapper);
            }
        }

        return temp;
    }

    public static Set<MenuItem> getAssignedMenuItems(List<MenuItemWrapper> menuItemWrappers, String label, String runReportURL) {
        Set<MenuItem> newMenuItems = new LinkedHashSet<MenuItem>();

        for (MenuItemWrapper menuItemWrapper : menuItemWrappers) {
            if (menuItemWrapper.isSelected()) {
                MenuItem newMenuItem = new MenuItem(label, menuItemWrapper.getMenuSection(), runReportURL);
                newMenuItems.add(newMenuItem);
            }

            if (menuItemWrapper.isHomePage()) {
                MenuItem newMenuItem = new MenuItem(label, menuItemWrapper.getHomePageMenuSection(), runReportURL);
                newMenuItems.add(newMenuItem);
            }
        }

        return newMenuItems;
    }

    public static boolean hasSelectedItems(List menuItemWrappers) {

        for (Iterator iterator = menuItemWrappers.iterator(); iterator.hasNext();) {
            MenuItemWrapper menuItemWrapper = (MenuItemWrapper) iterator.next();
            if (menuItemWrapper.isSelected() || (menuItemWrapper.isSupportsHomePage() && menuItemWrapper.isHomePage())) {
                return true;
            }
        }

        return false;
    }

    public static void setSelected(List menuItemWrappers, String[] positions) {

        // disable all first
        for (Iterator iterator = menuItemWrappers.iterator(); iterator.hasNext();) {
            MenuItemWrapper menuItemWrapper = (MenuItemWrapper) iterator.next();
            menuItemWrapper.setSelected(false);
        }


        // Takes a list of positions in the collection, finds the appropriate entry and set favourite as true
        if (positions != null) {
            for (int i = 0; i < positions.length; i++) {
                int position = Integer.parseInt(positions[i]);
                if (position < menuItemWrappers.size()) {
                    MenuItemWrapper menuItemWrapper = (MenuItemWrapper) menuItemWrappers.get(position);
                    if (menuItemWrapper != null) menuItemWrapper.setSelected(true);
                }
            }
        }
    }

    public static void setHomePage(List menuItemWrappers, String[] positions) {

        // disable all first
        for (Iterator iterator = menuItemWrappers.iterator(); iterator.hasNext();) {
            MenuItemWrapper menuItemWrapper = (MenuItemWrapper) iterator.next();
            menuItemWrapper.setHomePage(false);
        }

        // Takes a list of positions in the collection, finds the appropriate entry and set favourite as true
        if (positions != null) {
            for (int i = 0; i < positions.length; i++) {
                int position = Integer.parseInt(positions[i]);
                if (position < menuItemWrappers.size()) {
                    MenuItemWrapper menuItemWrapper = (MenuItemWrapper) menuItemWrappers.get(position);
                    if (menuItemWrapper != null) menuItemWrapper.setHomePage(true);
                }
            }
        }
    }

    public static List getAssignedArenas(Collection<MenuItem> assignedMenuItems) {

        final List<Arena> arenas = new ArrayList<Arena>();

        if (assignedMenuItems != null) {
            for (Iterator iterator = assignedMenuItems.iterator(); iterator.hasNext();) {
                final MenuItem menuItem = (MenuItem) iterator.next();
                final Arena arena = menuItem.getArena();

                // avoid duplicates
                if (!arenas.contains(arena)) {
                    arenas.add(arena);
                }
            }

            // sort by sort order
            Collections.sort(arenas, new Comparator<Arena>() {
                public int compare(Arena o1, Arena o2) {
                    return new Integer(o1.getSortOrder()).compareTo(new Integer(o2.getSortOrder()));
                }
            });
        }

        return arenas;
    }

    public static void clearMenuItems(IMenuItemContainer menuItemContainer, String[] menuItems, String[] homePageMenuItems) {
        if (ArrayUtils.isEmpty(menuItems)) {
            menuItemContainer.setActiveMenuItems(null);
        }

        if (ArrayUtils.isEmpty(homePageMenuItems)) {
            menuItemContainer.setHomePageMenuItems(null);
        }
    }

    static class MenuItemPredicate implements Predicate {

        private MenuSection menuSection;

        public MenuItemPredicate(MenuSection menuSection) {
            this.menuSection = menuSection;
        }

        public boolean evaluate(Object object) {
            MenuItem menuItem = (MenuItem) object;

            return menuItem.getMenuSection().equals(this.menuSection);
        }
    }

    static class HomePageMenuSectionPredicate implements Predicate {

        private MenuSection menuSection;

        public HomePageMenuSectionPredicate(MenuSection menuSection) {
            this.menuSection = menuSection;
        }

        public boolean evaluate(Object object) {
            MenuSection section = (MenuSection) object;

            final String id = section.getPrimaryKey().getId();
            return id.equals(menuSection.getPrimaryKey().getArenaId());
        }
    }

    public static final String ACTIVE_MENU_ITEMS_PARAM = "activeMenuItems";
    public static final String HOME_PAGE_MENU_ITEMS_PARAM = "homePageMenuItems";
}
