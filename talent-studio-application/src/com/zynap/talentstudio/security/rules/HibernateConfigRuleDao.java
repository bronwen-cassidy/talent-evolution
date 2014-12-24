/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.security.rules;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.HibernateCrudAdaptor;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class HibernateConfigRuleDao extends HibernateCrudAdaptor implements IConfigRuleDao {

    public Class getDomainObjectClass() {
        return Config.class;
    }

    public List findAll(String reportType, String artefactType, Long userId, boolean publicOnly) throws TalentStudioException {
        return getHibernateTemplate().find(" from Config config order by upper(config.label)");
    }
}
