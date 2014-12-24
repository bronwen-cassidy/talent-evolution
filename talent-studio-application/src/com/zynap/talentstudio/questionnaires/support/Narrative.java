/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.questionnaires.support;

import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class Narrative extends BaseQuestion {

    private static final long serialVersionUID = 5611943974114065767L;

    public Narrative() {
    }

    public Narrative(String description) {
        this.description = description;
    }

    final List<DynamicAttribute> getDynamicAttributes(QuestionnaireDefinition questionnaireDefinition) {
        return new ArrayList<DynamicAttribute>();
    }

    public boolean isMultiQuestion() {
        return false;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;
}
