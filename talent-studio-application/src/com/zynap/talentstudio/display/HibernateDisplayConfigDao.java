/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.display;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.common.HibernateCrudAdaptor;

import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class HibernateDisplayConfigDao extends HibernateCrudAdaptor implements IDisplayConfigDao {

    public Class getDomainObjectClass() {
        return DisplayConfig.class;
    }

    public DisplayConfigItem findConfigItemById(Long id) throws TalentStudioException {
        Object displayConfigItem;
        try {
            displayConfigItem = getHibernateTemplate().load(DisplayConfigItem.class, id);
        } catch (DataAccessException e) {
            throw new TalentStudioException("finding a displayConfigItem with id: " + id + " threw an exception with message: " + e.getMessage(), e);
        }
        return (DisplayConfigItem) displayConfigItem;
    }

    public List<DisplayConfig> find(String nodeType) throws TalentStudioException {
        List<DisplayConfig> displayConfigs = getHibernateTemplate().find("from DisplayConfig config where config.nodeType=? order by upper(config.label)", new Object[]{nodeType});
        if (displayConfigs == null || displayConfigs.isEmpty())
            throw new TalentStudioException("Could not find display config items for nodeType " + nodeType);
        return displayConfigs;
    }

    public List<DisplayConfig> findByType(String configType) throws TalentStudioException {
        List<DisplayConfig> displayConfigs = getHibernateTemplate().find("from DisplayConfig config where config.type=? order by upper(config.label)", new Object[]{configType});
        if (displayConfigs == null || displayConfigs.isEmpty())
            throw new TalentStudioException("Could not find display config items for type " + configType);
        return displayConfigs;
    }

    public DisplayConfig find(String nodeType, String configType) throws TalentStudioException {
        List displayConfigs = getHibernateTemplate().find("from DisplayConfig config where config.nodeType=? and config.type=? ", new Object[]{nodeType, configType});
        if (displayConfigs == null || displayConfigs.isEmpty())
            throw new TalentStudioException("Could not find display config items for nodeType " + nodeType + " and config type " + configType);
        return (DisplayConfig) displayConfigs.get(0);
    }

    public Report findDisplayConfigReport(String nodeType, String configType) {
        StringBuffer query = new StringBuffer("select itemReport.report from DisplayItemReport itemReport, DisplayConfigItem item, DisplayConfig config ");
        query.append("where config.nodeType= :nodeType and config.type = :type and item.displayConfig.id=config.id and itemReport.displayConfigItem.id=item.id ");
        final List<Report> results = getHibernateTemplate().findByNamedParam(query.toString(), new String[]{"nodeType", "type"}, new Object[]{nodeType, configType});
        return results.isEmpty() ? null : results.get(0);
    }

    public List<DisplayConfigItem> find(String nodeType, String configType, Long userId) {
        // query for the user roles
        StringBuffer query = new StringBuffer("select distinct from DisplayConfigItem item join fetch item.roles as roles left join fetch item.groups as groups, User user join fetch user.userRoles as userRoles ");
        query.append(" where user.id=:userId");
        query.append(" and (roles in userRoles or user.group in groups) and item.displayConfig.nodeType=:nodeType and item.displayConfig.type=:configType and item.active='T'");
        query.append(" order by item.sortOrder");
        List results = getHibernateTemplate().findByNamedParam(query.toString(), new String[]{"userId", "nodeType", "configType"}, new Object[]{userId, nodeType, configType});

        Set<DisplayConfigItem> unique = new LinkedHashSet<DisplayConfigItem>();
        for (int i = 0; i < results.size(); i++) {
            Object[] objects = (Object[]) results.get(i);
            unique.add((DisplayConfigItem) objects[0]);
        }
        return new ArrayList<DisplayConfigItem>(unique);
    }

    public List<DisplayConfig> findAll(String reportType, String artefactType, Long userId, boolean publicOnly) throws TalentStudioException {
        return getHibernateTemplate().find("from DisplayConfig config order by upper(config.label)");
    }

    public void updateItem(DisplayConfigItem displayConfigItem) throws TalentStudioException {
        List reportItems = displayConfigItem.getReportItems();
        for (int i = 0; i < reportItems.size(); i++) {
            DisplayItemReport itemReport = (DisplayItemReport) reportItems.get(i);
            Report report = itemReport.getReport();
            getHibernateTemplate().update(report);
        }
        getHibernateTemplate().update(displayConfigItem);
    }
}
