/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.exception;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 31-Jan-2008 12:49:24
 */
public class PessimisticLockingFailureException extends TalentStudioException {

    public PessimisticLockingFailureException(String message, String key) {
        super(message, key);
    }

    public PessimisticLockingFailureException(String key) {
        super("", key);
    }
}
