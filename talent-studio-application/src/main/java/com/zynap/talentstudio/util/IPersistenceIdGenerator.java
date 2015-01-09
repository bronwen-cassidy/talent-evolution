/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.util;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IPersistenceIdGenerator {

    String generateStringId(String baseString, String sequence);

    String generateStringId(String baseString);
}

