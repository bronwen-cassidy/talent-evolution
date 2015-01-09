/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.audit;

import com.zynap.common.persistence.ZynapPersistenceSupport;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 09-Jan-2009 11:33:54
 */
public class HibernateAuditDao extends ZynapPersistenceSupport implements IAuditDao {

    public Class getDomainObjectClass() {
        return Audit.class;
    }

    public void create(Audit audit) {
        getHibernateTemplate().save(audit);
    }
}
