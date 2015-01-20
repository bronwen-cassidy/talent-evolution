/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.security.rules;

import com.zynap.talentstudio.security.rules.Config;

import java.io.Serializable;
import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ConfigRuleWrapper implements Serializable {

    public void setConfigs(Collection configs) {
        this.configs = configs;
    }

    public Collection getConfigs() {
        return configs;
    }

    public void setTargetConfig(Config config) {
        this.targetConfig = config;
    }

    public Config getTargetConfig() {
        return targetConfig;
    }    

    private Collection configs;
    private Config targetConfig;
}
