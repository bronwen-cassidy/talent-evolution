package com.zynap.talentstudio.web.utils;

import com.zynap.talentstudio.common.exceptions.TalentStudioRuntimeException;

/**
 * User: amark
 * Date: 26-May-2005
 * Time: 13:46:34
 *
 * Exception thrown by {@link com.zynap.talentstudio.web.utils.RequestUtils} if a required parameter was not found.
 */
public class MissingRequestParameterException extends TalentStudioRuntimeException {

    /**
     * Constructor.
     * @param missingParamName The missing parameter name
     */
    public MissingRequestParameterException(String missingParamName) {
        super(missingParamName);
    }
}
