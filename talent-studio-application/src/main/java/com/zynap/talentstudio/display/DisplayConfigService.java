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
public class DisplayConfigService implements IDisplayConfigService {

    public DisplayConfig findById(Long id) throws TalentStudioException {
        return (DisplayConfig) displayConfigDao.findById(id);
    }

    public DisplayConfigItem findConfigItemById(Long id) throws TalentStudioException {
        return displayConfigDao.findConfigItemById(id);
    }

    public void update(DisplayConfig displayConfig) throws TalentStudioException {
        this.displayConfigDao.update(displayConfig);
    }

    public void update(DisplayConfigItem displayConfigItem) throws TalentStudioException {
        this.displayConfigDao.updateItem(displayConfigItem);
    }

    public List find(String nodeType) throws TalentStudioException {
        return this.displayConfigDao.find(nodeType);
    }

    public DisplayConfig find(String nodeType, String configType) throws TalentStudioException {
        return this.displayConfigDao.find(nodeType, configType);
    }

    public Report findDisplayConfigReport(String nodeType, String executiveSummaryType) {
        return displayConfigDao.findDisplayConfigReport(nodeType, executiveSummaryType);
    }

    public List<DisplayConfigItem> findUserDisplayItems(String nodeType, String configType, Long userId) {
        return displayConfigDao.find(nodeType, configType, userId);
    }

    public List findByType(String configType) throws TalentStudioException {
        return this.displayConfigDao.findByType(configType);
    }

    public List<DisplayConfig> findAll() throws TalentStudioException {
        return displayConfigDao.findAll();
    }

    public void setDisplayConfigDao(IDisplayConfigDao displayConfigDao) {
        this.displayConfigDao = displayConfigDao;
    }

    private IDisplayConfigDao displayConfigDao;
}
