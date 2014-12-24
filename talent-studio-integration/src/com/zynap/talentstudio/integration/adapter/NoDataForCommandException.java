/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.integration.adapter;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class NoDataForCommandException extends InvalidDataException {

    public NoDataForCommandException() {
        super("Cannot execute commands without element data");
    }

    public NoDataForCommandException(String message) {
        super(message);
    }

    public NoDataForCommandException(Throwable cause) {
        super(cause);
    }

    public NoDataForCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
