package com.zynap.talentstudio.integration;

import com.zynap.talentstudio.AbstractHibernateTestCase;

import java.util.List;

/**
 * User: amark
 * Date: 08-Feb-2005
 * Time: 10:40:54
 */
public abstract class BaseIntegrationTest extends AbstractHibernateTestCase {

    protected List<String> getConfigLocations() {

        final List<String> configLocations = super.getConfigLocations();

        String webappConfig = "classpath:config/integrationContext.xml";
        configLocations.add(webappConfig);

        return configLocations;
    }
}
