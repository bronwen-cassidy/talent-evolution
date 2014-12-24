/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.display;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.reports.Report;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IDisplayConfigService {

    /**
     * Finds a displayConfig with the given id.
     *
     * @param id
     * @return DisplayConfig
     * @throws TalentStudioException if none found
     */
    DisplayConfig findById(Long id) throws TalentStudioException;

    /**
     * Find the display configurations for the given node type.
     *
     * @param nodeType
     * @return List of {@link DisplayConfig} objects
     */
    List find(String nodeType) throws TalentStudioException;

    /**
     * Finds a display config matching the given node type and the type of config.
     *
     * @param nodeType
     * @param configType
     * @return DisplayConfig
     * @throws TalentStudioException
     */
    DisplayConfig find(String nodeType, String configType) throws TalentStudioException;

    Report findDisplayConfigReport(String nodeType, String executiveSummaryType);

    /**
     * Finds all the artefact display configurations of the matching type.
     *
     * @param configType The type
     * @return List of {@link DisplayConfig} objects
     * @throws TalentStudioException
     */
    List findByType(String configType) throws TalentStudioException;

    /**
     * Finds all the artefact display configurations.
     *
     * @return List of {@link DisplayConfig} objects
     * @throws TalentStudioException
     */
    List<DisplayConfig> findAll() throws TalentStudioException;

    /**
     * Updates the displayConfig with any items modified.
     *
     * @param displayConfig
     * @throws TalentStudioException
     */
    void update(DisplayConfig displayConfig) throws TalentStudioException;

    /**
     * Updates an item.
     * 
     * @param displayConfigItem
     * @throws TalentStudioException
     */
    void update(DisplayConfigItem displayConfigItem) throws TalentStudioException;

    DisplayConfigItem findConfigItemById(Long id) throws TalentStudioException;

    List<DisplayConfigItem> findUserDisplayItems(String nodeType, String configType, Long userId);

}
