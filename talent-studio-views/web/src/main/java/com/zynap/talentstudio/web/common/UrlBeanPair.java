/*
 * $Header: ${}
 * $Revision: ${}
 * $Date: 05-Nov-2007
 *
 * Copyright (c) 1999-2006 Bronwen Cassidy.  All rights reserved.
 */
package com.zynap.talentstudio.web.common;

import com.zynap.talentstudio.mail.IMailNotification;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 05-Nov-2007 17:27:38
 */
public class UrlBeanPair implements Serializable {

    public UrlBeanPair(String url, IMailNotification ref) {
        this.url = url;
        this.ref = ref;
    }

    public String getUrl() {
        return url;
    }

    public IMailNotification getRef() {
        return ref;
    }

    private String url;
    private IMailNotification ref;
}
