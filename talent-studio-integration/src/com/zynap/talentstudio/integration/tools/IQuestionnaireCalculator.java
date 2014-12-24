/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.integration.tools;

import com.zynap.exception.TalentStudioException;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 14-Jan-2010 17:01:56
 */
public interface IQuestionnaireCalculator {

    void calculateQuestions(Long questionnaireWorkflowId) throws TalentStudioException;
}
