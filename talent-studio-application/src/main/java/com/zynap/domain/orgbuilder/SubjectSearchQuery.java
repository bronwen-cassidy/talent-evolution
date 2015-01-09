/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.domain.orgbuilder;

import com.zynap.domain.admin.UserSearchQuery;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class SubjectSearchQuery extends UserSearchQuery {

    public String getPositionTitle() {
        return getStringQueryParamValue(JOB_TITLE);
    }

    public void setPositionTitle(String positionTitle) {
        setStringQueryParam(JOB_TITLE, positionTitle);
    }

    public Long getOrgUnitId() {
        return getLongQueryParamValue(SUBJECT_ORG_ID );
    }

    public void setOrgUnitId(Long orgUnitId) {
        setLongQueryParam(SUBJECT_ORG_ID, orgUnitId);
    }  

    public void setQuestionnaireId(Long questionnaireId){
        setLongQueryParam(QUE_WORKFLOW_ID, questionnaireId);
    }


    public Long getQuestionnaireId() {
        return getLongQueryParamValue(QUE_WORKFLOW_ID);
    }

    String positionTitle;
}
