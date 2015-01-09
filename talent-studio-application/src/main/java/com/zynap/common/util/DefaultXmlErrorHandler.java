/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.common.util;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class DefaultXmlErrorHandler implements ErrorHandler {

    public void error(SAXParseException exception) throws SAXException {
        throw exception;
    }

    public void fatalError(SAXParseException exception) throws SAXException {
        throw  exception;
    }

    public void warning(SAXParseException exception) throws SAXException {
        throw exception;
    }
}
