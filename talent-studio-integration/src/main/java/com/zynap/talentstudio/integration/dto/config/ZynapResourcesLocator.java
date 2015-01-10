package com.zynap.talentstudio.integration.dto.config;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.IZynapService;
import com.zynap.talentstudio.integration.dto.processors.IPostProcessor;

import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 26-Oct-2005
 * Time: 10:13:32
 */
public class ZynapResourcesLocator {

    public Class getClassFromAlias(String alias) throws TalentStudioException {
        ZynapResourcesConfig config = getConfigFromAlias(alias);
        try {
            return Class.forName(config != null ? config.getClassName() : alias);
        } catch (ClassNotFoundException e) {
            throw new TalentStudioException("Unable to get class for alias", e);
        }
    }

    private ZynapResourcesConfig getConfigFromAlias(String alias) {
        for (Iterator iterator = configSet.iterator(); iterator.hasNext();) {
            ZynapResourcesConfig config = (ZynapResourcesConfig) iterator.next();
            if (config.getAlias().equals(alias)) {
                return config;
            }
        }
        return null;
    }

    public Object getObjectFromAlias(String alias) throws TalentStudioException {
        try {
            return getClassFromAlias(alias).newInstance();
        } catch (Exception e) {
            throw new TalentStudioException("Unable to get class for alias", e);
        }
    }

    public String getAliasFromClass(Class objectClass) {
        for (Iterator iterator = configSet.iterator(); iterator.hasNext();) {
            ZynapResourcesConfig config = (ZynapResourcesConfig) iterator.next();
            if (config.getClassName().equals(objectClass.getName())) {
                return config.getAlias();
            }
        }
        return objectClass.getName();
    }


    public IZynapService getService(String alias) {
        return getConfigFromAlias(alias).getService();
    }

    public IPostProcessor getPostProcessor(String alias) {
        ZynapResourcesConfig config = getConfigFromAlias(alias);
        return (config != null ? config.getPostProcessor() : null);
    }


    private Set configSet;

    public Set getConfigSet() {
        return configSet;
    }

    public void setConfigSet(Set configSet) {
        this.configSet = configSet;
    }

    public Collection getSupportedClasses()
    {
        List list = new ArrayList();
        for (Iterator iterator = configSet.iterator(); iterator.hasNext();) {
            ZynapResourcesConfig zynapResourcesConfig = (ZynapResourcesConfig) iterator.next();
            list.add(zynapResourcesConfig.getClassName());
        }
        return list;
    }


}
