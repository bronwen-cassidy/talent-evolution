package com.zynap.talentstudio.integration.dto.processors;

import com.zynap.domain.IDomainObject;
import com.zynap.domain.admin.User;

/**
 * User: amark
 * Date: 05-Apr-2006
 * Time: 14:51:45
 */
public class OrganisationUnitProcessor extends NodeProccesor implements IPostProcessor {

    public void process(IDomainObject domainObject, User user) {
        super.process(domainObject, user);
    }
}
