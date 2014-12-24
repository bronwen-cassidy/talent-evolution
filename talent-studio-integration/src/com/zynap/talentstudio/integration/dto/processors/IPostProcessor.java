package com.zynap.talentstudio.integration.dto.processors;

import com.zynap.domain.IDomainObject;
import com.zynap.domain.admin.User;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 26-Oct-2005
 * Time: 09:40:47
 * To change this template use File | Settings | File Templates.
 */
public interface IPostProcessor {

    void process(IDomainObject domainObject, User user);

}
