/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.common.exceptions;

import org.springframework.dao.DataAccessException;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 15-Sep-2006 13:30:08
 */
public class TalentStudioDataAccessException extends DataAccessException {

    public TalentStudioDataAccessException(String msg) {
        super(msg);
    }

    public TalentStudioDataAccessException(String msg, Throwable ex) {
        super(msg, ex);
    }

    public TalentStudioDataAccessException(String errorMessage, int errorCode) {
        super(errorMessage);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    private int errorCode;
}
