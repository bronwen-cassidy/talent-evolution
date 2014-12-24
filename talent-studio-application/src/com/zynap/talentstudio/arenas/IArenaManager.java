/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.arenas;

import com.zynap.exception.TalentStudioException;

import java.util.Collection;
import java.util.List;


/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IArenaManager {

    Collection<Arena> getArenas() throws TalentStudioException;

    Collection<Arena> getSortedArenas() throws TalentStudioException;

    void updateArena(Arena arena);

    void updateArenaConfigItems(Arena arena) throws TalentStudioException;

    Arena getArena(String arenaId) throws TalentStudioException;

    /**
     * Get all menu sections that reports can be published to.
     *
     * @return Collection of {@link com.zynap.talentstudio.arenas.MenuSection} objects
     * @throws com.zynap.exception.TalentStudioException
     */
    Collection getReportMenuSections() throws TalentStudioException;

    Collection<MenuSection> getMenuSections(String arenaId);

    MenuSection getMenuSection(String arenaId, String sectionId);

    List<MenuItem> getActiveArenaPermits(Long userId, String userType) throws TalentStudioException;

    public static final String ADMIN_MODULE = "ADMINMODULE";
    public static final String MYZYNAP_MODULE = "MYZYNAPMODULE";
    public static final String ORGANISATION_MODULE = "ORGANISATIONMODULE";
    public static final String TALENT_IDENTIFICATION_MODULE = "TALENTIDENTIFIERMODULE";
    public static final String SUCCESSION_MODULE = "SUCCESSIONMODULE";
    public static final String ANALYSIS_MODULE = "ANALYSISMODULE";
    public static final String PERFORMANCE_MANAGEMENT_MODULE = "PERFMANMODULE";
}
