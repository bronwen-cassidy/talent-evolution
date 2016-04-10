/*
 * Copyright (c) 2004 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.arenas;

import com.zynap.exception.TalentStudioException;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Interface to be implemented by any class that can read up
 * existing menus for a particular arenas/module for a particular user.
 *
 * @author Andreas Andersson
 * @since 02/03/2004
 */
public interface IArenaManagerDao {

    /**
     * Find arena.
     *
     * @param id the id of the object
     * @return Arena the instance created by the query executed
     * @throws TalentStudioException
     */
    Arena findById(Serializable id) throws TalentStudioException;

    /**
     * Finds all arenas.
     *
     * @return Collection of {@link Arena } instances
     * @throws TalentStudioException
     */
    Collection<Arena> getArenas() throws TalentStudioException;

    /**
     * Finds all arenas sorted by sort order.
     *
     * @return Collection of {@link Arena } instances
     * @throws TalentStudioException
     */
    Collection<Arena> getSortedArenas() throws TalentStudioException;

    /**
     * Find all active arenas.
     *
     * @return Collection of {@link Arena } instances
     * @throws TalentStudioException
     */
    Collection<Arena> getActiveArenas() throws TalentStudioException;

    /**
     * Update arena.
     * @param arena
     */
    void update(Arena arena);

    /**
     * Get menu sections for arena ordered by label.
     * @param arenaId
     * @return Collection of menu sections.
     */
    Collection<MenuSection> getMenuSections(String arenaId);

    /**
     * Get menu section.
     * @param arenaId
     * @param sectionId
     * @return MenuSection
     */
    MenuSection getMenuSection(String arenaId, String sectionId);

    /**
     * Update menu section.
     * @param menuSection
     */
    void update(MenuSection menuSection);

    /**
     * Get all menu sections that reports can be published to.
     *
     * @return Collection of {@link com.zynap.talentstudio.arenas.MenuSection} objects
     */
    public Collection<MenuSection> getReportMenuSections();

    /**
     * Get all home page menu sections that reports can be published to.
     *
     * @return Collection of {@link com.zynap.talentstudio.arenas.MenuSection} objects
     */
    Collection<MenuSection> getHomePageReportMenuSections();

    List<MenuItem> getMenuItems(Long userId, String userType) throws TalentStudioException;
}
