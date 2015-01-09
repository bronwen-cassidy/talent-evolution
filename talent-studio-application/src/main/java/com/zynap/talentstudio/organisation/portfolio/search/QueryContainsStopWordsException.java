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
public class QueryContainsStopWordsException  extends ExternalSearchException {

    public QueryContainsStopWordsException() {
    }

    public QueryContainsStopWordsException(String message, String key) {
        super(message, key);
    }
}
