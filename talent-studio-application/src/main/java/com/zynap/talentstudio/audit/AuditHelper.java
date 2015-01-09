/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.audit;

import com.zynap.domain.UserSession;

import java.util.Date;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 12-Jan-2009 14:30:18
 */
public class AuditHelper {

    public static void create(Audit audit, IAuditDao auditDao, UserSession userSession) {
        audit.setModifiedById(userSession.getId());
        audit.setModifiedByUsername(userSession.getUserName());
        audit.setModifiedDate(new Date());
        auditDao.create(audit);
    }
}
