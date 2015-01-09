/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * todo class is currently not in use, has links to materialised views for updating the permits, we are not sure this is the correct way to go though
 *
 * @author bcassidy
 * @version 0.1
 * @since 12-Mar-2008 11:58:31
 */
public class ExpiryTask {

    public void run() throws Exception {

        try {
            final long startMillis = System.currentTimeMillis();
            logger.debug("........................ Permits refreshLoggedInUsersDomainPermits expiry task " + this.toString() + " started at " + startMillis);
            //securityManager.refreshLoggedInUsersDomainPermits();
            final long endMillis = System.currentTimeMillis();
            logger.debug("........................ Permits refreshLoggedInUsersDomainPermits expiry task " + this.toString() + " finished at " + endMillis);
            logger.debug("........................ Permits refreshLoggedInUsersDomainPermits expiry task " + this.toString() + " time taken  " + (endMillis - startMillis));

        } catch (Exception e) {
            e.printStackTrace(System.out);
            logger.error("Failed to run " + getClass() + " because of: ", e);
            throw e;
        }
    }

    public void runAll() throws Exception {
        try {
            final long startMillis = System.currentTimeMillis();
            logger.debug("........................ Permits refreshAllUsersDomainPermits expiry task " + this.toString() + " started at " + startMillis);
            //securityManager.refreshAllUsersDomainPermits();
            final long endMillis = System.currentTimeMillis();
            logger.debug("........................ Permits refreshAllUsersDomainPermits expiry task " + this.toString() + " finished at " + endMillis);
            logger.debug("........................ Permits refreshAllUsersDomainPermits expiry task " + this.toString() + " time taken  " + (endMillis - startMillis));

        } catch (Exception e) {
            e.printStackTrace(System.out);
            logger.error("Failed to run " + getClass() + " because of: ", e);
            throw e;
        }
    }

    public void setSecurityManager(ISecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    private static final Log logger = LogFactory.getLog(ExpiryTask.class);
    private ISecurityManager securityManager;
}
