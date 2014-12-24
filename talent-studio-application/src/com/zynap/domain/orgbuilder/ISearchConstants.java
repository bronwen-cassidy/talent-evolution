/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.domain.orgbuilder;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface ISearchConstants {

    static final String EMAIL = "coreDetail.contactEmail";
    static final String TELEPHONE = "coreDetail.contactTelephone";
    static final String PREF_NAME = "coreDetail.prefGivenName";
    static final String USER_NAME = "loginInfo.username";
    static final String FIRST_NAME = "coreDetail.firstName";
    static final String SECOND_NAME = "coreDetail.secondName";

    static final String LABEL = "label";
    static final String TITLE = "title";

    static final String CURRENT_POSITION_PREFIX = "subjectPrimaryAssociations.position.";
    static final String ORG_ID = "organisationUnit.id";

    static final String JOB_TITLE = CURRENT_POSITION_PREFIX + TITLE;
    static final String SUBJECT_ORG_ID = CURRENT_POSITION_PREFIX + ORG_ID;
    static final String QUE_WORKFLOW_ID="participant.primaryKey.workflowId";


    static final int DEFAULT_INT_VALUE = -1;

    static final String ACTIVE = "active";
    static final String INACTIVE = "inactive";
    static final String ALL = "all";

}
