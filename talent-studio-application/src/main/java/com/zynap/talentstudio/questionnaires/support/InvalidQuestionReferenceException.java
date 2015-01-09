package com.zynap.talentstudio.questionnaires.support;

/**
 * User: amark
 * Date: 23-Nov-2006
 * Time: 09:37:05
 */
public class InvalidQuestionReferenceException extends InvalidQuestionException {

    public InvalidQuestionReferenceException(String message, String key, String label) {
        super(message, key, new Object[]{label});
    }

    public InvalidQuestionReferenceException(String message, String key, Object[] args) {
        super(message, key, args);
    }
}