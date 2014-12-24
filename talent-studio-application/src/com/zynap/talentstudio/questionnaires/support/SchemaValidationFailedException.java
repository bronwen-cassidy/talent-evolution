/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.questionnaires.support;

/**
 * Exception thrown when validation using a schema fails.
 *
 * @author bcassidy
 * @version 0.1
 * @since 21-Mar-2006 09:31:11
 */
public class SchemaValidationFailedException extends Exception {

    public SchemaValidationFailedException() {
    }

    public SchemaValidationFailedException(String message) {
        super(message);
    }

    public SchemaValidationFailedException(Throwable cause) {
        super(cause);
    }

    public SchemaValidationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
