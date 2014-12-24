/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.attributes.validators;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ErrorMessageHandler implements Serializable {

	public ErrorMessageHandler(String typeErrorKey) {
		this.errorKey = typeErrorKey;
	}

	public ErrorMessageHandler(String errorKey, String errorValue) {
		this.errorKey = errorKey;
		this.errorValues = new Object[] {errorValue};
	}

	public String getErrorKey() {
		return errorKey;
	}

	public Object[] getErrorValues() {
		return errorValues;
	}

	private String errorKey;
	private Object[] errorValues;
}
