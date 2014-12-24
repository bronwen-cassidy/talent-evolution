/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.mail;

import com.zynap.domain.IDomainObject;
import com.zynap.domain.admin.User;

import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 29-Nov-2006 09:47:59
 */
public interface IMailNotification {

    void send(String url, User fromUser, IDomainObject domainObject, User... participants);
    void send(String url,User fromUser, String password);
}
