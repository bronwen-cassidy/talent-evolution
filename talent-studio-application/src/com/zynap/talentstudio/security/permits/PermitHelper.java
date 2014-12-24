package com.zynap.talentstudio.security.permits;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.arenas.Arena;
import com.zynap.talentstudio.arenas.MenuItem;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Iterator;

/**
 * User: amark
 * Date: 30-Nov-2005
 * Time: 09:30:39
 */
public final class PermitHelper {

    private PermitHelper() {
    }

    /**
     * For each menu item, check that the permit is set correctly.
     *  
     * @param menuItems
     * @param permitManagerDao
     * @param action
     * @param content
     * @throws com.zynap.exception.TalentStudioException
     */
    public static void checkMenuItemPermits(Collection menuItems, IPermitManagerDao permitManagerDao, String action, String content) throws TalentStudioException {

        if (menuItems != null && !menuItems.isEmpty()) {

            Collection permits = permitManagerDao.getPermits(action, content);
            for (Iterator iterator = menuItems.iterator(); iterator.hasNext();) {
                MenuItem menuItem = (MenuItem) iterator.next();
                if (menuItem.getPermit() == null) {
                    assignPermit(permits, menuItem);
                }
            }
        }
    }

    /**
     * Find the permit that corresponds to the arena the menuitem is being added to.
     *
     * @param permits
     * @param menuItem
     */
    private static void assignPermit(final Collection permits, final MenuItem menuItem) {

        class ArenaPredicate implements Predicate {

            private String arenaURL;

            public ArenaPredicate(Arena arena) {
                this.arenaURL = arena.getBaseURL();
            }

            public boolean evaluate(Object object) {
                Permit permit = (Permit) object;
                final String url = permit.getUrl();
                return StringUtils.hasText(arenaURL) && url.startsWith(arenaURL);
            }
        }

        final Arena arena = menuItem.getMenuSection().getArena();
        final Permit permit = (Permit) CollectionUtils.find(permits, new ArenaPredicate(arena));
        if (permit != null) menuItem.setPermit(permit);
    }
}
