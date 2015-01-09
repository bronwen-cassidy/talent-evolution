package com.zynap.talentstudio.arenas;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.security.roles.IRoleManager;
import com.zynap.talentstudio.security.roles.Role;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;
import java.util.List;

/**
 * User: aandersson
 * Date: 05-Feb-2004
 * Time: 15:45:20
 */
public class ArenaManager implements IArenaManager {

    public Collection<Arena> getArenas() throws TalentStudioException {
        return arenaManagerDao.getArenas();
    }

    public Collection<Arena> getSortedArenas() throws TalentStudioException {
        return arenaManagerDao.getSortedArenas();
    }

    public void updateArenaConfigItems(Arena arena) throws TalentStudioException {
        arenaManagerDao.update(arena);
    }

    public void updateArena(Arena arena) {
        arenaManagerDao.update(arena);

        // get menu section in home arena that matches this arena
        MenuSection menuSection = arenaManagerDao.getMenuSection(MYZYNAP_MODULE, arena.getArenaId());
        if (menuSection != null) {
            // set menu section nameand update
            menuSection.setLabel(arena.getLabel());
            arenaManagerDao.update(menuSection);
        }

        // get associated role and update it's label and description
        try {
            Role role = roleManager.findArenaRole(arena.getArenaId());
            role.setLabel(arena.getLabel() + ARENA_POSTFIX);
            role.setDescription(ENABLE_ACCESS_PREFIX + arena.getLabel() + ARENA_POSTFIX);
            roleManager.update(role);
        } catch (TalentStudioException e) {
            logger.error(e);
        }
    }

    public Arena getArena(String arenaId) throws TalentStudioException {
        return arenaManagerDao.findByID(arenaId);
    }

    public Collection getReportMenuSections() throws TalentStudioException {
        return arenaManagerDao.getReportMenuSections();
    }

    public Collection<MenuSection> getMenuSections(String arenaId) {
        return arenaManagerDao.getMenuSections(arenaId);
    }

    public MenuSection getMenuSection(String arenaId, String sectionId) {
        return arenaManagerDao.getMenuSection(arenaId, sectionId);
    }

    public List<MenuItem> getActiveArenaPermits(Long userId, String userType) throws TalentStudioException {
        return arenaManagerDao.getMenuItems(userId, userType);
    }

    public void setArenaManagerDao(IArenaManagerDao arenaManagerDao) {
        this.arenaManagerDao = arenaManagerDao;
    }

    public void setRoleManager(IRoleManager roleManager) {
        this.roleManager = roleManager;
    }

    private IArenaManagerDao arenaManagerDao;
    private IRoleManager roleManager;
    private static final String ARENA_POSTFIX = " Arena";
    private static final String ENABLE_ACCESS_PREFIX = "Enable access to the ";
    protected final Log logger = LogFactory.getLog(getClass());
}



