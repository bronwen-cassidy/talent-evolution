/*
 * $Header: ${}
 * $Revision: ${}
 * $Date: 29-Mar-2007
 *
 * Copyright (c) 1999-2006 Bronwen Cassidy.  All rights reserved.
 */
package com.zynap.talentstudio.objectives;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

/**
 * <p>Scheduled task to check to see if the objectives that have been set are due for expiry</p>
 *
 * Expires any objectives due for completion.
 *
 * @author bcassidy
 * @version 0.1
 * @since 29-Mar-2007 13:18:47
 */
public class ExpiryTask {

    public void run() throws Exception {

        logger.info(getClass() + " started at: " + new Date());

        try {
            objectiveService.expireCurrentObjectives();
            logger.info(getClass() + " completed at: " + new Date());

        } catch (Exception e) {
            logger.error("Failed to run " + getClass() + " because of: ", e);
            throw e;
        }
    }

    public void setObjectiveService(IObjectiveService objectiveService) {
        this.objectiveService = objectiveService;
    }

    private IObjectiveService objectiveService;

    private static final Log logger = LogFactory.getLog(ExpiryTask.class);
}
