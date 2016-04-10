/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.security.rules;

import com.zynap.exception.TalentStudioException;

import java.util.Collection;


/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ConfigRuleService implements IConfigRuleService {

    public void setConfigRuleDao(IConfigRuleDao configRuleDao) {
        this.configRuleDao = configRuleDao;
    }

    public Collection findAll() throws TalentStudioException {
        return configRuleDao.findAll();
    }

    public Config findById(Long configId) throws TalentStudioException {
        return (Config) configRuleDao.findById(configId);
    }

    public void update(Config config) throws TalentStudioException {
        configRuleDao.update(config);
    }

    private IConfigRuleDao configRuleDao;
}
