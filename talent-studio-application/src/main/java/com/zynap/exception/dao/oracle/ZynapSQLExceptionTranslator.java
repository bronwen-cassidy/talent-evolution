/*
 * Copyright (c) 2004 Zynap Limited. All rights reserved.
 */
package com.zynap.exception.dao.oracle;

import com.zynap.talentstudio.common.exceptions.TalentStudioDataAccessException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;

import java.sql.SQLException;

/**
 * Extending Spring frameworks SQLExceptionTranslator to translate our own
 * Oracle exceptions.
 *
 * @author Andreas Andersson
 * @see org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator
 */
public class ZynapSQLExceptionTranslator extends SQLErrorCodeSQLExceptionTranslator {

    /**
     * This method is called from the super class and will translate any custom
     * Oracle exceptions.
     *
     * @param task task being attempted
     * @param sql SQL that caused the problem
     * @param sqlex offending SQLException
     * @return null if no custom translation was possible, otherwise a DataAccessException.
     *         This exception should include the sqlex parameter
     *         as a nested root cause. If null is returned then the default error mapping try to resolve
     *         the SQL error
     */
    protected DataAccessException customTranslate(String task, String sql, SQLException sqlex) {

        int errorCode = sqlex.getErrorCode();
        String errorMessage = sqlex.getMessage();
        log(task, sql, errorCode, errorMessage);

        return new TalentStudioDataAccessException(errorMessage, errorCode);
    }


    private void log(String task, String sql, int errorCode, String errorMessage) {

        final StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Translating SQLException with errorCode '").append(errorCode);
        stringBuffer.append("' and message [").append(errorMessage).append("]; SQL was [").append(sql).append("] for task [").append(task).append("]");
        String message = stringBuffer.toString();

        logger.warn(message);
    }
}