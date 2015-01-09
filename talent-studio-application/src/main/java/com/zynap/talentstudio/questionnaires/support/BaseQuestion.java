/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.questionnaires.support;

import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;

import java.io.Serializable;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public abstract class BaseQuestion implements Serializable {

    private static final long serialVersionUID = -1112451052709384954L;

    abstract List<DynamicAttribute> getDynamicAttributes(QuestionnaireDefinition questionnaireDefinition) throws InvalidXmlDefinitionException;

    public abstract boolean isMultiQuestion();
}
