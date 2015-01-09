/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.questionnaires.support;

import com.zynap.exception.TalentStudioException;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class InvalidXmlDefinitionException extends TalentStudioException {

    public InvalidXmlDefinitionException(String message, Throwable e, int lineNumber, int columnNumber) {
        super(message, e);
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    public InvalidXmlDefinitionException(String message, String key, Object[] args) {
        super(message, key);
        this.args = args;
    }

    public Object[] getArgs() {
        return args;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    private int lineNumber = 0;
    private int columnNumber = 0;
    private Object[] args;
}
