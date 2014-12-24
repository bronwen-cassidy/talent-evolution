/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.portfolio.search;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.InitializingBean;

/**
 * Abstract class that knows how to build connections to Autonomy DRE.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public abstract class AbstractConnector implements InitializingBean {

    /**
     * Validate that connection details have been set and that version is correct.
     *
     * @throws IllegalStateException if port or host are not set.
     */
    public void afterPropertiesSet() throws Exception {
        checkConnectionDetails();
        //checkVersion();
    }

    /**
     * Check host and port properties have been supplied.
     */
    private void checkConnectionDetails() {

        if (port == -1 && (host == null || "".equals(host))) {

            final String message = "Host and port configuration required";
            logger.error(message);

            throw new IllegalStateException(message);
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public int getRetries() {
        return retries;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }    

    private int port = 8888;
    private int timeout = 5000;
    private String host = "localhost";
    private int retries = 3;

    protected final Log logger = LogFactory.getLog(getClass());
}
