package com.zynap.talentstudio.common.hibernate.interceptor.strategy;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.mapping.IExternalRefMappingDao;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.domain.Auditable;

import java.io.Serializable;

/**
 * User: amark
 * Date: 18-Aug-2006
 * Time: 12:16:36
 * <p/>
 * Implementation of InterceptorStrategy for node classes.
 */
public class NodeInterceptorStrategy extends DefaultInterceptorStrategy {

    public void onDelete(Auditable entity, Serializable id, Object[] state, String[] propertyNames) throws TalentStudioException {

        Node objectEntity = (Node) entity;
        externalRefMappingDao.delete(objectEntity.getClass().getName(), objectEntity.getId().toString());
        doAudit(entity, id, DELETE, getTableName(entity));

    }

    public void setExternalRefMappingDao(IExternalRefMappingDao externalRefMappingDao) {
        this.externalRefMappingDao = externalRefMappingDao;
    }

    private IExternalRefMappingDao externalRefMappingDao;
}
