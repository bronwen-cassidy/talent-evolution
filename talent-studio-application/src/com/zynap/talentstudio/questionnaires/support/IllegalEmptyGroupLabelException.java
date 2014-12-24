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
 * @since 23-Aug-2006 12:52:28
 */
public class IllegalEmptyGroupLabelException extends InvalidQuestionException {

    public IllegalEmptyGroupLabelException(String message, String key, Object[] args) {
        super(message, key, args);
    }
}
