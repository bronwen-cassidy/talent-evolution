/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.questionnaires.support;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 23-Aug-2006 13:19:19
 */
public class InvalidQuestionException extends InvalidXmlDefinitionException {

    public InvalidQuestionException(String message, String key, Object[] args) {
        super(message, key, args);
    }
}
