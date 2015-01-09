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
public interface IConfigRuleService {

    /**
     * Finds all the username and password rules.
     *
     * @return Collection containing {@link Config} objects.
     */
    Collection findAll() throws TalentStudioException;

    /**
     * Finds a specific config given the config id.
     *
     * @param configId
     * @return Config
     */
    Config findById(Long configId) throws TalentStudioException;

    /**
     * Updates the configuration with new values for it's rules.
     *
     * @param config
     * @throws TalentStudioException
     */
    void update(Config config) throws TalentStudioException;
}
