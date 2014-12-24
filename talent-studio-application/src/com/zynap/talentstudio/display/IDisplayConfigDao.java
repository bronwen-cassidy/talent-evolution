/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.display;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IDisplayConfigDao extends IFinder, IModifiable {

    /**
     * Find the display configurations for the given node type.
     *
     * @param nodeType
     * @return List of {@link DisplayConfig} objects
     */
    List<DisplayConfig> find(String nodeType) throws TalentStudioException;

    /**
     * Finds all the artefact display configurations of the matching type.
     *
     * @param configType The type
     * @return List of {@link DisplayConfig} objects
     * @throws TalentStudioException
     */
    List<DisplayConfig> findByType(String configType) throws TalentStudioException;    

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
     * Updates a specific display config item
     *
     * @param displayConfigItem
     */
    void updateItem(DisplayConfigItem displayConfigItem) throws TalentStudioException;

    DisplayConfigItem findConfigItemById(Long id) throws TalentStudioException;

    /**
     * Find the display config items that the given user has access to
     *
     * @param nodeType the artefact/node type which is being viewed
     * @param configType the configuration do we need examples ( {@link DisplayConfig#VIEW_TYPE} )
     * @param userId the id of the user who has to have the roles to see the certain display views
     * @return a list of {@link com.zynap.talentstudio.display.DisplayConfigItem } objects
     */
    List<DisplayConfigItem> find(String nodeType, String configType, Long userId);
}
