/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.performance;

import com.zynap.exception.TalentStudioException;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 14-May-2008 10:25:34
 */
public class TooManyManagersFoundException extends TalentStudioException {

    public TooManyManagersFoundException(Throwable e) {
        super(e);
    }

    public TooManyManagersFoundException(String message, Throwable e) {
        super(message, e);
    }
}
