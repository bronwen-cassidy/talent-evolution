package com.zynap.talentstudio.organisation.portfolio.search;

import com.zynap.exception.TalentStudioException;

/**
 * User: amark
 * Date: 10-Jul-2006
 * Time: 14:45:15
 */
public class ExternalSearchException extends TalentStudioException {

    public ExternalSearchException() {
    }

    public ExternalSearchException(String message) {
        super(message);
    }

    public ExternalSearchException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExternalSearchException(String message, String key) {
        super(message, key);
    }
}
