/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;

import com.zynap.talentstudio.questionnaires.QuestionnaireDTO;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflowDTO;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 24-Jan-2008 11:59:39
 */
public class QuestionnaireWorkflowListWrapper implements Serializable {

    public QuestionnaireWorkflowListWrapper(List<QuestionnaireWorkflowDTO> questionnaires) {
        this.questionnaires = questionnaires;
    }

    public List<QuestionnaireWorkflowDTO> getQuestionnaires() {
        return questionnaires;
    }

    private List<QuestionnaireWorkflowDTO> questionnaires = new ArrayList<QuestionnaireWorkflowDTO>();
}
