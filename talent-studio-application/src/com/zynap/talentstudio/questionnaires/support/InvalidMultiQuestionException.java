package com.zynap.talentstudio.questionnaires.support;

/**
 * User: amark
 * Date: 23-Nov-2006
 * Time: 09:37:05
 */
public class InvalidMultiQuestionException extends InvalidQuestionException {

    public InvalidMultiQuestionException(String message, String key, String multiQuestionLabel) {
        super(message, key, new Object[]{multiQuestionLabel});
    }
}
