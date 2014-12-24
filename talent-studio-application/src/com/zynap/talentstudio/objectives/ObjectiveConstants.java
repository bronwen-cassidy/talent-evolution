/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.objectives;

import com.zynap.talentstudio.organisation.Node;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 24-May-2006 09:44:16
 */
public interface ObjectiveConstants extends Serializable {

    final String OBJECTIVE_ID = "objective_id";
    final String OBJECTIVE_SET_ID = "objective_set_id";

    /**
     * Types of objective sets
     */
    final String CORPORATE_TYPE = "CORPORATE";
    final String ORG_UNIT_TYPE = "ORGUNIT";
    final String USER_TYPE = "USER";

    /* status of the objective set */
    final String STATUS_APPROVED = "APPROVED";
    final String STATUS_ARCHIVED = "ARCHIVED";
    final String STATUS_PUBLISHED = "PUBLISHED";
    final String STATUS_PENDING = "PENDING";
    final String STATUS_OPEN = "OPEN";
    final String STATUS_COMPLETE = "COMPLETE";

    final String ACTION_GROUP_MANAGER = "MANAGER";
    final String ACTION_GROUP_INDIVIDUAL = "INDIVIDUAL";
    final String ACTION_GROUP_UNASSIGNED = "UNASSIGNED";

    final String ACTION_REQUIRED_REVIEW = "REVIEW";
    final String ACTION_REQUIRED_APPROVE = "APPROVE";
    final String ACTION_REQUIRED_NOACTION = "NO_ACTION";

    final String NODE_TYPE = Node.OBJECTIVE_TYPE;
    final String RATING_LOOKUP_TYPE = "OBJ_RATING";
}
