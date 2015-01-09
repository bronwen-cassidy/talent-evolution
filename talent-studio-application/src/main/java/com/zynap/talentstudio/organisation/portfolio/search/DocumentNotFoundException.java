/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.portfolio.search;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class DocumentNotFoundException extends ExternalSearchException {

    public DocumentNotFoundException() {
    }

    public DocumentNotFoundException(String message, String messageKey) {
        super(message, messageKey);
    }
}
