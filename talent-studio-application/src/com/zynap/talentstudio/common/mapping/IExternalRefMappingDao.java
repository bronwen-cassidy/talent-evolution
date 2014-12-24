package com.zynap.talentstudio.common.mapping;

import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 18-Oct-2005
 * Time: 11:59:38
 * To change this template use File | Settings | File Templates.
 */
public interface IExternalRefMappingDao extends IFinder, IModifiable {

    ExternalRefMapping getMappingByExternalId(String externalId, Long externalUserSystemId, String internalRefName);

    ExternalRefMapping getMappingByInternalId(String internalId, Long externalUserSystemId, String internalRefName);

    void delete(String internalRefClassName, String internalRefId);
}
