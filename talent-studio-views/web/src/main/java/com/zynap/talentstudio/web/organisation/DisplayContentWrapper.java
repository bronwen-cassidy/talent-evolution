/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation;

import com.zynap.talentstudio.arenas.Arena;
import com.zynap.talentstudio.display.DisplayConfigItem;
import com.zynap.talentstudio.analysis.reports.Report;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Class used to encapsulate the display config information, keeping it independant of the separate wrappers
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class DisplayContentWrapper implements Serializable {

    public DisplayContentWrapper() {
    }

    public void setViewDisplayConfigItems(List<DisplayConfigItem> displayConfigItems, String currentArena) {
        this.viewDisplayConfigItems = displayConfigItems;
        setActiveDisplay(currentArena);
    }

    /**
     * Extracts all the active view items form the view display configuration
     *
     * @return List of {@link DisplayConfigItem } objects
     */
    public List<DisplayConfigItem> getViewDisplayConfigItems() {
        return viewDisplayConfigItems;
    }

    public String getActiveDisplay() {
        return activeTab;
    }

    /**
     * Sets the active tab according the the currentArena parameter.
     *
     * @param currentArena the arena for which this is the default display for.
     */
    private void setActiveDisplay(final String currentArena) {
        
        DisplayConfigItem targetItem = (DisplayConfigItem) CollectionUtils.find(viewDisplayConfigItems, new Predicate() {
            public boolean evaluate(Object o) {
                DisplayConfigItem displayConfigItem = (DisplayConfigItem) o;
                Set arenas = displayConfigItem.getArenas();
                for (Iterator iterator = arenas.iterator(); iterator.hasNext();) {
                    Arena arena = (Arena) iterator.next();
                    if(arena.getArenaId().equals(currentArena)) {
                        return true;
                    }
                }
                return false;
            }
        });
        if(targetItem == null) {
            if (!viewDisplayConfigItems.isEmpty()) {
                targetItem = viewDisplayConfigItems.get(0);
            } else {
                targetItem = new DisplayConfigItem("def_label");                
            }
        }
        activeTab = targetItem.getKey();
    }

    public void setExecutiveSummaryReport(Report execSummaryReport) {
        this.executiveSummaryReport = execSummaryReport;
    }

    public Report getExecutiveSummaryReport() {
        return executiveSummaryReport;
    }

    public boolean isHasObjectivesView() {
        for (DisplayConfigItem configItem : viewDisplayConfigItems) {
            if(configItem.isObjectives()) return true;
        }
        return false;
    }

    public boolean isHasPersonalReportsView() {
        for (DisplayConfigItem configItem : viewDisplayConfigItems) {
            if(configItem.isPersonReports()) return true;
        }
        return false;
    }

    public boolean isHasDashboardView() {
        for (DisplayConfigItem configItem : viewDisplayConfigItems) {
            if(configItem.isDashboard()) return true;
        }
        return false;
    }

    public DisplayConfigItem getPortfolioView() {
        for (DisplayConfigItem viewDisplayConfigItem : viewDisplayConfigItems) {
            if(viewDisplayConfigItem.isPortfolio()) {
                return viewDisplayConfigItem;
            }
        }
        return null;
    }

    public DisplayConfigItem getDashboardView() {
        for (DisplayConfigItem viewDisplayConfigItem : viewDisplayConfigItems) {
            if(viewDisplayConfigItem.isDashboard()) {
                return viewDisplayConfigItem;
            }
        }
        return null;
    }

    public boolean isHasProgressView() {
        for (DisplayConfigItem configItem : viewDisplayConfigItems) {
            if(configItem.isProgressReports()) return true;
        }
        return false;
    }

    private String activeTab;
    private List<DisplayConfigItem> viewDisplayConfigItems;
    private Report executiveSummaryReport;
}
