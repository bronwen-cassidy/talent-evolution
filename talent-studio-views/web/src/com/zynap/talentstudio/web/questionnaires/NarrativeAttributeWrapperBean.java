/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;

import com.zynap.talentstudio.questionnaires.QuestionAttribute;
import com.zynap.talentstudio.web.organisation.attributes.FormAttribute;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class NarrativeAttributeWrapperBean implements FormAttribute {

    public NarrativeAttributeWrapperBean() {
    }

    public NarrativeAttributeWrapperBean(QuestionAttribute narrative) {
        this.narrative = narrative;
    }

    public String getValue() {
        return null;
    }

    public String getLabel() {
        return narrative.getNarrative();
    }

    public void setLabel(String newLabel) {
        narrative.setNarrative(newLabel);
    }

    public String getId() {
        return null;
    }

    public boolean isEditable() {
        return false;
    }

    public boolean isLineItem() {
        return false;
    }

    public boolean isHidden() {
        return false;
    }

    public String getType() {
        return NARRATIVE_QUESTION_TYPE;
    }

    private QuestionAttribute narrative;

    public static final String NARRATIVE_QUESTION_TYPE = "NARRATIVE";
}
