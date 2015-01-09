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
 * @since 17-Aug-2006 11:33:32
 */
public class UnsupportedQuestionTypeException extends InvalidQuestionException {

    public UnsupportedQuestionTypeException(String message, String key, String type) {
        super(message, key, new Object[]{type});
    }
}
