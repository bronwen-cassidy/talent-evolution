/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.portfolio.search;

import com.zynap.exception.TalentStudioException;


/**
 * Class or Interface description.
 * 
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class IndexException extends TalentStudioException {

    public IndexException() {
    }

    public IndexException(String message, Throwable cause) {
        super(message, cause);
    }

    public IndexException(Throwable cause) {
        super(cause);
    }

    public IndexException(String message) {
        super(message);
    }
}
