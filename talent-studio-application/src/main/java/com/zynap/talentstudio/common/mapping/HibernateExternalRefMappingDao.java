package com.zynap.talentstudio.common.mapping;

import com.zynap.talentstudio.common.HibernateCrudAdaptor;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 18-Oct-2005
 * Time: 12:01:18
 * To change this template use File | Settings | File Templates.
 */
public class HibernateExternalRefMappingDao extends HibernateCrudAdaptor implements IExternalRefMappingDao  {

    public ExternalRefMapping getMappingByExternalId(String externalId, Long externalUserSystemId, String internalRefName) {
        List result = getHibernateTemplate().find("from ExternalRefMapping mapping where mapping.externalRefId = ? and mapping.user.id = ? and mapping.internalRef = ?", new Object[] {externalId, externalUserSystemId, internalRefName});
        return (ExternalRefMapping) (result.isEmpty() ? null : result.get(0));
    }

    public ExternalRefMapping getMappingByInternalId(String internalId, Long externalUserSystemId, String internalRefName) {
        List result = getHibernateTemplate().find("from ExternalRefMapping mapping where mapping.internalRefId = ? and mapping.user.id = ? and mapping.internalRef = ?", new Object[] {internalId, externalUserSystemId, internalRefName});
        return (ExternalRefMapping) (result.isEmpty() ? null : result.get(0));
    }

    public void delete(String internalRefClassName, String internalRefId) {
        getHibernateTemplate().delete("from ExternalRefMapping mapping where mapping.internalRefId = '" + internalRefId + "' and mapping.internalRef = '" + internalRefClassName + "'");
    }

    public Class getDomainObjectClass() {
        return ExternalRefMapping.class;
    }
}
