package com.zynap.talentstudio.common;

import sun.misc.MessageUtils;

/**
 *
 */
public interface QuerySpecification {

	String where();

	String from();

	String select();
	
	String orderBy();
}
