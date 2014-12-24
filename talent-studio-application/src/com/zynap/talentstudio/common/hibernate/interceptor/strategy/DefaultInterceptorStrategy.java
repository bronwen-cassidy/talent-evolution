/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.common.hibernate.interceptor.strategy;

import com.zynap.domain.Auditable;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.audit.Audit;
import com.zynap.talentstudio.audit.AuditHelper;
import com.zynap.talentstudio.audit.IAuditDao;
import com.zynap.talentstudio.audit.AuditObject;
import com.zynap.talentstudio.security.UserSessionFactory;

import java.io.Serializable;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 09-Jan-2009 11:47:45
 */
public class DefaultInterceptorStrategy implements InterceptorStrategy {

    public void onDelete(Auditable entity, Serializable id, Object[] state, String[] propertyNames) throws TalentStudioException {
        doAudit(entity, id, DELETE, getTableName(entity));    
    }

    public boolean onSave(Auditable entity, Serializable id) throws TalentStudioException {
        doAudit(entity, id, CREATE, getTableName(entity));
        return false;
    }

    public boolean onModify(Auditable entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames) throws TalentStudioException {
        doAudit(entity, id, UPDATE, getTableName(entity));
        return false;
    }

    protected void doAudit(Auditable entity, Serializable id, String action, String tableName) {
        doAudit(entity, id, action, tableName, entity.toString());        
    }

    protected void doAudit(Auditable entity, Serializable id, String action, String tableName, String description) {
        Audit audit = new Audit((Long) id, tableName, action, description);
        if (entity != null) {
            audit.setSerializedObject(new AuditObject(entity.createAuditable()));
        }
        AuditHelper.create(audit, auditDao, UserSessionFactory.getUserSession());
    }

    public String getTableName(Object entity) {
        return (String) entityTableMappings.get(entity.getClass().getName());
    }

    public void setAuditDao(IAuditDao auditDao) {
        this.auditDao = auditDao;
    }

    public void setEntityTableMappings(Map entityTableMappings) {
        this.entityTableMappings = entityTableMappings;
    }

    protected IAuditDao auditDao;
    private Map entityTableMappings;

    public static final String CREATE = "CREATED";
    public static final String DELETE = "DELETED";
    public static final String DELETE_CASCADE = "DELETED_CASCADE";
    public static final String UPDATE = "UPDATED";
}
