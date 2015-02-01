/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.security.rules;

import com.zynap.talentstudio.security.rules.Config;

import java.util.Collection;
import java.util.Iterator;

/**
 * Class or Interface description.
 * 
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ConfigHelper {

    public static Config getConfig(Long id, Collection configs) {
        for (Iterator iterator = configs.iterator(); iterator.hasNext();) {
            Config config = (Config) iterator.next();
            if (id.equals(config.getId())) {
                return config;
            }
        }
        // not found return null;
        return null;
    }
}
